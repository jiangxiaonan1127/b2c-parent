package com.turing.b2c;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.turing.b2c.mapper")
public class B2cDaoApplication {

	public static void main(String[] args) {
		SpringApplication.run(B2cDaoApplication.class, args);
	}

}
