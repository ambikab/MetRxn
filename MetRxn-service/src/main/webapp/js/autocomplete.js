$( "#appendedInputButtons" ).autocomplete({
      source: function( request, response ) {
		//var searchTerm = getAutoComplete1(request.term);
		var data = '{"query": {"query_string" : {"fields" : ["metab","rxn"],"query" : "'+ request.term + '*"}}}';
		//var pathParams = "pageNumber=1" + "&sortCol=1"  + "&sortOrder=" + sortOrder + "&queryString=" + encodeURIComponent(query);;
        $.ajax({
		  type: "GET",	
          url: "http://localhost:9200/autocomplete/compounds/_search?q="+ request.term +"*",
          dataType: "jsonp",
          success: function( data ) {
			response( $.map( data.hits.hits, function( item ) {
				var source = item["_source"];
				var labelvalue = "TEST";
				if (typeof  source.metab === 'undefined' ) {
					labelvalue = source.rxn;
					selectLabel = source.rxn + "(RXN)";
				} else {
					labelvalue = source.metab;
					selectLabel = source.metab + "(METAB)";
				}
				return {
						
							label:  selectLabel,
							value: labelvalue
						
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