package multiple_client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author linhao
 * @date 2020/5/13 18:00
 */
public class MyMultipleClient {
    /**
     * 启动器
     */
    private Bootstrap bootstrap;
    /**
     * 会话通道
      */
    private List<Channel> channels;
    /**
     * 通道索引
     */
    private final AtomicInteger workerIndex = new AtomicInteger();

    /**
     * 启动
     */
    public void init(int count) throws IOException {
        bootstrap = new Bootstrap();
        channels = new ArrayList<>();

        // 线程池
        EventLoopGroup subGroup = new NioEventLoopGroup();
        // 客户端 设置线程、通道、处理器
        bootstrap.group(subGroup)
                .channel(NioSocketChannel.class);
//                .handler(new MyClientInitializer());
        // 多连接
        for (int i = 0; i < count; i++) {
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8088);
            channels.add(channelFuture.channel());
        }
    }

    /**
     *  添加任务
      */
    public Channel nextChannel(){
        // 从0开始轮询
        return getFirstActiveChannel(0);
    }

    /**
     * 在线程组里轮询可用的线程
     * @param target
     * @return
     */
    private Channel getFirstActiveChannel(int target){
        // 线程安全
        int index = workerIndex.getAndIncrement();
        int size = channels.size();
        Channel channel = channels.get(Math.abs(index % size));
        // 通道断开
        if (!channel.isActive()){
            reconnect(channel);
            if (target >= size){
                throw new RuntimeException("没有可用的工作线程");
            }
            return getFirstActiveChannel(target + 1);
        }
        return channel;
    }

    /**
     * 通道重连
     * @param channel
     */
    private void reconnect(Channel channel){
        // 同步
        synchronized (channel){
            int index = channels.indexOf(channel);
            if (index < 0){
                return;
            }
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8088);
            Channel newChannel = channelFuture.channel();
            channels.set(index, newChannel);
        }
    }

}
