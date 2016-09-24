package com.melonstudio.appworks;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.melonstudio.clientconnector.ContentParameter;
import com.melonstudio.util.ConfigureManager;
import com.melonstudio.util.MyLog;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

/**
 * 
 * @author Wo
 * 
 */
public class RegistActivity extends SherlockActivity implements IActivity {
	private final static String TAG = "com.melonstudio.appworks.RegistActivity";

	@ViewInject(id = R.id.regist_layout_username)
	private EditText usernameEditText;
	@ViewInject(id = R.id.regist_layout_name)
	private EditText nameEditText;
	@ViewInject(id = R.id.regist_layout_password)
	private EditText passwordEditText;
	@ViewInject(id = R.id.regist_layout_email)
	private EditText emailEditText;
	@ViewInject(id = R.id.regist_layout_btn, click = "submit")
	private Button submitBtn;

	private AppContainer appContainer;
	private ConfigureManager configureManager;

	private InputMethodManager inputMethodManager;

	private String my_username;
	private String my_password;
	private String my_name;
	private String my_email;

	private void getValue() {
		my_email = emailEditText.getText().toString().trim();
		my_password = passwordEditText.getText().toString().trim();
		my_name = nameEditText.getText().toString().trim();
		my_username = usernameEditText.getText().toString().trim();
	}

	/**
	 * check the string
	 * 
	 * @param str
	 * @return ture if the str is null, else return false
	 */
	private boolean checkNull(String str) {
		if (str == null || "".equals(str)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist_layout);

		FinalActivity.initInjectedView(this);
		getSupportActionBar().setHomeButtonEnabled(true);

		appContainer = AppContainer.getInstance();
		configureManager = ConfigureManager
				.getInstance(getApplicationContext());

		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		log("onCreate in RegistActivity");
	}

	public void submit(View view) {
		log("submit click");
		getValue();
		if (checkNull(my_email) || checkNull(my_name) || checkNull(my_username)
				|| checkNull(my_password)) {
			Toast.makeText(getApplicationContext(), "请将信息填写完整",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (!check_email_legal(my_email)) {
			Toast.makeText(getApplicationContext(), "请填写正确的邮箱地址",
					Toast.LENGTH_SHORT).show();
			return;
		}
		JSONObject jsonObject = ContentParameter.createJsonObject();
		try {
			jsonObject.put("method", "register");
			jsonObject.put("username", my_username);
			jsonObject.put("password", my_password);
			jsonObject.put("email", my_email);
			jsonObject.put("name", my_name);
			appContainer.request(jsonObject);
		} catch (Exception e) {
			log("Exception in send request: " + e.toString());
		}
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
			ConfigureManager.getInstance(getApplicationContext());
		}
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

	/**
	 * 
	 * @param email
	 * @return
	 */
	private boolean check_email_legal(String email) {
		boolean tag = true;
		final String pattern1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		final Pattern pattern = Pattern.compile(pattern1);
		final Matcher mat = pattern.matcher(email);
		if (!mat.find()) {
			tag = false;
		}
		return tag;
	}

	@Override
	protected void onDestroy() {
		appContainer.unregistService(this);
		log("onDestroy in RegistActivity");
		super.onDestroy();
	}

	private void log(String msg) {
		MyLog.log(TAG, msg);
	}

	@Override
	public void handleMessage(final Object object) {
		submitBtn.post(new Runnable() {

			@Override
			public void run() {
				JSONObject jsonObject = (JSONObject) object;
				log("HandleMessage in RegistActivity: " + jsonObject.toString());
				try {
					if ("error".equals(jsonObject.getString("state"))) {
						String msg = jsonObject.getString("msg");
						Toast.makeText(getApplicationContext(), msg,
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getApplicationContext(), "注册成功",
								Toast.LENGTH_SHORT).show();
						SharedPreferences sharedPreferences = getSharedPreferences(
								ConfigureManager.SHARED_PREFERENCE_NAME,
								Context.MODE_PRIVATE);
						Editor editor = sharedPreferences.edit();
						editor.putString("username", my_username);
						editor.putString("password", my_password);
						editor.commit();
						log("Persistent Username and Password Successfully");
						JSONObject jsonObject2 = ContentParameter
								.createJsonObject();
						jsonObject2.put("username", my_username);
						jsonObject2.put("password", my_password);
						jsonObject2.put("method", "login");
						appContainer.request(jsonObject2);
						RegistActivity.this.finish();
					}
				} catch (Exception e) {
					log("Exception in HandleMessage: " + e.toString());
				}
			}
		});
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
