package com.mballem.curso.security.web.controller;

import java.text.ParseException;
import java.time.LocalDate;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mballem.curso.security.domain.Agendamento;
import com.mballem.curso.security.domain.Especialidade;
import com.mballem.curso.security.domain.Paciente;
import com.mballem.curso.security.domain.PerfilTipo;
import com.mballem.curso.security.service.AgendamentoService;
import com.mballem.curso.security.service.EmailService;
import com.mballem.curso.security.service.EspecialidadeService;
import com.mballem.curso.security.service.PacienteService;
import com.mballem.curso.security.utils.DataUtils;

/**
 * 
 * @author leonardoangelo
 *
 */
@Controller
@RequestMapping("agendamentos")
public class AgendamentoController {

	@Autowired
	private AgendamentoService agendamentoService;

	@Autowired
	private PacienteService pacienteService;

	@Autowired
	private EspecialidadeService especialidadesService;

	@Autowired
	private EmailService emailService;

	private DataUtils dataUtils = new DataUtils();

	@PreAuthorize("hasAnyAuthority('PACIENTE','MEDICO','ADMIN')")
	@GetMapping({ "/agendar" })
	public String agendarConsulta(Agendamento agendamento) {

		return "agendamento/cadastro";
	}

	/**
	 * Metodo para verificar os horarios disponiveis para efetuar uma consulta.
	 * 
	 * @param id
	 * @param data
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('PACIENTE','MEDICO','ADMIN')")
	@GetMapping("/horario/medico/{id}/data/{data}")
	public ResponseEntity<?> getHorariosDeConsulta(@PathVariable("id") Long id,
			@PathVariable("data") @DateTimeFormat(iso = ISO.DATE) LocalDate data) {
		return ResponseEntity.ok(agendamentoService.buscarHorariosNaoAgendadosPorMedicosIdEData(id, data));

	}

	/**
	 * Metodo para salvar um agendamento de consulta na clinica e envia um email
	 * para o email do usuario informado a consulta agendada.
	 * 
	 * @param agendamento
	 * @param attr
	 * @param user
	 * @return
	 * @throws MessagingException
	 * @throws ParseException 
	 */
	@PreAuthorize("hasAnyAuthority('PACIENTE','ADMIN')")

	@PostMapping({ "/salvar" })
	public String salvar(Agendamento agendamento, RedirectAttributes attr, @AuthenticationPrincipal User user)
			throws MessagingException, ParseException {
		
		
		
		Paciente paciente = pacienteService.buscarPorUsuarioEmail(user.getUsername());
		String titulo = agendamento.getEspecialidade().getTitulo();
		Especialidade especialidade = especialidadesService.buscarPorTitulos(new String[] { titulo }).stream()
				.findFirst().get();
		agendamento.setEspecialidade(especialidade);
		agendamento.setPaciente(paciente);
		
		Long dataConsulta = dataUtils.dataMarcarConsulta(agendamento.getDataConsulta());
		Long dataSistema = System.currentTimeMillis();
		if(dataConsulta >=dataSistema) {
		agendamentoService.salvarConsultaAgendada(agendamento);
		attr.addFlashAttribute("sucesso", "Sua consulta foi agendada com sucesso, verifique seu"
				+ "email com os dados da consulta agendada.");
		
		emailService.enviarConfirmacaoConsulta(user.getUsername(), agendamento.getEspecialidade(),
				agendamento.getMedico(), agendamento.getDataConsulta(), agendamento.getHorario());

		return "redirect:/agendamentos/agendar";
		}
		attr.addFlashAttribute("falha", "Uma consulta não pode ser agendada para qualquer outra data anterior a de hoje");
		
		return "redirect:/agendamentos/agendar";
	}

	/**
	 * Metodo para abrir uma pagina com o histórico de consultas agendadas do
	 * paciente
	 */
	@PreAuthorize("hasAnyAuthority('PACIENTE','ADMIN')")
	@GetMapping("/historico/paciente")
	public String historicoDeConsultasAgendadas() {

		return "agendamento/historico-paciente";
	}
	
	
	
	@PreAuthorize("hasAnyAuthority('MEDICO','ADMIN')")
	@GetMapping("/historico/consultas")
	public String historicoDeConsultasAgendadasMedico() {

		return "agendamento/historico-consultas";
	}
	
	

