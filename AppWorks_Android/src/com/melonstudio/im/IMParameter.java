package com.melonstudio.im;

import java.util.List;

import android.content.Context;

import com.melonstudio.appworks.ChatRoomActivity;
import com.melonstudio.appworks.ChatRoomCreateActivity;
import com.melonstudio.appworks.ChatRoomListActivity;
import com.melonstudio.model.ChatRoomMessage;

/**
 * 
 * @author Wo
 * 
 */
public class IMParameter {

	private String username;
	private String password;
	private String chatRoomName;
	private String appName;
	private String nickName;
	private String chatRoomPassword;
	private String chatRoomDescription;
	private int maxMembers;
	private ChatRoomMessage message;
	private ChatRoomActivity chatRoomActivity;
	private ChatRoomListActivity chatRoomListActivity;
	private ChatRoomCreateActivity chatRoomCreateActivity;
	private boolean freeToJoin;
	private Context context;
	private List<ChatRoomMessage> messages;

	public List<ChatRoomMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<ChatRoomMessage> messages) {
		this.messages = messages;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isFreeToJoin() {
		return freeToJoin;
	}

	public void setFreeToJoin(boolean freeToJoin) {
		this.freeToJoin = freeToJoin;
	}

	public ChatRoomActivity getChatRoomActivity() {
		return chatRoomActivity;
	}

	public void setChatRoomActivity(ChatRoomActivity chatRoomActivity) {
		this.chatRoomActivity = chatRoomActivity;
	}

	public ChatRoomMessage getMessage() {
		return message;
	}

	public void setMessage(ChatRoomMessage message) {
		this.message = message;
	}

	public int getMaxMembers() {
		return maxMembers;
	}

	public void setMaxMembers(int maxMembers) {
		this.maxMembers = maxMembers;
	}

	public String getChatRoomDescription() {
		return chatRoomDescription;
	}

	public void setChatRoomDescription(String chatRoomDescription) {
		this.chatRoomDescription = chatRoomDescription;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getChatRoomName() {
		return chatRoomName;
	}

	public void setChatRoomName(String chatRoomName) {
		this.chatRoomName = chatRoomName;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getChatRoomPassword() {
		return chatRoomPassword;
	}

	public void setChatRoomPassword(String chatRoomPassword) {
		this.chatRoomPassword = chatRoomPassword;
	}

	public ChatRoomListActivity getChatRoomListActivity() {
		return chatRoomListActivity;
	}

	public void setChatRoomListActivity(
			ChatRoomListActivity chatRoomListActivity) {
		this.chatRoomListActivity = chatRoomListActivity;
	}

	public ChatRoomCreateActivity getChatRoomCreateActivity() {
		return chatRoomCreateActivity;
	}

	public void setChatRoomCreateActivity(
			ChatRoomCreateActivity chatRoomCreateActivity) {
		this.chatRoomCreateActivity = chatRoomCreateActivity;
	}

}
