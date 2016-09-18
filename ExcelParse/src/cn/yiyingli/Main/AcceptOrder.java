package cn.yiyingli.Main;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class AcceptOrder {

	private static final String APPLICATION_JSON = "application/json";

	private static final String CONTENT_TYPE_TEXT_JSON = "text/json";

	public static JSONObject getOrderList(String uid, long teacherId, int page) {
		HttpClient httpClient = HttpClients.createDefault();
		HttpPost post = new HttpPost(
				"http://service.1yingli.cn/yiyingliService/manage");
		post.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);

		JSONObject send = new JSONObject();
		send.put("style", "order");
		send.put("method", "getListByTeacher");
		send.put("uid", uid);
		send.put("page", page + "");
		send.put("teacherId", teacherId + "");
		StringEntity se = new StringEntity(send.toString(), "utf-8");
		System.out.println(send.toString());
		se.setContentType(CONTENT_TYPE_TEXT_JSON);
		se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
				APPLICATION_JSON));
		post.setEntity(se);
		String result = null;
		try {
			HttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity(), "utf-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(result);
		return JSONObject.fromObject(result);
	}

	public static void acceptOrder(String orderId, String uid, long teacherId)
			throws UnsupportedEncodingException {
		HttpClient httpClient = HttpClients.createDefault();
		HttpPost post = new HttpPost(
				"http://service.1yingli.cn/yiyingliService/manage");
		post.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);

		JSONObject send = new JSONObject();
		send.put("style", "order");
		send.put("method", "acceptOrder");
		send.put("orderId", orderId);
		send.put("uid", uid);
		send.put("teacherId", teacherId + "");
		StringEntity se = new StringEntity(send.toString(), "utf-8");
		System.out.println(send.toString());
		se.setContentType(CONTENT_TYPE_TEXT_JSON);
		se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
				APPLICATION_JSON));
		post.setEntity(se);
		String result = null;
		try {
			HttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity(), "utf-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(result);
	}

	public static void ensureTime(String orderId, String uid, long teacherId)
			throws UnsupportedEncodingException {
		HttpClient httpClient = HttpClients.createDefault();
		HttpPost post = new HttpPost(
				"http://service.1yingli.cn/yiyingliService/manage");
		post.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);

		JSONObject send = new JSONObject();
		send.put("style", "order");
		send.put("method", "ensureTime");
		send.put("okTime", "请加微信好友：clove930423 备注姓名+订单号后四位");
		send.put("orderId", orderId);
		send.put("uid", uid);
		send.put("teacherId", teacherId + "");
		StringEntity se = new StringEntity(send.toString(), "utf-8");
		System.out.println(send.toString());
		se.setContentType(CONTENT_TYPE_TEXT_JSON);
		se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
				APPLICATION_JSON));
		post.setEntity(se);
		String result = null;
		try {
			HttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity(), "utf-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(result);
	}

	public static void endService(String orderId, String uid, long teacherId) {
		HttpClient httpClient = HttpClients.createDefault();
		HttpPost post = new HttpPost(
				"http://service.1yingli.cn/yiyingliService/manage");
		post.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);

		JSONObject send = new JSONObject();
		send.put("style", "order");
		send.put("method", "finishOrder");
		send.put("orderId", orderId);
		send.put("uid", uid);
		send.put("teacherId", teacherId + "");
		StringEntity se = new StringEntity(send.toString(), "utf-8");
		System.out.println(send.toString());
		se.setContentType(CONTENT_TYPE_TEXT_JSON);
		se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
				APPLICATION_JSON));
		post.setEntity(se);
		String result = null;
		try {
			HttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity(), "utf-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(result);
	}

	public static void send(int weight, String activityDes, long teacherId) {
		HttpClient httpClient = HttpClients.createDefault();
		HttpPost post = new HttpPost(
				"http://service.1yingli.cn/yiyingliService/manage");
		post.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);

		JSONObject send = new JSONObject();
		send.put("style", "manager");
		send.put("method", "createActivityContent");
		send.put("mid", "475ab59a-b953-4da7-9cb0-194009836af9");
		send.put("pagesId", "29");
		send.put("weight", weight);
		send.put("contentStyle", 1);
		send.put("activityDes", activityDes);
		send.put("teacherId", teacherId);
		StringEntity se = new StringEntity(send.toString(), "utf-8");
		System.out.println(send.toString());
		se.setContentType(CONTENT_TYPE_TEXT_JSON);
		se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
				APPLICATION_JSON));
		post.setEntity(se);
		String result = null;
		try {
			HttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity(), "utf-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(result);
	}

	public static void viewPassage(String uid, long passageId) {
		HttpClient httpClient = HttpClients.createDefault();
		HttpPost post = new HttpPost(
				"http://service.1yingli.cn/yiyingliService/manage");
		post.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);

		JSONObject send = new JSONObject();
		send.put("style", "function");
		send.put("method", "getPassage");
		send.put("uid", uid + "");
		send.put("pid", passageId + "");
		StringEntity se = new StringEntity(send.toString(), "utf-8");
		System.out.println(send.toString());
		se.setContentType(CONTENT_TYPE_TEXT_JSON);
		se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
				APPLICATION_JSON));
		post.setEntity(se);
		String result = null;
		try {
			HttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity(), "utf-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(result);
	}

	public static void likeTeacher(String uid, long teacherId) {
		HttpClient httpClient = HttpClients.createDefault();
		HttpPost post = new HttpPost(
				"http://service.1yingli.cn/yiyingliService/manage");
		post.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);

		JSONObject send = new JSONObject();
		send.put("style", "user");
		send.put("method", "likeTeacher");
		send.put("uid", uid);
		send.put("teacherId", teacherId + "");
		StringEntity se = new StringEntity(send.toString(), "utf-8");
		System.out.println(send.toString());
		se.setContentType(CONTENT_TYPE_TEXT_JSON);
		se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
				APPLICATION_JSON));
		post.setEntity(se);
		String result = null;
		try {
			HttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity(), "utf-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(result);
	}

	public static void registerUser(long i) {
		HttpClient httpClient = HttpClients.createDefault();
		HttpPost post = new HttpPost(
				"http://service.1yingli.cn/yiyingliService/manage");
		post.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);

		JSONObject send = new JSONObject();
		send.put("style", "manager");
		send.put("method", "registerUser");
		send.put("mid", "084ca92f-325b-459b-ad97-415b61be3ea9");
		send.put("username", i + "@qq.com");
		send.put(
				"password",
				"650dfd3b13f8132a80b82fa4a09588fa7be2ff8208d054a68cada00e45a1cbe108ad35efefcd172787f608e29a41e956f7ea3cf8d95d79f848a1d4e346d9dcec648a85aa9f70ad7f150ddd4054c02040e3903134e5f9996a6a463f248697e166c1e65788ca35258e7f8f422950f177f16ceb2001717008b105f20833f362a7b3");
		send.put("nickName", "");
		StringEntity se = new StringEntity(send.toString(), "utf-8");
		System.out.println(send.toString());
		se.setContentType(CONTENT_TYPE_TEXT_JSON);
		se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
				APPLICATION_JSON));
		post.setEntity(se);
		String result = null;
		try {
			HttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity(), "utf-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(result);
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		// long uids[] = {};88226
		// for(long uid:uids){
		// for(int i = 777;i>=500;i--){
		// registerUser(i);
		// }
		int tids[] = { 670, 847, 998, 725, 689, 848 };
		for (long i = 102531; i <= 102809; i++) {
			// viewPassage(String.valueOf(i), 324);
			// try {
			// Thread.sleep(new Random().nextInt(3000));
			// } catch (InterruptedException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// }
			for (int tid : tids) {
				if (tid == 725) {

				} else if (tid == 670) {

				} else {
					int o = new Random().nextInt(10);
					if (o < 5)
						continue;
				}
				likeTeacher(String.valueOf(i), tid);
				try {
					Thread.sleep(new Random().nextInt(5000));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(new Random().nextInt(10000));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// String[] ads = { "主席团", "战略VC部", "产品运营部", "技术部", "战略VC部成员",
		// "产品运营部成员", "技术部成员", "设计部成员" };
		// int[][] tids = { { 568, 831, 901, 838, 865 }, { 814, 959, 903, 906,
		// 911 }, { 874, 821, 872, 915, 873 },
		// { 913, 939, 941, 919 }, { 960, 940, 832, 827, 816, 834, 868, 892,
		// 909, 867 },
		// { 912, 824, 842, 864, 896, 565, 927, 839, 885, 828, 822 },
		// { 846, 860, 818, 817, 907, 858, 914, 870, 869, 888 }, { 853, 900,
		// 899, 825 } };
		// int c = 0;
		// for (int i = 0; i < ads.length; i++) {
		// for (int j = 0; j < tids[i].length; j++) {
		// send(1000-c, ads[i], tids[i][j]);
		// c++;
		// }
		// }
		// String[] orderIds = new String[] { "2016063100005646" ,
		// "2016063100005645" , "2016063100005644"};
		// for (int i = 0; i < orderIds.length; i++) {
		// acceptOrder(orderIds[i]);
		// ensureTime(orderIds[i]);
		// }
		// String uid = "21a36a72-a3e8-4ac1-ae5b-d0f73699c4de";
		// long teacherId = 322L;
		// String uid = "4dff6cb3-b717-4fc9-bfe5-97d70b2a55ff";
		// long teacherId = 354L;
		// for (int i = 1; i <= 20; i++) {
		// JSONObject data = getOrderList(uid, teacherId, i);
		// if (data.getString("state").equals("success")) {
		// JSONArray list = data.getJSONArray("data");
		// for (int j = 0; j < list.size(); j++) {
		// JSONObject batch = list.getJSONObject(j);
		// JSONArray orders = batch.getJSONArray("orders");
		// for (int z = 0; z < orders.size(); z++) {
		// String orderId = orders.getJSONObject(z).getString("orderId");
		// acceptOrder(orderId, uid, teacherId);
		// ensureTime(orderId, uid, teacherId);
		// // endService(orderId, uid, teacherId);
		// }
		// }
		// }
		// }
	}
}
