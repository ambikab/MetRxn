function showImage() {
  var imgName = '1';
  var pathParams = "inputSQL=" + getImageSearch(imgName) + "&sessionId=" + getCookie('session');
  $.ajax({
  url: 'http://localhost:8080/MetRxn-service/services/image',
  dataType: 'json',
  type: 'POST',
  data: pathParams,
  success: function(result) {
	$("#imgDiv").html( " <img src= 'data:image/gif;base64, " +  result['image'] +  "'/>");
	$("#newImage").attr("src",result['image']);
    }
  });
}