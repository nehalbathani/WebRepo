/*function fixSearchOperators() {
    var $grid = $("#trades"),
        columns = $grid.jqGrid ('getGridParam', 'colModel'),
        filterToolbar = $($grid[0].grid.hDiv).find("tr.ui-search-toolbar");
        
    filterToolbar.find("th").each(function(index) {
        var $searchOper = $(this).find(".ui-search-oper");
        var $clearOper = $(this).find(".ui-search-clear");
        $clearOper.hide();
    });
}*/
var expManBtnFlg = false, statusArr = new Array(), secGrpArr = new Array();

$(document).ready(
			function() {
				//TODO please check if this is getting called
				 initDateSearch = function (elem) {
	                    setTimeout(function () {
	                        var $myGrid = $(elem).closest('.ui-jqgrid-view').find('table.ui-jqgrid-btable:first');
	                        $(elem).datepicker({
	                            dateFormat: 'yy/mm/dd',
	                            autoSize: true,
	                            changeYear: true,
	                            changeMonth: true,
	                            showWeek: true,
	                            showButtonPanel: true,
	                            onSelect: function () {
	                                if (this.id.substr(0, 3) === "gs_") {
	                                    // call triggerToolbar only in case of searching toolbar
	                                    setTimeout(function () {
	                                        var triggerToolbar = $myGrid[0].triggerToolbar;
	                                        if ($.isFunction(triggerToolbar)) {
	                                            triggerToolbar();
	                                        }
	                                    }, 100);
	                                }
	                            }
	                        });
	                    }, 100);
	                };
				var URL='OutTradeAsJson.do?dest='+$('#selectedSystem').val();
				$("#trades")
						.jqGrid(
								{
									url : URL,
									datatype : "json",
									 mtype: "POST",
									 //TODO please clarify if this is required
					                 /*   postData:{
					                    	source: $('#source').val(),
					                    	status: $('#status').val(),
					                    	account: $('#account').val(),
					                    	fromDate: $('#fromDate').val(),
					                    	toDate: $('#toDate').val(),
					                    	rcvdDate: $('#rcvdDate').val() },*/
									autowidth : true,
									loadonce: true,
									sortable:true,
									height: gridHeight,
									colNames : [ '', 'Status', 'Source', 
											     'Type', 'Broker', 'Portfolio', 'CUSIP',
											     'Security Group', 'Security Type',
											     'Trade Date', 'Settle Date', 'Execution Price', 
											     'Quantity', 'Settlement Amount','', 
											     '', '', '',''],
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
										autowidth : true,
										sorttype:'string'
									}, {
										name : 'source',
										index : 'source',
										autowidth : true,
										sorttype:'string'
									},  {
										name : 'tradeType',
										index : 'tradeType',
										autowidth : true,
										sorttype:'string'
									}, {
										name : 'broker',
										index : 'broker',
										autowidth : true,
										sorttype:'string'
									}, {
										name : 'acct_CD',
										index : 'acct_CD',
										autowidth : true,
										sorttype:'string'
									}, {
										name : 'cusip',
										index : 'cusip',
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
										name:"exec_PRICE", 
										index:"exec_PRICE",
										autowidth:true,
										formatter:'number',
										formatoptions:{defaultValue:'', decimalPlaces: 3 },
										align:'right'
									}, {
										name:"exec_QTY",
										index:"exec_QTY",
										autowidth:true,
										formatoptions:{defaultValue:'', decimalPlaces: 2 },
										formatter:amountFormatter,
										align:'right'
									}, {
										name:"exec_AMT",
										index:"exec_AMT",
										autowidth:true,
										formatoptions:{defaultValue:'', decimalPlaces: 2 },
										formatter:amountFormatter,
										align:'right'
									},
									{name : 'mapping', index : 'mapping', width:55,search:false},
									{name : 'ManuAckIcon',index : 'ManuAckIcon',width:50,search:false , 
										formatter: function (cellvalue, options, rowObject) {
											//console.log(rowObject["status"]);
											if(rowObject["tph_STATUS"]=='Failed' || rowObject["tph_STATUS"]=='Manual'){
												return "<input name='btnManuAck' type='button' style='border: 0 none; width:16px;height:16px;margin:0.5px 0px 0px 0.5px;float:left;' class='btn-ack' title='Acknowledge Manual'/>";
											}
											else{
												return "";
											}
										}
											
									},
									{
										name : 'destination',
										index : 'destination',
										width:20,
										hidden:true,
										search: false
									}, {
										name : 'tphtradeNo',
										index : 'tphtradeNo',
										width:20,
										hidden:true,
										search: false
									}, {
										name : 'touch_COUNT',
										index : 'touch_COUNT',
										width:20,
										hidden:true,
										search: false
									}],
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
										var $this = $(this);
										if (orig_grid_width == 0) {
											orig_grid_width = $(this).jqGrid('getGridParam', 'width');
										} else {
											$(this).jqGrid('setGridWidth', orig_grid_width);
										}
								        var datatype = $this.getGridParam('datatype');
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
										            	loadFilterValue(data);
										            	var drillDownStatus = getQueryVariable("drillDown");
										            	var drillDownSource = getQueryVariable("source");
												    	if(drillDownStatus != null && drillDownSource != null){
												    		loadGridForDashBoardFilter(drillDownStatus, drillDownSource);
												    		commonFilterCall();
												    	}
										            }
										            expManBtnFlg = false;
											        attchExpManToNav(data);
										        }, 100);
									    	}
									    } else {
									    	//console.log("data is:" + data.length);
									    	clearResendDiscardOpr("t_trades");
									    }
									    
									    resizeColumns($this);
									    
									    //TODO change the method name
									    attachErrorToFailedTrades();
									    var mapUrl = "getTradeMappingDetails.do";
										var mappingFor = "tradeOutgoing";
								    	attachLinkForDataComparision($this, mapUrl, "trade", mappingFor);//grid, jsonurl, grid_id, mappingFor
									},
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
								    	
								    	var destination = myGrid.jqGrid ('getCell', row_id, 'destination');
								    	var tradeRefNo = myGrid.jqGrid ('getCell', row_id, 'tphtradeNo');
								    	var transactionType = myGrid.jqGrid ('getCell', row_id, 'tradeType');
								    	var securityGroup = myGrid.jqGrid ('getCell', row_id, 'securityGroup');
								    	var touchCount = myGrid.jqGrid ('getCell', row_id, 'touch_COUNT');
								    	var sourceIn = myGrid.jqGrid ('getCell', row_id, 'source');
										
								    	var urlStr = "getTradeDetails.do?trade_ref_id=" + tradeRefNo + 
								    	             "&tran_type=" + transactionType + "&touch_count=" + touchCount + "&source_out=" + destination+"&source_in="+sourceIn;

								    	var subgrid_table_id = subgrid_id + "_subgrid";
								    	$("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table>");
								    	if (securityGroup=="B" || securityGroup=="BND") {
									    	jQuery("#"+subgrid_table_id).jqGrid({
												url:urlStr,
												datatype: "json",
												 
												colNames: [ 'Security Description','Execution Quantity', 'Execution Price',
												            'Prinicipal Amt',
												            'Accrued Interest','Settlement Amt','Coupon', 'Ext Sys Ref No','Touch Count','TPH Trade No'],
												colModel: [
													{name:"description1",index:"description1",autowidth:true},
													{name:"exec_QTY",index:"exec_QTY",autowidth:true,formatoptions:{defaultValue:'', decimalPlaces: 2 },formatter:amountFormatter, align:'right'},
													{name:"exec_PRICE",index:"exec_PRICE",autowidth:true, formatter:'number',formatoptions:{defaultValue:'', decimalPlaces: 3 }, align:'right'},
													{name:"exec_AMT",index:"exec_AMT",autowidth:true,formatoptions:{defaultValue:'', decimalPlaces: 2 },formatter:amountFormatter, align:'right'},
													{name:"accr_INT_AMT",index:"accr_INT_AMT",autowidth:true, align:'right'},
													{name:"settlementAmount",index:"settlementAmount",autowidth:true, align:'right'},
													{name:"cpn_RATE",index:"cpn_RATE",autowidth:true, align:'right'},
													{name:"ext_SYS_REF_NO",index:"ext_SYS_REF_NO",autowidth:true, align:'right'},
													{name:"touch_COUNT",index:"touch_COUNT",autowidth:true, align:'right'},
													{name:"tph_TRD_SEQ_NO",index:"tph_TRD_SEQ_NO",hidden:true}
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
									    			window.open('getTradeReport.do?tph_trd_no='+tph_trd_no+'&touch_count='+touchCount+'&ticket_type='+ticket_type+'&system='+system+'&tran_type='+transactionType,'Popup','toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes, width=800,height=600,left=230,top=23');
									    		}
									    	
											});
								    	} else if (securityGroup == "" || securityGroup == null || 
								    			securityGroup=="FX" || securityGroup=="fx") {
								    		jQuery("#"+subgrid_table_id).jqGrid({
												url:urlStr,
												datatype: "json",
												colNames: [ 'Security Description', 
												           'FWD RATE', 'Spot Rate', 
												           'From Currency', 'From Amount', 
												           'To Currency', 'To Amount', 'Ext Sys Ref No','Touch Count', ''],
												colModel: [
													{name:"description1",index:"description1",autowidth:true},
													{name:"fx_RATE",index:"fx_RATE",autowidth:true,formatter:'number',	formatoptions:{defaultValue:'', decimalPlaces: 8 }, align:'right'},
													{name:"spot_RATE",index:"spot_RATE",autowidth:true,formatter:'number',	formatoptions:{defaultValue:'', decimalPlaces: 8 }, align:'right'},
													{name:"from_CURRENCY",index:"from_CURRENCY",autowidth:true},
													{name:"from_AMOUNT",index:"from_AMOUNT",autowidth:true,formatoptions:{defaultValue:'', decimalPlaces: 2 }, formatter:amountFormatter, align:'right'},
													{name:"to_CURRENCY",index:"to_CURRENCY",autowidth:true},
													{name:"to_AMOUNT",index:"to_AMOUNT",autowidth:true,formatoptions:{defaultValue:'', decimalPlaces: 2 }, formatter:amountFormatter, align:'right'},
													{name:"ext_SYS_REF_NO",index:"ext_SYS_REF_NO",autowidth:true, align:'right'},
													{name:"touch_COUNT",index:"touch_COUNT",autowidth:true, align:'right'},
													{name:"tph_TRD_SEQ_NO",index:"tph_TRD_SEQ_NO",hidden:true}
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
									    			window.open('getTradeReport.do?tph_trd_no='+tph_trd_no+'&touch_count='+touchCount+'&ticket_type='+ticket_type+'&system='+system+'&tran_type='+transactionType,'Popup','toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes, width=800,height=600,left=230,top=23');
									    		}
											});
								    	}
								    },
									/*onHeaderClick:function(status) {
										hideToolbar(status, $("#t_trades"));
									},*/
									viewrecords : true,
									rowNum : 20,
									rowList : [20],
									pager : '#trades-pager',
									caption:"TRADES",
									ignoreCase: true,
									multipleSearch:true,
									toolbar:[true, "top"],
									hidegrid: false
								});
				jQuery("#trades").jqGrid('navGrid', '#trades-pager', {
					edit : false,
					add : false,
					del : false
				});
				setTransTradeTyp(trade, outgoing);
				attachToolbarBtns($("#t_trades"));
			});

