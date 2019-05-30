package com.xinyue.doudizhu;

import com.alibaba.fastjson.JSON;
import com.xinyue.doudizhu.command.LoginRequest;
import com.xinyue.doudizhu.dao.model.User;
import com.xinyue.doudizhu.handlers.NettyClientCommandHandler;
import com.xinyue.doudizhu.handlers.NettyClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class NettyClient {
    public static void main(String args[]){
        User user = new User();
        user.setUserId(1);
        user.setUsername("ceshi");
        user.setPassword("CESHI");
        user.setToken(null);
        LoginRequest r = new LoginRequest();
        r.setUsername("ceshi");
        r.setPassword("CESHI");
        System.out.println("登录消息模型："+ JSON.toJSONString(r));
        System.out.println("user model ");
        startClien();
    }

    private static void startClien() {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap client = new Bootstrap();
        client.group(group);
        client.channel(NioSocketChannel.class);
        client.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline p = ch.pipeline();
                //p.addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, -4, 0));
                p.addLast(new NettyClientCommandHandler());
                p.addLast(new NettyClientHandler());
            }
        });
        try {
            ChannelFuture cf = client.connect(new InetSocketAddress(8080)).sync();
            Channel channel = cf.channel();
            for(;;){
                System.out.println("===================");
                byte[] b = new byte[1024];
                int len = System.in.read(b);
                String object = new String(b,0,len);
                int total = object.getBytes().length + 4 +4;
                ByteBuf byteBuf = Unpooled.buffer(1024);
                byteBuf.writeInt(total);
                byteBuf.writeInt(2);
                byteBuf.writeBytes(object.getBytes());
                System.out.println("============="+object);
                channel.writeAndFlush(byteBuf);
            }
//            channel.closeFuture().sync();

        } catch (Exception e) {
            group.shutdownGracefully();
        }
    }
}
