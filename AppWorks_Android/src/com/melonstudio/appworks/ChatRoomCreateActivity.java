package com.melonstudio.appworks;

import com.melonstudio.dao.ChatRoomInfoDao;
import com.melonstudio.dao.MyDataBaseHelper;
import com.melonstudio.factory.DaoFactory;
import com.melonstudio.im.IMParameter;
import com.melonstudio.im.SmackManager.IChatRoomCreateAgent;
import com.melonstudio.model.ChatRoomInfo;
import com.melonstudio.network.NetworkService;
import com.melonstudio.network.NetworkService.MyBinder;
import com.melonstudio.util.ConstUtil;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

/**
 * 在本类中完成创建ChatRoom的工作。并对创建过程中可能出现的各种问题进行处理。
 * 
 * @author Wo
 * 
 */
public class ChatRoomCreateActivity extends FinalActivity implements
		IChatRoomCreateAgent {

	@ViewInject(id = R.id.chatroom_create_setup, click = "createChatRoom")
	private Button setupBtn;
	private MyBinder binder;
	@ViewInject(id = R.id.chatroom_create_groupname)
	private EditText nameEditText;
	@ViewInject(id = R.id.chatroom_create_pnum)
	private EditText pnumEditText;
	@ViewInject(id = R.id.chatroom_create_radio_free)
	private RadioButton freeRadioButton;
	@ViewInject(id = R.id.chatroom_create_radio_admin)
	private RadioButton adminRadioButton;

	private MyHandler handler;

	/**
	 * 完成创建时的一些初始化工作
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chatroom_create);

		/**
		 * 与NetworkService相绑定。绑定成功时回调ServiceConnection的onServiceConnected方法
		 */
		Intent intent = new Intent(getApplicationContext(),
				NetworkService.class);
		bindService(intent, serviceConnection, BIND_AUTO_CREATE);
		handler = new MyHandler(getApplicationContext());
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		/**
		 * 当销毁Activity时关闭与NetworkService的连接
		 */
		unbindService(serviceConnection);
	}

	/**
	 * setup 按钮的事件处理函数。根据此时页面中各组件的值创建相应的ChatRoom。
	 * 包括ChatRoom的Name，最大人数，是否需要密码，加入是否需要管理员同意
	 * 
	 * @param view
	 */
	public void createChatRoom(View view) {
		IMParameter imParameter = new IMParameter();
		/**
		 * 设置App名称
		 */
		imParameter.setAppName(ConstUtil.APP_NAME);
		/**
		 * 设置在该聊天室中的昵称，默认为该用户注册时的昵称
		 */
		imParameter.setNickName(ConstUtil.NICK_NAME);
		/**
		 * 聊天室名称
		 */
		imParameter.setChatRoomName(nameEditText.getText().toString().trim());

		/**
		 * 此处可能会出现异常
		 */
		imParameter.setMaxMembers(Integer.parseInt(pnumEditText.getText()
				.toString()));
		/**
		 * 设置 加入该房间是否需要管理员同意
		 */
		if (freeRadioButton.isChecked())
			imParameter.setFreeToJoin(true);
		else if (adminRadioButton.isChecked()) {
			imParameter.setFreeToJoin(false);
		}
		/**
		 * 将此Activity绑定到SmackManager，以接受创建的结果
		 */
		imParameter.setChatRoomCreateActivity(this);
		/**
		 * 向服务器发送创建请求
		 */
		binder.binderCreateChatRoom(imParameter);
	}

	private ServiceConnection serviceConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub

		}

		/**
		 * 绑定NetworkService成功时回调
		 */
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			binder = (MyBinder) service;
		}
	};

	@Override
	public void createSuccess(IMParameter imParameter) {
		ChatRoomInfo chatRoomInfo = new ChatRoomInfo();
		chatRoomInfo.setDate(System.currentTimeMillis());
		chatRoomInfo.setName(imParameter.getChatRoomName());
		chatRoomInfo.setNickName(imParameter.getNickName());
		chatRoomInfo.setUnreadItems(0);
		chatRoomInfo.setMessageNum(0);
		/**
		 * 写入数据库
		 */
		MyDataBaseHelper helper = MyDataBaseHelper
				.getDBHelperInstance(getApplicationContext());
		SQLiteDatabase db = helper.getWritableDatabase();
		ChatRoomInfoDao chatRoomInfoDao = DaoFactory
				.getChatRoomInfoDaoInstance();
		long id = chatRoomInfoDao.insert(db, chatRoomInfo);
		chatRoomInfo.setId(id);
		db.close();
		/**
		 * 写入成功后跳转到ChatRoomListActivity
		 */
		Intent intent = new Intent(getApplicationContext(),
				ChatRoomListActivity.class);
		startActivity(intent);
	}

	@Override
	public void createFail(final String info) {
		Message message = new Message();
		message.what = 0x1126;
		Bundle bundle = new Bundle();
		bundle.putString("info", info);
		message.setData(bundle);
		handler.sendMessage(message);
	}

	private static class MyHandler extends Handler {
		private Context context;

		public MyHandler(Context context) {
			this.context = context;
		}

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x1126) {
				Toast.makeText(
						context,
						msg.getData().getString("info") == null ? "创建失败！" : msg
								.getData().getString("info"),
						Toast.LENGTH_SHORT).show();
			}
		}
	}
}
