package com.mballem.curso.security.web.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mballem.curso.security.domain.Medico;
import com.mballem.curso.security.domain.Perfil;
import com.mballem.curso.security.domain.PerfilTipo;
import com.mballem.curso.security.domain.Usuario;
import com.mballem.curso.security.service.MedicoService;
import com.mballem.curso.security.service.UsuarioService;

/**
 * 
 * @author leonardoangelo
 *
 */
@Controller
@RequestMapping("u")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private MedicoService medicoService;

	/**
	 * Metodo que abre o cadastro de usuarios(medico,admin e paciente)
	 * 
	 * @param usuario
	 * @return
	 */
	@GetMapping("/novo/cadastro/usuario")
	public String cadastroPorAdminParaAdminMedicoPaciente(Usuario usuario) {
		return "usuario/cadastro";
	}

	/**
	 * Metodo que abre a lista de usuários
	 * 
	 * @return
	 */
	@GetMapping("/lista")
	public String listarUsuarios() {
		return "usuario/lista";
	}

	/**
	 * Metodo para Listar todos os usuarios na tela.
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/datatables/server/usuarios")
	public ResponseEntity<?> listarUsuariosDatatables(HttpServletRequest request) {

		return ResponseEntity.ok(usuarioService.buscarTodos(request));
	}

	/**
	 * Metodo para salvar o cadastro de um novo usuario feito pelo usuario admin.
	 * 
	 * @param usuario
	 * @param attr
	 * @return
	 */
	@PostMapping("/cadastro/salvar")
	public String salvarUsuario(Usuario usuario, RedirectAttributes attr) {
		List<Perfil> perfis = usuario.getPerfis();
		if (perfis.size() > 2
				|| perfis.containsAll(
						Arrays.asList(new Perfil(PerfilTipo.ADMIN.getCod()), new Perfil(PerfilTipo.PACIENTE.getCod())))
				|| perfis.containsAll(Arrays.asList(new Perfil(PerfilTipo.MEDICO.getCod()),
						new Perfil(PerfilTipo.PACIENTE.getCod())))) {
			attr.addFlashAttribute("falha", "Paciente não pode ser admin e/ou médico.");
			attr.addFlashAttribute("usuario", usuario);
		} else {
			try {
				usuarioService.salvarsuario(usuario);
				attr.addFlashAttribute("sucesso", "Operação realizada com sucesso !");
			} catch (DataIntegrityViolationException ex) {
				attr.addFlashAttribute("falha", "Este e-mail já esta cadastrado no sistema");
			}
		}
		return "redirect:/u/novo/cadastro/usuario";

	}

	/**
	 * Metodo para pre editar credenciais do usuario.
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/editar/credenciais/usuario/{id}")
	public ModelAndView preEditarCredenciais(@PathVariable("id") Long id) {

		return new ModelAndView("usuario/cadastro", "usuario", usuarioService.buscarPorId(id));
	}

	/**
	 * Metodo para pre editar dados pessoais de usuarios.
	 * 
	 * @param usuarioId
	 * @param perfisId
	 * @return
	 */
	@GetMapping("/editar/dados/usuario/{id}/perfis/{perfis}")
	public ModelAndView preEditarCadastroDadosPessoais(@PathVariable("id") Long usuarioId,
			@PathVariable("perfis") Long[] perfisId) {

		Usuario us = usuarioService.buscarPorIdEPerfis(usuarioId, perfisId);
		if (us.getPerfis().contains(new Perfil(PerfilTipo.ADMIN.getCod()))
				&& !us.getPerfis().contains(new Perfil(PerfilTipo.MEDICO.getCod())))
			return new ModelAndView("usuario/cadastro", "usuario", us);

		else if (us.getPerfis().contains(new Perfil(PerfilTipo.MEDICO.getCod()))) {

			Medico medico = medicoService.buscarPorUsuarioId(usuarioId);
			return medico.hasNotId() ? new ModelAndView("medico/cadastro", "medico", new Medico(new Usuario(usuarioId)))
					: new ModelAndView("medico/cadastro", "medico", medico);

		} else if (us.getPerfis().contains(new Perfil(PerfilTipo.PACIENTE.getCod()))) {
			ModelAndView model = new ModelAndView("error");

			model.addObject("status", 403);
			model.addObject("error", "Área Restrita");
			model.addObject("message", "Os dados de pacientes são restritos ");
			return model;
		}

		return new ModelAndView("redirect:/u/lista");
	}

	/**
	 * Metodo para abrir a page de editar senha
	 * 
	 * @return
	 */
	@GetMapping("/editar/senha")
	public String abrirEditarSenhaPage() {
		return "usuario/editar-senha";
	}

	/**
	 * Metodo para editar a senha e receber o submit da page.
	 * 
	 * @param senha1
	 * @param senha2
	 * @param senha3
	 * @param user
	 * @param attr
	 * @return
	 */
	@PostMapping("/confirmar/senha")
	public String editarSenha(@RequestParam("senha1") String senha1, @RequestParam("senha2") String senha2,
			@RequestParam("senha3") String senha3, @AuthenticationPrincipal User user, RedirectAttributes attr) {

		if (!senha1.equals(senha2)) {
			attr.addFlashAttribute("falha", "Senhas não conferem, tente novamente.");
			return "redirect:/u/editar/senha";
		}
		Usuario usuario = usuarioService.buscarPorEmail(user.getUsername());
		if (!UsuarioService.isSenhaCorreta(senha3, usuario.getSenha())) {
			attr.addFlashAttribute("falha", "Senha atual não confere, tente novamente.");
			return "redirect:/u/editar/senha";
		}

		usuarioService.alterarSenha(usuario, senha1);
		attr.addFlashAttribute("sucesso", "Senha alterada com sucesso. !");
		return "redirect:/u/editar/senha";

	}

}
