package netty.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author linhao
 * @date 2020/5/22 17:38
 * @description: 自定义编码器
 */
public class RequestEncoder extends MessageToByteEncoder {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        // 序列化
        MyRequest myRequest = (MyRequest) o;
        // 数据包
        byteBuf.writeInt(CoderConst.PACKAGE_HEAD);
        byteBuf.writeShort(myRequest.getModule());
        byteBuf.writeShort(myRequest.getOperation());
        byteBuf.writeInt(myRequest.getDataLength());
        byteBuf.writeBytes(myRequest.getData());
    }
}
