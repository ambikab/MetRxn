package org.code.metrxn.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.code.metrxn.repository.workflow.MapperRepository;
import org.code.metrxn.util.JsonUtil;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class MapperServiceTest {

	static Connection connection;

	static MapperRepository mapperRepository;

	public static void createConnection() {
		String connectionURL = "jdbc:mysql://costas4086.engr.psu.edu:3306/test_Metrxn_version_2";
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(connectionURL, "ambika", "ambika");
			mapperRepository = new MapperRepository(connection);
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


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		createConnection();
		String json =  "{\" acronym\":\"formulaNeutral\",\" synonym\":\"formula\",\" smiles_standard\":\"marvin charge (pH 7)\",\" class\":\"marvin charge (pH 7.2)\",\" genericTautomer\":\"casNumber\"}";
		String wId =  "1d06fb64-5ade-4676-bf7c-211d15c45155";
		Boolean updateMapping = false;
		//Convert the mappingData Json to TableMapper
		HashMap<String, String> mappings = new HashMap<String, String>();
		try {
			JSONObject jsonObj = new JSONObject(json);
			System.out.println("Json string is :" + json);
			mappings = JsonUtil.jsonToString(jsonObj, mappings);
			Set<String> keys = mappings.keySet();
			Iterator<String> keyItr = keys.iterator();
			while (keyItr.hasNext())
				System.out.println("json key object : " + mappings.get(keyItr.next()));
			updateMapping = mapperRepository.updateMapping(mappings, wId);
		} catch (JSONException e) {
			System.out.println("Exception occured while converting into JSON");
			e.printStackTrace();
		}
		if (updateMapping)
			System.out.println("success");
		else
			System.out.println("error");
	}

}