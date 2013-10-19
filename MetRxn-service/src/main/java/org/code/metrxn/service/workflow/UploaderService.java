package org.code.metrxn.service.workflow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import org.code.metrxn.model.uploader.FileColumn;
import org.code.metrxn.model.uploader.TableMapper;
import org.code.metrxn.repository.workflow.MapperRepository;
import org.code.metrxn.util.JsonUtil;
import org.code.metrxn.util.file.Tokenizer;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;


@Path("/entity/uploader")
public class UploaderService {

	static MapperRepository mapperRepository = new MapperRepository();

	/**
	 * reads a input file 
	 * generates the mapping between the csv file and the table columns.
	 * @param uploadedInputStream
	 * @param fileDetail
	 * @return
	 */
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String uploadFile(@FormDataParam("file") InputStream uploadedInputStream, @FormDataParam("file") FormDataContentDisposition fileInfo, @FormDataParam("entityType") String entityType,  @FormDataParam("fileType") String fileType) {
		//
		//		String entityType = "";
		System.out.println("entity type is : " + entityType);
		System.out.println("delimiter is : " + fileType);
		String delimitter = ",";
		TableMapper tableMapper = null;
		Map<String, FileColumn> tableData = null;
		try {
			String workflowId = UUID.randomUUID().toString();
			List<String> headerTokens =  extractHeader(uploadedInputStream,delimitter);
			for( int i = 0 ; i < headerTokens.size(); i++)
				System.out.println("header contains :" + headerTokens.get(i));
			tableData = readContents(uploadedInputStream, headerTokens, workflowId, delimitter);
			tableMapper = mapperRepository.fetchDbMappings(workflowId, entityType, tableData);
			tableMapper.setId(workflowId);
		} catch (IOException e) {
			System.out.println("Error in reading csv files.");
			e.printStackTrace();
		}
		return JsonUtil.toJsonForObject(tableMapper).toString();
	}

	/**
	 * Fetch the header from the csv file.
	 * @param uploadedInputStream
	 * @return
	 */
	public List<String> extractHeader(InputStream uploadedInputStream, String delimitter) {
		List<String> headerTokens = new ArrayList<String>();
		BufferedReader ipReader = new BufferedReader(new InputStreamReader (uploadedInputStream));
		try {
			headerTokens = Tokenizer.readTokens(delimitter, ipReader.readLine());			
		} catch (IOException e) {
			System.out.println("Error ocurred while fetching the header of the csv file.");
		}

		return headerTokens;
	}

	/**
	 * Reads the content of the uploaded CSV file.
	 * @param uploadedInputStream
	 * @throws IOException
	 */
	public Map<String, FileColumn> readContents(InputStream uploadedInputStream, List<String> headerTokens, String uId, String delimitter) throws IOException {
		BufferedReader ipReader = new BufferedReader(new InputStreamReader (uploadedInputStream));
		Map<String, FileColumn> data = new HashMap<String, FileColumn>();
		int header = -1; // To skip header ie., the first line of the csv.
		String ipLine = ipReader.readLine();
		while (ipLine != null) {
			if (header == -1) {
				for(int i = 0; i < headerTokens.size(); i++) {
					String key = headerTokens.get(i), value = null;
					data.put(key, new FileColumn(uId, key, value));
				}
			}
			else  {
				StringTokenizer splitter = new StringTokenizer(ipLine, delimitter);
				for (int i = 0; i < headerTokens.size() && splitter.hasMoreTokens(); i++) {
					String value = "\'" + splitter.nextToken() + "\'", key = headerTokens.get(i);
					FileColumn columnData = data.get(key);
					//System.out.println("column data stored already is " + columnData.getData());
					value = columnData.getData() == null ? value : columnData.getData() + " , " + value;
					columnData.setData(value);
					data.put(key, columnData);	
				}
			}
			ipLine = ipReader.readLine();
			header = 1;
		}
		return data;
	}
}