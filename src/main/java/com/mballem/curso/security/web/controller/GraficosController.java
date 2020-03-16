package com.mballem.curso.security.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.mballem.curso.security.service.EspecialidadeService;
import com.mballem.curso.security.service.MedicoService;
import com.mballem.curso.security.service.PacienteService;
import com.mballem.curso.security.web.conversor.QuantidadeVO;

@Controller
@RequestMapping("dados")
public class GraficosController {

	@Autowired
	private MedicoService medicoService;

	@Autowired
	private PacienteService pacienteService;

	@Autowired
	private EspecialidadeService especialidadeService;

	@GetMapping("/quantidade-usuario")
	public ModelAndView quantidade() {
		ModelAndView modelAndView = new ModelAndView("graficos/quantidade-usuario");
		List<QuantidadeVO> qtd = new ArrayList<>();
		Long medicoQtd = medicoService.buscarQuantidadeMedicos();
		Long pacienteQtd = pacienteService.buscarQuantidadePaciente();
		
		
		QuantidadeVO qtdVo = new QuantidadeVO(medicoQtd, "Médicos");
		qtd.add(qtdVo);
				
		qtdVo = new QuantidadeVO(pacienteQtd, "Pacientes");
		qtd.add(qtdVo);
		modelAndView.addObject("grafico", qtd);

		return modelAndView;
	}

	@GetMapping("/quantidade-especialidade")
	public ModelAndView quantidadeEspecialidade() {
		ModelAndView modelAndView = new ModelAndView("graficos/quantidade-usuario");;
		List<QuantidadeVO> qtd = new ArrayList<>();
		Long especialidadeQtd = especialidadeService.buscarQuantidadeEspecialidade();

		QuantidadeVO qtdVO = new QuantidadeVO(especialidadeQtd, "Especialidades");
		qtd.add(qtdVO);
		modelAndView.addObject("grafico", qtd);
		
		return modelAndView;
	}

}
