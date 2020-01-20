package com.mballem.curso.security.service;

import java.time.LocalDate;
import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mballem.curso.security.domain.Agendamento;
import com.mballem.curso.security.domain.Horario;
import com.mballem.curso.security.repository.AgendamentoRepository;

/**
 * 
 * @author leonardoangelo
 *
 */
@Service
public class AgendamentoService {
	@Autowired
	private AgendamentoRepository agendamentoRepository;

	@Transactional
	public List<Horario> buscarHorariosNaoAgendadosPorMedicosIdEData(Long id, LocalDate data) {
		// TODO Auto-generated method stub
		return agendamentoRepository.findByHorarioNaoAgendadoMedicoIdEData(id, data);
	}

	@Transactional
	public void salvarConsultaAgendada(Agendamento agendamento) {
		// TODO Auto-generated method stub
		agendamentoRepository.save(agendamento);

	}

}
