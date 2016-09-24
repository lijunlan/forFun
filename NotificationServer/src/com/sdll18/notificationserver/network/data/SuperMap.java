package com.sdll18.notificationserver.network.data;

import java.util.HashMap;
import java.util.Map;

import com.sdll18.notificationserver.network.json.Json;


public class SuperMap {

	private Map<String, String> map = new HashMap<String, String>();

	public SuperMap() {

	}

	/**
	 * 
	 * @param method
	 *            <p>
	 *            src<br>
	 *            cls
	 *            </p>
	 */
	public SuperMap(String style) {
		map.put("style", style);
	}

	/**
	 * @return
	 */
	public Map<String, String> finish() {
		return map;
	}

	/**
	 * @return
	 */
	public String finishByJson() {
		return Json.getJson(map);
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 */
	public SuperMap put(String key, String value) {
		map.put(key, value);
		return this;
	}

	public SuperMap put(String key, int value) {
		map.put(key, String.valueOf(value));
		return this;
	}

	public SuperMap put(String key, boolean value) {
		map.put(key, String.valueOf(value));
		return this;
	}

	public SuperMap put(String key, Object value) {
		map.put(key, String.valueOf(value));
		return this;
	}

}
