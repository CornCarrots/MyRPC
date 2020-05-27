package netty.handler;

import netty.annotation.SocketModule;
import netty.annotation.SocketOperation;
import netty.module.Modules;
import netty.module.module2.Module2Cmd;

/**
 * @author linhao
 * @date 2020/5/25 16:47
 * @description:
 */
@SocketModule(moduleId = Modules.module2)
public class Module2Handler {
    @SocketOperation(operationId = Module2Cmd.ope1)
    public void ope1(){
        System.out.println("m2o1");
    }
    @SocketOperation(operationId = Module2Cmd.ope2)
    public void ope2(){
        System.out.println("m2o2");
    }
}
