package com.attendance.config;

import java.io.Serializable;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author harsha.patil
 *
 */

@SpringBootApplication
@ComponentScan(basePackages = { "com.attendance" })
public class AttendanceApplication extends SpringBootServletInitializer implements Serializable
{
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(AttendanceApplication.class);
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder springApplicationBuilder)
	{
		return springApplicationBuilder.sources(AttendanceApplication.class);
	}
	public static void main(String[] args)
	{
		SpringApplication.run(AttendanceApplication.class, args);
		logger.info("Server started at " + Calendar.getInstance().getTime());
	}
}
