$(document).ready(function() {
	callAjaxToGetToggleData("GetToggleValue.do");	
});

/*Get the json data form the uri and load the toggle*/
function callAjaxToGetToggleData(uri){
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
	    		$('#toggleErrorSection').html(response[0].errorMessage);
	    		$('#toggleSection').hide();
	    		$('#toggleSuccessSection').hide();
	    	}else{
	    		$('#toggleSection').show();
	    		$('#toggleErrorSection').hide();
	    		$('#toggleSuccessSection').hide();
	    		$("#toggleSection").html("");	    		
	    		
        		for(var len=0; len<response.length; len++){
        			var onCheckbox = "";
        			var offCheckBox = "";
        			if(response[len].param_VAL == 'STARTUP'){
        				onCheckbox =  "<input type='radio' class='toggleRadio' name='"+response[len].param_SYST_CD+"' checked='checked' value='On'/>";
        				offCheckBox =  "<input type='radio' class='toggleRadio' name='"+response[len].param_SYST_CD+"' value='Off'/>";
        			}else{
        				onCheckbox =  "<input type='radio' class='toggleRadio' name='"+response[len].param_SYST_CD+"' value='On'/>";
        				offCheckBox =  "<input type='radio' class='toggleRadio' name='"+response[len].param_SYST_CD+"' checked='checked' value='Off'/>";
        			}	
        			
        			html = "<div class='row' style='padding: 5px 5px 5px 20px; width: 100%;'>"+
				    		"<span class='input-group-addon' style='width: 42%; float: left; height: 34px; border-right: 1px solid #CCCCCC !important; border-bottom-right-radius: 4px !important ;border-top-right-radius: 4px !important;'>"+response[len].param_SYST_CD+"</span>"+
				    	    "<div class='input-group' style='width: 22%; float: left; margin-left: 10px;'>"+
				    	      "<span class='input-group-addon'>"+
				    	      	onCheckbox +
				    	      "</span>"+
				    	      "<input type='text' class='form-control' value='On'>"+
				    	    "</div>"+
				    	    
				    	    "<div class='input-group' style='width: 22%; margin-left: 10px; float: left;'>"+
				    	      "<span class='input-group-addon'>"+
				    	      	offCheckBox+
				    	      "</span>"+
				    	      "<input type='text' class='form-control' value='Off'>"+
				    	    "</div>"+
				        "</div>";
        			
        			$("#toggleSection").append(html);
        		}
        		
        		$(".toggleRadio").change(function(){
        			//call update toggle ajax
        			//alert($(this).val()+ " " + $(this).attr('name'));
        			var radioVal = "STARTUP";
        			if($(this).val() == "Off")
        				radioVal = "SHUTDOWN";
        			
        			var uri = "UpdateToggleValue.do" + "?" + "paramSystCd=" + $(this).attr('name') + "&paramVal=" + radioVal;
        			callAjaxToUpdateToggleData(uri);	
        		});
	    	}
        },
        error: function(jqXHR,error, errorThrown) {  
        	//TODO- what happens if server is down
        	//alert(error + " " + jqXHR.status);
        }
	});
}

/*update the toggle*/
function callAjaxToUpdateToggleData(uri){
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
	    		$('#toggleErrorSection').html(response[0].errorMessage);
	    		$('#toggleErrorSection').show();
	    		$('#toggleSuccessSection').hide();
	    	}else{
	    		$('#toggleErrorSection').hide();
	    		$('#toggleSuccessSection').html(response[0].successMsg);
	    		$('#toggleSuccessSection').show();
	    	}
        },
        error: function(jqXHR,error, errorThrown) {  
        	//TODO- what happens if server is down
        	//alert(error + " " + jqXHR.status);
        }
	});
}
