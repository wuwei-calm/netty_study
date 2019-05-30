package com.xinyue.doudizhu.service;

import com.xinyue.doudizhu.dao.model.User;

public interface IUserService {

	User login(String username,String password);
}
