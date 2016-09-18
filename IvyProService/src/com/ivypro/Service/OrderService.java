package com.ivypro.Service;

import java.util.List;

import com.ivypro.ExchangeData.ExOrder;

public interface OrderService {

	void create(ExOrder order) throws Exception;

	void remove(ExOrder order);

	void remove(int id);

	void update(ExOrder order);

	void endOrder(int id) throws Exception;

	void changeLink(int id, String link) throws Exception;

	ExOrder query(int id, boolean lazy);

	List<ExOrder> queryList(int userid);

	List<ExOrder> queryList(int userid, int page);

	List<ExOrder> queryList(int userid, int page, int pageSize);

	List<ExOrder> queryListByUser(int userid, boolean isEnd);

	List<ExOrder> queryList(int page, boolean lazy);

	List<ExOrder> queryList(int page, int pageSize, boolean lazy);

	List<ExOrder> queryList(int page, boolean lazy, boolean isEnd);

	List<ExOrder> queryList(int page, int pageSize, boolean lazy, boolean isEnd);
}
