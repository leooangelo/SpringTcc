package com.mballem.curso.security.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mballem.curso.security.domain.Paciente;
import com.mballem.curso.security.repository.PacienteRepository;
/**
 * 
 * @author leonardoangelo
 *
 */
@Service
public class PacienteService {
	
	@Autowired
	private PacienteRepository pacienteRepository;	
	
	/**
	 * Metodo para buscar um paciente pelo email.
	 * @param email
	 * @return
	 */
	@Transactional
	public Paciente buscarPorUsuarioEmail(String email) {
		
		return pacienteRepository.findByUsuarioEmail(email).orElse(new Paciente());
	}

	@Transactional
	public void salvar(Paciente paciente) {
		
		pacienteRepository.save(paciente);
		
	}
	
	@Transactional
	public void editar(Paciente paciente) {
		
		Paciente paci = pacienteRepository.findById(paciente.getId()).get();
		paci.setNome(paciente.getNome());
		paci.setDtNascimento(paciente.getDtNascimento());
		
		
	}
}
