$( "#appendedInputButtons" ).autocomplete({
      source: function( request, response ) {
		var query = getAutoComplete1(request.term);
		var pathParams = "pageNumber=1" + "&sortCol=1"  + "&sortOrder=" + sortOrder + "&queryString=" + encodeURIComponent(query);;
        $.ajax({
		  type: "POST",	
          url: "http://localhost:8080/MetRxn-service/services/queries/results",
          dataType: "json",
          data: pathParams,
          success: function( data ) {
			var suggestions = data.resultSet;
			response( $.map( suggestions, function( item ) {
              return {
                label: item.synonyms ,
                value: item.synonyms
              }
            }));
          }
        });
      },
      minLength: 2,
      select: function( event, ui ) {
        $( "#appendedInputButtons" ).val(ui.item.label);
      },
      open: function() {
        $( this ).removeClass( "ui-corner-all" ).addClass( "ui-corner-top" );
      },
      close: function() {
        $( this ).removeClass( "ui-corner-top" ).addClass( "ui-corner-all" );
      }
});