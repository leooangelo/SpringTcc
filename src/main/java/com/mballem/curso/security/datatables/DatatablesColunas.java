package com.mballem.curso.security.datatables;
/**
 * 
 * @author leonardoangelo
 * 
 *	Classe de configuração das colunas do banco.
 */
public class DatatablesColunas {

	public static final String[] ESPECIALIDADES = {"id", "titulo"};

	public static final String[] MEDICOS = {"id", "nome", "crm", "dtInscricao", "especialidades"};
	
	public static final String[] AGENDAMENTOS = {"id", "paciente.nome", "dataConsulta", "medico.nome", "especialidade.titulo"};

	public static final String[] USUARIOS = {"id", "email", "ativo", "perfis"};	
	
	public static final String[] PRONTUARIOS = {"id","descricao","paciente.nome"};
}
