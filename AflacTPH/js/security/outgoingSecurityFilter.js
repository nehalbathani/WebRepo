
$(document).ready(function() {
	/* activate outgoing filter Accordion*/
	$( "#FilterAccordion" ).accordion({
	      collapsible: true
	});		
});

var allStatus = new Array();
var allSources = new Array();
var allSecurityGroup = new Array();

function loadFilterValue(response){
	var statusHtml = "";
	var sourceHtml = "";
	var securityGroupHtml = "";
	
	//var issueDtHtml = "<option value=''>--Select--</option>";
	//var allIssueDates = new Array();
	
	/* dynamically add filter data to filter section from the outgoing grid data*/
	for(var jqlen = 0, uniqueStatLen = 0, uniqueSourceLen = 0, uniqSecurityGroupLen = 0, issueDtLen = 0; jqlen<response.length; jqlen++){
		/* add status filters*/
		if(response[jqlen].tph_STATUS != "" && !isUnique(response[jqlen].tph_STATUS, allStatus)){
    		statusHtml += "<input type='checkbox' name='"+response[jqlen].tph_STATUS+"' value='"+response[jqlen].tph_STATUS+"' class='status'>&nbsp;"+response[jqlen].tph_STATUS+"</input><br/>";
    		allStatus[uniqueStatLen++] = response[jqlen].tph_STATUS;
    	}
		
		/* add source filters*/
		if(response[jqlen].source != "" && !isUnique(response[jqlen].source, allSources)){
			var srcStr = "<div><input type='checkbox' name='" + 
                         response[jqlen].source + "' value='" + 
                         response[jqlen].source + 
                         "' class='source'>&nbsp;" + 
                         response[jqlen].source + "</input></div>";
            if (!(response[jqlen].source == 'AFL' || response[jqlen].source == 'afl')) {
            	sourceHtml = sourceHtml.concat(srcStr); 
            } else {
	            sourceHtml = srcStr.concat(sourceHtml);
            }
			allSources[uniqueSourceLen++] = response[jqlen].source;
    	}
		
		/* add security group filters*/
		if(response[jqlen].sec_GROUP != "" && !isUnique(response[jqlen].sec_GROUP, allSecurityGroup)){
			securityGroupHtml += "<input type='checkbox' name='"+response[jqlen].sec_GROUP+"' value='"+response[jqlen].sec_GROUP+"' class='securityGroup'>&nbsp;"+response[jqlen].sec_GROUP+"</input><br/>";
			allSecurityGroup[uniqSecurityGroupLen++] = response[jqlen].sec_GROUP;
    	}
		
		/* add issued dates filter
		if(response[jqlen].issue_DATE != "" && !isUnique(response[jqlen].issue_DATE, allIssueDates)){
			var dt = $.datepicker.formatDate('M, dd, yy', new Date(response[jqlen].issue_DATE));
			var dtVal = $.datepicker.formatDate('yy/mm/dd', new Date(response[jqlen].issue_DATE));
			issueDtHtml += "<option value='"+dtVal+"' class='issueDate'>"+dt+"</option>";
			allIssueDates[issueDtLen++] = response[jqlen].issue_DATE;
    	}*/
	}

	/* load the filter data to the corresponding UI*/
	$('#securityStatusAccordion').html(statusHtml);
	$('#securitySourceAccordion').html(sourceHtml);
	$('#securitySecurityGroupAccordion').html(securityGroupHtml);
	//$('#securityIssueDateAccordion').html(issueDtHtml);
	
	/* If any status filter is checked */
	$('.status').click(function () {
		commonFilterCall();
	});
	
	/* If any source filter is checked */
	$('.source').click(function () {
		commonFilterCall();
	});
	
	/* If any source filter is checked */
	$('.securityGroup').click(function () {
		commonFilterCall();
	});
	
	/* If a issue date filter is selected */
	/*$('#securityIssueDateAccordion').change(function(){
		commonFilterCall();
	});*/
	
	/*remove all the status filter that user selected*/
	$('#RemoveStatusFilter').click(function(){
		$(".status").attr("checked", false);
		commonFilterCall();
	});
	
	/*remove all the source filter that user selected*/
	$('#RemoveSourceFilter').click(function(){
		$(".source").attr("checked", false);
		commonFilterCall();
	});
	
	/*remove all the securityGroup filter that user selected*/
	$('#RemoveSecurityGroupFilter').click(function(){
		$(".securityGroup").attr("checked", false);
		commonFilterCall();
	});
	
	/*remove all the issue date filter that user selected*/
	/*$('#RemoveIssueDateFilter').click(function(){
		$('#securityIssueDateAccordion').get(0).selectedIndex = 0;
		commonFilterCall();
	});*/
	
	/*On click reload icon of the grid, reset the filter*/
	$('.ui-icon-refresh').click(function(){
		//reset filters
		$(".status").attr("checked", false);
		$(".source").attr("checked", false);
		$(".securityGroup").attr("checked", false);
		//$('#securityIssueDateAccordion').get(0).selectedIndex = 0;
		$('#FilterSelectedDiv').hide();
	});
	
	//for dash-board filtration testing for outgoing security
    //loadGridForDashBoardFilter("COMPLETE");
}

