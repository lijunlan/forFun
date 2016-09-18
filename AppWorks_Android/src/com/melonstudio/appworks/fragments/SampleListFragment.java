package com.melonstudio.appworks.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.FinalBitmap;

import android.annotation.SuppressLint;
import android.app.Instrumentation;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.melonstudio.appworks.AppContainer;
import com.melonstudio.appworks.IActivity;
import com.melonstudio.appworks.R;
import com.melonstudio.clientconnector.ContentParameter;
import com.melonstudio.ui.CircularImage;
import com.melonstudio.util.ConfigureManager;
import com.melonstudio.util.MyLog;

public class SampleListFragment extends ListFragment implements IActivity {

	private static final String TAG = "com.melonstudio.appworks.fragments.SampleListFragment";

	private SlideingMenuItemAdapter adapter;

	private ConfigureManager configureManager;
	private AppContainer appContainer;

	private void log(String msg) {
		MyLog.log(TAG, msg);
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setupViews();
	}

	@Override
	public void onStart() {
		super.onStart();
		log("onStart in Sliding Menu");
		initSlidingMenuAdapter();
	}

	private void setupViews() {
		try {
			configureManager = ConfigureManager.getInstance(getActivity());
			appContainer = AppContainer.getInstance();
			appContainer.registService(this);
		} catch (Exception e) {
			log("Exception in Constructor of SampleListFragment! "
					+ e.toString());
		}
		adapter = new SlideingMenuItemAdapter(getActivity());
		setListAdapter(adapter);
	}

