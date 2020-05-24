package IM.server;

import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author linhao
 * @date 2020/5/15 16:27
 */
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent) evt;
            IdleState state = event.state();
            switch (state){
                case READER_IDLE:
                    System.out.println("读空闲...");
                    break;
                case WRITER_IDLE:
                    System.out.println("写空闲...");
                    break;
                case ALL_IDLE:
                    System.out.println("读写空闲...");
                    dead(ctx.channel());
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 客户端死亡
     * @param channel
     */
    private void dead(Channel channel){
        ChannelFuture channelFuture = channel.writeAndFlush("掉线了...");
        channelFuture.addListener((ChannelFutureListener) channelFuture1 -> channel.close());
    }
}
