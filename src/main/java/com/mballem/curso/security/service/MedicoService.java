package com.mballem.curso.security.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mballem.curso.security.domain.Medico;
import com.mballem.curso.security.repository.MedicoRepository;

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
		
		if(!medico.getEspecialidades().isEmpty()) 
					medic.getEspecialidades().addAll(medico.getEspecialidades());
		
	}
}
