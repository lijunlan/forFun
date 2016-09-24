package com.melonstudio.appworks.fragments;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

import cn.bidaround.youtui_template.YouTuiViewType;
import cn.bidaround.youtui_template.YtTemplate;
import cn.bidaround.ytcore.data.ShareData;

import com.actionbarsherlock.app.SherlockFragment;
import com.melonstudio.appworks.AppContainer;
import com.melonstudio.appworks.R;
import com.melonstudio.clientconnector.ContentParameter;
import com.melonstudio.model.AbstractContent;
import com.melonstudio.model.ImageTextContent;
import com.melonstudio.util.ConfigureManager;
import com.melonstudio.util.MyLog;
import com.melonstudio.util.TimeUtil;

public class CollectionFragment extends SherlockFragment implements
		OnScrollListener {

	private static final String TAG = "com.melonstudio.appworks.fragments.CollectionFragment";

	private void log(String msg) {
		MyLog.log(TAG, msg);
	}

	private MainContentAdapter mainContentAdapter;
	private List<AbstractContent> contents = new ArrayList<AbstractContent>();
	private ListView listView;

	private AppContainer appContainer;
	private ConfigureManager configureManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mainContentAdapter = new MainContentAdapter(getSherlockActivity(),
				contents);

		AppContainer.setContext(getSherlockActivity());
		appContainer = AppContainer.getInstance();
		appContainer.registService(this);
		configureManager = ConfigureManager.getInstance(getSherlockActivity());
		getSherlockActivity().getSupportActionBar().setHomeButtonEnabled(true);
		listView.setAdapter(mainContentAdapter);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onStart() {
		if (appContainer == null) {
			AppContainer.setContext(getSherlockActivity());
			appContainer = AppContainer.getInstance();
		}
		appContainer.registService(this);
		if (configureManager == null) {
			configureManager = ConfigureManager
					.getInstance(getSherlockActivity());
		}
		super.onStart();
		mainContentAdapter.clear();
		new RefreshTask(RefreshTask.TYPE_REFRESH).execute(System
				.currentTimeMillis());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		appContainer.unregistService(this);
	}

	private class MainContentAdapter extends BaseAdapter {

		private static final String TAG = "com.melonstudio.appworks..MainContentAdapter";

		private Context context;
		private LayoutInflater layoutInflater;

		private FinalBitmap finalBitmap;

		private List<AbstractContent> contents;

		public MainContentAdapter(Context context,
				List<AbstractContent> contents) {
			/**
			 * to do some protection. I'm fraid that the contents passed by may
			 * be null
			 */
			if (contents != null) {
				this.contents = contents;
			} else {
				this.contents = new ArrayList<AbstractContent>();
			}
			this.context = context;
			layoutInflater = LayoutInflater.from(this.context);
			finalBitmap = FinalBitmap.create(this.context);
		}

		public void clear() {
			contents.clear();
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return contents.size();
		}

		@Override
		public Object getItem(int position) {
			return contents.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View view, ViewGroup parent) {

			ConfigureManager configureManager;
			configureManager = ConfigureManager.getInstance(context);

			ImageTextContent imageTextContent = (ImageTextContent) contents
					.get(position);

			ImgTextViewHolder viewHolder = null;
			if (view == null) {
				view = layoutInflater.inflate(R.layout.imgtext_item, parent,
						false);
				viewHolder = new ImgTextViewHolder();
				viewHolder.timeTextView = (TextView) view
						.findViewById(R.id.imgtext_item_time);
				viewHolder.imageView = (ImageView) view
						.findViewById(R.id.imgtext_item_img);
				viewHolder.titleTextView = (TextView) view
						.findViewById(R.id.imgtext_item_title);
				viewHolder.remarksTextView = (TextView) view
						.findViewById(R.id.imgtext_item_remarks);
				viewHolder.shareTextView = (TextView) view
						.findViewById(R.id.imgtext_item_shares);
				viewHolder.likesTextView = (TextView) view
						.findViewById(R.id.imgtext_item_likes);
				viewHolder.commentLayout = (LinearLayout) view
						.findViewById(R.id.imgtext_item_remarkclick);
				viewHolder.likeLayout = (LinearLayout) view
						.findViewById(R.id.imgtext_item_likeclick);
				viewHolder.shareLayout = (LinearLayout) view
						.findViewById(R.id.imgtext_item_shareclick);
				view.setTag(viewHolder);
			}
			viewHolder = (ImgTextViewHolder) view.getTag();

			viewHolder.commentLayout
					.setOnClickListener(new CommentClickListener(context,
							position));
			viewHolder.likeLayout.setOnClickListener(new LikeClickListener(
					position));
			viewHolder.shareLayout.setOnClickListener(new ShareClickListener(
					position));

			viewHolder.timeTextView.setText(TimeUtil
					.formatDateTime(imageTextContent.getTime()));
			finalBitmap.display(viewHolder.imageView, configureManager
					.check_user_img(imageTextContent.getImgPath()));
			viewHolder.titleTextView.setText(imageTextContent.getTitle());
			viewHolder.remarksTextView.setText(String.valueOf(imageTextContent
					.getComment_no()));
			viewHolder.likesTextView.setText(String.valueOf(imageTextContent
					.getPraise()));
			viewHolder.shareTextView.setText("0");
			return view;
		}

		/**
		 * duplicate filter works here
		 * 
		 * @param abstractContent
		 */
		public void add(AbstractContent abstractContent) {
			for (AbstractContent a : contents) {
				if (a.getId() == abstractContent.getId()) {
					log("update this!");
					a.copy(abstractContent);
					return;
				}
			}
			contents.add(abstractContent);
		}

		private void log(String msg) {
			MyLog.log(TAG, msg);
		}

	}

	// private static class TextImgViewHolder {
	//
	// }

	private static class ImgTextViewHolder {
		TextView timeTextView;
		ImageView imageView;
		TextView titleTextView;
		TextView remarksTextView;
		TextView likesTextView;
		TextView shareTextView;
		LinearLayout commentLayout;
		LinearLayout likeLayout;
		LinearLayout shareLayout;
	}

	private class LikeClickListener implements OnClickListener {

		private int position;

		public LikeClickListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			new RefreshTask(RefreshTask.TYPE_LIKE, position).execute();
		}
	}

	private class ShareClickListener implements OnClickListener {

		private AbstractContent abstractContent;

		public ShareClickListener(int position) {
			abstractContent = (AbstractContent) mainContentAdapter
					.getItem(position);
		}

		@Override
		public void onClick(View v) {
			YtTemplate blackTemplate = new YtTemplate(getSherlockActivity(),
					YouTuiViewType.WHITE_GRID, false);
			ShareData shareData = new ShareData();
			shareData.isAppShare = false;
			shareData.setDescription(String.valueOf(abstractContent.getTime()));
			shareData.setTitle(abstractContent.getTitle());
			shareData.setText("分享正文");
			shareData.setTarget_url("http://www.baidu.com");
			shareData.setImageUrl(configureManager.getDefault_image());
			blackTemplate.setShareData(shareData);
			blackTemplate.setPopwindowHeight(460);
			blackTemplate.setScreencapVisible(false);

			blackTemplate.show();

		}
	}

	private class CommentClickListener implements OnClickListener {

		private Context context;

		private int id;

		public CommentClickListener(Context context, int position) {
			this.context = context;
			ImageTextContent imageTextContent = (ImageTextContent) mainContentAdapter
					.getItem(position);
			id = imageTextContent.getId();
		}

		@Override
		public void onClick(View v) {
			ConfigureManager configureManager = ConfigureManager
					.getInstance(context);
			AppContainer appContainer = AppContainer.getInstance();
			JSONObject jsonObject = new JSONObject();
			try {
				Bundle bundle = new Bundle();
				bundle.putInt("passage_id", id);
				jsonObject.put("bundle", bundle);
				jsonObject.put("name", configureManager.getCommentAcitivity());
				appContainer.startActivity(jsonObject);
				jsonObject = ContentParameter.createJsonObject();
				jsonObject.put("method", "getCommentList");
				jsonObject.put("page", 1);
				jsonObject.put("id_type", "passage");
				jsonObject.put("formulate_time", false);
				jsonObject.put("passage_id", id);
				appContainer.request(jsonObject);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Refresh task to get update from the sever
	 * 
	 * @author guohaosu
	 * 
	 */
	private class RefreshTask extends AsyncTask<Long, Void, Void> {

		public RefreshTask(int type) {
			this.type = type;
			this.isLogin = configureManager.isLogin_state();
		}

		public RefreshTask(int type, int position) {
			this(type);
			this.position = position;
		}

		private JSONArray jsonArray;
		private JSONObject jsonObject;
		final static int TYPE_REFRESH = 0;
		final static int TYPE_LIKE = 1;
		int type;
		int position;
		boolean isLogin;
		boolean check;

		@Override
		protected Void doInBackground(Long... params) {
			switch (type) {
			case TYPE_REFRESH:
				try {
					JSONObject jsonObject = ContentParameter.createJsonObject();
					jsonObject.put("method", "getMessageList");
					jsonObject.put("refresh_time", params[0]);
					jsonObject.put("formulate_time", false);
					jsonArray = (JSONArray) appContainer
							.blockingRequest(jsonObject);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case TYPE_LIKE:
				if (!isLogin) {
					return null;
				} else {
					log("Begin to Check praise");
					ImageTextContent imageTextContent = (ImageTextContent) mainContentAdapter
							.getItem(position);
					JSONObject jsonObject = ContentParameter.createJsonObject();
					try {
						jsonObject
								.put("user_id", configureManager.getUser_id());
						jsonObject.put("type", "passage");
						jsonObject.put("passage_id", imageTextContent.getId());
						jsonObject.put("method", "blocking_checkPraise");
						this.jsonObject = (JSONObject) appContainer
								.blockingRequest(jsonObject);
						String check = this.jsonObject.getString("check");
						if ("yes".equals(check)) {
							this.check = true;
							return null;
						} else {
							this.check = false;
							jsonObject.put("user_id",
									configureManager.getUser_id());
							jsonObject.put("type", "passage");
							jsonObject.put("passage_id",
									imageTextContent.getId());
							jsonObject.put("method", "blocking_parise");
							this.jsonObject = (JSONObject) appContainer
									.blockingRequest(jsonObject);
							return null;
						}
					} catch (Exception e) {
						log("Check Praise Exception:  " + e.toString());
					}

				}
				break;
			default:
				break;
			}

			return null;
		}

		protected void onPostExecute(Void result) {
			switch (type) {
			case TYPE_REFRESH:
				if (jsonArray != null) {
					try {
						log(jsonArray.toString());
						for (int i = 0; i < jsonArray.length(); i++) {
							AbstractContent abstractContent = AbstractContent
									.create(jsonArray.getJSONObject(i));
							if (abstractContent != null) {
								mainContentAdapter.add(abstractContent);
							}
						}
						mainContentAdapter.notifyDataSetChanged();
					} catch (Exception e) {
						log(e.toString());
					}
				} else {
				}
				break;
			case TYPE_LIKE:
				if (!isLogin) {
					Toast.makeText(getSherlockActivity(), "您当前没有登录，无法操作",
							Toast.LENGTH_SHORT).show();
					log("Not Login, cannot zan!");
				} else {
					if (check) {
						Toast.makeText(getSherlockActivity(), "您在之前的日子里已经赞过我啦",
								Toast.LENGTH_SHORT).show();
						log("You zaned before!");
					} else if (jsonObject != null) {
						try {
							String state = jsonObject.getString("state");
							if ("error".equals(state)) {
								Toast.makeText(getSherlockActivity(), "点赞失败 ",
										Toast.LENGTH_SHORT).show();
								log("Zan Error!");
							} else {
								Toast.makeText(getSherlockActivity(), "点赞成功 ",
										Toast.LENGTH_SHORT).show();
								ImageTextContent imageTextContent = (ImageTextContent) mainContentAdapter
										.getItem(position);
								imageTextContent.setPraise(imageTextContent
										.getPraise() + 1);
								mainContentAdapter.notifyDataSetChanged();
							}
						} catch (Exception e) {
							log("Handle Message Error: " + e.toString());
						}
					}
				}
			default:
				break;
			}
		};
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		final int loadTotal = totalItemCount;
		int lastItemId = listView.getLastVisiblePosition();
		if ((lastItemId + 1) == loadTotal) {
			AbstractContent abstractContent = (AbstractContent) mainContentAdapter
					.getItem(totalItemCount - 1);
			if (abstractContent != null) {
				new RefreshTask(RefreshTask.TYPE_REFRESH)
						.execute(abstractContent.getTime());
			}
		}
	}
}
