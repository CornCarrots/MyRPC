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
public class ResponseDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {

        int readableBytes = byteBuf.readableBytes();
        // 读数据包
        if (readableBytes < CoderConst.BASE_LENGTH) {
            return;
        }
        if (readableBytes > 2048){
            byteBuf.skipBytes(readableBytes);
        }
        byteBuf.markReaderIndex();
        // 包头
        int packageLen = byteBuf.readInt();
        if (packageLen < CoderConst.PACKAGE_HEAD) {
            byteBuf.resetReaderIndex();
            byteBuf.readByte();
            return;
        }
        // 模块
        short module = byteBuf.readShort();
        // 操作
        short operation = byteBuf.readShort();
        // 状态
        int code = byteBuf.readInt();
        StateEnum state = StateEnum.getByCode(code);
        // 数据
        int dataLen = byteBuf.readInt();
        if (byteBuf.readableBytes() < dataLen){
            byteBuf.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLen];
        byteBuf.readBytes(data);
        // 反序列化
        MyResponse myResponse = new MyResponse();
        myResponse.setModule(module);
        myResponse.setOperation(operation);
        myResponse.setState(state);
        myResponse.setData(data);
        list.add(myResponse);
    }
}
