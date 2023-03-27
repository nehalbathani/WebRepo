String.prototype.isValidDate = function() {
	  var IsoDateRe = new RegExp("^([0-9]{4})/([0-9]{2})/([0-9]{2})$");
	  var matches = IsoDateRe.exec(this);
	  if (!matches) return false;
	  

	  var composedDate = new Date(matches[1], (matches[2] - 1), matches[3]);

	  return ((composedDate.getMonth() == (matches[2] - 1)) &&
	          (composedDate.getDate() == matches[3]) &&
	          (composedDate.getFullYear() == matches[1]));

	};

String.prototype.trim=function(){return this.replace(/^\s+|\s+$/g, '');};

String.prototype.ltrim=function(){return this.replace(/^\s+/,'');};

String.prototype.rtrim=function(){return this.replace(/\s+$/,'');};

String.prototype.fulltrim=function(){return this.replace(/(?:(?:^|\n)\s+|\s+(?:$|\n))/g,'').replace(/\s+/g,' ');};

var gridHeight = $(window).height() * 60/100;
//console.log("grid-height:" + gridHeight);
var autoRefreshFlg = false;
var trade = 1, security = 2, trans_type;
var incoming = 1, outgoing = 2, process_type;
var resend_operation = 1, discard_operation = 2;
//var radio_btn_arr = new Array(0);
var check_box_arr = new Array(0); 
var resendDiscardFlg = false;
var alertInfo = "alert alert-info", alertSuc = "alert alert-success", alertWarn = "alert alert-danger";
var orig_grid_width = 0;

$(document).ready(function() {
	setupConsole();
});

function formDate(respDate) {
	//console.log("date value:" + respDate);
	var strDate = respDate.split(" ");
	var segDt = strDate[0].split("-");
	var segTime = strDate[1].split(":");
	
	var date = new Date();
	date.setFullYear(parseInt(segDt[0]), parseInt(segDt[1]) - 1, parseInt(segDt[2]));
	date.setHours(parseInt(segTime[0]));
	date.setMinutes(parseInt(segTime[1]));
	date.setSeconds(parseInt(segTime[2]));
	//console.log("new date value:" + date);
	return date;
}

function setupConsole() {
	if (!(window.console && console.log)) {
		console = {
			log : function() {
			},
			debug : function() {
			},
			info : function() {
			},
			warn : function() {
			},
			error : function() {
			}
		};
	}
}

function resizeColumns(grid) {
	var colModelArr = grid.jqGrid("getGridParam", "colModel");
	//console.log("colModelArr.length:" + colModelArr.length);
	var gridRowIds = grid.getDataIDs();
	for (var i = 0; i < colModelArr.length; i++) {
		var colModel = colModelArr[i];
		var colNm = colModel.name;
		//console.log("column-name:" + colNm + "/" + colModel.width);
		if (colModel.hidden == true || colNm == "mapping" || 
				colNm == "subgrid" || colNm == "check_box" || colNm == "ManuAckIcon") {
			continue;
		}	
		var new_width = 0, mult_by = 0;
		if (colNm == "cusip") {
			mult_by = 25;
		} else {
			mult_by = 21;
		}
		gridRowIds.forEach(function(val, index) {
			var gridRowId = val;
			var cellData = grid.jqGrid ('getCell', gridRowId, colNm);
			var cal_width = cellData.length * mult_by;
			if (cal_width > new_width) {
				new_width = cal_width;
			}
		});		
		var current = grid.jqGrid('getColProp', colNm);
		var curr_width = current['width'];
		//console.log("column-name-before:" + colNm + "/" + new_width + "/" + curr_width);
		if (new_width > (curr_width * 2)) {
			if (new_width < 180) {
				new_width = 180;
			}
			//console.log("column-name-after:" + colNm + "/" + new_width + "/" + curr_width);
			if (curr_width < new_width) {
				grid.jqGrid('setColProp', colNm, {widthOrg:new_width});
				var grid_width = grid.jqGrid('getGridParam', 'width') + 20;
		        grid.jqGrid('setGridWidth', grid_width);
			}
		}
	}
}

function setTransTradeTyp(transType, processType) {
	trans_type = transType;
	process_type = processType;
}

//TODO refactor the method
function attachToolbarBtns(toolbar) {
	var method_name = "";
	var btnArr = getButtonName();
	if (trans_type == trade) {
		method_name = "reProcessTrade";
	} else {
		method_name = "reProcessSecurity";
	}
	toolbar.append("<div class='button-container'> " +
            "<input id='resendBtn' type='button' value='" + btnArr[0] + "' " +
                            "class='button button-success' " +
                            "onclick='" + method_name + "(1)'/>" +
            "<input id='discardBtn' type='button' value='" + btnArr[1] + "' " +
                            "class='button button-danger' " + 
                            "onclick='" + method_name + "(2)'/>" +
            "</div>");
}

function attachCheckBox(grid, grid_id, statusArr) {
	var gridRowIds = grid.getDataIDs();
	gridRowIds.forEach(function(val, index) {
		var gridRowId = val;
		var statusFlg = false;
		var status = "";
		if (process_type == incoming) {
			status = grid.jqGrid ('getCell', gridRowId, 'status'); 
		} else {
			status = grid.jqGrid ('getCell', gridRowId, 'tph_STATUS'); 
		}
		for (var i = 0; i < statusArr.length; i++) {
			if (status == statusArr[i]) {
				statusFlg = true;
				break;
			}
		}
		if (statusFlg == true) {
			var check_box_id = grid_id + "_checkbox_" + gridRowId;
			var check_box = '<input type="checkbox" id='+check_box_id+' enable onclick="clickCheckBox(this)"/>';
			grid.jqGrid('setRowData', gridRowId, {check_box:check_box});
		}
	});
}

