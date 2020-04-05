package com.yun.netty;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    @Test
    public void test1(){
        /**HyperLogLog  解决统计问题（提供不精确的去重计数方案  标准误差是0.81%）
         *  用于统计页面的UV（精确度要求不高）
         *  此时为每一个页面设置一个set  当某个页面有几千外的UV （访问某个页面的用户量（同一个用户一天之内只能计数一次））
         *          此时无疑浪费空间
         */
        Jedis jedis=new Jedis("localhost");
        for(int i=0;i<100000;i++){
            jedis.pfadd("codehole","user"+i);
        }
        long total=jedis.pfcount("codehole");

        System.out.printf("%d %d\n", 100000, total);
        //100000 99715
        jedis.close();
    }
    @Test
    public void contextLoads() {
        Jedis jedis=new Jedis("localhost");
        /**
         * 统计用户一年的签到记录 -位图
         * 位图统计指令bitcount ：统计用户一共签到了多少天
         * 位图查找指令bitpos：不指定参数  表示查找用户从哪一天开始第一次签到
         *                      指定参数[start,end] 可以统计在某个时间范围内用户签到了多少天
         *                         但是start,end 参数是字节参数（也就是指定的位范围是8的倍数，而不能任意指定） 也就无法直接计算用户某个月内的签到天数
         */
        Boolean setbit = jedis.setbit("1",5,true);
        System.out.println("是否成功"+setbit);   //0 --false  表示成功
        Boolean getbit = jedis.getbit("1", 5);
        System.out.println("第五个位置为："+getbit);
        Long bitcount = jedis.bitcount("1");
        Long bitpos = jedis.bitpos("1", true);
        System.out.println("第一个为1的位置"+bitpos);
        System.out.println(bitcount);

    }

}
