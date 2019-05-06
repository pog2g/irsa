var radarChartDiv = document.getElementById("radarChart");
var radarChart = echarts.init(radarChartDiv);
var radarOption = null;
radarOption = {
	title: {
		text: '案由分布',
		show: true
	},
	legend: {
		data: ['2018', '2017'],
		x: 'left',
		top: '40'
	},
	toolbox: {
		feature: {
			saveAsImage: {}
		}
	},
	radar: {
		name: {
			textStyle: {
				color: '#fff',
				backgroundColor: '#334455',
				borderRadius: 3,
				padding: [4, 6]
			}
		},
		indicator: [{
				name: '告知',
				max: 20
			},
			{
				name: '转办',
				max: 20
			},
			{
				name: '转送',
				max: 20
			},
			{
				name: '不予受理',
				max: 20
			},
			{
				name: '主动放弃申请',
				max: 20
			},
			{
				name: '终止（撤回）',
				max: 20
			},
			{
				name: '终止（其他/自定义）',
				max: 20
			},
			{
				name: '支持',
				max: 20
			},
			{
				name: '否定（驳回）',
				max: 20
			},
			{
				name: '否定（维持）',
				max: 20
			}
		]
	},
	series: [{
		name: '案件分布',
		type: 'radar',
		data: [{
				value: [11, 9, 7, 13, 6, 5, 15, 14, 2, 18],
				name: '2018'
			},
			{
				value: [6, 4, 10, 5, 8, 14, 12, 10, 3, 17],
				name: '2017'
			}
		]
	}]
};;
if(radarOption && typeof radarOption === "object") {
	radarChart.setOption(radarOption, true);
}