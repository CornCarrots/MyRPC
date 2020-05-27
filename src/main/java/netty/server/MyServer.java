package netty.server;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.ReflectUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import netty.annotation.SocketModule;
import netty.annotation.SocketOperation;
import netty.util.InvokeUtil;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.*;

import static netty.util.InvokeUtil.initService;

/**
 * @author linhao
 * @date 2020/5/13 18:00
 */
public class MyServer {

    public static void main(String[] args) throws Exception {
        InvokeUtil.initService();
        initServer();
    }

    private static void initServer(){
        // 线程池
        // 主线程
        EventLoopGroup mainGroup = new NioEventLoopGroup();
        // 从线程
        EventLoopGroup subGroup = new NioEventLoopGroup();
        try {
            // 服务器 设置线程、通道、处理器
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(mainGroup, subGroup)
                    .option(ChannelOption.SO_BACKLOG, 2048)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new MyServerInitializer());

            System.out.println("start...");
            // 同步启动 监听
//            ChannelFuture channelFuture = bootstrap.bind(8088).sync();
            ChannelFuture channelFuture = bootstrap.bind(8088);
            // 同步关闭
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mainGroup.shutdownGracefully();
            subGroup.shutdownGracefully();
        }
    }
}
