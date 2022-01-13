package org.hut;

import org.hut.annotation.RemoteCall;
import org.hut.context.MyRpcContext;
import org.hut.namespace.INamespaceService;
import org.hut.namespace.impl.NamespaceServiceImpl;
import org.hut.namespace.model.MwBean;

import java.io.File;

/**
 * Hello world!
 */
public class ProviderApp {
    private static INamespaceService namespaceService = new NamespaceServiceImpl();

    public static void main(String[] args) {
        System.setProperty("myrpc.port", "8080");
        // 注册中心
        registerAllService();
        MyRpcContext rpcContext = MyRpcContext.getRpcContext();
        rpcContext.start();

    }

    public static void registerAllService() {
        String path = ProviderApp.class.getResource("").getPath();
        System.out.println(path);
        File file = new File(path);
        boolean directory = file.isDirectory();
        if (directory) {
            registerDirectory(file);
        } else {
//            App.class.getClassLoader().loadClass()
        }
    }

    public static void registerDirectory(File file) {
        File[] files = file.listFiles();
        try {
            if (files != null) {
                for (File file1 : files) {
                    if (file1.isDirectory()) {
                        registerDirectory(file1);
                    } else {
                        String className = file.getPath().substring(file.getPath().indexOf("org/hut")) + "."
                                + file1.getName().substring(0,file1.getName().indexOf('.'));
                        className = className.replace('/', '.');
                        Class<?> loadClass = ProviderApp.class.getClassLoader().loadClass(className);
                        Class<?>[] interfaces = loadClass.getInterfaces();
                        for (Class<?> anInterface : interfaces) {
                            RemoteCall annotation = anInterface.getAnnotation(RemoteCall.class);
                            if (annotation != null){
                                MwBean mwBean = new MwBean(anInterface.getName(), null);
                                namespaceService.register(mwBean);
                                org.hut.middleware.MiddleWareServiceContainer.createBean(mwBean.getNamespace(),loadClass.newInstance());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
