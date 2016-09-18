package com.sdll18.server.data.Dao;

import java.util.List;

import com.sdll18.server.data.persistence.Comment;

public interface CommentDao {
	
	void create(Comment comment);

	void remove(Comment comment);
	
	void remove(int id);

	void update(Comment comment);
	
	void updateFromSql(String sql);

	Comment query(int id);
	
	List<Comment> queryListByUserId(int id,int page,int pageSize);
	
	List<Comment> queryListByPassageId(int id,int page,int pageSize);

	List<Comment> queryList(int page,int pageSize);
	
}
