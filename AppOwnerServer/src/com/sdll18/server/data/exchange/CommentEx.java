package com.sdll18.server.data.exchange;

import com.sdll18.server.data.persistence.Comment;

public class CommentEx {
	 
	private int id;

	private String content;

	private String time;

	private int praise;

	private short type;

	private String url;

	private String userId;
	
	private String username;
	
	private String iconUrl;
	
	public CommentEx(Comment comment){
		id = comment.getId();
		content = comment.getContent();
		time = comment.getTime();
		praise = comment.getPraise();
		
	}
	
}
