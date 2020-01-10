package com.mballem.curso.security.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mballem.curso.security.datatables.Datatables;
import com.mballem.curso.security.datatables.DatatablesColunas;
import com.mballem.curso.security.domain.Perfil;
import com.mballem.curso.security.domain.Usuario;
import com.mballem.curso.security.repository.UsuarioRepository;
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

	@Transactional
	public Usuario buscarPorEmail(String email) {
		return usuarioRepository.findByEmail(email);
	}
	
	/**
	 * Metodo para verificar o usuario e senha que esta sendo utilizado para fazer login.
	 */
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = buscarPorEmail(username);
		return new User(

				usuario.getEmail(), usuario.getSenha(),
				AuthorityUtils.createAuthorityList(getAtuthorities(usuario.getPerfis())));
	}

	private String[] getAtuthorities(List<Perfil> perfis) {
		String[] authorities = new String[perfis.size()];
		for (int i = 0; i < perfis.size(); i++) {
			authorities[i] = perfis.get(i).getDesc();
		}
		return authorities;
	}
	/**
	 * Metodos para buscar todos os usuarios no banco.
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
	 *  Metodo que salva o novo usuario cadastrado e criptografa a senha.
	 * @param usuario
	 */
	@Transactional
	public void salvarsuario(Usuario usuario) {
		String crypt = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(crypt);
		usuarioRepository.save(usuario);

	}
	/**
	 * @param id
	 * @return
	 */
	@Transactional
	public Usuario buscarPorId(Long id) {
		// TODO Auto-generated method stub
		return usuarioRepository.findById(id).get();
	}
	/**
	 * 
	 * @param usuarioId
	 * @param perfisId
	 * @return
	 */
	@Transactional
	public Usuario buscarPorIdEPerfis(Long usuarioId, Long[] perfisId) {
		// TODO Auto-generated method stub
		return usuarioRepository.findByIdEPerfis(usuarioId, perfisId)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario inexistente !"));
	}
	/**
	 * 
	 * @param senhaDigitada
	 * @param senhaCadastradaNoBanco
	 * @return
	 */
	public static boolean isSenhaCorreta(String senhaDigitada, String senhaCadastradaNoBanco) {
		// TODO Auto-generated method stub
		return new BCryptPasswordEncoder().matches(senhaDigitada, senhaCadastradaNoBanco);
	}
	/**
	 * 
	 * @param usuario
	 * @param senha
	 */
	@Transactional
	public void alterarSenha(Usuario usuario, String senha) {
		usuario.setSenha(new BCryptPasswordEncoder().encode(senha));
		usuarioRepository.save(usuario);
	}

}
