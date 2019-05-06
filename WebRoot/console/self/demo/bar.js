var barChartDiv = document.getElementById("barChart");
var barChart = echarts.init(barChartDiv);
var barOption = null;
barOption = {
	title: {
		text: '统计分析',
		show: true
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
		data: ['变更废止农业承包合同', '行政强制', '行政裁决', '行政登记', '行政确认', '行政确权', '行政不作为']
	},
	series: [{
		data: [120, 170, 250, 280, 370, 410, 1010],
		type: 'bar'
	}]
};;
if(barOption && typeof barOption === "object") {
	barChart.setOption(barOption, true);
}