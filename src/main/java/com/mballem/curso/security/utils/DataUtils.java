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
	
}
