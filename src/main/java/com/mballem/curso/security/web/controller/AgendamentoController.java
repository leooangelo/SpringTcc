package com.mballem.curso.security.web.controller;

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
	
	@PreAuthorize("hasAnyAuthority('PACIENTE','MEDICO')")
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
	@PreAuthorize("hasAnyAuthority('PACIENTE','MEDICO')")
	@GetMapping("/horario/medico/{id}/data/{data}")
	public ResponseEntity<?> getHorariosDeConsulta(@PathVariable("id") Long id,
			@PathVariable("data") @DateTimeFormat(iso = ISO.DATE) LocalDate data) {
		return ResponseEntity.ok(agendamentoService.buscarHorariosNaoAgendadosPorMedicosIdEData(id, data));

	}

	/**
	 * Metodo para salvar um agendamento de consulta na clinica.
	 * 
	 * @param agendamento
	 * @param attr
	 * @param user
	 * @return
	 * @throws MessagingException 
	 */
	@PreAuthorize("hasAnyAuthority('PACIENTE')")

	@PostMapping({ "/salvar" })
	public String salvar(Agendamento agendamento, RedirectAttributes attr, @AuthenticationPrincipal User user) throws MessagingException {
		Paciente paciente = pacienteService.buscarPorUsuarioEmail(user.getUsername());
		String titulo = agendamento.getEspecialidade().getTitulo();
		Especialidade especialidade = especialidadesService.buscarPorTitulos(new String[] { titulo }).stream()
				.findFirst().get();
		agendamento.setEspecialidade(especialidade);
		agendamento.setPaciente(paciente);
		agendamentoService.salvarConsultaAgendada(agendamento);
		attr.addFlashAttribute("sucesso", "Sua consulta foi agendada com sucesso");
		emailService.enviarConfirmacaoConsulta(user.getUsername(),
												agendamento.getEspecialidade(), agendamento.getMedico(),
												agendamento.getDataConsulta(), agendamento.getHorario());
		
		

		return "redirect:/agendamentos/agendar";
	}

	/**
	 * Metodo para abrir uma pagina com o histórico de consultas agendadas do
	 * paciente
	 */
	@PreAuthorize("hasAnyAuthority('PACIENTE','MEDICO')")

	@GetMapping({ "/historico/paciente", "/historico/consultas" })
	public String historicoDeConsultasAgendadas() {

		return "agendamento/historico-paciente";
	}

	/**
	 * Metodo para localizar o historico de consultas agendadas do usuário que
	 * estiver logado na aplicação.
	 */
	@PreAuthorize("hasAnyAuthority('PACIENTE','MEDICO')")
	@GetMapping("/datatables/server/historico")
	public ResponseEntity<?> historicoDeAgendamentosPorPaciente(HttpServletRequest request,
			@AuthenticationPrincipal User user) {

		if (user.getAuthorities().contains(new SimpleGrantedAuthority(PerfilTipo.PACIENTE.getDesc())))
			return ResponseEntity.ok(agendamentoService.buscarHistoricoDoPacientePorEmail(user.getUsername(), request));

		if (user.getAuthorities().contains(new SimpleGrantedAuthority(PerfilTipo.MEDICO.getDesc())))
			return ResponseEntity.ok(agendamentoService.buscarHistoricoDoMedicoPorEmail(user.getUsername(), request));
		else 
			return ResponseEntity.notFound().build();
	}

	/**
	 * Metodo que localiza uma consulta agendada pelo seu id e envia ela para a
	 * pagina de cadastro.
	 */
	@PreAuthorize("hasAnyAuthority('PACIENTE','MEDICO')")
	@GetMapping("/editar/consulta/{id}")
	public String preEditarConsultaAgendadaDoPaciente(@PathVariable("id") Long id, ModelMap model,
			@AuthenticationPrincipal User user) {
		Agendamento agendamento = agendamentoService.buscarPorIdEUsuario(id,user.getUsername());

		model.addAttribute("agendamento", agendamento);
		return "agendamento/cadastro";
	}
	/**
	 * Metodo para editar uma consulta agendada independente do perfil.
	 * @param agendamento
	 * @param attr
	 * @param user
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('PACIENTE','MEDICO')")
	@PostMapping("editar")
	public String editarConsultaAgendadaDoPaciente(Agendamento agendamento, RedirectAttributes attr,
			@AuthenticationPrincipal User user) {

		String titulo = agendamento.getEspecialidade().getTitulo();
		Especialidade especialidade = especialidadesService.buscarPorTitulos(new String[] { titulo }).stream()
				.findFirst().get();
		agendamento.setEspecialidade(especialidade);

		agendamentoService.editar(agendamento, user.getUsername());
		attr.addFlashAttribute("sucesso", "Sua consulta foi alterada com sucesso. !");
		return "redirect:/agendamentos/agendar";
	}
	
	/**
	 * Metodo para excluir uma consulta do paciente ja adendada pelo usuario do paciente.
	 * 	 * @param id
	 * @param attr
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('PACIENTE','ADMIN')")
	@GetMapping("excluir/consulta/{id}")
	public String excluirConsulta(@PathVariable("id") Long id, RedirectAttributes attr) {
		agendamentoService.remover(id);
		attr.addFlashAttribute("sucesso", "Consulta excluída com sucesso.");
		return "redirect:/agendamentos/historico/paciente";
		
	}
	
	
	
}