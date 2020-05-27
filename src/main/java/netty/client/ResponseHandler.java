package netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty.bean.module.testModule.response.OperationTwoResponse;
import netty.coder.MyResponse;
import netty.coder.StateEnum;
import netty.util.InvokeUtil;
import serializer.NettySerializer;

import java.util.List;

/**
 * @author linhao
 * @date 2020/5/24 10:46
 * @description:
 */
public class ResponseHandler extends SimpleChannelInboundHandler<MyResponse> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MyResponse myResponse) throws Exception {
        try {
            // 处理响应
            if (myResponse.getState() == StateEnum.FAIL){
                System.out.println("服务端异常");
                return;
            }else if (myResponse.getState() == StateEnum.SUCCESS){
                Object obj = InvokeUtil.invoke(myResponse.getModule(), myResponse.getOperation(), myResponse.getData());

//                if (myResponse.getModule() == 1){
//
//                    // 反序列化响应实体
//                    byte[] data = myResponse.getData();
//                    NettySerializer serializer = new NettySerializer();
//
//                    OperationTwoResponse operationTwoResponse = serializer.deserialize(data, OperationTwoResponse.class);
//                    System.out.println("id:" + operationTwoResponse.getId());
//                    System.out.println("money:" + operationTwoResponse.getMoney());
//                }else if (myResponse.getModule() == 2){
//
//                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
