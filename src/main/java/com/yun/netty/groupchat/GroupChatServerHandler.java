package com.yun.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {

    //定义一个channle 组  管理所有的channel
    //GlobalEventExecutor.INSTANCE  全局的事件执行器  是一个单例
    private static ChannelGroup channelGroup=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");

    //建立连接后 第一个被调用
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel=ctx.channel();
        //该方法会将channelGroup 中所有的channel遍历    并发送消息；
        //我们不需要自己遍历
        channelGroup.writeAndFlush("[客户端]"+channel.remoteAddress()+" 加入聊天 "+
                sdf.format(new Date())+"\n");
        //将该客户加入聊天的消息推送给其他在线的客户端
        channelGroup.add(channel);
    }

    //channel 表示处于活动状态  提示XXX上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+"上线了~~~");
    }

    //断开连接  将xxx客户离开信息推送给当前在线的客户
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[客户端]"+channel.remoteAddress()+"离开了\n");
        //不需要手动remove  方法内部已经执行remove了
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //获取到当前channel
        Channel channel = ctx.channel();
        //遍历channelGroup  根据不同的情况 回送不同的消息
        channelGroup.forEach(ch->{
            if(channel!=ch){
                ch.writeAndFlush("[客户]"+channel.remoteAddress()+"发送消息"+msg+"\n");
            }else{
                ch.writeAndFlush("[自己发送了消息]"+msg);
            }
        });
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常发生"+cause.getMessage());
        ctx.close(); //关闭连接
    }

}
