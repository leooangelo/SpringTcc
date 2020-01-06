package com.mballem.curso.security.web.Exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
// esta anotação fica esperando alguma regra ser veradeira, regras da classe
import org.springframework.web.servlet.ModelAndView;
@ControllerAdvice
public class ExceptionController {
	@ExceptionHandler(UsernameNotFoundException.class)
	public ModelAndView usuarioNaoEncontradoException(UsernameNotFoundException ex) {
		ModelAndView model = new ModelAndView("error");
		
		model.addObject("status", 404);
		model.addObject("error", "Operação não pode ser realizada");
		model.addObject("message", ex.getMessage() );
		return model;
	}
}
