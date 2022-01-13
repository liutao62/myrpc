package org.hut.registry.impl;

import com.alibaba.fastjson.JSONObject;
import org.hut.aop.RpcProxySupport;
import org.hut.context.MyRpcConstEnum;
import org.hut.registry.RegistryCenter;
import org.hut.registry.model.MwBean;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class RedisRegistryCenter implements RegistryCenter {

    private static final String MIDDLEWARE_KEY = "middleware_list";
    private static final String MIDDLEWARE_CHANE = "middleware_list_chang_event";
    private static int RPC_LOCAL_PORT = Integer.parseInt(System.getProperty(MyRpcConstEnum.RPC_PORT.getDefine()));
    private static String RPC_LOCAL_IP;

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(2);

    static {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress address = inetAddresses.nextElement();
                    if (!address.isLoopbackAddress()) {
                        if (address.isSiteLocalAddress()) {
                            inetAddress = address;
                        }
                    }
                }
            }
            RPC_LOCAL_IP = inetAddress.toString().substring(inetAddress.toString().indexOf("/") + 1);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private JedisPool jedisPool;
    private JedisPubSub jedisPubSub = new JedisPubSub() {
        @Override
        public void onMessage(String channel, String message) {
            System.out.println("redis.clients.jedis.JedisPubSub.onMessage");
        }

        @Override
        public void onSubscribe(String channel, int subscribedChannels) {
            System.out.println("redis.clients.jedis.JedisPubSub.onSubscribe");
        }

        @Override
        public void subscribe(String... channels) {
            System.out.println("redis.clients.jedis.JedisPubSub.subscribe");
        }

        @Override
        public void psubscribe(String... patterns) {
            System.out.println("redis.clients.jedis.JedisPubSub.psubscribe");
        }
    };

    public RedisRegistryCenter() {

    }

    @Override
    public void init() {
        jedisPool = new JedisPool(new JedisPoolConfig(), System.getProperty(MyRpcConstEnum.RPC_REDIS_REGISTER_HOST.getDefine()),
                Integer.parseInt(System.getProperty(MyRpcConstEnum.RPC_REDIS_REGISTER_PORT.getDefine())), 1000,
                System.getProperty(MyRpcConstEnum.RPC_REDIS_REGISTER_PASSWORD.getDefine()),
                Integer.parseInt(System.getProperty(MyRpcConstEnum.RPC_REDIS_REGISTER_DATABASE.getDefine())));

        jedisPool.getResource().del(MIDDLEWARE_KEY);

//        EXECUTOR_SERVICE.execute(() -> jedisPool.getResource().subscribe(jedisPubSub));
    }

    @Override
    public void start() {

    }

    @Override
    public void shutdown() {

    }

    @Override
    public boolean register(MwBean obj) {
//        obj.setIp(RPC_LOCAL_IP);
        obj.setIp("127.0.0.1");
        obj.setPort(RPC_LOCAL_PORT);
        jedisPool.getResource().sadd(MIDDLEWARE_KEY, JSONObject.toJSONString(obj));
        EXECUTOR_SERVICE.execute(() -> jedisPool.getResource().subscribe(new JedisPubSub() {
        }, MIDDLEWARE_CHANE));
        return true;
    }

    @Override
    public boolean unregister(MwBean obj) {
        jedisPool.getResource().srem(MIDDLEWARE_KEY, JSONObject.toJSONString(obj));
        EXECUTOR_SERVICE.execute(() -> jedisPool.getResource().subscribe(new JedisPubSub() {
        }, MIDDLEWARE_CHANE));
        return true;
    }

    @Override
    public List<MwBean> push() {
        Set<String> smembers = jedisPool.getResource().smembers(MIDDLEWARE_KEY);
        return smembers.stream().map(smember -> JSONObject.parseObject(smember, MwBean.class)).collect(Collectors.toList());
    }

    @Override
    public <T> T getRemoteService(String namespace, Class clz) {
        return RpcProxySupport.getRemoteService(namespace, clz);
    }

    @Override
    public void ping() {

    }


}
class RemoteServiceContainer {

    private static Map<String, Object> serviceMap = new HashMap<>();

    public static <T> T getBean(String serviceName) {
        return (T) serviceMap.get(serviceName);
    }

    public static void doCreateBean(String serviceName, Object instance) {
//        Object instance = null;
//        try {
//            // 目前这里是自己做的，后面应该接入 spring ，如果有事务 / 切面等，获取代理对象
//            instance = implClz.newInstance();
//        } catch (Exception e) {
//            System.out.println(e);
//        }

        serviceMap.put(serviceName, instance);
    }
}
