package netty.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import netty.coder.RequestEncoder;
import netty.coder.ResponseDecoder;

/**
 * @author linhao
 * @date 2020/5/13 22:51
 */
public class MyClientInitializer extends ChannelInitializer<NioSocketChannel> {

    @Override
    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
        // 管道
        ChannelPipeline pipeline = nioSocketChannel.pipeline();
        // 拦截器
        // 编码解码
//        pipeline.addLast(new StringDecoder());
        pipeline.addLast(new ResponseDecoder());
//        pipeline.addLast(new StringEncoder());
        pipeline.addLast(new RequestEncoder());
//        pipeline.addLast(new StringHandler());
        pipeline.addLast(new ResponseHandler());
    }
}
