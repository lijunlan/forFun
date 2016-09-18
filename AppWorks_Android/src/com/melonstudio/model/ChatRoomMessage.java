package com.melonstudio.model;

/**
 * 聊天室消息类 2014-04-19
 * 
 * @author Wo
 * 
 */
public class ChatRoomMessage {
	public static final int MessageType_Time = 0;
	public static final int MessageType_Receive = 1;
	public static final int MessageType_Send = 2;
	private long id;
	private int type;
	private String content;
	private long time;
	private long chatroomId;
	private String nickName;

	public ChatRoomMessage(int type, String content) {
		this.type = type;
		this.content = content;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public long getChatroomId() {
		return chatroomId;
	}

	public void setChatroomId(long chatroomId) {
		this.chatroomId = chatroomId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

}
