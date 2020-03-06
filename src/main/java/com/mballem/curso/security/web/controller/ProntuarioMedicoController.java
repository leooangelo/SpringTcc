package com.mballem.curso.security.web.controller;

import java.text.ParseException;

import javax.mail.MessagingException;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mballem.curso.security.domain.Agendamento;
import com.mballem.curso.security.domain.Especialidade;
import com.mballem.curso.security.domain.ProntuarioMedico;

@Controller
@RequestMapping("prontuario")
public class ProntuarioMedicoController {

	@PreAuthorize("hasAnyAuthority('MEDICO','ADMIN')")
	@GetMapping("/editar/prontuario/{id}")
	public String preEditarProntuarioDoPaciente(@PathVariable("id") Long id, ModelMap model,
			@AuthenticationPrincipal User user) {
		ProntuarioMedico prontuarioMedico = prontuarioMedicoService.buscarPorIdEUsuario(id, user.getUsername());

		model.addAttribute("prontuarioMedico", prontuarioMedico);
		return "agendamento/cadastro";
	}

	@PreAuthorize("hasAnyAuthority('MEDICO','ADMIN')")
	@PostMapping("editar")
	public String editarProntuarioDoPaciente(ModelMap model, @AuthenticationPrincipal User user,
			ProntuarioMedico prontuario) throws MessagingException, ParseException {
		String texto = prontuario.getDesc();
		ProntuarioMedico prontuarioMedico = prontuarioMedicoService.buscarProntuario();

		prontuario.setDesc(texto);
		prontuarioMedicoService.editar(prontuario, user.getUsername());
		model.addAttribute("sucesso", "Prontuario atualizado com sucesso. !");

		return "redirect:/agendamentos/agendar";
	}

}
