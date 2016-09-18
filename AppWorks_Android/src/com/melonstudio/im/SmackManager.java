package com.melonstudio.im;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.LoginException;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.RoomInfo;
import org.jivesoftware.smackx.muc.SubjectUpdatedListener;
import org.jivesoftware.smackx.muc.UserStatusListener;

import com.melonstudio.appworks.ChatRoomActivity;
import com.melonstudio.appworks.ChatRoomListActivity;
import com.melonstudio.dao.ChatRoomInfoDao;
import com.melonstudio.dao.ChatRoomMessageDao;
import com.melonstudio.dao.MyDataBaseHelper;
import com.melonstudio.exceptions.IMRegistException;
import com.melonstudio.exceptions.IMUpdateException;
import com.melonstudio.factory.DaoFactory;
import com.melonstudio.model.ChatRoomInfo;
import com.melonstudio.model.ChatRoomMessage;
import com.melonstudio.util.ConstUtil;
import com.melonstudio.util.TimeUtil;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

public class SmackManager implements IMManager {

	private ConnectionConfiguration config;
	private XMPPConnection connection;
	// private Map<String, ChatRoom> rooms = new HashMap<String, ChatRoom>();
	private Map<String, MultiUserChat> mucMap = new HashMap<String, MultiUserChat>();
	/**
	 * 只有当ChatRoomActivity在前台的时候才可以访问，其他时候是null
	 */
	private ChatRoomActivity chatRoomActivity;
	/**
	 * 只有当ChatRoomActivity在前台的时候才可以访问，其他时候是null
	 */
	private ChatRoomListActivity chatRoomListActivity;
	private Context context;

