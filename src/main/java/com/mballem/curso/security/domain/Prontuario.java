package com.mballem.curso.security.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity
@Table(name = "prontuario")
@Getter
@Setter
@NoArgsConstructor
public class Prontuario extends AbstractEntity{
	
	@Column(name = "descricao", columnDefinition = "TEXT")
	private String descricao;
	
	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "id_paciente")
	private Paciente paciente;
}
