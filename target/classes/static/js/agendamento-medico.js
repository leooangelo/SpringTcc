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
        	{	data : 'id',	
				render : function(id) {
					return ''.concat('<a class="btn btn-success btn-sm btn-block"', ' ')
							 .concat('href="').concat('/prontuario/editar/paciente/').concat(id, '"', ' ') 
							 .concat('role="button" title="Editar" data-toggle="tooltip" data-placement="right">', ' ')
							 .concat('<i class="fas fa-edit"></i></a>');
				},
				orderable : false
			}
        ]
    });
});
