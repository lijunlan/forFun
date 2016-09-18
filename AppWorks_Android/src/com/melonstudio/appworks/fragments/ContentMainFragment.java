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
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import cn.bidaround.youtui_template.YouTuiViewType;
import cn.bidaround.youtui_template.YtTemplate;
import cn.bidaround.ytcore.data.ShareData;

import com.actionbarsherlock.app.SherlockFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.melonstudio.appworks.AppContainer;
import com.melonstudio.appworks.R;
import com.melonstudio.clientconnector.ContentParameter;
import com.melonstudio.model.AbstractContent;
import com.melonstudio.model.ImageTextContent;
import com.melonstudio.model.PassageTextContent;
import com.melonstudio.model.PureTextContent;
import com.melonstudio.util.ConfigureManager;
import com.melonstudio.util.MyLog;
import com.melonstudio.util.TimeUtil;

public class ContentMainFragment extends SherlockFragment {
	private static final String TAG = "com.melonstudio.appworks.fragments.ContentMainFragment";

	private void log(String msg) {
		MyLog.log(TAG, msg);
	}

	private PullToRefreshListView mListView;
	private MainContentAdapter mainContentAdapter;
	private List<AbstractContent> contents = new ArrayList<AbstractContent>();

	private ConfigureManager configureManager;
	private AppContainer appContainer;

	private ResponseActivity responseActivity;

