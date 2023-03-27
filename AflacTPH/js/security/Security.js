/*Get the json data form the uri and load in the modal to display each row details*/
function getSecurityDetails(id, uri){

	var pager_id='security-detail-pager';
	$("#secdetail_grid_container").html("<table id='secdetail_grid' class='scroll'></table><div id='"+pager_id+"' class='scroll'></div>");
	
	jQuery("#secdetail_grid").jqGrid({
		url:uri,
		datatype: "json",
		loadonce: true,
		colNames: ['Field', 'Value'],
		colModel: [
			{name:"tagName",index:"tagName",autowidth:true},
			{name:"tagValue",index:"tagValue",autowidth:true},
		],
		sortable:false,
	    height: '100%',
	    width:'100%',
	    viewrecords : true,
	    rowNum : 20,
		rowList : [  20, 30, 40 ],
		pager : "#"+pager_id ,
	    caption:"Security Details",
	    	loadComplete: function(data) {
				var $this = $(this),
		        datatype = $this.getGridParam('datatype');
			    if (datatype === "xml" || datatype === "json") {
			    	if(data[0].errorCode != undefined && data[0].errorCode != ""){
			    		$("#secdetail_grid_container").hide();
			    	}else{
			    		$("#secdetail_grid_container").show();
			    	}
			    	
			    	}
	    	}
	});
	jQuery("#secdetail_grid").jqGrid('navGrid', "#"+pager_id, {
		edit : false,
		add : false,
		del : false,
		search: false
	});	

}

function showSecurityAttributes(urlStrSecattrGrid){
	/*******   Display Security Attribute Grid  ***********/
	var pager_id='security-attribute-pager';
	var secattr_grid_id = "secattr_subgrid";
	$("#secattr_grid_container").html("<table id='"+secattr_grid_id+"' class='scroll'></table><div id='"+pager_id+"' class='scroll'></div>");

	 	jQuery("#"+secattr_grid_id).jqGrid({
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
		    height: '100%',
		    width:'100%',
		    viewrecords : true,
		    rowNum : 10,
			rowList : [ 10, 20, 30 ],
			pager : "#"+pager_id ,
		    caption:"Security Attributes",
		    	loadComplete: function(data) {
					var $this = $(this),
			        datatype = $this.getGridParam('datatype');
				    if (datatype === "xml" || datatype === "json") {
				    	if(data[0].errorCode != undefined && data[0].errorCode != ""){
				    		$("#secattr_grid_container").hide();
				    	}else{
				    		$("#secattr_grid_container").show();
				    	}
				    	
				    	}
		    	}
		});
    	jQuery("#"+secattr_grid_id).jqGrid('navGrid', "#"+pager_id, {
			edit : false,
			add : false,
			del : false
		});	
    
}