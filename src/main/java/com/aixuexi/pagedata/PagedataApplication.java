package com.aixuexi.pagedata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PagedataApplication {

	public static void main(String[] args) {
		SpringApplication.run(PagedataApplication.class, args);
	}
}
