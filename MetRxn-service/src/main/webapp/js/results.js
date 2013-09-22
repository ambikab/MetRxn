// displays the search results on click of the search button
$("#searchBtn").click (function() {
	if ($("#appendedInputButtons").val().length == 0) {
		//addAlert("Warning", "Search string cannot be empty!!") TODO : Use bootstrap alert to show warnings.
		alert("Search string cannot be empty!");
		return 0;
	}
	sortCol = 'empId';
	sortOrder = 'ASC';
	searchVal = $("#appendedInputButtons").val();
	placeSortArrows(sortCol, chooseSortArrows(sortOrder));
	fetchJSONResults(1, 'empId', 'ASC');
});

function fetchJSONResults (requestedPageNumber, sortCol, sortOrder) {
	var pathParams = "/" + requestedPageNumber + "/" + sortCol + "/" + sortOrder + "/" + encodeURIComponent(getEmployeeSearch(searchVal)); 
	$.ajax({
		url: "http://localhost:8080/MetRxn-service/services/queries/results" + pathParams,
		dataType: "json",
		success: function(result){
			result = jQuery.parseJSON(JSON.stringify(result));
			$("#resultsTable tbody").empty();
			resultsMode();
			if ( result == null ) {
				var rowData = "<tr><td colspan = '4'>No results matched your search!! </td></tr>";
				$("#resultsTable tbody").append(rowData);
			} else {
				var collection = result.resultSet;
				var currentPage = parseInt(result.currentPageNumber);
				reqPgNo = currentPage;
				var total = parseInt(result.totalRecordCount);
				var recordsPerPage = 5; //TO be returned from the back end as a part of the dto.
				/** calculation for next and previous button values **/
				var totalPages = 0;
				if (total % recordsPerPage == 0)
					totalPages = total / recordsPerPage;
				else
					totalPages = parseInt(total / recordsPerPage) + 1;
				if (currentPage + 1 <= totalPages) {
					$("#next").show(); //TODO : append to dom
				}
				if (currentPage - 1 >= 1)
					$("#prev").show();
				var size = 0;
				$("#currentpageNumber").text(currentPage);
				$.each(collection, function(employee) {
					size = 1;
					console.log(collection[employee]);
					var extraData = "<td>10000</td><td>Sales</td>";
					var rowData = "<tr> <td> " + collection[employee].id + "</td> <td>" + collection[employee].name + "</td> " + extraData + "</tr>";
					$("#resultsTable tbody").append(rowData);
				});
			}				
		}
	});
}