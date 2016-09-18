package com.melonstudio.appworks;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import com.melonstudio.appworks.adapters.ChatRoomAdapter;
import com.melonstudio.dao.ChatRoomInfoDao;
import com.melonstudio.dao.ChatRoomMessageDao;
import com.melonstudio.dao.MyDataBaseHelper;
import com.melonstudio.factory.DaoFactory;
import com.melonstudio.im.IMParameter;
import com.melonstudio.im.SmackManager.IChatRoomActivityAgent;
import com.melonstudio.model.ChatRoomInfo;
import com.melonstudio.model.ChatRoomMessage;
import com.melonstudio.network.NetworkService;
import com.melonstudio.util.ConstUtil;
import com.melonstudio.util.TimeUtil;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * @author Wo
 * 
 */
public class ChatRoomActivity extends FinalActivity implements
		IChatRoomActivityAgent {

	@ViewInject(id = R.id.BtnSend, click = "sendButtonClick")
	private Button sendBtn;
	@ViewInject(id = R.id.InputBox)
	private EditText editText;
	private List<ChatRoomMessage> messages;
	private ChatRoomAdapter adapter;
	@ViewInject(id = R.id.MainList)
	private ListView listView;
	@ViewInject(id = R.id.titleBar)
	private TextView titleBar;

	private ChatRoomInfo chatRoomInfo;

	private NetworkService.MyBinder binder;
	private ServiceConnection serviceConnection = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			binder.binder_leaveChatroom(null);
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			binder = (NetworkService.MyBinder) service;
			IMParameter parameter = new IMParameter();
			parameter.setChatRoomName(titleBar.getText().toString());
			parameter.setChatRoomActivity(ChatRoomActivity.this);
			/**
			 * 绑定ChatRoomActivity到IMManager
			 */
			IMParameter imParameter = new IMParameter();
			imParameter.setChatRoomActivity(ChatRoomActivity.this);
			binder.binder_getIntoChatroom(imParameter);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chatroom);
		// 从Bundle中读出此聊天室的信息 TODO
		Intent intent = getIntent();
		if (intent.hasExtra("Name")) {
			titleBar.setText(intent.getStringExtra("Name"));
		}
		messages = new ArrayList<ChatRoomMessage>();
		adapter = new ChatRoomAdapter(this, messages);
		listView.setAdapter(adapter);
		listView.smoothScrollToPositionFromTop(messages.size(), 0);
	}

	@Override
	protected void onStart() {
		super.onStart();		
		Intent intent = new Intent(ChatRoomActivity.this, NetworkService.class);
		bindService(intent, serviceConnection, BIND_AUTO_CREATE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		adapter.getMessages().clear();
		loadDbData();
		adapter.refresh();
		listView.smoothScrollToPosition(adapter.getCount() - 1,
				adapter.getCount());
		if(binder != null){
			IMParameter imParameter = new IMParameter();
			imParameter.setChatRoomActivity(ChatRoomActivity.this);
			binder.binder_getIntoChatroom(imParameter);
		}				
	}

	@Override
	protected void onPause() {
		super.onPause();
		binder.binder_leaveChatroom(null);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(serviceConnection);
	}

	private void loadDbData() {
		String sql = "select * from " + ConstUtil.CHATROOM_TALBE
				+ " where name = \"" + titleBar.getText().toString() + "\"";
		MyDataBaseHelper helper = MyDataBaseHelper
				.getDBHelperInstance(getApplicationContext());
		SQLiteDatabase db = helper.getWritableDatabase();
		ChatRoomInfoDao chatRoomInfoDao = DaoFactory
				.getChatRoomInfoDaoInstance();
		Cursor cursor = chatRoomInfoDao.query(db, sql);
		if (cursor.moveToFirst()) {
			chatRoomInfo = chatRoomInfoDao.createChatRoomFromCursor(cursor, 0);
		}
		if (chatRoomInfo == null) {
			chatRoomInfo = ChatRoomInfo.createNewInstance(titleBar.getText()
					.toString());
			long id = chatRoomInfoDao.insert(db, chatRoomInfo);
			if (id != -1)
				chatRoomInfo.setId(id);
			else {
				// TODO 如果没有获取到Id怎么办
			}
		} else {
			sql = "select * from " + ConstUtil.CHATROOM_MESSAGE_TABLE
					+ " where chatroomId=\"" + chatRoomInfo.getId() + "\"";
			ChatRoomMessageDao chatRoomMessageDao = DaoFactory
					.getChatRoomMessageDaoInstance();
			List<ChatRoomMessage> chatRoomMessages = chatRoomMessageDao
					.queryChatRoomMessages(db, sql);
			if (chatRoomMessages != null && chatRoomMessages.size() > 0)
				messages.addAll(chatRoomMessages);
			/**
			 * 将未读数量置位0
			 */
			chatRoomInfo.setUnreadItems(0);
			chatRoomInfoDao.update(db, chatRoomInfo);
		}
		db.close();

	}

	/**
	 * sendBtn的click事件响应函数
	 * 
	 * @param view
	 */
	public void sendButtonClick(View view) {

		if (!editText.getText().toString().equals("")) {
			ChatRoomMessage message = new ChatRoomMessage(
					ChatRoomMessage.MessageType_Send, editText.getText()
							.toString());
			message.setTime(System.currentTimeMillis());
			message.setChatroomId(chatRoomInfo.getId());

			// 存储Message
			ChatRoomMessageDao chatRoomMessageDao = DaoFactory
					.getChatRoomMessageDaoInstance();
			MyDataBaseHelper helper = MyDataBaseHelper
					.getDBHelperInstance(getApplicationContext());
			SQLiteDatabase db = helper.getWritableDatabase();
			ChatRoomMessage timeMessage = TimeUtil.checkTimeMessage();
			/**
			 * 检查是否需要添加时间条
			 */
			if (timeMessage != null) {
				timeMessage.setChatroomId(chatRoomInfo.getId());
				adapter.getMessages().add(timeMessage);
				chatRoomMessageDao.insert(db, timeMessage);
			}
			chatRoomMessageDao.insert(db, message);
			/**
			 * 设置该聊天室的最新消息为发出的最后一条消息的内容
			 */
			ChatRoomInfoDao chatRoomInfoDao = DaoFactory
					.getChatRoomInfoDaoInstance();
			chatRoomInfo.setLastMessage(message.getContent());
			chatRoomInfoDao.update(db, chatRoomInfo);
			db.close();

			IMParameter parameter = new IMParameter();
			parameter.setMessage(message);
			parameter.setChatRoomName(titleBar.getText().toString());
			parameter.setChatRoomActivity(ChatRoomActivity.this);
			// 不该在这里设置这些
			parameter.setAppName(ConstUtil.APP_NAME);
			binder.sendMessage(parameter);
			adapter.getMessages().add(message);
			adapter.refresh();
			listView.smoothScrollToPosition(adapter.getCount() - 1);
			editText.setText("");
			editText.clearFocus();
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
		}

	}

	@Override
	public void sendMessageSuccess(IMParameter parameter) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendMessageFailed(IMParameter parameter) {
		// TODO Auto-generated method stub

	}

	@Override
	public void receiveMessages(IMParameter parameter) {
		/**
		 * 当来的消息正是来自当前打开的聊天室的时候，要直接显示在聊天室中，然后写入数据库
		 */
		if (parameter.getChatRoomName().equals(
						titleBar.getText().toString())) {
			// 当Activity在前台的时候
			ChatRoomMessage timeMessage = TimeUtil.checkTimeMessage();
			if (timeMessage != null) {
				timeMessage.setTime(System.currentTimeMillis());
				adapter.getMessages().add(timeMessage);
			}
			adapter.getMessages().addAll(parameter.getMessages());
			listView.post(new Runnable() {

				@Override
				public void run() {
					/**
					 * 通知ListView更新界面组件
					 */
					adapter.refresh();
					listView.smoothScrollToPosition(adapter.getCount());
				}
			});
			/**
			 * 将消息写入数据库，并指定ChatRoomMessageId
			 */
			MyDataBaseHelper helper = MyDataBaseHelper
					.getDBHelperInstance(getApplicationContext());
			SQLiteDatabase db = helper.getWritableDatabase();
			ChatRoomInfoDao chatRoomInfoDao = DaoFactory
					.getChatRoomInfoDaoInstance();
			ChatRoomInfo chatRoomInfo = chatRoomInfoDao
					.queryChatRoomInfoByName(db, titleBar.getText().toString());
			if (chatRoomInfo != null) {
				/**
				 * 如果查询该ChatRoom
				 */
				ChatRoomMessageDao chatRoomMessageDao = DaoFactory
						.getChatRoomMessageDaoInstance();
				for (ChatRoomMessage message : parameter.getMessages()) {
					message.setChatroomId(chatRoomInfo.getId());
					chatRoomMessageDao.insert(db, message);
				}
				/**
				 * 更新ChatRoomInfo的最后一条信息,和聊天室的消息总数
				 */
				chatRoomInfo.updateLastMessage(parameter.getMessages().get(
						parameter.getMessages().size() - 1));
				chatRoomInfo.updateMessageNum(parameter.getMessages().size());
				chatRoomInfoDao.update(db, chatRoomInfo);
				db.close();
			} else {
				/**
				 * 如果没查到该ChatRoom TODO
				 */
			}
		} else if (!parameter.getChatRoomName().equals(
				titleBar.getText().toString())) {
			/**
			 * 当该聊天室不是当前打开的聊天室的时候，需要将这些消息写入数据库，更新该聊天室的未读数量 TODO 跟新该聊天室的最后一条消息
			 */
			MyDataBaseHelper helper = MyDataBaseHelper
					.getDBHelperInstance(getApplicationContext());
			SQLiteDatabase db = helper.getWritableDatabase();
			ChatRoomInfoDao chatRoomInfoDao = DaoFactory
					.getChatRoomInfoDaoInstance();
			/**
			 * 房间名不是titleBar，而是parameter传来的参数
			 */
			ChatRoomInfo chatRoomInfo = chatRoomInfoDao
					.queryChatRoomInfoByName(db, parameter.getChatRoomName());
			if (chatRoomInfo != null) {
				/**
				 * 如果查询该ChatRoom
				 */
				ChatRoomMessageDao chatRoomMessageDao = DaoFactory
						.getChatRoomMessageDaoInstance();
				for (ChatRoomMessage message : parameter.getMessages()) {
					message.setChatroomId(chatRoomInfo.getId());
					chatRoomMessageDao.insert(db, message);
				}
				/**
				 * 更新ChatRoomInfo的最后一条信息，和消息总数
				 */
				chatRoomInfo.updateLastMessage(parameter.getMessages().get(
						parameter.getMessages().size() - 1));
				chatRoomInfo.updateMessageNum(parameter.getMessages().size());
				/**
				 * 设置未读数量
				 */
				chatRoomInfo.setUnreadItems(chatRoomInfo.getUnreadItems()
						+ parameter.getMessages().size());
				chatRoomInfoDao.update(db, chatRoomInfo);
				db.close();
			} else {
				/**
				 * 如果查不到该ChatRoom TODO
				 */
			}

		}
	}

}
