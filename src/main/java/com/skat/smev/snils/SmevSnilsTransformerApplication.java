package com.skat.smev.snils;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class SmevSnilsTransformerApplication extends SpringBootServletInitializer{

//	public static void main(String[] args) {
//		SpringApplication.run(SmevSnilsTransformerApplication.class, args);
//	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SmevSnilsTransformerApplication.class);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(SmevSnilsTransformerApplication.class, args);
	}
}
