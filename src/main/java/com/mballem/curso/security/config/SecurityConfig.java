package com.mballem.curso.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
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
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	private static final String ADMIN = PerfilTipo.ADMIN.getDesc();
	private static final String MEDICO = PerfilTipo.MEDICO.getDesc();
	private static final String PACIENTE = PerfilTipo.PACIENTE.getDesc();
	
	@Autowired
	private UsuarioService usuarioService;
	
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//			
//		/**
//		 * Permissão de acesso para todos os usuários.
//		 */
//		http.authorizeRequests()
//		.antMatchers("/webjars/**", "/css/**", "/image/**", "/js/**","/jspdf/**","/jquery/**").permitAll()
//		.antMatchers("/", "/home", "/check-ups", "/exames", "/consultas-lista").permitAll()
//		.antMatchers("/u/novo/cadastro","/u/cadastro/realizado","/u/cadastro/paciente/salvar").permitAll()
//		.antMatchers("/u/confirmacao/cadastro").permitAll()
//		.antMatchers("/pagamento", "/pay").permitAll()
//		.antMatchers("/u/p/**").permitAll()
//		
//		
//		
//		/**
//		 * Permissão de acesso para usuário ADMIN e MEDICO.
//		 */
//		.antMatchers("/u/editar/senha", "/u/confirmar/senha").hasAnyAuthority(MEDICO,ADMIN,PACIENTE)
//		.antMatchers("/u/**").hasAuthority(ADMIN)
//		.antMatchers("/u/quantidade").hasAuthority(ADMIN)
//		
//		
//		/**
//		 * Permissão de acesso a paginas de MEDICO
//		 */
//		.antMatchers("/medicos/especialidade/titulo/*").hasAnyAuthority(PACIENTE,MEDICO,ADMIN)
//		.antMatchers("/medicos/dados", "/medicos/salvar", "/medicos/editar").hasAnyAuthority(MEDICO,ADMIN)
//		.antMatchers("/medicos/**","/receita").hasAnyAuthority(MEDICO, ADMIN)
//		
//		/**
//		 * Permissão de acesso a paginas de PACIENTES.
//		 */
//		.antMatchers("/pacientes/dados", "/pacientes/salvar", "/pacientes/editar", "/u/pacientes/dados").hasAnyAuthority(PACIENTE,ADMIN)
//		.antMatchers("/pacientes/**").hasAnyAuthority(PACIENTE,ADMIN)
//		
//		
//		/**
//		 * Permissão de acesso a paginas de ESPECIALIDADES.
//		 */
//		.antMatchers("/especialidades/datatables/server/medico/*").hasAnyAuthority(MEDICO, ADMIN)
//		.antMatchers("/especialidades/titulo").hasAnyAuthority(MEDICO, ADMIN,PACIENTE)
//		.antMatchers("/especialidades/**").hasAuthority(ADMIN)
//		
//		.anyRequest().authenticated()
//		.and()
//			.formLogin()
//			.loginPage("/login")
//			.defaultSuccessUrl("/", true)
//			.failureUrl("/login-error")
//			.permitAll()
//		.and()
//			.logout()
//			.logoutSuccessUrl("/")
//		.and()
//			.exceptionHandling()
//			.accessDeniedPage("/acesso-negado")
//		.and()
//			.rememberMe();
//			
//	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.cors().and().csrf().disable().authorizeRequests().antMatchers(HttpMethod.GET,SecurityConstants.LOGAR)
		.permitAll()
		// permssao de acesso para todos os usuarios
		.antMatchers("/webjars/**", "/css/**", "/image/**", "/js/**","/jspdf/**","/jquery/**").permitAll()
		.antMatchers("/", "/home", "/check-ups", "/exames", "/consultas-lista").permitAll()
		.antMatchers("/u/novo/cadastro","/u/cadastro/realizado","/u/cadastro/paciente/salvar").permitAll()
		.antMatchers("/u/confirmacao/cadastro").permitAll()
		.antMatchers("/pagamento", "/pay").permitAll()
		.antMatchers("/u/p/**").permitAll()
		// Permissão de acesso para usuário ADMIN e MEDICO.
		.antMatchers("/u/editar/senha", "/u/confirmar/senha").hasAnyAuthority(MEDICO,ADMIN,PACIENTE)
		.antMatchers("/u/**").hasAuthority(ADMIN)
		.antMatchers("/u/quantidade").hasAuthority(ADMIN)
			
		/**
		 * Permissão de acesso a paginas de MEDICO
		 */
		.antMatchers("/medicos/especialidade/titulo/*").hasAnyAuthority(PACIENTE,MEDICO,ADMIN)
		.antMatchers("/medicos/dados", "/medicos/salvar", "/medicos/editar").hasAnyAuthority(MEDICO,ADMIN)
		.antMatchers("/medicos/**","/receita").hasAnyAuthority(MEDICO, ADMIN)
		
		/**
		 * Permissão de acesso a paginas de PACIENTES.
		 */
		.antMatchers("/pacientes/dados", "/pacientes/salvar", "/pacientes/editar", "/u/pacientes/dados").hasAnyAuthority(PACIENTE,ADMIN)
		.antMatchers("/pacientes/**").hasAnyAuthority(PACIENTE,ADMIN)
		
		
		/**
		 * Permissão de acesso a paginas de ESPECIALIDADES.
		 */
		.antMatchers("/especialidades/datatables/server/medico/*").hasAnyAuthority(MEDICO, ADMIN)
		.antMatchers("/especialidades/titulo").hasAnyAuthority(MEDICO, ADMIN,PACIENTE)
		.antMatchers("/especialidades/**").hasAuthority(ADMIN)
		
		//.anyRequest().authenticated()
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
			.accessDeniedPage("/acesso-negado")
		.and()
			.rememberMe()
			.and().addFilter(new JWTAuthentication(authenticationManager()))
			.addFilter(new JWTAuthorizationFilter(authenticationManager(),usuarioService));			
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(usuarioService).passwordEncoder(new BCryptPasswordEncoder());
	}	
	
	
}
