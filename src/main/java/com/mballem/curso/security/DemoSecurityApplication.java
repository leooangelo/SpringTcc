package com.mballem.curso.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.mballem.curso.security.service.EmailService;

@SpringBootApplication
public class DemoSecurityApplication implements CommandLineRunner{

	public static void main(String[] args) {
		//System.out.println(new BCryptPasswordEncoder().encode("123456"));
		
		SpringApplication.run(DemoSecurityApplication.class, args);
	}
	@Autowired
	EmailService emailService;
	
	@Override
	public void run(String... args) throws Exception {
		
		emailService.enviarPedidoDeConfirmacaoCadastro("clinicaspring@gmail.com", "2332teste");
		
		/*	
		SimpleMailMessage simple = new SimpleMailMessage();
		simple.setTo("clinicaspring@gmail.com");
		simple.setText("Teste 1");
		simple.setSubject("teste1");
		sender.send(simple);
		
		*/
	}	
}