//to add new status after resend/discard
function includeNewStatus(jqGridData) {
	var statusHtml = "";
	
	//remove if not present in the jqGridData
	var dataToRemove = new Array();
	var removeCount = 0;
	var dataToRemoveFlag = false;
	//get the data to remove
	/*for(var len = 0; len < jqGridData.length; len++) {
		var status = jqGridData[len].status;
		dataToRemoveFlag = false;
		for(var datalen = 0; datalen < allStatus.length; datalen++) {
			if(status == allStatus[datalen]){
				dataToRemoveFlag = true;
				break;
			}
		}
		
		if(!dataToRemoveFlag){
			dataToRemove[removeCount++] = status;
		}
	}

	//remove from filter if data is not present in DB(json data)
	for(var len = 0; len < dataToRemove.length; len++) {
		var nm = 'input[name="'+dataToRemove[len]+'"]';
		$(nm).parent().remove();
		//remove from array
		allStatus.splice(len,1);
	}*/
	
	//Add new status
	for(var len = 0, statLen = allStatus.length; len < jqGridData.length; len++) {
		var status = jqGridData[len].status;
		if (status != "" && !isUnique(status, allStatus)) {
			statusHtml += "<input type='checkbox' name='" + 
                          status + "' value='" + status + 
                          "' class='status'>&nbsp;" + 
                          status + "</input><br/>";
            allStatus[statLen++] = status;
		}
	}
	$('#securityStatusAccordion').append(statusHtml);
}

//to add new source after resend/discard or refresh
function includeNewSource(jqGridData) {
	var html = "";
	
	//remove if not present in the jqGridData
	var dataToRemove = new Array();
	var removeCount = 0;
	var dataToRemoveFlag = false;
	//get the data to remove
	for(var len = 0; len < jqGridData.length; len++) {
		var data = jqGridData[len].source;
		dataToRemoveFlag = false;
		for(var datalen = 0; datalen < allSources.length; datalen++) {
			if(data == allSources[datalen]){
				dataToRemoveFlag = true;
				break;
			}
		}
		
		if(!dataToRemoveFlag){
			dataToRemove[removeCount++] = data;
		}
	}

	//remove from filter if data is not present in DB(json data)
	for(var len = 0; len < dataToRemove.length; len++) {
		var nm = 'input[name="'+dataToRemove[len]+'"]';
		$(nm).parent().remove();
		//remove from array
		allSources.splice(len,1);
	}
	
	for(var len = 0, statLen = allSources.length; len < jqGridData.length; len++) {
		var source = jqGridData[len].source;
		if (source != "" && !isUnique(source, allSources)) {
			html += "<input type='checkbox' name='" + 
						source + "' value='" + source + 
                          "' class='source'>&nbsp;" + 
                          source + "</input><br/>";
			allSources[statLen++] = source;
		}
	}
	$('#securitySourceAccordion').append(html);
}

