package com.melonstudio.appworks;

import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.melonstudio.clientconnector.ContentParameter;
import com.melonstudio.serverprotocal.Protocol;
import com.melonstudio.ui.CircularImage;
import com.melonstudio.util.ConfigureManager;
import com.melonstudio.util.MyLog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.annotation.view.ViewInject;

/**
 * login function activity
 * 
 * @author Wo
 * 
 */
public class LoginActivity extends SherlockActivity implements IActivity {
	private static final String TAG = "com.melonstudio.appworks.LoginActivity";

	@ViewInject(id = R.id.login_header)
	private CircularImage circularImage;
	private FinalBitmap finalBitmap;
	@ViewInject(id = R.id.login_login, click = "login_click")
	private Button login;
	@ViewInject(id = R.id.login_forget, click = "fogot_click")
	private Button fogot;
	@ViewInject(id = R.id.login_username)
	private EditText username;
	@ViewInject(id = R.id.login_password)
	private EditText password;
	@ViewInject(id = R.id.login_regist, click = "regist_click")
	private TextView registTextView;

	private ConfigureManager configureManager;
	private AppContainer appContainer;
	private InputMethodManager inputMethodManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appContainer = AppContainer.getInstance();
		configureManager = ConfigureManager
				.getInstance(getApplicationContext());
		setContentView(R.layout.activity_login);
		FinalActivity.initInjectedView(this);
		
		finalBitmap = FinalBitmap.create(getApplicationContext());
		finalBitmap.display(circularImage,
				configureManager.check_user_img(configureManager.getIconUrl()));


		registTextView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		getSupportActionBar().setHomeButtonEnabled(true);

		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		log("onCreate in LoginActivity");
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
		try {
			Intent intent = getIntent();
			Bundle bundle = intent.getBundleExtra("bundle");
			String username = bundle.getString("username");
			if (username != null && !"".equals(username)) {
				this.username.setText(username.trim());
			}
		} catch (Exception exception) {
			log("Exception in LoginActivity : " + exception.toString());
		}

	}

	@Override
	protected void onDestroy() {
		appContainer.unregistService(LoginActivity.this);
		super.onDestroy();
		log("onDestroy in LoginActivity");
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

	@Override
	public void handleMessage(Object object) {
		final JSONObject jsonObject = (JSONObject) object;
		log(jsonObject.toString());
		try {
			if (jsonObject.getString("state").equals("error")) {
				final String msg = jsonObject.getString("msg");
				login.post(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(getApplicationContext(), msg,
								Toast.LENGTH_SHORT).show();
					}
				});
			} else {
				configureManager.updateLocalInfo(jsonObject);
				this.finish();
			}
		} catch (Exception e) {
			log("Exception in handle Login: " + e.toString());
		}
	}

	public void login_click(View view) {
		String msg = null;
		if ("".equals(username.getText().toString().trim())) {
			msg = "请填写用户名";
		}
		if ("".equals(password.getText().toString().trim())) {
			msg = "请填写密码";
		}
		if (msg != null) {
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT)
					.show();
		} else {
			JSONObject jsonObject = ContentParameter.createJsonObject();
			try {
				jsonObject.put("from", Protocol.LOGIN);
				jsonObject
						.put("username", username.getText().toString().trim());
				jsonObject
						.put("password", password.getText().toString().trim());
				jsonObject.put("method", "login");
				appContainer.request(jsonObject);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public void fogot_click(View view) {
		log("fogot click");
		Toast.makeText(getApplicationContext(), "正在开发中", Toast.LENGTH_SHORT)
				.show();
	}

	private static void log(String msg) {
		MyLog.log(TAG, msg);
	}

	public void regist_click(View view) {
		log("regist click");
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("name", configureManager.getRegistActivity());
			appContainer.startActivity(jsonObject, LoginActivity.this);
		} catch (Exception e) {
			log("Start Regist Activity Error: " + e.toString());
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
