package service.impl;

import annotation.SocketModuleImpl;
import annotation.SocketOperation;
import service.HelloService;

/**
 * @author linhao
 * @date 2020/5/25 16:47
 * @description:
 */
@SocketModuleImpl
public class HelloServiceImpl implements HelloService {

    @SocketOperation
    public String hello(){
        System.out.println("hello");
        return "hello";
    }

    @SocketOperation
    public int add(int a, int b){
        return a+b;
    }
}
