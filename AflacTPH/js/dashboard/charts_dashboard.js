/* This url will hold the backend link from which to fetch data */
var ajaxURL = "", drillDownURL = "";
var totalItems = 0;
var charts = [], chart_names = [];
var allData = [], allDrillDown = [], allSources = [];
var colors = Highcharts.getOptions().colors;
var chart;
var manualColor = "#ffc200", chart_bgColor = "#fff6eb", chart_Trade_bgColor = "#fff6eb", chart_Security_bgColor = "#ebecff";

/* Common Chart options */
var chart_options = {
		chart : {
//			renderTo: 'c_container' + i,
			plotBackgroundColor : chart_bgColor,
			plotBorderWidth : null,
			plotShadow : false,
			margin: [0, 0, 0, 0],
            spacingTop: 0,
            spacingBottom: 0,
            spacingLeft: 0,
            spacingRight: 0,
            events: {
                load: function (event) {
                	/* On chart load, disable showing the drillDownData series */
                    var drillDownSeries_opt = this.series[1].options;
                    drillDownSeries_opt.dataLabels.enabled = false;
                    this.series[1].update(drillDownSeries_opt);
                    this.series[1].hide();
                },
                click: function(event) {
                	var current_chart_name = this.options.title.text;
                    var currentChart = $(this.renderTo);
             		var carouselRoot = currentChart.parent();
             		if(carouselRoot.attr("id") == "subCarousel"){
             			var current_chart_data_index =  $.inArray(current_chart_name,allSources);
             			var current_chart_index = currentChart.index()+1;
             			//console.log("Current chart Position:"+current_chart_index+" Current Chart Data Index:"+current_chart_index + " Current chart:"+allSources[current_chart_index]);
             			var main_chart_source = charts[0].options.title.text;
             			var main_chart_data_index = $.inArray(main_chart_source,allSources);
             			//console.log("Index of Main Chart in data array:"+main_chart_data_index+" Main chart source:"+main_chart_source);
             			charts[0].destroy();
             			charts[current_chart_index].destroy();
             			$("#c_container0").remove();
             			$("#c_container"+current_chart_index).remove();
             			//console.log("Removing chart from position:"+current_chart_index);
             			//console.log("Total charts before removing:"+charts.length+" >>"+chart_names.join());
             			charts.shift();
             			charts.splice(current_chart_index-1,1);
             			chart_names.shift();
             			chart_names.splice(current_chart_index-1,1);
             			//console.log("Total charts after removing:"+charts.length +" <<"+chart_names.join());
             			buildChart(0,allSources[current_chart_data_index],allData[current_chart_data_index],allDrillDown[current_chart_data_index]);
             			buildChart(current_chart_index,allSources[main_chart_data_index],allData[main_chart_data_index],allDrillDown[main_chart_data_index]);
             		}
                }
            }
		},
		legend : {
			enabled: true
		},
		title : {
//			text : source,
			verticalAlign: 'middle',
		    floating: true,
		    style: {
		    	color: '#000000',
		    	fill: '#ffffff'
		    }
		},
		credits : {
			enabled: false
		},
		tooltip : {
			formatter: function() {
                var s = "";
                if (this.point.name) { // the pie chart
                    if(this.point.name == "PROCESSED"){
                    	s = ''+
                        this.point.name +': '+ this.y + ' Click to view details';
                    } else {
                    	s = ''+
                        this.point.name +': '+ this.y;
                    }
                } 
                return s;
			}
		},
		plotOptions : {
			pie : {
                center: ['50%', '50%'],
				cursor : 'pointer',
				dataLabels: {
					connectorPadding : 5,
					distance : 10
				},
				showInLegend: true,
				point: {
                    events: {
                        click: function () {
                            var clickedName = this.name;
                            var source = "&source="+this.series.name;
                            if(this.series.name == "Summary")
                            	source = "";
                            if(clickedName == "PROCESSED"){
                            	var drillDownSeries = this.series.chart.series[1];
                            	var drillDownOpts = this.series.chart.series[1].options;
                                if (drillDownSeries.visible) {
                                	drillDownOpts.dataLabels.enabled = false;
                                    this.series.chart.series[1].update(drillDownOpts);
                                    this.series.chart.series[1].hide();
                                } else {
                                	drillDownOpts.dataLabels.enabled = true;
                                    this.series.chart.series[1].update(drillDownOpts);
                                    this.series.chart.series[1].show();
                                }
                            } else {
                            	window.location.href = drillDownURL+"drillDown="+clickedName+source;
                            }
                          },
				          legendItemClick: function () {
				              return false; // <== returning false will cancel the default action
				          }
                    }
                }
			}
		},
		series : [ {
			type : 'pie',
			size: '65%',
			innerSize: '55%',
//			name : source,
//			data : data,
			dataLabels: {
				enabled: true,
				formatter: function () {
					if(this.point.y==0){
						return null;
					}else{
						return this.point.name + " - "+this.point.y;
					}
	            }
	        }
		}
		,{
			type : 'pie',
			size: '49%',
			innerSize: '47%',
//			name : source,
//			data : drillDownData,
			showInLegend: false,
			dataLabels: {
				enabled: true,
				formatter: function () {
					/* Don't show Data Label for PENDING and FAILED */
	                var drillDownPointName = this.point.name;
	                var drillDownPointValue = this.point.y;
	                var s = drillDownPointName + " - "+ drillDownPointValue;
	                if(drillDownPointName == "PENDING" || drillDownPointName == "FAILED" || drillDownPointValue == 0){
	                	s = " ";
	                } 
	                return s;
	            },
	            distance : -20
	        }
		} ]// End of Chart Series array
	};


