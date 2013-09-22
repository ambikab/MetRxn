reqPgNo = 0;
next = 0;
sortOrder = 'ASC';
sortCol =  'empId';
searchVal = '';

function chooseSortOrder(currentOrder) {
	if (currentOrder === 'ASC')
		return 'DESC';
	else 
		return 'ASC';
}

function chooseSortArrows(order) {
	if (order === 'ASC')
		return 'icon-arrow-down';
	else 
		return 'icon-arrow-up';
}

$("#empId").click(function() {
	sortOrder = 'ASC';
	sortCol =  'empId';
	fetchJSONResults(reqPgNo, sortCol, sortOrder);
});

function placeSortArrows(colName, arrowLabel) {
	$(".sortImage").html('');
	$("#" + colName + "Sort").html("<i class = 'sortActive " + arrowLabel + "'>");
}

$("#empName").click(function() {
	if (sortCol == 'empName') {
		sortOrder = chooseSortOrder(sortOrder);
	} else {
		sortCol = 'empName';
		sortOrder = 'ASC';
	}	
	placeSortArrows(sortCol, chooseSortArrows(sortOrder));
	fetchJSONResults(reqPgNo, sortCol, sortOrder);
});

$("#searchBtn").click (function() {
	if ($("#appendedInputButtons").val().length == 0) {
		addAlert("Warning", "Search string cannot be empty!!")
		//alert("Search string cannot be empty!");
		return 0;
	}
	searchVal = $("#appendedInputButtons").val();
	fetchJSONResults(1, 'empId', 'ASC');
});

$("#prev").click (function() {
	fetchJSONResults(reqPgNo - 1, sortCol, sortOrder);
});

$("#next").click (function() {
	fetchJSONResults(reqPgNo + 1, sortCol, sortOrder);
});

function fetchJSONResults (requestedPageNumber, sortCol, sortOrder) {
	var pathParams = "/" + requestedPageNumber + "/" + sortCol + "/" + sortOrder + "/" + searchVal; 
	$.ajax({
		url:"http://localhost:8080/MetRxn-service/services/employees/paginated" + pathParams,
		dataType : "json",
		success:function(result){
			$("#resultsTable tbody").empty();
			resultsMode();
			if ( result.employees == null ) {
				var rowData = "<tr><td colspan = '4'>No results matched your search!! </td></tr>";
				$("#resultsTable tbody").append(rowData);
			}
			var collection = result.employees.employees;
			var currentPage = parseInt(result.currentPage);
			reqPgNo = currentPage;
			var total = parseInt(result.totalRecords);
			var recordsPerPage = 5; //TO be returned from the back end as a part of the dto.
			/** calculation for next and previous button values **/
			var totalPages =0;
			if (total % recordsPerPage == 0)
				totalPages = total / recordsPerPage;
			else
				totalPages = total / recordsPerPage + 1;
			if (currentPage + 1 <= totalPages) {
				$("#next").show(); //TODO : append to dom 
			} 
			if (currentPage - 1 >= 1)
				$("#prev").show();
			var result = 0;
			$("#currentpageNumber").text(currentPage);
			$.each(collection, function(employee) {
				result = 1;
				console.log(collection[employee]);
				var rowData = "<tr> <td> " + collection[employee].id + "</td> <td>" + collection[employee].name + "</td> </tr>";
				$("#resultsTable tbody").append(rowData);
			});				
		}
	});
}


function resultsMode() {
	$("#homeContents").hide();
	$("#searchResults").show();
	$("#next").hide(); //TODO : Remove from DOM
	$("#prev").hide(); //TODO : Remove from DOM
}

function addAlert(alertType, alertMsg) {
	//TODO : add alert types based on the type of error messages!!
	var errorType = "<div class = 'alert'>;
	var htmlAlertMsg = "<button type='button' class='close' data-dismiss='alert'>&times;</button> <strong> " 
		+ alertType + "!</strong> "+ alertMsg 
		+ "</div>";		
	$(".messages").html(htmlAlertMsg);

}