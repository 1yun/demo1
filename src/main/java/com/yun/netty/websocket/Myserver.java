package com.yun.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class Myserver {

    public static void main(String[] args)throws  Exception{

        //创建两个线程组
        EventLoopGroup bossGroup=new NioEventLoopGroup(1);
        EventLoopGroup workerGroup=new NioEventLoopGroup();// 默认8个NioEventLoop
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline=ch.pipeline();
                    //因为基于http协议 使用http的编码和解码器
                    pipeline.addLast(new HttpServerCodec());
                    //是以块方式写  添加ChunkedWriteHandler处理器
                    /**
                     * 说明
                     * 1 http数据在传输过程中分段 HttpObjectAggregator 就是可以 将多个段聚合
                     * 2 这就是为什么  当浏览器发送大量数据时 就会发出多次httpq请求
                     */
                    pipeline.addLast(new HttpObjectAggregator(8192));
                    /**
                     * 说明
                     * 1 对应websocket 它的数据是以 帧frame 形式传递
                     * 2 可以看到WebSocketFrame 下面有六个子类
                     * 3 浏览器请求时 ws://localhost:7000/hello 表示请求的url
                     * 4 WebsocketServerProtocolHandler 核心功能是将http协议升级为wc协议 保持连接
                     *5 是通过一个转态码101
                     */
                    pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));
                    //自定义handler 处理业务逻辑
                    pipeline.addLast(new MyTextWebSocketFrameHandler());
                }
            });
            //启动服务器
            ChannelFuture channelFuture = serverBootstrap.bind(7000).sync();
            channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();

        }

    }
}
