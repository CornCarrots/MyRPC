package handler;

import annotation.SocketModule;
import annotation.SocketModuleImpl;
import annotation.SocketOperation;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import coder.MyRequest;
import coder.MyResponse;
import coder.StateEnum;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Data;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import serializer.NettySerializer;
import server.Service;
import server.ServiceDiscover;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author linhao
 * @date 2020/5/16 16:29
 */

@Data
public class RegisterHandler extends SimpleChannelInboundHandler<MyRequest> {

    private static NettySerializer serializer;

    // 注册容器
    private static Map<String, Object> context;

    private static ServiceDiscover discover;

    private static Map<Short, String> moduleClassMap;
    private static Map<String, Short> classModuleMap;
    private static Map<Short, Map<Short, Method>> operationMethodMap;

    static  {
        discover = new ServiceDiscover();
        serializer = new NettySerializer();
        context = new ConcurrentHashMap<>();
        moduleClassMap = new ConcurrentHashMap<>();
        classModuleMap = new ConcurrentHashMap<>();
        operationMethodMap = new ConcurrentHashMap<>();
        scanModuleAndOperation();
        registerModule();
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
        for (Class<?> clazz : classes) {
            SocketModule annotation = clazz.getAnnotation(SocketModule.class);
            if (annotation == null){
                continue;
            }
//            SocketModule socketModule = clazz.getAnnotation(SocketModule.class);
//            short mid = socketModule.moduleId();
            moduleClassMap.put(mid, clazz.getName());
            classModuleMap.put(clazz.getName(), mid);

            Set<Method> methods = ReflectionUtils.getMethods(clazz, ReflectionUtils.withAnnotation(SocketOperation.class));
            short oid = 0;
            for (Method method : methods) {
                SocketOperation socketOperation = method.getAnnotation(SocketOperation.class);
                if (socketOperation == null){
                    continue;
                }
//                short oid = socketOperation.operationId();
                Map<Short, Method> shortMethodMap = operationMethodMap.get(mid);
                if (shortMethodMap == null){
                    shortMethodMap = new HashMap<>();
                }
                shortMethodMap.put(oid, method);
                operationMethodMap.put(mid, shortMethodMap);
                oid++;
            }
            mid++;
        }
    }

    /**
     * 加载对象
     */
    private static void registerModule(){
        Reflections reflections = new Reflections("");
        Set<Class<?>> implClasses = reflections.getTypesAnnotatedWith(SocketModuleImpl.class);
        Set<String> set = classModuleMap.keySet();
        implClasses.forEach(implClass -> {
            // 实现了rpc接口
            Optional<Class<?>> first = Arrays.stream(implClass.getInterfaces()).filter(interfaceClass -> set.contains(interfaceClass.getName())).findFirst();
            // 将rpc接口进行实例化，缓存
            first.ifPresent(interfaceClass -> {
                context.put(interfaceClass.getName(), ReflectUtil.newInstance(implClass));
                Service service = new Service();
                service.setName(interfaceClass.getName());
                // TODO 配置文件读取
                service.setIp("127.0.0.1");
                service.setPort(8081);
                try {
                    discover.register(service);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            });
        });
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MyRequest myRequest) throws Exception {
        // 解析请求
        MyResponse response = new MyResponse(myRequest);
        Object result;
        try {
            short mid = response.getModule();
            short oid = response.getOperation();
            byte[] reqData = myRequest.getData();
            if (moduleClassMap.containsKey(mid)){
                // 目标接口
                String className = moduleClassMap.get(mid);
                // 实现类对象
                Object implObj = context.get(className);
                // 目标方法
                Method method = operationMethodMap.get(mid).get(oid);
                // 参数
                List params = serializer.deserialize(reqData);
                // 执行
                if (CollUtil.isNotEmpty(params))
                {
                    result = ReflectUtil.invoke(implObj, method.getName(), params.toArray());
                }else {
                    result = ReflectUtil.invoke(implObj, method.getName());
                }
                // 处理响应
                byte[] respData = serializer.serialize(result);
                response.setData(respData);
                response.setState(StateEnum.SUCCESS);
            }else {
                response.setState(StateEnum.NOTFOUND);
            }
        } catch (Exception e) {
            response.setState(StateEnum.FAIL);
            e.printStackTrace();
        } finally {
            channelHandlerContext.channel().writeAndFlush(response);
            channelHandlerContext.close();
        }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
