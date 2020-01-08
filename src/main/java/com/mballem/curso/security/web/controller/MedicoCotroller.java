package com.mballem.curso.security.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mballem.curso.security.domain.Medico;
import com.mballem.curso.security.domain.Usuario;
import com.mballem.curso.security.service.MedicoService;
import com.mballem.curso.security.service.UsuarioService;

@Controller
@RequestMapping("medicos")
public class MedicoCotroller {
	
	@Autowired
	private MedicoService medicoService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	//abre pagina de dados pessoais de médicos pelo perfil MEDICO
	@GetMapping({"/dados"})
	public String abrirPorMedico(Medico medico, ModelMap model, @AuthenticationPrincipal User user) {
		if(medico.hasNotId()) {
			medico = medicoService.buscarPorEmail(user.getUsername());
			model.addAttribute("medico", medico);
		}
		return "medico/cadastro";
	}	
	
	//Salva médico
	@PostMapping({"/salvar"})
	public String salvar(Medico medico, RedirectAttributes attr,@AuthenticationPrincipal User user) {
		
		if(medico.hasNotId() && medico.getUsuario().hasNotId()) {
			Usuario usuario = usuarioService.buscarPorEmail(user.getUsername());
			medico.setUsuario(usuario);
		}
		medicoService.salvar(medico);
		attr.addFlashAttribute("sucesso", "Operação realizado com sucesso.");
		attr.addFlashAttribute("medico", medico);

		return "redirect:/medicos/dados";
	}
	
	//Edita médico
	@PostMapping({"/editar"})
	public String editar(Medico medico, RedirectAttributes attr) {
			medicoService.editar(medico);
			attr.addFlashAttribute("sucesso", "Operação realizado com sucesso.");
			attr.addFlashAttribute("medico", medico);
			
			return "redirect:/medicos/dados";
		}
	
	//excluir Especialidadeo medicos/id/5/excluir/especializacao/1
		@GetMapping({"/id/{idMed}/excluir/especializacao/{idEsp}"})
		public String excluirEspecialidadePorMedico(@PathVariable("idMed") Long idMed, @PathVariable("idEsp") Long idEsp, RedirectAttributes attr) {
				medicoService.excluirEspecialidadePorMedico(idMed, idEsp);
				attr.addFlashAttribute("sucesso", "Especialidade removida com sucesso.");
				
				return "redirect:/medicos/dados";
			}
	
}