package com.mballem.curso.security.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * 
 * @author leonardoangelo
 *
 *	Classe de criação e congifuração de um usuário, esta classe usa lombok em Getter, Setter e no construtor
 *	padrão.
 */
@Getter
@Setter
@SuppressWarnings("serial")
@Entity
@Table(name = "usuarios", indexes = {@Index(name = "idx_usuario_email", columnList = "email")})
@NoArgsConstructor
public class Usuario extends AbstractEntity {	
	//get set lombok
	@Column(name = "email", unique = true, nullable = false)
	private String email;

	@Column(name = "senha", nullable = false)
	private String senha;

	@ManyToMany
	@JoinTable(
		name = "usuarios_tem_perfis", 
        joinColumns = { @JoinColumn(name = "usuario_id", referencedColumnName = "id") }, 
        inverseJoinColumns = { @JoinColumn(name = "perfil_id", referencedColumnName = "id") }
	)
	
	private List<Perfil> perfis;
	
	
	@Column(name = "ativo", nullable = false, columnDefinition = "TINYINT(1)")
	private boolean ativo;
	
	@Column(name = "codigo_verificador", length = 6)
	private String codigoVerificador;

	public Usuario(Long id) {
		super.setId(id);
	}
		
	

	/**
	 * Metodo que adiciona perfis a lista.
	 * @param tipo
	 */
	public void addPerfil(PerfilTipo tipo) {
		if (this.perfis == null) {
			this.perfis = new ArrayList<>();
		}
		this.perfis.add(new Perfil(tipo.getCod()));
	}

	public Usuario(String email) {
		this.email = email;
	}


}
