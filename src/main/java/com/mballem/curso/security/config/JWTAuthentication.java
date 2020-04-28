package com.mballem.curso.security.config;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mballem.curso.security.domain.Usuario;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTAuthentication extends UsernamePasswordAuthenticationFilter{

	private AuthenticationManager manager;

	public JWTAuthentication(AuthenticationManager manager) {
		this.manager = manager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			Usuario usuario = new ObjectMapper().readValue(request.getInputStream(),Usuario.class);
			UsernamePasswordAuthenticationToken authenticationToken = new 
					UsernamePasswordAuthenticationToken(usuario.getEmail(),usuario.getSenha());
			Authentication authentication = manager.authenticate(authenticationToken);
			return authentication;
		} catch (IOException e) {
			throw new RuntimeException();
		}
		
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		String email = ((User) authResult.getPrincipal()).getUsername();
		String token = Jwts.builder().setSubject(email).setExpiration(new Date(System.currentTimeMillis() + 
				SecurityConstants.EXPIRATION_TIME)).signWith(SignatureAlgorithm.HS512,SecurityConstants.SECRET)
				.compact();
		
		response.addHeader(SecurityConstants.HEADER_STRING,SecurityConstants.TOKEN_PREFIX+token);
	}
	
	
	
	
	
	
	
	
}