function clickCheckBox(obj) {
	var toolbarId = "t_trades";
    if (trans_type == security) {
    	toolbarId = "t_security";
    }
    toolbarDisplay($("#"+toolbarId), "inherit");
    var same_btn_clicked = false;
    check_box_arr.forEach(function(val, index) {
    	if (obj.id == $(val).attr('id')) {
    		check_box_arr.splice(index, 1);
    		same_btn_clicked = true;
    	}
    });
	if (same_btn_clicked != true) {
		check_box_arr.push(obj);
	} else if (check_box_arr.length == 0) {
		toolbarDisplay($("#"+toolbarId), "none");
	}
}

function getGridRowId(chk_box_id) {
	var sep_str = "_checkbox_", sep_str_index = chk_box_id.indexOf(sep_str);
	return chk_box_id.substr(sep_str_index + sep_str.length, chk_box_id.length);
}

function reProcessTrade(operation_type) {
	if (check_box_arr.length >= 1) {
		var grid_table = "trades";
		var trd_ref_no = new Array(), trd_se_no = new Array(), touch_cnt = new Array(), 
		    source_out = new Array(), dest_trade_type = new Array();
		var grid_row_id = 0, ind = 0;
		check_box_arr.forEach(function(val, index) {
			var chk_box_id = $(val).attr('id');
			grid_row_id = getGridRowId(chk_box_id);
			touch_cnt[ind] = $("#"+grid_table).jqGrid ('getCell', grid_row_id, 'touch_COUNT');
			if (process_type == incoming) {
				trd_se_no[ind] = $("#"+grid_table).jqGrid ('getCell', grid_row_id, 'tph_TRD_SEQ_NO');
				trd_ref_no[ind] = $("#"+grid_table).jqGrid ('getCell', grid_row_id, 'trd_REF_NO');
			} else {
				trd_ref_no[ind] = $("#"+grid_table).jqGrid ('getCell', grid_row_id, 'tphtradeNo');
				dest_trade_type[ind] = $("#"+grid_table).jqGrid ('getCell', grid_row_id, 'tradeType');
			}
			ind++;
		});
		if (process_type == outgoing) {
			source_out = $("#"+grid_table).jqGrid ('getCell', grid_row_id, 'destination');
			if (operation_type == resend_operation) {
				url = "resendTrade.do";
			} else {
				url = "discardTrade.do";
			}			
		} else {
			if (operation_type == resend_operation) {
				url = "reprocessTrade.do";
			} else {
				url = "cancelTrade.do";
			}			
		}
		//alert(url + "/" + trd_ref_no + "/" + touch_cnt + "/" + source_out + "/" + dest_trade_type);
		$.ajax({
   			type: "GET",
   			url: url,
   			dataType : "json",
   	        contentType : "application/json",
   	        data: {
   	        	trade_seq_no:trd_se_no,
   	        	trade_ref_no:trd_ref_no,
   	        	touch_count:touch_cnt,
   	        	source_out:source_out,
   	        	trade_type:dest_trade_type
   	        },
   			cache: false,
   			timeout:5000,
   			success: function(result, status, xhr) {
   				var resultStr = JSON.stringify(result);
  				var resultArr = $.parseJSON(resultStr);
  				var msg = "", sucMsg = "Trades Passed:", failMsg = "Trades Failed:";
  				var resultArrLen = 0, sucId = 0;
  				$.each(resultArr, function(index, val) {
  					var id = index.split(":");
  					resultArrLen++;
  					if (val >= 0) {
  						sucMsg += id[0] + ",";
                        sucId++;  							
  					} else {
  						failMsg += id[0] + ",";
  					}
                });
  				//alert("resultArrLen and successIds:" + resultArrLen + "/" + sucId);
  				if (sucId != 0) {
  					resendDiscardFlg = true;
  					$("#trades").setGridParam({datatype:'json', page:1}).trigger('reloadGrid');
   					toolbarDisplay($("#t_trades"), "none");
   					if (sucId == resultArrLen) {
   						msg = getOperationMsg(process_type, operation_type, true);
   					} else {
   						msg = sucMsg.substring(0, sucMsg.length - 1) + "  " + 
   						      failMsg.substring(0, sucMsg.length - 1);
   					}
   					alertHandling(alertSuc, msg);
  				} else {
  					msg = getOperationMsg(process_type, operation_type, false);
  					alertHandling(alertSuc, msg);
  				}
   			},
   			error: function(result, status, xhr) {
   				errorHandling(process_type, operation_type);
   			}   				
   		});
	} else if (radio_btn_arr.length == 0) {
		alertHandling(alertInfo, "Please select a trade!");
	} 
}

