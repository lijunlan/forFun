package com.ivypro.Handle.Service;

import java.util.Calendar;

import com.ivypro.ExchangeData.ExOrder;
import com.ivypro.ExchangeData.ExUser;
import com.ivypro.ExchangeData.SuperMap;
import com.ivypro.Service.OrderService;
import com.ivypro.Util.MsgUtil;

public class CreateOrderService extends MsgService {

	private OrderService orderService;

	public OrderService getOrderService() {
		return orderService;
	}

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	@Override
	protected boolean checkData() {
		return getData().containsKey("category")
				&& getData().containsKey("link")
				&& getData().containsKey("sum")
				&& getData().containsKey("title")
				&& getData().containsKey("request")
				&& getData().containsKey("mark");
	}

	@Override
	public void doit() {
		String mark = getData().get("mark");
		ExUser exUser = null;
		try {
			exUser = getUserMarkService().queryUser(mark);
		} catch (Exception e1) {
			e1.printStackTrace();
			setResMsg(MsgUtil.getErrorMsg(e1.getMessage()));
			return;
		}
		String userId = exUser.getId();
		SuperMap map = new SuperMap();
		String category = getData().get("category");
		String link = getData().get("link");
		String sum = getData().get("sum");
		String title = getData().get("title");
		String request = getData().get("request");
		map.put("category", category);
		map.put("link", link);
		map.put("sum", sum);
		map.put("createDate", Calendar.getInstance().getTimeInMillis());
		map.put("title", title);
		map.put("request", request);
		map.put("userId", userId);
		map.put("isEnd", false);
		ExOrder exOrder = new ExOrder();
		exOrder.setUpByMap(map.finish());
		try {
			getOrderService().create(exOrder);
		} catch (Exception e) {
			e.printStackTrace();
			setResMsg(MsgUtil.getErrorMsg(e.getMessage()));
			return;
		}
		setResMsg(MsgUtil.getSuccessMsg("create order successfully"));
	}
}
