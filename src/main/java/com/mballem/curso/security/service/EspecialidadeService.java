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
/**
 * 
 * @author leonardoangelo
 *
 */

@Service
public class EspecialidadeService {
	
	/**
	 * Instância de outras classe.
	 */
	@Autowired
	private EspecialidadeRepository especialidadeRepository;
	
	@Autowired Datatables dataTables;
	public Object buscarEspecialidade;
	
	/**
	 * Metodo para salvar uma especialidade cadastrada.
	 * @param especialidade
	 */
	@Transactional
	public void salvar(Especialidade especialidade) {
		especialidadeRepository.save(especialidade);	
	}
	/**
	 * Metodo para buscar especialidades cadastradados no banco.
	 * @param request
	 * @return
	 * 
	 */
	@Transactional
	public Map<String, Object> buscarEspecialidades(HttpServletRequest request) {
		dataTables.setRequest(request);
		dataTables.setColunas(DatatablesColunas.ESPECIALIDADES);
		Page<?> page = dataTables.getSearch().isEmpty()
				? especialidadeRepository.findAll(dataTables.getPageable())
						: especialidadeRepository.findAllByTitulo(dataTables.getSearch(), dataTables.getPageable());
				
		return dataTables.getResponse(page);
	}
	/**
	 * Metodo para buscar especialidade pelo id cadastrado no banco.
	 * @param id
	 * @return
	 */
	@Transactional
	public Especialidade buscarPorId(Long id) {
		return especialidadeRepository.findById(id).get();
	}
	/**
	 * Metodo para remover uma especialidade cadastrada.
	 * @param id
	 */
	@Transactional
	public void remover(Long id) {
		especialidadeRepository.deleteById(id);
		
	}
	/**
	 * Metodo para buscar uma especialidade pelo termo que é digitado na hora de cadastrar uma especialidade
	 * para o medico caso esta especialidade ja esteja cadastrada no banco.
	 * @param termo
	 * @return
	 */
	@Transactional
	public List<String> buscarEspecialidadeByTermo(String termo) {
		// TODO Auto-generated method stub
		return  especialidadeRepository.findEspecialidadesByTermo(termo);
	}
	/**
	 * Metodo para buscar uma especialidade pelo nome completo da mesma.
	 * @param titulos
	 * @return
	 */
	@Transactional
	public Set<Especialidade> buscarPorTitulos(String[] titulos) {
		// TODO Auto-generated method stub
		return especialidadeRepository.findByTitulos(titulos);
	}
	/**
	 * Metodo para que o medico busque especialidades caso queira alterar alguma especialidade em seu cadastro.
	 * @param id
	 * @param request
	 * @return
	 */
	@Transactional
	public Map<String, Object> buscarEspecialidadesPorMedico(Long id, HttpServletRequest request) {
		dataTables.setRequest(request);
		dataTables.setColunas(DatatablesColunas.ESPECIALIDADES);
		Page<Especialidade> page = especialidadeRepository.findByIdMedico(id, dataTables.getPageable());
		return dataTables.getResponse(page);
	}
	
	@Transactional
	public Long buscarQuantidadeEspecialidade() {
		return especialidadeRepository.findByQuantidade();
	}

	
	
	
	
}
