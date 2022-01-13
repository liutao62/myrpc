package org.hut.namespace.impl;

import com.alibaba.fastjson.JSONObject;
import org.hut.aop.RpcProxySupport;
import org.hut.namespace.INamespaceService;
import org.hut.namespace.model.MwBean;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.JedisShardInfo;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class NamespaceServiceImpl implements INamespaceService {

    private static final String MIDDLEWARE_KEY = "middleware_list";
    private static final String MIDDLEWARE_CHANE = "middleware_list_chang_event";

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(2);
    private static InetAddress INET_ADDRESS;

    static {
        try {
            INET_ADDRESS = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {

        }
    }

    Jedis jedis;

    public NamespaceServiceImpl() {
        init();
    }

    private void init() {
        JedisShardInfo info = new JedisShardInfo("redis://110.40.182.184:6379/15");
        info.setPassword("Zxq9802Redi11@11..");
        jedis = new Jedis(info);

        String string = jedis.get("liutao");
        if (string != null) {
            System.out.println("connect register center ");
        }
//        EXECUTOR_SERVICE.execute(() -> jedis.subscribe(new JedisPubSub() {
//        }, MIDDLEWARE_CHANE));
    }

    @Override
    public boolean register(MwBean obj) {
        System.out.println(INET_ADDRESS);
        String address = INET_ADDRESS.toString();
        obj.setIp(address.substring(address.indexOf("/") + 1));
        obj.setPort(Integer.parseInt(System.getProperty("myrpc.port")));
        jedis.sadd(MIDDLEWARE_KEY, JSONObject.toJSONString(obj));
        EXECUTOR_SERVICE.execute(() -> jedis.subscribe(new JedisPubSub() {
        }, MIDDLEWARE_CHANE));
        return true;
    }

    @Override
    public boolean unregister(MwBean obj) {
        jedis.srem(MIDDLEWARE_KEY, JSONObject.toJSONString(obj));
        EXECUTOR_SERVICE.execute(() -> jedis.subscribe(new JedisPubSub() {
        }, MIDDLEWARE_CHANE));
        return true;
    }

    @Override
    public List<MwBean> push() {
        Set<String> smembers = jedis.smembers(MIDDLEWARE_KEY);
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
