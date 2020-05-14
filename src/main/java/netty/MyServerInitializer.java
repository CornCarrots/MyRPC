package netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

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
        pipeline.addLast("HttpServerCodec", new HttpServerCodec());
        // 返回
        pipeline.addLast("MyClientHandler", new MyClientHandler());

    }
}
