var oldUserRoles = new Array();

$(document).ready(function() {
	callAjaxToGetUserRole("getAdminUserRoleJson.do");	
	callAjaxToGetUsers("getAdminUsersJson.do");	
	
	$("#updateUserRole").click(function(){
		var userIdVal = $('#userId').val();
		//var userRole = $('input[name=userRole]:checked').val();
		var newUserRoleIds = new Array();
		$('input[name=userRole]').each(function () {
			if(this.checked){
				newUserRoleIds.push($(this).val());
			}
		});
		//alert("oldUserRoles len= " + oldUserRoles.length + " newUserRoleIds len=" + newUserRoleIds.length);
		if(userIdVal != null && userIdVal != undefined && userIdVal != "-1" && userIdVal != ""){
			if(newUserRoleIds != undefined && newUserRoleIds.length > 0){
				var uri = "updateUserRole.do" + "?" + "userId=" + userIdVal + "&oldUserRoleIds=" + oldUserRoles + "&newUserRoleIds=" + newUserRoleIds;
				callAjaxToUpdateUserRole(uri, userIdVal);
			}else{
				alertHandlingForRole($("#adminUserRoleErrorSection"), "Please select 'User Role'.");
			}
		}else{
			alertHandlingForRole($("#adminUserRoleErrorSection"), "Please select 'User ID'.");
		}
	});
});

/*Get the json data form the uri and load the adminUserRole*/
function callAjaxToGetUserRole(uri){
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
	    		alertHandlingForRole($("#adminUserRoleErrorSection"), response[0].errorMessage);
	    		$('#userRoles').hide();
	    		$('#adminUserRoleSuccessSection').hide();
	    	}else{
	    		$('#adminUserRoleErrorSection').hide();
	    		$('#adminUserRoleSuccessSection').hide();
	    		$("#userRoles").html("");	
	    		$('#userRoles').show();
	    		
        		for(var len=0; len<response.length; len++){
        			var checkbox = "<input type='checkbox' class='adminUserRoleRadio' name='userRole' value='"+response[len].role_ID+"'/>";			
        			
        			html = "<div  class='input-group' style='width: 35%; margin-left: 10px; margin-top: 5px; float: left;'> " +
						      " <span class='input-group-addon'> " +
						      	checkbox+
						      " </span> " +
						      "<input type='text' class='form-control' value='"+response[len].role_NAME+"'/> "+
						    " </div>";
        			
        			$("#userRoles").append(html);
        		}
	    	}
        },
        error: function(jqXHR,error, errorThrown) {  
        	//TODO- what happens if server is down
        	//alert(error + " " + jqXHR.status);
        }
	});
}

/*Get the json data form the uri and load the users
 * if userId is passed, the success message hide is disabled and set the dropdown selected value by the user*/
function callAjaxToGetUsers(uri, userId){
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
	    		alertHandlingForRole($("#adminUserRoleErrorSection"), response[0].errorMessage);
	    		$('#userRoles').hide();
	    		if(userId == undefined || userId == "" )
	    			$('#adminUserRoleSuccessSection').hide();
	    	}else{
	    		$('#adminUserRoleErrorSection').hide();
	    		if(userId == undefined || userId == "" )
	    			$('#adminUserRoleSuccessSection').hide();
	    		$('#userId').empty();
	    		
	    		html += "<option value='-1'>--Select--</option>";
        		for(var len=0; len<response.length; len++){
        			html += "<option value="+response[len].user_CD+">"+response[len].user_NAME+"</option>";
        		}
        		$("#userId").append(html);
        		
        		if(userId != undefined || userId != "" )
        			$('#userId').val(userId);
        		
        		$("#userId").change(function(){
        			$("input:checkbox").prop('checked',false);
        			for(var len=0; len<response.length; len++){
        				//console.log(response[len]);
        				if(response[len].user_CD ==  $("#userId").val()){
        					var uri = "getUserRoles.do" + "?userCD=" + response[len].user_CD;
        					//check user roles for that user
        					callAjaxToGetUserRoles(uri);
        					
        					//var user_role = response[len].fk_USER_ROLE;
        					//get user role
        					//alert(user_role);
        					//$("input:checkbox[value="+user_role+"]").prop("checked", true);
        					
        				} else{
        					//$("input:radio[value="+response[len].fk_USER_ROLE+"]").prop("checked", false);
        				}
        			}
        		});
	    	}
        },
        error: function(jqXHR,error, errorThrown) {  
        	//TODO- what happens if server is down
        	//alert(error + " " + jqXHR.status);
        }
	});
}


/*update adminUserRole*/
function callAjaxToUpdateUserRole(uri, userId){
	oldUserRoles.length = 0; //clear the old user role array
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
	    		alertHandlingForRole($("#adminUserRoleErrorSection"), response[0].errorMessage);
	    		$('#adminUserRoleSuccessSection').hide();
	    	}else{
	    		$('#adminUserRoleErrorSection').hide();
	    		$('#adminUserRoleSuccessSection').hide();
	    		if(response[0].errorMessage != undefined && response[0].errorMessage != ""){
	    			alertHandlingForRole($("#mappingModalErrorSection"), response[0].errorMessage);
		    		$('#adminUserRoleSuccessSection').html("");
		    		$('#adminUserRoleSuccessSection').hide();
		    	}else{
		    		if(response[0].successMsg != null && response[0].successMsg != ""){
			    		$('#adminUserRoleErrorSection').hide();
			    		alertHandlingForRole($("#adminUserRoleSuccessSection"), "Successfully updated");
			    		
			    		//reload dropdown and select the selected by the user
			    		callAjaxToGetUsers("getAdminUsersJson.do", userId);
			    		//$('#userId').val(userId); //userId
			    	}else{
			    		//TODO
			    	}
		    	}
	    	}
        },
        error: function(jqXHR,error, errorThrown) {  
        	//TODO- what happens if server is down
        	//alert(error + " " + jqXHR.status);
        }
	});
}

/*Get roles of the user and then check the check box(s) of roles for that user*/
function callAjaxToGetUserRoles(uri){
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
        	if(response[0].errorMessage != undefined && response[0].errorMessage != ""){
	    		alertHandlingForRole($("#adminUserRoleErrorSection"), response[0].errorMessage);
	    		return "";
	    	}else{
				//console.log(response);
				for(var roleLen = 0; roleLen < response.length; roleLen++){
					$("input:checkbox[value="+response[roleLen].role_ID+"]").prop("checked", true);
					oldUserRoles.push(response[roleLen].role_ID);
				}
	    	}
        },
        error: function(jqXHR,error, errorThrown) {  
        	//TODO- what happens if server is down
        	//alert(error + " " + jqXHR.status);
        }
	});
}

function alertHandlingForRole(alert, msg) {
	alert.text(msg);
	alert.css("display", "inherit").fadeOut(10000);
}
