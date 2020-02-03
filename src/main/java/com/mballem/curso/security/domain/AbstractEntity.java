package com.mballem.curso.security.domain;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * 
 * @author leonardoangelo
 * 
 *	Classe de configuração de HashCode e Equals, esta classe usa lombok getter e setter para id.
 */
@SuppressWarnings("serial")
@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public abstract class AbstractEntity implements Serializable  {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	public boolean hasNotId() {
		return id == null;
	}

	public boolean hasId() {
		return id != null;
	}

	@Override
	public String toString() {
		return String.format("Entidade %s id: %s", this.getClass().getName(), getId());
	}

}
