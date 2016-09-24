package com.melonstudio.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.melonstudio.appworks.AppContainer;
import android.content.Context;
import android.os.Bundle;

/**
 * the content that contains text and image
 * 
 * @author Wo
 * 
 */
public class ImageTextContent extends AbstractContent {

	private static final long serialVersionUID = -7525882670439985164L;

	public ImageTextContent() {
		setType(AbstractContent.IMG_TEXT);
	}

	private String desc;
	private String imgPath;

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	/**
	 * TODO 没有处理labels
	 */
	@Override
	public void openUp(Context context) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("name",
					"com.melonstudio.appworks.ImgTextOpenupActivity");
			Bundle b = new Bundle();
			b.putSerializable("imgtext", this);
			jsonObject.put("bundle", b);
			AppContainer appContainer = AppContainer.getInstance();
			if (appContainer == null) {
				AppContainer.setContext(context);
				appContainer = AppContainer.getInstance();
			}
			appContainer.startActivity(jsonObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void copy(AbstractContent content) {
		ImageTextContent imageTextContent = (ImageTextContent) content;
		setComment_no(imageTextContent.getComment_no());
		this.desc = imageTextContent.getDesc();
		this.imgPath = imageTextContent.getImgPath();
		setPraise(imageTextContent.getPraise());
		setTime(imageTextContent.getTime());
		setTitle(imageTextContent.getTitle());
		setType(imageTextContent.getType());
	}

}
