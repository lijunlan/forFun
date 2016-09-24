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
	 * ֻ�е�ChatRoomActivity��ǰ̨��ʱ��ſ��Է��ʣ�����ʱ����null
	 */
	private ChatRoomActivity chatRoomActivity;
	/**
	 * ֻ�е�ChatRoomActivity��ǰ̨��ʱ��ſ��Է��ʣ�����ʱ����null
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
				 * ���ӷ������ɹ�
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
				throw new IMRegistException("�˺��Ѵ���");
			} else {
				Log.e("SmackManager", "Regist...IQ.Type.ERROR: "
						+ result.getError().toString());
				throw new IMRegistException("ע��ʧ��;"
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
				 * ��ChatRoomδ������߷Ǳ��������ң���Ӧ��֪ͨ�û�
				 */
				if (parameter.getChatRoomActivity() != null) {
					// TODO ����parameter����
					parameter.getChatRoomActivity()
							.sendMessageFailed(parameter);
				}
				return IMUtli.SEND_FAIL;
			} else {
				try {
					muc.sendMessage(parameter.getMessage().getContent());
					/**
					 * ���ͳɹ�
					 */
					if (parameter.getChatRoomActivity() != null) {
						// TODO ����parameter����
						parameter.getChatRoomActivity().sendMessageSuccess(
								parameter);
					}
					return IMUtli.SEND_SUCCESS;
				} catch (XMPPException e) {
					/**
					 * ����ʧ��
					 */
					e.printStackTrace();
					if (parameter.getChatRoomActivity() != null) {
						// TODO ����parameter����
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
			 * �û���¼
			 */
			connection.login(parameter.getUsername(), parameter.getPassword());
			/**
			 * �û���¼�ɹ��󣬵�¼�����Ѿ������ChatRoom
			 */
			joinAllLocalhatRooms();
		} catch (XMPPException e) {
			throw new LoginException("��¼ʧ��");
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
			throw new IMUpdateException("��������ʧ��");
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
		 * ���ڱ�־�������Ƿ�ɹ�����
		 */
		int flag = 0;
		try {
			/**
			 * �����������ҵ�MUC����
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
			 * ���������ҵ�owner
			 */
			List<String> owners = new ArrayList<String>();
			owners.add(connection.getUser());
			/**
			 * �������������ҵ�����
			 */
			submitForm.setAnswer("muc#roomconfig_roomowners", owners);
			/**
			 * �־õ�������
			 */
			submitForm.setAnswer("muc#roomconfig_persistentroom", true);
			/**
			 * ��������
			 */
			submitForm.setAnswer("muc#roomconfig_allowinvites", true);
			submitForm.setAnswer("muc#roomconfig_passwordprotectedroom", false);
			submitForm.setAnswer("muc#roomconfig_enablelogging", true);
			submitForm.setAnswer("x-muc#roomconfig_reservednick", true);
			submitForm.setAnswer("x-muc#roomconfig_canchangenick", true);
			submitForm.setAnswer("x-muc#roomconfig_registration", false);
			/**
			 * ��������
			 */
			submitForm.setAnswer("muc#roomconfig_roomdesc",
					parameter.getAppName() + "��Group Chat");
			/**
			 * �����������
			 */
			List<String> list = new ArrayList<String>();
			list.add(String.valueOf(parameter.getMaxMembers()));
			submitForm.setAnswer("muc#roomconfig_maxusers", list);
			/**
			 * �Ƿ������û��Լ����뷿�䣨�����Ƿ���Ҫ����Աͬ�⣩
			 */
			if (parameter.isFreeToJoin())
				/**
				 * ��Ҫ����
				 */
				submitForm.setAnswer("muc#roomconfig_membersonly", false);
			else {
				/**
				 * �����Լ�����
				 */
				submitForm.setAnswer("muc#roomconfig_membersonly", true);
			}

			/**
			 * ��������
			 */
			muc.sendConfigurationForm(submitForm);

			/**
			 * �����ɹ���֪ͨ�û����³ɹ������ҰѸ�muc���뵽mucMap��
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
			// ������Ⱥ�Ѿ�����
			if (e.getMessage().startsWith(
					"Creation failed - Missing acknowledge of room creation.")) {
				System.out.println(e.getMessage());
				if (parameter.getChatRoomCreateActivity() != null && flag == 0)
					parameter.getChatRoomCreateActivity().createFail("�������ѱ�ʹ��");
				return IMUtli.CONNECT_FAIL;
			}
			if (parameter.getChatRoomCreateActivity() != null && flag == 0)
				parameter.getChatRoomCreateActivity().createFail("����ʧ��");
		} catch (Exception e) {
			if (parameter.getChatRoomCreateActivity() != null && flag == 0)
				parameter.getChatRoomCreateActivity().createFail("����ʧ��");
		}
		return IMUtli.CREATE_CHATROOM_SUCCESS;
	}

	/**
	 * ʹ���������Ҫע�⣬���������������ǲ����ڵĻ����Ὠ��һ��instant�����͵������ҡ���Ϊ���˴���������֮�⣬�������������ҡ�
	 * �˳������ҵĲ������ǻ��ڵ���Ĳ������������û����룬���ǿ��������������Ӧ�ò�������������
	 */
	@Override
	public int joinChatRoom(IMParameter parameter) {
		// ChatRoom chatRoom = rooms.get(parameter.getChatRoomName());
		MultiUserChat muc = mucMap.get(parameter.getChatRoomName());
		/**
		 * �����ȡ��������ô���Ǽ����µ�������
		 */
		if (muc == null) {
			muc = new MultiUserChat(connection, parameter.getChatRoomName()
					+ "_" + ConstUtil.APP_NAME + "@conference."
					+ connection.getServiceName());
			try {
				muc.join(ConstUtil.NICK_NAME);
				/**
				 * ����ɹ��������Ϣ����
				 */
				addMessageListener(muc);
				addSubjectUpdatedListener(muc);
				addUserStatusListener(muc);
				/**
				 * ����ɹ���д�뱾�����ݿ�
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
				 * ����ʧ�ܣ�֪ͨ�û�
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
		 * �����ȡ�ĵ���˵���Ǳ����Ѽ����Ⱥ����ô�����ֿ��ܣ�1. ����˳�����룻 2.
		 * �������ϸ������Ѿ������ڣ������ʧ�ܡ���ʱӦ��֪ͨ�û���ͬʱɾ�����صĸ������Ҽ���Ϣ
		 */
		else if (!muc.isJoined()) {
			try {
				muc.join(ConstUtil.NICK_NAME);
				/**
				 * ����ɹ��������Ϣ����
				 */
				addMessageListener(muc);
				addSubjectUpdatedListener(muc);
				addUserStatusListener(muc);
			} catch (XMPPException e) {
				e.printStackTrace();
				/**
				 * ����ʧ�ܣ�˵�����������ڷ��������Ѿ�������
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
					 * ��ѯ�����ҳɹ�����ɾ���������ڵ�������Ϣ��������
					 */
					String deleteMessageSql = "delete from "
							+ ConstUtil.CHATROOM_MESSAGE_TABLE
							+ " where chatroomId = \"" + "\"";
					chatRoomMessageDao.delete(db, deleteMessageSql);
					chatRoomInfoDao.delete(db, chatRoomInfo);
				} else {
					/**
					 * ��ѯ����
					 */
				}
				/**
				 * ֪ͨ�û��޷������ԭ��1�������Ҳ����� 2���������������Ҫ����
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
	 * ������Ϣ������
	 * 
	 * @param muc
	 */
	private void addMessageListener(final MultiUserChat muc) {
		muc.addMessageListener(new PacketListener() {
			@Override
			public void processPacket(Packet packet) {
				Message message = (Message) packet;
				/**
				 * �Լ����͵���Ϣ�Լ�Ҳ����յ�����������Լ����͵ĲŴ���
				 */
				if (!ConstUtil.NICK_NAME
						.equals(getNickNameFromMessage(message))) {
					String roomName = getChatRoomName(muc);
					/**
					 * ����Message�����װChatRoomMessage����δ����ChatRoomInfoID
					 */
					ChatRoomMessage chatRoomMessage = new ChatRoomMessage(
							ChatRoomMessage.MessageType_Receive, message
									.getBody());
					chatRoomMessage.setTime(System.currentTimeMillis());
					chatRoomMessage
							.setNickName(getNickNameFromMessage(message));
					/**
					 * ��Message��������������Ʒ�װ��IMParameter������
					 */
					List<ChatRoomMessage> list = new ArrayList<ChatRoomMessage>();
					ChatRoomMessage timeMessage = TimeUtil.checkTimeMessage();
					/**
					 * ���ʱ�䣬�Ƿ���Ҫ����ʱ����Ϣ
					 */
					if (timeMessage != null) {
						list.add(timeMessage);
					}
					/**
					 * ���յ����ı���Ϣ�������ݿ�
					 */
					list.add(chatRoomMessage);
					if (chatRoomActivity != null) {
						/**
						 * ��ǰ��ChatRoomActivity����Actvity������ݿ����
						 */
						IMParameter parameter = new IMParameter();
						parameter.setChatRoomName(roomName);
						parameter.setMessages(list);
						if (!message.getFrom().equals(connection.getUser()))
							chatRoomActivity.receiveMessages(parameter);
					} else {
						/**
						 * ��ǰδ��ChatRoomActivity����������������ݿ����
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
							 * ���������ҵ�������Ϣ��δ������
							 */
							chatRoomInfo.updateLastMessage(chatRoomMessage);
							chatRoomInfo.updateMessageNum(1);
							chatRoomInfo.setUnreadItems(chatRoomInfo
									.getUnreadItems() + 1);
							chatRoomInfoDao.update(db, chatRoomInfo);
							chatRoomMessageDao.insert(db, chatRoomMessage);
						}
						/**
						 * �����ǰ�򿪵�ʱChatRoomListActivity����Ҫ֪ͨ���������
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
	 * ���������Ϣ�Ĵ�����
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
	 * ��������ı�Ĵ�����
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
	 * �����û�����״̬�ı�Ĵ�����
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
	 * �÷������ȴ����ݿ��ж������еı��������ң�Ȼ�����μ��롣�������쳣ʱ�����쳣������ɾ���������Ҽ������������еķ���������ʾ�û����������Ѿ�������
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
		// �ر����ݿ�����
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
	 * ChatRoomActivity��Ĵ���ӿڡ�ChatRoomAcvitiy�ڷ���Ϣ��ʱ���Լ���ʵ��set��IMParameter�����У�
	 * ��SmackMananger��ͨ���ýӿ�����ChatRoomActivity����
	 * 
	 * @author Wo
	 * 
	 */
	public interface IChatRoomActivityAgent {
		/**
		 * ֪ͨActivity����messages
		 * 
		 * @param list
		 *            �½��յ���Message�ļ���
		 */
		public void receiveMessages(IMParameter parameter);

		/**
		 * ֪ͨChatRoomActivity��Message���ͳɹ�
		 * 
		 * @param parameter
		 *            Ӧ�ð�����Message��Adapter��List��position
		 */
		public void sendMessageSuccess(IMParameter parameter);

		/**
		 * ֪ͨChatRoomActivity��Message����ʧ��
		 * 
		 * @param parameter
		 *            Ӧ�ð�����Message��Adapter��List��position
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
					// TODO ����Ϣ��װ��ChatRoomInfo���󣬼ӵ�list��
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
	 * @author Wo ChatRoomListActivity��Ĵ���ChatRoomListActivity�̳д˽ӿڣ�
	 *         ���÷���ʱ���Լ�set��IMParameter������
	 *         ����SmackManager��ͨ���˽ӿ��еķ���������ChatRoomListActivity������ݽ���
	 */
	public interface IChatRoomListAgent {
		/**
		 * ��SmackManager�ж���Ľӿڣ���SmackManager�е�searchAllChatrooms�����е��ã�
		 * �����ڱ�Ӧ�õ�����Ⱥ������Ⱥ�б���
		 * 
		 * @param chatroomsOnline
		 */
		public void getAllChatroomsOnline(List<ChatRoomInfo> chatroomsOnline);

		public void joinChatroomFailed();
		
		/**
		 * ����Ϣ�������ChatRoomActivity��û�д򿪣���ʱ�򿪵�ʱChatRoomListActivity��
		 * ��ô�ͻ�֪ͨChatRoomListActivityˢ�±����б���ʾ�����ҵ�������Ϣ��δ����Ϣ������
		 * ��Ҫ�жϴ�ʱ��ʾ���û����Ƿ��Ǳ���������
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
	 * ����muc���󣬾����ַ�������õ����������ơ����磺muc������ΪAAAA_app@conference.xxxxxxxx����ô�᷵��AAAA
	 * 
	 * @param muc
	 * @return ����������
	 */
	private String getChatRoomName(MultiUserChat muc) {
		String roomName = muc.getRoom();
		roomName = roomName.substring(0, roomName.lastIndexOf("_"));
		return roomName;
	}

	/**
	 * ���ݽ��ܵ���Message��ͨ���ַ��������õ������ߵ��ǳ�
	 * 
	 * @param message
	 *            ���յ���Message
	 * @return ���͸���Ϣ���û����ǳ�
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
