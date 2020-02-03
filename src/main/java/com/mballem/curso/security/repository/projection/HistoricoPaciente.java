package com.mballem.curso.security.repository.projection;

import com.mballem.curso.security.domain.Especialidade;
import com.mballem.curso.security.domain.Medico;
import com.mballem.curso.security.domain.Paciente;
/*
 * Esta classe ela recebe todos os parametos da query que tras o historico de consultas agendadas por paciente, esta classe serve
 * para fazer a projeção dos dados
 */
public interface HistoricoPaciente {
	
	Long getId();
	
	Paciente getPaciente();
	
	String getDataConsulta();
	
	Medico getMedico();
	
	Especialidade getEspecialidade();

}
