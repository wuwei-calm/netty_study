package com.xinyue.doudizhu.handlers;

import com.alibaba.fastjson.JSON;
import com.xinyue.doudizhu.command.IGameCommand;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

public class EncodeCommandHandler extends ChannelOutboundHandlerAdapter {
	private InternalLogger logger = InternalLoggerFactory.getInstance(EncodeCommandHandler.class);

	public void writeAndFlush(ChannelHandlerContext ctx, Object msg) throws Exception {

		if (msg instanceof IGameCommand) {
			// 协议格式：4(总字节数) + 4(命令id) + 变长消息体
			IGameCommand gameCommand = (IGameCommand) msg;
			int commandId = gameCommand.getCommandId();
			String jsonBody = JSON.toJSONString(msg);
			byte[] bytes = jsonBody.getBytes();
			int total = 4 + 4 + bytes.length;
			ByteBuf byteBuf = ctx.channel().alloc().buffer(total);
			byteBuf.writeInt(total);
			byteBuf.writeInt(commandId);
			byteBuf.writeBytes(bytes);
			ctx.writeAndFlush(byteBuf);
			logger.debug("返回命令成功：{}" + jsonBody);
		} else {
			throw new IllegalArgumentException(msg.getClass().getName() + "类型不是返回的对象");
		}

	}
}
