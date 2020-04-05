package com.yun.netty.Netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;


public class NettyClient {

    public static void main(String[] args)throws  Exception {
        //客户端需要一个事件循环组
        EventLoopGroup eventExecutors=new NioEventLoopGroup();
        try {
            //注意客户端 使用的不是serverBootstrap 而是bootstap
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(eventExecutors)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline=ch.pipeline();
                            //在pipeline中加入ProtoBufEncoder
                            pipeline.addLast("encoder",new ProtobufEncoder());
                            pipeline.addLast(new NettyClientHandler());

                        }
                    });
            System.out.println("客户端ok");
            ChannelFuture channelFuture=bootstrap.connect("127.0.0.1",6668).sync();

            //给关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        }finally {
            eventExecutors.shutdownGracefully();
        }
    }
}
