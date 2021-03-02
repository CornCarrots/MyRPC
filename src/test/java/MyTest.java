import netty.client.RpcProxy;
import netty.server.RegisterCenter;
import org.junit.BeforeClass;
import org.junit.Test;
import service.HelloService;

/**
 * @Author: zerocoder
 * @Description:
 * @Date: 2021/3/2 10:47
 */

public class MyTest {

//    @BeforeClass
    public static void before(){
        RegisterCenter.initRegister();
    }

    @Test
    public void server(){
        RegisterCenter.initRegister();
    }

    @Test
    public void test(){
        HelloService helloService = RpcProxy.invoke(HelloService.class);
        System.out.println(helloService.hello());
        System.out.println(helloService.add(1,2));
    }
}
