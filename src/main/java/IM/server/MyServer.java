package IM.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author linhao
 * @date 2020/5/13 18:00
 */
public class MyServer {
    public static void main(String[] args) throws Exception {
        // 线程池
        // 主线程
        EventLoopGroup mainGroup = new NioEventLoopGroup();
        // 从线程
        EventLoopGroup subGroup = new NioEventLoopGroup();
        try {
            // 服务器 设置线程、通道、处理器
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(mainGroup, subGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new MyServerInitializer());

            // 同步启动 监听
            ChannelFuture channelFuture = bootstrap.bind(8088).sync();
            // 同步关闭
            channelFuture.channel().closeFuture().sync();
        } finally {
            mainGroup.shutdownGracefully();
            subGroup.shutdownGracefully();
        }
    }
}
