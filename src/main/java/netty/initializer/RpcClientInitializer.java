package netty.initializer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Data;
import netty.client.ClientProxyHandler;
import netty.coder.RequestEncoder;
import netty.coder.ResponseDecoder;
import netty.handler.RpcClientHandler;

/**
 * @author linhao
 * 调用端
 * @date 2020/5/13 22:51
 */
public class RpcClientInitializer extends ChannelInitializer<NioSocketChannel> {

    RpcClientHandler rpcClientHandler;

    public RpcClientInitializer(ClientProxyHandler handler) {
        rpcClientHandler = new RpcClientHandler();
        handler.setAdapter(rpcClientHandler);
    }

    @Override
    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
        // 管道
        ChannelPipeline pipeline = nioSocketChannel.pipeline();
        // 拦截器
        // 编码解码
        pipeline.addLast(new ResponseDecoder());
        pipeline.addLast(new RequestEncoder());
        // 处理
        pipeline.addLast(rpcClientHandler);
    }
}
