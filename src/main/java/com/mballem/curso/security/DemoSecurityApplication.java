package com.mballem.curso.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.mballem.curso.security.service.EmailService;
import com.mballem.curso.security.service.S3Service;

@SpringBootApplication
public class DemoSecurityApplication implements CommandLineRunner{
	
	@Autowired
	private S3Service s3Service;
	
	public static void main(String[] args) {
		//System.out.println(new BCryptPasswordEncoder().encode("123456"));
		
		SpringApplication.run(DemoSecurityApplication.class, args);
	}
	@Autowired
	EmailService emailService;
	
	@Override
	public void run(String... args) throws Exception {
		s3Service.uploadFile("C:\\Users\\leonardoangelo\\Desktop\\Spring-Médico-Tcc"
				+ "\\demo-security\\src\\main\\resources\\static\\image\\spring-security.png");
		
	}	
}
