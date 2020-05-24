package netty.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author linhao
 * @date 2020/5/22 17:55
 * @description: 自定义解码器
 */
public class RequestDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        // 读数据包
        if (byteBuf.readableBytes() < CoderConst.BASE_LENGTH) {
            return;
        }
        byteBuf.markReaderIndex();
        // 包头
        int packageLen = byteBuf.readInt();
        if (packageLen < CoderConst.PACKAGE_HEAD) {
            byteBuf.resetReaderIndex();
            return;
        }
        // 模块
        short module = byteBuf.readShort();
        // 操作
        short operation = byteBuf.readShort();
        // 数据
        int dataLen = byteBuf.readInt();
        if (byteBuf.readableBytes() < dataLen){
            byteBuf.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLen];
        byteBuf.readBytes(data);
        // 反序列化
        MyRequest myRequest = new MyRequest();
        myRequest.setModule(module);
        myRequest.setOperation(operation);
        myRequest.setData(data);

        list.add(myRequest);
    }
}
