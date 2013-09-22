function addAlert(alertType, alertMsg) {
	var errorType = "<div class = 'alert'>";
	var htmlAlertMsg = "<button type='button' class='close' data-dismiss='alert'>&times;</button> <strong> " 
		+ alertType + "!</strong> "+ alertMsg 
		+ "</div>";
	$(".messages").html(htmlAlertMsg);
}