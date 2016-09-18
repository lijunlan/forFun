package com.melonstudio.model;

/**
 * 
 * @author Wo
 * 
 */
public class SlideMenuItem {
	private String text;
	private int drawable;

	public SlideMenuItem(String text, int drawable) {
		this.drawable = drawable;
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getDrawable() {
		return drawable;
	}

	public void setDrawable(int drawable) {
		this.drawable = drawable;
	}

}
