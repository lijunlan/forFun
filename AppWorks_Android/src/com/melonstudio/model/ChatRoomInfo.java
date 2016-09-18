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
	private long date; // 上次离开的时间
	private String nickName;
	private int unreadItems;// 未读数量
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
	 * 根据传进来的名称参数，创建聊天室
	 * 
	 * @param name
	 *            聊天室的名字
	 * @return 聊天室对象
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
	 * 根据message更新chatroom的lastMessage
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
			 * 如果是自己发送的消息，则不用加昵称
			 */
			if (message.getNickName().equals(ConstUtil.NICK_NAME)) {
				String content = message.getContent().length() > length ? message
						.getContent().substring(0, length) + "..."
						: message.getContent();
				this.setLastMessage(content);
			}
			/**
			 * 如果是别人发送的消息，需要加昵称
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
	 * 根据传入的num增加MessageNum的数量。 如果num是0，则归零，如果num<0，不作处理
	 * 
	 * @param num
	 *            MessageNum的增量
	 */
	public void updateMessageNum(int num) {
		if (num == 0)
			this.messageNum = num;
		else if (num > 0) {
			this.messageNum += num;
		}
	}

}
