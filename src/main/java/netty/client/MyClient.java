package netty.client;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ArrayUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import netty.bean.module.testModule.request.OperationOneRequest;
import netty.coder.MyRequest;
import netty.util.InvokeUtil;
import serializer.NettySerializer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * @author linhao
 * @date 2020/5/13 18:00
 */
public class MyClient {
    private static EventLoopGroup subGroup;
    private static ChannelFuture channelFuture;

    public static void initClient() {
        // 线程池
        // 从线程
        subGroup = new NioEventLoopGroup();
        // 客户端 设置线程、通道、处理器
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(subGroup)
                .channel(NioSocketChannel.class)
                .handler(new MyClientInitializer());

        // 连接
        channelFuture = bootstrap.connect("127.0.0.1", 8088);
    }

    public static void main(String[] args) throws Exception {
        try {
            InvokeUtil.initService();
            initClient();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("connect...");
            while (true) {
//                String msg = bufferedReader.readLine();
//                channelFuture.channel().writeAndFlush(msg);

                // 自定义请求实体
                int id = Integer.parseInt(bufferedReader.readLine());
                String name = bufferedReader.readLine();
                OperationOneRequest operationOneRequest = new OperationOneRequest();
                operationOneRequest.setId(id);
                operationOneRequest.setName(name);

                // 自定义请求协议
                MyRequest request = InvokeUtil.createReq((short) 1, (short) 1, operationOneRequest);
                // 发送请求
                channelFuture.channel().writeAndFlush(request);
            }
        } finally {
            subGroup.shutdownGracefully();
        }
    }
}