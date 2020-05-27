package netty.util;

import cn.hutool.core.util.ReflectUtil;
import netty.annotation.SocketModule;
import netty.annotation.SocketOperation;
import netty.coder.MyRequest;
import netty.coder.MyResponse;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import serializer.NettySerializer;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author linhao
 * @date 2020/5/25 17:44
 * @description:
 */
public class InvokeUtil {

    private static Map<Short, Map<Short, Method>> operations = new HashMap<>(16);
    private static Map<Short, Class> modules = new HashMap<>(16);
    private static NettySerializer serializer = new NettySerializer();

    public static void initService(){
        Reflections reflections = new Reflections("netty.handler.*"); //比如可以获取有Pay注解的class
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(SocketModule.class);
        for (Class<?> clazz : classes) {
            SocketModule socketModule = clazz.getAnnotation(SocketModule.class);
            short mid = socketModule.moduleId();
            modules.put(mid, clazz);
            Set<Method> methods = ReflectionUtils.getMethods(clazz, ReflectionUtils.withAnnotation(SocketOperation.class));
            for (Method method: methods) {
                SocketOperation socketOperation = method.getAnnotation(SocketOperation.class);
                short oid = socketOperation.operationId();
                Map<Short, Method> operation = operations.computeIfAbsent(mid, k -> new HashMap<>());
                operation.put(oid, method);
            }
        }
    }

    public static MyRequest createReq(short mid, short oid, Object...param) throws Exception {
        // 序列化实体
        byte[] data = serializer.serialize(param);
        MyRequest request = new MyRequest();
        request.setModule(mid);
        request.setOperation(oid);
        request.setData(data);
        return request;
    }

    public static Object invoke(short mid, short oid, byte[] data) throws Exception {
        Class clazz = modules.get(mid);
        Object obj = clazz.newInstance();
        Method method = operations.get(mid).get(oid);
        Object res = ReflectUtil.invoke(obj, method.getName());
        List param = serializer.deserialize(data);
        System.out.println(param);
        return res;
    }

    public static void createResp(MyResponse response, Object...param) throws Exception {
        byte[] data = serializer.serialize(param);
        response.setData(data);
    }
}
