package com.turing.b2c;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableDubbo
@EnableCaching	//开启识别缓存注解
public class B2cContentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(B2cContentServiceApplication.class, args);
	}

}
