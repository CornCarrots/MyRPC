package netty.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.log.LogFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Data;
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
@Data
public class RpcClientHandler extends SimpleChannelInboundHandler<MyResponse> {

    private Object response;

    private final NettySerializer serializer = new NettySerializer();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MyResponse myResponse) throws Exception {
        try {
            // 处理响应
            if (myResponse.getState() == StateEnum.FAIL){
                LogFactory.get().info("服务端异常");
            }else {
                List temp = serializer.deserialize(myResponse.getData());
                if (CollUtil.isNotEmpty(temp)){
                    response = temp.get(0);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
