package com.mballem.curso.security.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
	
	
	/*
	 * Este metodo faz abrir a pagina de dados pessoais do paciente.
	 */
	@GetMapping({"/dados"})
	public String cadastrarDadosPaciente(Paciente paciente, ModelMap model, @AuthenticationPrincipal User user) {
		paciente = pacienteService.buscarPorUsuarioEmail(user.getUsername());
		
		if(paciente.hasNotId()) {
			paciente.setUsuario(new Usuario(user.getUsername()));
			model.addAttribute("paciente", paciente);
		}
		return	"paciente/dados";
	}
	@PostMapping({"/salvar"})
	public String salvarDadosPaciente(Paciente paciente, ModelMap model, @AuthenticationPrincipal User user) {
			
		Usuario usu = usuarioService.buscarPorEmail(user.getUsername());
		if (UsuarioService.isSenhaCorreta(paciente.getUsuario().getSenha(), usu.getSenha())) {
			paciente.setUsuario(usu);
			pacienteService.salvar(paciente);
			model.addAttribute("sucesso", "Seus dados foram inseridos com sucesso.");
		} else {
			model.addAttribute("falha", "Sua senha não confere, tente novamente.");
		}
		return "paciente/cadastro";
	}	
	
	@PostMapping({"/editar"})
	public String editarDadosPaciente(Paciente paciente, ModelMap model, @AuthenticationPrincipal User user) {
			
		Usuario usu = usuarioService.buscarPorEmail(user.getUsername());
		if (UsuarioService.isSenhaCorreta(paciente.getUsuario().getSenha(), usu.getSenha())) {
			pacienteService.editar(paciente);
			model.addAttribute("sucesso", "Seus dados foram editados com sucesso.");
		} else {
			model.addAttribute("falha", "Sua senha não confere, tente novamente.");
		}
		return "paciente/cadastro";
	}	
	
}
