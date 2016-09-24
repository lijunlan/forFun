package com.melonstudio.appworks;

import java.util.ArrayList;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.melonstudio.clientconnector.ContentParameter;
import com.melonstudio.model.User;
import com.melonstudio.ui.CircularImage;
import com.melonstudio.util.ConfigureManager;
import com.melonstudio.util.MyLog;
import com.melonstudio.util.TimeUtil;

public class ChatListActivity extends SherlockActivity {
	private final static String TAG = "com.melonstudio.appworks.ChatListActivity";

	private void log(String msg) {
		MyLog.log(TAG, msg);
	}

	private AppContainer appContainer;
	private ConfigureManager configureManager;
	private FinalBitmap finalBitmap;

	@ViewInject(id = R.id.chat_list_layout_listview, itemClick = "onItemSelected")
	private ListView mListView;

	private ChatListAdapter adapter;

	private int next_page = 1;
	private int start_page = 1;

	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		log("item click " + position);
		User user = new User();
		ChatListItem chatListItem = (ChatListItem) adapter.getItem(position);
		user.setName(chatListItem.name);
		user.setIconUrl(chatListItem.iconUrl);
		user.setUser_id(chatListItem.user_id);
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("name", configureManager.getChatActivity());
			Bundle bundle = new Bundle();
			bundle.putSerializable("user", user);
			jsonObject.put("bundle", bundle);
			appContainer.startActivity(jsonObject);
		} catch (JSONException e) {
			log("Exception in Line 78: " + e.toString());
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_list_layout);

		FinalActivity.initInjectedView(this);
		getSupportActionBar().setHomeButtonEnabled(true);

		AppContainer.setContext(getApplicationContext());
		appContainer = AppContainer.getInstance();
		configureManager = ConfigureManager
				.getInstance(getApplicationContext());
		/**
		 * the unread message has been read. Reset the message number to 0
		 */
		configureManager.setMessage_num(0);
		finalBitmap = FinalBitmap.create(getApplicationContext());
		adapter = new ChatListAdapter(getApplicationContext());

		initListView();

		mListView.setAdapter(adapter);

	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void initListView() {
		mListView.setOnScrollListener(new OnScrollListener() {

			private int lastItem;

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (lastItem == adapter.getCount()
						&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					new RefreshChatListTask().execute(REFRESH, next_page);
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				lastItem = firstVisibleItem + visibleItemCount - 1;
			}
		});

		mListView
				.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
					@Override
					public void onCreateContextMenu(ContextMenu menu, View v,
							ContextMenuInfo menuInfo) {
						menu.add(0, MENU_DELETE, 0, "删除和TA的所有私信");
					}
				});

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo itemInfo = (AdapterContextMenuInfo) item
				.getMenuInfo();
		ChatListItem chatListItem = (ChatListItem) adapter
				.getItem(itemInfo.position);
		log(chatListItem.name);
		if (item.getItemId() == MENU_DELETE) {
			new RefreshChatListTask().execute(DELETE, chatListItem.user_id,
					itemInfo.position);
			log("DELETE selected");
		} else {
			log("ELETE selected error!");
		}
		return super.onContextItemSelected(item);
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

		adapter.clear();
		new RefreshChatListTask().execute(REFRESH, start_page);

		log("onStart in ChatListActivity");
	}

	@Override
	protected void onDestroy() {
		appContainer.unregistService(this);
		super.onDestroy();
	}

	private class ChatListItem implements Cloneable {
		String name;
		String iconUrl;
		int user_id;
		String r_m_content;
		long time;

		public ChatListItem(JSONObject jsonObject) {
			try {
				name = jsonObject.getString("name");
				iconUrl = jsonObject.getString("iconUrl");
				user_id = jsonObject.getInt("user_id");
				JSONObject jo = jsonObject.getJSONObject("recent_message");
				r_m_content = jo.getString("content");
				time = jo.getLong("time");
			} catch (JSONException e) {
				log("Exception in Line 103: " + e.toString());
			}
		}

		@Override
		public ChatListItem clone() throws CloneNotSupportedException {
			return (ChatListItem) super.clone();
		}
	}

	private class ChatListAdapter extends BaseAdapter {

		List<ChatListItem> list;
		TreeSet<Integer> id_set;
		LayoutInflater layoutInflater;

		public void clear() {
			id_set.clear();
			list.clear();
		}

		public void add(ChatListItem chatListItem) {
			if (id_set.contains(chatListItem.user_id)) {
				log("filter this out! " + chatListItem.user_id);
			} else {
				id_set.add(chatListItem.user_id);
				list.add(chatListItem);
			}
		}

		public void remove(int position) {
			ChatListItem chatListItem = list.get(position);
			if (chatListItem != null) {
				id_set.remove(chatListItem.user_id);
				list.remove(chatListItem);
			} else {
				log("this position doesn't exist");
			}
		}

		public ChatListAdapter(Context context) {
			list = new ArrayList<ChatListActivity.ChatListItem>();
			layoutInflater = LayoutInflater.from(context);
			id_set = new TreeSet<Integer>();
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
				viewHolder = new ViewHolder();
				convertView = layoutInflater.inflate(
						R.layout.chat_list_layout_item, null);
				viewHolder.header = (CircularImage) convertView
						.findViewById(R.id.chat_list_item_header);
				viewHolder.time = (TextView) convertView
						.findViewById(R.id.chat_list_item_time);
				viewHolder.content = (TextView) convertView
						.findViewById(R.id.chat_list_item_content);
				viewHolder.name = (TextView) convertView
						.findViewById(R.id.chat_list_item_name);
				convertView.setTag(viewHolder);
			}
			viewHolder = (ViewHolder) convertView.getTag();
			ChatListItem chatListItem = list.get(position);
			finalBitmap.display(viewHolder.header,
					configureManager.check_user_img(chatListItem.iconUrl));
			viewHolder.header.setOnClickListener(new HeaderClickListener(
					chatListItem));
			viewHolder.name.setText(chatListItem.name);
			viewHolder.content.setText(chatListItem.r_m_content);
			viewHolder.time.setText(TimeUtil
					.simple_formateDateTime(chatListItem.time));
			return convertView;
		}

	}

	private class ViewHolder {
		CircularImage header;
		TextView name;
		TextView content;
		TextView time;
	}

	private class RefreshChatListTask extends AsyncTask<Integer, Integer, Void> {

		int type;
		JSONArray jsonArray;
		JSONObject jsonObject;
		int position;

		@Override
		protected Void doInBackground(Integer... params) {
			type = params[0];
			switch (type) {
			case REFRESH:
				int page = params[1];
				JSONObject jsonObject = ContentParameter.createJsonObject();
				try {
					jsonObject.put("user_id", configureManager.getUser_id());
					jsonObject.put("page", page);
					jsonObject.put("formulate_time", false);
					jsonObject.put("method", "get_recent_list");
					this.jsonArray = (JSONArray) appContainer
							.blockingRequest(jsonObject);
					log(this.jsonArray.toString());
				} catch (Exception e) {
					log("Exception in Line 176: " + e.toString());
				}
				break;
			case DELETE:
				int another_user_id = params[1];
				JSONObject jsonObject2 = ContentParameter.createJsonObject();
				position = params[2];
				try {
					jsonObject2.put("method", "delete_many_msg");
					jsonObject2.put("own_user_id",
							configureManager.getUser_id());
					jsonObject2.put("another_user_id", another_user_id);
					this.jsonObject = (JSONObject) appContainer
							.blockingRequest(jsonObject2);
				} catch (Exception e) {
					log("Exception in Line 323: " + e.toString());
				}
				break;
			default:
				break;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			switch (type) {
			case REFRESH:
				if (jsonArray != null) {
					if (jsonArray.length() > 0) {
						++next_page;
						JSONObject jsonObject;
						ChatListItem item;
						for (int i = 0; i < jsonArray.length(); i++) {
							try {
								jsonObject = jsonArray.getJSONObject(i);
								item = new ChatListItem(jsonObject);
								adapter.add(item.clone());
							} catch (Exception e) {
								log("Exception in Line 207: " + e.toString());
							}
						}
						adapter.notifyDataSetChanged();
					} else {
						log("there is no more!");
					}
				} else {
					log("jsonArray is null");
				}
				break;
			case DELETE:
				if (jsonObject != null) {
					log(jsonObject.toString());
					try {
						String state = jsonObject.getString("state");
						if (state != null && "sucess".equals(state)) {
							adapter.remove(position);
							adapter.notifyDataSetChanged();
						}
					} catch (Exception e) {
						log("Exception in Line 370: " + e.toString());
					}
				} else {
					log("In Line 367: JsonObject is null");
				}
				break;
			default:
				break;
			}
		}
	}

	private class HeaderClickListener implements OnClickListener {

		ChatListItem chatListItem;

		public HeaderClickListener(ChatListItem chatListItem) {
			this.chatListItem = chatListItem;
		}

		@Override
		public void onClick(View v) {
			log(chatListItem.user_id + " click header");
			JSONObject jsonObject = ContentParameter.createJsonObject();
			try {
				jsonObject.put("method", "get_user_info");
				jsonObject.put("user_id", chatListItem.user_id);
				if (appContainer != null) {
					appContainer.request(jsonObject);
				}
			} catch (Exception e) {
				log(e.toString());
			}
		}

	}

	private final int REFRESH = 0;
	private final int DELETE = 1;

	private final int MENU_DELETE = 0;

}
