function getCookie(c_name) {
	var c_value = document.cookie;
	var c_start = c_value.indexOf(" " + c_name + "=");
	if (c_start == -1)
	{
	c_start = c_value.indexOf(c_name + "=");
	}
	if (c_start == -1)
	{
	c_value = null;
	}
	else
	{
	c_start = c_value.indexOf("=", c_start) + 1;
	var c_end = c_value.indexOf(";", c_start);
	if (c_end == -1)
	{
		c_end = c_value.length;
	}
	c_value = unescape(c_value.substring(c_start,c_end));
	}
	return c_value;
}


function setCookie(c_name,value,exdays)
{
	var exdate=new Date();
	exdate.setDate(exdate.getDate() + exdays);
	var c_value=escape(value) + ((exdays==null) ? "" : "; expires="+exdate.toUTCString());
	document.cookie=c_name + "=" + c_value;
}

$(document).ready(function(){ 
	if ((getCookie('session') != null)) {
		$("#sessionMenu").html('<a href = "#" id = "logOutBtn">Log Out <i class="icon-user"></i> </a>');
	} else {
		$("#sessionMenu").html('<a href="/MetRxn-service/login.html">Log in <i class="icon-user"></i></a>');
	}
});