	public SmackManager() {
		config = new ConnectionConfiguration(IMUtli.HOST, IMUtli.PORT);
		SmackConfiguration.setPacketReplyTimeout(30000);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			config.setTruststoreType("AndroidCAStore");
			config.setTruststorePassword(null);
			config.setTruststorePath(null);
		} else {
			config.setTruststoreType("BKS");
			String path = System.getProperty("javax.net.ssl.trustStore");
			if (path == null)
				path = System.getProperty("java.home") + File.separator + "etc"
						+ File.separator + "security" + File.separator
						+ "cacerts.bks";
			config.setTruststorePath(path);
		}
	}

	@Override
	public int connect(IMParameter parameter) {
		context = parameter.getContext();
		if (connection == null) {
			connection = new XMPPConnection(config);
			try {
				connection.connect();
				/**
				 * 连接服务器成功
				 */
				addInvitationListener(connection);
			} catch (XMPPException e) {
				e.printStackTrace();
			}
		}
		return IMUtli.CONNECT_SUCCESS;
	}

	@Override
	public int regist(IMParameter parameter) throws IMRegistException {
		if (connection == null)
			return IMUtli.CONNECT_FAIL;
		Registration registration = new Registration();
		registration.setType(IQ.Type.SET);
		registration.setTo(connection.getServiceName());
		registration.setUsername(parameter.getUsername());
		registration.setPassword(parameter.getPassword());
		registration.addAttribute("device", "Android");
		PacketFilter filter = new AndFilter(new PacketIDFilter(
				registration.getPacketID()), new PacketTypeFilter(IQ.class));
		PacketCollector collector = connection.createPacketCollector(filter);
		connection.sendPacket(registration);
		IQ result = (IQ) collector.nextResult(SmackConfiguration
				.getPacketReplyTimeout());
		collector.cancel();
		if (result == null) {
			Log.e("SmackManager", "Regist...No response from Server");
			return IMUtli.REGIST_FAIL;
		} else if (result.getType() == IQ.Type.RESULT) {
			return IMUtli.REGIST_SUCCESS;
		} else {
			if (result.getError().toString().equalsIgnoreCase("conflict(409)")) {
				Log.e("SmackManager", "Regist...IQ.Type.ERROR: "
						+ result.getError().toString());
				throw new IMRegistException("账号已存在");
			} else {
				Log.e("SmackManager", "Regist...IQ.Type.ERROR: "
						+ result.getError().toString());
				throw new IMRegistException("注册失败;"
						+ result.getError().toString());
			}
		}
	}

	@Override
	public int send(IMParameter parameter) {
		if (connection == null)
			return IMUtli.SEND_FAIL;
		else {
			MultiUserChat muc = mucMap.get(parameter.getChatRoomName());
			if (muc == null || muc.isJoined() == false) {
				/**
				 * 该ChatRoom未加入或者非本地聊天室，则应该通知用户
				 */
				if (parameter.getChatRoomActivity() != null) {
					// TODO 设置parameter参数
					parameter.getChatRoomActivity()
							.sendMessageFailed(parameter);
				}
				return IMUtli.SEND_FAIL;
			} else {
				try {
					muc.sendMessage(parameter.getMessage().getContent());
					/**
					 * 发送成功
					 */
					if (parameter.getChatRoomActivity() != null) {
						// TODO 设置parameter参数
						parameter.getChatRoomActivity().sendMessageSuccess(
								parameter);
					}
					return IMUtli.SEND_SUCCESS;
				} catch (XMPPException e) {
					/**
					 * 发送失败
					 */
					e.printStackTrace();
					if (parameter.getChatRoomActivity() != null) {
						// TODO 设置parameter参数
						parameter.getChatRoomActivity().sendMessageFailed(
								parameter);
					}
					return IMUtli.SEND_FAIL;
				}
			}
		}
	}

	@Override
	public int login(IMParameter parameter) throws LoginException {
		if (connection == null) {
			return IMUtli.CONNECT_FAIL;
		}
		try {
			/**
			 * 用户登录
			 */
			connection.login(parameter.getUsername(), parameter.getPassword());
			/**
			 * 用户登录成功后，登录所有已经加入的ChatRoom
			 */
			joinAllLocalhatRooms();
		} catch (XMPPException e) {
			throw new LoginException("登录失败");
		}
		return IMUtli.LOGIN_SUCCESS;
	}

	@Override
	public int updateInfo(IMParameter parameter) throws IMUpdateException {
		if (connection == null)
			return IMUtli.CONNECT_FAIL;
		try {
			connection.getAccountManager().changePassword(
					parameter.getPassword());
		} catch (XMPPException e) {
			throw new IMUpdateException("更改密码失败");
		}
		return IMUtli.UPDATE_SUCCESS;
	}

	@Override
	public int logout(IMParameter parameter) {
		connection.disconnect();
		return IMUtli.LOGOUT_SUCCESS;
	}

	@Override
	public int createChatRoom(IMParameter parameter) {
		if (connection == null)
			return IMUtli.CONNECT_FAIL;
		/**
		 * 用于标志聊天室是否成功创建
		 */
		int flag = 0;
		try {
			/**
			 * 创建此聊天室的MUC对象
			 */
			MultiUserChat muc = new MultiUserChat(connection,
					parameter.getChatRoomName() + "_" + parameter.getAppName()
							+ "@conference." + connection.getServiceName());
			muc.create(parameter.getNickName());
			Form form = muc.getConfigurationForm();
			Form submitForm = form.createAnswerForm();
			for (Iterator<FormField> fields = form.getFields(); fields
					.hasNext();) {
				FormField field = fields.next();
				if (!FormField.TYPE_HIDDEN.equals(field.getType())
						&& field.getVariable() != null)
					submitForm.setDefaultAnswer(field.getVariable());
			}
			/**
			 * 设置聊天室的owner
			 */
			List<String> owners = new ArrayList<String>();
			owners.add(connection.getUser());
			/**
			 * 重新配置聊天室的属性
			 */
			submitForm.setAnswer("muc#roomconfig_roomowners", owners);
			/**
			 * 持久的聊天室
			 */
			submitForm.setAnswer("muc#roomconfig_persistentroom", true);
			/**
			 * 允许邀请
			 */
			submitForm.setAnswer("muc#roomconfig_allowinvites", true);
			submitForm.setAnswer("muc#roomconfig_passwordprotectedroom", false);
			submitForm.setAnswer("muc#roomconfig_enablelogging", true);
			submitForm.setAnswer("x-muc#roomconfig_reservednick", true);
			submitForm.setAnswer("x-muc#roomconfig_canchangenick", true);
			submitForm.setAnswer("x-muc#roomconfig_registration", false);
			/**
			 * 设置描述
			 */
			submitForm.setAnswer("muc#roomconfig_roomdesc",
					parameter.getAppName() + "的Group Chat");
			/**
			 * 设置最大人数
			 */
			List<String> list = new ArrayList<String>();
			list.add(String.valueOf(parameter.getMaxMembers()));
			submitForm.setAnswer("muc#roomconfig_maxusers", list);
			/**
			 * 是否允许用户自己加入房间（加入是否需要管理员同意）
			 */
			if (parameter.isFreeToJoin())
				/**
				 * 需要邀请
				 */
				submitForm.setAnswer("muc#roomconfig_membersonly", false);
			else {
				/**
				 * 可以自己加入
				 */
				submitForm.setAnswer("muc#roomconfig_membersonly", true);
			}

			/**
			 * 更新配置
			 */
			muc.sendConfigurationForm(submitForm);

			/**
			 * 创建成功，通知用户更新成功，并且把该muc加入到mucMap中
			 */
			mucMap.put(parameter.getChatRoomName(), muc);
			addMessageListener(muc);
			addSubjectUpdatedListener(muc);
			addUserStatusListener(muc);
			if (parameter.getChatRoomCreateActivity() != null) {
				flag = 1;
				parameter.getChatRoomCreateActivity().createSuccess(parameter);
			}
		} catch (XMPPException e) {
			// 如果这个群已经存在
			if (e.getMessage().startsWith(
					"Creation failed - Missing acknowledge of room creation.")) {
				System.out.println(e.getMessage());
				if (parameter.getChatRoomCreateActivity() != null && flag == 0)
					parameter.getChatRoomCreateActivity().createFail("该名称已被使用");
				return IMUtli.CONNECT_FAIL;
			}
			if (parameter.getChatRoomCreateActivity() != null && flag == 0)
				parameter.getChatRoomCreateActivity().createFail("创建失败");
		} catch (Exception e) {
			if (parameter.getChatRoomCreateActivity() != null && flag == 0)
				parameter.getChatRoomCreateActivity().createFail("创建失败");
		}
		return IMUtli.CREATE_CHATROOM_SUCCESS;
	}

	/**
	 * 使用这个方法要注意，如果加入的聊天室是不存在的话，会建立一个instant的类型的聊天室。因为除了创建聊天室之外，其他加入聊天室、
	 * 退出聊天室的操作都是基于点击的操作（不允许用户输入，但是可以搜索），因此应该不会出现这种情况
	 */
	@Override
	public int joinChatRoom(IMParameter parameter) {
		// ChatRoom chatRoom = rooms.get(parameter.getChatRoomName());
		MultiUserChat muc = mucMap.get(parameter.getChatRoomName());
		/**
		 * 如果获取不到，那么就是加入新的聊天室
		 */
		if (muc == null) {
			muc = new MultiUserChat(connection, parameter.getChatRoomName()
					+ "_" + ConstUtil.APP_NAME + "@conference."
					+ connection.getServiceName());
			try {
				muc.join(ConstUtil.NICK_NAME);
				/**
				 * 加入成功，添加消息监听
				 */
				addMessageListener(muc);
				addSubjectUpdatedListener(muc);
				addUserStatusListener(muc);
				/**
				 * 加入成功后，写入本地数据库
				 */
				MyDataBaseHelper helper = MyDataBaseHelper
						.getDBHelperInstance(context);
				SQLiteDatabase db = helper.getWritableDatabase();
				ChatRoomInfoDao chatRoomInfoDao = DaoFactory
						.getChatRoomInfoDaoInstance();
				ChatRoomInfo chatRoomInfo = new ChatRoomInfo();
				chatRoomInfo.setName(parameter.getChatRoomName());
				chatRoomInfo.setUnreadItems(0);
				chatRoomInfo.setDate(System.currentTimeMillis());
				chatRoomInfoDao.insert(db, chatRoomInfo);
				db.close();
				mucMap.put(parameter.getChatRoomName(), muc);
			} catch (XMPPException e) {
				/**
				 * 加入失败，通知用户
				 */
				e.printStackTrace();
				IChatRoomListAgent agent = parameter.getChatRoomListActivity();
				if (agent != null) {
					agent.joinChatroomFailed();
				}
			}
		}
		/**
		 * 
		 * 如果获取的到则说明是本地已加入的群，那么有两种可能：1. 可以顺利加入； 2.
		 * 服务器上该聊天已经不存在，会加入失败。此时应该通知用户，同时删除本地的该聊天室及消息
		 */
		else if (!muc.isJoined()) {
			try {
				muc.join(ConstUtil.NICK_NAME);
				/**
				 * 加入成功，添加消息监听
				 */
				addMessageListener(muc);
				addSubjectUpdatedListener(muc);
				addUserStatusListener(muc);
			} catch (XMPPException e) {
				e.printStackTrace();
				/**
				 * 加入失败，说明该聊天室在服务器上已经不存在
				 */
				MyDataBaseHelper helper = MyDataBaseHelper
						.getDBHelperInstance(context);
				SQLiteDatabase db = helper.getWritableDatabase();
				ChatRoomInfoDao chatRoomInfoDao = DaoFactory
						.getChatRoomInfoDaoInstance();
				ChatRoomMessageDao chatRoomMessageDao = DaoFactory
						.getChatRoomMessageDaoInstance();
				String queryChatroomSql = "select * from "
						+ ConstUtil.CHATROOM_TALBE + " where name = \""
						+ parameter.getChatRoomName() + "_"
						+ ConstUtil.APP_NAME + "\"";
				ChatRoomInfo chatRoomInfo = chatRoomInfoDao.queryChatRoomInfo(
						db, queryChatroomSql);
				if (chatRoomInfo != null) {
					/**
					 * 查询聊天室成功，则删除聊天室内的所有消息和聊天室
					 */
					String deleteMessageSql = "delete from "
							+ ConstUtil.CHATROOM_MESSAGE_TABLE
							+ " where chatroomId = \"" + "\"";
					chatRoomMessageDao.delete(db, deleteMessageSql);
					chatRoomInfoDao.delete(db, chatRoomInfo);
				} else {
					/**
					 * 查询出错
					 */
				}
				/**
				 * 通知用户无法加入的原因：1、聊天室不存在 2、加入该聊天室需要邀请
				 */
				IChatRoomListAgent agent = parameter.getChatRoomListActivity();
				if (agent != null) {
					agent.joinChatroomFailed();
				}
				db.close();
			}
		}

		return IMUtli.JOIN_CHATROOM_SUCCESS;
	}

	/**
	 * 加入消息处理器
	 * 
	 * @param muc
	 */
	private void addMessageListener(final MultiUserChat muc) {
		muc.addMessageListener(new PacketListener() {
			@Override
			public void processPacket(Packet packet) {
				Message message = (Message) packet;
				/**
				 * 自己发送的消息自己也会接收到。如果不是自己发送的才处理
				 */
				if (!ConstUtil.NICK_NAME
						.equals(getNickNameFromMessage(message))) {
					String roomName = getChatRoomName(muc);
					/**
					 * 根据Message对象封装ChatRoomMessage对象，未设置ChatRoomInfoID
					 */
					ChatRoomMessage chatRoomMessage = new ChatRoomMessage(
							ChatRoomMessage.MessageType_Receive, message
									.getBody());
					chatRoomMessage.setTime(System.currentTimeMillis());
					chatRoomMessage
							.setNickName(getNickNameFromMessage(message));
					/**
					 * 将Message对象和聊天室名称封装进IMParameter对象中
					 */
					List<ChatRoomMessage> list = new ArrayList<ChatRoomMessage>();
					ChatRoomMessage timeMessage = TimeUtil.checkTimeMessage();
					/**
					 * 检查时间，是否需要加入时间消息
					 */
					if (timeMessage != null) {
						list.add(timeMessage);
					}
					/**
					 * 将收到的文本消息存入数据库
					 */
					list.add(chatRoomMessage);
					if (chatRoomActivity != null) {
						/**
						 * 当前打开ChatRoomActivity，在Actvity完成数据库操作
						 */
						IMParameter parameter = new IMParameter();
						parameter.setChatRoomName(roomName);
						parameter.setMessages(list);
						if (!message.getFrom().equals(connection.getUser()))
							chatRoomActivity.receiveMessages(parameter);
					} else {
						/**
						 * 当前未打开ChatRoomActivity，则在这里完成数据库操作
						 */
						MyDataBaseHelper helper = MyDataBaseHelper
								.getDBHelperInstance(context);
						SQLiteDatabase db = helper.getWritableDatabase();
						ChatRoomInfoDao chatRoomInfoDao = DaoFactory
								.getChatRoomInfoDaoInstance();
						ChatRoomMessageDao chatRoomMessageDao = DaoFactory
								.getChatRoomMessageDaoInstance();
						ChatRoomInfo chatRoomInfo = chatRoomInfoDao
								.queryChatRoomInfoByName(db, roomName);
						if (chatRoomInfo != null) {
							chatRoomMessage.setChatroomId(chatRoomInfo.getId());
							/**
							 * 更新聊天室的最新消息和未读数量
							 */
							chatRoomInfo.updateLastMessage(chatRoomMessage);
							chatRoomInfo.updateMessageNum(1);
							chatRoomInfo.setUnreadItems(chatRoomInfo
									.getUnreadItems() + 1);
							chatRoomInfoDao.update(db, chatRoomInfo);
							chatRoomMessageDao.insert(db, chatRoomMessage);
						}
						/**
						 * 如果当前打开的时ChatRoomListActivity，则要通知其更新数据
						 */
						if(chatRoomListActivity != null){
							IChatRoomListAgent agent = chatRoomListActivity;
							agent.refreshLocalChatrooms();
						}
					}

				}
			}
		});
	}

	/**
	 * 加入接收消息的处理器
	 * 
	 * @param muc
	 * @param connection
	 */
	private void addInvitationListener(XMPPConnection connection) {
		if (connection == null)
			return;
		MultiUserChat.addInvitationListener(connection,
				new InvitationListener() {

					@Override
					public void invitationReceived(Connection arg0,
							String arg1, String arg2, String arg3, String arg4,
							Message arg5) {
						// TODO Auto-generated method stub

					}
				});
	}

	/**
	 * 加入主题改变的处理器
	 * 
	 * @param muc
	 */
	private void addSubjectUpdatedListener(MultiUserChat muc) {
		muc.addSubjectUpdatedListener(new SubjectUpdatedListener() {

			@Override
			public void subjectUpdated(String arg0, String arg1) {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * 加入用户自身状态改变的处理器
	 * 
	 * @param muc
	 */
	private void addUserStatusListener(MultiUserChat muc) {
		muc.addUserStatusListener(new UserStatusListener() {

			@Override
			public void voiceRevoked() {
				// TODO Auto-generated method stub

			}

			@Override
			public void voiceGranted() {
				// TODO Auto-generated method stub

			}

			@Override
			public void ownershipRevoked() {
				// TODO Auto-generated method stub

			}

			@Override
			public void ownershipGranted() {
				// TODO Auto-generated method stub

			}

			@Override
			public void moderatorRevoked() {
				// TODO Auto-generated method stub

			}

			@Override
			public void moderatorGranted() {
				// TODO Auto-generated method stub

			}

			@Override
			public void membershipRevoked() {
				// TODO Auto-generated method stub

			}

			@Override
			public void membershipGranted() {
				// TODO Auto-generated method stub

			}

			@Override
			public void kicked(String arg0, String arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void banned(String arg0, String arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void adminRevoked() {
				// TODO Auto-generated method stub

			}

			@Override
			public void adminGranted() {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * 该方法首先从数据库中读出所有的本地聊天室，然后依次加入。当加入异常时，在异常处理中删除该聊天室及该聊天室所有的方法。并提示用户该聊天室已经不存在
	 */
	private void joinAllLocalhatRooms() {
		List<ChatRoomInfo> chatrooms = new ArrayList<ChatRoomInfo>();
		MyDataBaseHelper helper = MyDataBaseHelper.getDBHelperInstance(context);
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
		IMParameter parameter = new IMParameter();
		for (ChatRoomInfo room : chatrooms) {
			MultiUserChat muc = new MultiUserChat(connection, room.getName()
					+ "_" + ConstUtil.APP_NAME + "@conference."
					+ connection.getServiceName());
			mucMap.put(room.getName(), muc);
			parameter.setChatRoomName(room.getName());
			joinChatRoom(parameter);
		}
	}

	/**
	 * ChatRoomActivity类的代理接口。ChatRoomAcvitiy在发消息的时候将自己的实例set到IMParameter对象中，
	 * 在SmackMananger中通过该接口来与ChatRoomActivity交互
	 * 
	 * @author Wo
	 * 
	 */
	public interface IChatRoomActivityAgent {
		/**
		 * 通知Activity更新messages
		 * 
		 * @param list
		 *            新接收到的Message的集合
		 */
		public void receiveMessages(IMParameter parameter);

		/**
		 * 通知ChatRoomActivity该Message发送成功
		 * 
		 * @param parameter
		 *            应该包含该Message在Adapter中List的position
		 */
		public void sendMessageSuccess(IMParameter parameter);

		/**
		 * 通知ChatRoomActivity该Message发送失败
		 * 
		 * @param parameter
		 *            应该包含该Message在Adapter中List的position
		 */
		public void sendMessageFailed(IMParameter parameter);
	}

	@Override
	public void searchAllChatrooms(IMParameter parameter) {
		List<ChatRoomInfo> chatRoomInfos = new ArrayList<ChatRoomInfo>();
		if (connection == null)
			return;
		try {
			Collection<HostedRoom> hostedRooms;
			hostedRooms = MultiUserChat.getHostedRooms(connection,
					connection.getServiceName());
			for (HostedRoom entry : hostedRooms) {
				for (HostedRoom e : MultiUserChat.getHostedRooms(connection,
						entry.getJid())) {
					// TODO 将信息封装成ChatRoomInfo对象，加到list中
					@SuppressWarnings("unused")
					RoomInfo info = MultiUserChat.getRoomInfo(connection,
							e.getJid());
					if (e.getJid().indexOf("@") > 0
							&& e.getName().contains(
									"_" + parameter.getAppName())) {
						ChatRoomInfo chatRoomInfo = new ChatRoomInfo();
						String name = e.getName();
						name = name.substring(0, name.indexOf("_"));
						chatRoomInfo.setName(name);
						chatRoomInfos.add(chatRoomInfo);
					}
				}
			}
			IChatRoomListAgent agent = parameter.getChatRoomListActivity();
			agent.getAllChatroomsOnline(chatRoomInfos);
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @author Wo ChatRoomListActivity类的代理。ChatRoomListActivity继承此接口，
	 *         调用方法时将自己set到IMParameter对象中
	 *         。在SmackManager中通过此接口中的方法进行与ChatRoomListActivity类的数据交互
	 */
	public interface IChatRoomListAgent {
		/**
		 * 在SmackManager中定义的接口，在SmackManager中的searchAllChatrooms方法中调用，
		 * 将属于本应用的所有群发动到群列表中
		 * 
		 * @param chatroomsOnline
		 */
		public void getAllChatroomsOnline(List<ChatRoomInfo> chatroomsOnline);

		public void joinChatroomFailed();
		
		/**
		 * 有消息到达，但是ChatRoomActivity并没有打开，此时打开的时ChatRoomListActivity，
		 * 那么就会通知ChatRoomListActivity刷新本地列表，显示聊天室的最新消息和未读消息。但是
		 * 需要判断此时显示给用户的是否是本地聊天室
		 */
		public void refreshLocalChatrooms();
	}

	public interface IChatRoomCreateAgent {
		public void createSuccess(IMParameter imParameter);

		public void createFail(String info);
	}

	@Override
	public void leaveChatroom(IMParameter parameter) {
		chatRoomActivity = null;
	}

	@Override
	public void getIntoChatroom(IMParameter parameter) {
		chatRoomActivity = parameter.getChatRoomActivity();
	}

	/**
	 * 根据muc对象，经过字符串处理得到聊天室名称。例如：muc的名称为AAAA_app@conference.xxxxxxxx，那么会返回AAAA
	 * 
	 * @param muc
	 * @return 聊天室名称
	 */
	private String getChatRoomName(MultiUserChat muc) {
		String roomName = muc.getRoom();
		roomName = roomName.substring(0, roomName.lastIndexOf("_"));
		return roomName;
	}

	/**
	 * 根据接受到的Message，通过字符串操作得到发送者的昵称
	 * 
	 * @param message
	 *            接收到的Message
	 * @return 发送该消息的用户的昵称
	 */
	private String getNickNameFromMessage(Message message) {
		String nickName = message.getFrom();
		int index = nickName.lastIndexOf('/');
		nickName = nickName.substring(index + 1);
		return nickName;
	}

	@Override
	public void enterChatRoomList(IMParameter parameter) {
		chatRoomListActivity = parameter.getChatRoomListActivity();
	}

	@Override
	public void leaveChatRoomList(IMParameter parameter) {
		chatRoomListActivity = null;
	}

}
