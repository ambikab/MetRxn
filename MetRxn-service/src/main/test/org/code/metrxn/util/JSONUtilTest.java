package org.code.metrxn.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.code.metrxn.dto.ViewResource;
import org.code.metrxn.model.uploader.ColumnMapping;
import org.code.metrxn.model.uploader.TableMapper;

import com.sun.grizzly.util.http.mapper.Mapper;

/**
 * 
 * @author ambika_b
 *
 */
public class JSONUtilTest {
	
	static ViewResource viewResource;

	public static void beforeTest () {
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("empName", "Ambika Babuji");
		result.put("empId", "935");
		List<HashMap<String, Object>> results = new ArrayList<HashMap<String, Object>>();
		results.add(result);
		viewResource = new ViewResource();
	}
	
	public static void mapperJson () {
		ArrayList<ColumnMapping> colMapping = new ArrayList<ColumnMapping>();
		colMapping.add(new ColumnMapping("COLUMN2", 2));
		HashMap<String, ArrayList<ColumnMapping>> colMap = new HashMap<String, ArrayList<ColumnMapping>>();
		colMap.put("TABLECOLUMN", colMapping);
		TableMapper mapper = new TableMapper();
		mapper.setEntityName("METABOLITE");
		mapper.setId("1234");
		mapper.setMapper(colMap);
		System.out.println(JsonUtil.toJsonForObject(mapper));
	}
	
	public static void main(String[] args) {
		mapperJson();
		//System.out.println(JsonUtil.toJsonForObject(viewResource));
	}
}
