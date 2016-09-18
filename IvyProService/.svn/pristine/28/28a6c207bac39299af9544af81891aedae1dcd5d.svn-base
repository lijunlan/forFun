package com.ivypro.ExchangeData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.hibernate.LazyInitializationException;

import com.ivypro.Persistant.Order;
import com.ivypro.Util.Json;

public class ExOrder extends ExAbstract {

	private String id;

	private String category;

	private String isEnd;

	private String link;

	private String sum;

	private String createDate;

	private String title;

	private String request;

	private String userId;

	private String userEmail;

	public ExOrder() {
		id = "";
		category = "";
		isEnd = "";
		link = "";
		sum = "";
		createDate = "";
		title = "";
		request = "";
		userId = "";
		userEmail = "";

	}

	public static final String CATEGORY_STRING_PS = "PS";
	public static final String CATEGORY_STRING_TOEFL = "TOEFL";
	public static final String CATEGORY_STRING_GRE = "GRE";
	public static final String CATEGORY_STRING_SAT = "SAT";
	public static final String CATEGORY_STRING_GMAT = "GMAT";

	public static final short CATEGORY_SHORT_PS = 0;
	public static final short CATEGORY_SHORT_TOEFL = 1;
	public static final short CATEGORY_SHORT_GRE = 2;
	public static final short CATEGORY_SHORT_SAT = 3;
	public static final short CATEGORY_SHORT_GMAT = 4;

	public static String categoryChange(Short category) {
		String c = "unknown";
		switch (category) {
		case CATEGORY_SHORT_PS:
			c = CATEGORY_STRING_PS;
			break;
		case CATEGORY_SHORT_TOEFL:
			c = CATEGORY_STRING_TOEFL;
			break;
		case CATEGORY_SHORT_GRE:
			c = CATEGORY_STRING_GRE;
			break;
		case CATEGORY_SHORT_SAT:
			c = CATEGORY_STRING_SAT;
			break;
		case CATEGORY_SHORT_GMAT:
			c = CATEGORY_STRING_GMAT;
			break;
		}
		return c;
	}

	public static Short categoryChange(String category) {
		Short c = -1;
		switch (category) {
		case CATEGORY_STRING_PS:
			c = CATEGORY_SHORT_PS;
			break;
		case CATEGORY_STRING_TOEFL:
			c = CATEGORY_SHORT_TOEFL;
			break;
		case CATEGORY_STRING_GRE:
			c = CATEGORY_SHORT_GRE;
			break;
		case CATEGORY_STRING_SAT:
			c = CATEGORY_SHORT_SAT;
			break;
		case CATEGORY_STRING_GMAT:
			c = CATEGORY_SHORT_GMAT;
			break;
		}
		return c;
	}

	public static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yy/MM/dd HH:mm:ss");

	public static String formatTime(String time) {
		return dateFormat.format(new Date(Long.parseLong(time)));
	}

	@Override
	public String toJson() {
		SuperMap map = new SuperMap();
		map.put("id", id);
		map.put("category", category);
		if ("true".equals(isEnd)) {
			map.put("isEnd", "完成");
		} else if ("false".equals(isEnd)) {
			map.put("isEnd", "处理中");
		} else {
			map.put("isEnd", isEnd);
		}
		map.put("link", link);
		map.put("sum", sum);
		map.put("createDate", createDate);
		map.put("title", title);
		map.put("request", request);
		map.put("userId", userId);
		map.put("userEmail", userEmail);
		return map.finishByJson();
	}

	@Override
	public void setUpByJson(String json) {
		Map<String, String> map = Json.getMap(json);
		id = map.get("id");
		category = map.get(category);
		isEnd = map.get("isEnd");
		link = map.get("link");
		sum = map.get("sum");
		createDate = map.get("createDate");
		title = map.get("title");
		request = map.get("request");
		userId = map.get("userId");
		userEmail = map.get("userEmail");
	}

	@Override
	public Object toPersistant() {
		Order order = new Order();
		if (id != null && !"null".equals(id))
			order.setId(Integer.valueOf(id));
		order.setCategory(categoryChange(category));
		order.setCreateDate(String.valueOf(Calendar.getInstance()
				.getTimeInMillis()));
		order.setIsEnd(Boolean.valueOf(isEnd));
		order.setLink(link);
		order.setRequest(request);
		order.setSum(Float.valueOf(sum));
		order.setTitle(title);
		return order;
	}

	@Override
	public void setUpByPersistant(Object persistant) {
		if (persistant == null)
			return;
		Order order = (Order) persistant;
		id = String.valueOf(order.getId());
		category = categoryChange(order.getCategory());
		isEnd = String.valueOf(order.getIsEnd());
		link = order.getLink();
		sum = String.valueOf(order.getSum());
		createDate = formatTime(order.getCreateDate());
		title = order.getTitle();
		request = order.getRequest();
		try {
			userId = String.valueOf(order.getCreateUser().getId());
			userEmail = order.getCreateUser().getEmail();
		} catch (LazyInitializationException e) {

		}
	}

	@Override
	public void setUpByMap(Map<String, String> map) {
		id = map.get("id");
		category = map.get("category");
		isEnd = map.get("isEnd");
		link = map.get("link");
		sum = map.get("sum");
		createDate = map.get("createDate");
		title = map.get("title");
		request = map.get("request");
		userId = map.get("userId");
		userEmail = map.get("userEmail");
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getIsEnd() {
		return isEnd;
	}

	public void setIsEnd(String isEnd) {
		this.isEnd = isEnd;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getSum() {
		return sum;
	}

	public void setSum(String sum) {
		this.sum = sum;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
}
