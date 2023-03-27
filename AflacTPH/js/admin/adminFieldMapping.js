$(document).ready(
			function() {
				$("#adminMappingGrid")
						.jqGrid(
								{
									url : 'AdminFieldMappingJson.do',
									datatype : "json",
									mtype: "POST",
									autowidth : true,
									loadonce: true,
						            sortable:true,
									height: 300,
									//Change
									colNames : [ 'Field Name', 'Client Code', 'Client Value', 'TPH Value' ],
									//Change
									colModel : [{
										name : 'field_NAME',
										index : 'field_NAME',
										autowidth : true
									},{
										name : 'cust_CD',
										index : 'cust_CD',
										autowidth : true
									},{
										name : 'client_CD_VALUE',
										index : 'client_CD_VALUE',
										autowidth : true
									},{
										name : 'tph_CD_VALUE',
										index : 'tph_CD_VALUE',
										autowidth : true
									}],
									sortname: "field_NAME",
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
									    		$('#adminMappingGrid tr').hide();
									    		$('.ui-paging-info').html(data[0].errorMessage);
									    		//remove sortable, coz, on click it will load an empty row
									    		$('.ui-jqgrid-htable tr').removeClass('ui-sortable');
									    		$('.ui-jqgrid-htable tr div').removeClass('ui-jqgrid-sortable');
									    	}else{
									    		$('#errorSection').html("");
									    		$('#errorSection').hide();
									    	}
									    } 
									},
									viewrecords : true,
									rowNum : 10,
									rowList : [ 10, 20, 30 ],
									pager : '#adminMappingGrid-pager',
									caption:"Mapping",
									//Change
									multipleSearch:true,
									toolbar:[true, "top"]
				});
				
				jQuery("#adminMappingGrid").jqGrid('navGrid', '#adminMappingGrid-pager',{
					edit : false,
					add : false,
					del : false
				});
				
		$("#addFieldMapping").click(function(){
			$('#errorSection').hide();
    		$("#errorSection").html('');
    		
    		$("#mappingModal").modal('show');//Show the modal
			
    		$(".mappingInput").val("");//clear all data
    		//Call ajax to pull field names
    		callAjaxToGetTPHCode("getTPHCodeJson.do");
		});	
		
		$("#resetMappingVal").click(function(){
			$(".mappingInput").val("");//clear all data
			//reset dropdown fieldName
			$('#fieldName').get(0).selectedIndex = 0;
			$('#mappingModalErrorSection').html("");
    		$('#mappingModalErrorSection').hide();
		});
		
		$("#saveMapping").click(function(){
			var fieldName = $('#fieldName').val();
			var clientCode = $('#clientCode').val();
			var clientValue = $('#clientValue').val();
			var tphValue = $('#tphValue').val();
			if(fieldName != null && fieldName != "-1"){
				if(clientCode.trim() != ""){
					if(clientValue.trim() != ""){
						if(tphValue.trim() != ""){
							$('#mappingModalErrorSection').html("");
							$('#mappingModalErrorSection').hide();
							var uri = "saveOrDeleteFieldMapping.do" + "?" + "fieldName=" + fieldName + "&clientCode=" + clientCode + "&clientValue=" + clientValue +"&tphValue=" + tphValue + "&action=ADD";
							callAjaxToSaveTPHCode(uri);
						}else{
							alertHandling($("#mappingModalErrorSection"), "Please enter 'TPH Value'.");
						}
					}else{
						alertHandling($("#mappingModalErrorSection"), "Please enter 'Client Value'.");
					}
				}else{
					alertHandling($("#mappingModalErrorSection"), "Please enter 'Client Code'.");
				}
			}else{
				alertHandling($("#mappingModalErrorSection"), "Please select 'Field Name'.");
			}
		});
		
		$("#deleteFieldMapping").click(function(){
			var rowId = $("#adminMappingGrid").jqGrid('getGridParam','selrow');
			if(rowId == null){
				$('#successSection').html("");
	    		$('#successSection').hide();
	    		alertHandling($("#errorSection"), "Please select one row to delete");
			}
			else{
				var rowData = $("#adminMappingGrid").jqGrid('getRowData',rowId);  
				//alert("selected = " + rowData.field_NAME + " " + rowData.cust_CD + " " + rowData.client_CD_VALUE + " " + rowData.tph_CD_VALUE);
				var uri = "saveOrDeleteFieldMapping.do" + "?" + "fieldName=" + rowData.field_NAME + "&clientCode=" + rowData.cust_CD + "&clientValue=" + rowData.client_CD_VALUE +"&tphValue=" + rowData.tph_CD_VALUE + "&action=DELETE";
				callAjaxToDeleteTPHCode(uri);
			}
		});
});

