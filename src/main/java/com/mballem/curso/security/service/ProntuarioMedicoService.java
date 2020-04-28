package com.mballem.curso.security.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mballem.curso.security.domain.Prontuario;
import com.mballem.curso.security.repository.ProntuarioMedicoRepository;

@Service
public class ProntuarioMedicoService {
	
	@Autowired
	private ProntuarioMedicoRepository prontuarioMedicoRepository;
	
	
	@Transactional
	public void salvar(Prontuario prontuario) {
		prontuarioMedicoRepository.save(prontuario);
	}


	public Object buscarPorId(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
