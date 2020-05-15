package com.mballem.curso.security.web.controller;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
/**
 * 
 * @author leonardoangelo
 *
 */
@Controller
public class HomeController {

	/**
	 *  Abre a pagina home
	 * @return
	 */
	
	
	@GetMapping({"/", "/home"})
	public String home() {
		return "home";
	}	
	
	
	/**
	 *  Abre a pagina login
	 * @return
	 */
		@GetMapping({"/login"})
		public String login() {
			return "login";
		}	
		
		@GetMapping("/exames")
		public String exames() {
			return "exames";
		}	
		
		@GetMapping("/consultas-lista")
		public String consultasLista() {
			return "consultas-lista";
		}	
		
		@GetMapping("/check-ups")
		public String checkups() {
			return "check-ups";
		}
		
		@GetMapping("/receita")
		public String receita() {
			return "receita";
		}
		
		/**
		 * Metodo para lançar alertas de erro no login.
		 * @param model
		 * @return
		 */
		@GetMapping({"/login-error"})
		public String loginError(ModelMap model) {
			model.addAttribute("alerta", "erro");
			model.addAttribute("titulo", "Credenciais Inválidas");
			model.addAttribute("texto", "Login ou senha incorretos, tente novamente");
			model.addAttribute("subtexto", "Acesso permitido apenas para usuários cadastrados");
			return "login";
		}	
		
		/**
		 *  Acesso Negado
		 * @param model
		 * @param response
		 * @return
		 */
		@GetMapping({"/acesso-negado"})
		public String acessoNegado(ModelMap model, HttpServletResponse response ) {
			model.addAttribute("status", response.getStatus());
			model.addAttribute("error", "Área Restrita");
			model.addAttribute("message", "Você não tem permissão de acesso para esta área ");
			return "error";
		}
		
		
}
