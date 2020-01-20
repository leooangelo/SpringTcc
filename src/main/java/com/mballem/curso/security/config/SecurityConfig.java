package com.mballem.curso.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.mballem.curso.security.domain.PerfilTipo;
import com.mballem.curso.security.service.UsuarioService;
/**
 * 
 * @author leonardoangelo
 * 
 * Classe para configuração de permissões nas páginas web para cada tipo de usuário. 
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	private static final String ADMIN = PerfilTipo.ADMIN.getDesc();
	private static final String MEDICO = PerfilTipo.MEDICO.getDesc();
	private static final String PACIENTE = PerfilTipo.PACIENTE.getDesc();
	
	@Autowired
	private UsuarioService usuarioService;
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		/**
		 * Permissão de acesso para todos os usuários.
		 */
		http.authorizeRequests()
		.antMatchers("/webjars/**", "/css/**", "/image/**", "/js/**").permitAll()
		//editado
		.antMatchers("/", "/home").permitAll()
		
		/**
		 * Permissão de acesso para usuário ADMIN e MEDICO.
		 */
		.antMatchers("/u/editar/senha", "/u/confirmar/senha").hasAnyAuthority(MEDICO,ADMIN,PACIENTE)
		.antMatchers("/u/**").hasAuthority(ADMIN)
		
		/**
		 * Permissão de acesso a paginas de MEDICO
		 */
		.antMatchers("/medicos/especialidade/titulo/*").hasAuthority(PACIENTE)
		.antMatchers("/medicos/dados", "/medicos/salvar", "/medicos/editar").hasAnyAuthority(MEDICO,ADMIN)
		.antMatchers("/medicos/**").hasAuthority(MEDICO)
		
		/**
		 * Permissão de acesso a paginas de PACIENTES.
		 */
		.antMatchers("/pacientes/dados", "/pacientes/salvar", "/pacientes/editar").hasAnyAuthority(PACIENTE,ADMIN)
		.antMatchers("/pacientes/**").hasAnyAuthority(PACIENTE)
		
		
		/**
		 * Permissão de acesso a paginas de ESPECIALIDADES.
		 */
		.antMatchers("/especialidades/datatables/server/medico/*").hasAnyAuthority(MEDICO, ADMIN)
		.antMatchers("/especialidades/titulo").hasAnyAuthority(MEDICO, ADMIN,PACIENTE)
		.antMatchers("/especialidades/**").hasAuthority(ADMIN)
		
		.anyRequest().authenticated()
		.and()
			.formLogin()
			.loginPage("/login")
			.defaultSuccessUrl("/", true)
			.failureUrl("/login-error")
			.permitAll()
		.and()
			.logout()
			.logoutSuccessUrl("/")
		.and()
			.exceptionHandling()
			.accessDeniedPage("/acesso-negado");
			
	}
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(usuarioService).passwordEncoder(new BCryptPasswordEncoder());
	}	
	
	
}
