package netty.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author linhao
 * @date 2020/5/13 22:51
 */
public class MyServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        // 管道
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 拦截器
        // 编码解码
//        pipeline.addLast("HttpServerCodec", new HttpServerCodec());
        pipeline.addLast(new StringDecoder());
        pipeline.addLast(new StringEncoder());

        // 返回
//        pipeline.addLast("MyClientHandler", new MyClientHandler());
        pipeline.addLast(new StringHandler());
    }
}
