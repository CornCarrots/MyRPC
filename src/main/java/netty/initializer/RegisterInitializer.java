package netty.initializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import netty.coder.RequestDecoder;
import netty.coder.ResponseEncoder;
import netty.handler.RegisterHandler;

/** 注册中心的初始化器
 * @author linhao
 * @date 2020/5/13 22:51
 */
public class RegisterInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        // 管道
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 拦截器
        // 编码解码
        pipeline.addLast(new RequestDecoder());
        pipeline.addLast(new ResponseEncoder());

        // 返回
        pipeline.addLast(new RegisterHandler());
    }
}
