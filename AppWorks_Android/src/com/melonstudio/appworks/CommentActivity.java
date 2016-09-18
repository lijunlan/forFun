package com.melonstudio.appworks;

import java.util.ArrayList;
import java.util.List;

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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.melonstudio.clientconnector.ContentParameter;
import com.melonstudio.model.Comment;
import com.melonstudio.ui.CircularImage;
import com.melonstudio.util.ConfigureManager;
import com.melonstudio.util.MyLog;
import com.melonstudio.util.TimeUtil;

/**
 * 
 * @author guohaosu
 * 
 */
public class CommentActivity extends SherlockActivity implements IActivity,
		OnScrollListener {

	private static final String TAG = "com.melonstudio.appworks.CommentActivity";

	@ViewInject(id = R.id.comments_send_comment, click = "send_button_click")
	private ImageButton sendButton;
	@ViewInject(id = R.id.comments_input)
	private EditText editText;

	private LinearLayout footerView;

	@ViewInject(id = R.id.comments_list)
	private ListView listview;
	private CommentAdapter adapter;

	private AppContainer appContainer;
	private ConfigureManager configureManager;
	private FinalBitmap finalBitmap;

	private InputMethodManager inputMethodManager;

	private long passage_id;

	private int next_page = 1;

	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comments);
		log("onCreate in CommentActivity!");

		FinalActivity.initInjectedView(this);

		appContainer = AppContainer.getInstance();
		configureManager = ConfigureManager
				.getInstance(getApplicationContext());
		finalBitmap = FinalBitmap.create(getApplicationContext());

		List<Comment> list = new ArrayList<Comment>();

		adapter = new CommentAdapter(getApplicationContext(), list);

		footerView = (LinearLayout) LayoutInflater
				.from(getApplicationContext()).inflate(
						R.layout.listview_footer, null);
		listview.addFooterView(footerView);
		hideFooter();
		listview.setAdapter(adapter);
		listview.setOnScrollListener(this);

		Intent intent = getIntent();
		if (intent.hasExtra("bundle")) {
			Bundle bundle = intent.getBundleExtra("bundle");
			this.passage_id = bundle.getInt("passage_id");
			log("passage_id: " + this.passage_id);
		}

		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		getSupportActionBar().setHomeButtonEnabled(true);

		new CommentTask().execute(CommentTask.TYPE_GETLIST, next_page);
	}

	@Override
	protected void onStart() {
		if (appContainer == null) {
			AppContainer.setContext(getApplicationContext());
			appContainer = AppContainer.getInstance();
		}
		appContainer.registService(this);
		if (configureManager == null) {
			configureManager = ConfigureManager
					.getInstance(getApplicationContext());
		}
		super.onStart();
	}

	@Override
	protected void onDestroy() {
		appContainer.unregistService(this);
		log("onDestroy in CommentActivity!");
		super.onDestroy();
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
	public void handleMessage(final Object object) {

	}

	public void refresh_click(View view) {
		log("refresh button clicked!");
	}

	public void send_button_click(View view) {
		log("send button click!");
		new CommentTask().execute(CommentTask.TYPE_SEND);
	}

	/**
	 * check the login state. That is to say, whether the user can send comment
	 * 
	 * @return if the user can send comment, return True; else return False
	 */
	private boolean check_login_state() {
		if (configureManager.isLogin_state()) {
			return true;
		} else {
			return false;
		}
	}

	private void log(String msg) {
		MyLog.log(TAG, msg);
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

	private static class ViewHolder {
		CircularImage header;
		TextView nick;
		TextView content;
		TextView time;
	}

	private class UserClickListener implements View.OnClickListener {

		private int position;

		public UserClickListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			Comment comment = (Comment) adapter.getItem(position);
			log(comment.getUser_id() + " click");
			JSONObject jsonObject = ContentParameter.createJsonObject();
			try {
				jsonObject.put("method", "get_user_info");
				jsonObject.put("user_id", comment.getUser_id());
				if (appContainer != null) {
					appContainer.request(jsonObject);
				}
			} catch (Exception e) {
				log(e.toString());
			}
		}

	}

	private class CommentAdapter extends BaseAdapter {

		private List<Comment> list;
		private Context context;

		public CommentAdapter(Context context, List<Comment> comments) {
			this.context = context;
			this.list = new ArrayList<Comment>(comments);

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

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder viewHolder = null;
			Comment comment = (Comment) getItem(position);
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.commnets_list_item, parent, false);
				viewHolder.header = (CircularImage) convertView
						.findViewById(R.id.comment_item_header);
				viewHolder.nick = (TextView) convertView
						.findViewById(R.id.comment_user_id);
				viewHolder.content = (TextView) convertView
						.findViewById(R.id.comment_user_content);
				viewHolder.time = (TextView) convertView
						.findViewById(R.id.comment_user_time);
				convertView.setTag(viewHolder);
			}

			viewHolder = (ViewHolder) convertView.getTag();
			finalBitmap.display(viewHolder.header,
					configureManager.check_user_img(comment.getUser_url()));
			viewHolder.nick.setText(comment.getUser_name());
			viewHolder.content.setText(comment.getContent());
			viewHolder.time.setText(TimeUtil.formatDateTime(Long
					.parseLong(comment.getTime())));

			viewHolder.nick.setOnClickListener(new UserClickListener(position));
			viewHolder.header
					.setOnClickListener(new UserClickListener(position));
			return convertView;
		}

		public void append(Comment comment) {
			list.add(list.size(), comment);
			this.notifyDataSetChanged();
		}

		public void appendAll(List<Comment> comments) {
			for (int i = 0; i < comments.size(); i++) {
				list.add(comments.get(i));
			}
			this.notifyDataSetChanged();
		}

		@SuppressWarnings("unused")
		public void prepend(Comment comment) {
			list.add(0, comment);
			this.notifyDataSetChanged();
		}

	}

	private class CommentTask extends AsyncTask<Object, Intent, Void> {

		static final int TYPE_GETLIST = 0;
		static final int TYPE_SEND = 1;

		int type;
		boolean isLogin = true;
		JSONObject jsonObject;
		JSONArray jsonArray;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showFooter();
		}

		@Override
		protected Void doInBackground(Object... params) {
			type = (int) params[0];
			JSONObject jo = ContentParameter.createJsonObject();
			switch (type) {
			case TYPE_GETLIST:
				int page_num = (int) params[1];
				try {
					jo.put("method", "blocking_getCommentList");
					jo.put("page", page_num);
					jo.put("id_type", "passage");
					jo.put("formulate_time", false);
					jo.put("passage_id", passage_id);
					jsonArray = (JSONArray) appContainer.blockingRequest(jo);
					log("blocking comments arrived!");
				} catch (Exception e) {
					log("Exception in get comment list: " + e.toString());
				}
				break;
			case TYPE_SEND:
				log("send button click!");
				if (check_login_state()) {
					log("the user can send the comment");
					try {
						jo.put("passage_id", passage_id);
						jo.put("user_id", configureManager.getUser_id());
						jo.put("content", editText.getText().toString().trim());
						jo.put("method", "blocking_uploadComment");
						jsonObject = (JSONObject) appContainer
								.blockingRequest(jo);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					isLogin = false;
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
			case TYPE_GETLIST:
				if (jsonArray != null) {
					try {
						adapter.appendAll(Comment
								.createFromJsonArray(jsonArray));
						adapter.notifyDataSetChanged();
						if (next_page == 1) {
							listview.setSelection(adapter.getCount() - 1);
						}
						next_page++;
					} catch (Exception e) {
						log("Exception in appending comments: " + e.toString());
					}
				}
				hideFooter();
				break;
			case TYPE_SEND:
				if (isLogin) {
					if (jsonObject != null) {
						try {
							String state = jsonObject.getString("state");
							if ("error".equals(state)) {
								log("send comment error");
								String msg = jsonObject.getString("msg");
								Toast.makeText(getApplicationContext(),
										"发送评论失败：" + msg, Toast.LENGTH_SHORT)
										.show();
							} else {
								log("Send comment success");
								Comment comment = new Comment();
								comment.setUser_id(configureManager
										.getUser_id());
								comment.setUser_url(configureManager
										.getIconUrl());
								comment.setUser_name(configureManager.getName());
								comment.setContent(editText.getText()
										.toString().trim());
								adapter.append(comment);
								editText.setText("");
								editText.clearFocus();
							}
						} catch (Exception e) {
							log("Exception in post send: " + e.toString());
						}

					} else {
						log("JsonObject is null");
					}
				} else {
					Toast.makeText(getApplicationContext(), "您还没有登录，无法进行操作",
							Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;
			}
		}

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		int itemsLastIndex = adapter.getCount() - 1;
		int lastIndex = itemsLastIndex + 1;
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& visibleLastIndex == lastIndex) {
			new CommentTask().execute(CommentTask.TYPE_GETLIST, next_page);
		}

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
	}

	private int visibleLastIndex;

	private void hideFooter() {
		footerView.setVisibility(View.GONE);
		footerView.setPadding(0, -footerView.getHeight(), 0, 0);
	}

	private void showFooter() {
		footerView.setVisibility(View.VISIBLE);
		footerView.setPadding(0, 0, 0, 0);
	}
}
