// displays the search results on click of the search button
$("#searchBtn").click (function() {
	if ($("#appendedInputButtons").val().length == 0) {
		//addAlert("Warning", "Search string cannot be empty!!") TODO : Use bootstrap alert to show warnings.
		alert("Search string cannot be empty!");
		return 0;
	}

	sortOrder = 'ASC';
	searchVal = $("#appendedInputButtons").val();
	placeSortArrows("source", chooseSortArrows(sortOrder));
	reqPgNoOne = 1;
	fetchJSONResults("One", getSearchResults1(searchVal, 'test'),1, 'sources', 'ASC');
	reqPgNoTwo = 1;
	fetchJSONResults("Two", getSearchResults2(searchVal, 'test'),1, 'source', 'ASC');
	reqPgNoThree = 1;
	fetchJSONResults("Three", getSearchResults3(searchVal, 'test'),1, 'source', 'ASC');
	return false;
});

function fetchJSONResults (tableId, query, requestedPageNumber, sortCol, sortOrder) {
	var pathParams = "pageNumber=" + requestedPageNumber + "&sortCol=" + sortCol + "&sortOrder=" + sortOrder + "&queryString=" + encodeURIComponent(query); 
	$.ajax({
		type: "POST",
		url: "http://localhost:8080/MetRxn-service/services/queries/results",
		data : pathParams, 
		dataType: "json",
		success: function(result){
			result = jQuery.parseJSON(JSON.stringify(result));
			$("#resultsTable"+tableId + " tbody").empty();
			resultsMode(tableId);
			if ( result.isEmpty == true ) {
				var rowData = "<tr><td colspan = '4'>No results matched your search!! </td></tr>";
				$("#resultsTable"+tableId + " tbody").append(rowData);
			} else {
				var collection = result.resultSet;
				var currentPage = parseInt(result.currentPageNumber);
				//reqPgNo = currentPage;
				var total = parseInt(result.totalRecordCount);
				var recordsPerPage = 5; //TO be returned from the back end as a part of the dto.
				/** calculation for next and previous button values **/
				var totalPages = 0;
				if (total % recordsPerPage == 0)
					totalPages = total / recordsPerPage;
				else
					totalPages = parseInt(total / recordsPerPage) + 1;
				if (currentPage + 1 <= totalPages) {
					$("#next" + tableId).show(); //TODO : append to dom
				}
				if (currentPage - 1 >= 1)
					$("#prev" + tableId).show();
				$("#currentpageNumber" + tableId).text(currentPage);
				var header = '0';
				$.each(collection, function(employee) {
					var rowBegin = "<tr>";
					var rowData = "";
					var rowHead = "";
					var testData = "<td> <a class = 'searchLinks' href= 'http://localhost:8080/MetRxn-service/services/queries/results'> NAD+</a></td>";
					
					$.each(collection[employee], function(key,value){
						rowHead = rowHead + " <th> <a id = '"+ key + "' href='#'>" + key + "</th>";
						rowData = rowData + " <td> " + value + "</td> ";
					});
					rowHead = rowHead ;
					var rowEnd =  "</tr>";
					if (header != '1')
						$("#resultsTable"+tableId + " thead").html(rowBegin + rowHead + rowEnd);
					$("#resultsTable"+tableId + " tbody").append(rowBegin + rowData +  rowEnd );
					header = '1';
				});
			}				
		}
	});
}