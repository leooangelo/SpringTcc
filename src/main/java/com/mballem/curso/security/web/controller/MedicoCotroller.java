package com.mballem.curso.security.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mballem.curso.security.domain.Medico;
import com.mballem.curso.security.service.MedicoService;

@Controller
@RequestMapping("medicos")
public class MedicoCotroller {
	
	@Autowired
	private MedicoService medicoService;
	
	//abre pagina de dados pessoais de médicos pelo perfil MEDICO
	@GetMapping({"/dados"})
	public String abrirPorMedico(Medico medico, ModelMap model) {
		return "medico/cadastro";
	}	
	
	//Salva médico
	@PostMapping({"/salvar"})
	public String salvar(Medico medico, RedirectAttributes attr) {
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
	}