function attchExpManToNav(gridData) {
	//console.log("at attchExpManToNav");
	for (var len = 0; len < gridData.length; len++) {
		var gridRow = gridData[len];
		var status = gridRow.tph_STATUS;
		if (status == 'Manual' || status == 'manual') {
			var secGrp = gridRow.securityGroup;
			if (secGrp == null || secGrp == 'FX' || secGrp == 'fx') {
				expManBtnFlg = true;
				attachExpManBtn();
				break;
			}
		}
	}	
}

//TODO change the method name
function attachExpManBtn() {
	jQuery("#trades").jqGrid('navButtonAdd', '#trades-pager', 
			   { 
				  id:"export_manual",
				  caption:"",
				  title:"Export Manual Trades in Excel",
				  buttonicon:"ui-icon-refresh",
				  onClickButton:function() {
					  var tradeIds = getManualTrades();
					  callExportManualAjax(tradeIds);
				  }, 
				  position: "last" 
			   }
	);
	var expManId = $("#export_manual");
	var appliedClass = expManId.find('span').attr('class');
	expManId.find('span').removeClass(appliedClass);
	expManId.find('span').addClass('export-manual-btn-icon');
}

function getManualTrades() {
	var tradeIds = "";
	var gridData = $("#trades").jqGrid('getGridParam', 'data');
	for (var len = 0; len < gridData.length; len++) {
		var gridRow = gridData[len];
		var status = gridRow.tph_STATUS;
		if (status == 'Manual' || status == 'manual') {
			var secGrp = gridRow.securityGroup;
			if (secGrp == null || secGrp == 'FX' || secGrp == 'fx') {
				tradeIds += gridRow.tphtradeNo + "_";
			}
		}
	}
	tradeIds = tradeIds.substring(0, tradeIds.length - 1);
	return tradeIds;
}

