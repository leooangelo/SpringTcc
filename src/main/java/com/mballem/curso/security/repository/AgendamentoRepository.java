package com.mballem.curso.security.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mballem.curso.security.domain.Agendamento;
import com.mballem.curso.security.domain.Horario;
import com.mballem.curso.security.repository.projection.HistoricoPaciente;
/**
 * 
 * @author leonardoangelo
 *
 */
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long>{
	/**
	 * 
	 * @param id
	 * @param data
	 * @return
	 */
	@Query("select h "
			+ "from Horario h "
			+ "where not exists("
				+ "select a.horario.id "
					+ "from Agendamento a "
					+ "where "
						+ "a.medico.id = :id and "
						+ "a.dataConsulta = :data and "
						+ "a.horario.id = h.id "
			+") "
			+ "order by h.horaMinuto asc")
	List<Horario> findByHorarioNaoAgendadoMedicoIdEData(Long id, LocalDate data);
	/**
	 * 
	 * @param email
	 * @param pageable
	 * @return
	 */
	@Query("select a.id as id,"
			+ "a.paciente as paciente,"
			+ "CONCAT(a.dataConsulta, ' ', a.horario.horaMinuto) as dataConsulta,"
			+ "a.medico as medico,"
			+ "a.especialidade as especialidade "
		+ "from Agendamento a "
		+ "where a.paciente.usuario.email like :email")
	Page<HistoricoPaciente> buscarHistoricoDoPacientePorEmail(String email, Pageable pageable);
	
	/**
	 * 
	 * @param email
	 * @param pageable
	 * @return
	 */
	@Query("select a.id as id,"
			+ "a.paciente as paciente,"
			+ "CONCAT(a.dataConsulta, ' ', a.horario.horaMinuto) as dataConsulta,"
			+ "a.medico as medico,"
			+ "a.especialidade as especialidade "
		+ "from Agendamento a "
		+ "where a.medico.usuario.email like :email")
	Page<HistoricoPaciente> buscarHistoricoDoMedicoPorEmail(String email, Pageable pageable);

}