	/**
	 * Metodo para localizar o historico de consultas agendadas do usuário que
	 * estiver logado na aplicação.
	 */
	@PreAuthorize("hasAnyAuthority('PACIENTE','MEDICO','ADMIN')")
	@GetMapping("/datatables/server/historico")
	public ResponseEntity<?> historicoDeAgendamentosPorPaciente(HttpServletRequest request,
			@AuthenticationPrincipal User user) {

		if (user.getAuthorities().contains(new SimpleGrantedAuthority(PerfilTipo.PACIENTE.getDesc())))
			return ResponseEntity.ok(agendamentoService.buscarHistoricoDoPacientePorEmail(user.getUsername(), request));

		else if (user.getAuthorities().contains(new SimpleGrantedAuthority(PerfilTipo.MEDICO.getDesc())))
			return ResponseEntity.ok(agendamentoService.buscarHistoricoDoMedicoPorEmail(user.getUsername(), request));

		if (user.getAuthorities().contains(new SimpleGrantedAuthority(PerfilTipo.ADMIN.getDesc())))
			return ResponseEntity.ok(agendamentoService.buscarHistoricoDeConsultas(request));
		else
			return ResponseEntity.notFound().build();
	}

	/**
	 * Metodo que localiza uma consulta agendada pelo seu id e envia ela para a
	 * pagina de cadastro.
	 */
	@PreAuthorize("hasAnyAuthority('PACIENTE','ADMIN')")
	@GetMapping("/editar/consulta/{id}")
	public String preEditarConsultaAgendadaDoPaciente(@PathVariable("id") Long id, ModelMap model,
			@AuthenticationPrincipal User user) {
		Agendamento agendamento = agendamentoService.buscarPorIdEUsuario(id, user.getUsername());

		model.addAttribute("agendamento", agendamento);
		return "agendamento/cadastro";
	}

	/**
	 * Metodo para editar uma consulta agendada independente do perfil que envia
	 * um email confirmando que a consulta foi altera e com uma regra de negócio
	 * que a consulta só pode ser alterada 2 dias antes..
	 * 
	 * @param agendamento
	 * @param attr
	 * @param user
	 * @return
	 * @throws MessagingException
	 * @throws ParseException
	 */
	@PreAuthorize("hasAnyAuthority('PACIENTE','ADMIN')")
	@PostMapping("editar")
	public String editarConsultaAgendadaDoPaciente(Agendamento agendamento, RedirectAttributes attr,
			@AuthenticationPrincipal User user) throws MessagingException, ParseException {

		Long dataConsulta = dataUtils.dataConsulta(agendamento.getDataConsulta());
		Long dataSistema = System.currentTimeMillis();
		if (dataSistema <= dataConsulta) {
			String titulo = agendamento.getEspecialidade().getTitulo();
			Especialidade especialidade = especialidadesService.buscarPorTitulos(new String[] { titulo }).stream()
					.findFirst().get();
			agendamento.setEspecialidade(especialidade);

			agendamentoService.editar(agendamento, user.getUsername());
			attr.addFlashAttribute("sucesso", "Sua consulta foi alterada com sucesso. !");
			emailService.enviarAlteraçãoConsultaAgendada(user.getUsername(),
					agendamento.getEspecialidade(), agendamento.getMedico(),
					agendamento.getDataConsulta(), agendamento.getHorario());
			return "redirect:/agendamentos/agendar";
		}

		attr.addFlashAttribute("falha", "A data e hora da consulta só pode ser alterada 2 dias antes. !");
		return "redirect:/agendamentos/agendar";
	}

	/**
	 * Metodo para excluir uma consulta do paciente ja adendada pelo usuario do
	 * paciente. * @param id
	 * 
	 * @param attr
	 * @return
	 * @throws MessagingException 
	 */
	@PreAuthorize("hasAnyAuthority('PACIENTE','ADMIN')")
	@GetMapping("excluir/consulta/{id}")
	public String excluirConsulta(@PathVariable("id") Agendamento agendamento, RedirectAttributes attr) throws MessagingException {
		Long idUsuario = agendamento.getId();
		String emailUsuarioPaciente= emailService.buscarEmailPaciente(idUsuario);
		emailService.enviarConsultaDesmarcada(emailUsuarioPaciente,
				agendamento.getEspecialidade(), agendamento.getMedico(),
				agendamento.getDataConsulta(), agendamento.getHorario());
		agendamentoService.remover(agendamento.getId());
		attr.addFlashAttribute("sucesso", "Consulta excluída com sucesso.");
		
		
		
		return "redirect:/agendamentos/historico/paciente";

	}
	
	

}