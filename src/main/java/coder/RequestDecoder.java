package coder;

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
        int readableBytes = byteBuf.readableBytes();
        // 数据包过小...
        if (readableBytes < CoderConst.BASE_LENGTH) {
            return;
        }
        // 数据包过大...防止socket攻击
        if (readableBytes > CoderConst.MAX_LENGTH) {
            byteBuf.skipBytes(readableBytes);
            return;
        }

        // 数据包长度符合
        while (true) {
            byteBuf.markReaderIndex();
            // 包头
            int packageLen = byteBuf.readInt();
            if (packageLen == CoderConst.PACKAGE_HEAD) {
                break;
            }
            // 从下一位开始读，避免跳过包头
            byteBuf.resetReaderIndex();
            byteBuf.readByte();

            // 整个数据包虽然满足了基本长度，却不是完整的数据包
            if (byteBuf.readableBytes() < CoderConst.BASE_LENGTH) {
                return;
            }
        }
        //-----------------------------解析请求-----------------------------

        // 模块
        short module = byteBuf.readShort();
        // 操作
        short operation = byteBuf.readShort();
        // 数据
        int dataLen = byteBuf.readInt();
        if (readableBytes < dataLen) {
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
