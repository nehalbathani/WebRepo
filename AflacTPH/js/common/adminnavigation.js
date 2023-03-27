var local;
$(document).ready(function() {
	setupConsole();
	manageNav();	
	createAdminBreadcrumb();
	local = wwnaviGetLang();
});

//Get local from browser
function wwnaviGetLang(){
    return (navigator.userLanguage||navigator.browserLanguage||navigator.language);//.substr(0,2);
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

function manageNav() {
	// Only setting Navigation value for Trades and Security
	var selectedView = $('#selectedView').val();
	var selectedSystem = $('#selectedSystem').val();
	//alert(selectedView);
	
	$('#view_nav ul:first li').each(function() {
		$(this).removeClass('admin-active');
		$('a', this).removeClass('admin-active');
	});
	
	$('#view_nav #' + selectedView).addClass('admin-active');
	$('a', '#view_nav #' + selectedView).addClass('admin-active');
	$('#system_nav #' + selectedSystem).addClass('admin-active');
}

function createAdminBreadcrumb() {
	var activeView = $('#view_nav ul:first li.admin-active a.admin-active');
	var activeViewSpan = $('#view_nav ul:first li.admin-active a.admin-active span');
	var activeSystem = $('#system_nav ul:first li.admin-active a');
	//console.log("Active view:" + activeView.html());
	//console.log("Active System:" + activeSystem.html());

	var olist1 = $("<li><a href='adminFTP.do'>Home</a></li>");
	var olist2 = $("<li><a href='" + activeView.attr("href") + "'>"
			+ activeViewSpan.html().trim() + "</a></li>");
	var olist3 = $("<li><a href='" + activeSystem.attr("href") + "'>"
			+ activeSystem.html() + "</a></li>");
	olist1.hide();
	olist2.hide();
	olist3.hide();
	$('#admin_page_breadcrumb').append(olist1).append(olist2);
	olist1.fadeIn();
	olist2.fadeIn();
	olist3.fadeIn();

	$('#admin_page_breadcrumb li:last').addClass('active');
}
