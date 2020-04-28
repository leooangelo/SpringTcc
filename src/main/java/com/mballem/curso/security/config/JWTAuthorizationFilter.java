package com.mballem.curso.security.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.mballem.curso.security.service.UsuarioService;

import io.jsonwebtoken.Jwts;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	private UsuarioService usuarioService;
	
	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, UsuarioService usuarioService) {
		super(authenticationManager);
		this.usuarioService = usuarioService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String header = request.getHeader(SecurityConstants.HEADER_STRING);
		if(header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
			chain.doFilter(request, response);
			return ;
		}
		
		
		getAuthenticationToken(request);
		
		UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(request);
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		chain.doFilter(request, response);
	}
	
	private UsernamePasswordAuthenticationToken getAuthenticationToken(HttpServletRequest request) {
		String token = request.getHeader(SecurityConstants.HEADER_STRING);
		
		if(token == null) return null;
		
		String email = Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(
				token.replace(SecurityConstants.SECRET,"")).getBody().getSubject();
		
		UserDetails userDetails = usuarioService.loadUserByUsername(email);
		return email!=null ? new UsernamePasswordAuthenticationToken(email,null,userDetails.getAuthorities()):null;
		
	}
	
	

}
