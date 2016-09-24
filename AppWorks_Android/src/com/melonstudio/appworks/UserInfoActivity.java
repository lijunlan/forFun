package com.melonstudio.appworks;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.melonstudio.clientconnector.ContentParameter;
import com.melonstudio.util.Base64Coder;
import com.melonstudio.util.ConfigureManager;
import com.melonstudio.util.MyLog;

public class UserInfoActivity extends SherlockActivity implements
		OnItemSelectedListener {

	private static final String TAG = "com.melonstudio.appworks.UserInfoActivity";

	private AppContainer appContainer;
	private ConfigureManager configureManager;

	@ViewInject(id = R.id.my_userinfo_listview, itemClick = "onItemSelected")
	private ListView listView;

	private UserInfoActivity.UserInfoAdapter adapter;

	private Bitmap headerBitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		log("onCreate in UserInfoActivity");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userinfo);

		FinalActivity.initInjectedView(this);

		appContainer = AppContainer.getInstance();
		configureManager = ConfigureManager
				.getInstance(getApplicationContext());

		adapter = new UserInfoAdapter(getApplicationContext());

		listView.setAdapter(adapter);
		listView.setOnItemSelectedListener(this);

		getSupportActionBar().setHomeButtonEnabled(true);

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

		initAdapter();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (appContainer == null) {
			AppContainer.setContext(getApplicationContext());
			appContainer = AppContainer.getInstance();
		}
		if (configureManager == null) {
			configureManager = ConfigureManager
					.getInstance(getApplicationContext());
		}
	}

	private void initAdapter() {
		adapter.clear();
		adapter.addSeperator();
		adapter.add(new UserInfoItem("头像", configureManager.getIconUrl(),
				"iconUrl", HEADR));
		adapter.add(new UserInfoItem("用户名", configureManager.getUsername(),
				"username", ITEM_UNCLICKABLE));
		adapter.add(new UserInfoItem("昵称", configureManager.getName(), "name",
				ITEM));
		adapter.addSeperator();
		adapter.add(new UserInfoItem("年龄", configureManager.getAge(), "age",
				ITEM));
		adapter.add(new UserInfoItem("电子邮箱", configureManager.getEmail(),
				"email", ITEM));
		adapter.add(new UserInfoItem("手机", configureManager.getPhone(),
				"phone", ITEM));
		adapter.addSeperator();
		adapter.add(new UserInfoItem("个性签名", configureManager.getIntroduce(),
				"introduce", ITEM));
		adapter.addSeperator();
		adapter.notifyDataSetChanged();
	}

	@Override
	protected void onDestroy() {
		log("onDestroy in UserInfoActivity");
		appContainer.unregistService(this);
		super.onDestroy();
	}

	public void onItemSelected(UserInfoAdapter adapter, View view, int index,
			long l) {
		log("Select Item: " + index);
		if (index > 0) {
			UserInfoItem userInfoItem = (UserInfoItem) this.adapter
					.getItem(index);
			JSONObject jsonObject = userInfoItem.process_click();
			Bundle bundle = new Bundle();
			try {
				bundle.putString("key", jsonObject.getString("key"));
				bundle.putString("value", jsonObject.getString("value"));
				bundle.putString("keyword", jsonObject.getString("keyword"));
				jsonObject.put("name",
						configureManager.getUserinfoUpdateAcitivity());
				jsonObject.put("bundle", bundle);
				appContainer.startActivity(jsonObject, UserInfoActivity.this);
			} catch (Exception e) {
				MyLog.log(TAG, e.toString());
			}
		}
		if (HEADR == adapter.getItemViewType(index)) {
			pick_header(view);
		}
	}

	private void log(String msg) {
		MyLog.log(TAG, msg);
	}

	public void pick_header(View view) {
		Intent intent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, SELECT_IMG);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == SELECT_IMG && resultCode == RESULT_OK) {
			log("Receive SELECT Result!");
			Uri uri = data.getData();
			Intent intent = new Intent();
			intent.setAction("com.android.camera.action.CROP");
			intent.setDataAndType(uri, "image/*");
			intent.putExtra("crop", true);
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("outputX", 150);
			intent.putExtra("outputY", 150);
			intent.putExtra("return-data", true);
			startActivityForResult(intent, CUT_IMG);
			// header.setImageURI(uri);
		} else if (requestCode == CUT_IMG && resultCode == RESULT_OK) {
			log("Receive CUT Result!");
			headerBitmap = data.getParcelableExtra("data");
			uploadHeader();

		}
	}

	private void uploadHeader() {
		if (headerBitmap != null) {
			AjaxParams params = new AjaxParams();
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			headerBitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
			byte[] b = stream.toByteArray();
			// 将图片流以字符串形式存储下来
			String file = new String(Base64Coder.encodeLines(b));
			params.put("file", file);
			FinalHttp finalHttp = new FinalHttp();
			finalHttp.addHeader("Accept",
					"text/javascript, text/html, application/xml, text/xml");
			finalHttp.addHeader("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
			finalHttp.addHeader("Accept-Encoding", "gzip,deflate,sdch");
			finalHttp.addHeader("Connection", "Keep-Alive");
			finalHttp.addHeader("Cache-Control", "no-cache");
			finalHttp.addHeader("Content-Type",
					"application/x-www-form-urlencoded");
			finalHttp.post(configureManager.getUploadImageUrl(), params,
					new AjaxCallBack<String>() {
						@Override
						public void onLoading(long count, long current) {
							super.onLoading(count, current);
						}

						@Override
						public void onSuccess(String t) {
							super.onSuccess(t);
							log("upload success!");
							try {
								JSONObject jsonObject = new JSONObject(t);
								String iconUrl = jsonObject.getString("url");
								new InfoUpdateTask().execute(
										InfoUpdateTask.TYPE_HEADER, iconUrl);
							} catch (Exception e) {
								log(e.toString());
							}
							log(t.toString());
						}

						@Override
						public void onFailure(Throwable t, int errorNo,
								String strMsg) {
							super.onFailure(t, errorNo, strMsg);
							log("upload failed!");
						}
					});
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

	private void updateHeader() {
		if (headerBitmap != null) {
			UserInfoItem userInfoItem = (UserInfoItem) adapter
					.getItem(adapter.header_position);
			userInfoItem.setBitmap(headerBitmap);
			adapter.notifyDataSetChanged();
		}
	}

	private final int SELECT_IMG = 1;
	private final int CUT_IMG = 2;

	private class UserInfoItem {
		String key;
		String value;
		String keyword;
		int type;
		Bitmap bitmap;

		public UserInfoItem(int TYPE) {
			type = TYPE;
		}

		public UserInfoItem(String key, String value, String keywrod, int TYPE) {
			this.key = key;
			this.value = value;
			this.keyword = keywrod;
			type = TYPE;
		}

		public void setBitmap(Bitmap bitmap) {
			this.bitmap = bitmap;
			value = null;
		}

		public JSONObject process_click() {
			if (type == ITEM) {
				JSONObject jsonObject = new JSONObject();
				try {
					jsonObject.put("key", key);
					jsonObject.put("value", value);
					jsonObject.put("keyword", keyword);
					return jsonObject;
				} catch (Exception e) {
					log("Exceptrion: " + e.toString());
				}
			}
			return null;

		}
	}

	private static final int HEADR = 0;
	private static final int ITEM = 1;
	private static final int ITEM_UNCLICKABLE = 2;
	private static final int SEPERATOR = 3;
	private static final int TYPE_NUM = SEPERATOR + 1;

	private class UserInfoAdapter extends BaseAdapter {

		private final static String TAG = "com.melonstudio.appworks.UserInfoActivity.UserInfoAdapter";

		private void log(String msg) {
			MyLog.log(TAG, msg);
		}

		private List<UserInfoItem> list;
		private TreeSet<Integer> seperator_position;
		private LayoutInflater layoutInflater;
		private FinalBitmap finalBitmap;
		int header_position;

		public void add(UserInfoItem userInfoItem) {
			if (list != null) {
				list.add(userInfoItem);
				this.notifyDataSetChanged();
			} else {
				log("list is null");
			}

		}

		public void addSeperator() {
			if (list != null) {
				list.add(new UserInfoItem(SEPERATOR));
				seperator_position.add(list.size() - 1);
				notifyDataSetChanged();
			} else {
				log("List is Null");
			}
		}

		public void clear() {
			if (list != null) {
				list.clear();
			}
			if (seperator_position != null) {
				seperator_position.clear();
			}
			this.notifyDataSetChanged();
		}

		public UserInfoAdapter(Context context) {
			list = new ArrayList<UserInfoItem>();
			layoutInflater = LayoutInflater.from(context);
			finalBitmap = FinalBitmap.create(context);
			seperator_position = new TreeSet<Integer>();
		}

		@Override
		public int getViewTypeCount() {
			return TYPE_NUM;
		}

		@Override
		public int getItemViewType(int position) {
			if (seperator_position.contains(position)) {
				return SEPERATOR;
			} else {
				UserInfoItem userInfoItem = (UserInfoItem) getItem(position);
				return userInfoItem.type;
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
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			OtherViewHolder otherViewHolder = null;
			HeaderViewHolder headerViewHolder = null;
			UserInfoItem userInfoItem = list.get(position);
			int type = getItemViewType(position);
			if (convertView == null) {
				switch (type) {
				case HEADR:
					convertView = layoutInflater.inflate(
							R.layout.userinfo_header_item, parent, false);
					headerViewHolder = new HeaderViewHolder();
					headerViewHolder.imageView = (ImageView) convertView
							.findViewById(R.id.userinfo_item_header);
					headerViewHolder.keyTextView = (TextView) convertView
							.findViewById(R.id.userinfo_header_item_key);
					headerViewHolder.keyTextView.setText(userInfoItem.key
							.trim());
					if (userInfoItem.value != null) {
						finalBitmap.display(headerViewHolder.imageView,
								configureManager
										.check_user_img(userInfoItem.value));
					} else {
						headerViewHolder.imageView
								.setImageBitmap(userInfoItem.bitmap);
					}
					convertView.setBackgroundColor(getResources().getColor(
							R.color.white));
					convertView.setTag(R.layout.userinfo_header_item,
							headerViewHolder);
					break;
				case ITEM:
				case ITEM_UNCLICKABLE:
					convertView = layoutInflater.inflate(
							R.layout.userinfo_other_item, parent, false);
					otherViewHolder = new OtherViewHolder();
					otherViewHolder.keyTextView = (TextView) convertView
							.findViewById(R.id.userinfo_other_item_key);
					otherViewHolder.valueTextView = (TextView) convertView
							.findViewById(R.id.userinfo_other_item_value);
					otherViewHolder.keyTextView.setText(userInfoItem.key);
					convertView.setBackgroundColor(getResources().getColor(
							R.color.white));
					otherViewHolder.valueTextView.setText(userInfoItem.value);
					convertView.setTag(R.layout.userinfo_other_item,
							otherViewHolder);
					break;
				case SEPERATOR:
					convertView = layoutInflater.inflate(
							R.layout.userinfo_other_item, parent, false);
					otherViewHolder = new OtherViewHolder();
					otherViewHolder.keyTextView = (TextView) convertView
							.findViewById(R.id.userinfo_other_item_key);
					otherViewHolder.valueTextView = (TextView) convertView
							.findViewById(R.id.userinfo_other_item_value);
					otherViewHolder.keyTextView.setText("");
					otherViewHolder.valueTextView.setText("");
					convertView.setTag(R.layout.userinfo_other_item,
							otherViewHolder);
					break;
				default:
					break;
				}
			} else {
				switch (type) {
				case HEADR:
					headerViewHolder = (HeaderViewHolder) convertView
							.getTag(R.layout.userinfo_header_item);
					headerViewHolder.imageView = (ImageView) convertView
							.findViewById(R.id.userinfo_item_header);
					headerViewHolder.keyTextView = (TextView) convertView
							.findViewById(R.id.userinfo_header_item_key);
					headerViewHolder.keyTextView.setText(userInfoItem.key
							.trim());
					if (userInfoItem.value != null) {
						finalBitmap.display(headerViewHolder.imageView,
								configureManager
										.check_user_img(userInfoItem.value));
					} else {
						headerViewHolder.imageView
								.setImageBitmap(userInfoItem.bitmap);
					}
					convertView.setBackgroundColor(getResources().getColor(
							R.color.white));
					break;
				case ITEM:
				case ITEM_UNCLICKABLE:
					otherViewHolder = (OtherViewHolder) convertView
							.getTag(R.layout.userinfo_other_item);
					otherViewHolder.keyTextView.setText(userInfoItem.key);
					convertView.setBackgroundColor(getResources().getColor(
							R.color.white));
					otherViewHolder.valueTextView.setText(userInfoItem.value);
					break;
				case SEPERATOR:
					otherViewHolder = (OtherViewHolder) convertView
							.getTag(R.layout.userinfo_other_item);
					otherViewHolder.keyTextView.setText("");
					otherViewHolder.valueTextView.setText("");
					break;
				default:
					break;
				}
			}
			return convertView;
		}

		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}

		@Override
		public boolean isEnabled(int position) {
			return (list.get(position).type != SEPERATOR);
		}
	}

	class InfoUpdateTask extends AsyncTask<Object, Integer, Void> {

		final static int TYPE_HEADER = 0;
		final static int TYPE_NAME = 1;
		final static int TYPE_AGE = 2;
		final static int TYPE_INTRODUCE = 3;
		final static int TYPE_EMAIL = 4;
		final static int TYPE_PHONE = 5;
		final static int TYPE_SEX = 6;

		int type;
		JSONObject jsonObject;

		@Override
		protected Void doInBackground(Object... params) {
			type = (int) params[0];
			switch (type) {
			case TYPE_HEADER:
				JSONObject jo = ContentParameter.createJsonObject();
				try {
					String iconUrl = (String) params[1];
					jo.put("user_id", configureManager.getUser_id());
					jo.put("iconUrl", iconUrl);
					jo.put("method", "blocking_changeInfo");
					jsonObject = (JSONObject) appContainer.blockingRequest(jo);
				} catch (Exception e) {
					log("Exceptrion: " + e.toString());
				}
				break;
			case TYPE_NAME:
				break;
			case TYPE_AGE:
				break;
			case TYPE_INTRODUCE:
				break;
			case TYPE_EMAIL:
				break;
			case TYPE_PHONE:
				break;
			case TYPE_SEX:
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
			case TYPE_HEADER:
				if (jsonObject != null) {
					try {
						String state = jsonObject.getString("state");
						log("state: " + state);
						if ("success".equals(state)) {
							configureManager.updateLocalInfo(jsonObject);
							updateHeader();
						} else {
							Toast.makeText(getApplicationContext(), "更换头像失败",
									Toast.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						log(e.toString());
					}
				} else {
					log("Exception in post Header: jsonObject is null");
				}
				break;
			case TYPE_NAME:
				break;
			case TYPE_AGE:
				break;
			case TYPE_INTRODUCE:
				break;
			case TYPE_EMAIL:
				break;
			case TYPE_PHONE:
				break;
			case TYPE_SEX:
				break;
			default:
				break;
			}
		}

	}

	private static class OtherViewHolder {
		TextView keyTextView;
		TextView valueTextView;
	}

	private static class HeaderViewHolder {
		TextView keyTextView;
		ImageView imageView;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int index,
			long id) {
		log("Select Item: " + index);
		if (index > 0) {
			UserInfoItem userInfoItem = (UserInfoItem) this.adapter
					.getItem(index);
			JSONObject jsonObject = userInfoItem.process_click();
			Bundle bundle = new Bundle();
			try {
				bundle.putString("key", jsonObject.getString("key"));
				bundle.putString("value", jsonObject.getString("value"));
				bundle.putString("keyword", jsonObject.getString("keyword"));
				jsonObject.put("name",
						configureManager.getUserinfoUpdateAcitivity());
				jsonObject.put("bundle", bundle);
				appContainer.startActivity(jsonObject, UserInfoActivity.this);
			} catch (Exception e) {
				MyLog.log(TAG, e.toString());
			}
		}
		if (HEADR == adapter.getItemViewType(index)) {
			pick_header(view);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}
}
