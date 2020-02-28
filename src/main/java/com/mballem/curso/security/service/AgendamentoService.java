package com.mballem.curso.security.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mballem.curso.security.datatables.Datatables;
import com.mballem.curso.security.datatables.DatatablesColunas;
import com.mballem.curso.security.domain.Agendamento;
import com.mballem.curso.security.domain.Horario;
import com.mballem.curso.security.repository.AgendamentoRepository;
import com.mballem.curso.security.repository.projection.HistoricoPaciente;
import com.mballem.curso.security.web.Exception.AcessoNegadoException;

/**
 * 
 * @author leonardoangelo
 *
 */
@Service
public class AgendamentoService {
	@Autowired
	private AgendamentoRepository agendamentoRepository;

	@Autowired
	private Datatables dataTables;

	@Transactional
	public List<Horario> buscarHorariosNaoAgendadosPorMedicosIdEData(Long id, LocalDate data) {
		return agendamentoRepository.findByHorarioNaoAgendadoMedicoIdEData(id, data);
	}

	@Transactional
	public void salvarConsultaAgendada(Agendamento agendamento) {
		agendamentoRepository.save(agendamento);

	}

	@Transactional
	public Map<String, Object> buscarHistoricoDoPacientePorEmail(String email, HttpServletRequest request) {

		dataTables.setRequest(request);
		dataTables.setColunas(DatatablesColunas.AGENDAMENTOS);
		Page<HistoricoPaciente> page = agendamentoRepository.buscarHistoricoDoPacientePorEmail(email,
				dataTables.getPageable());

		return dataTables.getResponse(page);

	}

	@Transactional
	public Map<String, Object> buscarHistoricoDoMedicoPorEmail(String email, HttpServletRequest request) {

		dataTables.setRequest(request);
		dataTables.setColunas(DatatablesColunas.AGENDAMENTOS);
		Page<HistoricoPaciente> page = agendamentoRepository.buscarHistoricoDoMedicoPorEmail(email,
				dataTables.getPageable());

		return dataTables.getResponse(page);
	}

	@Transactional
	public Agendamento buscarConsultaAgendadaPorId(Long id) {

		return agendamentoRepository.findById(id).get();
	}

	@Transactional
	public void editar(Agendamento agendamento, String email) {
		Agendamento ag = buscarPorIdEUsuario(agendamento.getId(), email);
		ag.setDataConsulta(agendamento.getDataConsulta());
		ag.setEspecialidade(agendamento.getEspecialidade());
		ag.setHorario(agendamento.getHorario());
		ag.setMedico(agendamento.getMedico());
	}
	/**
	 * Metodo que busca uma consulta agendada pelo id e o email do usuário, caso o email não tenha tenha uma consulta com aquele id
	 * é lançado um acesso negado.
	 * @param id
	 * @param email
	 * @return
	 */
	@Transactional
	public Agendamento buscarPorIdEUsuario(Long id, String email) {
		return agendamentoRepository.FindByIdAndPacienteOrMedicoEmail(id,email)
				.orElseThrow(() -> new AcessoNegadoException("Acesso Negado ao usuário: " + email));
	}
	/**
	 * Metodo para remover uma consulta no banco de dados.
	 * @param id
	 */
	@Transactional
	public void remover(Long id) {
		agendamentoRepository.deleteById(id);
	}
	

}
