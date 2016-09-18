package com.melonstudio.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Comment {

	private int id;

	private String content;

	private String time;

	private int praise;

	private String passage_id;
	private String user_id;
	private String user_name;

	private String user_url;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getPraise() {
		return praise;
	}

	public void setPraise(int praise) {
		this.praise = praise;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_url() {
		return user_url;
	}

	public void setUser_url(String user_url) {
		this.user_url = user_url;
	}

	public String getPassage_id() {
		return passage_id;
	}

	public void setPassage_id(String passage_id) {
		this.passage_id = passage_id;
	}

	public static List<Comment> createFromJsonArray(JSONArray jsonArray) {
		List<Comment> list = new ArrayList<Comment>();
		if (jsonArray != null) {
			try {
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					Comment comment = new Comment();
					comment.setId(jsonObject.getInt("id"));
					comment.setPraise(jsonObject.getInt("praise"));
					comment.setTime(jsonObject.getString("time"));
					comment.setPassage_id(jsonObject.getString("passage_id"));
					comment.setUser_id(jsonObject.getString("user_id"));
					comment.setContent(jsonObject.getString("content"));
					comment.setUser_name(jsonObject.getString("user_name"));
					comment.setUser_url(jsonObject.getString("user_icon"));
					list.add(comment);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

}
