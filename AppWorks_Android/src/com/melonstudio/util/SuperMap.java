package com.melonstudio.util;
import java.util.HashMap;


/**
 * 
 * @author Wo
 *
 */
public class SuperMap extends HashMap<String, String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 711633968767001794L;

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
		this.put("style", style);
	}

	/**
	 * @return
	 */
	public String finishByJson() {
		return JSONHelper.toJSON(this);
	}


	public void put(String key, int value) {
		this.put(key, String.valueOf(value));
	}

	public void put(String key, boolean value) {
		this.put(key, String.valueOf(value));
	}

	public void put(String key, Object value) {
		this.put(key, String.valueOf(value));
	}

}
