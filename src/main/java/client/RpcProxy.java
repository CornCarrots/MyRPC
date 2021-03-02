package client;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: zerocoder
 * @Description: rpc代理对象
 * @Date: 2021/3/2 11:28
 */

public class RpcProxy {

    private static Map<Class<?>, Object> objectPool = new ConcurrentHashMap<>();

    public static <T> T invoke(Class<T> clazz) {
        if (objectPool.containsKey(clazz)){
            return (T) objectPool.get(clazz);
        }else {
            Class<?> [] interfaces = clazz.isInterface() ? new Class[]{clazz} : clazz.getInterfaces();
            T result = (T) Proxy.newProxyInstance(clazz.getClassLoader(),interfaces,new ClientProxyHandler(clazz));
            objectPool.put(clazz, result);
            return result;
        }
    }
}
