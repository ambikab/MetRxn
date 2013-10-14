function showImage(imageName) {
	$("#displayImage").html("<img src='http://localhost:8080/MetRxn-service/services/image/" + imageName +"' />");
	/* Modal properties on info button click*/
	$("#displayImage").dialog({
		autoOpen: false,
		modal: true,
		title: imageName,
		maxHeight: 600,
		minHeight: 200,
		resizable: false,
		draggable: false,
		width:650,
		buttons: {
			'Close': function () {
				$(this).dialog("close");
			}
		}
	});
	/*Render the modal*/
	$('#displayImage').dialog('open');
}