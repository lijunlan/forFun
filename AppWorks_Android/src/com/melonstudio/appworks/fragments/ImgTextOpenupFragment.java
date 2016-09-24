package com.melonstudio.appworks.fragments;

import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.melonstudio.appworks.R;
import com.melonstudio.util.MyLog;

public class ImgTextOpenupFragment extends SherlockFragment {

	private static final String TAG = "com.melonstudio.appworks.fragments.ImgTextOpenupFragment";

	private void log(String msg) {
		MyLog.log(TAG, msg);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		log("onCreate in ImgTextOpenupFragment");
		super.onCreate(savedInstanceState);
	}

	@ViewInject(id = R.id.imgtext_write_comment_label, click = "write_comment")
	private ImageView comments_imageView;
	@ViewInject(id = R.id.imgtext_write_comment, click = "write_comment")
	private Button write_comment;
	@ViewInject(id = R.id.imgtext_date)
	private TextView dateTextView;

	@ViewInject(id = R.id.imgtext_img)
	private ImageView imageView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		log("onCreateView in ImgTextOpenupFragment");
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.imgtext_openup_layout, container,
				false);

		comments_imageView = (ImageView) view
				.findViewById(R.id.imgtext_write_comment_label);
		write_comment = (Button) view.findViewById(R.id.imgtext_write_comment);
		dateTextView = (TextView) view.findViewById(R.id.imgtext_date);

		return view;
	}

	@Override
	public void onPause() {
		log("onPause in ImgTextOpenupFragment");
		super.onPause();
	}

	@Override
	public void onStart() {
		log("onStart in ImgTextOpenupFragment");
		super.onStart();
	}

	@Override
	public void onDestroy() {
		log("onDestroy in ImgTextOpenupFragment");
		super.onDestroy();
	}
}
