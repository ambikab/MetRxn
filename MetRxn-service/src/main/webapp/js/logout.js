$(document).ready(function(){ 
$("#logOutBtn").live('click', function(event) {
	event.preventDefault();
	var sId = getCookie("session");
	var pathParams = "sessionId=" + sId;
	$.ajax({
		type: "POST",
		url: "http://localhost:8080/MetRxn-service/services/authenticate/logOut",
		data : pathParams,
		dataType: "json",
		success: function(result){ 
			eraseCookie('session');
			$("#sessionMenu").html('<a href="/MetRxn-service/login.html">Log in <i class="icon-user"></i></a>');
			addAlert(result.Status.toLowerCase(), result.Result);
		}
	});
	
	function eraseCookie(name) {
		setCookie(name,null,-1);
	}
});
});