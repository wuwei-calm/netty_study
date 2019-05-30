package com.xinyue.doudizhu.handlers;

import com.xinyue.doudizhu.command.IGameCommand;
import com.xinyue.doudizhu.command.LoginRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class NettyClientCommandHandler extends ChannelInboundHandlerAdapter {
	private InternalLogger logger = InternalLoggerFactory.getInstance(NettyClientCommandHandler.class);
	private Map<Integer, Class<? extends IGameCommand>> commandClassMap = new HashMap<>();

	public NettyClientCommandHandler() {
		// 这里注册要收到的命令
		commandClassMap.put(2, LoginRequest.class);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		logger.info(msg.toString());
		if (msg instanceof ByteBuf) {
			//协议格式：4(总字节数) + 4(命令id) + 变长消息体
			ByteBuf byteBuf = (ByteBuf) msg;
			logger.debug("收到数据{}", msg);
			int commandId = byteBuf.readInt();
			byte[] bodyBytes = new byte[byteBuf.readableBytes()];
			byteBuf.readBytes(bodyBytes);
			String json = new String(bodyBytes);
			if (json != null) {
				logger.info("收到的响应："+json);
			} else {
				throw new NullPointerException(commandId + "找不到解析");
			}

		}
	}
}
