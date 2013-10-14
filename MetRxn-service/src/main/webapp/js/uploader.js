/* on completion of step one */
$("#step1").click (function() { 
	$("#stepOneContents").hide();
	$("#step1").removeClass("active");
	$("#step2").addClass("active");
	$("#stepTwoContents").show();
});