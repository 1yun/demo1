package com.yun.netty.Netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    //当通道就绪就会触发该方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
       // System.out.println("client"+ctx);
        //ctx.writeAndFlush(Unpooled.copiedBuffer("helloserver: lalalal", CharsetUtil.UTF_8));
        studentPOJO.Student student = studentPOJO.Student.newBuilder().setId(4).setName("yun").build();
        ctx.writeAndFlush(student);
    }
    //当通道有读取事件时  会触发

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf=(ByteBuf) msg;
        System.out.println("服务器回复的消息"+buf.toString(CharsetUtil.UTF_8));
        System.out.println("服务器的地址"+ctx.channel().remoteAddress());
    }
    //异常捕获
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常发生"+cause.getMessage());
        ctx.close(); //关闭连接
    }
}
