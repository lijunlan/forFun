package com.melonstudio.appworks;

import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.melonstudio.appworks.adapters.ChatRoomItemAdapter;
import com.melonstudio.appworks.adapters.ChatRoomItemAdapterLocal;
import com.melonstudio.dao.ChatRoomInfoDao;
import com.melonstudio.dao.MyDataBaseHelper;
import com.melonstudio.factory.DaoFactory;
import com.melonstudio.im.IMParameter;
import com.melonstudio.im.SmackManager.IChatRoomListAgent;
import com.melonstudio.model.ChatRoomInfo;
import com.melonstudio.network.NetworkService;
import com.melonstudio.network.NetworkService.MyBinder;
import com.melonstudio.util.ConstUtil;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

/**
 * 
 * @author Wo
 * 
 *         显示已加入的所有的聊天室
 */
public class ChatRoomListActivity extends SherlockActivity implements
		IChatRoomListAgent {
	private NetworkService.MyBinder binder;
	private ServiceConnection serviceConnection = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			binder.binder_leaveChatroomList(null);
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			binder = (MyBinder) service;
			IMParameter parameter = new IMParameter();
			parameter.setChatRoomListActivity(ChatRoomListActivity.this);
			binder.binder_enterChatroomList(parameter);

		}
	};
	private List<ChatRoomInfo> chatRoomInfos;
	/**
	 * 保存网络上所有ChatRoomInfo的Adapter
	 */
	private ChatRoomItemAdapter onlineAdapter;
	/**
	 * 保存本地所有ChatRoomInfo的Adapter
	 */
	private ChatRoomItemAdapterLocal localAdapter;
	@ViewInject(id = R.id.chatroom_list_id, itemClick = "onItemSelected")
	private ListView listView;
	@ViewInject(id = R.id.chatroom_list_message, click = "messageClick")
	private ImageView messageImageView;
	@ViewInject(id = R.id.chatroom_list_all, click = "allGroupClick")
	private ImageView allGroupImageView;
	@ViewInject(id = R.id.activity_chatroomlist_content)
	private LinearLayout contentLayout;
	@ViewInject(id = R.id.activity_chatroomlist_menu_list)
	private ListView menuListView;
	/**
	 * 表示当前显示给用户的是否是本地消息列表。true是，false不是
	 */
	private boolean isDisplayLocal = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chatroom_list);

		FinalActivity.initInjectedView(this);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		chatRoomInfos = new ArrayList<ChatRoomInfo>();
		chatRoomInfos.addAll(loadChatRoomInfosFromDB());
		localAdapter = new ChatRoomItemAdapterLocal(getApplicationContext(),
				chatRoomInfos);
		onlineAdapter = new ChatRoomItemAdapter(getApplicationContext(),
				new ArrayList<ChatRoomInfo>());
		listView.setAdapter(localAdapter);
		listView.smoothScrollToPosition(0);
		localAdapter.refresh();
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
		/**
		 * 与NetworkService绑定
		 */
		Intent intent = new Intent(getApplicationContext(),
				NetworkService.class);
		bindService(intent, serviceConnection, BIND_AUTO_CREATE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (binder != null) {
			IMParameter parameter = new IMParameter();
			parameter.setChatRoomListActivity(ChatRoomListActivity.this);
			binder.binder_enterChatroomList(parameter);
		}
		if (isDisplayLocal) {
			/**
			 * 更换Adapter，并更新数据
			 */
			localAdapter.getChatRoomInfos().clear();
			localAdapter.getChatRoomInfos().addAll(loadChatRoomInfosFromDB());
			listView.setAdapter(localAdapter);
			localAdapter.refresh();
		} else {
			searchAllChatRooms();
		}
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onPause() {
		super.onPause();
		binder.binder_leaveChatroomList(null);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		/**
		 * 与NetworkService解除绑定
		 */
		unbindService(serviceConnection);

	}

	/**
	 * 获取已经加入的群，从本地数据库中加载
	 * 
	 * @return
	 */
	private List<ChatRoomInfo> loadChatRoomInfosFromDB() {
		List<ChatRoomInfo> chatrooms = new ArrayList<ChatRoomInfo>();
		MyDataBaseHelper helper = MyDataBaseHelper
				.getDBHelperInstance(getApplicationContext());
		ChatRoomInfoDao dao = DaoFactory.getChatRoomInfoDaoInstance();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = dao.query(db, ChatRoomInfoDao.searchAllSql);
		if (cursor.moveToFirst()) {
			for (int i = 0; i < cursor.getCount(); i++) {
				chatrooms.add(dao.createChatRoomFromCursorWithNoCloseCursor(
						cursor, i));
			}
		}
		// 关闭数据库连接
		cursor.close();
		db.close();
		return chatrooms;
	}

	/**
	 * 搜索服务器中所有群列表。当此时显示的房间为已加入的所有房间时，点击@+id/chatroom_list_mode会调用此函数
	 * 
	 * 该函数被changeModeClick函数调用
	 */
	private void searchAllChatRooms() {
		IMParameter imParameter = new IMParameter();
		imParameter.setAppName(ConstUtil.APP_NAME);
		imParameter.setChatRoomListActivity(ChatRoomListActivity.this);
		binder.searchAllChatRooms(imParameter);
	}

	/**
	 * 对Listview的选择做出响应
	 * 
	 * @param adapter
	 * @param view
	 * @param index
	 * @param l
	 */
	public void onItemSelected(AdapterView<?> adapter, View view, int index,
			long l) {
		Intent intent = new Intent(getApplicationContext(),
				ChatRoomActivity.class);
		if (isDisplayLocal) {
			intent.putExtra("Name", localAdapter.getChatRoomInfos().get(index)
					.getName());
		} else {
			intent.putExtra("Name", onlineAdapter.getChatRoomInfos().get(index)
					.getName());
		}
		startActivity(intent);
	}

	@Override
	public void getAllChatroomsOnline(final List<ChatRoomInfo> chatroomsOnline) {
		listView.post(new Runnable() {

			@Override
			public void run() {
				/**
				 * 更换Adapter，并更新数据
				 */
				onlineAdapter.getChatRoomInfos().clear();
				onlineAdapter.getChatRoomInfos().addAll(chatroomsOnline);
				listView.setAdapter(onlineAdapter);
				onlineAdapter.refresh();
				listView.smoothScrollToPosition(0);
			}
		});
	}

	/**
	 * 含有消息的聊天室选中时的处理函数
	 * 
	 * @param view
	 */
	public void messageClick(View view) {
		if (isDisplayLocal == false)
			isDisplayLocal = true;
		messageImageView.setImageResource(R.drawable.message);
		allGroupImageView.setImageResource(R.drawable.all2);
		/**
		 * 更换Adapter，并更新数据
		 */
		localAdapter.getChatRoomInfos().clear();
		localAdapter.getChatRoomInfos().addAll(loadChatRoomInfosFromDB());
		listView.setAdapter(localAdapter);
		localAdapter.refresh();
	}

	/**
	 * 所有聊天室选中时的处理函数
	 * 
	 * @param view
	 */
	public void allGroupClick(View view) {
		if (isDisplayLocal == true)
			isDisplayLocal = false;
		messageImageView.setImageResource(R.drawable.message1);
		allGroupImageView.setImageResource(R.drawable.all);
		searchAllChatRooms();
	}

	@Override
	public void joinChatroomFailed() {
		// TODO Auto-generated method stub

	}

	@Override
	public void refreshLocalChatrooms() {
		if (isDisplayLocal == true) {
			localAdapter.getChatRoomInfos().clear();
			localAdapter.getChatRoomInfos().addAll(loadChatRoomInfosFromDB());
			listView.post(new Runnable() {
				@Override
				public void run() {
					localAdapter.refresh();
				}
			});
		}
	}
}