	public void updateMessageNum() {
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				MenuItem menuItem = (MenuItem) adapter.getItem(2);
				menuItem.message_num = configureManager.getMessage_num();
				adapter.notifyDataSetChanged();
			}
		});
	}

	public void initSlidingMenuAdapter() {
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				adapter.clear();
				if (configureManager == null) {
					configureManager = ConfigureManager
							.getInstance(getActivity());
				}
				if (configureManager.isLogin_state()) {
					adapter.addItem(new MenuItem(configureManager
							.check_user_img(configureManager.getIconUrl()), 0,
							"用戶中心", 0, MenuItem.HEADER));
				} else {
					adapter.addItem(new MenuItem(null,
							R.drawable.icon_user_home, "登录/注册", 0,
							MenuItem.HEADER));
				}

//				adapter.addItem(new MenuItem(null, R.drawable.icon_shopping,
//						"商店", 3, MenuItem.SHOP));
				adapter.addItem(new MenuItem(null, R.drawable.icon_message,
						"私信", configureManager.getMessage_num(),
						MenuItem.MESSAGE));
				// adapter.addItem(new MenuItem(null, R.drawable.icon_collect,
				// "收藏", 0, MenuItem.COLLECTION));
				adapter.addItem(new MenuItem(null, R.drawable.icon_setting,
						"设置", 0, MenuItem.SETTINGS));
				adapter.addItem(new MenuItem(null, R.drawable.icon_about, "关于",
						0, MenuItem.ABOUT));
				if (configureManager.isLogin_state()) {
					adapter.addSeperator(new MenuItem(null, 0, "", 0));
					adapter.addItem(new MenuItem(null, R.drawable.icon_logout,
							"退出登录", 0, MenuItem.LOGOUT));
				}
				adapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		JSONObject jsonObject = new JSONObject();
		MenuItem menuItem = (MenuItem) adapter.getItem(position);
		switch (menuItem.label) {
		case MenuItem.HEADER:
			if (configureManager.isLogin_state()) {
				try {
					jsonObject.put("name",
							configureManager.getUserinfoActivity());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				try {
					jsonObject.put("name", configureManager.getLoginActivity());
					Bundle bundle = new Bundle();
					String username = configureManager.getUsername();
					if (username != null && !"".equals(username.trim())
							&& !"null".equals(username.trim())) {
						bundle.putString("username", username);
						jsonObject.put("bundle", bundle);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
			break;
		case MenuItem.SHOP:
			Toast.makeText(getActivity(), "商店功能正在开发中", Toast.LENGTH_SHORT)
					.show();
			break;
		case MenuItem.MESSAGE:
			try {
				String user_id = configureManager.getUser_id();
				if (user_id == null || "".equals(user_id)
						|| "null".equals(user_id)
						|| !configureManager.isLogin_state()) {
					Toast.makeText(getActivity(), "您还未登录，无法查看私信",
							Toast.LENGTH_SHORT).show();
					return;
				}
				jsonObject.put("name", configureManager.getChatListActivity());
			} catch (JSONException e2) {
				log(e2.toString());
			}
			break;
		case MenuItem.COLLECTION:
			try {
				jsonObject
						.put("name", configureManager.getCollectionActivity());
			} catch (Exception e) {
				log(e.toString());
			}
			break;
		case MenuItem.SETTINGS:
			try {
				jsonObject.put("name", configureManager.getSettingActivity());
			} catch (JSONException e3) {
				log(e3.toString());
			}
			break;
		case MenuItem.ABOUT:
			JSONObject jsonObject2 = new JSONObject();
			try {
				jsonObject2.put("name", configureManager.getAboutActivity());
				appContainer.startActivity(jsonObject2);
			} catch (Exception e2) {
				log("Exception in About: " + e2.toString());
			}
			break;
		case MenuItem.LOGOUT:
			jsonObject = ContentParameter.createJsonObject();
			try {
				jsonObject.put("method", "logout");
				jsonObject.put("user_id", configureManager.getUser_id());
				appContainer.request(jsonObject);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			return;
		}
		try {
			appContainer.startActivity(jsonObject);
		} catch (Exception e) {
			log("Error in StartActivity in FragmentList " + e.toString());
		}
	}

	private class MenuItem implements Cloneable {
		static final int HEADER = 0;
		static final int MESSAGE = 1;
		static final int COLLECTION = 2;
		static final int SETTINGS = 3;
		static final int ABOUT = 4;
		static final int LOGOUT = 5;
		static final int SEPERATOR = 6;
		static final int SHOP = 7;

		public String url;
		public int img;
		public String text;
		public int message_num;
		final int label;

		public MenuItem(String url, int img, String text, int num) {
			this.url = url;
			this.img = img;
			this.text = text;
			this.message_num = num;
			this.label = SEPERATOR;
		}

		public MenuItem(String url, int img, String text, int num, int label) {
			this.url = url;
			this.img = img;
			this.text = text;
			this.message_num = num;
			this.label = label;
		}

		@Override
		protected Object clone() throws CloneNotSupportedException {
			return super.clone();
		}

	}

	private class SlideingMenuItemAdapter extends BaseAdapter {

		private static final String TAG = "com.melonstudio.appworks.fragments.SlideingMenuItemAdapter";
		private static final int ITEM = 0;
		private static final int SEPERATOR = 1;
		private static final int TYPE_NUM = SEPERATOR + 1;
		private LayoutInflater inflater;
		private FinalBitmap finalBitmap;

		private List<MenuItem> list;
		private Context context;
		private TreeSet<Integer> seperator_position;

		public SlideingMenuItemAdapter(Context context) {
			this.context = context;
			this.list = new ArrayList<MenuItem>();
			this.seperator_position = new TreeSet<Integer>();
			inflater = LayoutInflater.from(this.context);
			finalBitmap = FinalBitmap.create(context);
		}

		public void clear() {
			if (list != null) {
				list.clear();
			}
			if (seperator_position != null) {
				seperator_position.clear();
			}
		}

		public void addItem(MenuItem menuItem) {
			if (list != null) {
				try {
					list.add((MenuItem) menuItem.clone());
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
			} else {
				log("MenuItem List is Null");
			}
		}

		public void addSeperator(MenuItem menuItem) {
			if (list != null) {
				try {
					list.add((MenuItem) menuItem.clone());
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
				seperator_position.add(list.size() - 1);
			} else {
				log("MenuItem List is Null");
			}
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
		public int getViewTypeCount() {
			return TYPE_NUM;
		}

		@Override
		public int getItemViewType(int position) {
			return (seperator_position.contains(position) ? SEPERATOR : ITEM);
		}

		@Override
		public boolean isEnabled(int position) {
			return (getItemViewType(position) == ITEM);
		}

		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint({ "ViewHolder", "InflateParams" })
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			int type = getItemViewType(position);
			switch (type) {
			case ITEM:
				convertView = inflater
						.inflate(R.layout.sliding_menu_item, null);
				CircularImage image = (CircularImage) convertView
						.findViewById(R.id.sliding_menu_item_header);
				TextView text = (TextView) convertView
						.findViewById(R.id.sliding_menu_item_text);
				TextView msgnum = (TextView) convertView
						.findViewById(R.id.sliding_menu_item_msgnum);
				MenuItem menuItem = (MenuItem) getItem(position);
				if (menuItem.url == null || menuItem.url.equals("")
						|| "null".equals(menuItem.url)) {
					image.setImageResource(menuItem.img);
				} else {
					finalBitmap.display(image, menuItem.url);
				}

				text.setText(menuItem.text);
				if (menuItem.message_num != 0) {
					msgnum.setText(String.valueOf(menuItem.message_num));
				}
				return convertView;
			case SEPERATOR:
				convertView = inflater
						.inflate(R.layout.group_list_header, null);
				TextView textView = (TextView) convertView
						.findViewById(R.id.sliding_menu_seperator);
				textView.setText("");
				return convertView;
			default:
				break;
			}
			return null;
		}

		private void log(String msg) {
			MyLog.log(TAG, msg);
		}

	}

	private void backPress() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Instrumentation instrumentation = new Instrumentation();
					instrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
				} catch (Exception e) {
					log("Exception when onBack   " + e.toString());
				}
			}
		}).start();
	}

	@Override
	public void handleMessage(Object object) {
		JSONObject jsonObject = (JSONObject) object;
		log(jsonObject.toString());
		String state;
		try {
			state = jsonObject.getString("state");
			if (state != null && "sucess".equals(state)) {
				log("logout succeed! msg: " + jsonObject.getString("msg"));
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(getActivity(), "登出成功！",
								Toast.LENGTH_SHORT).show();
						backPress();
						configureManager.setLogin_state(false);
						initSlidingMenuAdapter();
						adapter.notifyDataSetChanged();
					}
				});

			} else if (state != null && "error".equals(state)) {
				log("logout error! msg: " + jsonObject.getString("msg"));
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(getActivity(), "登出失败！",
								Toast.LENGTH_SHORT).show();
					}
				});
			} else {
				log("ERROR! msg: " + jsonObject.getString("msg"));
			}
		} catch (Exception e) {
			log("error in handleMessages " + e.toString());
		}
	}

	@Override
	public void onDestroy() {
		log("onDestroy in SampleListFragment");
		if (appContainer == null) {
			appContainer = AppContainer.getInstance();
		}
		appContainer.unregistService(this);
		super.onDestroy();
	}

}
