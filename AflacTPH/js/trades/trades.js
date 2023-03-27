/*var incoming_trade = 1, outgoing_trade = 2;
var resend_operation = 1, discard_operation = 2;
var radio_btn_arr = new Array(0);

function attachToolbarBtns(toolbar, trade_type) {
	toolbar.append("<div class='btn_container'> " +
            "<input id='resendBtn' type='button' value='Resend' " +
                            "class='btn btn-success' " +
                            "onclick='reProcessTrade(\"" + trade_type + "\", 1)'/> " +
            "<input id='discardBtn' type='button' value='Discard' " +
                            "class='btn btn-danger' " + 
                            "onclick='reProcessTrade(\"" + trade_type + "\", 2)'/>" +
            "</div>");
}

function reProcessTrade(trade_type, operation_type) {
	if (radio_btn_arr.length == 1) {
		var radio_btn = radio_btn_arr.pop(), radio_btn_id = radio_btn.id;
		var sep_str = "_radio_", sep_str_index = radio_btn_id.indexOf(sep_str);
		var grid_table = radio_btn_id.substr(0, sep_str_index);
		var url = "";
		if (trade_type == incoming_trade) {
			grid_table = grid_table + "_subgrid";
		} 
		var grid_row_id = radio_btn_id.substr(sep_str_index + sep_str.length, radio_btn_id.length);
		var trd_ref_no;
		var touch_cnt = $("#"+grid_table).jqGrid ('getCell', grid_row_id, 'touch_COUNT');
		var dest_trade_type = "", source_out = "";
		if (trade_type == outgoing_trade) {
			if (operation_type == resend_operation) {
				url = "resendTrade.do";
			} else {
				url = "discardTrade.do";
			}
			source_out = $("#"+grid_table).jqGrid ('getCell', grid_row_id, 'destination');
			trd_ref_no = $("#"+grid_table).jqGrid ('getCell', grid_row_id, 'tphtradeNo');
			dest_trade_type = $("#"+grid_table).jqGrid ('getCell', grid_row_id, 'tradeType');
		} else {
			if (operation_type == resend_operation) {
				url = "reprocessTrade.do";
			} else {
				url = "cancelTrade.do";
			}
			trd_ref_no = $("#"+grid_table).jqGrid ('getCell', grid_row_id, 'trd_REF_NO');
		}
		alert(trd_ref_no + "/" + touch_cnt + "/" + url);
		$.ajax({
   			type: "GET",
   			url: url,
   			dataType : "json",
   	        contentType : "application/json",
   	        data: {
   	        	trade_ref_no:trd_ref_no,
   	        	touch_count:touch_cnt,
   	        	source_out:source_out,
   	        	trade_type:dest_trade_type
   	        },
   			cache: false,
   			timeout:5000,
   			success: function(result, status, xhr) {
   				alert("result at success:" + result);
   				if ((operation_type == resend_operation && result == 0) ||
   					(operation_type == discard_operation && 
   					trade_type == incoming_trade && result > 0) ||
   					(operation_type == discard_operation && 
   		   			trade_type == outgoing_trade && result >= 0)) {
   					$("#trades").setGridParam({datatype:'json', page:1}).trigger('reloadGrid');
   					toolbarDisplay($("#t_trades"), "none");
   					var msg = sendMsg(trade_type, operation_type, true);
   					alertHandling($("#alert_success"), msg);
   					no_failed_row_clicked = 0;
   				} else {
   					radio_btn_arr.push(radio_btn);
   					errorHandling(trade_type, operation_type);
   				}
   			},
   			error: function(result, status, xhr) {
   				errorHandling(trade_type, operation_type);
   				radio_btn_arr.push(radio_btn);
   			}
   		});
	} else if (radio_btn_arr.length == 0) {
		alertHandling($("#alert_success"), "Please select a trade!");
	} 
}

function errorHandling(trade_type, operation_type) {
	var msg = sendMsg(trade_type, operation_type, false);
	alertHandling($("#alert_danger"), msg);
}

function sendMsg(trade_type, operation_type, succFlg) {
	var msg = "";
	if (trade_type == incoming_trade && operation_type == resend_operation && succFlg == true) {
		msg = "Trade has been placed in queue for processing";
	} else if (trade_type == incoming_trade && operation_type == resend_operation && succFlg != true) {
		msg = "Trade has been FAILED to go for processing";
	} else if (trade_type == incoming_trade && operation_type == discard_operation && succFlg == true) {
		msg = "Trade has been removed from processing";
	} else if (trade_type == incoming_trade && operation_type == discard_operation && succFlg == false) {
		msg = "FAILED to Remove Trade from processing";
	} else if (trade_type == outgoing_trade && operation_type == resend_operation && succFlg == true) {
		msg = "Trade has been placed in queue to resend";
	} else if (trade_type == outgoing_trade && operation_type == resend_operation && succFlg != true) {
		msg = "Trade has been FAILED to resend";
	} else if (trade_type == outgoing_trade && operation_type == discard_operation && succFlg == true) {
		msg = "Trade has been discarded successfully";
	} else if (trade_type == outgoing_trade && operation_type == discard_operation && succFlg == false) {
		msg = "Trade has been FAILED to discard";
	} 
	return msg;
}

function alertHandling(alert, msg) {
	alert.text(msg);
	alert.css("display", "inherit").fadeOut(3000);
}

function toolbarDisplay(toolbarId, display) {
	toolbarId.css("display", display);
}

function clickRadioBtn(obj, trade_type) {
	if (trade_type == outgoing_trade) {
		toolbarDisplay($("#t_trades"), "inherit");
	}
	var len = radio_btn_arr.length;
	var same_btn_clicked = false;
	if (len != 0) {
		radio_btn_arr.forEach (function(val, index) {
			$(val).attr('checked', false); 
			remove_btn_arr = radio_btn_arr.splice(index, 1);
			if (obj.id == remove_btn_arr.pop().id) {
				same_btn_clicked = true;
			}
	    });
	}
	if (same_btn_clicked != true) {
		radio_btn_arr.push(obj);
	} else if (trade_type == outgoing_trade) {
		toolbarDisplay($("#t_trades"), "none");
	}
}
*/

function applyLabelForTradeTicket(subgrid, id) {	
	jQuery('#'+id+'_subgrid').jqGrid({edit:false,add:false,del:false});
	//jQuery('#'+id).find("#indicator").remove();// this also works
	if(jQuery('#'+id).find("#indicator").length == 0)
		jQuery('#'+id).prepend("<div id='indicator'>Click on Trade Detail to view Trade Ticket</div>");
}