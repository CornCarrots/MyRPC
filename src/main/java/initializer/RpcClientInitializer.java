package initializer;

import client.ClientProxyHandler;
import coder.ResponseDecoder;
import handler.RpcClientHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import coder.RequestEncoder;

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
