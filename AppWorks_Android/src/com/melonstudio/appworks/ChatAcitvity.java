package com.melonstudio.appworks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.annotation.view.ViewInject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.melonstudio.clientconnector.ContentParameter;
import com.melonstudio.model.User;
import com.melonstudio.ui.CircularImage;
import com.melonstudio.util.ConfigureManager;
import com.melonstudio.util.MyLog;
import com.melonstudio.util.TimeUtil;

public class ChatAcitvity extends SherlockActivity implements IActivity {

	private static final String TAG = "com.melonstudio.appworks.ChatAcitvity";

	private AppContainer appContainer;
	private ConfigureManager configureManager;

	private InputMethodManager inputMethodManager;
	private FinalBitmap finalBitmap;

	private User other;

	private void log(String msg) {
		MyLog.log(TAG, msg);
	}

	@ViewInject(id = R.id.chat_layout_listview)
	private PullToRefreshListView mListView;

	@ViewInject(id = R.id.chat_layout_send, click = "send_click")
	private Button sendBtn;
	@ViewInject(id = R.id.chat_layout_input)
	private EditText input;

	private ChatActivityAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getSupportActionBar().setHomeButtonEnabled(true);

		AppContainer.setContext(getApplicationContext());
		appContainer = AppContainer.getInstance();
		configureManager = ConfigureManager
				.getInstance(getApplicationContext());

		setContentView(R.layout.activity_chat_layout);
		FinalActivity.initInjectedView(this);

		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		finalBitmap = FinalBitmap.create(getApplicationContext());

		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("bundle");
		other = (User) bundle.getSerializable("user");
		getSupportActionBar().setTitle("与 " + other.getName() + " 的私信");
		log("onCreate in ChatActivity");

		adapter = new ChatActivityAdapter(getApplicationContext());

		mListView.setOnRefreshListener(new PullDownListener());
		mListView.setAdapter(adapter);
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

		new RefreshTask().execute(RefreshTask.TYPE_RECEIVE,
				System.currentTimeMillis());

		log("onStart in ChatActivity");
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
	protected void onDestroy() {
		appContainer.unregistService(this);
		super.onDestroy();

		log("onDestroy in ChatActivity");
	}

	public void send_click(View view) {
		new RefreshTask().execute(RefreshTask.TYPE_SEND);
	}

	private final int SEND = 0;
	private final int RECEIVE = 1;

	private class ChatMessage implements Cloneable {
		int message_id;
		int type;
		long time;
		String content;

		public ChatMessage(JSONObject jsonObject) {
			if (jsonObject != null) {
				try {
					message_id = jsonObject.getInt("message_id");
					int from_user_id = jsonObject.getInt("from_user_id");
					if (from_user_id == other.getUser_id()) {
						type = RECEIVE;
					} else if (from_user_id == Integer
							.parseInt(configureManager.getUser_id())) {
						type = SEND;
					} else {
						log("from User id error!" + from_user_id);
					}
					time = jsonObject.getLong("time");
					content = jsonObject.getString("content");
				} catch (JSONException e) {
					log(e.toString());
				}
			}
		}

		@Override
		public ChatMessage clone() throws CloneNotSupportedException {
			return (ChatMessage) super.clone();
		}
	}

	private class ChatActivityAdapter extends BaseAdapter {

		private List<ChatMessage> list;
		private LayoutInflater layoutInflater;
		private TreeSet<Integer> idSet;

		public ChatActivityAdapter(Context context) {
			list = new ArrayList<ChatAcitvity.ChatMessage>();
			idSet = new TreeSet<Integer>();
			layoutInflater = LayoutInflater.from(context);

		}

		public void addMessage(ChatMessage message) {
			if (idSet.contains(message.message_id)) {
				log("filter this out! " + message.message_id);
			} else {
				log("add this message: " + message.content);
				idSet.add(message.message_id);
				list.add(message);
			}
		}

		@Override
		public boolean isEnabled(int position) {
			return false;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		public long getItemTime(int position) {
			return list.get(position).time;
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint({ "InflateParams", "NewApi", "ViewHolder" })
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ChatMessage chatMessage = list.get(position);
			switch (chatMessage.type) {
			case SEND:

				convertView = layoutInflater.inflate(
						R.layout.activity_chatroom_send, null);
				CircularImage send_header = (CircularImage) convertView
						.findViewById(R.id.activity_chatroom_send_header);
				TextView send_text = (TextView) convertView
						.findViewById(R.id.activity_chatroom_send_text);
				TextView send_time = (TextView) convertView
						.findViewById(R.id.activity_chatroom_send_time);

				send_text.setText(chatMessage.content);
				finalBitmap.display(send_header, configureManager
						.check_user_img(configureManager.getIconUrl()));
				send_time.setText(TimeUtil.formatDateTime(chatMessage.time));
				break;
			case RECEIVE:

				convertView = layoutInflater.inflate(
						R.layout.activity_chatroom_recieve, null);
				CircularImage recv_header = (CircularImage) convertView
						.findViewById(R.id.activity_chatroom_receive_header);
				TextView recv_text = (TextView) convertView
						.findViewById(R.id.activity_chatroom_receive_text);
				TextView recv_time = (TextView) convertView
						.findViewById(R.id.activity_chatroom_receive_time);

				recv_text.setText(chatMessage.content);
				finalBitmap.display(recv_header,
						configureManager.check_user_img(other.getIconUrl()));
				recv_time.setText(TimeUtil.formatDateTime(chatMessage.time));
				break;
			}
			return convertView;
		}

		/**
		 * sort the list base on the time, and notifydatasetchanged
		 */
		public void refresh() {
			Collections.sort(list, new Comparator<ChatMessage>() {
				public int compare(ChatMessage a, ChatMessage b) {
					return (int) (a.message_id - b.message_id);
				}
			});
			this.notifyDataSetChanged();
		}

	}

