package com.melonstudio.network;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.melonstudio.appworks.ContentMainActivity;
import com.melonstudio.exceptions.IMConnectException;
import com.melonstudio.exceptions.IMRegistException;
import com.melonstudio.im.IMManager;
import com.melonstudio.im.IMManagerFactory;
import com.melonstudio.im.IMParameter;
import com.melonstudio.im.IMUtli;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

/**
 * 
 * @author Wo
 * 
 * 
 */
public class NetworkService extends Service {

	private ExecutorService threadPool;

	private IMManager imManager = null;
	private MyBinder binder;
	private IMParameter parameter;

	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;

	@SuppressLint("HandlerLeak")
	private final Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.arg1 == 0x000001) {
				/*
				 * process the request: it's ready to go to the main activity
				 */
				Intent intent = new Intent(getApplicationContext(),
						ContentMainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		}
	};

	public class MyBinder extends Binder {

		/**
		 * Activity֪ͨServiceע��
		 * 
		 * @param username
		 * @param password
		 * @return ע����
		 */
		public int binderRegistUser(String username, String password) {
			if (parameter.getUsername() == null
					|| parameter.getPassword() == null)
				parameter = setParameter(username, password);
			return regist(parameter);
		}

		/**
		 * Activity֪ͨService��¼ �п��ܵ�¼ʧ�ܡ� ���ܵ�ԭ���У��������⡢δע���
		 * 
		 * @return ��¼�Ľ��Ӧ��ֻ�ܷ��سɹ�
		 * 
		 */
		public int binderLoginUser() {
			parameter = getParameter(null);
			return login(parameter);
		}

		/**
		 * Activity֪ͨService��ݴ����IMParameter���󴴽�������
		 * 
		 * @param parameter
		 *            Ҫ��chatroomname��nickname���Ƿ���Ҫ���루�����Ҫ��Ҫ��password��
		 *            ��Appname�� chatroom������
		 * @return
		 */
		public int binderCreateChatRoom(IMParameter parameter) {
			return createChatRoom(parameter);
		}

		/**
		 * ChatRoomActivity�е�����������Ϣ
		 * 
		 * @param parameter
		 * @return
		 */
		public int sendMessage(IMParameter imParameter) {
			return send(imParameter);
		}

		/**
		 * �����µ�������
		 * 
		 * @param imParameter
		 *            �����������Ϣ�Ĳ���
		 * 
		 */
		public void createNewChatRoom(IMParameter imParameter) {

		}

		/**
		 * ���������������ڱ�Ӧ�õ�����Ⱥ
		 * 
		 * Ⱥ�������׼��Ⱥ���_App���@conference.host��
		 * 
		 * @param imParameter
		 *            Ӧ�ð�App��Ƶ���Ϣ
		 */
		public void searchAllChatRooms(IMParameter imParameter) {
			searchAllChatroomsOfThisApp(imParameter);
		}

		/**
		 * ��ChatRoomActivity�е��ã���ChatRoomActivity���ɼ�ʱ���˷���������
		 * 
		 * @param parameter
		 */
		public void binder_leaveChatroom(IMParameter parameter) {
			leaveChatroom(parameter);
		}

		/**
		 * ��ChatRoomActivity�е��ã���ChatRoomActivity�ɼ�ʱ���˷���������
		 * 
		 * @param parameter
		 *            ��ChatRoomActivity�����ʵ��
		 */
		public void binder_getIntoChatroom(IMParameter parameter) {
			getIntoChatroom(parameter);
		}

		/**
		 * ��ChatRoomListActivity�е��ã���ChatRoomListActivity�ɼ�ʱ���˷���������
		 * 
		 * @param parameter
		 *            ��ChatRoomListActivity����ʵ��
		 */
		public void binder_enterChatroomList(IMParameter parameter) {
			enterChatroomList(parameter);
		}

		/**
		 * ��ChatRoomListActivity�����뿪ǰ̨��ʱ�����
		 * 
		 * @param parameter
		 *            Ϊnull
		 */
		public void binder_leaveChatroomList(IMParameter parameter) {
			leaveChatroomList(parameter);
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.v("NetworkService", "Created!");

		if (imManager == null)
			imManager = IMManagerFactory.newSmackManager();

		threadPool = Executors.newCachedThreadPool();
		final IMParameter imParameter = new IMParameter();
		imParameter.setContext(getApplicationContext());
		Log.v("NetworkService", "�������߳�...");
		// ���ӵ�������
		@SuppressWarnings("unused")
		Future<Integer> future = threadPool.submit(new Callable<Integer>() {

			@Override
			public Integer call() throws Exception {
				int flag = imManager.connect(imParameter);
				Log.v("NetworkService", "New Thread... flag: " + flag);
				if (flag != IMUtli.CONNECT_SUCCESS) {
					throw new IMConnectException("����ʧ��");
				}
				return flag;
			}
		});

		preferences = getSharedPreferences("profile", MODE_PRIVATE);
		editor = preferences.edit();
		Log.v("NetworkService", "onCreate����...");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent.hasExtra("from")
				&& intent.getStringExtra("from").equals("welcomactivity")) {
			// TODO to do something to initialize

			Message message = myHandler.obtainMessage();
			message.arg1 = 0x000001;
			myHandler.sendMessage(message);
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {

		// ��ʼ�� Mybinder
		Log.v("NetworkService", "onBind");
		if (binder == null) {
			binder = new MyBinder();
		}
		parameter = getParameter(null);
		return binder;
	}

	private IMParameter setParameter(String username, String password) {
		IMParameter parameter = new IMParameter();
		parameter.setUsername(username);
		parameter.setPassword(password);
		return parameter;
	}

	/**
	 * ���intent��װIMParameter
	 * 
	 * @param intent
	 *            ����Service��intent
	 * @return IMParameter����
	 */
	private IMParameter getParameter(Intent intent) {
		IMParameter parameter = new IMParameter();
		if (intent == null) {
			SharedPreferences preferences = getSharedPreferences("profile",
					MODE_PRIVATE);
			parameter.setUsername(preferences.getString("username", null));
			parameter.setPassword(preferences.getString("password", null));
		}
		return parameter;
	}

	/**
	 * �û�ע��
	 * 
	 * @param parameter
	 * @return
	 */
	public int regist(final IMParameter parameter) {
		@SuppressWarnings("unused")
		Future<Integer> future = threadPool.submit(new Callable<Integer>() {

			@Override
			public Integer call() throws Exception {
				int flag;
				try {
					flag = imManager.regist(parameter);
					if (flag != IMUtli.REGIST_SUCCESS) {
						throw new IMRegistException("ע��ʧ��");
					} else {
						// ע��ɹ�
						editor.putString("username", parameter.getUsername());
						editor.putString("password", parameter.getPassword());
						editor.commit();
					}
					Log.v("NetworkService", "Regist...flag: " + flag);
				} catch (IMRegistException e) {
					throw e;
				}
				return flag;
			}
		});
		return IMUtli.REGIST_SUCCESS;
	}

	/**
	 * �û���¼
	 * 
	 * @param parameter
	 * @return
	 */
	public int login(final IMParameter parameter) {
		@SuppressWarnings("unused")
		Future<Integer> future = threadPool.submit(new Callable<Integer>() {

			@Override
			public Integer call() throws Exception {
				return imManager.login(parameter);
			}
		});
		return IMUtli.LOGIN_SUCCESS;
	}

	/**
	 * ������Ϣ
	 * 
	 * @param parameter
	 *            parameter����Ҫ���͵���Ϣ�ͷ��͵���Ŀ�ķ����JID
	 * @return
	 */
	public int send(final IMParameter parameter) {
		@SuppressWarnings("unused")
		Future<Integer> future = threadPool.submit(new Callable<Integer>() {

			@Override
			public Integer call() throws Exception {
				return imManager.send(parameter);
			}
		});
		return IMUtli.SEND_SUCCESS;
	}

	/**
	 * �û��������
	 * 
	 * @param parameter
	 * @return
	 */
	public int updateInfo(final IMParameter parameter) {
		@SuppressWarnings("unused")
		Future<Integer> future = threadPool.submit(new Callable<Integer>() {

			@Override
			public Integer call() throws Exception {
				return imManager.updateInfo(parameter);

			}
		});
		return IMUtli.UPDATE_SUCCESS;
	}

	/**
	 * �˳���¼
	 * 
	 * @param parameter
	 * @return
	 */
	public int logout(final IMParameter parameter) {
		@SuppressWarnings("unused")
		Future<Integer> future = threadPool.submit(new Callable<Integer>() {

			@Override
			public Integer call() throws Exception {
				return imManager.logout(parameter);
			}
		});
		return IMUtli.LOGOUT_SUCCESS;
	}

	public int createChatRoom(final IMParameter parameter) {
		@SuppressWarnings("unused")
		Future<Integer> future = threadPool.submit(new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				return imManager.createChatRoom(parameter);
			}
		});
		return IMUtli.CREATE_CHATROOM_SUCCESS;
	}

	public void searchAllChatroomsOfThisApp(final IMParameter imParameter) {
		threadPool.submit(new Callable<Integer>() {

			@Override
			public Integer call() throws Exception {
				imManager.searchAllChatrooms(imParameter);
				return null;
			}
		});
	}

	public void leaveChatroom(final IMParameter imParameter) {
		threadPool.submit(new Callable<Integer>() {

			@Override
			public Integer call() throws Exception {
				imManager.leaveChatroom(imParameter);
				return null;
			}
		});
	}

	public void getIntoChatroom(final IMParameter imParameter) {
		threadPool.submit(new Callable<Integer>() {

			@Override
			public Integer call() throws Exception {
				imManager.getIntoChatroom(imParameter);
				return null;
			}
		});
	}

	public void enterChatroomList(final IMParameter parameter) {
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				imManager.enterChatRoomList(parameter);
			}
		});
	}

	public void leaveChatroomList(final IMParameter parameter) {
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				imManager.leaveChatRoomList(parameter);
			}
		});
	}

}
