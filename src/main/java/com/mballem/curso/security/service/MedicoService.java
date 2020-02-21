package com.mballem.curso.security.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mballem.curso.security.domain.Medico;
import com.mballem.curso.security.repository.MedicoRepository;
/**
 * 
 * @author leonardoangelo
 *
 */
@Service
public class MedicoService {

	@Autowired
	private MedicoRepository medicoRepository;

	@Transactional
	public Medico buscarPorUsuarioId(Long id) {
		return medicoRepository.findByUsuarioId(id).orElse(new Medico());
	}

	@Transactional
	public void salvar(Medico medico) {

		medicoRepository.save(medico);
	}

	@Transactional
	public void editar(Medico medico) {
		Medico medic = medicoRepository.findById(medico.getId()).get();

		medic.setCrm(medico.getCrm());
		medic.setDtInscricao(medico.getDtInscricao());
		medic.setNome(medico.getNome());

		if (!medico.getEspecialidades().isEmpty())
				medic.getEspecialidades().addAll(medico.getEspecialidades());
	}
	/**
	 * Metodo para buscar um usuario medico pelo email cadastrado.
	 * @param email
	 * @return
	 */
	@Transactional
	public Medico buscarPorEmail(String email) {
		return medicoRepository.findByUsuarioEmail(email).orElse(new Medico());
	}
	
	/**
	 * Metodo para excluir uma especialidade cadastrada por um medico em seu cadastro.
	 * @param idMed
	 * @param idEsp
	 */
	@Transactional
	public void excluirEspecialidadePorMedico(Long idMed, Long idEsp) {
		Medico medico = medicoRepository.findById(idMed).get();
		medico.getEspecialidades().removeIf(e -> e.getId().equals(idEsp));

	}
	
	/**
	 * Metodo para buscar todos os medicos pela especialidade selecionada na hora de marcar a consulta.
	 * @param titulo
	 * @return
	 */
	@Transactional
	public List<Medico> buscarMedicosPorEspecialidade(String titulo) {
		return medicoRepository.findByMedicosPorEspecialidade(titulo);
	}
	/**
	 * Metodo quue retorna uma consulta agendada em uma especialidade verificando se o objeto long é nulo se o retorno for
	 * false segnifica que o objeto é nulo sendo assim podendo excluir uma especialidade ja cadastrada no banco de dados.
	 * @param idMed
	 * @param idEsp
	 * @return
	 */
	@Transactional
	public boolean existeConsultaEspecialidadeAngendada(Long idMed, Long idEsp) {
		return medicoRepository.hasEspecialidadeAgendada(idMed,idEsp).isPresent();
	}
}
