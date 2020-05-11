window.onload = function() {
    var grafico = /*[[${grafico}]]*/ 'default';
    
    //  Only a test
    grafico.forEach(myFunction);
    function myFunction(value, index, array) {
    	console.log(value); 
    }    
    
    var chart = new CanvasJS.Chart("chartContainer", {
    	animationEnabled: true,
    	title: {
    		text: "Quantidade de Usuarios"
    	},
    	ajax:{
    		url:'/quantidade-usuario/'+ grafico,
    		data:'data'
    	},
    	data: [{        
    		type: "pie",
    		startAngle: 240,
    		yValueFormatString: "##0.00\"\"",
    		indexLabel: "{label} {y}",     
    		dataPoints: []
    	}]
    });

    chart.options.data[0].dataPoints = grafico;	
    chart.render();		   	
}
    	
/*]]>*/
