/**
 *  @author leonardoangelo
 *  
 *  Busca as especialidades com auto-complete na pagina de agendamentos
 */
$("#especialidade").autocomplete({
    source: function (request, response) {
        $.ajax({
            method: "GET",
            url: "/especialidades/titulo",
            data: {
            	termo: request.term
			},
            success: function (data) {
            	response(data);
            }
        });
    }
});


/**
 * Datatable histórico de consultas com a ordenação dos dados quem vem do array trazendo todas as informação de uma consulta
 * agendada para poder ser mostrada.
*/
$(document).ready(function() {
    moment.locale('pt-BR');
    var table = $('#table-paciente-historico').DataTable({
        searching : true,
        lengthMenu : [ 5, 10 ],
        processing : true,
        serverSide : true,
        responsive : true,
        order: [2, 'desc'],
        ajax : {
            url : '/agendamentos/datatables/server/historico',
            data : 'data'
        },
        columns : [
            {data : 'id'},
            {data : 'paciente.nome'},
            {data: 'dataConsulta', render:
                function( dataConsulta ) {
                    return moment(dataConsulta).format('LLL');
                }
            
			},
            {data : 'medico.nome'},
            {data : 'especialidade.titulo'},
            
            {orderable : false,	data : 'id', "render" : function(id) {
                    return '<a class="btn btn-danger btn-sm btn-block" href="/agendamentos/excluir/consulta/'
                    + id +'" role="button" data-toggle="modal" data-target="#confirm-modal"><i class="fas fa-times-circle"></i></a>';
                }
            },
            {data: 'prontuario.descricao'},
            {orderable : false,	data : 'id', "render" : function(id) {
                return '<a class="btn btn-info btn-sm btn-block" href="/prontuario/editar/prontuario/'
                + id +'" role="button"><i class="fas fa-print"></i></a>';
            	}
            
            }
        ]
    });
});


