package com.ivypro.Handle.Service;

import com.ivypro.ExchangeData.ExOrder;
import com.ivypro.ExchangeData.ExUser;
import com.ivypro.Service.OrderService;
import com.ivypro.Util.MsgUtil;

public class ChangeOrderService extends MsgService {

	private OrderService orderService;

	public OrderService getOrderService() {
		return orderService;
	}

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	@Override
	protected boolean checkData() {
		return getData().containsKey("id") && getData().containsKey("link")
				&& getData().containsKey("mark");
	}

	@Override
	public void doit() {
		int id = Integer.valueOf(getData().get("id"));
		String link = getData().get("link");
		String mark = getData().get("mark");
		try {
			ExUser exUser = getUserMarkService().queryUser(mark);
			ExOrder exOrder = getOrderService().query(id, true);
			if (!(exUser.getId().equals(exOrder.getUserId()))) {
				setResMsg(MsgUtil.getErrorMsg("The order is not own to you"));
				return;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			setResMsg(MsgUtil.getErrorMsg(e1.getMessage()));
			return;
		}
		try {
			getOrderService().changeLink(id, link);
		} catch (Exception e) {
			e.printStackTrace();
			setResMsg(MsgUtil.getErrorMsg("order is not existed"));
			return;
		}
		setResMsg(MsgUtil.getSuccessMsg("link changed"));
	}

}