$(document).ready(function() {
	/*Set up URLs for Dashboard elements*/
	setupDashboardURLs();
	/*Create the complete dashboard*/
	createDashboard();
	/* Reload the dashboard every 5 minutes */
	setInterval(reloadDashboard,300000);
	
	/*Setup listener for filtering panel*/
	$("input[name=serviceType_radio]").change(function(){
		reloadDashboard();
	});
	
	/*Setup listener for the Source filtering panel*/
	$("#sourceTable").on("change","input[type=checkbox]", function(){
		/* Based on what source the user selects, the carousel should display the chart */
		var totalChecked = $("input[type=checkbox]:checked").length;
		if(totalChecked > 2){
			$(this).prop("rel","tooltip").prop("title","Please select two sources at most").prop("checked","").tooltip({
				placement: 'right'
			});
		} else{
			$(".jcarousel ul li").css("display","none");
			$("input[type=checkbox]:checked").each(function(){
				var value = $(this).attr("value");
				$("#c_container"+value).css("display","block");
				//$(this).tooltip('hide'); //commented out by Sudipta
			});
		}
	});
	
});

/* Function to completely reload the dashboard */
function reloadDashboard(){
	/* 
	 * 1. Setup new Dashboard URLs
	 * 2. Remove Sources from the Filter
	 * 3. Remove the Charts and Destroy the Carousel
	 * 4. Create new Dashboard all over
	 */
	setupDashboardURLs();
//	removeExistingSources();
	removeExistingCharts();
	createDashboard();
}

/* Function to setup the various links in the dashboard */
function setupDashboardURLs(){
	var selectedView=$('#selectedView').val();
	var selectedSystem=$('#selectedSystem').val();
	var serviceType = $('input:radio[name=serviceType_radio]:checked').val();
	var serviceType_radio = "serviceType=" + serviceType;
	
	if(selectedView == "DASHBOARD"){
		if(selectedSystem == "EXT_MNG"){
			ajaxURL = "/tph/IncomingDashboard.do?"+serviceType_radio+"&r="+Math.random();
			if(serviceType == "Trades"){
				drillDownURL = "/tph/TRADE_EXT_MNG.do?";
				chart_bgColor = chart_Trade_bgColor;
			} else {
				drillDownURL = "/tph/SECURITY_EXT_MNG.do?";
				chart_bgColor = chart_Security_bgColor;
			}
		} else{
			ajaxURL = "/tph/OutgoingDashboard.do?"+serviceType_radio+"&dest="+selectedSystem+"&r="+Math.random();
			if(serviceType == "Trades"){
				drillDownURL = "/tph/TRADE_OUTGOING.do?dest="+selectedSystem+"&";
				chart_bgColor = chart_Trade_bgColor;
			} else {
				drillDownURL = "/tph/SECURITY_OUTGOING.do?dest="+selectedSystem+"&";
				chart_bgColor = chart_Security_bgColor;
			}
		}
	}
	var selectedSystemName = $("#"+selectedSystem+" > a > span").html();
	$("#chartHeader").html("<center>"+selectedSystemName+" - "+serviceType+" </center>");
}

