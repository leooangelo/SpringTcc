package com.mballem.curso.security.web.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mballem.curso.security.domain.Medico;
import com.mballem.curso.security.domain.Perfil;
import com.mballem.curso.security.domain.PerfilTipo;
import com.mballem.curso.security.domain.Usuario;
import com.mballem.curso.security.service.MedicoService;
import com.mballem.curso.security.service.UsuarioService;

@Controller
@RequestMapping("u")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private MedicoService medicoService;

	// abre o cadastro de usuarios(medico,admin e paciente)
	@GetMapping("/novo/cadastro/usuario")
	public String cadastroPorAdminParaAdminMedicoPaciente(Usuario usuario) {
		return "usuario/cadastro";
	}

	// abre a lista de usuários
	@GetMapping("/lista")
	public String listarUsuarios() {
		return "usuario/lista";
	}

	// Listar usuarios na datatables
	@GetMapping("/datatables/server/usuarios")
	public ResponseEntity<?> listarUsuariosDatatables(HttpServletRequest request) {

		return ResponseEntity.ok(usuarioService.buscarTodos(request));
	}

	// salvar cadastro de user por admn
	@PostMapping("/cadastro/salvar")
	public String salvarUsuario(Usuario usuario, RedirectAttributes attr) {
		List<Perfil> perfis = usuario.getPerfis();
		if (perfis.size() > 2 || perfis.containsAll(Arrays.asList(new Perfil(PerfilTipo.ADMIN.getCod()),
																		new Perfil(PerfilTipo.PACIENTE.getCod())))
				|| perfis.containsAll(Arrays.asList(new Perfil(PerfilTipo.MEDICO.getCod()), new Perfil(PerfilTipo.PACIENTE.getCod())))) {
			attr.addFlashAttribute("falha", "Paciente não pode ser admin e/ou médico.");
			attr.addFlashAttribute("usuario", usuario);
		} else {
			try {
				usuarioService.salvarsuario(usuario);
				attr.addFlashAttribute("sucesso", "Operação realizada com sucesso !");
			} catch (DataIntegrityViolationException ex) {
				attr.addFlashAttribute("falha", "Este e-mail já esta cadastrado no sistema");
			}
		}
		return "redirect:/u/novo/cadastro/usuario";

	}
	//Metodo pre para editar credenciais do usuario
	@GetMapping("/editar/credenciais/usuario/{id}")
	public ModelAndView preEditarCredenciais(@PathVariable("id")Long id ) {
		
		return new ModelAndView("usuario/cadastro", "usuario", usuarioService.buscarPorId(id));
	}
	// metodo pre para editar cadastro de usuarios
	@GetMapping("/editar/dados/usuario/{id}/perfis/{perfis}")
	public ModelAndView preEditarCadastroDadosPessoais(@PathVariable("id")Long usuarioId,
														@PathVariable("perfis") Long[] perfisId) {
		
		Usuario us = usuarioService.buscarPorIdEPerfis(usuarioId,perfisId);
		if(us.getPerfis().contains(new Perfil(PerfilTipo.ADMIN.getCod())) &&
				!us.getPerfis().contains(new Perfil(PerfilTipo.MEDICO.getCod())) ) 
					return new ModelAndView("usuario/cadastro", "usuario",us);
			
		else if(us.getPerfis().contains(new Perfil(PerfilTipo.MEDICO.getCod())) ) {
			
			Medico medico = medicoService.buscarPorUsuarioId(usuarioId);
			return medico.hasNotId()
				? new ModelAndView("medico/cadastro", "medico", new Medico(new Usuario(usuarioId)))
				: new ModelAndView("medico/cadastro", "medico", medico);
	
		}
		else if(us.getPerfis().contains(new Perfil(PerfilTipo.PACIENTE.getCod()))) {
			ModelAndView model = new ModelAndView("error");
			
			model.addObject("status", 403);
			model.addObject("error", "Área Restrita");
			model.addObject("message", "Os dados de pacientes são restritos ");
			return model;
		}

		return new ModelAndView("redirect:/u/lista");
	}
	
	
}
