package com.mballem.curso.security.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
	@Transactional
	public List<String> buscarEspecialidadeByTermo(String termo) {
		// TODO Auto-generated method stub
		return  especialidadeRepository.findEspecialidadesByTermo(termo);
	}
	@Transactional
	public Set<Especialidade> buscarPorTitulos(String[] titulos) {
		// TODO Auto-generated method stub
		return especialidadeRepository.findByTitulos(titulos);
	}
	@Transactional
	public Map<String, Object> buscarEspecialidadesPorMedico(Long id, HttpServletRequest request) {
		dataTables.setRequest(request);
		dataTables.setColunas(DatatablesColunas.ESPECIALIDADES);
		Page<Especialidade> page = especialidadeRepository.findByIdMedico(id, dataTables.getPageable());
		return dataTables.getResponse(page);
	}
}
