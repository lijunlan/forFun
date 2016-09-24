package com.melonstudio.model;

import com.melonstudio.util.ConstUtil;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

/**
 * 
 * @author Wo
 * 
 */
@Table(name = "ChatRoomInfo")
public class ChatRoomInfo {
	@Id
	private long id;
	private String name;
	private long date; // �ϴ��뿪��ʱ��
	private String nickName;
	private int unreadItems;// δ������
	private long messageNum;
	private String lastMessage;

	public long getMessageNum() {
		return messageNum;
	}

	public void setMessageNum(long messageNum) {
		this.messageNum = messageNum;
	}

	public String getLastMessage() {
		return lastMessage;
	}

	public void setLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public int getUnreadItems() {
		return unreadItems;
	}

	public void setUnreadItems(int unreadItems) {
		this.unreadItems = unreadItems;
	}

	/**
	 * ���ݴ����������Ʋ���������������
	 * 
	 * @param name
	 *            �����ҵ�����
	 * @return �����Ҷ���
	 */
	public static ChatRoomInfo createNewInstance(String name) {
		ChatRoomInfo chatRoomInfo = new ChatRoomInfo();
		chatRoomInfo.setName(name);
		chatRoomInfo.setDate(System.currentTimeMillis());
		chatRoomInfo.setLastMessage("");
		chatRoomInfo.setMessageNum(0);
		chatRoomInfo.setNickName(ConstUtil.NICK_NAME);
		chatRoomInfo.setUnreadItems(0);
		return chatRoomInfo;
	}

	/**
	 * ����message����chatroom��lastMessage
	 * 
	 * @param message
	 */
	public void updateLastMessage(ChatRoomMessage message) {
		if (message != null && message.getTime() != 0
				&& message.getContent() != null
				&& message.getNickName() != null) {
			final int length = 25;
			this.setDate(message.getTime());
			/**
			 * ������Լ����͵���Ϣ�����ü��ǳ�
			 */
			if (message.getNickName().equals(ConstUtil.NICK_NAME)) {
				String content = message.getContent().length() > length ? message
						.getContent().substring(0, length) + "..."
						: message.getContent();
				this.setLastMessage(content);
			}
			/**
			 * ����Ǳ��˷��͵���Ϣ����Ҫ���ǳ�
			 */
			else {
				String content = message.getNickName() + ":"
						+ message.getContent();
				content = content.length() > length ? content.substring(0,
						length) + "..." : content;
				this.setLastMessage(content);
			}
		}
	}

	/**
	 * ���ݴ����num����MessageNum�������� ���num��0������㣬���num<0����������
	 * 
	 * @param num
	 *            MessageNum������
	 */
	public void updateMessageNum(int num) {
		if (num == 0)
			this.messageNum = num;
		else if (num > 0) {
			this.messageNum += num;
		}
	}

}
