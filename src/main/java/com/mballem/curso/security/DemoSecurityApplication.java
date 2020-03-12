package com.mballem.curso.security;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoSecurityApplication implements CommandLineRunner{
	
	
	public static void main(String[] args) {
		//System.out.println(new BCryptPasswordEncoder().encode("123456"));
		
		SpringApplication.run(DemoSecurityApplication.class, args);
	}

	
	@Override
	public void run(String... args) throws Exception {

		
	}	
}
