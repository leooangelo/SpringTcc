package com.mballem.curso.security.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import com.mballem.curso.security.datatables.Datatables;
import com.mballem.curso.security.datatables.DatatablesColunas;
import com.mballem.curso.security.domain.Perfil;
import com.mballem.curso.security.domain.PerfilTipo;
import com.mballem.curso.security.domain.Usuario;
import com.mballem.curso.security.repository.UsuarioRepository;
import com.mballem.curso.security.web.Exception.AcessoNegadoException;

/**
 * 
 * @author leonardoangelo
 *
 */
@Service
public class UsuarioService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private Datatables datatables;

	@Autowired
	private EmailService emailService;

	@Transactional
	public Usuario buscarPorEmail(String email) {
		return usuarioRepository.findByEmail(email);
	}

	/**
	 * Metodo para verificar o usuario e senha que esta sendo utilizado para fazer
	 * login.
	 */
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = buscarPorEmailEAtivo(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario" + username + "não encontrado."));
		return new User(

				usuario.getEmail(), usuario.getSenha(),
				AuthorityUtils.createAuthorityList(getAtuthorities(usuario.getPerfis())));
	}

	/**
	 * Metodo que lista todos os tipos de perfil da aplicação e retorna as
	 * autoridades que cada tipo de perfil tem.
	 * 
	 * @param perfis
	 * @return
	 */
	private String[] getAtuthorities(List<Perfil> perfis) {
		String[] authorities = new String[perfis.size()];
		for (int i = 0; i < perfis.size(); i++) {
			authorities[i] = perfis.get(i).getDesc();
		}
		return authorities;
	}

	/**
	 * Metodos para buscar todos os usuarios no banco.
	 * 
	 * @param request
	 * @return
	 */
	@Transactional
	public Map<String, Object> buscarTodos(HttpServletRequest request) {
		datatables.setRequest(request);
		datatables.setColunas(DatatablesColunas.USUARIOS);
		Page<Usuario> page = datatables.getSearch().isEmpty() ? usuarioRepository.findAll(datatables.getPageable())
				: usuarioRepository.findByEmailOrPerfil(datatables.getSearch(), datatables.getPageable());
		return datatables.getResponse(page);
	}

	/**
	 * Metodo que salva o novo usuario cadastrado e criptografa a senha.
	 * 
	 * @param usuario
	 */
	@Transactional
	public void salvarsuario(Usuario usuario) {

		String crypt = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(crypt);
		usuarioRepository.save(usuario);

	}

	/**
	 * Metodo para fazer buscar por Id.
	 * 
	 * @param id
	 * @return
	 */
	@Transactional
	public Usuario buscarPorId(Long id) {
		return usuarioRepository.findById(id).get();
	}

	/**
	 * Metodo para buscar por Id e o tipo de perfil.
	 * 
	 * @param usuarioId
	 * @param perfisId
	 * @return
	 */
	@Transactional
	public Usuario buscarPorIdEPerfis(Long usuarioId, Long[] perfisId) {
		return usuarioRepository.findByIdEPerfis(usuarioId, perfisId)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario inexistente !"));
	}

	/**
	 * Metodo que verifica se a senha digitada na sistema é a mesma senha que já
	 * esta cadastada no banco de dados.
	 * 
	 * @param senhaDigitada
	 * @param senhaCadastradaNoBanco
	 * @return
	 */
	public static boolean isSenhaCorreta(String senhaDigitada, String senhaCadastradaNoBanco) {
		return new BCryptPasswordEncoder().matches(senhaDigitada, senhaCadastradaNoBanco);
	}

	/**
	 * Metodo para criptografar a senha do usuário que foi alterada e salvar no
	 * banco de dados.
	 * 
	 * @param usuario
	 * @param senha
	 */
	@Transactional
	public void alterarSenha(Usuario usuario, String senha) {
		usuario.setSenha(new BCryptPasswordEncoder().encode(senha));
		usuarioRepository.save(usuario);
	}

	/**
	 * Metodo que recebe o form de cadastro, faz a criptogradia da sua senha
	 * cadastrada, da um tipo de perfil paciente para este novo usuário e o salva no
	 * banco.
	 * 
	 * @param usuario
	 * @throws MessagingException
	 */
	@Transactional
	public void salvarCadastroPaciente(Usuario usuario) throws MessagingException {
		String crypt = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(crypt);
		usuario.addPerfil(PerfilTipo.PACIENTE);
		usuarioRepository.save(usuario);

		emailDeConfirmacaoCadastro(usuario.getEmail());

	}

	/**
	 * Metodo que buscar o email e o numero do ativo do usuario.
	 * 
	 * @param email
	 * @return
	 */
	@Transactional
	public Optional<Usuario> buscarPorEmailEAtivo(String email) {
		return usuarioRepository.findByEmailAndAtivo(email);
	}

	/**
	 * Metodo que envia que faz a chamada do email service para enviar o email de
	 * confirmação.
	 * 
	 * @param email
	 * @throws MessagingException
	 */
	public void emailDeConfirmacaoCadastro(String email) throws MessagingException {
		String codigo = Base64Utils.encodeToString(email.getBytes());
		emailService.enviarPedidoDeConfirmacaoCadastro(email, codigo);
	}

	/**
	 * Este metodo ativa o cadastro do paciente pelo codigo que é recebido via email
	 * de confirmação de cadastro. O código que é recebido ele é gerado pelo próprio
	 * email do usuario transformado para a base 64.
	 * 
	 * @param codigo
	 */
	@Transactional
	public void ativarCadastroPaciente(String codigo) {
		String email = new String(Base64Utils.decodeFromString(codigo));
		Usuario usuario = buscarPorEmail(email);
		if (usuario.hasNotId()) {
			throw new AcessoNegadoException("Não foi possível ativar seu cadastro. Entre em contato com o suporte.");
		}
		usuario.setAtivo(true);
	}
	/**
	 * Metodo que realiza o update da senha enviando codigo verificado la na tabela usuarios.
	 * @param email
	 * @throws MessagingException 
	 */
	@Transactional
	public void pedidoRedefinicaoDeSenha(String email) throws MessagingException {
		Usuario usuario = buscarPorEmailEAtivo(email).orElseThrow(
				() -> new UsernameNotFoundException("Usuario" + email + "não encontrado."));
		String verificador = RandomStringUtils.randomNumeric(6);
		usuario.setCodigoVerificador(verificador);
		
		emailService.enviarPedidoRedefinirSenha(email, verificador);
	}
	
	@Transactional
	public Map<String, Object> buscarConsultas(HttpServletRequest request) {
		datatables.setRequest(request);
		datatables.setColunas(DatatablesColunas.AGENDAMENTOS);
		Page<Usuario> page = datatables.getSearch().isEmpty() ? usuarioRepository.findAll(datatables.getPageable())
				: usuarioRepository.findByEmailOrPerfil(datatables.getSearch(), datatables.getPageable());
		return datatables.getResponse(page);
	}
	
	@Transactional
	public void remove(Long id) {
		usuarioRepository.deleteById(id);
		
	}


}
