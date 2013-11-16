$(function() {
	
	getAjaxRequest('rs/stats', function(data) {
		$("#stats1").text(data.totalNewsSize);
		$("#stats2").text(data.categoryNewsSize);
		$("#stats3").text(data.moreKClusterNewsSize);
		$("#stats4").text(data.unCategoryNewsSize);
		$("#stats5").text(data.lessKClusterNewsSize);
		$("#stats6").text(data.lessKClusterSize);
		var rightCategoryAccuracy = data.rightCategoryNewsSize * 1.0 / data.categoryNewsSize;
		$("#stats7").text(rightCategoryAccuracy);
		$("#stats8").text(data.rightCategoryNewsSize);
		var rightClusterAccuracy = data.rightClusterNewsSize * 1.0 / data.moreKClusterNewsSize;
		$("#stats9").text(rightClusterAccuracy);
		$("#stats10").text(data.moreKClusterNewsSize);
		
		var contentData = [
	         ['未分类新闻数',	data.unCategoryNewsSize],
	         ['小于K聚类的新闻数',	data.lessKClusterNewsSize],
	         ['聚类后准确新闻数',		data.rightClusterNewsSize],
         	 {
                 name: '聚类后分类错误新闻数',
                 y: data.moreKClusterNewsSize - data.rightClusterNewsSize,
                 sliced: true,
                 selected: true
             }
	    ];
		var contentData2 = [
  	         ['分类后准确率',	rightCategoryAccuracy],
  	         ['聚类后增加准确率',	rightClusterAccuracy - rightCategoryAccuracy],
  	     	 {
                 name: '错误率',
                 y: 1 - rightClusterAccuracy,
                 sliced: true,
                 selected: true
             }
  	    ];
		var content = {
				title: "新闻数统计信息",
				name: "比例",
				data: contentData
		};
		var content2 = {
				title: "准确率统计信息",
				name: "准确率",
				data: contentData2
		};
		$('#statsCharts').highcharts(chartTemplate(content));
		$('#statsCharts2').highcharts(chartTemplate(content2));
	});
	
});

function chartTemplate(content) {
	var temp = {
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false,
            width: 500,
            height: 300
        },
        title: {
            text: content.title
        },
        tooltip: {
    	    pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: true,
                    color: '#000000',
                    connectorColor: '#000000',
                    format: '<b>{point.name}</b>: {point.percentage:.1f} %'
                }
            }
        },
        series: [{
            type: 'pie',
            name: content.name,
            data: content.data
        }]
    };
	return temp;
}