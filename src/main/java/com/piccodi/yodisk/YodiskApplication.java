package com.piccodi.yodisk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class YodiskApplication {

	public static void main(String[] args) {
		SpringApplication.run(YodiskApplication.class, args);
	}

}

//todo сделать нормальную обработку ошибок!!!