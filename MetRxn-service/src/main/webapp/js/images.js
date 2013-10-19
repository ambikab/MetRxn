function showImage() {
  var imgName = '1';
  var pathParams = "inputSQL=" + getImageSearch(imgName);
  $.ajax({
  url: 'http://localhost:8080/MetRxn-service/services/image',
  dataType: 'text',
  type: 'POST',
  data: pathParams,
  success: function(data) {
	$("#imgDiv").html( " <img src= 'data:image/gif;base64, " +  data +  "'/>");
	$("#newImage").attr("src",data);
    }
  });
}

function getBase64Image(imgElem) {
// imgElem must be on the same server otherwise a cross-origin error will be thrown "SECURITY_ERR: DOM Exception 18"
    var canvas = document.createElement("canvas");
    canvas.width = imgElem.clientWidth;
    canvas.height = imgElem.clientHeight;
    var ctx = canvas.getContext("2d");
    ctx.drawImage(imgElem, 0, 0);
    var dataURL = canvas.toDataURL("image/png");
    return dataURL.replace(/^data:image\/(png|jpg);base64,/, "");
}

