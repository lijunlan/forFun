package com.melonstudio.appworks;

import org.json.JSONException;
import org.json.JSONObject;

import com.melonstudio.network.ContentService;
import com.melonstudio.util.ConfigureManager;
import com.melonstudio.util.MyLog;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.Toast;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

/**
 * 
 * @author Wo
 * 
 */
public class WelcomActivity extends FinalActivity implements IActivity {

	@ViewInject(id = R.id.user_welcome_img)
	private ImageView userImageView;
	@ViewInject(id = R.id.ads_welcome_img)
	private ImageView adsImageView;

	private AppContainer appContainer;
	private ConfigureManager configureManager;

	private int startTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		configureManager = ConfigureManager
				.getInstance(getApplicationContext());
		/*
		 * Initialize the App Container
		 */
		AppContainer.setContext(getApplicationContext());
		appContainer = AppContainer.getInstance();
		appContainer.registService(this);

		startTime = 0;

		// test();

		/**
		 * welcome page settings, it will be over in 2 seconds
		 */
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				Intent intent = new Intent(getApplicationContext(),
						ContentService.class);
				intent.putExtra("from", "welcomactivity");
				startService(intent);
				log("Start ContentServie");
				JSONObject jsonObject = new JSONObject();
				try {
					jsonObject.put("name",
							configureManager.getContentMainActivity());
					appContainer.startActivity(jsonObject, WelcomActivity.this);
				} catch (Exception e) {
					log("Exception in start main activity: " + e.toString());
				}
				log("startActivity");
			}
		}, 2000);

	}

	@Override
	protected void onStart() {
		super.onStart();
		startTime++;
		if (startTime == 2) {
			finish();
			System.exit(0);
		}
	}

	@Override
	protected void onDestroy() {
		appContainer.unregistService(this);
		super.onDestroy();
	}

	@Override
	public void handleMessage(final Object object) {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				JSONObject jsonObject = (JSONObject) object;
				try {
					String state = jsonObject.getString("state");
					if ("error".equals(state)) {
						Toast.makeText(getApplicationContext(), "无法连接服务器",
								Toast.LENGTH_SHORT).show();
						String msg = jsonObject.getString("msg");
						log("无法连接服务： " + msg);
					} else {
						Toast.makeText(getApplicationContext(), "成功连接服务器",
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					log("Exception in handleMessage: " + e.toString());
				}

			}
		});

	}

	private static final String TAG = "com.melonstudio.appworks.WelcomActivity";

	private void log(String msg) {
		MyLog.log(TAG, msg);
	}

	// private void test() {
	// for (int i = 0; i < 3; i++) {
	// appContainer.notificationRequest("来自  hhh  的私信", "cecece", "有您的私信",
	// configureManager.getChatListActivity(), null);
	// }
	// }
}
