var Charts = {

	pie: function(id,data,title){
		$('#'+id).highcharts({
			chart: {
				plotBackgroundColor: null,
				plotBorderWidth: 0,
				plotShadow: false
			},
			title: {
				text: title,
				align: 'center',
				verticalAlign: 'middle',
				y: 50
			},
			tooltip: {
				pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b> ({point.y})Bytes'
			},
			plotOptions: {
				pie: {
					dataLabels: {
						enabled: true,
						distance:5,
						shadow:true,
						color:"#333333"
					},
					startAngle: -90,
					endAngle: 90,
					center: ['50%', '75%'] ,
					colors:["#00FF00","#FF0000"]
				}
			},
			series: [{
				type: 'pie',
				name: 'Oran',
				innerSize: '50%',
				data: data
			}]
		});
	} ,

	gauge: function(id,data,title){
		$('#'+id).highcharts({
			chart: {
				type: 'gauge',
				plotBackgroundColor: null,
				plotBackgroundImage: null,
				plotBorderWidth: 0,
				plotShadow: false
			},

			title: {
				text: title
			},
			pane: {
				startAngle: -150,
				endAngle: 150,
				background: [{
					backgroundColor: {
						linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1 },
						stops: [
							[0, '#FFF'],
							[1, '#333']
						]
					},
					borderWidth: 0,
					outerRadius: '109%'
				}, {
					backgroundColor: {
						linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1 },
						stops: [
							[0, '#333'],
							[1, '#FFF']
						]
					},
					borderWidth: 1,
					outerRadius: '107%'
				}, {
					// default background
				}, {
					backgroundColor: '#DDD',
					borderWidth: 0,
					outerRadius: '105%',
					innerRadius: '103%'
				}]
			},

			yAxis: {
				min: 0,
				max: data[1],

				minorTickInterval: 'auto',
				minorTickWidth: 1,
				minorTickLength: 10,
				minorTickPosition: 'inside',
				minorTickColor: '#666',

				tickPixelInterval: 30,
				tickWidth: 2,
				tickPosition: 'inside',
				tickLength: 10,
				tickColor: '#666',
				labels: {
					step: 1,
					rotation: 'auto'
				},
				title: {
					text: 'Active Thread'
				},
				plotBands: [{
					from: 0,
					to: (data[1]*0.7).toFixed(0),
					color: '#55BF3B' // green
				}, {
					from: (data[1]*0.7).toFixed(0),
					to: (data[1]*0.85).toFixed(0),
					color: '#DDDF0D' // yellow
				}, {
					from: (data[1]*0.85).toFixed(0),
					to: data[1],
					color: '#DF5353' // red
				}]
			},

			series: [{
				name: 'Active',
				data: [data[0]]
			}]


		});
	}
}