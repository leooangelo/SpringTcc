package com.mballem.curso.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mballem.curso.security.domain.Prontuario;

public interface ProntuarioRepository extends JpaRepository<Prontuario, Long> {
	
	
	@Query("select id from Prontuario where id = :id ")
	Prontuario findProntuarioById(Long id);



}
