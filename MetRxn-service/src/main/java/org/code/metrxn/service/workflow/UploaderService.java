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
import org.code.metrxn.model.uploader.FileColumn;
import org.code.metrxn.model.uploader.FileData;
import org.code.metrxn.model.uploader.TableMapper;
import org.code.metrxn.repository.workflow.MapperRepository;
import org.code.metrxn.util.JsonUtil;
import org.code.metrxn.util.Logger;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;


@Path("/entity/uploader")
public class UploaderService {

	static MapperRepository mapperRepository ;
	
	public UploaderService() {
		mapperRepository = new MapperRepository();
	}
	
	public UploaderService(MapperRepository mapperRepository) {
		this.mapperRepository = mapperRepository;
	}

	/**
	 * reads a input file 
	 * generates the mapping between the csv file and the table columns.
	 * @param uploadedInputStream
	 * @param fileDetail
	 * @return
	 */
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public String uploadFile(@FormDataParam("file") InputStream uploadedInputStream, @FormDataParam("file") FormDataContentDisposition fileInfo, @FormDataParam("entityType") String entityType, @FormDataParam("fileType") String fileType) {
		String splitField = "SF", source = "SOURCE";
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