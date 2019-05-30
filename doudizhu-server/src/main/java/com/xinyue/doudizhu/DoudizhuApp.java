package com.xinyue.doudizhu;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.xinyue.doudizhu.boot.ServerBoot;

@SpringBootApplication
public class DoudizhuApp implements ApplicationContextAware {
	@Autowired
	private ServerBoot serverBoot;

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(DoudizhuApp.class);
		app.setWebEnvironment(false);
		app.run(args);
	}

	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		Thread t = new Thread(()->{
			serverBoot.startServer();
		});
		t.start();
	}
}
