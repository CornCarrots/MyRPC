package service;

import annotation.SocketModule;
import annotation.SocketOperation;

/**
 * @Author: zerocoder
 * @Description:
 * @Date: 2021/3/2 13:57
 */
@SocketModule
public interface HelloService {
    @SocketOperation
    String hello();
    @SocketOperation
    int add(int a, int b);
}
