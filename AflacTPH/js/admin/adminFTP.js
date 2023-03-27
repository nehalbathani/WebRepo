$(document).ready(
			function() {
				$("#ftpgrid")
						.jqGrid(
								{
									url : 'FTPStatusJson.do',
									datatype : "json",
									mtype: "POST",
									autowidth : true,
									loadonce: true,
						            sortable:true,
									height: 300,
									//Change
									colNames : strings,
									//Change
									colModel : [{
									    name:"status_ID",
										index:"status_ID",
										hidden:true
									}, {
										name : 'client_CODE',
										index : 'client_CODE',
										autowidth : true
									},{
										name : 'file_CODE',
										index : 'file_CODE',
										autowidth : true
									},{
										name : 'file_NAME',
										index : 'file_NAME',
										autowidth : true
									},{
										name : 'task',
										index : 'task',
										autowidth : true
									},{
										name : 'status',
										index : 'status',
										autowidth : true
										
									},{
										name : 'time_STAMP',
										index : 'time_STAMP',
										autowidth : true,
										sorttype : "date",
										formatter : "date",
										formatoptions: {newformat:'Y/m/d'}
									}],
									sortname: "client_CODE",
									sortorder: "asc",
									loadComplete: function(data) {
										var $this = $(this),
										
								        datatype = $this.getGridParam('datatype');
									    if (datatype === "xml" || datatype === "json") {
									    	//console.log(data);
									    	if(data[0].errorCode != undefined && data[0].errorCode != ""){
									    		$('#errorSection').html(data[0].errorMessage);
									    		$('#errorSection').show();
									    		//Hide empty row and show the error message bottom bar of the grid
									    		$('#ftpgrid tr').hide();
									    		$('.ui-paging-info').html(data[0].errorMessage);
									    		//remove sortable, coz, on click it will load an empty row
									    		$('.ui-jqgrid-htable tr').removeClass('ui-sortable');
									    		$('.ui-jqgrid-htable tr div').removeClass('ui-jqgrid-sortable');
									    	}else{
									    		$('#errorSection').html("");
									    		$('#errorSection').hide();
									    		
									    		//$("#ftpgrid").jqGrid().trigger("reloadGrid");
									    		//TODO-to evaluate set time out is required
									    		/*setTimeout(function () {
									    			if (autoRefreshFlg == true || resendDiscardFlg == true) {
										            	console.log("Going to refresh");
										            	resendDiscardFlg = false;
										            	if (autoRefreshFlg == true) {
										            		console.log("Going add new filter-options");
										            		autoRefreshFlg = false;
															includeNewSource(data);
															includeNewSecurityGroup(data);
										            	}
										            	includeNewStatus(data);
										            	commonFilterCall();
										            } else if (resendDiscardFlg != true) {
										            	loadFilterValue(data);
										            	var drillDownStatus = getQueryVariable("drillDown");
										            	var drillDownSource = getQueryVariable("source");
												    	if(drillDownStatus != null && drillDownSource != null){
												    		loadGridForDashBoardFilter(drillDownStatus, drillDownSource);
												    		commonFilterCall();
												    	}
										            }
										        }, 100);*/
									    	}
									    } 
									},
									viewrecords : true,
									rowNum : 10,
									rowList : [ 10, 20, 30 ],
									pager : '#ftpgrid-pager',
									caption:ftpGridCaption,
									//Change
									multipleSearch:true,
									toolbar:[true, "top"]
				});
				jQuery("#ftpgrid").jqGrid('navGrid', '#ftpgrid-pager', {
					edit : false,
					add : false,
					del : false
				});
});

setInterval(function() {
	autoRefreshFlg = true;
	$("#ftpgrid").jqGrid('setGridParam', 
		{
			datatype:"json",
			page:1
		}).trigger("reloadGrid");
//}, 10000);//auto refresh the grid every mentioned millisonds	
}, 300000);//auto refresh the grid every mentioned millisonds