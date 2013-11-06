var workFlowId, colName;
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
		dataType : 'json',
		processData: false,
		contentType: false,
		async: false,
        success: function(data) {
			workFlowId = data.id;
			var mapping = data.mapper;
			var entity = data.entityName;
			hideStepOne(); //displays the table.
			var tHeadBegin = "<thead> <tr> ";
			var tHeadEnd = "</tr> </thead>";
			var tHead = " <th colspan = '2'> - </th>";
			var trArr = [];
			var colName = 0;
			//var jsonString = $.parseJSON(mapping);
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
		},
		 error: function(jqXHR, textStatus, errorThrown)
        {
        	console.log('ERRORS: ' + textStatus);
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

/* function updateMappings(cellObject) {
	var col = $(cellObject).closest("td").index();
	var row = $(cellObject).closest("tr").index();
	col = col + 1;
	row = row + 1;
	$(cellObject).removeClass("nonZeroClass");
	$(cellObject).addClass("update");
	var fileName = $('tr:nth-child(' +row+ ') > td:nth-child(1)').text();
	var dbName = $('tr:nth-child('+ 1 +') > th:nth-child(' +col + ')').text();
	jsonarr[dbName] = $.trim(fileName);
	$.each(jsonarr, function(key,value) {
		console.log ("key is : " + key + value);
	});
} */

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
		dataType: "json"
	})
	return false;
}
function saveMapping() {
var jsonString = (JSON.stringify(jsonarr));
var pathParams = "updatedMapping=" + jsonString + "&sessionId=" + workFlowId;
$.ajax({
        url: 'http://localhost:8080/MetRxn-service/services/entity/uploader/mapper',  //Server script to process data
        type: 'POST',
        data: pathParams,
		dataType: "json"
	}).complete(function( ) {
		//console.log("mappings updated!!");		
			$.ajax({
				url: 'http://localhost:8080/MetRxn-service/services/entity/data/migrate',  //Server script to process data
				type: 'POST',
				data: "workFlowId=" + workFlowId,
				success: function() {
					console.log("mappings updated!!");		
				}
			});
    console.log("nested function");
  });
}