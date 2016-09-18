package com.melonstudio.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

/**
 * 
 * @author guohaosu
 * 
 */
public abstract class AbstractContent implements Cloneable, Serializable {

	private static final long serialVersionUID = 1592006058883832249L;
	public static final String PASSAGE_IMG_TEXT = "image";
	public static final String PASSAGE_PURE_TEXT = "message";
	public static final String PASSAGE_TEXT_TEXT = "recommend";

	public static final int IMG_TEXT = 0;
	public static final int PASSAGE_TEXT = IMG_TEXT + 1;
	public static final int PURE_TEXT = PASSAGE_TEXT + 1;
	public static final int TYPE_COUNT = PURE_TEXT + 1;

	private int id;

	private int type;

	private long time;

	private String title;

	private int praise;

	private int comment_no;

	private int share;

	private String url;

	/**
	 * 在列表中，被点击时的响应
	 */
	public abstract void openUp(Context context);

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public static AbstractContent create(JSONObject jsonObject)
			throws JSONException {
		AbstractContent abstractContent = null;
		if (jsonObject != null) {
			switch (jsonObject.getString("type")) {
			case PASSAGE_PURE_TEXT:
				PureTextContent pureTextContent = new PureTextContent();
				pureTextContent.setContent(jsonObject.getString("message"));
				abstractContent = pureTextContent;
				break;
			case PASSAGE_IMG_TEXT:
				ImageTextContent imageTextContent = new ImageTextContent();
				imageTextContent.setDesc(jsonObject.getString("message"));
				imageTextContent.setImgPath(jsonObject.getString("url"));
				abstractContent = imageTextContent;
				break;
			case PASSAGE_TEXT_TEXT:
				PassageTextContent passageTextContent = new PassageTextContent();
				passageTextContent.setDesc(jsonObject.getString("message"));
				passageTextContent.setImgPath(jsonObject.getString("url"));
				abstractContent = passageTextContent;
				break;
			default:
				break;
			}
			try {
				abstractContent.setShare(jsonObject.getString("share"));
			} catch (Exception e) {
				abstractContent.setShare("-1");
			}
			abstractContent.setPraise(jsonObject.getString("praise"));
			abstractContent.setUrl(jsonObject.getString("original_url"));
			abstractContent.setComment_no(jsonObject.getString("comment_no"));
			abstractContent.setTime(jsonObject.getLong("time"));
			abstractContent.setTitle(jsonObject.getString("title"));
			abstractContent.setId(jsonObject.getInt("id"));
		}
		return abstractContent;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public abstract void copy(AbstractContent content);

	public int getPraise() {
		return praise;
	}

	public void setPraise(int praise) {
		this.praise = praise;
	}

	public void setPraise(String praise) {
		try {
			this.praise = Integer.parseInt(praise);
		} catch (Exception e) {
			this.praise = -1;
		}
	}

	public int getComment_no() {
		return comment_no;
	}

	public void setComment_no(int comment_no) {
		this.comment_no = comment_no;
	}

	public void setComment_no(String comment_no) {
		try {
			this.comment_no = Integer.parseInt(comment_no);
		} catch (Exception e) {
			this.comment_no = -1;
		}
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getShare() {
		return share;
	}

	public void setShare(String share) {
		try {
			this.share = Integer.parseInt(share);
		} catch (Exception e) {
			this.share = -1;
		}
	}

}
