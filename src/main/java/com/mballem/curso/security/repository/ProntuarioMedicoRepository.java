package com.mballem.curso.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mballem.curso.security.domain.ProntuarioMedico;

public interface ProntuarioMedicoRepository extends JpaRepository<ProntuarioMedico, Long> {

}