function reProcessSecurity(operation_type) {
	if (check_box_arr.length >= 1) {
		var grid_table = "security";
		var sec_ref_no = new Array(), src_out = "";
		var grid_row_id = 0, ind = 0;
		check_box_arr.forEach(function(val, index) {
			var chk_box_id = $(val).attr('id');
			grid_row_id = getGridRowId(chk_box_id);
			if (process_type == incoming) {
				sec_ref_no[ind++] = $("#"+grid_table).jqGrid ('getCell', grid_row_id, 'tph_SEC_SEQ_NO');
			} else {
				sec_ref_no[ind++] = $("#"+grid_table).jqGrid ('getCell', grid_row_id, 'sec_ID');
			}
		});
		var url = "";
		if (process_type == incoming) {
			if (operation_type == resend_operation) {
				url = "reprocessSecurity.do";
			} else {
				url = "cancelSecurity.do";
			}
		} else {
			src_out = $("#"+grid_table).jqGrid ('getCell', grid_row_id, 'source_OUT');
			if (operation_type == resend_operation) {
				url = "resendSecurity.do";
			} else {
				url = "discardSecurity.do";
			}
		}
		//alert("url and sec-ref-no and source:" + url + "/" + sec_ref_no + "/" + src_out);
		$.ajax({
   			type: "GET",
   			url: url,
   			dataType : "json",
   	        contentType : "application/json",
   	        data: {
   	        	sec_ref_no:sec_ref_no,
   	        	source_out:src_out
   	        },
   			cache: false,
   			timeout:5000,
   			success: function(result, status, xhr) {
   				var resultStr = JSON.stringify(result);
  				var resultArr = $.parseJSON(resultStr);
  				var msg = "", sucMsg = "Securites Passed:", failMsg = "Securites Failed:";
  				var resultArrLen = 0, sucId = 0;
  				$.each(resultArr, function(index, val) {
  					var id = index.split(":");
  					//alert("id:" + id[0]);
  					resultArrLen++;
  					if (val >= 0) {
  						sucMsg += id[0] + ",";
                        sucId++;  							
  					} else {
  						failMsg += id[0] + ",";
  					}
                });
  				//alert("resultArrLen and successIds:" + resultArrLen + "/" + sucId);
  				if (sucId != 0) {
  					resendDiscardFlg = true;
   					$("#security").setGridParam({datatype:'json', page:1}).trigger('reloadGrid');
   					toolbarDisplay($("#t_security"), "none");
   					if (sucId == resultArrLen) {
   						msg = getOperationMsg(process_type, operation_type, true);
   					} else {
   						msg = sucMsg.substring(0, sucMsg.length - 1) + "  " + 
   						      failMsg.substring(0, sucMsg.length - 1);
   					}
   					alertHandling(alertSuc, msg);
  				} else {
  					msg = getOperationMsg(process_type, operation_type, false);
  					alertHandling(alertSuc, msg);
  				}
   			},
   			error: function(result, status, xhr) {
   				errorHandling(process_type, operation_type);
   			}
   		});
	} else if (check_box_arr.length == 0) {
		alertHandling(alertInfo, "Please select a security!");
	} 
}

function errorHandling(process_type, operation_type) {
	var msg = getOperationMsg(process_type, operation_type, false);
	alertHandling(alertWarn, msg);
}

function getButtonName() {
	var btnNm = new Array();
	if (process_type == incoming) {
		btnNm.push("Reprocess");
		btnNm.push("Delete");
	} else {
		btnNm.push("Resend");
		btnNm.push("Discard");
	}
	return btnNm;
}

function getOperationMsg(process_type, operation_type, succFlg) {
	var msg = "";
	if (trans_type == trade) {
		if (process_type == incoming && operation_type == resend_operation && succFlg == true) {
			msg = "All the trade have been placed in queue for processing";
		} else if (process_type == incoming && operation_type == resend_operation && succFlg != true) {
			msg = "All the trade have been failed to go for processing";
		} else if (process_type == incoming && operation_type == discard_operation && succFlg == true) {
			msg = "All the trade have been removed from processing";
		} else if (process_type == incoming && operation_type == discard_operation && succFlg == false) {
			msg = "All the trade have been failed to remove  from processing";
		} else if (process_type == outgoing && operation_type == resend_operation && succFlg == true) {
			msg = "All the trade have been places in queue for resend";
		} else if (process_type == outgoing && operation_type == resend_operation && succFlg != true) {
			msg = "All the trade have been failed to resend";
		} else if (process_type == outgoing && operation_type == discard_operation && succFlg == true) {
			msg = "All the trade have been discarded successfully";
		} else if (process_type == outgoing && operation_type == discard_operation && succFlg == false) {
			msg = "All the trade have been failed to discard";
		} 
	} else {
		if (process_type == incoming && operation_type == resend_operation && succFlg == true) {
			msg = "All the securities have been placed in queue for processing";
		} else if (process_type == incoming && operation_type == resend_operation && succFlg != true) {
			msg = "All the securities have been failed to go for processing";
		} else if (process_type == incoming && operation_type == discard_operation && succFlg == true) {
			msg = "All the securities have been removed from processing";
		} else if (process_type == incoming && operation_type == discard_operation && succFlg == false) {
			msg = "All the securities have been failed to remove from processing";
		} else if (process_type == outgoing && operation_type == resend_operation && succFlg == true) {
			msg = "All the securities have been placed in queue to resend";
		} else if (process_type == outgoing && operation_type == resend_operation && succFlg != true) {
			msg = "All the securities have been failed to go for resend";
		} else if (process_type == outgoing && operation_type == discard_operation && succFlg == true) {
			msg = "All the securities have been discarded successfully";
		} else if (process_type == outgoing && operation_type == discard_operation && succFlg == false) {
			msg = "All the securities have been failed to discard";
		} 
	}
	return msg;
}

function alertHandling(alertClass, msg) {
	var msgAlert = $("#alert");
	var appliedClass = msgAlert.attr('class');
	msgAlert.text(msg);
	if (appliedClass != null) {
		msgAlert.removeClass(appliedClass);
		msgAlert.addClass(alertClass).fadeIn().fadeOut(10000);
	} else {
		msgAlert.addClass(alertClass).fadeOut(10000);
	}
}

function toolbarDisplay(toolbarId, display) {
	toolbarId.css("display", display);
}

function clearResendDiscardOpr(toolbarId) {
	var noOfChks = check_box_arr.length;
	if (noOfChks != 0) {
		check_box_arr.splice(0, noOfChks);
		toolbarDisplay($("#" + toolbarId), "none");
	}
}

