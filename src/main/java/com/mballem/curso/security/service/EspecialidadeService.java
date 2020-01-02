package com.mballem.curso.security.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.mballem.curso.security.datatables.Datatables;
import com.mballem.curso.security.datatables.DatatablesColunas;
import com.mballem.curso.security.domain.Especialidade;
import com.mballem.curso.security.repository.EspecialidadeRepository;

@Service
public class EspecialidadeService {

	@Autowired
	private EspecialidadeRepository especialidadeRepository;
	@Autowired Datatables dataTables;
	public Object buscarEspecialidade;
	
	@Transactional
	public void salvar(Especialidade especialidade) {
		especialidadeRepository.save(especialidade);	
	}
	@Transactional
	public	Map<String, Object> buscarEspecialidades(HttpServletRequest request) {
		dataTables.setRequest(request);
		dataTables.setColunas(DatatablesColunas.ESPECIALIDADES);
		Page<?> page = dataTables.getSearch().isEmpty()
				? especialidadeRepository.findAll(dataTables.getPageable())
						: especialidadeRepository.findAllByTitulo(dataTables.getSearch(), dataTables.getPageable());
				
		return dataTables.getResponse(page);
	}
	
	@Transactional
	public Especialidade buscarPorId(Long id) {
		return especialidadeRepository.findById(id).get();
	}
	@Transactional
	public void remover(Long id) {
		especialidadeRepository.deleteById(id);
		
	}
}
