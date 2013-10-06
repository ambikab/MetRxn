package org.code.metrxn.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.code.metrxn.dto.ViewResource;

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
	
	public static void main(String args[]) {
		beforeTest();
		System.out.println(JsonUtil.toJsonForObject(viewResource));
	}
}
