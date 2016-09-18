package com.ivypro.Dao;

import java.util.List;

import com.ivypro.Persistant.Order;

public interface OrderDao {
	void create(Order order);

	void remove(Order order);

	void remove(int id);

	void update(Order order);

	void updateFromSql(String sql);

	Order query(int id, boolean lazy);

	List<Order> queryList(int userid);

	List<Order> queryList(int userid, int page, int pageSize);

	List<Order> queryList(int userid, boolean isEnd);

	List<Order> queryList(int page, int pageSize, boolean lazy);

	List<Order> queryList(int page, int pageSize, boolean lazy, boolean isEnd);

}
