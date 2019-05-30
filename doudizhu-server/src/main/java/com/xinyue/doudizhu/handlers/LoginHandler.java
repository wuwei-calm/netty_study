package com.xinyue.doudizhu.handlers;

import com.alibaba.fastjson.JSON;
import com.xinyue.doudizhu.command.LoginRequest;
import com.xinyue.doudizhu.command.LoginResponse;
import com.xinyue.doudizhu.dao.model.User;
import com.xinyue.doudizhu.service.IUserService;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

//处理登陆
public class LoginHandler extends ChannelInboundHandlerAdapter {
	private IUserService userService;

	public LoginHandler(IUserService userService) {
		this.userService = userService;
	}

	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof LoginRequest) {
			LoginRequest loginRequest = (LoginRequest) msg;
			String username = loginRequest.getUsername();
			String password = loginRequest.getPassword();
			User user = userService.login(username, password);
			LoginResponse response = new LoginResponse();

			if (user != null) {
				response.setCommandId(loginRequest.getCommandId());
				response.setToken(user.getToken());
				response.setUserId(user.getUserId());
			} else {
				// 返回错误码
				System.out.println("返回错误码");
				response.setErrorCode(1001);
			}
			byte[] data = JSON.toJSONString(response).getBytes();
			ByteBuf b = Unpooled.buffer(1024);
			b.writeBytes(data);
			ctx.writeAndFlush(b);
		}
	}
}