/* Function to build the dashboard. Does the following:
 * 1. Ajax based data fetch
 * 2. Build charts
 * 3. Add each chart to Carousel element
 * 4. Setup for filtering panel
 */
function createDashboard(){
	$.ajax({
		url : ajaxURL,
		type : 'GET',
		async : true,
		dataType : "json",
		success : function(data) {
			var totalFailedCount = 0, totalPendingCount = 0, totalOthersCount = 0, totalProcessedCount = 0, totalManualCount = 0, totalDeletedCount = 0, totalSources = 0;
			/* For each source */
			$.each(data, function(index, element) {
				/*
				 * myDataArray - array of PENDING, FAILED and OTHERS
				 * drillDownDataArray - array of PENDING, FAILED, PROCESSED, MANUAL and DELETED
				 */
				var myDataArray = [], drillDownDataArray = [];
				var source = "";
				var othersCount = 0;
				/* For each of the counts */
				$.each(element,function(key,val) {
					if (!(key == "source" || key == "camralastProcessDate" || key == "totalCnt")) {
						key = key.replace("Cnt","").toUpperCase();
						
						/*
						 * dataPoint - Object to be inserted into myDataArray
						 * drillDownDataPoint - Object to be inserted into drillDownDataArray
						 */
						var dataPoint = {name:key,y:val,color:''};
						var drillDownDataPoint = {name:key,y:val,color:colors[2]};
						//console.log("1 "+colors[1]+" 2 "+colors[2]+" 3 "+colors[3]+" 4 "+colors[4]);
						switch(key){
							case "FAILED": totalFailedCount += val;
										   dataPoint.color = colors[3];
										   myDataArray.push(dataPoint);
										   drillDownDataPoint.color  = 'rgba(255,255,255,1)';
										   drillDownDataArray.push(drillDownDataPoint);
										   break;
										   
							case "PENDING": totalPendingCount += val;
											dataPoint.color = colors[4];
											myDataArray.push(dataPoint);
											drillDownDataPoint.color  = 'rgba(255,255,255,1)';
											drillDownDataArray.push(drillDownDataPoint);
											break;
							
							case "PROCESSED": totalProcessedCount +=val; 
											  totalOthersCount += val;
											  othersCount += val;
											  drillDownDataPoint.color  = colors[2];
											  drillDownDataPoint.name = "STP";
											  drillDownDataArray.push(drillDownDataPoint);
											  break;
											  
							case "MANUAL": totalManualCount +=val; 
										   totalOthersCount += val;
										   othersCount += val;
										   drillDownDataPoint.color = manualColor;
										   drillDownDataArray.push(drillDownDataPoint);
										   break;
										   
							case "DELETED": totalDeletedCount +=val; 
										    totalOthersCount += val;
										    othersCount += val;
										    drillDownDataPoint.color = colors[2];
										    drillDownDataArray.push(drillDownDataPoint);
										    break;
						}
												
					} else if (key == "source") {
						source = val;
					}
				});
				totalSources++;
				dataPoint = {name:'PROCESSED',y:othersCount, color: colors[2]};
				myDataArray.push(dataPoint);
				
				allData.push(myDataArray);
				allDrillDown.push(drillDownDataArray);
				allSources.push(source);
				
				buildChart(index+1,source,myDataArray,drillDownDataArray);
			});
			/* Building the Overview Chart BEGIN */
			
			/*
			 * myDataArray - array of PENDING, FAILED and OTHERS
			 * drillDownDataArray - array of PENDING, FAILED, PROCESSED, MANUAL and DELETED
			 * dataPoint - Object to be inserted into myDataArray
			 * drillDownDataPoint - Object to be inserted into drillDownDataArray
			 */
			var myDataArray = [], drillDownDataArray = [];
			var dataPoint, drillDownDataPoint;
			
			dataPoint = {name:"FAILED",y:totalFailedCount, color: colors[3]};
			drillDownDataPoint = {name:"FAILED",y:totalFailedCount};
			myDataArray.push(dataPoint);
			drillDownDataPoint.color  = 'rgba(255,255,255,1)';
			drillDownDataArray.push(drillDownDataPoint);
			
			
			dataPoint = {name:"PENDING",y:totalPendingCount, color: colors[4]};
			drillDownDataPoint = {name:"PENDING",y:totalPendingCount,color: colors[2]};
			myDataArray.push(dataPoint);
			drillDownDataPoint.color  = 'rgba(255,255,255,1)';
			drillDownDataArray.push(drillDownDataPoint);
			
			dataPoint = {name:"PROCESSED",y:totalOthersCount, color: colors[2]};
			myDataArray.push(dataPoint);

			drillDownDataPoint = {name:"STP",y:totalProcessedCount, color: colors[2]};
			drillDownDataArray.push(drillDownDataPoint);
			
			drillDownDataPoint = {name:"MANUAL",y:totalManualCount, color: manualColor};
			drillDownDataArray.push(drillDownDataPoint);
			
			drillDownDataPoint = {name:"DELETED",y:totalDeletedCount, color: colors[2]};
			drillDownDataArray.push(drillDownDataPoint);

			allData.unshift(myDataArray);
			allDrillDown.unshift(drillDownDataArray);
			allSources.unshift("Summary");

			buildChart(0,"Summary",myDataArray, drillDownDataArray);
			
			/* Building the Overview Chart END */
			
//			createCarousel(totalSources+1);
			
			/* This is needed, because for some bloody reason,
			 *  left padding was getting applied on the chart */
			$(window).trigger('resize');
			
			
		}
	});
}