//to add new security group after resend/discard or refresh
function includeNewSecurityGroup(jqGridData) {
	var html = "";
	
	//remove if not present in the jqGridData
	var dataToRemove = new Array();
	var removeCount = 0;
	var dataToRemoveFlag = false;
	//get the data to remove
	for(var len = 0; len < jqGridData.length; len++) {
		var data = jqGridData[len].sec_GROUP;
		dataToRemoveFlag = false;
		for(var datalen = 0; datalen < allSecurityGroup.length; datalen++) {
			if(data == allSecurityGroup[datalen]){
				dataToRemoveFlag = true;
				break;
			}
		}
		
		if(!dataToRemoveFlag){
			dataToRemove[removeCount++] = data;
		}
	}

	//remove from filter if data is not present in DB(json data)
	for(var len = 0; len < dataToRemove.length; len++) {
		var nm = 'input[name="'+dataToRemove[len]+'"]';
		$(nm).parent().remove();
		//remove from array
		allSecurityGroup.splice(len,1);
	}
	
	for(var len = 0, statLen = allSecurityGroup.length; len < jqGridData.length; len++) {
		var securityGroup = jqGridData[len].sec_GROUP;
		if (securityGroup != "" && !isUnique(securityGroup, allSecurityGroup)) {
			html += "<input type='checkbox' name='" + 
			securityGroup + "' value='" + securityGroup + 
                          "' class='securityGroup'>&nbsp;" + 
                          securityGroup + "</input><br/>";
			allSecurityGroup[statLen++] = securityGroup;
		}
	}
	$('#securitySecurityGroupAccordion').append(html);
}


/* any filter is selected or changed, filter grid data */
function commonFilterCall(){
	var isFilterAvail = false;
	var filter = new Array();
	var statusFilter = "";
	var statusCount = 0;
	/* Create status filter array which contains all the status filter of each type user selected. */
	$('.status').each(function() {
		if($(this).is(':checked')){
			/* Create status filter. 
			 * field: should matched with the grid column index to filter
			 * op : is the filter option, 'eq' is equal
			 * data: the value of the checkbox to be filtered
			 */
			filter[statusCount++] = {field:"tph_STATUS","op":"eq","data":$(this).val()};
			//form filter selected view formation
			statusFilter +=  $(this).val() + ", ";
		}
	});
	/*Show user filter selection to to UI*/
	if(statusFilter != ""){
		statusFilter = statusFilter.substring(0, statusFilter.length-2);
		$("#statusFilterDisp").show();
		$("#UserStatusFilter").html(statusFilter);
		isFilterAvail = true;
		$('#RemoveStatusFilter').tooltip();
	}
	else
		$("#statusFilterDisp").hide();
	
	var sourceCount=0;
	var sourceFilter = "";
	var filter2 = new Array();
	/* Create source filter array which contains all the source filter of each type user selected. */
	$('.source').each(function() {
		if($(this).is(':checked')){
			/* Create source filter. 
			 * field: should matched with the grid column index to filter
			 * op : is the filter option, 'eq' is equal
			 * data: the value of the checkbox to be filtered
			 */
			filter2[sourceCount++] = {field:"source","op":"eq","data":$(this).val()};
			//form filter selected view formation
			sourceFilter +=  $(this).val() + ", ";
		}
	});
	/*Show user filter selection to to UI*/
	if(sourceFilter != ""){
		sourceFilter = sourceFilter.substring(0, sourceFilter.length-2);
		$("#sourceFilterDisp").show();
		$("#UserSourceFilter").html(sourceFilter);
		isFilterAvail = true;
		$('#RemoveSourceFilter').tooltip();
	}
	else
		$("#sourceFilterDisp").hide();
	
	var securityGroupCount=0;
	var securityGroupSelceted = "";
	var securityGroupFilter = new Array();
	/* Create source filter array which contains all the security group filter of each type user selected. */
	$('.securityGroup').each(function() {
		if($(this).is(':checked')){
			/* Create security group filter. 
			 * field: should matched with the grid column index to filter
			 * op : is the filter option, 'eq' is equal
			 * data: the value of the checkbox to be filtered
			 */
			securityGroupFilter[securityGroupCount++] = {field:"sec_GROUP","op":"eq","data":$(this).val()};
			//form filter selected view formation
			securityGroupSelceted +=  $(this).val() + ", ";
		}
	});
	
	/*Show user filter selection to to UI*/
	if(securityGroupSelceted != ""){
		securityGroupSelceted = securityGroupSelceted.substring(0, securityGroupSelceted.length-2);
		$("#securityGroupFilterDisp").show();
		$("#UserSecurityGroupFilter").html(securityGroupSelceted);
		isFilterAvail = true;
		$('#RemoveSecurityGroupFilter').tooltip();
	}
	else
		$("#securityGroupFilterDisp").hide();
	
	/*var issueDt = $('#securityIssueDateAccordion :selected').val();
	var dtFilter = new Array();
	 Create issue date filter array which contains the issue date which user selected. 
	if(issueDt != "" && issueDt != "-1"){
		 Create issue date filter. 
		 * field: should matched with the grid column index to filter
		 * op : is the filter option, 'eq' is equal
		 * data: the value of the dropdown list to be filtered
		 
		dtFilter[0] = {field:"issue_DATE","op":"eq","data":issueDt};
		//form filter selected view formation
		$('#issueDateFilterDisp').show();
		$('#UserIssueDateFilter').html($('#securityIssueDateAccordion :selected').text());
		isFilterAvail = true;
	}else{
		$('#issueDateFilterDisp').hide();
	}*/
	
	if(isFilterAvail)
		$('#FilterSelectedDiv').show();
	else
		$('#FilterSelectedDiv').hide();
	
	OnChangeGridSelect(filter, filter2, securityGroupFilter);//filterDate
}

