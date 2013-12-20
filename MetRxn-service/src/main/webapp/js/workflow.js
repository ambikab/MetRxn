var workFlowId, colName;

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

function uploadFile() {
	var formdata = new FormData();
	var entityType = $("#entityType").val();
	var fileType = $("#fileType").val();
	var fileContent = document.getElementById("fileContent").files[0];
	formdata.append("file", fileContent);
	formdata.append("entityType", entityType);
	formdata.append("fileType", fileType);
	formdata.append("sessionId", getCookie("session"));
	$.ajax({
		url: 'http://localhost:8080/MetRxn-service/services/entity/uploader/' + fileType,  //Server script to process data
		type: 'POST',
		data: formdata,
		dataType : 'json',
		processData: false,
		contentType: false,
		context: this,
		async: false,
		success: function(data) {
			if (fileType == 'sbml') {
				workFlowId = data.workflowId;
				$("#metaboliteCnt").text(data.speciesCnt);
				$("#compartmentCnt").text(data.compartmentCnt);
				$("#rxnCnt").text(data.rxnCnt);
				$( "#dialog-confirm" ).dialog( "open" );
			} else {
				workFlowId = data.id;
				var mapping = data.mapper;
				var entity = data.entityName;
				hideStepOne(); //displays the table.
				var tHeadBegin = "<thead> <tr> ";
				var tHeadEnd = "</tr> </thead>";
				var tHead = " <th colspan = '2'> - </th>";
				var trArr = [];
				var colName = 0;
				var rowCol;
				$.each(mapping, function(tableColumn) {
					rowCol = tableColumn;
					tHead = tHead + " <th> " + tableColumn + "</th>";
					var mapResult = mapping[tableColumn];
					$.each(mapResult, function(index) {
						var object = mapResult[index];
						$.each(object, function(key, value) {
							if ((colName === 0) && (key === "colName")){
								trArr[index] = "<td><strong>" + value + "</strong></td><td><button class = 'metaData'><i class = 'icon-tasks'></i></button></td>"
							} else if (key === "count") {
								if (value > 0) {
									style = "class = 'nonZeroClass clickable'";
								} else {
									style = "class = 'clickable'";
								}
								trArr[index] =  trArr[index] + "<td " + style + ">" + value + "</td>";
							}
						});

					});
					colName = 1;
				});
				var htmlVal = tHeadBegin + tHead + tHeadEnd;
				$.each(trArr, function(key,value) {
					htmlVal = htmlVal + "<tr>" + value + "</tr>";
				});
				$("#resultsTableOne").html(htmlVal);
			}

		},
		error: function(jqXHR, textStatus, errorThrown)
		{
			addAlert('error' , textStatus);
		}
	});
	return false;
}

/* on completion of step one */
function hideStepOne() { 
	$("#stepOneContents").hide();
	$("#step1").removeClass("active");
	$("#step2").addClass("active");
	$("#stepTwoContents").show();
}

