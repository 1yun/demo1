package com.yun.netty.dubbo.provider;

import com.yun.netty.dubbo.NettyServer.NettyServer;

//ServerBootstrap 会启动一个服务提供者  就是NettyServer
public class ServerBootstrap {
    public static void main(String[] args) {
        NettyServer.startServer("127.0.0.1", 7000);
    }
}