/*TODO change the method name
 * change the function call from using get query string
*/
function callExportManualAjax(tradeIds) {
	//console.log("tradeIds:" + tradeIds);
	window.location.replace('ManulTradesExcelReport.do?trade_seq_nos='+tradeIds);
}
//TODO change the method name
function enableDisableExpManBtn() {
	if (expManBtnFlg == true) {
		var statusManFlg = false;
		for (var id = 0; id < statusArr.length; id++) {
			var status = statusArr[id];
			if (status == 'Manual' || status == 'manual') {
				statusManFlg = true;
				break;
			}
		}
		var secGrpFxFlg = false;
		for (var ind = 0; ind < secGrpArr.length; ind++) {
			var secGrp = secGrpArr[ind];
			if (secGrp == 'null' || secGrp == 'FX' || secGrp == 'fx') {
				secGrpFxFlg = true;
				break;
			}
		}
		if (secGrpFxFlg == true && statusManFlg == true) {
			$("#export_manual").show();
		} else if (secGrpFxFlg == true && statusArr.length == 0) {
			$("#export_manual").show();
		} else if (secGrpArr.length == 0 && statusManFlg == true) {
			$("#export_manual").show();
		} else if (secGrpArr.length == 0 && statusArr.length == 0) {
			$("#export_manual").show();
		} else {
			$("#export_manual").hide();
		}
		statusArr = new Array();
		secGrpArr = new Array();
	}
}

