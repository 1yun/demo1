package com.yun.netty.Netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;

public class NettyServer {

    public static void main(String[] args)throws  Exception {




            //BossGroup 和WorkerGroup
            // 1 创建两个线程组  bossGroup 和workerGroup
            // bossGroup 只是处理连接请求  真正和客户端业务处理的  会交给和workerGroup
            // 3 都是无限循环
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //创建服务器端的启动对象  配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup) //设置两个线程组
                    .channel(NioServerSocketChannel.class)   //使用NioSocketChannel 作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG, 128)   //设置线程队列得到连接的个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true) //保持活动连接动态
                    .childHandler(new ChannelInitializer<SocketChannel>() {  //创建一个通道测试对象
                        //给pipeline  设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //可以使用一个集合管理 SocketChannel  推送消息时可以将业务加入到各个channel 对应的NioEventLoop的taskQueue 或者scheduleTaskQueue
                            ChannelPipeline pipeline = ch.pipeline();
                            //指定对哪种对象解码
                            pipeline.addLast("decoder",new ProtobufDecoder(studentPOJO.Student.getDefaultInstance()));

                            pipeline.addLast(new NettyServerHandler());


                        }
                    });//给我们的workerGroup 的EventLoop 对应管道设置处理器
            System.out.println("ready ....");
            //绑定一个端口  并且同步  生成了一个ChannelFuture 对象
            //启动服务器
            ChannelFuture cf = bootstrap.bind(6668).sync();
            //对关闭通道进行监听
            cf.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
