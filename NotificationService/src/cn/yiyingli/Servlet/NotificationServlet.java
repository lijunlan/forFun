package cn.yiyingli.Servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.yiyingli.Util.Json;
import cn.yiyingli.Util.LogUtil;
import nl.justobjects.pushlet.core.Dispatcher;
import nl.justobjects.pushlet.core.Event;

@SuppressWarnings("serial")
public class NotificationServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// System.out.println("get!");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Map<String, String> data = Json.getMap(getJson(req));
		String castType = data.get("castType");
		String json = data.get("data");
		if ("UNI".equals(castType)) {
			// TODO GET SESSIONID
			uniCast(json, "000");
		} else if ("MULTI".equals(castType)) {
			String group = data.get("group");
			multiCast(json, group);
		} else if ("BROAD".equals(castType)) {
			broadcast(json);
		}
		String msg = "{'state':'success'}";
		resp.setCharacterEncoding("UTF-8");
		resp.setHeader("Content-type", "text/html;charset=UTF-8");
		try {
			OutputStream stream = resp.getOutputStream();
			LogUtil.info("send---->>>" + msg, NotificationServlet.class);
			stream.write(msg.getBytes("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		try {
			String json = URLDecoder.decode(sBuffer.toString(), "utf-8");
			LogUtil.info("receive->>>" + json, NotificationServlet.class);
			return json;
		} catch (UnsupportedEncodingException e) {
			LogUtil.info("receive->>>" + sBuffer.toString(),
					NotificationServlet.class);
			e.printStackTrace();
		}
		return "";
	}

	private void uniCast(String json, String sessionId) {
		Event event = Event.createDataEvent("/yiyingli/none");
		event.setField("dataJson", json);
		Dispatcher.getInstance().unicast(event, sessionId); // 向SESSIONID为sessionId的用户推送
		LogUtil.info(
				"Uni Cast-->SessionID: " + sessionId + "content-->" + json,
				this.getClass());
	}

	private void multiCast(String json, String group) {
		Event event = Event.createDataEvent("/yiyingli/" + group);
		event.setField("dataJson", json);
		Dispatcher.getInstance().multicast(event); // 向所有和event名称匹配的事件推送
		LogUtil.info("Multi Cast-->Group: " + group + "-->content-->" + json,
				this.getClass());
	}

	private void broadcast(String json) {
		Event event = Event.createDataEvent("/yiyingli"); // 向所有的事件推送，不要求和这的名称匹配
		event.setField("dataJson", json);
		Dispatcher.getInstance().broadcast(event);
		LogUtil.info("Broad Cast-->content-->" + json, this.getClass());
	}
}
