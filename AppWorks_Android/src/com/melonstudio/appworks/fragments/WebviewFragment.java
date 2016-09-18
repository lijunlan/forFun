package com.melonstudio.appworks.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockFragment;
import com.melonstudio.appworks.AppContainer;
import com.melonstudio.appworks.R;
import com.melonstudio.model.AbstractContent;
import com.melonstudio.util.ConfigureManager;
import com.melonstudio.util.MyLog;

public class WebviewFragment extends SherlockFragment {

	private static final String TAG = "com.melonstudio.appworks.fragments.WebviewFragment";

	private void log(String msg) {
		MyLog.log(TAG, msg);
	}

	private WebView webView;
	private ImageView back;
	private ImageView go;
	private ImageView close;
	private ImageView refresh;

	private boolean isTwoPane;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		log("onAttach in WebviewFragment");
	}

	private AppContainer appContainer;
	private ConfigureManager configureManager;

	private ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		log("onCreate in WebviewFragment");
		try {
			isTwoPane = getArguments().getBoolean("isTwoPane");
			log("isTwoPane is : " + isTwoPane);
		} catch (Exception e) {
			log(e.toString());
		}
		AppContainer.setContext(getSherlockActivity());
		appContainer = AppContainer.getInstance();
		appContainer.registService(this);
		configureManager = ConfigureManager.getInstance(getSherlockActivity());
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_webview_layout,
				container, false);
		back = (ImageView) view.findViewById(R.id.webview_img_back);
		go = (ImageView) view.findViewById(R.id.webview_img_forward);
		refresh = (ImageView) view.findViewById(R.id.webview_img_refresh);

		webView = (WebView) view.findViewById(R.id.fragment_webview);
		webView.setWebViewClient(new UMWebviewClient());
		webView.setWebChromeClient(new UMWeChromeClient());
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings()
				.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		if (!isTwoPane) {
			close = (ImageView) view.findViewById(R.id.webview_img_close);
			AbstractContent abstractContent = (AbstractContent) getArguments()
					.getSerializable("abstractContent");
			webView.loadUrl(abstractContent.getUrl());
		}

		initButtons();
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		if (appContainer == null) {
			AppContainer.setContext(getSherlockActivity());
			appContainer = AppContainer.getInstance();
		}
		appContainer.registService(this);
		if (configureManager == null) {
			configureManager = ConfigureManager
					.getInstance(getSherlockActivity());
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		appContainer.unregistService(this);
	}

	private class UMWebviewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			webView.loadUrl(url);
			return true;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			if (!isTwoPane) {
				if (progressDialog == null) {
					progressDialog = new ProgressDialog(getSherlockActivity());
					progressDialog.setTitle("请稍候");
					progressDialog.setMessage("正在加载...");
					progressDialog.show();
				} else if (!progressDialog.isShowing()) {
					progressDialog.show();
				}
			}
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			if (!isTwoPane) {
				progressDialog.dismiss();
			}
		}
	}

	private class UMWeChromeClient extends WebChromeClient {

		@Override
		public void onReceivedTitle(WebView view, String title) {
			super.onReceivedTitle(view, title);
			getSherlockActivity().getSupportActionBar().setTitle(title);
		}

	}

	private void initButtons() {
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				webView.goBack();
			}
		});
		go.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				webView.goForward();
			}
		});
		refresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				webView.reload();
			}
		});
		if (!isTwoPane && close != null) {
			close.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					getSherlockActivity().getSupportFragmentManager()
							.popBackStack();
				}
			});
		}
	}

	public void loadUrl(AbstractContent abstractContent) {
		webView.loadUrl(abstractContent.getUrl());
	}
}
