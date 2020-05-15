package IM.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author linhao
 * @date 2020/5/13 22:51
 */
public class MyServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        // 管道
        ChannelPipeline pipeline = socketChannel.pipeline();
        // -------------------------拦截器----------------------
        //-------------------------netty------------------------
        // 编码解码
        pipeline.addLast(new HttpServerCodec());
        // 大数据流
        pipeline.addLast(new ChunkedWriteHandler());
        // 聚合数据
        pipeline.addLast(new HttpObjectAggregator(1024 * 64));
        //-------------------------心跳------------------------
        pipeline.addLast(new IdleStateHandler(8, 10, 12));
        pipeline.addLast(new HeartBeatHandler());
        //-------------------------IM------------------------
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        pipeline.addLast(new MyClientHandler());
    }
}
