package com.melonstudio.factory;

import com.melonstudio.dao.ChatRoomInfoDao;
import com.melonstudio.dao.ChatRoomMessageDao;

/**
 * 
 * @author Wo
 * 
 */
public class DaoFactory {
	private static ChatRoomInfoDao chatRoomInfoDao;
	private static ChatRoomMessageDao chatRoomMessageDao;

	synchronized public static ChatRoomInfoDao getChatRoomInfoDaoInstance() {
		if (chatRoomInfoDao == null)
			chatRoomInfoDao = new ChatRoomInfoDao();
		return chatRoomInfoDao;
	}

	synchronized public static ChatRoomMessageDao getChatRoomMessageDaoInstance() {
		if (chatRoomMessageDao == null)
			chatRoomMessageDao = new ChatRoomMessageDao();
		return chatRoomMessageDao;
	}
}
