package org.code.metrxn.repository.workflow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;

import org.code.metrxn.model.uploader.Rxn;
import org.code.metrxn.model.uploader.SBMLContent;
import org.code.metrxn.model.uploader.SpeciesRef;
import org.code.metrxn.util.DBUtil;
import org.code.metrxn.util.Logger;

/**
 * 
 * @author ambika_b
 *
 */
public class SBMLRepository {

	Connection connection;
	
	public SBMLRepository(Connection connection) {
		this.connection = connection;
	}
	
	public SBMLRepository() {
		connection = DBUtil.getConnection();
	}
	
	public boolean updateStatus(String workflowId) {
		boolean flag = false;
		String updateStatus = "update upload.sbml set status = 1 where workflowId = ? ";
		try {
			PreparedStatement psSBML = connection.prepareStatement(updateStatus);
			psSBML.setString(1, workflowId);
			flag = psSBML.executeUpdate() == 1 ? true : false;
		} catch (SQLException e) {
			Logger.error("Error when updating the status of the SBML contents ", SBMLRepository.class);
			e.printStackTrace();
		}
		return flag;
	}
	
	public boolean insertSbml(SBMLContent sbmlContent, String workflowId) {
		String insertSBML = "insert into upload.sbml(workflowId, version, compartmentType, metaboliteCount, reactionCount) value(?,?,?,?,?)";
		try {
			PreparedStatement psSBML = connection.prepareStatement(insertSBML);
			psSBML.setString(1, workflowId);
			psSBML.setInt(2, sbmlContent.getVersion());
			psSBML.setInt(3, sbmlContent.getComparatmentType());
			psSBML.setInt(4, sbmlContent.getMetaboliteCnt());
			psSBML.setInt(5, sbmlContent.getRxnCount());
			psSBML.executeUpdate();
			insertReaction(sbmlContent.getRxns(), workflowId);
		} catch (SQLException e) {
			Logger.error("Error while saving the SBML file contents ",SBMLRepository.class);
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean insertReaction(List<Rxn> reactions, String workflowId) throws SQLException {
		String insertReactions = "insert into upload.sbmlReaction(rxnId, sboTerm, sboTermId, name, workflowId) values(?,?,?,?,?)";
		for (Rxn reaction : reactions) {
			try {
				String rxnId = UUID.randomUUID().toString();
				PreparedStatement psRxn = connection.prepareStatement(insertReactions);
				psRxn.setString(5, workflowId);
				psRxn.setString(4,reaction.getName());
				psRxn.setString(3, reaction.getSboTermId());
				psRxn.setInt(2, reaction.getSboTerm());
				psRxn.setString(1,rxnId);
				psRxn.executeUpdate();
				insertSpecies(reaction.getProducts(), rxnId, "PRODUCT");
				insertSpecies(reaction.getReactants(), rxnId, "REACTANT");
			} catch (SQLException e) {
				Logger.error("Error while saving the sbml's reaction data!!", SBMLRepository.class);
				e.printStackTrace();
				throw new SQLException();
			}
		}
		return true;
	}
	
	public boolean insertSpecies(List<SpeciesRef> products, String rxnId, String type) throws SQLException {
		String insertProduct = "insert into upload.sbmlSpecies(stoichiometry, name, speciesId, id, rxnId, type) values(?,?,?,?,?,?)";
		for (SpeciesRef product : products) {
			try {
				PreparedStatement psProduct = connection.prepareStatement(insertProduct);
				psProduct.setDouble(1, product.getStoichiometry());
				psProduct.setString(2, product.getName());
				psProduct.setString(3, product.getSpeciesId());
				psProduct.setString(4, product.getId());
				psProduct.setString(5, rxnId);
				psProduct.setString(6, type);
				psProduct.executeUpdate();
			} catch (SQLException e) {
				Logger.error("Error in saving the SBML species data.",SBMLRepository.class);
				e.printStackTrace();
				throw new SQLException();
			}
		}
		return true;
	}
}
