package com.mballem.curso.security.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mballem.curso.security.domain.Agendamento;
import com.mballem.curso.security.domain.Prontuario;
import com.mballem.curso.security.repository.ProntuarioMedicoRepository;
import com.mballem.curso.security.web.Exception.AcessoNegadoException;

@Service
public class ProntuarioMedicoService {
	
	@Autowired
	private ProntuarioMedicoRepository prontuarioMedicoRepository;
	
	
	@Transactional
	public void salvar(Prontuario prontuario) {
		prontuarioMedicoRepository.save(prontuario);
	}


	public Prontuario buscarPorIdEUsuario(Long id) {
		// TODO Auto-generated method stub
		return prontuarioMedicoRepository.findProntuarioById(id);
	}


}
