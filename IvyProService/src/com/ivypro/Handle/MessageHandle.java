package com.ivypro.Handle;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import com.ivypro.Handle.Service.MsgService;
import com.ivypro.Util.ConfigurationXmlUtil;
import com.ivypro.Util.Json;
import com.ivypro.Util.LogUtil;
import com.ivypro.Util.MsgUtil;

public class MessageHandle {

	/**
	 * deal with the application
	 * 
	 * @param rq
	 * @param rp
	 * @param context
	 */
	public static void deal(HttpServletRequest rq, HttpServletResponse rp,
			ApplicationContext context) {
		MessageHandle mHandle = new MessageHandle(rq, rp, context);
		mHandle.start();
		mHandle.doit();
	}

	private HttpServletRequest req;
	private HttpServletResponse resp;
	private MsgService util;
	private ApplicationContext applicationContext;
	private Map<String, String> data;

	private MessageHandle(HttpServletRequest rq, HttpServletResponse rp,
			ApplicationContext context) {
		req = rq;
		resp = rp;
		applicationContext = context;
	}

	/**
	 * get json data from the httpRequest's content
	 * 
	 * @param rq
	 * @return
	 */
	private static String getJson(HttpServletRequest rq) {
		InputStream in = null;
		StringBuffer sBuffer = new StringBuffer();
		byte[] buffer = new byte[1024];
		try {
			in = rq.getInputStream();
			while (in.read(buffer) > 0) {
				sBuffer.append(new String(buffer));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		LogUtil.info("receive->>>" + sBuffer.toString(), MessageHandle.class);
		// System.out.println(sBuffer.toString());
		return sBuffer.toString();
	}

	/**
	 * mission start<br/>
	 * choose method to deal with the application by the <b>style</b> field
	 */
	private void start() {
		data = Json.getMap(getJson(req));
		Map<String, Map<String, String>> configData = ConfigurationXmlUtil
				.getInstance().getConfigData();
		if (data.containsKey("style") && data.containsKey("method")) {
			String style = data.get("style");
			String method = data.get("method");
			if (configData.containsKey(style)) {
				Map<String, String> methodData = configData.get(style);
				if (methodData.containsKey(method)) {
					util = (MsgService) applicationContext.getBean(methodData
							.get(method));
					data.put("IP", req.getRemoteAddr());
				} else {
					returnError(MsgUtil.getErrorMsg("method is not acurate"));
				}
			} else {
				returnError(MsgUtil.getErrorMsg("style is not acurate"));
			}

		} else {
			returnError(MsgUtil
					.getErrorMsg("the style or method field is not included"));
		}
		// data = Json.getMap(getJson(req));
		// if (data.containsKey("style") && data.containsKey("method")) {
		// String style = data.get("style");
		// String method = data.get("method");
		// switch (style) {
		// case Protocol.STYLE_USER:
		// userProcess(method);
		// break;
		// case Protocol.STYLE_ORDER:
		// orderProcess(method);
		// break;
		// case Protocol.STYLE_MANAGER:
		// managerProcess(method);
		// break;
		// default:
		// returnError(MsgUtil.getErrorMsg("style is not acurate"));
		// break;
		// }
		//
		// } else {
		// returnError(MsgUtil
		// .getErrorMsg("the style or method field is not included"));
		// }
	}

	//
	// private void userProcess(String method) {
	// // TODO
	// switch (method) {
	// case Protocol.METHOD_USER_LOGIN:
	// util = (MsgService) applicationContext.getBean("loginService");
	// break;
	// // case Protocol.METHOD_CHANGE_USER_INFO:
	// // util = (MsgService) applicationContext
	// // .getBean("changeUserInfoService");
	// // break;
	// case Protocol.METHOD_USER_LOGOUT:
	// util = (MsgService) applicationContext.getBean("logoutService");
	// break;
	// case Protocol.METHOD_GET_USER_INFO:
	// util = (MsgService) applicationContext
	// .getBean("getUserInfoService");
	// break;
	// case Protocol.METHOD_REGISTER:
	// util = (MsgService) applicationContext.getBean("registerService");
	// break;
	// default:
	// returnError(MsgUtil.getErrorMsg("method is not acurate"));
	// break;
	// }
	// }
	//
	// private void orderProcess(String method) {
	// // TODO
	// switch (method) {
	// case Protocol.METHOD_ORDER_CREATE:
	// util = (MsgService) applicationContext
	// .getBean("createOrderService");
	// break;
	// // case Protocol.METHOD_ORDER_DELETE:
	// // util = (MsgService) applicationContext.getBean("deleteAppService");
	// // break;
	// case Protocol.METHOD_ORDER_CHANGE:
	// util = (MsgService) applicationContext
	// .getBean("changeOrderService");
	// break;
	// case Protocol.METHOD_ORDER_GET_LIST:
	// util = (MsgService) applicationContext
	// .getBean("getOrderListService");
	// break;
	// case Protocol.METHOD_ORDER_DONE:
	// util = (MsgService) applicationContext.getBean("endOrderService");
	// break;
	// default:
	// returnError(MsgUtil.getErrorMsg("method is not acurate"));
	// break;
	// }
	// }
	//
	// private void managerProcess(String method) {
	// // TODO
	// switch (method) {
	// case Protocol.METHOD_MANAGER_LOGIN:
	// util = (MsgService) applicationContext
	// .getBean("managerLoginService");
	// break;
	// // case Protocol.METHOD_CHANGE_USER_INFO:
	// // util = (MsgService) applicationContext
	// // .getBean("changeUserInfoService");
	// // break;
	// default:
	// returnError(MsgUtil.getErrorMsg("method is not acurate"));
	// break;
	// }
	// }

	/**
	 * use relevant tool to process it<br/>
	 * return error message if data is incomplete
	 */
	private void doit() {
		if (util != null) {
			if (util.setDataMap(data)) {
				util.doit();
				returnMsg(util.getResponseMsg());
			} else {
				returnError(MsgUtil.getErrorMsg("data is incomplete"));
			}
		}
	}

	/**
	 * return message through httpResponse
	 * 
	 * @param msg
	 */
	private void returnMsg(String msg) {
		resp.setCharacterEncoding("UTF-8");
		resp.setHeader("Content-type", "text/html;charset=UTF-8");
		try {
			OutputStream stream = resp.getOutputStream();
			// System.out.println(msg);
			LogUtil.info("send---->>>" + msg, MessageHandle.class);
			stream.write(msg.getBytes("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * return error message
	 * 
	 * @param msg
	 */
	private void returnError(String msg) {
		returnMsg(msg);
	}
}
