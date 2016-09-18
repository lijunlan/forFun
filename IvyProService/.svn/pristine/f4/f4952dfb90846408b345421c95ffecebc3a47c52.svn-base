package com.ivypro.Handle.Service;

import com.ivypro.Service.OrderService;
import com.ivypro.Util.MsgUtil;

public class EndOrderService extends MsgService {

	private OrderService orderService;

	public OrderService getOrderService() {
		return orderService;
	}

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	@Override
	protected boolean checkData() {
		return getData().containsKey("id");
	}

	@Override
	public void doit() {
		try {
			getOrderService().endOrder(Integer.valueOf(getData().get("id")));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			setResMsg(MsgUtil.getErrorMsg("id is not accurate"));
			return;
		} catch (Exception e) {
			e.printStackTrace();
			setResMsg(MsgUtil.getErrorMsg("order is not existed"));
			return;
		}
		setResMsg(MsgUtil.getSuccessMsg("order ended"));
	}

}
