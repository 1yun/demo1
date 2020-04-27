package com.yun.netty.dubbo.NettyServer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {
    private ChannelHandlerContext context;
    private String result; //返回的结果
    private String para;//客户端调用的方法  传入的参数

    //与服务器连接创建后  就会被调用  第一个被调用   （1）
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context = ctx;
    }

    //收到服务器的数据后  调用方法  synchronized                  (4)
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        result = msg.toString();
        System.out.println(result);
        notify();  //唤醒等待的线程
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    //被代理对象调用  发送数据给服务器 ->wait  ->等待被唤醒（channelRead） ->返回结果
    //                                      (3)->wait->(5)
    @Override
    public synchronized Object call() throws Exception {
        System.out.println("123456"+para);
        context.writeAndFlush(para);
        //进行wait
        wait();  //等待channelRead 方法 获取到服务器的结果后 唤醒（notify）
        System.out.println("123456"+result);
        return result;
    }

    //                                       (2)
    void setPara(String para) {
        this.para = para;
    }
}
