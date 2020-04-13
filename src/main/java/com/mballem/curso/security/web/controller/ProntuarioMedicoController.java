package com.mballem.curso.security.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
	
	
	@GetMapping("/editar/credenciais/usuario/{id}")
	public ModelAndView preEditarCredenciais(@PathVariable("id") Long id) {

		return new ModelAndView("paciente/prontuario", "prontuario", prontuarioMedicoService.buscarPorId(id));
	}
}