/*function hideToolbar(status, toolbarId) {
	if (check_box_arr.length == 0) {
		if (status == "visible") {
			toolbarDisplay(toolbarId, "none");
			$(".button-container").show();
		} else {
			$(".button-container").hide();
		}
	}
}*/
	
function amountFormatter(cellvalue, options) {
    var  retult,
    op = $.extend({}, $.jgrid.formatter.number); // or $.jgrid.formatter.integer
   if(cellvalue!=null && cellvalue!=''){
	   var value = parseFloat(cellvalue);
		if(!$.fmatter.isUndefined(options.colModel.formatoptions)) {
		    op = $.extend({}, op,options.colModel.formatoptions);
		}
		retult = $.fmatter.util.NumberFormat(Math.abs(value), op);
		return (value >= 0 ? retult : '(' + retult + ')');
	}
	else
	{
		return '';
	}
}

function getQueryVariable(variable) {
	var query = window.location.search.substring(1);
	var vars = query.split("&");
	for (var i = 0; i < vars.length; i++) {
		var pair = vars[i].split("=");
		if (pair[0] == variable) {
			return pair[1];
		}
	}
	return (false);
}

function loadGridForDashBoardFilter(statusFilterVal, sourceFilterVal) {
	if(statusFilterVal == "STP")
		statusFilterVal = "PROCESSED";
	$('.status').each(function() {
		if ($(this).val().toUpperCase() == statusFilterVal) {
			$(this).prop('checked', true);
		} else {
			$(this).prop('checked', false);
		}
	});
	$('.source').each(function() {
		if ($(this).val() == sourceFilterVal) {
			$(this).prop('checked', true);
		} else {
			$(this).prop('checked', false);
		}
	});
}

/**
 * For Data comparison logic
**/
//Attach the compare button link to each row
/**
 * grid = the grid object
 * jsonurl = the url to be called through json
 * grid_id = the grid/subgrid id which contains the plcat the mapping button to put
 * mappingFor = as it is the common function for both trade and security, 
 * 		to decide for which one the mapping will show we need to pass trade/security
 */
function attachLinkForDataComparision(grid, jsonurl, grid_id, mappingFor) {
	var gridRowIds = grid.getDataIDs();
	gridRowIds.forEach(function(val, index) {
		var gridRowId = val;
		var btn = "";
		if(mappingFor == 'trade'){
			var tphSeqNo = grid.jqGrid ('getCell', gridRowId, 'tph_TRD_SEQ_NO'); 
			var tphSource = grid.jqGrid ('getCell', gridRowId, 'source');
			var tphTouchCount = grid.jqGrid ('getCell', gridRowId, 'touch_COUNT');
			
			var btn_id = grid_id + "_mapping_" + tphSeqNo;
			var url = jsonurl + "?" + "refNo=" + tphSeqNo + "&source=" + tphSource + "&touch_count=" + tphTouchCount; 
			btn = '<input type="button" id="'+btn_id+'" class="mapping-btn-icon" onclick="clickMappingBtn('+gridRowId+', \''+url+'\', \''+grid_id+'\', \''+mappingFor+'\')" title="Compare Views"/>';
		}else if(mappingFor == 'tradeOutgoing'){
			var tphSeqNo = grid.jqGrid ('getCell', gridRowId, 'tphtradeNo'); 
			var tphSource = grid.jqGrid ('getCell', gridRowId, 'source');
			var tphTouchCount = grid.jqGrid ('getCell', gridRowId, 'touch_COUNT');
			
			var btn_id = grid_id + "_mapping_" + tphSeqNo;
			var url = jsonurl + "?" + "refNo=" + tphSeqNo + "&source=" + tphSource + "&touch_count=" + tphTouchCount; 
			btn = '<input type="button" id="'+btn_id+'" class="mapping-btn-icon" onclick="clickMappingBtn('+gridRowId+', \''+url+'\', \''+grid_id+'\', \'trade\')" title="Compare Views"/>';
		}else if(mappingFor == 'security'){
			var tphSeqNo = grid.jqGrid ('getCell', gridRowId, 'tph_SEC_SEQ_NO'); 
			var tphSource = grid.jqGrid ('getCell', gridRowId, 'source'); 
			
			var btn_id = grid_id + "_mapping_" + tphSeqNo;
			var url = jsonurl + "?" + "refNo=" + tphSeqNo + "&source=" + tphSource;
			btn = '<input type="button" id="'+btn_id+'" class="mapping-btn-icon" onclick="clickMappingBtn('+gridRowId+', \''+url+'\', \''+grid_id+'\', \''+mappingFor+'\')" title="Compare Views"/>';			
		}else if(mappingFor == 'securityOutgoing'){
			var tphSeqNo = grid.jqGrid ('getCell', gridRowId, 'sec_ID'); 
			var tphSource = grid.jqGrid ('getCell', gridRowId, 'source'); 
			
			var btn_id = grid_id + "_mapping_" + tphSeqNo;
			var url = jsonurl + "?" + "refNo=" + tphSeqNo + "&source=" + tphSource;
			btn = '<input type="button" id="'+btn_id+'" class="mapping-btn-icon" onclick="clickMappingBtn('+gridRowId+', \''+url+'\', \''+grid_id+'\', \'security\')" title="Compare Views"/>';			
		}
		grid.jqGrid('setRowData', gridRowId, {mapping:btn});
	});
}

