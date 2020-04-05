package com.yun.netty.Netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 说明
 *  我们自定义一个handler 需要继续netty 规定好的某个HandlerAdapter
 *  这时 我们自定义一个Handler  才能成为一个handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    /**
     *
     * @param ctx 上下文对象 含有管道 pipeline 通道channel
     * @param msg  就是客户端发送的数据  默认Object
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        System.out.println("server ctx = "+ctx);
//        //将msg 转成一个ByteBuf
//        // ByteBuf 是Netty 提供的  不是NIO byteBuffer
//        ByteBuf buf=(ByteBuf)msg;
//        System.out.println("客户端发送消息是："+buf.toString(CharsetUtil.UTF_8));
//        System.out.println("客户端地址"+ctx.channel().remoteAddress());
        //读取从客户端发送的studentPojo.Student
        studentPOJO.Student student=(studentPOJO.Student)msg;
        System.out.println(student.getId()+"--------"+student.getName());
    }


    //数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
       //数据写入缓存并刷新
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello 客户端  lalalal",CharsetUtil.UTF_8));

    }
    //异常捕获
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常发生"+cause.getMessage());
        ctx.close(); //关闭连接
    }

}
