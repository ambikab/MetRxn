var sessionId = '';
$("#loginBtn").click (function() {
	var pathParams = "userName=" + $("#userName").val() + "&userPassword=" + $("#userPassword").val();
	$.ajax({
		type: "POST",
		url: "http://localhost:8080/MetRxn-service/services/authenticate/login",
		data : pathParams, 
		dataType: "json",
		success: function(result){ 
			if(result.active == true) {
				addAlert('success', 'You are logged in.');
				setCookie('session',result.sessionId);
			} else {
				addAlert('error', 'User name / password is incorrect!!');
			}			
		}
	});
});

$("#signUpBtn").click (function() {
	var pathParams = "userName=" + $("#userName").val() + "&userPassword=" + $("#userPassword").val();
	$.ajax({
		type: "POST",
		url: "http://localhost:8080/MetRxn-service/services/authenticate/user",
		data : pathParams,
		dataType: "json",
		success: function(result){ 
			addAlert(result.status.toLowerCase(), result.Result);
		}
	});
});