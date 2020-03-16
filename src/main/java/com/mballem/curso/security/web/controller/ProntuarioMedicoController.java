package com.mballem.curso.security.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
