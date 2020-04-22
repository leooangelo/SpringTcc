package com.mballem.curso.security.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.mballem.curso.security.domain.Agendamento;
import com.mballem.curso.security.domain.Prontuario;
import com.mballem.curso.security.service.ProntuarioMedicoService;

@Controller
@RequestMapping("prontuario")
public class ProntuarioMedicoController {
	
	@Autowired
	private ProntuarioMedicoService prontuarioMedicoService;
		
		
	@GetMapping("/editar/prontuario")
	private String abrirEditarProntuarioPaciente() {
		
		return "paciente/prontuario";
	}
	
	
	@PreAuthorize("hasAnyAuthority('PACIENTE','ADMIN')")
	@GetMapping("/editar/prontuario/{id}")
	public String preEditarConsultaAgendadaDoPaciente(@PathVariable("id") Long id, ModelMap model) {
		Prontuario prontuario = prontuarioMedicoService.buscarPorIdEUsuario(id);

		model.addAttribute("prontuario", prontuario);
		return "prontuario/prontuario";
	}
}
