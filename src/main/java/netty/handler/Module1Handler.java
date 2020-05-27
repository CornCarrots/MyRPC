package netty.handler;

import netty.annotation.SocketModule;
import netty.annotation.SocketOperation;
import netty.module.Modules;
import netty.module.module1.Module1Cmd;

/**
 * @author linhao
 * @date 2020/5/25 16:47
 * @description:
 */
@SocketModule(moduleId = Modules.module1)
public class Module1Handler {
    @SocketOperation(operationId = Module1Cmd.ope1)
    public void ope1(){
        System.out.println("m1o1");
    }
    @SocketOperation(operationId = Module1Cmd.ope2)
    public void ope2(){
        System.out.println("m1o2");
    }
}
