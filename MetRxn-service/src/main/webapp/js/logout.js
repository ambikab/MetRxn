$("#logOutBtn").click (function() {
	var pathParams = "sessionId=" + getCookie("session");
	$.ajax({
		type: "POST",
		url: "http://localhost:8080/MetRxn-service/services/authenticate/logOut",
		data : pathParams,
		dataType: "json",
		success: function(result){ 
			addAlert(result.status.toLowerCase(), result.Result);
		}
	});
});