/* Filter the grid with the filter array(s), passed to filter.*/
function OnChangeGridSelect (filterObjStatus, filterSource, securityGroupFilter) {
	var grid = jQuery("#security");
    
	/* Get the postdata of the intial grid */
    var postdata = grid.jqGrid('getGridParam', 'postData');
    
    /* Apply the filter(s) on the grid data.
     * groupOp: AND/ OR : are the rules to be applied on each filter type that has passed.
     * rules: the filters that passed to be applied
     * groups : groups of filters that belongs to same category of filter rules
     * 
     * Here, for example: issue date is applied with AND rules with the 'groups'. Hence, issue date(if selected) AND status(if selected) AND source(if selected)
     * The 'groups' contains status and source filter(s) and so on...
     * status filter(s) themselves are applied on each other on OR. Same as source.
    */
    $.extend(postdata, {
    	filters: JSON.stringify({
            groupOp: "AND",
            //rules: filterDate,
            groups: [{
                groupOp: "OR",
                rules: filterObjStatus
            },
            {
                groupOp: "OR",
                rules: filterSource
            },
            {
                groupOp: "OR",
                rules: securityGroupFilter
            }]
        })
    });    
    
    /*reload grid with filtered data*/
    grid.jqGrid('setGridParam', { search: true, postData: postdata });
    grid.trigger("reloadGrid", [{ page: 1}]);
}

/*Only accepts one type of filter field of each filter types. 
 * Example: There should be only one 'P' status, one 'N' status, one 'BR' source and so on.*/
function isUnique(val, arrayVals){
	for(var len = 0; len<arrayVals.length; len++){
		if(arrayVals[len] == val)
			return true;
	}
	return false;
}

/*While loading the grid, filter the grid according to the status passed*/
function loadGridForDashBoardFilter(statusFilterVal){
	$('.status').each(function() {
		if($(this).val() == statusFilterVal){
			$(this).prop('checked', true);
			commonFilterCall();
		}
	});
}
