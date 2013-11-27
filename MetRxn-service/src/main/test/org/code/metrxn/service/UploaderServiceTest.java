package org.code.metrxn.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import javax.xml.stream.XMLStreamException;
import org.code.metrxn.model.uploader.FileColumn;
import org.code.metrxn.model.uploader.Rxn;
import org.code.metrxn.model.uploader.SBMLContent;
import org.code.metrxn.model.uploader.SpeciesRef;
import org.code.metrxn.model.uploader.TableMapper;
import org.code.metrxn.repository.workflow.MapperRepository;
import org.code.metrxn.repository.workflow.SBMLRepository;
import org.code.metrxn.repository.dml.*;
import org.code.metrxn.service.workflow.UploaderService;
import org.code.metrxn.util.Logger;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.SpeciesReference;

/**
 * TO Test the uploader service
 * @author ambika_b
 *
 */
public class UploaderServiceTest {
	static Connection connection;

	static MapperRepository mapperRepository;

	//static InsertRepository insertRepository;

	static SBMLRepository sbmlRepository;

	static UploaderService uploaderService;

	static {
		Logger.info("creating connection", UploaderServiceTest.class);
		createConnection();
	}

	public static void createConnection() {
		String connectionURL = "jdbc:mysql://costas4086.engr.psu.edu:3306/test_Metrxn_version_2";
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(connectionURL, "ambika", "ambika");
			mapperRepository = new MapperRepository(connection);
			sbmlRepository = new SBMLRepository(connection);
			uploaderService = new UploaderService(mapperRepository , sbmlRepository);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main (String[] args) throws FileNotFoundException {
		File file = new File("R:/univproject/otherDocs/test/sbml.xml");
		FileInputStream uploadedInputStream = new FileInputStream(file);
		String workflowId = UUID.randomUUID().toString();
		try {
			StringBuilder fileContents = new StringBuilder(uploaderService.getFileContents(uploadedInputStream, workflowId));
			SBMLDocument document = SBMLReader.read(fileContents.toString());
			mapperRepository.saveRawFileData(fileContents, workflowId, "SBML");
			Model model = document.getModel();
			SBMLContent content = new SBMLContent(model.getNumReactions(), model.getNumSpecies(), model.getNumCompartments(), model.getVersion(), new ArrayList<Rxn>());
			for (Reaction rxn : model.getListOfReactions()) {
				Rxn reaction = new Rxn();
				reaction.setName(rxn.getName());
				reaction.setSboTerm(rxn.getSBOTerm());
				reaction.setSboTermId(rxn.getSBOTermID());
				ArrayList<SpeciesRef> products = new ArrayList<SpeciesRef>(); 
				for (SpeciesReference element : rxn.getListOfProducts()) {
					products.add(new SpeciesRef(element.getStoichiometry(),element.getElementName(),element.getSpecies(), element.getId()));
				}
				ArrayList<SpeciesRef> reactants = new ArrayList<SpeciesRef>();
				for (SpeciesReference element : rxn.getListOfReactants()) {
					reactants.add(new SpeciesRef(element.getStoichiometry(),element.getElementName(),element.getSpecies(), element.getId()));
				}
				reaction.setProducts(products);
				reaction.setReactants(reactants);
				content.getRxns().add(reaction);
			}
			sbmlRepository.insertSbml(content, workflowId);
			sbmlRepository.updateStatus(workflowId);
		} catch (XMLStreamException e) {
		} 
		Logger.info("saved to the db!!", UploaderServiceTest.class);
	}

	public static void csvUploadTest() {
		String delimitter = ",";
		String entityType = "METABOLITE";
		TableMapper tableMapper = null;
		String workflowId = "";
		Map<String, FileColumn> tableData = null;
		File file = new File("R:/univproject/otherDocs/test/sbml.xml");
		try {
			FileInputStream uploadedInputStream = new FileInputStream(file);
			workflowId = UUID.randomUUID().toString();
			Logger.info("reading contents", UploaderServiceTest.class);
			tableData = uploaderService.readContents(uploadedInputStream, workflowId, entityType, delimitter, "\\", "TESTFILE");
			Logger.info("fetching mappings", UploaderServiceTest.class);
			tableMapper = mapperRepository.fetchDbMappings(workflowId, entityType, tableData);
			tableMapper.setId(workflowId);

		} catch (IOException e) {
			System.out.println("Error in reading csv files.");
			e.printStackTrace();
		}
		//insertRepository.metabolitesUpload(workflowId);
		//System.out.println(JsonUtil.toJsonForObject(tableMapper).toString());
	}
}