package com.mballem.curso.security.web.conversor;

import java.util.ArrayList;
import java.util.List;

import com.mballem.curso.security.domain.Agendamento;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PontoVO {
	
	private Long y;
	
	private List<Agendamento> x = new ArrayList<Agendamento>();
}
