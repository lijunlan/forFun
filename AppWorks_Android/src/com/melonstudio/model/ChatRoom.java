package com.melonstudio.model;

import org.jivesoftware.smackx.muc.MultiUserChat;

import com.melonstudio.appworks.ChatRoomActivity;

/**
 * 包括对ChatRoomActivity的引用和聊天室的连接
 * 
 * @author Wo
 * 
 */
public class ChatRoom {
	private ChatRoomActivity activity;
	private MultiUserChat multiUserChat;

	public ChatRoomActivity getActivity() {
		return activity;
	}

	public void setActivity(ChatRoomActivity activity) {
		this.activity = activity;
	}

	public MultiUserChat getMultiUserChat() {
		return multiUserChat;
	}

	public void setMultiUserChat(MultiUserChat multiUserChat) {
		this.multiUserChat = multiUserChat;
	}

}
