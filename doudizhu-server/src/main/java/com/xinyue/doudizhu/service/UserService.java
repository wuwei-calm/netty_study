package com.xinyue.doudizhu.service;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import org.springframework.stereotype.Service;

import com.xinyue.doudizhu.dao.model.User;

@Service
public class UserService implements IUserService {
	private InternalLogger logger = InternalLoggerFactory.getInstance(UserService.class);
	@Override
	public User login(String username, String password) {
		logger.warn("进入登录方法了");
		return new User();
	}

}
