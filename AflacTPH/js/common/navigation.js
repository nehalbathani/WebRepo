$(document).ready(function() {
	setupConsole();
	manageNav();
	createBreadcrumb();
	setStatusIndicator();
	setInterval(setStatusIndicator, 60000);
	//heartbeat();
	//setInterval(heartbeat, 60000);

	//setInterval(desktopNotification,10000);
});

/*
function desktopNotification(){
	var havePermission = window.webkitNotifications.checkPermission();
	  if (havePermission == 0) {
	    // 0 is PERMISSION_ALLOWED
	    var notification = window.webkitNotifications.createNotification(
	      'http://i.stack.imgur.com/dmHl0.png',
	      'Chrome notification!',
	      'Here is the notification text'
	    );
	   
	    notification.onclick = function () {
	      window.open("http://stackoverflow.com/a/13328397/1269037");
	      notification.close();
	    }; 
	    notification.show();
	  } else {
	      window.webkitNotifications.requestPermission();
	  }
	
}
*/
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

var tradesExtMngNm = "Incoming Trades";
var securityExtMngNm = "Incoming Security";

function manageNav() {
	// Only setting Navigation value for Trades and Security
	var selectedView = $('#selectedView').val();
	var selectedSystem = $('#selectedSystem').val();
	var serviceType_radio = "serviceType="
			+ $('input:radio[name=serviceType_radio]:checked').val();
	//console.log("Navigation: serviceType_radio:" + serviceType_radio);

	// apply css to header and navigation
	$('#view_nav ul:first li').each(function() {
		$(this).removeClass('active');
		$('a', this).removeClass('active');
	});

	$('#system_nav ul:first li').each(function() {
		$(this).removeClass('active');
	});

	$('#view_nav #' + selectedView).addClass('active');
	$('a', '#view_nav #' + selectedView).addClass('active');
	$('#system_nav #' + selectedSystem).addClass('active');

	if (selectedView != "DASHBOARD") {
		// assign href value to navigation
		$('#system_nav ul:first li').each(
				function() {
					system = this.id;
					if (this.id == 'EXT_MNG') {
						/*console.log("Navigation: Link URL:"
								+ $('#selectedView').val() + "_" + system
								+ ".do");*/
						$('a', this).each(
								function() {
									if (selectedView == "TRADE") {
										$('span',this).text(tradesExtMngNm);
									} else {
										$('span',this).text(securityExtMngNm);
									}									
									$(this).attr(
											"href",
											$('#selectedView').val() + "_"
													+ system + ".do");
								});
					} else {
						/*console.log("Navigation: Link URL:"
								+ $('#selectedView').val()
								+ "_OUTGOING.do?dest=" + system);*/
						$('a', this).each(
								function() {
									$(this).attr(
											"href",
											$('#selectedView').val()
													+ "_OUTGOING.do?dest="
													+ system);
								});
					}
				});
	} else {
		// assign href value to navigation
		$('#system_nav ul:first li').each(
				function() {
					system = this.id;
					if (this.id == 'EXT_MNG') {
						/*console.log("Navigation: Link URL:"
								+ $('#selectedView').val() + ".do" + "?"
								+ serviceType_radio);*/
						$('a', this).each(
								function() {
									$(this).attr(
											"href",
											$('#selectedView').val() + ".do"
													+ "?" + serviceType_radio);
								});
					} else {
						/*console.log("Navigation: Link URL:"
								+ $('#selectedView').val() + ".do" + "?dest="
								+ system + "&" + serviceType_radio);*/
						$('a', this).each(
								function() {
									$(this).attr(
											"href",
											$('#selectedView').val() + ".do"
													+ "?dest=" + system + "&"
													+ serviceType_radio);
								});
					}
				});
	}
}

