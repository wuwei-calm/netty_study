package com.xinyue.doudizhu.boot;

import java.util.concurrent.TimeUnit;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinyue.doudizhu.handlers.DecodeCommandHandler;
import com.xinyue.doudizhu.handlers.EncodeCommandHandler;
import com.xinyue.doudizhu.handlers.LoginHandler;
import com.xinyue.doudizhu.service.IUserService;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

@Service
public class ServerBoot {
	@Autowired
	private IUserService userService;
	private EventLoopGroup bossGroup = null;
	private EventLoopGroup workerGroup = null;
	private InternalLogger logger = InternalLoggerFactory.getInstance(ServerBoot.class);

	public void startServer() {
		int port = 8080;
		bossGroup = new NioEventLoopGroup(1);
		workerGroup = new NioEventLoopGroup(32);
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<Channel>() {

						@Override
						protected void initChannel(Channel ch) throws Exception {
							ChannelPipeline p = ch.pipeline();
							//p.addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, -4, 0));
							p.addLast("decode", new DecodeCommandHandler());
							p.addLast(new EncodeCommandHandler());
							p.addLast(new LoginHandler(userService));
						}
					}).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);
			b.childOption(ChannelOption.TCP_NODELAY, true);
			logger.info("服务器启动成功，端口号:{}", port);
			ChannelFuture f = b.bind(port).sync();
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	public void stop() {
		int quietPeriod = 5;
		int timeout = 30;
		TimeUnit timeUnit = TimeUnit.SECONDS;
		workerGroup.shutdownGracefully(quietPeriod, timeout, timeUnit);
		bossGroup.shutdownGracefully(quietPeriod, timeout, timeUnit);
	}
}