	private boolean isTwoPane;
	private boolean isStart;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		log("onAttach in ContentMainFragment");
		responseActivity = (ResponseActivity) activity;
	}

	@Override
	public void onDetach() {
		log("onDetach in ContentMainFragment");
		super.onDetach();
		responseActivity = null;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		log("onCreate in ContentMainFragment");
		super.onCreate(savedInstanceState);
		isStart = false;
		try {
			isTwoPane = getArguments().getBoolean("isTwoPane");
			if (isTwoPane) {
				isStart = true;
			}
			log("isTwoPane is : " + isTwoPane);
		} catch (Exception e) {
			log(e.toString());
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		log("onCreateView in ContentMainFragment");
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_main_content, container,
				false);
		mListView = (PullToRefreshListView) view
				.findViewById(R.id.main_p2r_listview);

		configureManager = ConfigureManager.getInstance(getSherlockActivity());
		AppContainer.setContext(getSherlockActivity());
		appContainer = AppContainer.getInstance();
		appContainer.registService(this);

		mainContentAdapter = new MainContentAdapter(getSherlockActivity(),
				contents);

		initptrListView();

		getSherlockActivity().getSupportActionBar().setTitle(
				getResources().getString(R.string.app_name));

		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		log("onStart in ContentMainFragment");
		if (appContainer == null) {
			AppContainer.setContext(getSherlockActivity());
			appContainer = AppContainer.getInstance();
		}
		appContainer.registService(this);
		if (configureManager == null) {
			configureManager = ConfigureManager
					.getInstance(getSherlockActivity());
		}

		mainContentAdapter.clear();
		new RefreshTask(RefreshTask.TYPE_REFRESH).execute(System
				.currentTimeMillis());
	}

	@Override
	public void onDestroy() {
		log("onDestroy in ContentMainFragment");
		appContainer.unregistService(this);
		super.onDestroy();
	}

	private void initptrListView() {
		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getSherlockActivity(),
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);

				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				new RefreshTask(RefreshTask.TYPE_REFRESH).execute(System
						.currentTimeMillis());
			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				onItemSelected(parent, view, position, id);
			}
		});
		mListView.setOnLastItemVisibleListener(new AutoLoaderListener());
		mListView.setAdapter(mainContentAdapter);
	}

	/**
	 * 对ListView的点击事件作出响应
	 * 
	 * @param adapter
	 * @param view
	 * @param index
	 * @param l
	 */
	public void onItemSelected(AdapterView<?> adapter, View view, int index,
			long l) {
		// contents.get(index - 1).openUp(getSherlockActivity());
		responseActivity.response(contents.get(index - 1));
	}

	private class AutoLoaderListener implements OnLastItemVisibleListener {

		@Override
		public void onLastItemVisible() {
			try {
				new RefreshTask(RefreshTask.TYPE_REFRESH).execute(contents.get(
						contents.size() - 1).getTime());
			} catch (Exception e) {
				log("loading error: " + e.toString());
			}
		}

	}

	private class MainContentAdapter extends BaseAdapter {

		private static final String TAG = "com.melonstudio.appworks.ContentMainActivity.MainContentAdapter";

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

		@Override
		public int getItemViewType(int position) {
			AbstractContent abstractContent = (AbstractContent) getItem(position);
			return abstractContent.getType();
		}

		@Override
		public int getViewTypeCount() {
			return AbstractContent.TYPE_COUNT;
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

			AbstractContent abstractContent = (AbstractContent) contents
					.get(position);

			ImgTextViewHolder imgTextViewHolder = null;
			PureTextViewHolder pureTextViewHolder = null;
			PassageTextViewHolder passageTextViewHolder = null;

			if (view == null) {
				switch (abstractContent.getType()) {
				case AbstractContent.PURE_TEXT:
					pureTextViewHolder = new PureTextViewHolder();
					view = layoutInflater.inflate(R.layout.puretext_item,
							parent, false);
					pureTextViewHolder.textView = (TextView) view
							.findViewById(R.id.puretext_text);
					pureTextViewHolder.timeView = (TextView) view
							.findViewById(R.id.puretext_item_time);
					pureTextViewHolder.remarksTextView = (TextView) view
							.findViewById(R.id.puretext_item_remarks);
					pureTextViewHolder.shareTextView = (TextView) view
							.findViewById(R.id.puretext_item_shares);
					pureTextViewHolder.likesTextView = (TextView) view
							.findViewById(R.id.puretext_item_likes);
					pureTextViewHolder.commentLayout = (LinearLayout) view
							.findViewById(R.id.puretext_item_remarkclick);
					pureTextViewHolder.likeLayout = (LinearLayout) view
							.findViewById(R.id.puretext_item_likeclick);
					pureTextViewHolder.shareLayout = (LinearLayout) view
							.findViewById(R.id.puretext_item_shareclick);
					view.setTag(R.layout.puretext_item, pureTextViewHolder);
					break;
				case AbstractContent.IMG_TEXT:
					view = layoutInflater.inflate(R.layout.imgtext_item,
							parent, false);
					imgTextViewHolder = new ImgTextViewHolder();
					imgTextViewHolder.timeTextView = (TextView) view
							.findViewById(R.id.imgtext_item_time);
					imgTextViewHolder.imageView = (ImageView) view
							.findViewById(R.id.imgtext_item_img);
					imgTextViewHolder.titleTextView = (TextView) view
							.findViewById(R.id.imgtext_item_title);

					imgTextViewHolder.remarksTextView = (TextView) view
							.findViewById(R.id.imgtext_item_remarks);
					imgTextViewHolder.shareTextView = (TextView) view
							.findViewById(R.id.imgtext_item_shares);
					imgTextViewHolder.likesTextView = (TextView) view
							.findViewById(R.id.imgtext_item_likes);
					imgTextViewHolder.commentLayout = (LinearLayout) view
							.findViewById(R.id.imgtext_item_remarkclick);
					imgTextViewHolder.likeLayout = (LinearLayout) view
							.findViewById(R.id.imgtext_item_likeclick);
					imgTextViewHolder.shareLayout = (LinearLayout) view
							.findViewById(R.id.imgtext_item_shareclick);
					view.setTag(R.layout.imgtext_item, imgTextViewHolder);
					break;
				case AbstractContent.PASSAGE_TEXT:
					passageTextViewHolder = new PassageTextViewHolder();
					view = layoutInflater.inflate(R.layout.passage_item,
							parent, false);
					passageTextViewHolder.timeTextView = (TextView) view
							.findViewById(R.id.passage_item_time);
					passageTextViewHolder.imageView = (ImageView) view
							.findViewById(R.id.passage_item_img);
					passageTextViewHolder.titleTextView = (TextView) view
							.findViewById(R.id.passage_item_title);
					passageTextViewHolder.remarksTextView = (TextView) view
							.findViewById(R.id.passage_item_remarks);
					passageTextViewHolder.shareTextView = (TextView) view
							.findViewById(R.id.passage_item_shares);
					passageTextViewHolder.likesTextView = (TextView) view
							.findViewById(R.id.passage_item_likes);
					passageTextViewHolder.commentLayout = (LinearLayout) view
							.findViewById(R.id.passage_item_remarkclick);
					passageTextViewHolder.likeLayout = (LinearLayout) view
							.findViewById(R.id.passage_item_likeclick);
					passageTextViewHolder.shareLayout = (LinearLayout) view
							.findViewById(R.id.passage_item_shareclick);
					view.setTag(R.layout.passage_item, passageTextViewHolder);
					break;
				default:
					break;
				}

			}
			switch (abstractContent.getType()) {
			case AbstractContent.PURE_TEXT:
				PureTextContent pureTextContent = (PureTextContent) abstractContent;
				pureTextViewHolder = (PureTextViewHolder) view
						.getTag(R.layout.puretext_item);
				pureTextViewHolder.commentLayout
						.setOnClickListener(new CommentClickListener(context,
								position));
				pureTextViewHolder.likeLayout
						.setOnClickListener(new LikeClickListener(position));
				pureTextViewHolder.shareLayout
						.setOnClickListener(new ShareClickListener(position));
				pureTextViewHolder.textView.setText(pureTextContent
						.getContent());
				pureTextViewHolder.timeView.setText(TimeUtil
						.formatDateTime(pureTextContent.getTime()));
				pureTextViewHolder.remarksTextView.setText(String
						.valueOf(pureTextContent.getComment_no()));
				pureTextViewHolder.likesTextView.setText(String
						.valueOf(pureTextContent.getPraise()));
				pureTextViewHolder.shareTextView.setText(String
						.valueOf(pureTextContent.getShare()));
				break;
			case AbstractContent.IMG_TEXT:
				imgTextViewHolder = (ImgTextViewHolder) view
						.getTag(R.layout.imgtext_item);
				ImageTextContent imageTextContent = (ImageTextContent) abstractContent;
				imgTextViewHolder.commentLayout
						.setOnClickListener(new CommentClickListener(context,
								position));
				imgTextViewHolder.likeLayout
						.setOnClickListener(new LikeClickListener(position));
				imgTextViewHolder.shareLayout
						.setOnClickListener(new ShareClickListener(position));

				imgTextViewHolder.timeTextView.setText(TimeUtil
						.formatDateTime(imageTextContent.getTime()));
				finalBitmap.display(imgTextViewHolder.imageView,
						configureManager.check_user_img(imageTextContent
								.getImgPath()));
				imgTextViewHolder.titleTextView.setText(imageTextContent
						.getTitle());
				imgTextViewHolder.remarksTextView.setText(String
						.valueOf(imageTextContent.getComment_no()));
				imgTextViewHolder.likesTextView.setText(String
						.valueOf(imageTextContent.getPraise()));
				imgTextViewHolder.shareTextView.setText(String
						.valueOf(imageTextContent.getShare()));
				break;
			case AbstractContent.PASSAGE_TEXT:
				passageTextViewHolder = (PassageTextViewHolder) view
						.getTag(R.layout.passage_item);
				PassageTextContent passageTextContent = (PassageTextContent) abstractContent;
				passageTextViewHolder.commentLayout
						.setOnClickListener(new CommentClickListener(context,
								position));
				passageTextViewHolder.likeLayout
						.setOnClickListener(new LikeClickListener(position));
				passageTextViewHolder.shareLayout
						.setOnClickListener(new ShareClickListener(position));

				passageTextViewHolder.timeTextView.setText(TimeUtil
						.formatDateTime(passageTextContent.getTime()));
				finalBitmap.display(passageTextViewHolder.imageView,
						configureManager.check_user_img(passageTextContent
								.getImgPath()));
				passageTextViewHolder.titleTextView.setText(passageTextContent
						.getTitle());
				passageTextViewHolder.remarksTextView.setText(String
						.valueOf(passageTextContent.getComment_no()));
				passageTextViewHolder.likesTextView.setText(String
						.valueOf(passageTextContent.getPraise()));
				passageTextViewHolder.shareTextView.setText(String
						.valueOf(passageTextContent.getShare()));
				break;
			default:
				break;
			}

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

	private static class PureTextViewHolder {
		TextView textView;
		TextView timeView;
		TextView remarksTextView;
		TextView likesTextView;
		TextView shareTextView;
		LinearLayout commentLayout;
		LinearLayout likeLayout;
		LinearLayout shareLayout;
	}

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

	private static class PassageTextViewHolder {
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

	private class CommentClickListener implements OnClickListener {

		private Context context;

		private int id;

		public CommentClickListener(Context context, int position) {
			this.context = context;
			AbstractContent abstractContent = (AbstractContent) mainContentAdapter
					.getItem(position);
			id = abstractContent.getId();
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
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

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
			shareData.setDescription("");
			shareData.setTitle(abstractContent.getTitle());
			shareData.setText("分享正文");
			shareData.setTarget_url(abstractContent.getUrl());
			shareData.setImageUrl(configureManager.getDefault_image());
			blackTemplate.setShareData(shareData);
			blackTemplate.setPopwindowHeight(460);
			blackTemplate.setScreencapVisible(false);

			blackTemplate.show();

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
						mListView.onRefreshComplete();
					} catch (Exception e) {
						log(e.toString());
					}
				} else {
					mListView.onRefreshComplete();
				}
				if (isStart && mainContentAdapter.getCount() > 0) {
					responseActivity
							.response((AbstractContent) mainContentAdapter
									.getItem(0));
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

	public interface ResponseActivity {
		public void response(AbstractContent abstractContent);
	}

}