function createBreadcrumb() {
	var activeView = $('#view_nav ul:first li.active a.active');
	var activeViewSpan = $('#view_nav ul:first li.active a.active span');
	var activeSystemSpan = $('#system_nav ul:first li.active a span');
	var activeSystem = $('#system_nav ul:first li.active a');
	//console.log("Active view:" + activeView.html());
	//console.log("Active System:" + activeSystem.html());

	var olist1 = $("<li><a href='DASHBOARD.do'>Home</a></li>");
	var olist2 = $("<li><a href='" + activeView.attr("href") + "'>"
			+ activeViewSpan.html().trim() + "</a></li>");
	var olist3 = $("<li><a href='" + activeSystem.attr("href") + "'>"
			+ activeSystemSpan.html() + "</a></li>");
	olist1.hide();
	olist2.hide();
	olist3.hide();
	$('#page_breadcrumb').append(olist1).append(olist2).append(olist3);
	olist1.fadeIn();
	olist2.fadeIn();
	olist3.fadeIn();

	$('#page_breadcrumb li:last').addClass('active');
}


function setStatusIndicator(){
	
	$('#system_nav ul:first li').each(function(index, element) {
		var systemName=$(this).attr('id');
		$(".statusIndicator",this).each(function(index, element){
			var cntType= $(this).attr('id');
			$.ajax({
				url : "/tph/getStatusForIndicator.do?system_name=" + (systemName=='EXT_MNG'?'IN':systemName)+"&count_type="+cntType+"&r="+Math.random(),
				type : 'GET',
				async : true,
				dataType : "json",
				success : function(data) {
					$('#'+systemName+' #'+cntType).removeClass();
					$('#'+systemName+' #'+cntType).addClass('statusIndicator');
					
                    if(data==0){
                    	$('#'+systemName+' #'+cntType).addClass('red');
                    }
                    else if(data==1){
                    	$('#'+systemName+' #'+cntType).addClass('orange');
                	}
                    else if(data==2){
                    	$('#'+systemName+' #'+cntType).addClass('blue');
                    }
                    else if(data==3){
                    	$('#'+systemName+' #'+cntType).addClass('green');
                    }
                    else{
                    	
                    }
                        
				}
			});
		});
	});
};


/*
var deadCounters = [ 0, 0, 0, 0 ];

function heartbeat() {
	// Get all system elements
	// For each system element, do the following
	// Get the system name, and make the ajax call to get its status
	// At a periodic interval of 1 minute
	// Update the Navigation bar
	$('#system_nav ul:first li a').each(function(index, element) {
		var systemName = element.innerHTML.replace(" ", "");
		var ping_systemName = systemName + "_PING";
		// $(this).addClass("system-dead");
		var currentElem = $(this);
		if (systemName != "Incoming" && 
			systemName != "IncomingTrades" &&
			systemName != "IncomingSecurity") {
			console.log("system-name-inside:" + systemName);
			$.ajax({
				url : "/tph/HeartBeat.do?system_name=" + ping_systemName+"&r="+Math.random(),
				type : 'GET',
				async : true,
				dataType : "json",
				success : function(data) {
					// console.log($(currentElem).css());
					if (data == 0) {
						var deadCounter = deadCounters[index];
						deadCounter++;
						deadCounters[index] = deadCounter;
						$(currentElem).removeClass("system-alive");
						$(currentElem).removeClass("system-standby");
						$(currentElem).addClass("system-dead");
						/*
						 * if(deadCounter < 4) {
						 * $(currentElem).removeClass("system-alive");
						 * $(currentElem).removeClass("system-dead");
						 * $(currentElem).addClass("system-standby"); } else
						 * if(deadCounter > 4) {
						 * $(currentElem).removeClass("system-alive");
						 * $(currentElem).removeClass("system-standby");
						 * $(currentElem).addClass("system-dead"); }
						 */
						// Increment deadCounter by 1
						// If deadCounter == 3, turn the system orange
						// If DeadCounter >=5, turn the system red.
			/*		} else if (data == 1) {
						// Turn the system orange
						deadCounters[index] = 0;
						$(currentElem).removeClass("system-dead");
						$(currentElem).removeClass("system-alive");
						$(currentElem).addClass("system-standby");
					}else if (data == 2) {
						// Turn the system green
						deadCounters[index] = 0;
						$(currentElem).removeClass("system-dead");
						$(currentElem).removeClass("system-standby");
						$(currentElem).addClass("system-alive");
					}
				}
			});
		}
	});
}*/