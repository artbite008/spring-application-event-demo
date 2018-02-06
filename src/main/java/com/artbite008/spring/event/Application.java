package com.artbite008.spring.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;


@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.artbite008.spring.event")
@ServletComponentScan
public class Application implements CommandLineRunner {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public void run(String... arg0) throws Exception {
		logger.info("Init application");
	}

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

}
