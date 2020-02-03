package com.mballem.curso.security.web.Exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * @author leonardoangelo
 *
 */
@ControllerAdvice
public class ExceptionController {
	/**
	 * Metodo para lançar um excessão quando não encontra um usuário ou quando um
	 * usuario tenta acessar páginas que não tem permissão.
	 * 
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(UsernameNotFoundException.class)
	public ModelAndView usuarioNaoEncontradoException(UsernameNotFoundException ex) {
		ModelAndView model = new ModelAndView("error");

		model.addObject("status", 404);
		model.addObject("error", "Operação não pode ser realizada");
		model.addObject("message", ex.getMessage());
		return model;
	}
}
