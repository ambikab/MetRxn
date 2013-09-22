package org.code.metrxn.util;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class JsonUtil {


	private static Map emptyMap;

	static {
		emptyMap = new HashMap(1);
		emptyMap.put("isEmpty", "true");
	}

	public static JSONArray toJson(Object object) {
		System.out.println("Values in List");
		JSONArray jsonArray = new JSONArray();
		Collection collection = (Collection) object;
		if (collection.isEmpty()) {
			return jsonArray.put(emptyMap);
		} else {
			for (Object collectionObject : collection) {
				if (collectionObject instanceof Collection ) {
					jsonArray.put(toJson(collectionObject));
				}
				else {
					jsonArray.put(toJsonForObject(collectionObject));
				}  
			}
		}
		return jsonArray;
	}

	public static JSONObject toJsonForObject(Object object) {
		JSONObject jsonObj = new JSONObject();
		Object value = null;
		if ( object instanceof Map) {
			for (Map.Entry<String, Object> entry : ((Map<String, Object>) object).entrySet()) {
				String key = entry.getKey();
				Object mapValue = entry.getValue();
				try {
					jsonObj.put(key,mapValue);
				} catch (JSONException e) {
					e.printStackTrace();
				}                           
			}
		} else {
			Field[] fields = object.getClass().getFields();
			for (int i = 0; i < fields.length; i++) {
				String key = fields[i].getName();
				System.out.println("key : " + key);

				try {
					value = fields[i].get(object);
					if (value instanceof Collection ) {
						value = toJson(value);
						jsonObj.put(key, (Object) value);
					} else if (value != null  && value.getClass().getName().startsWith("org.code.metrxn")) {
						value = toJsonForObject(value);
						jsonObj.accumulate(key, value);
					} else {
						jsonObj.accumulate(key, value);
					}
					System.out.println("Iterating thru the collection of value : " + value);
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return jsonObj;
	}

}
