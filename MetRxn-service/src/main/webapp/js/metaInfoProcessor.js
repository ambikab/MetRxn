//TODO : what goes into the href attribute?!
function createAnchors(key, value, type) {
	if (type == 'anchor') {
		aEnd = "<a >";
		aBegin = "<a class = 'searchLinks' href = 'http://localhost:8080/MetRxn-service/services/queries/results'>NAD+</a><span></span>";
		return aBegin + value + aEnd;
	}
}

$(document).ready(function(){
$(".searchLinks").live('click', function(e) {
        e.preventDefault();
        var url = $(this).attr('href');
		var searchText = $(this).text();
		searchVal = searchText;
		searchType = $(this).parent().children('span:first').text();
		searchResults(url,"One", getSearchResults1(searchText, searchType),reqPgNoTwo, "sources", sortOrder);
		searchResults(url,"Two", getSearchResults2(searchText, searchType),reqPgNoTwo, "source", sortOrder);
		searchResults(url,"Three", getSearchResults3(searchText,searchType),reqPgNoTwo, "source", sortOrder);
    });
});


function searchResults (url, tableId, query, requestedPageNumber, sortCol, sortOrder) {
	var pathParams = "sessionId=" + getCookie('session') + "&pageNumber=" + requestedPageNumber + "&sortCol=" + sortCol + "&sortOrder=" + sortOrder + "&queryString=" + encodeURIComponent(query); 
	$.ajax({
		type: "POST",
		url: url,
		data : pathParams, 
		dataType: "json",
		success: function(result){
			result = jQuery.parseJSON(JSON.stringify(result));
			$("#resultsTable"+tableId + " tbody").empty();
			resultsMode(tableId);
			if ( result.resultSet[0]['isEmpty']) {
				var rowData = "<tr><td colspan = '4'>No results matched your search!! </td></tr>";
				$("#resultsTable"+tableId + " tbody").append(rowData);
			} else if (! result['sessionId']) {
				var rowData = "<tr><td colspan = '4'>Invalid session!! Please log in to continue. </td></tr>";
				$("#resultsTable"+tableId + " tbody").append(rowData);
				$("#prev" + tableId).hide();
				$("#next" + tableId).hide();
			}else {
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
					$.each(collection[employee], function(key,value){
						rowHead = rowHead + " <th> <a id = '"+ key + "' href='#'>" + key + "</th>";
						rowData = rowData + " <td> " + value + "</td> ";
					});
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