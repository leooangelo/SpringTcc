package com.mballem.curso.security.config;

public class SecurityConstants {

	public static final String SECRET = "Teste";
	
	public static final String TOKEN_PREFIX = "Bearer";
	
	public static final String HEADER_STRING = "Authorization";
	
	public static final String LOGAR = "/login";
	
	public static final long EXPIRATION_TIME = 86400000; // vai expirar em um dia
	
	//public static void main(String[] args) {
		//System.out.println(TimeUnit.MILLISECONDS.convert(1,TimeUnit.DAYS));
//	}
}
