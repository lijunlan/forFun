package com.melonstudio.appworks;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.melonstudio.model.User;
import com.melonstudio.ui.CircularImage;
import com.melonstudio.util.ConfigureManager;
import com.melonstudio.util.MyLog;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.annotation.view.ViewInject;

/**
 * 
 * @author guohaosu
 * 
 */
public class OtherUserInfoActivity extends SherlockActivity {
	private static final String TAG = "com.melonstudio.appworks.OtherUserInfoActivity";

	private User user;

	public void send_click(View view) {
		String user_id = configureManager.getUser_id();
		if (user_id == null || "".equals(user_id.trim())
				|| "null".equals(user_id.trim())) {
			Toast.makeText(getApplicationContext(), "未登录，无法发送消息",
					Toast.LENGTH_SHORT).show();
		} else if (user_id.equals(String.valueOf(user.getUser_id()))) {
			Toast.makeText(getApplicationContext(), "您不可以给自己发消息",
					Toast.LENGTH_SHORT).show();
		} else {
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("name", configureManager.getChatActivity());
				Bundle bundle = new Bundle();
				bundle.putSerializable("user", user);
				jsonObject.put("bundle", bundle);
				appContainer.startActivity(jsonObject,
						OtherUserInfoActivity.this);
			} catch (JSONException e) {
				log(e.toString());
			}
		}
	}

	private void log(String msg) {
		MyLog.log(TAG, msg);
	}

	private AppContainer appContainer;
	private ConfigureManager configureManager;

	private FinalBitmap finalBitmap;

	private OtherUserInfoAdapter adapter;

	@ViewInject(id = R.id.userinf_other_header)
	private CircularImage header;
	@ViewInject(id = R.id.userinfo_layout_listview)
	private ListView listView;
	@ViewInject(id = R.id.userinfo_layout_username)
	private TextView usernameTextView;
	@ViewInject(id = R.id.userinfo_layout_name)
	private TextView nicknameTextView;
	@ViewInject(id = R.id.userinfo_layout_send, click = "send_click")
	private Button sendButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_userinfo_layout);

		FinalActivity.initInjectedView(this);
		getSupportActionBar().setHomeButtonEnabled(true);

		AppContainer.setContext(getApplicationContext());
		appContainer = AppContainer.getInstance();
		configureManager = ConfigureManager
				.getInstance(getApplicationContext());
		finalBitmap = FinalBitmap.create(getApplicationContext());

		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("bundle");
		user = (User) bundle.getSerializable("user");
		initAdapter(user);
		finalBitmap.display(header,
				configureManager.check_user_img(user.getIconUrl()));
		usernameTextView.setText("账号：" + user.getUsername().trim());
		nicknameTextView.setText("昵称：" + user.getName().trim());
		log("onCreate in OtherUserInfoActivity");
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
		log("onStart in OtherUserInfoActivity");
	}

	@Override
	protected void onDestroy() {
		appContainer.unregistService(this);
		super.onDestroy();
		log("onDestroy in OtherUserInfoActivity");
	}

	private class OtherUserInfoItem {
		private String key;
		private String value;

		public OtherUserInfoItem(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}

	}

	private void initAdapter(User user) {
		adapter = new OtherUserInfoAdapter(getApplicationContext());
		listView.setAdapter(adapter);
		if (user != null) {
			adapter.add(new OtherUserInfoItem("年龄", user.getAge()));
			if (user.isShowEmail()) {
				adapter.add(new OtherUserInfoItem("邮箱", user.getEmail()));
			}
			if (user.isShowPhone()) {
				adapter.add(new OtherUserInfoItem("手机", user.getPhone()));
			}
			adapter.add(new OtherUserInfoItem("个性签名", user.getIntroduce()));
		}
		adapter.notifyDataSetChanged();
	}

	private class OtherUserInfoAdapter extends BaseAdapter {

		private List<OtherUserInfoItem> list;
		private LayoutInflater layoutInflater;

		public OtherUserInfoAdapter(Context context) {
			list = new ArrayList<OtherUserInfoItem>();
			layoutInflater = LayoutInflater.from(context);
		}

		public void add(OtherUserInfoItem otherUserInfoItem) {
			list.add(otherUserInfoItem);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = layoutInflater.inflate(R.layout.user_info_item,
						null);
				viewHolder = new ViewHolder();
				TextView keyTextView = (TextView) convertView
						.findViewById(R.id.user_info_key);
				viewHolder.setKey(keyTextView);
				TextView valueTextView = (TextView) convertView
						.findViewById(R.id.user_info_value);
				viewHolder.setValue(valueTextView);
				keyTextView.setText(list.get(position).getKey().trim());
				valueTextView.setText(list.get(position).getValue().trim());
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
				TextView keyTextView = viewHolder.getKey();
				TextView valueTextView = viewHolder.getValue();
				keyTextView.setText(list.get(position).getKey().trim());
				valueTextView.setText(list.get(position).getValue().trim());
			}
			return convertView;
		}
	}

	private class ViewHolder {
		private TextView key;
		private TextView value;

		public TextView getKey() {
			return key;
		}

		public void setKey(TextView key) {
			this.key = key;
		}

		public TextView getValue() {
			return value;
		}

		public void setValue(TextView value) {
			this.value = value;
		}

	}
}
