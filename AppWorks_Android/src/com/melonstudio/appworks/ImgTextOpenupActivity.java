package com.melonstudio.appworks;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.annotation.view.ViewInject;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.melonstudio.clientconnector.ContentParameter;
import com.melonstudio.model.ImageTextContent;
import com.melonstudio.util.ConfigureManager;
import com.melonstudio.util.MyLog;
import com.melonstudio.util.TimeUtil;

public class ImgTextOpenupActivity extends SherlockActivity implements
		IActivity {

	private static final String TAG = "com.melonstudio.appworks.ImgTextOpenupActivity";

	private FinalBitmap finalBitmap;

	@ViewInject(id = R.id.imgtext_write_comment_label, click = "write_comment")
	private ImageView comments_imageView;
	@ViewInject(id = R.id.imgtext_write_comment, click = "write_comment")
	private Button write_comment;
	@ViewInject(id = R.id.imgtext_date)
	private TextView dateTextView;

	@ViewInject(id = R.id.imgtext_img)
	private ImageView imageView;

	private Menu menu;

	private ImageTextContent imageTextContent;

	private boolean liked = false;

	private AppContainer appContainer;
	private ConfigureManager configureManager;

	private int w;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imgtext_openup_layout);

		FinalActivity.initInjectedView(this);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		appContainer = AppContainer.getInstance();
		configureManager = ConfigureManager
				.getInstance(getApplicationContext());
		log("onCreate in ImgTextOpenupActivity");
		configureManager.setContentPopAcitivity(this.getClass().getName());
		log(configureManager.getContentPopAcitivity());

		finalBitmap = FinalBitmap.create(getApplicationContext());

		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		w = size.x;
		LayoutParams layoutParams = new LayoutParams(w, w);
		imageView.setLayoutParams(layoutParams);

		Intent intent = getIntent();
		processIntent(intent);
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (appContainer == null) {
			AppContainer.setContext(getApplicationContext());
			appContainer = AppContainer.getInstance();
		}
		appContainer.registService(this);

		if (configureManager == null) {
			configureManager = ConfigureManager
					.getInstance(getApplicationContext());
		}
		check_praise();
	}

	@Override
	protected void onDestroy() {
		log("onDestroy in ImgTextOpenupActivity");
		appContainer.unregistService(this);
		finalBitmap.closeCache();
		super.onDestroy();
	}

	/**
	 * 处理从Intent中收到的信息
	 * 
	 * @param intent
	 */
	private void processIntent(Intent intent) {
		Bundle bundle = intent.getBundleExtra("bundle");
		imageTextContent = (ImageTextContent) bundle.getSerializable("imgtext");
		finalBitmap.display(imageView,
				configureManager.check_user_img(imageTextContent.getImgPath()));

		getSupportActionBar().setTitle(imageTextContent.getTitle());

		TextView content = (TextView) findViewById(R.id.imgtext_content);
		content.setText(imageTextContent.getDesc());

		dateTextView
				.setText(TimeUtil.generalFormat(imageTextContent.getTime()));

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			return true;
		case R.id.imgtext_openup_menu_like:
			if (liked == true) {
				dePraise();
			} else {
				praise();
			}
			invalidateOptionsMenu();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getSupportMenuInflater().inflate(R.menu.imgtext_openup_menu, menu);
		this.menu = menu;
		return true;
	}

	public void write_comment(View view) {
		log("write Comment Click!");
		JSONObject jsonObject = new JSONObject();
		try {
			Bundle bundle = new Bundle();
			bundle.putInt("passage_id", this.imageTextContent.getId());
			jsonObject.put("bundle", bundle);
			jsonObject.put("name", configureManager.getCommentAcitivity());
			appContainer.startActivity(jsonObject, ImgTextOpenupActivity.this);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void check_praise() {
		log("Begin to Check praise");
		String user_id = configureManager.getUser_id();
		if (user_id != null && !"".equals(user_id)) {
			/*
			 * to check whether the user has login
			 */
			JSONObject jsonObject = ContentParameter.createJsonObject();
			try {
				jsonObject.put("user_id", user_id);
				jsonObject.put("type", "passage");
				jsonObject.put("passage_id", imageTextContent.getId());
				jsonObject.put("method", "checkPraise");
				appContainer.request(jsonObject);
			} catch (Exception e) {
				log("Check Praise Exception:  " + e.toString());
			}

		}
	}

	private void dePraise() {
		log("Begin to Cancel Praise");
		String user_id = configureManager.getUser_id();
		if (user_id != null && !"".equals(user_id)
				&& configureManager.isLogin_state()) {
			JSONObject jsonObject = ContentParameter.createJsonObject();
			try {
				jsonObject.put("user_id", user_id);
				jsonObject.put("type", "passage");
				jsonObject.put("passage_id", imageTextContent.getId());
				jsonObject.put("method", "cancel_parise");
				appContainer.request(jsonObject);
			} catch (Exception e) {
				log("Praise Exception: " + e.toString());
			}
		} else {
			Toast.makeText(getApplicationContext(), "您当前没有登录，无法操作",
					Toast.LENGTH_SHORT).show();
		}

	}

	private void praise() {
		log("Begin to Praise");
		String user_id = configureManager.getUser_id();
		if (user_id != null && !"".equals(user_id)
				&& configureManager.isLogin_state()) {
			JSONObject jsonObject = ContentParameter.createJsonObject();
			try {
				jsonObject.put("user_id", user_id);
				jsonObject.put("type", "passage");
				jsonObject.put("passage_id", imageTextContent.getId());
				jsonObject.put("method", "praise");
				appContainer.request(jsonObject);
			} catch (Exception e) {
				log("Praise Exception: " + e.toString());
			}
		} else {
			Toast.makeText(getApplicationContext(), "您当前没有登录，无法操作",
					Toast.LENGTH_SHORT).show();
		}
	}

	private void log(String msg) {
		MyLog.log(TAG, msg);
	}

	private void update_praise() {
		log("Update Praise State!");
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				MenuItem menuItem = null;
				if (menu != null) {
					menuItem = menu.findItem(R.id.imgtext_openup_menu_like);
					if (menuItem == null) {
						return;
					}
				}
				if (liked) {
					menuItem.setIcon(R.drawable.liked_imgbtn);
				} else {
					menuItem.setIcon(R.drawable.like_imgbtn);
				}
				ImgTextOpenupActivity.this.invalidateOptionsMenu();
				ImgTextOpenupActivity.this.onPrepareOptionsMenu(menu);
			}
		});
	}

	@Override
	public void handleMessage(Object object) {
		JSONObject jsonObject = (JSONObject) object;
		log(jsonObject.toString());
		try {
			String state = jsonObject.getString("check");
			if (state != null && "yes".equals(state)) {
				liked = true;
			} else if (state != null && "no".equals(state)) {
				liked = false;
			}
		} catch (Exception e) {
			log("Handle Message Error: " + e.toString());
		}
		try {
			String state = jsonObject.getString("state");
			String method = jsonObject.getString("method");
			if (state != null && "sucess".equals(state) && method != null
					&& "praise".equals(method)) {
				liked = true;
			} else if (state != null && "error".equals(state) && method != null
					&& "praise".equals(method)) {
				log("Praise Error: " + jsonObject.getString("msg"));
			} else if (state != null && "error".equals(state) && method != null
					&& "cancel_praise".equals(method)) {
				log("DePraise Error: " + jsonObject.getString("msg"));
			} else if (state != null && "sucess".equals(state)
					&& method != null && "cancel_praise".equals(method)) {
				liked = false;
			}
		} catch (Exception e) {
			log("Handle Message Error: " + e.toString());
		}
		update_praise();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		MenuItem menuItem = menu.findItem(R.id.imgtext_openup_menu_like);
		if (liked) {
			menuItem.setIcon(R.drawable.liked_imgbtn);
			menuItem.setTitle("取消赞");
		} else {
			menuItem.setIcon(R.drawable.like_imgbtn);
			menuItem.setTitle("点赞");
		}
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		finalBitmap.setExitTasksEarly(false);
	}

	@Override
	protected void onPause() {
		super.onPause();
		finalBitmap.setExitTasksEarly(true);
	}

}
