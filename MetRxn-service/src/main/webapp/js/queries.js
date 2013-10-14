function getSearchResults1(searchTerm, optionSel) {
	//SMILES Structure 
	return "SELECT group_concat(DISTINCT B.source) `sources` ,A.SMILES " 
	+ " FROM metrxn_version_2.searchResults A, metrxn_version_2.metabolites B "
	+ " WHERE A.source = B.source "
	+ " AND A.SMILES_hash = B.smiles_standard_hash "
	+ " AND A.`metab_names_hash` = PASSWORD(lower('" + searchTerm + "'))"
	+ " GROUP BY A.SMILES_hash";
}

function getSearchResults2(searchTerm, optionSelected) {
	//If option selected === metabolite => show corresponding metabolite names.
	return "SELECT DISTINCT B.source ,A.SMILES, group_concat(distinct `metrxn_version_2`.anchors(B.synonym," 
		    + "'http://localhost:8080/MetRxn-service/services/queries/results','searchLinks','metabolite','internal')) `Metabolite synonyms`," 
		    + " group_concat(distinct B.acronym) `Metabolite acronyms` "
		    + " FROM metrxn_version_2.searchResults A, metrxn_version_2.metabolites B "
			+ " WHERE A.source = B.source "
			+ " AND A.SMILES_hash = B.smiles_standard_hash "
			+ " AND A.`metab_names_hash` = PASSWORD(lower('" + searchTerm + "')) "
			+ " GROUP BY A.SMILES_hash,B.source "; //If option selected == reaction => show corresponding reaction names.
}

function getSearchResults3(searchTerm, optionSel) {
	//If option selected == metabolite => show corresponding reaction names.
	return "select B.source,group_concat(distinct B.`Reaction synonyms`) `Reaction synonyms`, group_concat(distinct B.`Reaction acronyms`) `Reaction acronyms`, group_concat(distinct coalesce(B.`EC Number`,'')) `EC Numbers` " 
	+ " from metrxn_version_2.metabolites A, metrxn_version_2.searchResults B " 
	+ " where A.names_hash = PASSWORD(lower('" + searchTerm + "')) "
	+ " and A.smiles_standard_hash = B.SMILES_hash "
	+ " group by B.source ";
}