//on click of the mapping button, modal will load the data which returned from the service
function clickMappingBtn(gridRowId, jsonurl, gridId, mappingFor){//mappingFor = trade/security
    var mappingForId = "#" + mappingFor + "Modal";
    $(mappingForId).modal('show');//'#tradeModal'
    var uri = jsonurl+'&r='+Math.random();
    $.ajax({
        type : 'GET',
        async : true,
        dataType : "json",
        url: uri,
       success: function( response ){
        	//console.log(response);
        	var html = "";
        	var descHtml = "";
        	var radioHtml = "";
        	var outgoingHtml = "";
        	var radioSecId = "#" + mappingFor + "ModalRadioSection";
    		var errorSecId = "#" + mappingFor + "ModalErrorSection";
    		var modalTableId = "#" + mappingFor + "ModalTable";
    		
        	if(response[0].errorMessage != undefined && response[0].errorMessage != ""){
        		$(radioSecId).html("");
    	    	$(errorSecId).html(response[0].errorMessage);   
    	    	$(modalTableId).hide();
	    	}else{
	    		$(errorSecId).html("");   
    	    	$(modalTableId).show();
	    		for(var reslen = 0; reslen<response.length; reslen++ ) {
	    			radioHtml = "Compare &nbsp;Incoming &nbsp;&nbsp; with &nbsp;&nbsp;";
	    			
	    			descHtml += "<tr>";
	        			descHtml += "<th>TAG</th>";
	        		descHtml += "</tr>";
	    			
	    			html += "<tr>";
		    		html += "<th colspan=3 textalign=center>Incoming</th>";
		    		dest = response[reslen].destinationBean;
	    			for(key in dest) {
	    				radioHtml +="<input type='radio' name='outgoing' class='outgoingRadio' value='"+key+"' title='Only one can be selected.'>&nbsp;"+key+"</input>&nbsp;&nbsp;";	    				
	    			}
		    		html += "</tr>";
	    			break;
	    		}
	    		$(radioSecId).html(radioHtml);
	    		
	    		//create nav tabs
	    		var tagUniqueGroups = new Array();
	    		var navTabhtml = "";
	    		var navTabBodyHtml = "";
	    		for(var reslen = 0; reslen<response.length; reslen++ ) {
	    			dest = response[reslen].destinationBean;
	    			//Creating the tabs
	    			for(key in dest) { 
	    				if(key == $('.outgoingRadio:first').val()){
			    			if( $.inArray( response[reslen].groupName, tagUniqueGroups ) <= -1 ){
			    				var groupNameVal = response[reslen].groupName;
			    				if(groupNameVal == null || groupNameVal == "" || groupNameVal.toLowerCase() == "null")
			    					groupNameVal = "Others";
			    				
			    				tagUniqueGroups.push(groupNameVal);
			    				//alert(key + " " + $('.outgoingRadio:first').val() + " groupnm = " + groupNameVal);
				    			if(reslen == 0){
				    				navTabhtml += "<li class='active'><a href='#navTabBody"+groupNameVal+"' data-toggle='tab'>"+groupNameVal+"</a></li>";		  				
				    				//tab body for initial active tab
				    				navTabBodyHtml += "<div class='tab-pane active' id='navTabBody"+groupNameVal+"'>" +
							    				"<table style='width: 100%;'>" +
								    			    "<tr style='width: 100%;'>"+
									    			    "<td colspan='1'>"+
													      "<table class='table table-striped table-bordered table-condensed table-hover table-custom' > "+
															  "<tbody id='modalTableDescBody"+groupNameVal+"'>"+
															  "</tbody>"+
													      "</table>"+
													    "</td>"+
													    
													    "<td colspan='1'>"+
													      "<table class='table table-striped table-bordered table-condensed table-hover table-custom' style='background-color: #EBCCD1;'> "+
															  "<tbody id='modalTableIncominBody"+groupNameVal+"'>"+
															  "</tbody>"+
													      "</table>"+
													    "</td>"+
													    
													    "<td colspan='1'>"+
													      "<table class='table table-striped table-bordered table-condensed table-hover table-custom' style='background-color: #D6E9C6;'> "+
															  "<tbody id='modalTableOutgoingBody"+groupNameVal+"'>"+
															  "</tbody>"+
													      "</table>"+
													    "</td>"+
								    			    "</tr>"+
							    			    "</table>"+
				    						"</div>";		    	            		    		       
				    			}
				    			else{
				    				navTabhtml += "<li><a href='#navTabBody"+groupNameVal+"' data-toggle='tab'>"+groupNameVal+"</a></li>";
				    				//tab body for intial inactive tab(s)
				    				navTabBodyHtml += "<div class='tab-pane' id='navTabBody"+groupNameVal+"'>" +
									    				"<table style='width: 100%;'>" +
										    			    "<tr style='width: 100%;'>"+
											    			    "<td colspan='1'>"+
															      "<table class='table table-striped table-bordered table-condensed table-hover table-custom' > "+
																	  "<tbody id='modalTableDescBody"+groupNameVal+"'>"+
																	  "</tbody>"+
															      "</table>"+
															    "</td>"+
															    
															    "<td colspan='1'>"+
															      "<table class='table table-striped table-bordered table-condensed table-hover table-custom' style='background-color: #EBCCD1;'> "+
																	  "<tbody id='modalTableIncominBody"+groupNameVal+"'>"+
																	  "</tbody>"+
															      "</table>"+
															    "</td>"+
															    
															    "<td colspan='1'>"+
															      "<table class='table table-striped table-bordered table-condensed table-hover table-custom' style='background-color: #D6E9C6;'> "+
																	  "<tbody id='modalTableOutgoingBody"+groupNameVal+"'>"+
																	  "</tbody>"+
															      "</table>"+
															    "</td>"+
										    			    "</tr>"+
									    			    "</table>"+
													"</div>";		    
				    			}
			    			}
	    				}
	    			}
	    		}
	    		$('#securityNavTab').html(navTabhtml);//create tabs
	    		$('#compare-tab-content').append(navTabBodyHtml); // create tab body 
	    		
	    		//Column header loading only
	    		for(var reslen = 0; reslen<response.length; reslen++ ) {
	    			var groupNameVal = response[reslen].groupName;
    				if(groupNameVal == null || groupNameVal == "" || groupNameVal.toLowerCase() == "null")
    					groupNameVal = "Others";
    				
	    			var desbodyId = "#modalTableDescBody"+groupNameVal;
	    			var incomingId = "#modalTableIncominBody"+groupNameVal;
	    			var outgoingId = "#modalTableOutgoingBody"+groupNameVal;
	    			
	    			descHtml = "";
	    			descHtml += "<tr>";
	        			descHtml += "<th class='"+groupNameVal+"'>TAG</th>";
	        		descHtml += "</tr>";

	        		if(! $(desbodyId).hasClass("'" + groupNameVal + "'"))
	        			$(desbodyId).html(descHtml);
	        		
	        		
	        		var inhtml = "<tr>";
	        		inhtml += "<th colspan=3 textalign=center class='" + groupNameVal + "'>Incoming</th>";
	        		inhtml += "</tr>";
	        		
	        		if(! $(incomingId).hasClass("'" + groupNameVal + "'"))
	        			$(incomingId).html(inhtml);
	        		
	        		dest = response[reslen].destinationBean;
	    			for(key in dest) { 
	    				if(key == $('.outgoingRadio:first').val()){
		    				var outgoingHtm = "<tr>";
			        		outgoingHtm += "<th colspan=2 class='" + groupNameVal + "'>"+key+"</th>";
			        		outgoingHtm += "</tr>";
			        		
			        		if(! $(outgoingId).hasClass("'" + groupNameVal + "'"))
			        			$(outgoingId).html(outgoingHtm);
		    				
		    				break;
	    				}
	    			}
	    		}
	    		
	    		//Data compare values loading under corresponding tab(s)
	    		for(var reslen = 0; reslen<response.length; reslen++ ) {
	    			var groupNameVal = response[reslen].groupName;
    				if(groupNameVal == null || groupNameVal == "" || groupNameVal.toLowerCase() == "null")
    					groupNameVal = "Others";
    				
	    			//Tag description
	    			var desbodyId = "#modalTableDescBody"+groupNameVal;
	    			var incomingId = "#modalTableIncominBody"+groupNameVal;
	    			var outgoingId = "#modalTableOutgoingBody"+groupNameVal;
	    			
	    			descHtml = "";
	    			descHtml += "<tr>";
		    			descHtml += "<td>";
		    				descHtml += response[reslen].businessName;
		    			descHtml += "</td>";
	    			descHtml += "</tr>";
	    			$(desbodyId).append(descHtml);
	    			
	    			//Incoming
	    			var incominghtml = "";
	    			incominghtml += "<tr>";
		    			incominghtml += "<td>";
		    				incominghtml += response[reslen].sourceTag;
		    			incominghtml += "</td>";
		    			incominghtml += "<td>";
		    				incominghtml += response[reslen].sourceTagValue;
		    			incominghtml += "</td>";
	    			incominghtml += "</tr>";
	    			$(incomingId).append(incominghtml);
	    			
	    			//outgoing
	    			dest = response[reslen].destinationBean;
	    			for(key in dest) { 
	    				if(key == $('.outgoingRadio:first').val()){
	    					var outgoingTagHtml = "";
	    					if(reslen == 0){
	    						outgoingTagHtml += "<tr>";
	    						outgoingTagHtml += "<th colspan=2>"+key+"</th>";
	    						outgoingTagHtml += "</tr>";
	    	    				//$(outgoingId).html(outgoingTagHtml);
	    	    			}
	    					
		    				outgingVal = dest[key];
		    				outgoingTagHtml= "";
		    				outgoingTagHtml += "<tr>";
		    				outgoingTagHtml += "<td>";
		    					outgoingTagHtml += outgingVal.destTag;
		    				outgoingTagHtml += "</td>";
			    			
		    				outgoingTagHtml += "<td>";
		    					outgoingTagHtml += outgingVal.destTagValue;
		    				outgoingTagHtml += "</td>";
		    				outgoingTagHtml += "</tr>";
		    				$(outgoingId).append(outgoingTagHtml);
	    				}
	    			}
	    		}
	    		
	    		
	    		
	    		
	    		
	    		
	    		/* May not required - need to test
	    		 * for(var reslen = 0; reslen<response.length; reslen++ ) {
	    			descHtml += "<tr>";
		    			descHtml += "<td>";
		    				descHtml += response[reslen].businessName;
		    			descHtml += "</td>";
	    			descHtml += "</tr>";
	    			
		    		html += "<tr>";
		    			html += "<td>";
		    				html += response[reslen].sourceTag;
		    			html += "</td>";
		    			html += "<td>";
		    				html += response[reslen].sourceTagValue;
		    			html += "</td>";
		    	  	html += "</tr>";
	    		}*/
	    		
	    		/*May not required - need to test
	    		 * for(var reslen = 0; reslen<response.length; reslen++ ) {
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
	    		}*/
	    	  	
	    		/*var modalTableDescBodyId = "#" + mappingFor + "ModalTableDescBody";
	    		var modalTableBodyId = "#" + mappingFor + "ModalTableBody";
	    		var outgoingModalTableBodyId = "#" + mappingFor + "OutgoingModalTableBody";
	    		
	    		$(modalTableDescBodyId).html(descHtml);
	    	  	$(modalTableBodyId).html(html);
	    	  	$(outgoingModalTableBodyId).html(outgoingHtml);*/
	    		
	    	  	$('.outgoingRadio:first').attr('checked', true);
	    	  	var outgoingHtmlold = "";
	    	  	//Onclick of radio button
	    	  	$('.outgoingRadio').click(function(){	
	    	  		
	    	  		$('#securityNavTab').html("");//clear tabs
		    		$('#compare-tab-content').html(""); // clear tab body 
		    		navTabhtml = "";
		    		navTabBodyHtml = "";
		    		tagUniqueGroups.length = 0;
	    	  		for(var reslen = 0; reslen<response.length; reslen++ ) {
	    	  			dest = response[reslen].destinationBean;
	    	  			
	    	  			var groupNameVal = response[reslen].groupName;
	    				if(groupNameVal == null || groupNameVal == "" || groupNameVal.toLowerCase() == "null")
	    					groupNameVal = "Others";
	    				
		    			for(key in dest) { 
		    				if(key == $(this).val()){
				    			if( $.inArray( groupNameVal, tagUniqueGroups ) <= -1 ){
				    				tagUniqueGroups.push(groupNameVal);
				    				
					    			if(reslen == 0){
					    				navTabhtml += "<li class='active'><a href='#navTabBody"+groupNameVal+"' data-toggle='tab'>"+groupNameVal+"</a></li>";		  				
					    				//tab body for initial active tab
					    				navTabBodyHtml += "<div class='tab-pane active' id='navTabBody"+groupNameVal+"'>" +
								    				"<table style='width: 100%;'>" +
									    			    "<tr style='width: 100%;'>"+
										    			    "<td colspan='1'>"+
														      "<table class='table table-striped table-bordered table-condensed table-hover table-custom' > "+
																  "<tbody id='modalTableDescBody"+groupNameVal+"'>"+
																  "</tbody>"+
														      "</table>"+
														    "</td>"+
														    
														    "<td colspan='1'>"+
														      "<table class='table table-striped table-bordered table-condensed table-hover table-custom' style='background-color: #EBCCD1;'> "+
																  "<tbody id='modalTableIncominBody"+groupNameVal+"'>"+
																  "</tbody>"+
														      "</table>"+
														    "</td>"+
														    
														    "<td colspan='1'>"+
														      "<table class='table table-striped table-bordered table-condensed table-hover table-custom' style='background-color: #D6E9C6;'> "+
																  "<tbody id='modalTableOutgoingBody"+groupNameVal+"'>"+
																  "</tbody>"+
														      "</table>"+
														    "</td>"+
									    			    "</tr>"+
								    			    "</table>"+
					    						"</div>";		    	            		    		       
					    			}
					    			else{
					    				navTabhtml += "<li><a href='#navTabBody"+groupNameVal+"' data-toggle='tab'>"+groupNameVal+"</a></li>";
					    				//tab body for intial inactive tab(s)
					    				navTabBodyHtml += "<div class='tab-pane' id='navTabBody"+groupNameVal+"'>" +
										    				"<table style='width: 100%;'>" +
											    			    "<tr style='width: 100%;'>"+
												    			    "<td colspan='1'>"+
																      "<table class='table table-striped table-bordered table-condensed table-hover table-custom' > "+
																		  "<tbody id='modalTableDescBody"+groupNameVal+"'>"+
																		  "</tbody>"+
																      "</table>"+
																    "</td>"+
																    
																    "<td colspan='1'>"+
																      "<table class='table table-striped table-bordered table-condensed table-hover table-custom' style='background-color: #EBCCD1;'> "+
																		  "<tbody id='modalTableIncominBody"+groupNameVal+"'>"+
																		  "</tbody>"+
																      "</table>"+
																    "</td>"+
																    
																    "<td colspan='1'>"+
																      "<table class='table table-striped table-bordered table-condensed table-hover table-custom' style='background-color: #D6E9C6;'> "+
																		  "<tbody id='modalTableOutgoingBody"+groupNameVal+"'>"+
																		  "</tbody>"+
																      "</table>"+
																    "</td>"+
											    			    "</tr>"+
										    			    "</table>"+
														"</div>";		    
					    			}
				    			}
		    				}
		    			}
		    		}
		    		$('#securityNavTab').html(navTabhtml);//create tabs
		    		$('#compare-tab-content').append(navTabBodyHtml); // create tab body 
		    		
		    		
	    	  		
		    		//Column header loading only
		    		for(var reslen = 0; reslen<response.length; reslen++ ) {
		    			var groupNameVal = response[reslen].groupName;
	    				if(groupNameVal == null || groupNameVal == "" || groupNameVal.toLowerCase() == "null")
	    					groupNameVal = "Others";
	    				
		    			var desbodyId = "#modalTableDescBody"+groupNameVal;
		    			var incomingId = "#modalTableIncominBody"+groupNameVal;
		    			var outgoingId = "#modalTableOutgoingBody"+groupNameVal;
		    			
		    			descHtml = "";
		    			descHtml += "<tr>";
		        			descHtml += "<th class='"+groupNameVal+"'>TAG</th>";
		        		descHtml += "</tr>";

		        		if(! $(desbodyId).hasClass("'" + groupNameVal + "'"))
		        			$(desbodyId).html(descHtml);
		        		
		        		
		        		var inhtml = "<tr>";
		        		inhtml += "<th colspan=3 textalign=center class='" + groupNameVal + "'>Incoming</th>";
		        		inhtml += "</tr>";
		        		
		        		if(! $(incomingId).hasClass("'" + groupNameVal + "'"))
		        			$(incomingId).html(inhtml);
		        		
		        		dest = response[reslen].destinationBean;
		    			for(key in dest) { 
		    				if(key == $('.outgoingRadio:first').val()){
			    				var outgoingHtm = "<tr>";
				        		outgoingHtm += "<th colspan=2 class='" + groupNameVal + "'>"+key+"</th>";
				        		outgoingHtm += "</tr>";
				        		
				        		if(! $(outgoingId).hasClass("'" + groupNameVal + "'"))
				        			$(outgoingId).html(outgoingHtm);
			    				
			    				break;
		    				}
		    			}
		    		}
	    	  		
	    	  		outgoingHtml="";		    	  		
	    	  		for(var reslen = 0; reslen<response.length; reslen++ ) {
	    	  			var groupNameVal = response[reslen].groupName;
	    				if(groupNameVal == null || groupNameVal == "" || groupNameVal.toLowerCase() == "null")
	    					groupNameVal = "Others";
	    				
	    	  			var outgoingId = "#modalTableOutgoingBody"+groupNameVal;
	    	  			$(outgoingId).html("");
	    	  		}
	    	  		
	    	  		for(var reslen = 0; reslen<response.length; reslen++ ) {
	    	  			var groupNameVal = response[reslen].groupName;
	    				if(groupNameVal == null || groupNameVal == "" || groupNameVal.toLowerCase() == "null")
	    					groupNameVal = "Others";
	    				
	    	  			//Tag description
		    			var desbodyId = "#modalTableDescBody"+groupNameVal;
		    			var incomingId = "#modalTableIncominBody"+groupNameVal;
		    			
		    			descHtml = "";
		    			descHtml += "<tr>";
			    			descHtml += "<td>";
			    				descHtml += response[reslen].businessName;
			    			descHtml += "</td>";
		    			descHtml += "</tr>";
		    			$(desbodyId).append(descHtml);
		    			
		    			//Incoming
		    			var incominghtml = "";
		    			incominghtml += "<tr>";
			    			incominghtml += "<td>";
			    				incominghtml += response[reslen].sourceTag;
			    			incominghtml += "</td>";
			    			incominghtml += "<td>";
			    				incominghtml += response[reslen].sourceTagValue;
			    			incominghtml += "</td>";
		    			incominghtml += "</tr>";
		    			$(incomingId).append(incominghtml);
		    			
	    	  			dest = response[reslen].destinationBean;
		    			var outgoingId = "#modalTableOutgoingBody"+groupNameVal;
		    			for(key in dest) { 
		    				if(key == $(this).val()){
			    	  			var outgoingHtmlHeader = "<tr>";
			    	  			outgoingHtmlHeader += "<th colspan=2 class='"+groupNameVal+"'>"+key+"</th>";
			    	  			outgoingHtmlHeader += "</tr>";
			    				
			    				if(! $(outgoingId).hasClass("'" + groupNameVal + "'")){
			    					$(outgoingId).html(outgoingHtmlHeader);
			    				}
			    			}
		    			}	
	    	  		}
	    	  		
		    		for(var reslen = 0; reslen<response.length; reslen++ ) {
		    			dest = response[reslen].destinationBean;
		    			
		    			var groupNameVal = response[reslen].groupName;
	    				if(groupNameVal == null || groupNameVal == "" || groupNameVal.toLowerCase() == "null")
	    					groupNameVal = "Others";
	    				
		    			var outgoingId = "#modalTableOutgoingBody"+groupNameVal;
		    			for(key in dest) { 
		    				if(key == $(this).val()){
		    					/*outgoingHtml = "";
		    					if(reslen == 0)
		    					{
		    						
		    	    				outgoingHtml += "<tr>";
		    	    				outgoingHtml += "<th colspan=2 class='"+response[reslen].groupName+"'>"+key+"</th>";
		    	    				outgoingHtml += "</tr>";
		    	    				
		    	    				if(! $(outgoingId).hasClass("'" + response[reslen].groupName + "'")){
		    	    					$(outgoingId).html(outgoingHtml);
		    	    				}
		    	    			}*/
		    					
			    				outgingVal = dest[key];
			    				outgoingHtml= "";
			    				outgoingHtml += "<tr>";
			    				outgoingHtml += "<td>";
			    					outgoingHtml += outgingVal.destTag;
			    				outgoingHtml += "</td>";
				    			
			    				outgoingHtml += "<td>";
			    					outgoingHtml += outgingVal.destTagValue;
			    				outgoingHtml += "</td>";
			    				outgoingHtml += "</tr>";
			    				$(outgoingId).append(outgoingHtml);
		    				}
		    				
		    				//old code
		    				/*if(key == $(this).val()){
		    					if(reslen == 0){
		    	    				outgoingHtmlold += "<tr>";
		    	    				outgoingHtmlold += "<th colspan=2>"+key+"</th>";
		    	    				outgoingHtmlold += "</tr>";
		    	    			}
		    					
			    				outgingVal = dest[key];
			    				outgoingHtmlold += "<tr>";
			    				outgoingHtmlold += "<td>";
			    					outgoingHtmlold += outgingVal.destTag;
			    				outgoingHtmlold += "</td>";
				    			
			    				outgoingHtmlold += "<td>";
			    					outgoingHtmlold += outgingVal.destTagValue;
			    				outgoingHtmlold += "</td>";
			    				outgoingHtmlold += "</tr>";
		    				}*/
		    			}
		    		}
		    	  	
		    	  	//$(outgoingModalTableBodyId).html(outgoingHtmlold);
	    	  	});
	    	}
        },
        error: function(jqXHR,error, errorThrown) {  
        	//TODO- what happens if server is down
        	//alert(error + " " + jqXHR.status);
        }
	});
}