setInterval(function() {
	autoRefreshFlg = true;
	$("#adminMappingGrid").jqGrid('setGridParam', 
		{
			datatype:"json",
			page:1
		}).trigger("reloadGrid");
}, 300000);//auto refresh the grid every mentioned millisonds

/*Get the json data form the uri and load in the modal to display the field names*/
function callAjaxToGetTPHCode(uri){
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
        	if(response[0].errorMessage != undefined && response[0].errorMessage != ""){
	    		//$('#mappingModalErrorSection').html(response[0].errorMessage);
	    		alertHandling($("#mappingModalErrorSection"), response[0].errorMessage);
	    		$('#mappingAddSection').hide();
	    	}else{
	    		$('#mappingAddSection').show();
	    		$('#mappingModalErrorSection').hide();
	    		$('#fieldName').empty();//clear field name dropdown values
	    		
        		html += "<option value='-1'>--Select--</option>";
        		for(var len=0; len<response.length; len++){
        			html += "<option value="+response[len].field_NAME+">"+response[len].field_NAME+"</option>";
        		}
        		$("#fieldName").append(html);
	    	}
        },
        error: function(jqXHR,error, errorThrown) {  
        	//TODO- what happens if server is down
        	//alert(error + " " + jqXHR.status);
        }
	});
}

/*save field mapping*/
function callAjaxToSaveTPHCode(uri){
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
        	if(response[0].errorMessage != undefined && response[0].errorMessage != ""){
	    		/*$('#mappingModalErrorSection').html(response[0].errorMessage);
	    		$('#mappingModalErrorSection').show();*/
	    		alertHandling($("#mappingModalErrorSection"), response[0].errorMessage);
	    		$('#successSection').html("");
	    		$('#successSection').hide();
	    		$('#errorSection').html("");
	    		$('#errorSection').hide();
	    	}else{
	    		//alert(response[0].successMsg);
	    		if(response[0].successMsg != null && response[0].successMsg != ""){
		    		$('#mappingAddSection').show();
		    		$('#mappingModalErrorSection').hide();
		    		$("#mappingModal").modal('hide');//hide the modal
		    		
		    		$('#errorSection').hide();
		    		alertHandling($("#successSection"), "Successfully saved");
		    		
		    		//reload the grid with new data
		    		$("#adminMappingGrid").jqGrid('setGridParam', 
					{
						datatype:"json",
						page:1
					}).trigger("reloadGrid");
		    	}else{
		    		//TODO
		    	}
	    	}
        },
        error: function(jqXHR,error, errorThrown) {  
        	//TODO- what happens if server is down
        	//alert(error + " " + jqXHR.status);
        }
	});
}

/*Delete field mapping*/
function callAjaxToDeleteTPHCode(uri){
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
        	if(response[0].errorMessage != undefined && response[0].errorMessage != ""){
	    		/*$('#errorSection').html(response[0].errorMessage);
	    		$('#errorSection').show();*/
	    		alertHandling($("#errorSection"), response[0].errorMessage);
	    		$('#successSection').html("");
	    		$('#successSection').hide();
	    	}else{
	    		//alert(response[0].successCode);
	    		if(response[0].successMsg != null && response[0].successMsg != ""){
		    		$('#errorSection').hide();
		    		$("#errorSection").html('');
		    		alertHandling($("#successSection"), "Successfully deleted");
		    		
		    		//reload the grid with new data
		    		$("#adminMappingGrid").jqGrid('setGridParam', 
					{
						datatype:"json",
						page:1
					}).trigger("reloadGrid");
	    		}else{
	    			//TODO
	    		}
	    	}
        },
        error: function(jqXHR,error, errorThrown) {  
        	//TODO- what happens if server is down
        	//alert(error + " " + jqXHR.status);
        }
	});
}

function alertHandling(alert, msg) {
	alert.text(msg);
	alert.css("display", "inherit").fadeOut(10000);
}
