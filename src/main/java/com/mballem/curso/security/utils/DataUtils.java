package com.mballem.curso.security.utils;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneOffset;

public class DataUtils {

	public Long dataConsulta(LocalDate dataConsulta) throws ParseException { 
		dataConsulta.toString();
		Long data = dataConsulta.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
		Long dataFinal = data - 1728000000L;
		return dataFinal;
	}
	public Long dataMarcarConsulta(LocalDate dataConsulta) throws ParseException { 
		dataConsulta.toString();
		Long data = dataConsulta.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
		Long dataFinal = data ;		
		return dataFinal;
	}
	
//	public Long dataNascimento(LocalDate dataNascimento) {
//		dataNascimento.toString();
//		Long data = dataNascimento.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
//		Long dataFinal = data - 23652000000L;
//		return dataFinal;
//
//	}
	
}
