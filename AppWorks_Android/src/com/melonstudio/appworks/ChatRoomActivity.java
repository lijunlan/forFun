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
			 * ��ChatRoomActivity��IMManager
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
		// ��Bundle�ж����������ҵ���Ϣ TODO
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
				// TODO ���û�л�ȡ��Id��ô��
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
			 * ��δ��������λ0
			 */
			chatRoomInfo.setUnreadItems(0);
			chatRoomInfoDao.update(db, chatRoomInfo);
		}
		db.close();

	}

	/**
	 * sendBtn��click�¼���Ӧ����
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

			// �洢Message
			ChatRoomMessageDao chatRoomMessageDao = DaoFactory
					.getChatRoomMessageDaoInstance();
			MyDataBaseHelper helper = MyDataBaseHelper
					.getDBHelperInstance(getApplicationContext());
			SQLiteDatabase db = helper.getWritableDatabase();
			ChatRoomMessage timeMessage = TimeUtil.checkTimeMessage();
			/**
			 * ����Ƿ���Ҫ���ʱ����
			 */
			if (timeMessage != null) {
				timeMessage.setChatroomId(chatRoomInfo.getId());
				adapter.getMessages().add(timeMessage);
				chatRoomMessageDao.insert(db, timeMessage);
			}
			chatRoomMessageDao.insert(db, message);
			/**
			 * ���ø������ҵ�������ϢΪ���������һ����Ϣ������
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
			// ����������������Щ
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
		 * ��������Ϣ�������Ե�ǰ�򿪵������ҵ�ʱ��Ҫֱ����ʾ���������У�Ȼ��д�����ݿ�
		 */
		if (parameter.getChatRoomName().equals(
						titleBar.getText().toString())) {
			// ��Activity��ǰ̨��ʱ��
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
					 * ֪ͨListView���½������
					 */
					adapter.refresh();
					listView.smoothScrollToPosition(adapter.getCount());
				}
			});
			/**
			 * ����Ϣд�����ݿ⣬��ָ��ChatRoomMessageId
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
				 * �����ѯ��ChatRoom
				 */
				ChatRoomMessageDao chatRoomMessageDao = DaoFactory
						.getChatRoomMessageDaoInstance();
				for (ChatRoomMessage message : parameter.getMessages()) {
					message.setChatroomId(chatRoomInfo.getId());
					chatRoomMessageDao.insert(db, message);
				}
				/**
				 * ����ChatRoomInfo�����һ����Ϣ,�������ҵ���Ϣ����
				 */
				chatRoomInfo.updateLastMessage(parameter.getMessages().get(
						parameter.getMessages().size() - 1));
				chatRoomInfo.updateMessageNum(parameter.getMessages().size());
				chatRoomInfoDao.update(db, chatRoomInfo);
				db.close();
			} else {
				/**
				 * ���û�鵽��ChatRoom TODO
				 */
			}
		} else if (!parameter.getChatRoomName().equals(
				titleBar.getText().toString())) {
			/**
			 * ���������Ҳ��ǵ�ǰ�򿪵������ҵ�ʱ����Ҫ����Щ��Ϣд�����ݿ⣬���¸������ҵ�δ������ TODO ���¸������ҵ����һ����Ϣ
			 */
			MyDataBaseHelper helper = MyDataBaseHelper
					.getDBHelperInstance(getApplicationContext());
			SQLiteDatabase db = helper.getWritableDatabase();
			ChatRoomInfoDao chatRoomInfoDao = DaoFactory
					.getChatRoomInfoDaoInstance();
			/**
			 * ����������titleBar������parameter�����Ĳ���
			 */
			ChatRoomInfo chatRoomInfo = chatRoomInfoDao
					.queryChatRoomInfoByName(db, parameter.getChatRoomName());
			if (chatRoomInfo != null) {
				/**
				 * �����ѯ��ChatRoom
				 */
				ChatRoomMessageDao chatRoomMessageDao = DaoFactory
						.getChatRoomMessageDaoInstance();
				for (ChatRoomMessage message : parameter.getMessages()) {
					message.setChatroomId(chatRoomInfo.getId());
					chatRoomMessageDao.insert(db, message);
				}
				/**
				 * ����ChatRoomInfo�����һ����Ϣ������Ϣ����
				 */
				chatRoomInfo.updateLastMessage(parameter.getMessages().get(
						parameter.getMessages().size() - 1));
				chatRoomInfo.updateMessageNum(parameter.getMessages().size());
				/**
				 * ����δ������
				 */
				chatRoomInfo.setUnreadItems(chatRoomInfo.getUnreadItems()
						+ parameter.getMessages().size());
				chatRoomInfoDao.update(db, chatRoomInfo);
				db.close();
			} else {
				/**
				 * ����鲻����ChatRoom TODO
				 */
			}

		}
	}

}
