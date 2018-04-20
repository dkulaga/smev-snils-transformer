package com.skat.smev.snils;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class Smev3SnilsSkatApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(Smev3SnilsSkatApplication.class, args);
	}

//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//		return application.sources(Smev3SnilsSkatApplication.class);
//	}
//
//	public static void main(String[] args) throws Exception {
//		SpringApplication.run(Smev3SnilsSkatApplication.class, args);
//	}
}
