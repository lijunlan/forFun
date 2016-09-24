package com.melonstudio.appworks;

import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.melonstudio.clientconnector.ContentParameter;
import com.melonstudio.util.ConfigureManager;
import com.melonstudio.util.MyLog;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

/**
 * 
 * @author guohaosu
 * 
 */
public class UserInfoUpdateActivity extends SherlockActivity implements
		IActivity {

	private final static String TAG = "com.melonstudio.appworks.UserInfoUpdateActivity";

	private AppContainer appContainer;
	private ConfigureManager configureManager;
	private InputMethodManager inputMethodManager;

	@ViewInject(id = R.id.update_info_input)
	private EditText editText;

	private String keyword;
	private String key;
	private String value;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		log("onCreate in UserInfoUpdateActivity");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userinfo_update);

		FinalActivity.initInjectedView(this);
		getSupportActionBar().setHomeButtonEnabled(true);

		AppContainer.setContext(getApplicationContext());
		appContainer = AppContainer.getInstance();

		configureManager = ConfigureManager
				.getInstance(getApplicationContext());

		Bundle bundle = getIntent().getBundleExtra("bundle");
		log("Bundle: " + bundle.toString());
		key = bundle.getString("key", "title").trim();
		getActionBar().setTitle(key);
		value = bundle.getString("value", "value").trim();
		editText.setText(value);
		keyword = bundle.getString("keyword", "keyword").trim();
		log("keyword: " + keyword);
		log("key: " + key);
		log("value: " + value);

		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			return true;
		case R.id.userinfo_update_menu_save:
			save();
			return true;
		case R.id.userinfo_update_menu_canel:
			this.finish();
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getSupportMenuInflater().inflate(R.menu.userinfo_update_menu, menu);
		return true;
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
	}

	@Override
	protected void onDestroy() {
		log("onDestroy in UserInfoUpdateActivity");
		appContainer.unregistService(UserInfoUpdateActivity.this);
		super.onDestroy();
	}

	private void log(String msg) {
		MyLog.log(TAG, msg);
	}

	public void save() {
		JSONObject jsonObject = ContentParameter.createJsonObject();
		try {
			jsonObject.put("user_id", configureManager.getUser_id());
			jsonObject.put(keyword, editText.getText().toString().trim());
			jsonObject.put("method", "changeInfo");
			appContainer.request(jsonObject);
		} catch (Exception e) {
			log("Exception: " + e.toString());
		}

		log("click save: " + jsonObject.toString());
	}

	@Override
	public void handleMessage(Object object) {
		JSONObject jsonObject = (JSONObject) object;
		try {
			String state = jsonObject.getString("state");
			log("state: " + state);
			if ("success".equals(state)) {
				configureManager.updateLocalInfo(jsonObject);
				this.finish();
			} else {
				Toast.makeText(getApplicationContext(), "更新个人信息失败",
						Toast.LENGTH_SHORT).show();
			}
		} catch (JSONException e) {
			log(e.toString());
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (getCurrentFocus() != null
					&& getCurrentFocus().getWindowToken() != null) {
				inputMethodManager.hideSoftInputFromWindow(getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
		return super.onTouchEvent(event);
	}
}
