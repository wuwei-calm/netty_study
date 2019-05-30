package com.xinyue.doudizhu.handlers;

import java.util.HashMap;
import java.util.Map;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.xinyue.doudizhu.command.IGameCommand;
import com.xinyue.doudizhu.command.LoginRequest;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class DecodeCommandHandler extends ChannelInboundHandlerAdapter {
	private InternalLogger logger = InternalLoggerFactory.getInstance(DecodeCommandHandler.class);
	private Map<Integer, Class<? extends IGameCommand>> commandClassMap = new HashMap<>();

	public DecodeCommandHandler() {
		// 这里注册要收到的命令
		commandClassMap.put(2, LoginRequest.class);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof ByteBuf) {
			//协议格式：4(总字节数) + 4(命令id) + 变长消息体
			ByteBuf byteBuf = (ByteBuf) msg;
			int total = byteBuf.readInt();
			logger.debug("收到请求，字节数{}", total);
			int commandId = byteBuf.readInt();
			byte[] bodyBytes = new byte[byteBuf.readableBytes()];
			byteBuf.readBytes(bodyBytes);
			String json = new String(bodyBytes);
			logger.info("入栈消息："+json);
			Class<? extends IGameCommand> cl = commandClassMap.get(commandId);
			if (cl == null) {
				logger.debug("{}命令未注册，请在上面注册", commandId);
			}
			IGameCommand command = JSON.parseObject(json, commandClassMap.get(commandId));
			if (command != null) {
				ctx.fireChannelRead(command);
			} else {
				throw new NullPointerException(commandId + "找不到解析");
			}

		}
	}
}
