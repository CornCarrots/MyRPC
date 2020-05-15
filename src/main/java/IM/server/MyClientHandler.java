package IM.server;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import IM.bean.Msg;
import IM.bean.MyData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @author linhao
 * @date 2020/5/13 22:58
 */
public class MyClientHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    /**
     * 管理所有客户端
     */
    private static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 用户-客户端
     */
    private static HashMap<Long, Channel> users = new HashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        Channel channel = channelHandlerContext.channel();
        String content = textWebSocketFrame.text();
        System.out.printf("接收消息：%s%n", content);
        // 群发push
//        String msg = "[服务器发送消息]";
//        TextWebSocketFrame serverFrame = new TextWebSocketFrame(msg);
//        group.writeAndFlush(serverFrame);

        // 单点发送消息
        MyData data = JSONUtil.toBean(content, MyData.class);
        Msg msg = data.getMsg();
        switch (data.getAction()){
            case REGISTER:
                register(msg, channel);
                break;
            case SEND:
                send(msg);
                break;
            case CHECK:
                check(data);
                break;
            case KEEPALIVE:
                keepAlive(channel);
                break;
            default:
                break;
        }
    }

    /**
     * 注册当前的用户和它的通道
     * @param msg
     * @param channel
     */
    private void register(Msg msg, Channel channel){
        long sendid = msg.getSendId();
        users.put(sendid, channel);
    }

    /**
     * 发送消息
     * @param msg
     */
    private void send(Msg msg){
        // TODO addMsg
        long receiveid = msg.getReceiveId();
        Channel channel = users.get(receiveid);
        // 用户离线
        if (channel == null || group.find(channel.id()) == null){
            // push
        }else {
            channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(msg)));
        }
    }

    /**
     * 查看离线消息
     * @param data
     */
    private void check(MyData data){
        // 接收的消息
        String extand = data.getExtand();
        ArrayList<Long> msgId = CollUtil.newArrayList(Arrays.stream(extand.split(",")).map(Long::parseLong).iterator());
        // TODO checkMsg
    }

    /**
     * 心跳包
     */
    private void keepAlive(Channel channel){
        System.out.printf("接收 %s 心跳包%n", channel.remoteAddress());
        // 拉取离线消息 fetchUnreadMsg
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel 注册");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel 注销");
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel 活动");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel 睡眠");
        super.channelInactive(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel 读取完成");
        super.channelReadComplete(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        System.out.println("用户事件触发");
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel 写入");
        super.channelWritabilityChanged(ctx);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("加入handler");
        group.add(ctx.channel());
        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("移除handler");
        group.remove(ctx.channel());
        super.handlerRemoved(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常");
        cause.printStackTrace();
        group.remove(ctx.channel());
    }
}
