package org.code.metrxn.service.workflow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.stream.XMLStreamException;
import org.code.metrxn.model.uploader.FileColumn;
import org.code.metrxn.model.uploader.FileData;
import org.code.metrxn.model.uploader.Rxn;
import org.code.metrxn.model.uploader.SBMLContent;
import org.code.metrxn.model.uploader.SpeciesRef;
import org.code.metrxn.model.uploader.TableMapper;
import org.code.metrxn.repository.workflow.MapperRepository;
import org.code.metrxn.repository.workflow.SBMLRepository;
import org.code.metrxn.util.JsonUtil;
import org.code.metrxn.util.Logger;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.SpeciesReference;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;


@Path("/entity/uploader")
public class UploaderService {

	static MapperRepository mapperRepository ;

	static SBMLRepository sbmlRepository;

	public UploaderService(MapperRepository mapperRepository, SBMLRepository sbmlRepository) {
		this.mapperRepository = mapperRepository;
	}

	public UploaderService() {
		mapperRepository = new MapperRepository();
		sbmlRepository = new SBMLRepository();
	}

	/**
	 * reads a input file 
	 * generates the mapping between the csv file and the table columns.
	 * @param uploadedInputStream
	 * @param fileDetail
	 * @return
	 */
	@Path("/csv")
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public String uploadCSVFile(@FormDataParam("file") InputStream uploadedInputStream, @FormDataParam("file") FormDataContentDisposition fileInfo, @FormDataParam("entityType") String entityType, @FormDataParam("fileType") String fileType) {
		String splitField = "\\", source = "SOURCE"; //TODO : remove fields as they are captured in the modal pop up of the next screen.
		String delimitter = ",";
		TableMapper tableMapper = null;
		Map<String, FileColumn> tableData = null;
		try {
			String workflowId = UUID.randomUUID().toString();
			tableData = readContents(uploadedInputStream, workflowId, entityType, delimitter, splitField, source);
			tableMapper = mapperRepository.fetchDbMappings(workflowId, entityType, tableData);
			tableMapper.setId(workflowId);

		} catch (IOException e) {
			Logger.error("Error in reading csv files." , UploaderService.class);
			e.printStackTrace();
		}
		return JsonUtil.toJsonForObject(tableMapper).toString();
	}

	@Path("/sbml")
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public String uploadSBMLFile(@FormDataParam("file") InputStream uploadedInputStream, @FormDataParam("file") FormDataContentDisposition fileInfo, @FormDataParam("entityType") String entityType, @FormDataParam("fileType") String fileType) {
		Map<String, String> response = new HashMap<String, String>();
		String workflowId = UUID.randomUUID().toString();
		response.put("workflowId",workflowId);
		try {
			StringBuilder fileContents = new StringBuilder(getFileContents(uploadedInputStream, workflowId));
			SBMLDocument document = SBMLReader.read(fileContents.toString());
			mapperRepository.saveRawFileData(fileContents, workflowId, "SBML");
			Model model = document.getModel();
			response.put("rxnCnt", model.getNumReactions()+"");
			response.put("speciesCnt",  model.getNumSpecies()+"");
			response.put("compartmentCnt", model.getNumCompartments()+"");
			SBMLContent content = new SBMLContent(model.getNumReactions(), model.getNumSpecies(), model.getNumCompartments(), model.getVersion(), new ArrayList<Rxn>());
			for (Reaction rxn : model.getListOfReactions()) {
				Rxn reaction = new Rxn();
				reaction.setName(rxn.getName());
				//TODO: rxn.getNotes() to be saved in col rxnNotes.
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
		} catch (XMLStreamException e) {
			Logger.error("error in fetching the contents of the SBML file.", UploaderService.class);
			e.printStackTrace();
		}
		return JsonUtil.toJsonForObject(response).toString();
	}

	public String getFileContents(InputStream uploadedInputStream, String workFlowId) {
		BufferedReader ipReader = new BufferedReader(new InputStreamReader (uploadedInputStream));
		StringBuilder fileContents = new StringBuilder();
		String ipLine = null;
		try {
			ipLine = ipReader.readLine();
			while(ipLine != null){
				fileContents.append(ipLine);
				ipLine = ipReader.readLine();
			}
		} catch (IOException e) {
			Logger.error("error in saving the SBML file contetns to the database.", UploaderService.class);
			e.printStackTrace();
		}
		return fileContents.toString();
	}

	/**
	 * Reads the content of the uploaded CSV file.
	 * @param uploadedInputStream
	 * @throws IOException
	 */
	public Map<String, FileColumn> readContents(InputStream uploadedInputStream, String uId, String entityType, String delimitter, String splitField, String source) throws IOException {
		List<String> headerTokens = new ArrayList<String>();
		BufferedReader ipReader = new BufferedReader(new InputStreamReader (uploadedInputStream));
		Map<String, FileColumn> data = new HashMap<String, FileColumn>();
		int header = -1; // To skip header ie., the first line of the csv.
		String ipLine = ipReader.readLine();
		StringBuilder fileContents = new StringBuilder();
		List<FileData> fileData = new ArrayList<FileData>();
		for (int rowId = 0 ;ipLine != null; rowId ++) {
			fileContents.append(ipLine + "\n");
			if (header == -1) {
				header = 1;
				ipLine = ipLine.concat(",splitField,source");
				StringTokenizer splitter = new StringTokenizer(ipLine, delimitter);
				while (splitter.hasMoreTokens()) {
					String key =splitter.nextToken(), value = null;
					headerTokens.add(key);
					data.put(key, new FileColumn(uId, key, value));
				}
			}
			else  {
				ipLine = ipLine.concat("," + splitField + "," + source);
				StringTokenizer splitter = new StringTokenizer(ipLine, delimitter);
				for (int i = 0; i < headerTokens.size() ; i++) {
					String value = "'" + (splitter.hasMoreTokens() ? splitter.nextToken() : "") + "'", key = headerTokens.get(i);
					FileColumn columnData = data.get(key);
					fileData.add(new FileData(key, value, rowId));
					value = columnData.getData() == null ? value : columnData.getData() + " , " + value;
					columnData.setData(value);
					data.put(key, columnData);	
				}
			}
			ipLine = ipReader.readLine();
		}
		mapperRepository.saveFileData(uId, fileData);
		mapperRepository.saveRawFileData(fileContents, uId, entityType);
		return data;
	}
}