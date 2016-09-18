package com.sdll18.notificationserver.network.json;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class Json {

	// private final static String TAG = "MelonStudio.Util.Json";

	/**
	 * 将Json字符串转化为Map
	 * 
	 * @param json
	 *            JSON字符串
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> getMap(String json) {
		try {
			Map<String, String> result = (Map<String, String>) JSONObject
					.toBean(JSONObject.fromObject(json), Map.class);
			for (String key : result.keySet()) {
				Object obj = result.get(key);
				if (obj.equals(JSONNull.getInstance())) {
					result.put(key, "null");
				}
			}
			return result;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将Map转化为Json字符串
	 * 
	 * @param map
	 *            map
	 * @return
	 */
	public static String getJson(Map<String, String> map) {
		try {
			return JSONObject.fromObject(map).toString();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取List of String
	 * 
	 * @param json
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getStringArr(String json) {
		try {
			return (List<String>) JSONArray.toList(JSONArray.fromObject(json),
					new String(), new JsonConfig());
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取List of Map
	 * 
	 * @param json
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Map<String, String>> getMapArr(String json) {
		try {
			return (List<Map<String, String>>) JSONArray.toList(
					JSONArray.fromObject(json), new HashMap<String, String>(),
					new JsonConfig());
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将List of Map 转化为 Json
	 * 
	 * @param list
	 * @return
	 */
	public static String getJsonFromListMap(List<Map<String, String>> list) {
		try {
			JSONArray array = JSONArray.fromObject(list);
			for (int i = 0; i < array.size(); i++) {
				array.set(i, JSONObject.fromObject(array.get(i)).toString());
			}
			return array.toString();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将List转化为Json
	 * 
	 * @param list
	 * @return
	 */
	public static String getJson(List<String> list) {
		try {
			return JSONArray.fromObject(list).toString();
		} catch (Exception e) {
			return null;
		}
	}
}
