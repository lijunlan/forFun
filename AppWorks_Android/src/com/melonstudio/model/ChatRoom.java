package com.melonstudio.model;

import org.jivesoftware.smackx.muc.MultiUserChat;

import com.melonstudio.appworks.ChatRoomActivity;

/**
 * ������ChatRoomActivity�����ú������ҵ�����
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