function attachErrorToFailedTrades(){
	var ids =jQuery("#trades").jqGrid('getDataIDs');
	for(var i=0;i < ids.length;i++){
	  var rowId = ids[i];
   	  var Status = jQuery("#trades").jqGrid('getCell',rowId,'tph_STATUS');
   	  if(Status=='Failed'){
   		var tphTrdNo = jQuery("#trades").jqGrid('getCell',rowId,'tphtradeNo');
   		var tradeType = jQuery("#trades").jqGrid('getCell',rowId,'tradeType');
   		var destination=$('#selectedSystem').val();
   			(function(key){
   				$.ajax({
	   			type: "GET",
	   			url: "getErrorMessageTrade.do",
	   			dataType : "json",
	   	        contentType : "application/json",
	   			cache: false,
	   			data: { 
	   				tradeRefNo:tphTrdNo,
	   				tranType:tradeType,
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
 		/*var radio_btn_id = "trades" + "_radio_" + rowId;
		var radio_btn = '<input type="radio" id='+radio_btn_id+' enable onclick="clickRadioBtn(this)"/>';
    	$("#trades").jqGrid('setRowData', rowId, {radio_btn:radio_btn});*/
    	var check_box_id = "trades" + "_checkbox_" + rowId;
		var check_box = '<input type="checkbox" id='+check_box_id+' enable onclick="clickCheckBox(this)"/>';
		$("#trades").jqGrid('setRowData', rowId, {check_box:check_box});
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
	$("#trades").jqGrid('setGridParam', 
		{
			datatype:"json",
			page:1
		}).trigger("reloadGrid");
}, 300000);//auto refresh the grid every mentioned millisonds