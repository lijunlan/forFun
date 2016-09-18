package com.melonstudio.model;

import android.content.Context;

public class PureTextContent extends AbstractContent {

	private static final long serialVersionUID = 3471184898681505929L;

	public PureTextContent() {
		setType(AbstractContent.PURE_TEXT);
	}

	/**
	 * content of this content
	 */
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public void openUp(Context context) {
		// TODO Auto-generated method stub
	}

	@Override
	public void copy(AbstractContent content) {
		PureTextContent pureTextContent = (PureTextContent) content;
		this.content = pureTextContent.getContent();
		setTime(pureTextContent.getTime());
		setComment_no(pureTextContent.getComment_no());
		setId(pureTextContent.getId());
		setPraise(pureTextContent.getPraise());
		setTitle(pureTextContent.getTitle());
		setType(pureTextContent.getType());
		setUrl(pureTextContent.getUrl());
	}

}
