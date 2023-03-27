jQuery(document).ready(function ($) {
		$('#SecurityDetailstabs').tab();
});
var subGridFlg = false;
$(document).ready(
			function() {
				$("#security")
						.jqGrid(
								{
									url : 'SecurityMngJson.do',
									datatype : "json",
									mtype: "POST",
									autowidth : true,
									loadonce: true,
						            sortable:true,
						            height: gridHeight,
									//Change
									colNames : [ '', 'Status', 'Source', 'Security Group', 
									              'Security Type', 'CUSIP', 
									              'Security Description', 'Ticker', 
									              'Issue Price', 'Issue Date', 'Issuer ID', 
									              'Country', 'Currency', 'Coupon Frequency', 'sec id', '' ],
									//Change
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
										name : 'status',
										index : 'status',
										autowidth : true
									},{
										name : 'source',
										index : 'source',
										autowidth : true
									},{
										name : 'sm_SEC_GROUP',
										index : 'sm_SEC_GROUP',
										autowidth : true
									},{
										name : 'sm_SEC_TYPE',
										index : 'sm_SEC_TYPE',
										autowidth : true
									},{
										name : 'cusip',
										index : 'cusip',
										autowidth : true
										
									},{
										name : 'desc_INSTMT',
										index : 'desc_INSTMT',
										autowidth : true
										
									},{
										name : 'ticker',
										index : 'ticker',
										autowidth : true
										
									},{
										name : 'issue_PRICE',
										index : 'issue_PRICE',
										autowidth : true,
										formatter:'number',	
										formatoptions:{defaultValue:'', decimalPlaces: 3 },
										align:'right'
									},{
										name : 'issue_DT',
										index : 'issue_DT',
										autowidth : true,
										sorttype : "date",
										formatter : "date",
										formatoptions: {newformat:'Y/m/d'}
									},{
										name : 'issuer_ID',
										index : 'issuer_ID',
										autowidth : true
										
									},{
										name : 'country',
										index : 'country',
										autowidth : true
									},{
										name : 'currency',
										index : 'currency',
										width:170
									},{
										name : 'coup_FREQ',
										index : 'coup_FREQ',
										width:200
									},{
										name : 'tph_SEC_SEQ_NO',
										index : 'tph_SEC_SEQ_NO',
										hidden: true
									},{
										name : 'mapping',
										index : 'mapping',
										width:55,
										search: false
									}],
									sortname: "issue_DT",
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
									    		//TODO-to evaluate set time out is required
									    		setTimeout(function () {
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
									    
									    var statusArr = ["Failed", "failed", "f", "F"];
									    attachCheckBox($this, "security", statusArr);
									    //attachRadioBtn($this, "security", statusArr);
								    	//attachSecurityLink($this, "security", "getSecurityMappingDetails.do", "securityMappingModal");
								    	
										var mapUrl = "getSecurityMappingDetails.do";
										var mappingFor = "security";
								    	attachLinkForDataComparision($this, mapUrl, "security", mappingFor);//grid, jsonurl, grid_id, mappingFor
									    
								    	//var drillDownStatus = getQueryVariable("drillDown");
								    	//loadGridForDashBoardFilter(drillDownStatus);
									},
									onSelectRow: function(rowid) {
										if (subGridFlg != true) {
										    // do something with row
										    var myGrid = $('#security');
									    	var id = myGrid.jqGrid ('getCell', rowid, 'tph_SEC_SEQ_NO');
									    	
									    	var uri = "EMFSecurityDetail.do?sec_id="+id;
									    	$('#myModal').modal('show');
									    	getSecurityDetails(id, uri);//TODO-rename the function name
									    	
									    	var source = myGrid.jqGrid ('getCell', rowid, 'source');
									    	var urlStrSecattrGrid = "getIncomingSecurityAttributeDetails.do?sec_id=" + id+ "&source="+source+"&r="+Math.random();
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
									    	var issuerCD = myGrid.jqGrid ('getCell', row_id, 'issuer_ID');
									    	var source = myGrid.jqGrid ('getCell', row_id, 'source');
									    	var urlStrIssuerGrid = "getIncomingIssuerDetails.do?issuer_cd=" + issuerCD + "&source="+source+"&r="+Math.random();

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
													sortable:false,
												    height: '100%',
												    autowidth:true,
												    caption:"Issuer",
												    loadComplete: function(data) {
														var $this = $(this);
														resizeColumns($this);
												        var datatype = $this.getGridParam('datatype');
													    if (datatype === "xml" || datatype === "json") {
													    	if(data[0].errorCode != undefined && data[0].errorCode != ""){
													    		$('#errorSection').html(data[0].errorMessage);
													    		$('#errorSection').show();
													    		//Hide empty row and show the error message bottom bar of the grid
													    		$('#'+subgrid_id+' tr').hide();
													    		$('#'+subgrid_id+' .ui-paging-info').html(data[0].errorMessage);
													    		//remove sortable, coz, on click it will load an empty row
													    		$('#'+subgrid_id+' .ui-jqgrid-htable tr').removeClass('ui-sortable');
													    		$('#'+subgrid_id+' .ui-jqgrid-htable tr div').removeClass('ui-jqgrid-sortable');
													    	}else{
													    		$('#errorSection').html("");
													    		$('#errorSection').hide();
													    	}
													    }
												    }
												});
										    	
										    	/*******   Display Security Attribute Grid  ***********/
										  /*  	var secID = myGrid.jqGrid ('getCell', row_id, 'tph_SEC_SEQ_NO');
										    	var source = myGrid.jqGrid ('getCell', row_id, 'source');
										    	
										    	var urlStrSecattrGrid = "getIncomingSecurityAttributeDetails.do?sec_id=" + secID+ "&source="+source+"&r="+Math.random();
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
									    subGridRowColapsed: function(subgrid_id, row_id){
									    	$('#errorSection').html("");
								    		$('#errorSection').hide();
									    },
								    	
									viewrecords : true,
									rowNum : 20,
									rowList : [20],
									pager : '#security-pager',
									caption:"SECURITY",
									//Change
									multipleSearch:true,
									toolbar:[true, "top"],
									hidegrid: false
				});
				jQuery("#security").jqGrid('navGrid', '#security-pager', {
					edit : false,
					add : false,
					del : false
				});
				//jQuery("#security").jqGrid('gridResize', { minHeight: 400 });
				//Change
				//TODO-check with Sangeetha for these methods
				setTransTradeTyp(security, incoming);
				attachToolbarBtns($("#t_security"));
});


/*
 * Auto reload grid with new data from server, 
 * it will not reset filter, 
 * after reload it should apply filter(s) that user selected
 * if the filter data is not present in the server for the new response, it will remove the filter as well as the filter effect of it(if selected)
 * if new filter data is present in the server, it will add to the filter section
*/
//TODO- improve this call
setInterval(function() {
	autoRefreshFlg = true;
	$("#security").jqGrid('setGridParam', 
		{
			datatype:"json",
			page:1
		}).trigger("reloadGrid");
//}, 10000);//auto refresh the grid every mentioned millisonds	
}, 300000);//auto refresh the grid every mentioned millisonds

/*function attachSecurityLink(grid, grid_id, jsonurl) {
	var gridRowIds = grid.getDataIDs();
	gridRowIds.forEach(function(val, index) {
		var gridRowId = val;
		var tphSeqNo = grid.jqGrid ('getCell', gridRowId, 'tph_SEC_SEQ_NO'); 
		var tphSource = grid.jqGrid ('getCell', gridRowId, 'source'); 
		
		var btn_id = grid_id + "_mapping_" + tphSeqNo;
		var btn = '<input type="button" id="'+btn_id+'" class="mapping-btn-icon" onclick="clickMappingBtn('+gridRowId+', \''+jsonurl+'\', \''+tphSeqNo+'\', \''+tphSource+'\')" title="See Mapping"/>';
		grid.jqGrid('setRowData', gridRowId, {mapping:btn});
		
	});
}

function clickMappingBtn(gridRowId, url, refNo, source, modelId){
	//alert(gridRowId);
	//alert("id=" + gridRowId + " url=" +url+" refno=" +refNo+" source=" +source);$('#myModal').modal('show');
	$('#securityModal').modal('show');
	var uri = url + "?" + "refNo=" + refNo + "&source=" + source;
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
        		$('#securityModalRadioSection').html("");
    	    	$('#securityModalErrorSection').html(response[0].errorMessage);
    	    	$('#securityModalTable').hide();
	    	}else{
	    		$('#securityModalErrorSection').html("");
    	    	$('#securityModalTable').show();
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
	    		$('#securityModalRadioSection').html(radioHtml);
	    		
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
	    	  	
	    	  	$('#securityModalTableBody').html(html);
	    	  	$('#securityOutgoingModalTableBody').html(outgoingHtml);
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
		    	  	
		    	  	$('#securityOutgoingModalTableBody').html(outgoingHtml);
	    	  	});
	    	}
        },
        error: function(jqXHR,error, errorThrown) {  
        	//TODO- what happens if server is down
        	//alert(error + " " + jqXHR.status);
        }
	});
}*/