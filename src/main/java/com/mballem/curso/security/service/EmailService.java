package com.mballem.curso.security.service;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.mballem.curso.security.domain.Especialidade;
import com.mballem.curso.security.domain.Horario;
import com.mballem.curso.security.domain.Medico;
import com.mballem.curso.security.repository.AgendamentoRepository;
import com.mballem.curso.security.repository.MedicoRepository;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private SpringTemplateEngine template;

	@Autowired
	private MedicoRepository medicoRepository;

	@Autowired
	private AgendamentoRepository agendamentoRepository;

	public void enviarPedidoDeConfirmacaoCadastro(String destino, String codigo) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				"UTF-8");

		Context context = new Context();
		context.setVariable("titulo", "Bem vindo a Clíniica Spring Security");
		context.setVariable("texto", "Precisamos que confirme seu cadastro, clicando neste link abaixo.");
		context.setVariable("linkConfirmacao", "http://localhost:8080/u/confirmacao/cadastro?codigo=" + codigo);

		String html = template.process("email/confirmacao", context);
		helper.setTo(destino);
		helper.setText(html, true);
		helper.setSubject("Confirmção de Cadastro");
		helper.setFrom("nao-responder@clinica.com.br");

		helper.addInline("logo", new ClassPathResource("/static/image/spring-security.png"));

		mailSender.send(message);
	}

	public void enviarPedidoRedefinirSenha(String destino, String verificador) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				"UTF-8");

		Context context = new Context();
		context.setVariable("titulo", "Redefinição de Senha");
		context.setVariable("texto",
				"Para redefinir sua senha use o código de verificação quando exigido no formulario");
		context.setVariable("verificador", verificador);

		String html = template.process("email/confirmacao", context);
		helper.setTo(destino);
		helper.setText(html, true);
		helper.setSubject("Redefinição de Senha");
		helper.setFrom("nao-replay@clinica.com.br");

		helper.addInline("logo", new ClassPathResource("/static/image/spring-security.png"));

		mailSender.send(message);
	}
	/**
	 * Metodo que cria a estrutura do email e o envia a menssagem feita.
	 * @param username
	 * @param especialidade
	 * @param medico
	 * @param dataConsulta
	 * @param horario
	 * @throws MessagingException
	 */
	public void enviarConfirmacaoConsulta(String username, Especialidade especialidade, Medico medico,
			LocalDate dataConsulta, Horario horario) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				"UTF-8");

		Context context = new Context();
		context.setVariable("titulo", "Bem vindo a Clíniica Spring Security");
		context.setVariable("texto", "Informações de consulta Agendada.");
		context.setVariable("especialidade", especialidade.getTitulo());
		context.setVariable("medico", buscaNomeMedico(medico.getId()));
		context.setVariable("dataConsulta", dataConsulta);
		context.setVariable("horario", horaConsulta(horario.getId()));

		String html = template.process("email/confirmacao_agendamento", context);
		helper.setTo(username);
		helper.setText(html, true);
		helper.setSubject("Consulta Agendada");
		helper.setFrom("nao-replay@clinica.com.br");

		helper.addInline("logo", new ClassPathResource("/static/image/spring-security.png"));

		mailSender.send(message);

	}
	/**
	 * Metodo que recebe o nome do medico pelo id que vem da consulta agendada.
	 * @param id
	 * @return
	 */
	public String buscaNomeMedico(Long id) {
		String nomeMedico = medicoRepository.buscaNomeMedico(id);
		return nomeMedico;
	}
	
	/**
	 * Metodo que traz a hora da consulta pelo id que vem da consulta agendada.
	 * @param id
	 * @return
	 */
	public LocalTime horaConsulta(Long id) {
		LocalTime horaConsulta = agendamentoRepository.buscaHoraConsulta(id);
		return horaConsulta;
	}

}
