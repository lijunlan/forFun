package com.melonstudio.model;

import android.content.Context;

public class PassageTextContent extends AbstractContent {

	private static final long serialVersionUID = -5259847305687277223L;
	private String desc;
	private String imgPath;

	public PassageTextContent() {
		setType(AbstractContent.PASSAGE_TEXT);
	}

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

	@Override
	public void openUp(Context context) {
		// TODO Auto-generated method stub

	}

	@Override
	public void copy(AbstractContent content) {
		// TODO Auto-generated method stub

	}

}
