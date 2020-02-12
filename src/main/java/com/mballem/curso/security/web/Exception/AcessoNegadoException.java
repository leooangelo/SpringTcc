package com.mballem.curso.security.web.Exception;

/**
 * 
 * @author leonardoangelo
 *
 */
public class AcessoNegadoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param message
	 */
	public AcessoNegadoException(String message) {
		super(message);

	}

}
