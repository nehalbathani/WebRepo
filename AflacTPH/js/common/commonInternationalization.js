$(document).ready(function() {
	var language = getURLParameter("lang");
	//set the language to the dropdown from the url 
	if(language == "en" || language == "ja"){
		$('#chooseLanguage').val(language);
	}else{
		//alert($('#chooseLanguage').val());
		var url = document.URL + "?lang=en";
		window.location.href = url;
	}
	
	/*alert($('#local').val());
	if($('#local').val() != ""){
		$('#chooseLanguage').val($('#local').val());
	}*/
		
	//on change of language, reloade the url with the lang as parameter
	$('#chooseLanguage').change(function(){
		if($(this).val() != "-1"){
			var lang = getURLParameter("lang");
			var url = "";
			if(lang == "")
			{
				url = document.URL + "?lang=" + $(this).val();
			}else{
				var oldUrl = document.URL;
				url = replaceUrlParam(oldUrl, "lang", $(this).val());
			}
			window.location.href = url;
		}
	});
});


//Get the parameter from url
function getURLParameter(name) {
  return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search) || [, ""])[1].replace(/\+/g, '%20')) || null;
}

//replace param value in the url
function replaceUrlParam(url, paramName, paramValue){
 var pattern = new RegExp('('+paramName+'=).*?(&|$)') ;
 var newUrl = url.replace(pattern,'$1' + paramValue + '$2');
 if(newUrl == url){
     newUrl = newUrl + (newUrl.indexOf('?')>0 ? '&' : '?') + paramName + '=' + paramValue ;
 }
 return newUrl;
}