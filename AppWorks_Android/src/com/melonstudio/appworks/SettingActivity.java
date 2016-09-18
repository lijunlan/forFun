package com.melonstudio.appworks;

import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.melonstudio.clientconnector.ContentParameter;
import com.melonstudio.util.ConfigureManager;
import com.melonstudio.util.MyLog;
import com.zcw.togglebutton.ToggleButton;
import com.zcw.togglebutton.ToggleButton.OnToggleChanged;

public class SettingActivity extends SherlockActivity {
	private final static String TAG = "com.melonstudio.appworks.SettingActivity";

	private void log(String msg) {
		MyLog.log(TAG, msg);
	}

	private AppContainer appContainer;
	private ConfigureManager configureManager;

	@ViewInject(id = R.id.setting_notify)
	private ToggleButton notifyToggleButton;
	@ViewInject(id = R.id.setting_phone)
	private ToggleButton phoneToggleButton;
	@ViewInject(id = R.id.setting_email)
	private ToggleButton emailToggleButton;

	@ViewInject(id = R.id.setting_change_password, click = "change_password_click")
	private Button changePassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setHomeButtonEnabled(true);
		log("onCreate in Setting Activity");

		setContentView(R.layout.activity_setting_layout);

		FinalActivity.initInjectedView(this);
		AppContainer.setContext(getApplicationContext());
		appContainer = AppContainer.getInstance();
		configureManager = ConfigureManager
				.getInstance(getApplicationContext());

		initToggleButtons();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void initToggleButtons() {
		if (configureManager.isReceive_notify()) {
			notifyToggleButton.toggleOn();
		} else {
			notifyToggleButton.toggleOff();
		}
		notifyToggleButton.setOnToggleChanged(new OnToggleChanged() {
			@Override
			public void onToggle(boolean on) {
				log(String.valueOf(on));
				if (on) {
					configureManager.setReceive_notify(true);
				} else {
					configureManager.setReceive_notify(false);
				}
			}
		});
		notifyToggleButton.setVisibility(View.VISIBLE);

		if (configureManager.isShow_phone()) {
			phoneToggleButton.toggleOn();
		} else {
			phoneToggleButton.toggleOff();
		}
		phoneToggleButton.setOnToggleChanged(new OnToggleChanged() {

			@Override
			public void onToggle(boolean on) {
				if (!configureManager.isLogin_state()) {

					Toast.makeText(getApplicationContext(), "您还没有登录，无法完成操作",
							Toast.LENGTH_SHORT).show();
				} else {
					new SettingAsycTask().execute(SettingAsycTask.TYPE_PHONE,
							on);
				}

			}
		});

		if (configureManager.isShow_email()) {
			emailToggleButton.toggleOn();
		} else {
			emailToggleButton.toggleOff();
		}
		emailToggleButton.setOnToggleChanged(new OnToggleChanged() {

			@Override
			public void onToggle(boolean on) {
				if (!configureManager.isLogin_state()) {
					Toast.makeText(getApplicationContext(), "您还没有登录，无法完成操作",
							Toast.LENGTH_SHORT).show();
				} else {
					new SettingAsycTask().execute(SettingAsycTask.TYPE_EMAIL,
							on);
				}
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		log("onStart in Setting Activity");
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
		super.onDestroy();
		log("onDestroy in Setting Activity");
		appContainer.unregistService(this);
	}

	private class SettingAsycTask extends AsyncTask<Object, Object, Void> {

		final static int TYPE_PHONE = 0;
		final static int TYPE_EMAIL = 1;

		int type;
		JSONObject jsonObject;

		@Override
		protected Void doInBackground(Object... params) {
			type = (int) params[0];
			JSONObject jo = ContentParameter.createJsonObject();
			switch (type) {
			case TYPE_PHONE:
				try {
					boolean flag = (boolean) params[1];
					jo.put("method", "blocking_changeInfo");
					jo.put("user_id", configureManager.getUser_id());
					jo.put("showPhone", String.valueOf(flag));
					jsonObject = (JSONObject) appContainer.blockingRequest(jo);
				} catch (Exception e) {
					log("Exception in send TYPE_PHONE: " + e.toString());
				}
				break;
			case TYPE_EMAIL:
				try {
					boolean flag = (boolean) params[1];
					jo.put("method", "blocking_changeInfo");
					jo.put("user_id", configureManager.getUser_id());
					jo.put("showEmail", String.valueOf(flag));
					jsonObject = (JSONObject) appContainer.blockingRequest(jo);
				} catch (Exception e) {
					log("Exception in send TYPE_EMAIL: " + e.toString());
				}
				break;
			default:
				break;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			switch (type) {
			case TYPE_PHONE:
				if (jsonObject != null) {
					try {
						String state = jsonObject.getString("state");
						log("state: " + state);
						if ("success".equals(state)) {
							log(jsonObject.toString());
							configureManager.updateLocalInfo(jsonObject);
						} else {
							Toast.makeText(getApplicationContext(), "更改设置失败",
									Toast.LENGTH_SHORT).show();
							String msg = jsonObject.getString("msg");
							log(msg);
						}
					} catch (JSONException e) {
						log(e.toString());
					}
				} else {
					log("jsonOjbect is null");
				}
				break;
			case TYPE_EMAIL:
				if (jsonObject != null) {
					try {
						String state = jsonObject.getString("state");
						log("state: " + state);
						if ("success".equals(state)) {
							log(jsonObject.toString());
							configureManager.updateLocalInfo(jsonObject);
						} else {
							Toast.makeText(getApplicationContext(), "更改设置失败",
									Toast.LENGTH_SHORT).show();
							String msg = jsonObject.getString("msg");
							log(msg);
						}
					} catch (JSONException e) {
						log(e.toString());
					}
				} else {
					log("jsonOjbect is null");
				}
				break;
			default:
				break;
			}
			super.onPostExecute(result);
		}

	}

	public void change_password_click(View view) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("name", configureManager.getResetPasswordActivity());
			appContainer.startActivity(jsonObject, this);
		} catch (Exception e) {
			log("Exception in start resetpassword activity: " + e.toString());
		}
	}

}
