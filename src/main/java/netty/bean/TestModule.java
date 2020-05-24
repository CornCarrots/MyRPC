package netty.bean;

import netty.annotation.SocketModule;
import netty.annotation.SocketOperation;

/**
 * @author linhao
 * @date 2020/5/24 15:50
 * @description:
 */
@SocketModule(moduleId = 1)
public interface TestModule {

    @SocketOperation(operationId = 1)
    String testOperation1(int id);


    @SocketOperation(operationId = 2)
    String testOperation2(int id);

}