/*Function that dynamically builds charts and loads it into the carousel*/
function buildChart(i, source, data, drillDownData) {
	var carouselElem = "<li id=\"c_container"+i+"\"></li>";	
	var circleradius = 0;
//	var sourceElem = "<tr><td><input type=\"checkbox\" name=\""+source+"\" value=\""+i+"\"> "+source+"</td></tr>";
	if(i > 0) {
		if(i==1){
			$("#subCarousel").prepend(carouselElem);
		} else {
			$("#subCarousel li:eq("+(i-2)+")").after(carouselElem);
		}
		chart_options.chart.height = 198;
		chart_options.legend.enabled = false;
		circleradius = 50;
//		$("#sourceTable").append(sourceElem);
	} else {
		$("#carouselRoot").prepend(carouselElem);
		chart_options.chart.height = 396;
		chart_options.legend.enabled = true;
		circleradius = 100;
	}
	chart_options.chart.renderTo = 'c_container'+i;
	chart_options.chart.plotBackgroundColor = chart_bgColor;
	chart_options.title.text = source;
	chart_options.series[0].name = source;
	chart_options.series[0].data = data;
	chart_options.series[1].name = source;
	chart_options.series[1].data = drillDownData;
	
	chart = new Highcharts.Chart(chart_options,
			function(chart){
				/* Function to fill the center of each chart with a white circle */
				var xpos = '50%';
			    var ypos = '50%';
				chart.renderer.circle(xpos, ypos, circleradius).attr({
			        fill: '#ffffff',
			    }).add();
			});

	charts.splice(i,0,chart);
	chart_names.splice(i,0,source);
}

/* Function to create the carousel that contains the charts */
function createCarousel(totalCharts){
	/*var carousel_width = {width:"100%"};
	var summary_chart_width = {width:"50%"};
	var source_chart_width = {width:"25%", height:"25%"};
	$(".jcarousel ul").css(carousel_width);
	$(".jcarousel li").css(summary_chart_width);
	$(".jcarousel li").css("display","block");*/
//	setSourceCheckBoxes();
}

/* Function to check the first two source checkboxes */
function setSourceCheckBoxes(){
	//Apply tooltip for all the checkbox for dashboard source - by Sudipta
	$("input[type=checkbox]").prop("rel","tooltip").prop("title","Please select two sources at most").tooltip({
		placement: 'right'
	});
	
	$("input[type=checkbox]:lt(2)").prop("checked","checked");	
}

/* Function to clear Sources when user switches from
 * one Service to another
 */
function removeExistingSources(){
	//console.log("Removing the source filter");
	$("#sourceTable tr:gt(1)").remove();
	$("input:checkbox[value=0]").prop("checked","checked");
}

/* Function to remove all existing charts and destroy the carousel */
function removeExistingCharts(){
	//console.log("Removing the charts");
	$("#carouselRoot li:eq(0)").remove();
	$("#subCarousel").empty();
	charts.splice(0,charts.length);
	chart_names.splice(0,chart_names.length);
}