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
	if (sortCol == 'empId') {
		sortOrder = chooseSortOrder(sortOrder);
	} else {
		sortCol = 'empId';
		sortOrder = 'ASC';
	}	
	placeSortArrows(sortCol, chooseSortArrows(sortOrder));
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




$("#prev").click (function() {
	fetchJSONResults(reqPgNo - 1, sortCol, sortOrder);
});

$("#next").click (function() {
	fetchJSONResults(reqPgNo + 1, sortCol, sortOrder);
});

function resultsMode() {
	$("#homeContents").hide();
	$("#searchResults").show();
	$("#next").hide(); //TODO : Remove from DOM
	$("#prev").hide(); //TODO : Remove from DOM
}