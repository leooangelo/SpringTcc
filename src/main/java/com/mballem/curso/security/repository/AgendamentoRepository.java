package com.mballem.curso.security.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

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
	
	@Query("select a from Agendamento a "
			+ "where "
			+ " (a.id = :id AND a.paciente.usuario.email like :email) "
			+ " OR"
			+ " (a.id = :id AND a.medico.usuario.email like :email)")
	Optional <Agendamento> FindByIdAndPacienteOrMedicoEmail(Long id, String email);
	
	/**
	 * Query que traz a hora de uma consulta pelo id
	 * @param id
	 * @return
	 */
	@Query("select horaMinuto from Horario where id = :id")
	LocalTime buscaHoraConsulta(Long id);
	
}

