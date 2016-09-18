package com.melonstudio.appworks;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.melonstudio.clientconnector.ContentParameter;
import com.melonstudio.util.ConfigureManager;
import com.melonstudio.util.MyLog;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

public class ResetPasswordActivity extends SherlockActivity implements
		IActivity {

	private static final String TAG = "com.melonstudio.appworks.ResetPasswordActivity";

	private AppContainer appContainer;
	private ConfigureManager configureManager;

	@ViewInject(id = R.id.resetp_origin)
	private EditText origin;
	@ViewInject(id = R.id.resetp_new)
	private EditText newp;
	@ViewInject(id = R.id.resetp_new_again)
	private EditText new_again;

	@ViewInject(id = R.id.resetp_submit, click = "submit_click")
	private Button submit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		FinalActivity.initInjectedView(this);

		appContainer = AppContainer.getInstance();
		setContentView(R.layout.activity_reset_password);
		log("OnCreate in ResetPasswordActivity");
		configureManager = ConfigureManager
				.getInstance(getApplicationContext());

		getSupportActionBar().setHomeButtonEnabled(true);
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
	protected void onStart() {
		if (appContainer == null) {
			AppContainer.setContext(getApplicationContext());
			appContainer = AppContainer.getInstance();
		}
		appContainer.registService(ResetPasswordActivity.this);
		if (configureManager == null) {
			configureManager = ConfigureManager
					.getInstance(getApplicationContext());
		}
		super.onStart();
	}

	@Override
	protected void onDestroy() {
		appContainer.unregistService(ResetPasswordActivity.this);
		super.onDestroy();
		log("OnDestroy in ResetPasswordActivity");
	}

	@Override
	public void handleMessage(Object object) {
		log("RECEIVED RETURNING MESSAGE");
		final JSONObject jsonObject = (JSONObject) object;
		log(jsonObject.toString());
		submit.post(new Runnable() {

			@Override
			public void run() {
				try {
					if (jsonObject.getString("state").equals("sucess")) {
						Toast.makeText(getApplicationContext(), "更改密码成功",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getApplicationContext(),
								"更改密码失败：" + jsonObject.getString("msg"),
								Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					log("Exception in handle message: " + e.toString());
				}
			}
		});
	}

	private static void log(String msg) {
		MyLog.log(TAG, msg);
	}

	public void submit_click(View view) {
		log("点击确定按钮");
		String originString = origin.getText().toString().trim();
		String new_string = newp.getText().toString().trim();
		String new_again_string = new_again.getText().toString().trim();

		String msg = null;

		if ("".equals(originString)) {
			msg = "请输入原密码";
		} else if ("".equals(new_string)) {
			msg = "请输入新密码";
		} else if ("".equals(new_again_string)) {
			msg = "请再次输入新密码";
		} else if (!new_again_string.equals(new_string)) {
			msg = "两次新密码输入不同，请重新确认";
		}

		if (msg == null) {
			JSONObject jsonObject = ContentParameter.createJsonObject();
			try {
				jsonObject.put("user_id", configureManager.getUser_id());
				jsonObject.put("username", configureManager.getUsername());
				jsonObject.put("old_password", originString);
				jsonObject.put("new_password", new_again_string);
				jsonObject.put("method", "changePassword");
				appContainer.request(jsonObject);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT)
					.show();
		}
	}
}
