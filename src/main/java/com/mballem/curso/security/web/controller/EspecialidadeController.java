package com.mballem.curso.security.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mballem.curso.security.domain.Especialidade;
import com.mballem.curso.security.service.EspecialidadeService;
/**
 * 
 * @author leonardoangelo
 *
 */
@Controller
@RequestMapping("especialidades")
public class EspecialidadeController {
	
	@Autowired
	private EspecialidadeService especialidadeService;
	/**
	 * Metodo para abrir a pagina de especialidades.
	 * @param especialidade
	 * @return
	 */
	@GetMapping({"", "/"})
	public String abrir(Especialidade especialidade) {
		return "especialidade/especialidade";
	}
	
	/**
	 * Metodo para cadastrar uma especialidade na clinica.
	 * @param especialidade
	 * @param redirect
	 * @return
	 */
	@PostMapping("/salvar")
	public String salvar(Especialidade especialidade, RedirectAttributes redirect) {
		especialidadeService.salvar(especialidade);
		redirect.addFlashAttribute("sucesso", "Operação realizado com sucesso !");
		return "redirect:/especialidades";
	}
	
	/**
	 * Metodo para buscar todas especialidade do banc
	 * @param request
	 * @return
	 */
	@GetMapping("/datatables/server")
	public ResponseEntity<?> getEspecialidades(HttpServletRequest request){
		return ResponseEntity.ok(especialidadeService.buscarEspecialidades(request));
	}
	
	/**
	 * Metodo para editar uma especialidade cadastrada na clinica.
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") Long id, ModelMap model) {
		model.addAttribute("especialidade", especialidadeService.buscarPorId(id));
		return "especialidade/especialidade";
	}

	
	/**
	 * Metodo para excluir uma especialidade.
	 * @param id
	 * @param redirect
	 * @return
	 */
	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, RedirectAttributes redirect) {
		especialidadeService.remover(id);
		redirect.addFlashAttribute("sucesso", "Operação realizada com sucesso");
		return "redirect:/especialidades";
	}
	
	/**
	 * Metodo para filtrar especialidades por caracteres.
	 * @param termo
	 * @return
	 */
	@GetMapping("/titulo")
	public ResponseEntity<?> getEspecialidadesPorTermo(@RequestParam("termo") String termo){
		List<String> especialidades = especialidadeService.buscarEspecialidadeByTermo(termo);
		return ResponseEntity.ok(especialidades);
	}
	
	/**
	 * Metodo para buscar a especialidade pelo id do medico.
	 * @param id
	 * @param request
	 * @return
	 */
	@GetMapping("/datatables/server/medico/{id}")
	public ResponseEntity<?> getEspecialidadesPorMedico(@PathVariable("id") Long id, HttpServletRequest request){
		return ResponseEntity.ok(especialidadeService.buscarEspecialidadesPorMedico(id,request));
	}
	
	
}