package netty.server;

import cn.hutool.log.LogFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import netty.initializer.RegisterInitializer;

/**
 * @author linhao
 * 注册中心
 * @date 2021/03/02
 */
public class RegisterCenter {

    private static final int port = 8081;

    public static void initRegister(){
        // 线程池
        // 主线程
        EventLoopGroup mainGroup = new NioEventLoopGroup();
        // 从线程
        EventLoopGroup subGroup = new NioEventLoopGroup();
        try {
            // 服务器 设置线程、通道、处理器
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(mainGroup, subGroup)
                    // 主线程最大连接数
                    .option(ChannelOption.SO_BACKLOG, 2048)
                    // 子线程长连接 即时响应
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    // 管道执行 keys 轮询
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new RegisterInitializer());

            System.out.println("start register...");
            // 同步启动 监听
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
//            ChannelFuture channelFuture = bootstrap.bind(port);
            LogFactory.get().info("start register success! listen port:{}", port);
            // 回调
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mainGroup.shutdownGracefully();
            subGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        RegisterCenter.initRegister();
    }
}
