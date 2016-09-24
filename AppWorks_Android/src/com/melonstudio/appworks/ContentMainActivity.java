package com.melonstudio.appworks;

import net.tsz.afinal.FinalActivity;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.Toast;

import cn.bidaround.youtui_template.YtTemplate;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;
import com.melonstudio.appworks.fragments.ContentMainFragment.ResponseActivity;
import com.melonstudio.appworks.fragments.SampleListFragment;
import com.melonstudio.model.AbstractContent;
import com.melonstudio.util.ConfigureManager;
import com.melonstudio.util.MyLog;

public class ContentMainActivity extends SherlockFragmentActivity implements
		IActivity, ResponseActivity {

	private static final String TAG = "com.melonstudio.appworks.ContentMainActivity";

	private AppContainer appContainer;
	private ConfigureManager configureManager;

	private SlidingMenu menu;
	private SampleListFragment sampleListFragment;

	private boolean isTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		log("onCreate in ContentMainActivity");

		setContentView(R.layout.activity_main_content);
		FinalActivity.initInjectedView(this);

		sampleListFragment = new SampleListFragment();

		AppContainer.setContext(getApplicationContext());
		appContainer = AppContainer.getInstance();

		configureManager = ConfigureManager
				.getInstance(getApplicationContext());

		getSupportActionBar().setHomeButtonEnabled(true);

		initSlidingMenu();

		if (findViewById(R.id.fragment_detail_content) == null) {
			isTwoPane = false;
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			isTwoPane = true;
		}

		try {
			Bundle bundle = new Bundle();
			bundle.putBoolean("isTwoPane", isTwoPane);
			if (!isTwoPane) {
				SherlockFragment fragment = (SherlockFragment) Class.forName(
						getResources()
								.getString(R.string.content_main_fragment))
						.newInstance();
				fragment.setArguments(bundle);
				FragmentTransaction fragmentTransaction = getSupportFragmentManager()
						.beginTransaction();
				fragmentTransaction.replace(R.id.fragment_main_content,
						fragment);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
			} else {
				SherlockFragment listFragment = (SherlockFragment) Class
						.forName(
								getResources().getString(
										R.string.content_main_fragment))
						.newInstance();
				listFragment.setArguments(bundle);
				SherlockFragment detailFragment = (SherlockFragment) Class
						.forName(
								getResources().getString(
										R.string.webview_fragment))
						.newInstance();
				detailFragment.setArguments(bundle);
				FragmentTransaction fragmentTransaction = getSupportFragmentManager()
						.beginTransaction();
				fragmentTransaction.replace(R.id.fragment_main_content,
						listFragment);
				fragmentTransaction.replace(R.id.fragment_detail_content,
						detailFragment);
				fragmentTransaction.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		YtTemplate.init(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (appContainer == null) {
			AppContainer.setContext(getApplicationContext());
			appContainer = AppContainer.getInstance();
		}
		appContainer.registService(ContentMainActivity.this);

		if (configureManager == null) {
			configureManager = ConfigureManager
					.getInstance(getApplicationContext());
		}
		if (menu.isMenuShowing()) {
			menu.toggle(false);
		}

	}

	@Override
	protected void onDestroy() {
		appContainer.unregistService(ContentMainActivity.this);
		super.onDestroy();
		log("onDestroy in ContentMainActivity");
		YtTemplate.release(this);
	}

	private void log(String msg) {
		MyLog.log(TAG, msg);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_user_info) {
			log("UserInfo Menu!");
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("name", configureManager.getUserinfoActivity());
				appContainer
						.startActivity(jsonObject, ContentMainActivity.this);
			} catch (JSONException e) {
				log(e.toString());
			}
		} else if (item.getItemId() == R.id.menu_exit) {
			log("Exit Menu!");
			this.finish();
		} else if (item.getItemId() == R.id.menu_login) {
			log("Login Menu");
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("name", configureManager.getLoginActivity());
				appContainer
						.startActivity(jsonObject, ContentMainActivity.this);
			} catch (JSONException e) {
				log(e.toString());
			}
		} else if (item.getItemId() == R.id.menu_regist) {
			log("Regist Menu");
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("name", configureManager.getRegistActivity());
				appContainer.startActivity(jsonObject);
			} catch (JSONException e) {
				log(e.toString());
			}
		} else if (item.getItemId() == R.id.menu_reset_password) {
			log("Reset Menu");
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("name",
						configureManager.getResetPasswordActivity());
				appContainer.startActivity(jsonObject);
			} catch (JSONException e) {
				log(e.toString());
			}
		} else if (item.getItemId() == android.R.id.home) {
			menu.toggle();
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		// 点击返回键关闭滑动菜单
		if (menu.isMenuShowing()) {
			menu.showContent();
		} else {
			super.onBackPressed();
		}
	}

	private void initSlidingMenu() {
		// 设置滑动菜单的属性值
		menu = new SlidingMenu(this);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setBehindWidth(getResources().getDimensionPixelOffset(
				R.dimen.menu_width));
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		menu.setBehindCanvasTransformer(new CanvasTransformer() {

			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				float scale = (float) (percentOpen * 0.25 + 0.75);
				canvas.scale(scale, scale, canvas.getWidth() / 2,
						canvas.getHeight() / 2);
			}
		});
		// 设置滑动菜单的视图界面
		menu.setMenu(R.layout.menu_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, sampleListFragment).commit();
	}

	/**
	 * refresh the sliding menu
	 */
	@Override
	public void handleMessage(Object object) {
		sampleListFragment.initSlidingMenuAdapter();
	}

	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
				FragmentTransaction transaction = getSupportFragmentManager()
						.beginTransaction();
				getSupportFragmentManager().popBackStack();
				transaction.commit();
			} else if ((System.currentTimeMillis() - exitTime) > 1500) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void response(AbstractContent abstractContent) {
		try {
			if (isTwoPane) {
				appContainer.responseUrl(abstractContent);
			} else {
				Bundle bundle = new Bundle();
				bundle.putBoolean("isTwoPane", isTwoPane);
				bundle.putSerializable("abstractContent", abstractContent);
				SherlockFragment fragment = (SherlockFragment) Class.forName(
						getResources().getString(R.string.webview_fragment))
						.newInstance();
				fragment.setArguments(bundle);
				FragmentTransaction transaction = getSupportFragmentManager()
						.beginTransaction();
				transaction.addToBackStack(null);
				transaction.replace(R.id.fragment_main_content, fragment)
						.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
