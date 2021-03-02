package netty.client;

import java.lang.reflect.Proxy;

/**
 * @Author: zerocoder
 * @Description: rpc代理对象
 * @Date: 2021/3/2 11:28
 */

public class RpcProxy {
    public static <T> T invoke(Class<T> clazz){
        Class<?> [] interfaces = clazz.isInterface() ? new Class[]{clazz} : clazz.getInterfaces();
        T result = (T) Proxy.newProxyInstance(clazz.getClassLoader(),interfaces,new ClientProxyHandler(clazz));
        return result;
    }
}
