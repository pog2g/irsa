$(function() {
	$.ajax({
		url: "cases/total",
		type: "post",
		dataType: "json",
		success: function(data) {
			if(data.result != "1") {
				showMsg(data.error_msg);
				return;
			}

			$("#number_new").html(data.data.number_new);
			$("#number_process_1").html(data.data.number_process_1);
			$("#number_process_2").html(data.data.number_process_2);
			$("#number_total").html(data.data.number_total);

			initChart4Line("lineChart", "案件处理", data.data.line);
			if (data.data.pie_1 != null) {
				initChart4PieNest("pieChart1", "受理结果", "30%", "50%", data.data.pie_1);
			}
			if (data.data.pie_2 != null) {
				$(".pie-chart-2").show();
				initChart4PieNest("pieChart2", "审理结果", "40%", "50%", data.data.pie_2);
			} else {
				$(".pie-chart-2").hide();
			}
			
			if (data.data.pie_3 != null) {
				initChart4RoseType("pieChart3", "案由分布", data.data.pie_3);
			}
			if (data.data.bar_3 != null) {
				initChart4Bar("barChart3", "申请人排名", data.data.bar_3);
			}
			if (data.data.pie_4 != null) {
				initChart4Funnel("pieChart4", "被申请人类型", 'center', data.data.pie_4);
			}
			if (data.data.pie_5 != null) {
				initChart4Funnel("pieChart5", "被申请人排名", 'left', data.data.pie_5);
			}
		},
		error: function(xhr, type) {
			showMsg("系统繁忙");
			$("#table_loading").hide();
		}
	});

	function initChart4Line(elementId, title, data) {
		var lineChartDiv = document.getElementById(elementId);
		var lineChart = echarts.init(lineChartDiv);
		var lineOption = null;
		lineOption = {
			title: {
				text: title,
				x: 'center'
			},
			tooltip: {
				trigger: 'axis'
			},
			toolbox: {
				feature: {
					saveAsImage: {}
				}
			},
			legend: {
				orient: 'vertical',
				x: 'left',
				data: data.series
			},
			grid: {
				left: '7%',
				right: '4%',
				bottom: '3%',
				containLabel: true
			},
			toolbox: {
				feature: {
					saveAsImage: {}
				}
			},
			xAxis: {
				type: 'category',
				boundaryGap: false,
				data: data.x_data
			},
			yAxis: {
				type: 'value'
			},
			calculable: true,
			series: data.series
		};
		if(lineOption && typeof lineOption === "object") {
			lineChart.setOption(lineOption, true);
		}
	}

	function initChart4PieNest(elementId, title, inRate, outRate, data) {
		var pieNestChartDiv = document.getElementById(elementId);
		var pieNestChart = echarts.init(pieNestChartDiv, 'light');
		var pieNestOption = null;
		pieNestOption = {
			title: {
				text: title,
				x: 'center'
			},
			tooltip: {
				trigger: 'item',
				formatter: "{b}：{c}（{d}%）"
			},
			toolbox: {
				feature: {
					saveAsImage: {}
				}
			},
			legend: {
				orient: 'vertical',
				x: 'left',
				data: data.legend
			},
			calculable: true,
			series: [{
					name: '数据统计',
					type: 'pie',
					selectedMode: 'single',
					radius: [0, '30%'],

					label: {
						normal: {
							position: 'inner'
						}
					},
					labelLine: {
						normal: {
							show: false
						}
					},
					data: data.inner
				},
				{
					name: title,
					type: 'pie',
					radius: [inRate, outRate],
					label: {
						normal: {
							formatter: '{b| {b}：}{c} {per|{d}%}  ',
							backgroundColor: '#eee',
							borderColor: '#aaa',
							borderWidth: 1,
							borderRadius: 4,
							rich: {
								b: {
									fontSize: 16,
									lineHeight: 33
								},
								per: {
									color: '#eee',
									backgroundColor: '#334455',
									padding: [2, 4],
									borderRadius: 2
								}
							}
						}
					},
					data: data.data
				}
			]
		};
		if(pieNestOption && typeof pieNestOption === "object") {
			pieNestChart.setOption(pieNestOption, true);
		}
	}

	function initChart4Funnel(elementId, title, align, data) {
		var label = {};
		var sort = "descending";
		if(align == "center") {
			label = {
				normal: {
					show: true,
					position: 'inner'
				}
			}
			sort = "ascending";
		}

		var funnelChartDiv = document.getElementById(elementId);
		var funnelChart = echarts.init(funnelChartDiv);
		var funnelOption = null;
		funnelOption = {
			title: {
				text: title,
				x: 'center'
			},
			tooltip: {
				trigger: 'item',
				formatter: "{b}"
			},
			toolbox: {
				feature: {
					saveAsImage: {}
				}
			},
//			legend: {
//				orient: 'vertical',
//				x: 'left',
//				data: data.legend
//			},
			calculable: true,
			series: [{
				name: title,
				type: 'funnel',
				funnelAlign: align,
				sort: sort,
				label: label,
				width: '50%',
				left: '25%',
				data: data.data
			}]
		};
		if(funnelOption && typeof funnelOption === "object") {
			funnelChart.setOption(funnelOption, true);
		}
	}

	function initChart4RoseType(elementId, title, data) {
		var roseChartDiv = document.getElementById(elementId);
		var roseChart = echarts.init(roseChartDiv, 'light');
		var roseOption = null;
		roseOption = {
			title: {
				text: title,
				x: 'center'
			},
			tooltip: {
				trigger: 'item',
				formatter: "{b}：{c}（{d}%）"
			},
			legend: {
				orient: 'vertical',
				x: 'left',
				data: data.legend
			},
			toolbox: {
				feature: {
					saveAsImage: {}
				}
			},
			calculable: true,
			series: [{
				name: title,
				type: 'pie',
				radius: [0, '55%'],
				roseType: 'radius',
				label: {
					normal: {
						show: true
					},
					emphasis: {
						show: true
					}
				},
				lableLine: {
					normal: {
						show: true
					},
					emphasis: {
						show: true
					}
				},
				data: data.data
			}]
		};
		if(roseOption && typeof roseOption === "object") {
			roseChart.setOption(roseOption, true);
		}
	}

	function initChart4Bar(elementId, title, data) {
		var barChartDiv = document.getElementById(elementId);
		var barChart = echarts.init(barChartDiv);
		var barOption = null;
		barOption = {
			title: {
				text: title,
				x: 'center'
			},
			tooltip: {
				trigger: 'item',
				axisPointer: {
					type: 'shadow'
				}
			},
			toolbox: {
				feature: {
					saveAsImage: {}
				}
			},
			grid: {
				left: '20%'
			},
			xAxis: {
				type: 'value',
				position: 'top'
			},
			yAxis: {
				type: 'category',
				data: data.legend
			},
			calculable: true,
			series: [{
				data: data.data,
				type: 'bar'
			}]
		};;
		if(barOption && typeof barOption === "object") {
			barChart.setOption(barOption, true);
		}
	}



})