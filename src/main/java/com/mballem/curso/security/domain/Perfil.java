package com.mballem.curso.security.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * 
 * @author leonardoangelo
 *
 *	Classe Perfil esta classe esta associadada ao tipo de perfil de cada usuario, esta classe usa lombok
 *	em Getter, Settere e construtor padrão.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "perfis")
@Getter
@Setter
@NoArgsConstructor
public class Perfil extends AbstractEntity {
	
	@Column(name = "descricao", nullable = false, unique = true)
	private String desc;

	public Perfil(Long id) {
		super.setId(id);
	}

	
}
