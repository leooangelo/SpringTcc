package com.mballem.curso.security.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mballem.curso.security.domain.Paciente;
import com.mballem.curso.security.domain.Usuario;
import com.mballem.curso.security.service.PacienteService;
import com.mballem.curso.security.service.UsuarioService;

/**
 * 
 * @author leonardoangelo
 *
 */
@Controller
@RequestMapping("pacientes")
public class PacienteController {
	
	@Autowired
	private PacienteService pacienteService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	/**
	 * Este metodo faz abrir a pagina de dados pessoais do paciente, com a logica de id nulo ele verifica se o paciente
	 * tem id nulo caso ele tiver o thymeleaf consegue subentender que estamos fazendo um insert no banco e caso o id não seja
	 * nulo ele sub-entende que estamos fazendo um update.
	 * @param paciente
	 * @param model
	 * @param user
	 * @return
	 */
	@GetMapping("/dados")
	public String cadastrarDadosDoPaciente(Paciente paciente, @AuthenticationPrincipal User user, ModelMap model ) {
		paciente = pacienteService.buscarPorUsuarioEmail(user.getUsername());
		
		if(paciente.hasNotId()) 
			paciente.setUsuario(new Usuario(user.getUsername()));
		
		model.addAttribute("paciente",paciente);
		return "paciente/cadastro";
	}
	/**
	 * Metodo para salvar dados atualizados de um paciente.
	 * @param paciente
	 * @param model
	 * @param user
	 * @return
	 */
	@PostMapping({"/salvar"})
	public String salvarDadosPaciente(Paciente paciente, ModelMap model, @AuthenticationPrincipal User user) {
			
		Usuario usu = usuarioService.buscarPorEmail(user.getUsername());
		if (UsuarioService.isSenhaCorreta(paciente.getUsuario().getSenha(), usu.getSenha())) {
			paciente.setUsuario(usu);
			pacienteService.salvar(paciente);
			model.addAttribute("sucesso", "Seus dados foram inseridos com sucesso.");
		} else 
			
			model.addAttribute("falha", "Sua senha não confere, tente novamente.");
		
		return "paciente/cadastro";
	}	
	/**
	 * Metodo para editar os dados de um paciente.
	 * @param paciente
	 * @param model
	 * @param user
	 * @return
	 */
	@PostMapping({"/editar"})
	public String editarDadosPaciente(Paciente paciente, ModelMap model, @AuthenticationPrincipal User user) {
		
		Usuario usu = usuarioService.buscarPorEmail(user.getUsername());
		if (UsuarioService.isSenhaCorreta(paciente.getUsuario().getSenha(), usu.getSenha()) ) {
			pacienteService.editar(paciente);
			model.addAttribute("sucesso", "Seus dados foram editados com sucesso.");
		} 
		else 
			model.addAttribute("falha", "Sua senha não confere, tente novamente.");
		
		return "paciente/cadastro";
	}

}
