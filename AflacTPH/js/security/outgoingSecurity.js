var subGridFlg = false;
$(document).ready(
			function() {
				$("#security")
						.jqGrid(
								{
									url : 'OutSecurityAsJson.do?dest='+$('#selectedSystem').val(),
									datatype : "json",
									autowidth : true,
									loadonce: true,
						            sortable:true,
						            height: gridHeight,
									colNames : [  '', 'Status', 'Source', 'Security Group','CUSIP', 
									              'Security Description', 'Ticker', 
									              'Maturity Date', 'Issuer ID', 
									              'Country', 'Currency', 'Accrual', 'sec id', 'source_OUT', ''],
									colModel : [{
									    name:"check_box",
										index:"check_box",
										width:45,
										search: false
									}, /*{
									    name:"radio_btn",
										index:"radio_btn",
										width:45
									}, */{
										name : 'tph_STATUS',
										index : 'tph_STATUS',
										autowidth : true
									},{
										name : 'source',
										index : 'source',
										autowidth : true
									},{
										name : 'sec_GROUP',
										index : 'sec_GROUP',
										autowidth : true
									},{
										name : 'cusip',
										index : 'cusip',
										autowidth : true
										
									},{
										name : 'description1',
										index : 'description1',
										autowidth : true
										
									},{
										name : 'symb_POOL',
										index : 'symb_POOL',
										autowidth : true
										
									},{
										name : 'maturity_DATE',
										index : 'maturity_DATE',
										autowidth : true,
										sorttype : "date",
										formatter : "date",
										formatoptions: {newformat:'Y/m/d'}
									},{
										name : 'issuer_CD',
										index : 'issuer_CD',
										autowidth : true
										
									},{
										name : 'country',
										index : 'country',
										autowidth : true
									},{
										name : 'prn_CURR',
										index : 'prn_CURR',
										width:170										
									},{
										name : 'accrual',
										index : 'accrual',
										autowidth : true
										
									},{
										name : 'sec_ID',
										index : 'sec_ID',
										hidden: true
									},{
										name : 'source_OUT',
										index : 'source_OUT',
										hidden: true
									},{
										name : 'mapping',
										index : 'mapping',
										width:55,
										search: false
									}],
									sortname: "maturity_DATE",
									sortorder: "desc",
									subGrid: true,
									subGridOptions: {
										"plusicon"  : "ui-icon-triangle-1-e",
								        "minusicon" : "ui-icon-triangle-1-s",
								        "openicon"  : "ui-icon-arrowreturn-1-e",
								    	"reloadOnExpand" : true,
										"selectOnExpand" : true
								    },
									loadComplete: function(data) {
										var $this = $(this);
										if (orig_grid_width == 0) {
											orig_grid_width = $(this).jqGrid('getGridParam', 'width');
										} else {
											$(this).jqGrid('setGridWidth', orig_grid_width);
										}
								        var datatype = $this.getGridParam('datatype');
									    if (datatype === "xml" || datatype === "json") {
									    	//console.log(data);
									    	if(data[0].errorCode != undefined && data[0].errorCode != ""){
									    		$('#errorSection').html(data[0].errorMessage);
									    		$('#errorSection').show();
									    		//Hide empty row and show the error message bottom bar of the grid
									    		$('#security tr').hide();
									    		$('.ui-paging-info').html(data[0].errorMessage);
									    		//remove sortable, coz, on click it will load an empty row
									    		$('.ui-jqgrid-htable tr').removeClass('ui-sortable');
									    		$('.ui-jqgrid-htable tr div').removeClass('ui-jqgrid-sortable');
									    	}else{
									    		$('#errorSection').html("");
									    		$('#errorSection').hide();
										        setTimeout(function () {
										            $this.trigger("reloadGrid");
										            if (autoRefreshFlg == true || resendDiscardFlg == true) {
										            	//console.log("Going to refresh");
										            	resendDiscardFlg = false;
										            	if (autoRefreshFlg == true) {
										            		//console.log("Going add new filter-options");
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
										        }, 100);
									    	}
									    } else {
									    	clearResendDiscardOpr("t_security");
									    }
									    
									    resizeColumns($this);
									    
									    attachErrorToFailedSecurity();
				                        
				                        //Attach data compare link
				                        var mapUrl = "getSecurityMappingDetails.do";
										var mappingFor = "securityOutgoing";
								    	attachLinkForDataComparision($this, mapUrl, "security", mappingFor);//grid, jsonurl, grid_id, mappingFor    
									},
									onSelectRow: function(rowid) {
										if (subGridFlg != true) {
											var row = $(this).getLocalRow(rowid);
										    // do something with row
										    var myGrid = $('#security');
									    	var id = myGrid.jqGrid ('getCell', rowid, 'sec_ID');
									    	var source_OUT = myGrid.jqGrid ('getCell', rowid, 'source_OUT');
									    	
									    	var uri = "OutgoingSecurityDetails.do?sec_id="+id+"&source_out="+source_OUT;
									    	$('#myModal').modal('show');
									    	
									    	getSecurityDetails(id, uri);
									    	var selectedSystem = $('#selectedSystem').val();
									    	var urlStrSecattrGrid = "getOutgoingSecurityAttributeDetails.do?sec_id=" + id+ "&dest="+selectedSystem+"&r="+Math.random();
									    	showSecurityAttributes(urlStrSecattrGrid);
										} else {
											subGridFlg = false;
										}									    
									},
									/*onHeaderClick:function(status) {
										hideToolbar(status, $("#t_security"));
									},*/
									subGridRowExpanded: function(subgrid_id, row_id) {
										subGridFlg = true;
  								    	var myGrid = $('#security');
								    	var issuerCD = myGrid.jqGrid ('getCell', row_id, 'issuer_CD');
								    	var selectedSystem = $('#selectedSystem').val();
								    	var urlStrIssuerGrid = "getOutgoingIssuerDetails.do?issuer_cd=" + issuerCD + "&dest="+selectedSystem;

								    	/*******   Display Issuer Grid  ***********/
								    	var issuer_subgrid_id = subgrid_id + "_issuer_subgrid";
								    	$("#"+subgrid_id).append("<table id='"+issuer_subgrid_id+"' class='scroll'></table>");
								    
									    	jQuery("#"+issuer_subgrid_id).jqGrid({
												url:urlStrIssuerGrid,
												datatype: "json",
												colNames: ['Issuer ID', 'Issuer Name', 'Country', 'State',
												           'Moody\'s Rating', 'S&P Rating', 'Internal Rating','TPH Issuer ID'],
												colModel: [
													{name:"issuer_CD",index:"issuer_CD",autowidth:true},
													{name:"description",index:"description",autowidth:true},
													{name:"country",index:"country",autowidth:true},
													{name:"state",index:"state",autowidth:true},
													{name:"moody_RATE",index:"moody_RATE",autowidth:true},
													{name:"s_P_RATE",index:"s_P_RATE",autowidth:true},
													{name:"int_RATE",index:"int_RATE",autowidth:true},
													{name:"tph_ISSUER_SEQ_NO",index:"tph_ISSUER_SEQ_NO",autowidth:true,hidden:true},
													
												],
												loadComplete: function(data) {
													resizeColumns($(this));
												},
												sortable:false,
											    height: '%100',
											    autowidth:true,
											    caption:"Issuer"
											});
								    
									    	/*******   Display Security Attribute Grid  ***********/
									    /*	var secID = myGrid.jqGrid ('getCell', row_id, 'sec_ID');
									    	
									    	var urlStrSecattrGrid = "getOutgoingSecurityAttributeDetails.do?sec_id=" + secID+ "&dest="+selectedSystem;
									    	var pager_id='security-attribute-pager';
									    	var secattr_subgrid_id = subgrid_id + "_secattr_subgrid";
									    	$("#"+subgrid_id).append("<table id='"+secattr_subgrid_id+"' class='scroll'></table><div id='"+pager_id+"' class='scroll'></div>");
									    
										    	jQuery("#"+secattr_subgrid_id).jqGrid({
													url:urlStrSecattrGrid,
													datatype: "json",
													loadonce: true,
													colNames: ['Attribute Type', 'Date', 'Price', 'Amount'],
													colModel: [
														{name:"attrib_TYPE",index:"attrib_TYPE",autowidth:true},
														{name:"date1",index:"date1",autowidth : true,sorttype : "date",formatter : "date",formatoptions: {newformat:'Y/m/d'}},
														{name:"price",index:"price",autowidth:true},
														{name:"amount",index:"amount",autowidth:true},
													],
													sortable:false,
												    height: '%100',
												    autowidth:true,
												    viewrecords : true,
												    rowNum : 5,
													rowList : [ 5, 10, 20 ],
													pager : "#"+pager_id ,
												    caption:"Security Attributes"
												});
										    	jQuery("#"+secattr_subgrid_id).jqGrid('navGrid', "#"+pager_id, {
													edit : false,
													add : false,
													del : false
												});	*/
								    },
									viewrecords : true,
									rowNum : 20,
									rowList : [20],
									pager : '#security-pager',
									toolbar:[true, "top"],
									caption:"SECURITY",
									hidegrid: false
				});
				jQuery("#security").jqGrid('navGrid', '#security-pager', {
					edit : false,
					add : false,
					del : false
				});	
				setTransTradeTyp(security, outgoing);
				attachToolbarBtns($("#t_security"));
});

function attachErrorToFailedSecurity(){
	var ids =jQuery("#security").jqGrid('getDataIDs');
	for(var i=0;i < ids.length;i++){
	  var rowId = ids[i];
   	  var Status = jQuery("#security").jqGrid('getCell',rowId,'tph_STATUS');
   	  if(Status=='Failed'){
   		var tphSecId = jQuery("#security").jqGrid('getCell',rowId,'sec_ID');
   		var destination=$('#selectedSystem').val();
   			(function(key){
   				$.ajax({
	   			type: "GET",
	   			url: "getErrorMessageSecurity.do",
	   			dataType : "json",
	   	        contentType : "application/json",
	   			cache: false,
	   			data: { 
	   				secRefNo:tphSecId,
	   				dest:destination
	   				},
	   			success: function(errorMSG){
	   				$('#'+key).attr("data-content",errorMSG);
	   			},
	   			error: function(){
	   				$('#'+key).attr("data-content","error while requesting data...");
	   			}
	   		});
   			 //console.debug("Row:"+rowId);
   		    })(rowId);
	   		
   		  
   		
   		$('#'+rowId).attr("rel","popover");
   		$('#'+rowId).attr("data-placement","top");
   		$('#'+rowId).attr("data-original-title","Failed");
   		$('#'+rowId).attr("data-trigger","hover");
   		$('#'+rowId).attr("data-container","body");
 		$('#'+rowId).popover();
 		/*var radio_btn_id = "security" + "_radio_" + rowId;
		var radio_btn = '<input type="radio" id='+radio_btn_id+' enable onclick="clickRadioBtn(this)"/>';
    	$("#security").jqGrid('setRowData', rowId, {radio_btn:radio_btn});*/
 		var check_box_id = "security" + "_checkbox_" + rowId;
		var check_box = '<input type="checkbox" id='+check_box_id+' enable onclick="clickCheckBox(this)"/>';
		$("#security").jqGrid('setRowData', rowId, {check_box:check_box});
      }
	}
}

/*
 * Auto reload grid with new data from server, 
 * it will not reset filter, 
 * after reload it should apply filter(s) that user selected
 * if the filter data is not present in the server for the new response, it will remove the filter as well as the filter effect of it(if selected)
 * if new filter data is present in the server, it will add to the filter section
*/
setInterval(function() {
	autoRefreshFlg = true;
	$("#security").jqGrid('setGridParam', 
		{
			datatype:"json",
			page:1
		}).trigger("reloadGrid");
}, 300000);//auto refresh the grid every mentioned millisonds

