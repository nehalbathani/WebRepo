//var no_failed_row_clicked = 0, failed_row_arr = new Array(0);

$(document).ready(
		reloadGrid = function() {
				$("#trades")
						.jqGrid(
								{
				                    url : 'TradesMngJson.do',
				                    mtype: "POST",
				                    //TODO remove postData element 
				                   /* postData:{
				                    	source: $('#source').val(),
				                    	status: $('#status').val(),
				                    	account: $('#account').val(),
				                    	fromDate: $('#fromDate').val(),
				                    	toDate: $('#toDate').val(),
				                    	rcvdDate: $('#rcvdDate').val() },*/
				                    datatype : "json",
									autowidth : true,
									loadonce: true,
						            sortable:true,
									height: gridHeight,
									colNames : ['', 'Status', 'Source','TPH Trade No', 
									            'Type', 'Broker', 'Portfolio', 'CUSIP', 
											    'Security Group', 'Security Type', 'Trade Date',
											    'Settle Date', 'Execution Price', 'Quantity',
											    'Settlement Amount', 'Execution Currency',
											    'Fixed Currency', 'Float Currency', 'Touch Count','Trade Ref No','',''],
									colModel : [{
									    name:"check_box",
										index:"check_box",
										width:45,
										search: false
									}, {
										name : 'status',
										index : 'status',
										autowidth : true,
										sorttype:'string',
									}, {
										name : 'source',
										index : 'source',
										autowidth : true,
										sorttype:'string' 
										
									}, {
										name : 'tph_TRD_SEQ_NO',
										index : 'tph_TRD_SEQ_NO',
									    //autowidth : true,
										hidden:true,
										sorttype:'float'
									}, {
										name : 'transactionType',
										index : 'transactionType',
										autowidth : true,
										sorttype:'string'
									}, {
										name : 'brokerCode',
										index : 'brokerCode',
										autowidth : true,
										sorttype:'string',
									}, {
										name : 'acct_CD',
										index : 'acct_CD',
										autowidth : true,
										sorttype:'string' 
									}, {
										name : 'cusip',
										index : 'cusip',
										//width:50,
										autowidth : true,
										sorttype:'string' 
									}, {
										name : 'securityGroup',
										index : 'securityGroup',
										autowidth : true,
										sorttype:'string'
									}, {
										name : 'securityType',
										index : 'securityType',
										autowidth : true,
										sorttype:'string' 
									}, {
										name : 'tradeDate',
										index : 'tradeDate',
										autowidth : true,
										sorttype : "date",
										formatter : "date",
										formatoptions: {newformat:'Y/m/d'}
									}, {
										name : 'settleDate',
										index : 'settleDate',
										autowidth : true,
										sorttype : "date",
										formatter : "date",
										formatoptions: {newformat:'Y/m/d'}
									}, {
										name : 'executionPrice',
										index : 'executionPrice',
										autowidth : true,
										sorttype:'float',
										formatter:'number',
										formatoptions:{defaultValue:'', decimalPlaces: 3 },
									    align:'right'
									},{
										name : 'executionQty',
										index : 'executionQty',
										sorttype:'float',
										formatoptions:{defaultValue:'', decimalPlaces: 2 },
										formatter:amountFormatter,
										align:'right'										
									}, {
										name : 'executionAmount',
										index : 'executionAmount',
										sorttype:'float',
										formatoptions:{defaultValue:'', decimalPlaces: 2 },
										formatter:amountFormatter,
										align:'right'										
									}, {
										name : 'executionCurrency',
										index : 'executionCurrency',
										width:170,
										sorttype:'string' 
									}, {
										name : 'fixedCurrency',
										index : 'fixedCurrency',
										width : '100%',
										sorttype:'string' ,
										hidden:true,
										search: false
									}, {
										name : 'floatCurrency',
										index : 'floatCurrency',
										autowidth : true,
										sorttype:'string',
										hidden:true,
										search: false
									},{
										name : 'touch_COUNT',
										index : 'touch_COUNT',
										hidden:true,
										search: false
										//autowidth : true
									},
									{
										name : 'trd_REF_NO',
										index : 'trd_REF_NO',
										hidden:true,
										search: false
										//autowidth : true
									},
									{name : 'mapping',index : 'mapping', width:50, search:false},
									{name : 'ManuAckIcon',index : 'ManuAckIcon',width:50, search:false, 
										formatter: function (cellvalue, options, rowObject) {
											//console.log(rowObject["status"]);
											if(rowObject["status"]=='Failed' || rowObject["status"]=='Manual'){
												return "<input name='btnManuAck' style='border: 0 none; width: 16px;height:16px;margin:0.5px 0px 0px -1px;float:left;' class='btn-ack' type='button' title='Acknowledge Manual'/>";
											}
											else{
												return "";
											}
										}
											
									}
									],//class='btn-ack' 
									sortname: "tradeDate",
									sortorder: "desc",
									grouping : true,
									groupingView : {
										groupField : [ 'tradeDate' ],
										groupColumnShow : [true],
										groupText : ['{0} - <font size="2"><span class="label label-info">{1} Trade(s)</span></font>'],
										groupOrder: ['desc']
									},
									loadComplete: function(data) {
										//console.log("at loadComplete()");
										var $this = $(this);
										if (orig_grid_width == 0) {
											orig_grid_width = $(this).jqGrid('getGridParam', 'width');
										} else {
											$(this).jqGrid('setGridWidth', orig_grid_width);
										}
								        var datatype = $this.getGridParam('datatype');
										//console.log("at loadComplete() datatype:" + datatype);
									    if (datatype === "xml" || datatype === "json") {
									    	if(data[0].errorCode != undefined && data[0].errorCode != ""){
									    		$('#errorSection').html(data[0].errorMessage);
									    		$('#errorSection').show();
									    		//Hide empty row and show the error message bottom bar of the grid
									    		$('#trades tr').hide();
									    		$('.ui-paging-info').html(data[0].errorMessage);
									    		//remove sortable, coz, on click it will load an empty row
									    		$('.ui-jqgrid-htable tr').removeClass('ui-sortable');
									    		$('.ui-jqgrid-htable tr div').removeClass('ui-jqgrid-sortable');
									    	}else{
									    		$('#errorSection').html("");
									    		$('#errorSection').hide();
									    		
									    		setTimeout(function () {
										    	    //TODO why we need this reloadGrid?
										            $this.trigger("reloadGrid");
										            if (autoRefreshFlg == true || resendDiscardFlg == true) {
										            	//console.log("Going to refresh");
										            	resendDiscardFlg = false;
										            	if (autoRefreshFlg == true) {
										            		//console.log("Going add new filter-options");
										            		autoRefreshFlg = false;
															includeNewSource(data);
															includeNewAccount(data);
															includeNewSecurityGroup(data);
															includeNewTradeDate(data);
										            	}
										            	includeNewStatus(data);
										            	commonFilterCall();
										            } else if (resendDiscardFlg != true) {
										            	//console.log("going to call loadFilterValue");
										            	loadFilterValue(data);
										             	var drillDownStatus = getQueryVariable("drillDown");
										            	var drillDownSource = getQueryVariable("source");
												    	if (drillDownStatus != null && drillDownSource != null) {
												    		loadGridForDashBoardFilter(drillDownStatus, drillDownSource);
												    		commonFilterCall();
												    	}
										            } 
										        }, 100);
									    	}
									    } else {
									    	//console.log("going to clear toolbar");
									    	clearResendDiscardOpr("t_trades");
									    }
									    
									    resizeColumns($this);
									    
									    var statusArr = ["failed", "Failed"];
									    attachCheckBox($this, "trades", statusArr);

									    var mapUrl = "getTradeMappingDetails.do";
										var mappingFor = "trade";
								    	attachLinkForDataComparision($this, mapUrl, "trade", mappingFor);//grid, jsonurl, grid_id, mappingFor
									},
									viewrecords : true,
									rowNum : 20,
									rowList : [20],
									subGrid: true,
									subGridOptions: {
										"plusicon"  : "ui-icon-triangle-1-e",
								        "minusicon" : "ui-icon-triangle-1-s",
								        "openicon"  : "ui-icon-arrowreturn-1-e",
								    	"reloadOnExpand" : false,
										"selectOnExpand" : true
								    },
								    onSelectRow: function(rowId) {
								    	var myGrid = $('#trades');
								    	myGrid.expandSubGridRow(rowId);
									},
									subGridRowExpanded: function(subgrid_id, row_id) {
								    	var myGrid = $('#trades');
								    	//myGrid.jqGrid('setSelection', row_id); 
								    	var touchCnt = myGrid.jqGrid ('getCell', row_id, 'touch_COUNT');
								    	var TPHTrdNo = myGrid.jqGrid ('getCell', row_id, 'tph_TRD_SEQ_NO');
								    	var securityGroup = myGrid.jqGrid ('getCell', row_id, 'securityGroup');
								    	var transactionType = myGrid.jqGrid ('getCell', row_id, 'transactionType');
										var subgrid_table_id = subgrid_id + "_subgrid";
										if(securityGroup==null || securityGroup.trim()==""){
											if(transactionType.trim()=="FX FWRD" && securityGroup.trim()==""){
												securityGroup="FX";
											}
											else{
												//Specifying default view as Bond View
												securityGroup="BND";
											}
										}
								    	$("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table>");
								    	if(securityGroup=="B" || securityGroup=="BND"){
									    	jQuery("#"+subgrid_table_id).jqGrid({
												url:"getTradesForOrder.do?touch_count="+touchCnt+"&order_ref_no="+TPHTrdNo,
												datatype: "json",
												colNames: ['Security Description','Quantity','Execution Price',
												           'Prinicipal Amt',
												           'Accrued Interest','Settlement Amt','Coupon','Trade ID','Touch Count','TPH Trade No', ''
												           ,'Status'/*,
												           'Portfolio ID','Type','CUSIP','Security Type',
												           'Trade Date',
												           'Settlement Date','Broker Code','Execution Curr'*/],
												colModel: [
													{name:"desc_INSTMT", index:"desc_INSTMT", autowidth:true},
													{name:"trd_TD_PAR",index:"trd_TD_PAR",autowidth:true,formatoptions:{defaultValue:'', decimalPlaces: 2 }, formatter:amountFormatter, align:'right'},
													{name:"trd_PRICE",index:"trd_PRICE",autowidth:true,formatter:'number',	formatoptions:{defaultValue:'', decimalPlaces: 3 }, align:'right'},
													{name:"trd_PRINCIPAL",index:"trd_PRINCIPAL",autowidth:true,formatoptions:{defaultValue:'', decimalPlaces: 2 }, formatter:amountFormatter, align:'right'},
													{name:"trd_INTEREST",index:"trd_INTEREST",autowidth:true,formatoptions:{defaultValue:'', decimalPlaces: 2 }, formatter:amountFormatter, align:'right'},
													{name:"settlementAmount",index:"settlementAmount",autowidth:true,formatoptions:{defaultValue:'', decimalPlaces: 2 }, formatter:amountFormatter, align:'right'},
													{name:"trd_COUPON",index:"trd_COUPON",autowidth:true, align:'right'},
													{name:"trd_REF_NO",index:"trd_REF_NO",autowidth:true, align:'right'},
													{name:"touch_COUNT",index:"touch_COUNT",autowidth:true, align:'right'},
													{name:"tph_TRD_SEQ_NO",index:"tph_TRD_SEQ_NO",hidden:true},
													{name : 'source',index : 'source',hidden:true},													
													{name:"status",index:"status",autowidth:true,hidden:true}/*,
													{name:"portfolios_PORTFOLIO_NAME",index:"portfolios_PORTFOLIO_NAME",autowidth:true,hidden:true},
													{name:"tran_TYPE",index:"tran_TYPE",autowidth:true,hidden:true},
													{name:"cusip",index:"cusip",autowidth:true,hidden:true},
													{name:"sec_TYPE_CD",index:"sec_TYPE_CD",autowidth:true,hidden:true}, 
													{name:"trade_DATE",index:"trade_DATE",autowidth:true,formatoptions: {newformat:'Y/m/d'},hidden:true},
													{name:"settle_DATE",index:"settle_DATE",autowidth:true,formatoptions: {newformat:'Y/m/d'},hidden:true},
													{name:"broker_CD",index:"broker_CD",autowidth:true, hidden:true},
													{name:"trd_CURRENCY",index:"trd_CURRENCY",autowidth:true,hidden:true}*/
												],
												sortable:false,
											    height: '100%',
											    autowidth:true,
											    loadComplete: function(data) {
											    	resizeColumns($(this));
											    	//TODO remove $this from parameter list
									                applyLabelForTradeTicket($(this),subgrid_id);

											    },
												onSelectRow: function(rowid) {
									    			var subgrid = jQuery("#"+subgrid_table_id);
									    			
											    	var touchCount = subgrid.jqGrid ('getCell', rowid, 'touch_COUNT');
											    	var tph_trd_no = subgrid.jqGrid ('getCell', rowid, 'tph_TRD_SEQ_NO');
											    	var ticket_type = "BND";
											    	var system=$('#selectedSystem').val();
									    			window.open('getTradeReport.do?tph_trd_no='+tph_trd_no+'&touch_count='+touchCount+'&ticket_type='+ticket_type+'&system='+system,'Popup','toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes, width=800,height=600,left=230,top=23');
									    		}
									    		 
											});
								    	} else if(securityGroup=="FX"){
								    		jQuery("#"+subgrid_table_id).jqGrid({
												url:"getTradesForOrder.do?touch_count=1&order_ref_no="+TPHTrdNo,
												datatype: "json",
												colNames: ['Security Description','FWD RATE', 'Fixed Curr','Fixed Amt','Float Curr','Float Amt','Trade Ref No','Touch Count',
												           'TPH Trade No', ''
												           ,'Status'/*,'Portfolio ID','Type', 'Trade Date','Settlement Date','CUSIP','Security Type','Broker Code','Execution Curr'*/],
												colModel: [
	                                                {name:"desc_INSTMT",index:"DESC_INSTMT", width:350},
													{name:"fx_PRICE",index:"fx_PRICE",autowidth:true,formatter:'number',	formatoptions:{defaultValue:'', decimalPlaces: 8 }, align:'right'},
													{name:"fx_PAY_CURR",index:"fx_PAY_CURR",autowidth:true},
													{name:"fx_PAY_AMT",index:"fx_PAY_AMT",autowidth:true,formatoptions:{defaultValue:'', decimalPlaces: 2 }, formatter:amountFormatter, align:'right'},
													{name:"fx_RCV_CURR",index:"fx_RCV_CURR",autowidth:true},
													{name:"fx_RCV_AMT",index:"fx_RCV_AMT",autowidth:true,formatoptions:{defaultValue:'', decimalPlaces: 2 },	formatter:amountFormatter, align:'right'},
													{name:"trd_REF_NO",index:"trd_REF_NO",autowidth:true, align:'right'},
	                                                {name:"touch_COUNT",index:"touch_COUNT",autowidth:true, align:'right'},
	                                                {name:"tph_TRD_SEQ_NO",index:"tph_TRD_SEQ_NO",hidden:true},
													{name : 'source',index : 'source',hidden:true},
													{name:"status",index:"status",autowidth:true,hidden:true}/*,
	                                                {name:"portfolios_PORTFOLIO_NAME",index:"portfolios_PORTFOLIO_NAME",autowidth:true},
													{name:"tran_TYPE",index:"tran_TYPE",autowidth:true},
													{name:"trade_DATE",index:"trade_DATE",autowidth:true,formatoptions: {newformat:'Y/m/d'}},
													{name:"settle_DATE",index:"settle_DATE",autowidth:true,formatoptions: {newformat:'Y/m/d'}},
													{name:"cusip",index:"cusip",autowidth:true},
													{name:"sec_TYPE_CD",index:"SEC_TYPE_CD",autowidth:true},
													{name:"broker_CD",index:"broker_CD",autowidth:true, hidden:true},
													{name:"trd_CURRENCY",index:"TRD_CURRENCY",autowidth:true}
													*/
												],
												sortable:false,
											    height: '100%',
											    autowidth:true,
											    loadComplete: function(data) {
											    	resizeColumns($(this));
											    	//TODO remove $this from parameter list
									                applyLabelForTradeTicket($(this),subgrid_id);
											    },
												onSelectRow: function(rowid,status,e) {
									    			var subgrid = jQuery("#"+subgrid_table_id);
									    			
											    	var touchCount = subgrid.jqGrid ('getCell', rowid, 'touch_COUNT');
											    	var tph_trd_no = subgrid.jqGrid ('getCell', rowid, 'tph_TRD_SEQ_NO');
											    	var ticket_type = "FX";
											    	var system=$('#selectedSystem').val();
									    			window.open('getTradeReport.do?tph_trd_no='+tph_trd_no+'&touch_count='+touchCount+'&ticket_type='+ticket_type+'&system='+system,'Popup','toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes, width=800,height=600,left=230,top=23');
									    		},
									    		toolbar: [true,"top"]
											});
								    	}
								    },
								    /*onHeaderClick:function(status) {
								    	hideToolbar(status, $("#t_trades"));
									},*/
									pager : '#trades-pager',
									caption:"Trades",
									ignoreCase: true,
									multipleSearch:true,
									toolbar: [true, "top"],
									hidegrid: false
				});
				jQuery("#trades").jqGrid('navGrid', '#trades-pager', {
					edit : false,
					add : false,
					del : false
				});
				setTransTradeTyp(trade, incoming);
				attachToolbarBtns($("#t_trades"));
});
//Hi I am here.
/*
 * Auto reload grid with new data from server, 
 * it will not reset filter, 
 * after reload it should apply filter(s) that user selected
 * if the filter data is not present in the server for the new response, it will remove the filter as well as the filter effect of it(if selected)
 * if new filter data is present in the server, it will add to the filter section
*/
setInterval(function() {
	autoRefreshFlg = true;
	$("#trades").jqGrid('setGridParam', 
		{
			datatype:"json",
			page:1
		}).trigger("reloadGrid");
},300000);//auto refresh the grid every mentioned millisonds

