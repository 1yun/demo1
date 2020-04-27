package com.yun.netty.dubbo.provider;

import com.yun.netty.dubbo.publicinterface.HelloService;

public class HelloServiceImpl implements HelloService {
    //当有消费方调用 该方法时  就返回一个结果
    @Override
    public String hello(String mes) {
        System.out.println("收到客户端消息" + mes);
        //根据mes 返回不同的结果
        if(mes!=null){
            return "我已经收到{"+mes+"}";
        }else
        return "我已经收到";
    }
}
