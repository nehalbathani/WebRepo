$(document).ready(function() {
	/* activate incoming filter Accordion*/
	$( "#FilterAccordion" ).accordion({
	      collapsible: true
	});		
});

var allStatus = new Array();
var allSources = new Array();
var allAcs = new Array();
var allSecGrp = new Array();
var allTradeDates = new Array();

function loadFilterValue(response){
	var statusHtml = "";
	var sourceHtml = "";
	var acHtml = "";
	var secGrpHtml = "";
	
	var tradeDtHtml = "<option value=''>--Select--</option>";
	
	/* dynamically add filter data to filter section from the outgoing grid data*/
	for(var jqlen = 0, uniqueStatLen = 0, uniqueSourceLen = 0, uniqueAcLen = 0,
			           uniqueSecGrpLen = 0, tradeDtLen = 0; 
	                   jqlen<response.length; jqlen++) {
		/* add status filters*/
		if (response[jqlen].status != "" && !isUnique(response[jqlen].tph_STATUS, allStatus)) {
    		statusHtml += "<div><input type='checkbox' name='" + 
    		              response[jqlen].tph_STATUS + 
    		              "' value='" + response[jqlen].tph_STATUS + 
    		              "' class='status'>&nbsp;" + 
    		              response[jqlen].tph_STATUS + "</input></div>";
    		allStatus[uniqueStatLen] = response[jqlen].tph_STATUS;
    		uniqueStatLen++;
    	}
		
		/* add source filters*/
		if (response[jqlen].source != "" && !isUnique(response[jqlen].source, allSources)) {
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
			allSources[uniqueSourceLen] = response[jqlen].source;
			uniqueSourceLen++;
    	}
		
		/* add account filters*/
		if (response[jqlen].acct_CD != "" && !isUnique(response[jqlen].acct_CD, allAcs)) {
			acHtml += "<div><input type='checkbox' name='" + 
			              response[jqlen].acct_CD + "' value='" + 
			              response[jqlen].acct_CD + 
			              "' class='account'>&nbsp;" + 
			              response[jqlen].acct_CD + "</input></div>";
			allAcs[uniqueAcLen] = response[jqlen].acct_CD;
			uniqueAcLen++;
    	}
		
		/* add security group filters*/
		if (response[jqlen].securityGroup != "" && !isUnique(response[jqlen].securityGroup, allSecGrp)) {
			secGrpHtml += "<div><input type='checkbox' name='" + 
			              response[jqlen].securityGroup + 
			              "' value='" + response[jqlen].securityGroup + 
			              "' class='secGrp'>&nbsp;" + response[jqlen].securityGroup + 
			              "</input></div>";
			allSecGrp[uniqueSecGrpLen] = response[jqlen].securityGroup;
			uniqueSecGrpLen++;
    	}
		
		/* add issued dates filter*/
		if (response[jqlen].tradeDate != "" && !isUnique(response[jqlen].tradeDate, allTradeDates)) {
			var date = formDate(response[jqlen].tradeDate);
			var dt = $.datepicker.formatDate('M, dd, yy', date);//Display date
			var dtVal = $.datepicker.formatDate('yy/mm/dd', date);//date value, for filtering
			tradeDtHtml += "<option value='"+dtVal+"' class='tradeDate'>"+dt+"</option>";
			allTradeDates[tradeDtLen] = response[jqlen].tradeDate;
			tradeDtLen++;
    	}
	}
	
	/* load the filter data to the corresponding UI*/
	$('#tradesStatusAccordion').html(statusHtml);
	$('#tradesSourceAccordion').html(sourceHtml);
	$('#tradesAcAccordion').html(acHtml);
	$('#tradesSecGrpAccordion').html(secGrpHtml);
	$('#tradesTradeDateAccordion').html(tradeDtHtml);
	
	/* If any status filter is checked */
	$('.status').click(function () {
		commonFilterCall();
	});
	
	/* If any source filter is checked */
	$('.source').click(function () {
		commonFilterCall();
	});
	
	/* If any account filter is checked */
	$('.account').click(function () {
		commonFilterCall();
	});
	
	/* If any security group filter is checked */
	$('.secGrp').click(function () {
		commonFilterCall();
	});
	
	/* If a trade date filter is selected */
	$('#tradesTradeDateAccordion').change(function(){
		commonFilterCall();
	});
	
	/* If an trade date filter is selected */
	$('#tradesTradeDateAccordion').change(function(){
		commonFilterCall();
	});
	
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
	
	/*remove all the account filter that user selected*/
	$('#RemoveAccountFilter').click(function(){
		$(".account").attr("checked", false);
		commonFilterCall();
	});
	
	/*remove all the securityGroup filter that user selected*/
	$('#RemoveSecurityGroupFilter').click(function(){
		$(".secGrp").attr("checked", false);
		commonFilterCall();
	});
	
	/*remove all the trade date filter that user selected*/
	$('#RemoveTradeDateFilter').click(function(){
		$('#tradesTradeDateAccordion').get(0).selectedIndex = 0;
		commonFilterCall();
	});
	
	/*On click reload icon of the grid, reset the filter*/
	$('.ui-icon-refresh').click(function(){
		//reset filters
		$(".status").attr("checked", false);
		$(".source").attr("checked", false);
		$(".account").attr("checked", false);
		$(".secGrp").attr("checked", false);
		$('#tradesTradeDateAccordion').get(0).selectedIndex = 0;
		$('#FilterSelectedDiv').hide();
	});
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
		var status = jqGridData[len].tph_STATUS;
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
	
	for(var len = 0, statLen = allStatus.length; len < jqGridData.length; len++) {
		var status = jqGridData[len].tph_STATUS;
		if (status != "" && !isUnique(status, allStatus)) {
			statusHtml += "<input type='checkbox' name='" + 
                          status + "' value='" + status + 
                          "' class='status'>&nbsp;" + 
                          status + "</input><br/>";
            allStatus[statLen++] = status;
		}
	}
	$('#tradesStatusAccordion').append(statusHtml);
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
	$('#tradesSourceAccordion').append(html);
}

