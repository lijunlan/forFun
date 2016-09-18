package com.ivypro.Service.Impl;

import java.util.ArrayList;
import java.util.List;

import com.ivypro.Dao.OrderDao;
import com.ivypro.Dao.UserDao;
import com.ivypro.ExchangeData.ExOrder;
import com.ivypro.Persistant.Order;
import com.ivypro.Persistant.User;
import com.ivypro.Service.OrderService;

public class OrderServiceImpl implements OrderService {

	private OrderDao orderDao;

	private UserDao userDao;

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public OrderDao getOrderDao() {
		return orderDao;
	}

	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}

	@Override
	public void create(ExOrder exOrder) throws Exception {
		Order order = (Order) exOrder.toPersistant();
		User user = getUserDao().query(Integer.valueOf(exOrder.getUserId()),
				false);
		if (user == null)
			throw new Exception("user is not existed!");
		order.setCreateUser(user);
		getOrderDao().create(order);
	}

	@Override
	public void remove(ExOrder order) {
		getOrderDao().remove((Order) order.toPersistant());
	}

	@Override
	public void remove(int id) {
		getOrderDao().remove(id);
	}

	@Override
	public void update(ExOrder order) {
		getOrderDao().update((Order) order.toPersistant());
	}

	@Override
	public ExOrder query(int id, boolean lazy) {
		Order order = getOrderDao().query(id, lazy);
		ExOrder exOrder = new ExOrder();
		exOrder.setUpByPersistant(order);
		return exOrder;
	}

	@Override
	public List<ExOrder> queryList(int userid) {
		List<ExOrder> exOrders = new ArrayList<ExOrder>();
		List<Order> orders = getOrderDao().queryList(userid);
		for (Order order : orders) {
			ExOrder exOrder = new ExOrder();
			exOrder.setUpByPersistant(order);
			exOrders.add(exOrder);
		}
		return exOrders;
	}

	@Override
	public List<ExOrder> queryList(int userid, int page) {
		return queryList(userid, page, PAGESIZE_INT);
	}

	@Override
	public List<ExOrder> queryList(int userid, int page, int pageSize) {
		List<ExOrder> exOrders = new ArrayList<ExOrder>();
		List<Order> orders = getOrderDao().queryList(userid, page, pageSize);
		for (Order order : orders) {
			ExOrder exOrder = new ExOrder();
			exOrder.setUpByPersistant(order);
			exOrders.add(exOrder);
		}
		return exOrders;
	}

	private static final int PAGESIZE_INT = 10;

	@Override
	public List<ExOrder> queryList(int page, boolean lazy) {
		return queryList(page, PAGESIZE_INT, lazy);
	}

	@Override
	public List<ExOrder> queryList(int page, int pageSize, boolean lazy) {
		List<ExOrder> exOrders = new ArrayList<ExOrder>();
		List<Order> orders = getOrderDao().queryList(page, pageSize, lazy);
		for (Order order : orders) {
			ExOrder exOrder = new ExOrder();
			exOrder.setUpByPersistant(order);
			exOrders.add(exOrder);
		}
		return exOrders;
	}

	@Override
	public List<ExOrder> queryList(int page, boolean lazy, boolean isEnd) {
		return queryList(page, PAGESIZE_INT, lazy, isEnd);
	}

	@Override
	public List<ExOrder> queryList(int page, int pageSize, boolean lazy,
			boolean isEnd) {
		List<ExOrder> exOrders = new ArrayList<ExOrder>();
		List<Order> orders = getOrderDao().queryList(page, pageSize, lazy,
				isEnd);
		for (Order order : orders) {
			ExOrder exOrder = new ExOrder();
			exOrder.setUpByPersistant(order);
			exOrders.add(exOrder);
		}
		return exOrders;
	}

	@Override
	public void endOrder(int id) throws Exception {
		Order order = getOrderDao().query(id, false);
		if (order == null)
			throw new Exception("order is not existed!");
		order.setIsEnd(true);
		getOrderDao().update(order);
	}

	@Override
	public void changeLink(int id, String link) throws Exception {
		Order order = getOrderDao().query(id, false);
		if (order == null)
			throw new Exception("order is not existed!");
		order.setLink(link);
		getOrderDao().update(order);
	}

	@Override
	public List<ExOrder> queryListByUser(int userid, boolean isEnd) {
		List<ExOrder> exOrders = new ArrayList<ExOrder>();
		List<Order> orders = getOrderDao().queryList(userid, isEnd);
		for (Order order : orders) {
			ExOrder exOrder = new ExOrder();
			exOrder.setUpByPersistant(order);
			exOrders.add(exOrder);
		}
		return exOrders;
	}
}
