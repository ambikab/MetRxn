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
	return "select B.source,group_concat(distinct B.`Reaction synonyms`) `Reaction synonyms`, " +
				"	group_concat(distinct B.`Reaction acronyms`) `Reaction acronyms`, " +
				"	group_concat(distinct coalesce(B.`EC Numbers`,'')) `EC Numbers` " +
				"	from " +
				"	(select distinct B.source,B.`Reaction synonyms`,B.`Reaction acronyms`, " +
				"		coalesce(B.`EC Number`,'') `EC Numbers`,@rownum := @rownum + 1 AS `rownum`, " +
				"		(@rownum - mod(@rownum,10))/10 AS `resultGroupIndex` " +
				"		from metrxn_version_2.metabolites A, metrxn_version_2.searchResults B,(SELECT @rownum := 0) r " +
				"		where A.names_hash = PASSWORD(lower('" + searchTerm + "')) " +
						"	and A.smiles_standard_hash = B.SMILES_hash) B " +
						"	group by B.source,B.`resultGroupIndex`";
}

function getImageSearch(imageName) {
	return "select images from FormulaChargeDistribution where idFormulaChargeDistribution = 1 ";
}

function getFileColumnData(workFlowId, columnName) {
	return "select group_concat(distinct columnData) from entity_data " 
		+ "where workFlowId = '" + workFlowId + "'" 
		+ "and columnName = '"+ columnName 
		+ "' group by workFlowId,columnName";
}

function getAutoComplete1 (searchTerm) {
	return "select distinct `Metabolite synonyms` `synonyms` from test_Metrxn_version_2.searchResults where `Metabolite synonyms` like '" + searchTerm + "%'";
}

function getSBMLData(workflowId) {
	return "select * from `upload`.sbml where workflowId = '" + workflowId + "'";
}