package netty.client;

import cn.hutool.core.io.IoUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author linhao
 * @date 2020/5/13 18:00
 */
public class MyClient {
    public static void main(String[] args) throws Exception {
        // 线程池
        // 从线程
        EventLoopGroup subGroup = new NioEventLoopGroup();
        try {
            // 客户端 设置线程、通道、处理器
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(subGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new MyClientInitializer());

            // 连接
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8088);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            while (true){
                System.out.println("请输入");
                String msg = bufferedReader.readLine();
                channelFuture.channel().writeAndFlush(msg);
            }
        } finally {
            subGroup.shutdownGracefully();
        }
    }
}