//to add new account after resend/discard or refresh
function includeNewAccount(jqGridData) {
	var html = "";
	
	//remove if not present in the jqGridData
	var dataToRemove = new Array();
	var removeCount = 0;
	var dataToRemoveFlag = false;
	//get the data to remove
	for(var len = 0; len < jqGridData.length; len++) {
		var data = jqGridData[len].acct_CD;
		dataToRemoveFlag = false;
		for(var datalen = 0; datalen < allAcs.length; datalen++) {
			if(data == allAcs[datalen]){
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
		allAcs.splice(len,1);
	}
	
	for(var len = 0, statLen = allAcs.length; len < jqGridData.length; len++) {
		var acct_CD = jqGridData[len].acct_CD;
		if (acct_CD != "" && !isUnique(acct_CD, allAcs)) {
			html += "<input type='checkbox' name='" + 
			acct_CD + "' value='" + acct_CD + 
                          "' class='acct_CD'>&nbsp;" + 
                          acct_CD + "</input><br/>";
			allAcs[statLen++] = acct_CD;
		}
	}
	$('#tradesAcAccordion').append(html);
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
		var data = jqGridData[len].securityGroup;
		dataToRemoveFlag = false;
		for(var datalen = 0; datalen < allSecGrp.length; datalen++) {
			if(data == allSecGrp[datalen]){
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
		allSecGrp.splice(len,1);
	}
	
	for(var len = 0, statLen = allSecGrp.length; len < jqGridData.length; len++) {
		var securityGroup = jqGridData[len].securityGroup;
		if (securityGroup != "" && !isUnique(securityGroup, allSecGrp)) {
			html += "<input type='checkbox' name='" + 
			securityGroup + "' value='" + securityGroup + 
                          "' class='securityGroup'>&nbsp;" + 
                          securityGroup + "</input><br/>";
			allSecGrp[statLen++] = securityGroup;
		}
	}
	$('#tradesSecGrpAccordion').append(html);
}

//to add new trade date after resend/discard or refresh
function includeNewTradeDate(jqGridData) {
	var html = "";
	
	//remove if not present in the jqGridData
	var dataToRemove = new Array();
	var removeCount = 0;
	var dataToRemoveFlag = false;
	//get the data to remove
	for(var len = 0; len < jqGridData.length; len++) {
		var data = jqGridData[len].tradeDate;
		dataToRemoveFlag = false;
		for(var datalen = 0; datalen < allTradeDates.length; datalen++) {
			if(data == allTradeDates[datalen]){
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
		var dtVal = $.datepicker.formatDate('yy/mm/dd', new Date(dataToRemove[len]));
		var dateItem = "#tradesTradeDateAccordion option[value='"+dtVal+"']";
		$(dateItem).remove();
		//remove from array
		allTradeDates.splice(len,1);
	}
	
	for(var len = 0, statLen = allTradeDates.length; len < jqGridData.length; len++) {
		var dateVal = jqGridData[len].tradeDate;
		if (dateVal != "" && !isUnique(dateVal, allTradeDates)) {
			var dt = $.datepicker.formatDate('M, dd, yy', new Date(dateVal));//Display date
			var dtVal = $.datepicker.formatDate('yy/mm/dd', new Date(dateVal));//date value, for filtering
			html += "<option value='"+dtVal+"' class='tradeDate'>"+dt+"</option>";
			allTradeDates[statLen++] = dateVal;
    	}
	}
	$('#tradesTradeDateAccordion').append(html);
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
			statusArr.push($(this).val());
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
	
	var acCount=0;
	var accountFilter = "";
	var filter3 = new Array();
	/* Create source filter array which contains all the source filter of each type user selected. */
	$('.account').each(function() {
		if($(this).is(':checked')){
			/* Create source filter. 
			 * field: should matched with the grid column index to filter
			 * op : is the filter option, 'eq' is equal
			 * data: the value of the checkbox to be filtered
			 */
			filter3[acCount++] = {field:"acct_CD","op":"eq","data":$(this).val()};
			//form filter selected view formation
			accountFilter +=  $(this).val() + ", ";
		}
	});
	/*Show user filter selection to to UI*/
	if(accountFilter != ""){
		accountFilter = accountFilter.substring(0, accountFilter.length-2);
		$("#accountFilterDisp").show();
		$("#UserAccountFilter").html(accountFilter);
		isFilterAvail = true;
		$('#RemoveAccountFilter').tooltip();
	}
	else
		$("#accountFilterDisp").hide();
	
	var secGrpCount=0;
	var securityGroupSelceted = "";
	var filter4 = new Array();
	/* Create source filter array which contains all the source filter of each type user selected. */
	$('.secGrp').each(function() {
		if($(this).is(':checked')){
			/* Create source filter. 
			 * field: should matched with the grid column index to filter
			 * op : is the filter option, 'eq' is equal
			 * data: the value of the checkbox to be filtered
			 */
			secGrpArr.push($(this).val());
			filter4[secGrpCount++] = {field:"securityGroup","op":"eq","data":$(this).val()};
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
	
	var tradeDt = $('#tradesTradeDateAccordion :selected').val();
	var dtFilter = new Array();
	/* Create issue date filter array which contains the issue date which user selected. */
	if(tradeDt != "" && tradeDt != "-1"){//If a valid date is selected
		/* Create issue date filter. 
		 * field: should matched with the grid column index to filter
		 * op : is the filter option, 'eq' is equal
		 * data: the value of the dropdown list to be filtered
		 */
		dtFilter[0] = {field:"tradeDate","op":"eq","data":tradeDt};
		//form filter selected view formation
		$('#tradeDateFilterDisp').show();
		$('#UserTradeDateFilter').html($('#tradesTradeDateAccordion :selected').text());
		isFilterAvail = true;
		$('#RemoveTradeDateFilter').tooltip();
	}else{
		$('#tradeDateFilterDisp').hide();
	}
	
	if(isFilterAvail)
		$('#FilterSelectedDiv').show();
	else
		$('#FilterSelectedDiv').hide();
	
	OnChangeGridSelect(filter, filter2, filter3, filter4, dtFilter);
}

/* Filter the grid with the filter array(s), passed to filter.*/
function OnChangeGridSelect (filterObjStatus, filterSource, filterAc, filterSecGrp, filterDate) {
	var grid = jQuery("#trades");
    
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
            rules: filterDate,
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
                rules: filterAc
            },
            {
                groupOp: "OR",
                rules: filterSecGrp
            }]
        })
    });    
    
    /*reload grid with filtered data*/
    grid.jqGrid('setGridParam', { search: true, postData: postdata });
    grid.trigger("reloadGrid", [{ page: 1}]);
    enableDisableExpManBtn();
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