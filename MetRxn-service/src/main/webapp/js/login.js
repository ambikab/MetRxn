var sessionId = '';
$("#loginBtn").click (function() {
	var pathParams = "userName=" + $("#userName").val() + "&userPassword=" + $("#userPassword").val();
	$.ajax({
		type: "POST",
		url: "http://localhost:8080/MetRxn-service/services/authenticate/login",
		data : pathParams, 
		dataType: "json",
		success: function(result){ 
			addAlert('success', 'You are logged in.');
		}
	});
});