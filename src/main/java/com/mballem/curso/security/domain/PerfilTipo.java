package com.mballem.curso.security.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * 
 * @author leonardoangelo
 *
 *	Classe que define os tipos de perfis existentes na aplicação, esta classe usa lombok em Getter e no
 *	construtor com todos os argumentos.
 */
@Getter
@AllArgsConstructor
public enum PerfilTipo {
	ADMIN(1, "ADMIN"), MEDICO(2, "MEDICO"), PACIENTE(3, "PACIENTE");
	
	private long cod;
	private String desc;
}
