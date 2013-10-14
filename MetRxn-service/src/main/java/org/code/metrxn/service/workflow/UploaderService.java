package org.code.metrxn.service.workflow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import org.code.metrxn.model.uploader.ColumnData;
import org.code.metrxn.model.uploader.TableMapper;
import org.code.metrxn.repository.workflow.MapperRepository;
import org.code.metrxn.util.JsonUtil;
import org.code.metrxn.util.file.Tokenizer;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Path("/entity/uploader")
@Consumes(MediaType.MULTIPART_FORM_DATA)
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
	public String uploadFile(@FormDataParam("file") InputStream uploadedInputStream, @FormDataParam("file") FormDataContentDisposition fileDetail) {
		TableMapper tableMapper = null;
		List<ColumnData> tableData = null;
		
		try {
		/** generate a session id which is unique **/
		String entryId = UUID.randomUUID().toString();
		List<String> headerTokens =  extractHeader(uploadedInputStream);
		tableData = readContents(uploadedInputStream, headerTokens, entryId);
		tableMapper = mapperRepository.fetchDbMappings(entryId, "entityName", tableData);
		tableMapper.setId(entryId);
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
	public List<String> extractHeader(InputStream uploadedInputStream) {
		List<String> headerTokens = new ArrayList<String>();
		BufferedReader ipReader = new BufferedReader(new InputStreamReader (uploadedInputStream));
		try {
			headerTokens = Tokenizer.readTokens(",", ipReader.readLine());			
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
	public List<ColumnData> readContents(InputStream uploadedInputStream, List<String> headerTokens, String uId) throws IOException {
		BufferedReader ipReader = new BufferedReader(new InputStreamReader (uploadedInputStream));
		List<ColumnData> data = new ArrayList<ColumnData>();
		int header = -1; // To skip header ie., the first line of the csv.
		String delimitter = ","; // CSV delimitter.

		String ipLine = ipReader.readLine();
		while (ipLine != null) {
			if (header != -1)  {
				StringTokenizer splitter = new StringTokenizer(ipLine, delimitter);
				for (int i = 0; splitter.hasMoreTokens(); i++) {
					ColumnData column = new ColumnData(uId, headerTokens.get(i), splitter.nextToken());
					data.add(column);
				}
			}
			ipLine = ipReader.readLine();
			header = 1;
		}
		return data;
	}

}
