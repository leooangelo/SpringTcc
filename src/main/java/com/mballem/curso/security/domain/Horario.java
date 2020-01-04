package com.mballem.curso.security.domain;

import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity
@Table(name = "horas", indexes = {@Index(name = "idx_hora_minuto", columnList = "hora_minuto")})
@Getter
@Setter
public class Horario extends AbstractEntity {
	
	@Column(name = "hora_minuto", unique = true, nullable = false)
	private LocalTime horaMinuto;
}
