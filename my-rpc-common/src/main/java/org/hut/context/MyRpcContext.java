package org.hut.context;

import org.hut.annotation.CustomImpl;
import org.hut.annotation.RemoteCall;
import org.hut.container.RemoteServiceContainer;
import org.hut.endpoint.Endpoint;
import org.hut.registry.RegistryCenter;
import sun.jvm.hotspot.utilities.Assert;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class MyRpcContext {

    private static final MyRpcContext RPC_CONTEXT = new MyRpcContext();

    private static List<Class> classes;
    private static ClassLoader classLoader = MyRpcContext.class.getClassLoader();// 默认使用的类加载器

    Endpoint endpoint;

    RegistryCenter registryCenter;

    private MyRpcContext() {
        System.setProperty("myrpc.port", "8080");
    }

    public static MyRpcContext getRpcContext() {
        return RPC_CONTEXT;
    }

    public Endpoint getEndpoint() {
        return endpoint;
    }

    public void start() {
        LifeCycleContainer.startEvent();
    }

    public RegistryCenter getRegistryCenter() {
        return this.registryCenter;
    }

    /**
     * 获取端口，加载/初始化微服务实现类
     *
     * @param propertyPath
     * @return
     */
    public static MyRpcContext loadProperty(String propertyPath) {
        init(propertyPath);
        processScanPackage();
        LifeCycleContainer.initEvent();
        RPC_CONTEXT.start();
        LifeCycleContainer.startEvent();
        return RPC_CONTEXT;
    }

    private static void processScanPackage() {
        String scanPackage = System.getProperty(MyRpcConstEnum.RPC_SCANNER_PACKAGE.getDefine(), "");
        scanPackage = scanPackage.replace(".", "/");
        String[] split = scanPackage.trim().split(",");
        try {
            boolean isRemoteService, isLifeCycle, isCustomImpl;
            String serviceName = "";
            for (String s : split) {
                List<Class> classes = searchClasses(System.getProperty(MyRpcConstEnum.RPC_SCANNER_PACKAGE.getDefine(), ""), true);
                for (Class clz : classes) {
                    if (clz.isInterface()) {
                        continue;
                    }
                    isLifeCycle = false;
                    isRemoteService = false;
                    isCustomImpl = false;
                    // todo 接入 spring
                    Class<?>[] interfaces = clz.getInterfaces();
                    for (Class<?> anInterface : interfaces) {
                        if (anInterface.getAnnotation(RemoteCall.class) != null) {
                            isRemoteService = true;
                            serviceName = anInterface.getName();
                        }
                        if (anInterface.equals(MyRpcContextLifeCycle.class)) {
                            isLifeCycle = true;
                        }
                        if (anInterface.equals(RegistryCenter.class) && clz.getAnnotation(CustomImpl.class) != null) {
                            isCustomImpl = true;
                        }
                    }
                    Object instance = null;
                    if (isLifeCycle || isRemoteService || isCustomImpl) {
                        instance = clz.newInstance();
                        if (isLifeCycle) {
                            MyRpcContextLifeCycle lifeCycle = (MyRpcContextLifeCycle) instance;
                            LifeCycleContainer.addLifeCycleListener(serviceName, lifeCycle);
                        }
                        if (isRemoteService) {
                            RemoteServiceContainer.doCreateBean(serviceName, instance);
                        }
                        if (isCustomImpl) {

                        }
                    }


                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static void init(String propertyPath) {
        Assert.that(propertyPath != null, "MyRpcContext load property path is null");

        Properties properties = new Properties();
        try {
            properties.load(MyRpcContext.class.getClassLoader().getResourceAsStream(propertyPath));
        } catch (IOException e) {
            System.out.println(e);
        }
        for (String propertyName : properties.stringPropertyNames()) {
            System.setProperty(propertyName, properties.getProperty(propertyName, ""));
        }
        String path = MyRpcContext.class.getClassLoader().getResource("").getPath();
        System.setProperty(MyRpcConstEnum.RPC_PROJECT_PATH.getDefine(), path);
    }

    private static void processLifeCycleEvent(Object instance) {
        if (instance instanceof MyRpcContextLifeCycle) {
            MyRpcContextLifeCycle lifeCycle = (MyRpcContextLifeCycle) instance;
            LifeCycleContainer.addLifeCycleListener(lifeCycle);
        }
    }


    /**
     * 查找包内的class
     *
     * @param classPath       要查找的类路径
     * @param needSearchInJar 是否需要在jar里查找  false:只在本地查找  true:即在本地查找，也在jar里查找
     * @return
     */
    public static List<Class> searchClasses(String classPath, boolean needSearchInJar) {
        classes = new ArrayList<>();
        try {
            if (needSearchInJar) {
                Enumeration<URL> urls = classLoader.getResources(classPath.replace(".", "/"));
                while (urls.hasMoreElements()) {
                    URL url = urls.nextElement();
                    String protocol = url.getProtocol();
                    if ("file".equals(protocol)) {
                        // 本地自己可见的代码
                        findClassLocal(url.toURI(), classPath);
                    } else if ("jar".equals(protocol)) {
                        // 引用jar包的代码
                        findClassJar(url, classPath.replace(".", "/"));
                    }
                }
            } else {
                URI uri = classLoader.getResource(classPath.replace(".", "/")).toURI();
                findClassLocal(uri, classPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }

    /**
     * 本地查找
     */
    private static void findClassLocal(URI uri, String packName) throws Exception {
        File file = new File(uri);
        file.listFiles(new FileFilter() {
            public boolean accept(File chiFile) {
                if (chiFile.isDirectory()) {
                    String subPackName = packName + "." + chiFile.getName();
                    try {
                        URI subUrl = classLoader.getResource(subPackName.replace(".", "/")).toURI();
                        findClassLocal(subUrl, subPackName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (chiFile.getName().endsWith(".class")) {
                    Class<?> clazz = null;
                    try {
                        clazz = classLoader.loadClass(packName + "." + chiFile.getName().replace(".class", ""));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    classes.add(clazz);
                    return true;
                }
                return false;
            }
        });

    }

    /**
     * jar包查找
     *
     * @param url
     */
    private static void findClassJar(URL url, String pathName) throws IOException {
        JarFile jarFile = null;
        try {
            JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
            jarFile = jarURLConnection.getJarFile();
        } catch (IOException e) {
            throw new RuntimeException("未找到策略资源");
        }

        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            String jarEntryName = jarEntry.getName();

            if (jarEntryName.contains(pathName) && !jarEntryName.equals(pathName + "/")) {
                //递归遍历子目录
                if (jarEntry.isDirectory()) {
                    String clazzName = jarEntry.getName().replace("/", ".");
                    int endIndex = clazzName.lastIndexOf(".");
                    String prefix = null;
                    if (endIndex > 0) {
                        prefix = clazzName.substring(0, endIndex);
                    }
                    prefix = prefix.replace(".", "/");
                    Enumeration<URL> subUrls = classLoader.getResources(prefix);
                    while (subUrls.hasMoreElements()) {
                        URL subUrl = subUrls.nextElement();
                        if (subUrl.getPath().startsWith(url.getPath())) {//子目录以父目录作为开始，保证是同一个jar包内
                            findClassJar(subUrl, prefix);
                        }
                    }

                }
                if (jarEntry.getName().endsWith(".class")) {
                    Class<?> clazz = null;
                    try {
                        clazz = classLoader.loadClass(jarEntry.getName().replace("/", ".").replace(".class", ""));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    classes.add(clazz);
                }
            }

        }

    }
}
