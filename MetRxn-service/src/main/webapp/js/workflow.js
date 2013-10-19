function uploadFile() {
var formdata = new FormData();
	var entityType = document.getElementById("entityType").value;
	var fileType = document.getElementById("fileType").value;
	var fileContent = document.getElementById("fileContent").files[0];
formdata.append("file", fileContent);
	formdata.append("entityType", entityType);
	formdata.append("fileType", fileType);
$.ajax({
        url: 'http://localhost:8080/MetRxn-service/services/entity/uploader',  //Server script to process data
        type: 'POST',
        data: formdata,
		processData: false,
        contentType: false,
        success: function(data) {
			console.log(data);
    }
  });
}