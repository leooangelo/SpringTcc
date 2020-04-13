package com.mballem.curso.security.web.controller;

import java.util.Arrays;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mballem.curso.security.domain.Medico;
import com.mballem.curso.security.domain.Paciente;
import com.mballem.curso.security.domain.Perfil;
import com.mballem.curso.security.domain.PerfilTipo;
import com.mballem.curso.security.domain.Usuario;
import com.mballem.curso.security.service.MedicoService;
import com.mballem.curso.security.service.PacienteService;
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
	
	@Autowired
	private PacienteService pacienteService;


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
	 * Metodo para excluir um usuário cadastrado no banco.
	 * @param id
	 * @param attr
	 * @return
	 */
	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {
		usuarioService.remove(id);
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso !");
		return "redirect:/u/lista";
		
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
	
	//teste lista de especialidade e descrição ainda a fazer.
	
	@GetMapping("/lista-especialidades")
	public String listarEspecialidadesEDesc(){
		return "epecialidade/lista-especialidades";
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
				|| perfis.containsAll(
						Arrays.asList(new Perfil(PerfilTipo.ADMIN.getCod()), new Perfil(PerfilTipo.MEDICO.getCod())))
				|| perfis.containsAll(Arrays.asList(new Perfil(PerfilTipo.MEDICO.getCod()),
						new Perfil(PerfilTipo.PACIENTE.getCod())))) {
			attr.addFlashAttribute("falha", "Paciente não pode ser admin e/ou médico.");
			attr.addFlashAttribute("usuario", usuario);
		} else {
			try {
				usuarioService.salvarUsuario(usuario);
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
	
	
	@GetMapping("/editar/prontuario/paciente/{id}")
	public ModelAndView preEditarProntuarioMédico(@PathVariable("id")Long usuarioId) {
		
		return new ModelAndView("usuario/prontuario", "usuario", usuarioService.buscarPorId(usuarioId));
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

			Paciente paciente = pacienteService.buscarPorUsuarioId(usuarioId);
			return paciente.hasNotId() ? new ModelAndView("paciente/cadastro", "paciente", new Paciente(new Usuario(usuarioId)))
					: new ModelAndView("paciente/cadastro", "paciente", paciente);

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

	/**
	 * Metodo que abre a pagina de cadastro de novo usuario
	 * 
	 * @param usuario
	 * @return
	 */
	@GetMapping("/novo/cadastro")
	public String novoCadastro(Usuario usuario) {
		
		
		return "cadastrar-se";
	}

	/**
	 * Metodo que confirma um novo usuario cadastrado
	 * 
	 * @return
	 */
	@GetMapping("/cadastro/realizado")
	public String cadastroRealizado() {

		return "fragments/mensagem";

	}
	
	/**
	 * Metodo que recebe o form de cadastro, usando BindingResult que sever para lançar a mensagem de erro quando
	 * tentam cadastrar um usuario ja cadastrado no sistema.
	 * @throws MessagingException 
	 */
	@PostMapping("/cadastro/paciente/salvar")
	public String salvaCadastroPaciente(Usuario usuario, BindingResult result) throws MessagingException {
		try {
		usuarioService.salvarCadastroPaciente(usuario);
		}catch(DataIntegrityViolationException ex) {
			result.reject("email","Ops... Este e-mail já existe na base de dados.");
			return"cadastrar-se";
		}
		return "redirect:/u/cadastro/realizado";
	}
	
	/**
	 * Metodo que recebe a resposta do usuario sobre a confirmação do cadastro do mesmo na plataforma.
	 * @param codigo 
	 * @param attr
	 * @return
	 */
	@GetMapping("/confirmacao/cadastro")
	public String respostaConfirmacaoCadastroPaciente(@RequestParam("codigo") String codigo, RedirectAttributes attr ) {
		
		usuarioService.ativarCadastroPaciente(codigo);
		attr.addFlashAttribute("alerta", "sucesso");
		attr.addFlashAttribute("titulo", "Cadastro realizado com sucesso");
		attr.addFlashAttribute("texto", "Parabéns, seu cadastro está ativo");
		attr.addFlashAttribute("subtexto", "Siga com seu login e senha");
		
		return "redirect:/login";
		
	}
	/**
	 * Metodo para abrir a pagina de recuperação de senha.
	 * @return
	 */
	@GetMapping("/p/redefinir/senha")
	private String pedidoParaRedefinirSenha() {
		
		return "usuario/pedido-recuperar-senha";
	}
	
	/**
	 * Metodo que abre o form de pedido de recuperação de senha.
	 * @throws MessagingException 
	 */
	@GetMapping("/p/recuperar/senha")
	public	String redefinirSenha(String email, ModelMap model) throws MessagingException {
		
	usuarioService.pedidoRedefinicaoDeSenha(email);
	model.addAttribute("sucesso","Em instantes você irá receber um email para prosseguir com  a redefinição de senha");
	model.addAttribute("usuario", new Usuario(email));
	return "usuario/recuperar-senha";		
		
	}
	
	/**
	 * Metodo que salva a nova senha cadastrada no momento que a recuperação de senha é finalizado.
	 * 
	 */
	@PostMapping("/p/nova/senha")
	public String confirmacaoDeRedefinicaoSenha(Usuario usuario, ModelMap model) {
		Usuario u = usuarioService.buscarPorEmail(usuario.getEmail());
		if(!usuario.getCodigoVerificador().equals(u.getCodigoVerificador())) {
			model.addAttribute("falha","Código verificador não confere");
			return "usuario/recuperar-senha";
		}
		u.setCodigoVerificador(null);
		usuarioService.alterarSenha(u, usuario.getSenha());
		model.addAttribute("alerta", "sucesso");
		model.addAttribute("titulo", "Senha foi redefinida com sucesso");
		model.addAttribute("texto", "Voce ja pode fazer login no sistema");
		return "login";
		
	}
	
	
}