	private class RefreshTask extends AsyncTask<Object, Integer, Void> {

		final static int TYPE_SEND = 0;
		final static int TYPE_RECEIVE = 1;

		private JSONObject jsonObject;
		boolean hasContent = true;
		boolean isLogin = true;
		int type;

		@Override
		protected Void doInBackground(Object... params) {
			type = (int) params[0];
			JSONObject jo = ContentParameter.createJsonObject();
			switch (type) {
			case TYPE_RECEIVE:
				long time = (long) params[1];
				try {
					jo.put("method", "get_msg_list");
					jo.put("own_user_id", configureManager.getUser_id());
					jo.put("other_user_id", other.getUser_id());
					jo.put("refresh_time", time);
					jo.put("formulate_time", false);
					this.jsonObject = (JSONObject) appContainer
							.blockingRequest(jo);
					log(this.jsonObject.toString());
				} catch (Exception e) {
					log("Exception in doinbackgroun of receive: "
							+ e.toString());
				}
				break;
			case TYPE_SEND:
				try {
					String content = input.getText().toString().trim();
					String user_id = configureManager.getUser_id();
					if (content == null || "".equals(content)) {
						hasContent = false;
					} else if (user_id == null || "".equals(user_id)
							|| "null".equals(user_id)) {
						isLogin = false;
					} else {
						jo.put("method", "blocking_upload_msg");
						jo.put("from_user_id", user_id);
						jo.put("to_user_id", other.getUser_id());
						jo.put("content", content);
						jsonObject = (JSONObject) appContainer
								.blockingRequest(jo);
					}
				} catch (Exception e) {
					log("Exception in doinbackground send: " + e.toString());
				}
			default:
				break;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			switch (type) {
			case TYPE_RECEIVE:
				if (jsonObject != null) {
					try {
						String messagesString = jsonObject
								.getString("messages");
						JSONArray jsonArray = new JSONArray(messagesString);
						if (jsonArray != null) {
							JSONObject jo;
							for (int i = 0; i < jsonArray.length(); i++) {
								jo = jsonArray.getJSONObject(i);
								ChatMessage chatMessage = new ChatMessage(jo);
								adapter.addMessage(chatMessage.clone());
							}
							mListView.onRefreshComplete();
							adapter.refresh();
							adapter.notifyDataSetChanged();
							mListView.setSelection(adapter.getCount() - 1);
						}
					} catch (Exception e) {
						log(e.toString());
					}
				} else {
					mListView.onRefreshComplete();
				}
				break;
			case TYPE_SEND:
				if (!hasContent) {
					Toast.makeText(getApplicationContext(), "消息不能为空",
							Toast.LENGTH_SHORT).show();
				} else if (!isLogin) {
					Toast.makeText(getApplicationContext(), "您还没有登录，不能发送私信",
							Toast.LENGTH_SHORT).show();
				} else {
					if (jsonObject != null) {
						try {
							String state = jsonObject.getString("state");
							if ("error".equals(state)) {
								String msg = jsonObject.getString("msg");
								Toast.makeText(getApplicationContext(),
										"发送消息失败：" + msg, Toast.LENGTH_SHORT)
										.show();
							} else {
								log("Upload Message Success!");
								input.setText("");
								Thread.sleep(1000);
								new RefreshTask().execute(
										RefreshTask.TYPE_RECEIVE,
										System.currentTimeMillis());
							}
						} catch (Exception e) {
							log("Exception in post send: " + e.toString());
						}
					} else {
						log("jsonObject is null");
					}
				}
			default:
				break;
			}

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

	private class PullDownListener implements OnRefreshListener<ListView> {

		@Override
		public void onRefresh(PullToRefreshBase<ListView> refreshView) {
			String label = DateUtils.formatDateTime(getApplicationContext(),
					System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
							| DateUtils.FORMAT_SHOW_DATE
							| DateUtils.FORMAT_ABBREV_ALL);

			refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
			if (adapter.getCount() > 0) {
				new RefreshTask().execute(RefreshTask.TYPE_RECEIVE,
						adapter.getItemTime(0));
			}
			new RefreshTask().execute(RefreshTask.TYPE_RECEIVE,
					System.currentTimeMillis());
		}

	}

	@Override
	public void handleMessage(Object object) {
		final JSONObject jsonObject = (JSONObject) object;

		ChatAcitvity.this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				try {
					final String state = jsonObject.getString("state");
					if ("error".equals(state)) {
						String msg = jsonObject.getString("msg");
						Toast.makeText(getApplicationContext(), "发送失败！" + msg,
								Toast.LENGTH_SHORT).show();
					} else {
						new RefreshTask().execute(RefreshTask.TYPE_RECEIVE,
								System.currentTimeMillis());
					}
				} catch (Exception e) {
					log(e.toString());
				}
			}
		});

	}

}
