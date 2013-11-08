reqPgNoOne = 0;
reqPgNoTwo = 0;
reqPgNoThree = 0;
next = 0;
sortOrder = 'ASC';
sortCol =  'empId';
searchVal = '';
sortOrderOne = 'ASC';
sortOrderTwo = 'ASC';
sortOrderThree = 'ASC';
currentSortValue = '';

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

$("#column1").click(function() {
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

/**
 * binding pagination elements with the result fetching ajax calls.
 */
$("#prevOne").click (function() {
	reqPgNoOne = reqPgNoOne - 1;
	fetchJSONResults("One", getSearchResults1(searchVal, 'test'),reqPgNoOne, "source", sortOrder);
	return false;
});

$("#nextOne").click (function() {
	reqPgNoOne = reqPgNoOne + 1;
	fetchJSONResults("One", getSearchResults1(searchVal, 'test'),reqPgNoOne, "source", sortOrder);
	return false;
});

$("#prevTwo").click (function() {
	reqPgNoTwo = reqPgNoTwo - 1;
	fetchJSONResults("Two", getSearchResults2(searchVal, 'test'),reqPgNoTwo, "source", sortOrder);
	return false;
});

$("#nextTwo").click (function() {
	reqPgNoTwo = reqPgNoTwo + 1;
	fetchJSONResults("Two", getSearchResults2(searchVal, 'test'),reqPgNoTwo, "source", sortOrder);
	return false;
});

$("#prevThree").click (function() {
	reqPgNoThree = reqPgNoThree - 1;
	fetchJSONResults("Three", getSearchResults3(searchVal, 'test'),reqPgNoThree, "source", sortOrder);
	return false;
});

$("#nextThree").click (function() {
	reqPgNoThree = reqPgNoThree + 1;
	fetchJSONResults("Three", getSearchResults3(searchVal, 'test'),reqPgNoThree, "source", sortOrder);
	return false;
});

function resultsMode(tableId) {
	$("#homeContents").hide();
	$("#searchResults").show();
	$("#next" + tableId).hide(); //TODO : Remove from DOM
	$("#prev" + tableId).hide(); //TODO : Remove from DOM
}