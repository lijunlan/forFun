package com.ivypro.Handle.Service;

import java.util.List;

import com.ivypro.ExchangeData.ExOrder;
import com.ivypro.ExchangeData.ExUser;
import com.ivypro.Service.OrderService;
import com.ivypro.Util.Json;
import com.ivypro.Util.MsgUtil;

public class GetOrderListService extends MsgService {

	private OrderService orderService;

	public OrderService getOrderService() {
		return orderService;
	}

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	@Override
	protected boolean checkData() {
		return (getData().containsKey("mark") && getData().containsKey("isEnd"))
				|| (getData().containsKey("mark") && getData().containsKey(
						"page"))
				|| getData().containsKey("mark")
				|| (getData().containsKey("isEnd") && getData().containsKey(
						"page")) || getData().containsKey("page");
	}

	@Override
	public void doit() {
		List<ExOrder> exOrders = null;
		if (getData().containsKey("mark")) {
			String mark = getData().get("mark");
			ExUser exUser = null;
			try {
				exUser = getUserMarkService().queryUser(mark);
			} catch (Exception e) {
				e.printStackTrace();
				setResMsg(MsgUtil.getErrorMsg(e.getMessage()));
				return;
			}
			if (getData().containsKey("isEnd")) {
				boolean isEnd = Boolean.valueOf(getData().get("isEnd"));
				int userId = Integer.valueOf(exUser.getId());
				exOrders = getOrderService().queryListByUser(userId, isEnd);
			} else if (getData().containsKey("page")) {
				int userId = Integer.valueOf(exUser.getId());
				int page = Integer.valueOf(getData().get("page"));
				exOrders = getOrderService().queryList(userId, page);
			} else {
				int userId = Integer.valueOf(exUser.getId());
				exOrders = getOrderService().queryList(userId);
			}
		} else if (getData().containsKey("isEnd")) {
			boolean isEnd = Boolean.valueOf(getData().get("isEnd"));
			int page = Integer.valueOf(getData().get("page"));
			exOrders = getOrderService().queryList(page, true, isEnd);
		} else {
			int page = Integer.valueOf(getData().get("page"));
			exOrders = getOrderService().queryList(page, true);
		}
		try {
			setResMsg(Json.getJsonByEx(exOrders));
		} catch (Exception e) {
			e.printStackTrace();
			setResMsg(MsgUtil.getErrorMsg("service error"));
		}
	}
}
