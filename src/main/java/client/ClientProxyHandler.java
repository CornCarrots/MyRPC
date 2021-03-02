package client;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.log.LogFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Data;
import annotation.SocketModule;
import annotation.SocketOperation;
import coder.MyRequest;
import handler.RpcClientHandler;
import initializer.RpcClientInitializer;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import serializer.NettySerializer;
import server.Service;
import server.ServiceDiscover;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author linhao
 * @date 2020/5/13 18:00
 */
@Data
public class ClientProxyHandler implements InvocationHandler {

    private Class<?> clazz;

    private String host = "127.0.0.1";

    private int port = 8081;

    private static Map<String, Short> classModuleMap;

    private static Map<Short, Map<String, Short>> methodOptionMap;

    private static NettySerializer serializer;

    private ServiceDiscover discover;

    static {
        serializer = new NettySerializer();
        classModuleMap = new ConcurrentHashMap<>();
        methodOptionMap = new ConcurrentHashMap<>();
        scanModuleAndOperation();
    }

    public ClientProxyHandler(Class<?> clazz) {
        this.clazz = clazz;
        Service service = initService();
        if (service != null) {
            this.host = service.getIp();
            this.port = service.getPort();
        }else {
            LogFactory.get().error("service:{} not found server", clazz.getName());
            throw new RuntimeException("服务不存在! " + clazz.getName());
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(Object.class.equals(method.getDeclaringClass())){
            // 如果当前就是一个实现类
            return method.invoke(proxy,args);
        }else{
            return rpcInvoke(method,args);
        }
    }

    private EventLoopGroup subGroup;

    private ChannelFuture channelFuture;

    private Bootstrap bootstrap;

    // 自定义请求协议
    private MyRequest request;

    private RpcClientHandler adapter;


    private Object rpcInvoke(Method method, Object[] args){
        try {
            initRequest(method, args);
            initClient();
            // 连接
            channelFuture = bootstrap.connect(host, port).sync();
            LogFactory.get().info("connect host:{}, port:{} success!", host, port);
            // 发送请求
            channelFuture.channel().writeAndFlush(request).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            subGroup.shutdownGracefully();
        }
        return adapter.getResponse();
    }

    private void initRequest(Method method, Object[] args) throws Exception {
        request = new MyRequest();
        Short mid = classModuleMap.get(clazz.getName());
        request.setModule(mid); // 类名称
        Map<String, Short> moduleMap = methodOptionMap.get(mid);
        Short oid = moduleMap.get(method.getName());
        request.setOperation(oid); // 方法名称
        if (ArrayUtil.isNotEmpty(args)) {
            request.setData(serializer.serialize(args)); // 入参列表
        }
    }

    private void initClient() {
        // 从线程
        subGroup = new NioEventLoopGroup();
        // 客户端 设置线程、通道、处理器
        bootstrap = new Bootstrap();
        bootstrap.group(subGroup)
                .channel(NioSocketChannel.class)
                .handler(new RpcClientInitializer(this));
    }

    /**
     * 扫描模块及其方法
     */
    private static void scanModuleAndOperation() {
        Reflections reflections = new Reflections("");
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(SocketModule.class);
        if (CollUtil.isEmpty(classes)){
            return;
        }
        short mid = 0;
        short oid = 0;
        for (Class<?> clazz : classes) {
            SocketModule annotation = clazz.getAnnotation(SocketModule.class);
            if (annotation == null){
                continue;
            }
            classModuleMap.put(clazz.getName(), mid);

            Set<Method> methods = ReflectionUtils.getMethods(clazz, ReflectionUtils.withAnnotation(SocketOperation.class));
            for (Method method : methods) {
                SocketOperation socketOperation = method.getAnnotation(SocketOperation.class);
                if (socketOperation == null){
                    continue;
                }
                Map<String, Short> stringShortMap = methodOptionMap.get(mid);
                if (stringShortMap == null){
                    stringShortMap = new HashMap<>();
                }
                stringShortMap.put(method.getName(), oid);
                methodOptionMap.put(mid, stringShortMap);
                oid++;
            }
            mid++;
        }
    }

    private Service initService(){
        String serviceName = clazz.getName();
        List<Service> servers = discover.getServer(serviceName);
        if (CollUtil.isEmpty(servers)){
            return null;
        }
        // 随机选择
        int randam = RandomUtil.randomInt(servers.size());
        return servers.get(randam);
    }
}