var jsonarr = {};
$(document).ready(function(){
	$("#fileType").change(function() {
		if ($("#fileType").val() === 'sbml') {
			$("#entityType").hide();
		} else {
			$("#entityType").show();
		}
	});
	$("#dialog-confirm" ).dialog({
					autoOpen: false,
					resizable: true,
					height:250,
					width: 500,
					modal: true,
					buttons: {
						"Submit": function() {
						$.ajax({
							url: 'http://localhost:8080/MetRxn-service/services/entity/uploader/mapper/sbml',  //Server script to process data
							type: 'POST',
							data: "workflowId=" + workFlowId,
							context:this,
							success: function() {
								$( this ).dialog( "close" );	
								addAlert('success','SBML contents saved to the database.');
								var pathParams = "pageNumber=1" + "&sortCol=workflowId" + "&sortOrder=ASC" + "&queryString=" + getSBMLData(workFlowId); 
								$.ajax({
									url: 'http://localhost:8080/MetRxn-service/services/queries/results',  //Server script to process data
									type: 'POST',
									data: pathParams,
									success: function(result) {
										$("#stepTwoContentsSBML").show();
										var index = 0;
										$.each(result.resultSet, function(index, object) {
											var html = "<tr>";
											var header = "<thead><tr>";
											$.each(object, function(key, value) {
												if(index === 0) {
													header  = header + "<th>" + key.toUpperCase() + "</th>";
												}
												html = html + "<td>" + value + "</td>";
											});
											html = html + "</tr>";
											index = 1;
											$("#SBMLResultsTable").append(header + "</tr></thead>");
											$("#SBMLResultsTable").append(html);
										});
									}
								});
							},
							error: function(jqXHR, textStatus, errorThrown)
							{
								addAlert('error' , textStatus);
							}
						});
					},
					Cancel: function() {
						addAlert("info","File content saving has been cancelled by the user!");
						$( this ).dialog( "close" );
					}
					}
	});
	

	$(".clickable").live('click', function() {
		var col = $(this).closest("td").index();
		var row = $(this).closest("tr").index();
		col = col + 1;
		row = row + 1;
		$(this).removeClass("nonZeroClass");
		$(this).removeClass("clickable");
		$(this).addClass("update");
		var fileName = $('#resultsTableOne tr:nth-child(' +row+ ') > td:nth-child(1)').text();
		var dbName = $('#resultsTableOne tr:nth-child('+ 1 +') > th:nth-child(' +col + ')').text();
		jsonarr[dbName] = $.trim(fileName);
	});

	$(".metaData").live('click',function() {
		var row = $(this).closest("tr").index();
		row = row + 1;
		colName = $('#resultsTableOne tr:nth-child(' +row+ ') > td:nth-child(1)').text();
		colName = $.trim(colName);
		var sortCol = "columnData";
		var pathParams = "pageNumber=1" + "&sortCol=" + sortCol + "&sortOrder=" + sortOrder + "&queryString=" + getFileColumnData(workFlowId,colName); 
		$.ajax({
			type: "POST",
			url: "http://localhost:8080/MetRxn-service/services/queries/results",
			data : pathParams, 
			dataType: "json",
			success: function(result){
				$.each(result.resultSet, function(index, object) {
					$.each(object, function(key, value) {
						$("#fileColumnContents").text(value);
					});
				});
			}, 
			error: function(jqXHR, textStatus, errorThrown) {
			addAlert('error' , textStatus);
			}
		});
		/* Render the modal */
		$("#modalForm").dialog({
			autoOpen: false,
			modal: true,
			title: "Meta information",
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
		$('#modalForm').dialog('open');
	}); 

	$(".update").live('click', function() { 
		var col = $(this).closest("td").index();
		var row = $(this).closest("tr").index();
		col = col + 1;
		row = row + 1;
		var dbName = $('#resultsTableOne tr:nth-child('+ 1 +') > th:nth-child(' +col + ')').text();
		$(this).removeClass("update");
		if ($(this).text() === 0 )
			$(this).addClass("nonZeroClass");
		$(this).addClass("clickable");
		delete jsonarr[dbName];
	});
});

function updateMetaInformation() {
	var metaInformation = {};
	metaInformation['PP'] = $("#PP").val();
	metaInformation['PPSource'] = $("#PPSource").val();
	metaInformation['PPpH'] = $("#PPpH").val();
	metaInformation['externalIdSource'] = $("#externalIdSource").val();
	metaInformation['splitSymbol'] = $("#splitSymbol").val();
	var metaInfoJson = (JSON.stringify(metaInformation));
	var pathParams = "metaData=" + metaInfoJson + "&workflowId=" + workFlowId + "&columnName=" + colName;
	$.ajax({
		url: 'http://localhost:8080/MetRxn-service/services/entity/uploader/mapper/metaData',
		type: 'POST',
		data: pathParams,
		dataType: "json",
		success: function(data) {
			addAlert("success","Mappings updated sucessfully.");
		}
	})
	return false;
}
function saveMapping() {
	var jsonString = (JSON.stringify(jsonarr));
	var pathParams = "updatedMapping=" + jsonString + "&workflowId=" + workFlowId + "&sessionId=" + getCookie('session');
	$.ajax({
		url: 'http://localhost:8080/MetRxn-service/services/entity/uploader/mapper',  //Server script to process data
		type: 'POST',
		data: pathParams,
		dataType: "json"
	}).complete(function( ) {		
		$.ajax({
			url: 'http://localhost:8080/MetRxn-service/services/entity/data/migrate',  //Server script to process data
			type: 'POST',
			data: "workFlowId=" + workFlowId + "&sessionId=" + getCookie('session'),
			success: function() {
				addAlert('success','mappings updated!!');
			}
		});
	});
}

    