package com.mballem.curso.security.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mballem.curso.security.domain.Prontuario;
import com.mballem.curso.security.repository.ProntuarioRepository;
@Service
public class ProntuarioService {
	
	@Autowired
	private ProntuarioRepository prontuarioRepository;
	
	@Transactional
	public void salvar(Prontuario prontuario) {
		prontuarioRepository.save(prontuario);
	}
	
	@Transactional
	public void editar(Prontuario prontuario) {
		Prontuario pt = buscarProntuarioPorId(prontuario.getId());
		pt.setDescricao(prontuario.getDescricao());
	}
	
	@Transactional
	public Prontuario buscarProntuarioPorId(Long id) {
		return prontuarioRepository.findProntuarioById(id);
	}
	
	
}
