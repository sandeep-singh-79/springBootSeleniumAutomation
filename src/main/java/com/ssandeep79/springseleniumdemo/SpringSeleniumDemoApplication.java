package com.ssandeep79.springseleniumdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication (exclude = MongoAutoConfiguration.class)
public class SpringSeleniumDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSeleniumDemoApplication.class, args);
	}

}