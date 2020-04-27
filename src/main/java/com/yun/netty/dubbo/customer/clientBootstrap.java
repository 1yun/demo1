package com.yun.netty.dubbo.customer;

import com.yun.netty.dubbo.NettyServer.NettyClient;
import com.yun.netty.dubbo.publicinterface.HelloService;

public class clientBootstrap {

    //定义协议头
    public static final String providerName = "HelloService#hello#";

    public static void main(String[] args) {
        NettyClient customer = new NettyClient();
        //创建代理对象
        HelloService helloService = (HelloService) customer.getBean(HelloService.class, providerName);
        String result = helloService.hello("你好 dubbo");
        System.out.println("调用的结果" + result);
    }

}
