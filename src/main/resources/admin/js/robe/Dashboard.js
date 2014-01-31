function initializeDashboard() {
	$.ajax({
    		type: "GET",
    		url: "http://127.0.0.1:8080/admin/deneme.json",
    		dataType: "json",
    		contentType: "application/json; charset=utf-8",
    		success: function(response) {
//    		"memory" : {
//                  "totalInit" : 1.58793728E8,
//                  "totalUsed" : 1.61402776E8,
//                  "totalMax" : 2.045247488E9,
//                  "totalCommitted" : 2.74137088E8,
//                  "heapInit" : 1.34217728E8,
//                  "heapUsed" : 1.20443584E8,
//                  "heapMax" : 1.908932608E9,
//                  "heapCommitted" : 2.32783872E8,
//                  "heap_usage" : 0.0630947281717763,
//                  "non_heap_usage" : 0.30047484177809497,
//                  "memory_pool_usages" : {
//                    "Code Cache" : 0.04534912109375,
//                    "PS Eden Space" : 0.12241299605021377,
//                    "PS Old Gen" : 0.01702477311913347,
//                    "PS Perm Gen" : 0.4498167270567359,
//                    "PS Survivor Space" : 0.9996236165364584
//                  }
//                },
    			var data = response["jvm"]["memory"];
    			var chartData = [{name:"Free",value: data["totalMax"]-data["totalUsed"]},{name:"Used",value: data["totalUsed"]}];
				var chart;
				var legend;
				// PIE CHART
				chart = new AmCharts.AmPieChart();
				chart.dataProvider = chartData;
				chart.titleField = "name";
				chart.valueField = "value";
				chart.outlineColor = "#FFFFFF";
				chart.outlineAlpha = 0.8;
				chart.outlineThickness = 2;
                chart.labelText = "[[title]]: [[percents]]% [[value]]/1024";
				// WRITE
				chart.write("chartdiv");
    		},
    		error:function( jqXHR ,  textStatus,  errorThrown ) {
    		      console.log(errorThrown);
    		}

    });

}