/*function attachLink(grid, grid_id, jsonurl) {
	var gridRowIds = grid.getDataIDs();
	gridRowIds.forEach(function(val, index) {
		var gridRowId = val;
		var tphSeqNo = grid.jqGrid ('getCell', gridRowId, 'tph_TRD_SEQ_NO'); 
		var tphSource = grid.jqGrid ('getCell', gridRowId, 'source');
		var tphTouchCount = grid.jqGrid ('getCell', gridRowId, 'touch_COUNT');
		
		
		var btn_id = grid_id + "_mapping_" + tphSeqNo;
		var btn = '<input type="button" id="'+btn_id+'" class="mapping-btn-icon" onclick="clickMappingBtn('+gridRowId+', \''+jsonurl+'\', \''+tphSeqNo+'\', \''+tphSource+'\', \''+tphTouchCount+'\')" title="See Mapping"/>';
		grid.jqGrid('setRowData', gridRowId, {mapping:btn});
		
	});
}

function clickMappingBtn(gridRowId, url, refNo, source, touch_count){
	$('#tradeModal').modal('show');
	var uri = url + "?" + "refNo=" + refNo + "&source=" + source + "&touch_count=" + touch_count;
	$.ajax({
        type: "GET",
        url: uri,
        accepts : "json",
        mimeType: 'application/json',
        crossDomain: true,//TODO- what is the need for cross domian before send
        beforeSend: function(xhr) {  
            xhr.setRequestHeader("Accept", "application/json");  
            xhr.setRequestHeader("Content-Type", "application/json");  
        },
        success: function( response ){
        	//console.log(response);
        	var html = "";
        	var radioHtml = "";
        	var outgoingHtml = "";
        	if(response[0].errorMessage != undefined && response[0].errorMessage != ""){
        		$('#tradeModalRadioSection').html("");
    	    	$('#tradeModalErrorSection').html(response[0].errorMessage);   
    	    	$('#tradeModalTable').hide();
	    	}else{
	    		$('#tradeModalErrorSection').html("");   
    	    	$('#tradeModalTable').show();
	    		for(var reslen = 0; reslen<response.length; reslen++ ) {
	    			radioHtml = "Compare <input type='radio' name='incoming' value='Incoming' disabled='disabled' checked='checked' >&nbsp;Incoming</input>&nbsp;&nbsp; with &nbsp;&nbsp;";
	    			
	    			html += "<tr>";
		    		html += "<th>TAG</th>";
		    		html += "<th colspan=2 textalign=center>Incoming</th>";
		    		dest = response[reslen].destinationBean;
	    			for(key in dest) {
	    				radioHtml +="<input type='radio' name='outgoing' class='outgoingRadio' value='"+key+"' title='Only one can be selected.'>&nbsp;"+key+"</input>&nbsp;&nbsp;";	    				
	    			}
		    		html += "</tr>";
	    			break;
	    		}
	    		$('#tradeModalRadioSection').html(radioHtml);
	    		
	    		for(var reslen = 0; reslen<response.length; reslen++ ) {
		    		html += "<tr>";
		    			html += "<td>";
		    				html += response[reslen].businessName;
		    			html += "</td>";
		    			html += "<td>";
		    				html += response[reslen].sourceTag;
		    			html += "</td>";
		    			html += "<td>";
		    				html += response[reslen].sourceTagValue;
		    			html += "</td>";
		    	  	html += "</tr>";
	    		}
	    		
	    		for(var reslen = 0; reslen<response.length; reslen++ ) {
	    			dest = response[reslen].destinationBean;
	    			for(key in dest) { 
	    				if(key == $('.outgoingRadio:first').val()){
	    					if(reslen == 0){
	    	    				outgoingHtml += "<tr>";
	    	    				outgoingHtml += "<th colspan=2>"+key+"</th>";
	    	    				outgoingHtml += "</tr>";
	    	    			}
	    					
		    				outgingVal = dest[key];
		    				outgoingHtml += "<tr>";
		    				outgoingHtml += "<td>";
		    					outgoingHtml += outgingVal.destTag;
		    				outgoingHtml += "</td>";
			    			
		    				outgoingHtml += "<td>";
		    					outgoingHtml += outgingVal.destTagValue;
		    				outgoingHtml += "</td>";
		    				outgoingHtml += "</tr>";
	    				}
	    			}
	    		}
	    	  	
	    	  	$('#tradeModalTableBody').html(html);
	    	  	$('#tradeOutgoingModalTableBody').html(outgoingHtml);
	    	  	$('.outgoingRadio:first').attr('checked', true);
	    	  	
	    	  	$('.outgoingRadio').click(function(){		    		
	    	  		outgoingHtml="";		    		
		    		for(var reslen = 0; reslen<response.length; reslen++ ) {
		    			dest = response[reslen].destinationBean;
		    			for(key in dest) { 
		    				if(key == $(this).val()){
		    					if(reslen == 0){
		    	    				outgoingHtml += "<tr>";
		    	    				outgoingHtml += "<th colspan=2>"+key+"</th>";
		    	    				outgoingHtml += "</tr>";
		    	    			}
		    					
			    				outgingVal = dest[key];
			    				outgoingHtml += "<tr>";
			    				outgoingHtml += "<td>";
			    					outgoingHtml += outgingVal.destTag;
			    				outgoingHtml += "</td>";
				    			
			    				outgoingHtml += "<td>";
			    					outgoingHtml += outgingVal.destTagValue;
			    				outgoingHtml += "</td>";
			    				outgoingHtml += "</tr>";
		    				}
		    			}
		    		}
		    	  	
		    	  	$('#tradeOutgoingModalTableBody').html(outgoingHtml);
	    	  	});
	    	}
        },
        error: function(jqXHR,error, errorThrown) {  
        	//TODO- what happens if server is down
        	//alert(error + " " + jqXHR.status);
        }
	});
}*/