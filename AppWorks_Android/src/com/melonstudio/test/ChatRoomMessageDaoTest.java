package com.melonstudio.test;

import com.melonstudio.dao.ChatRoomMessageDao;
import com.melonstudio.dao.MyDataBaseHelper;
import com.melonstudio.factory.DaoFactory;
import com.melonstudio.model.ChatRoomMessage;
import com.melonstudio.util.ConstUtil;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

public class ChatRoomMessageDaoTest extends AndroidTestCase {

	public void testInsert() {
		MyDataBaseHelper helper = MyDataBaseHelper
				.getDBHelperInstance(getContext());
		SQLiteDatabase db = helper.getWritableDatabase();
		ChatRoomMessageDao dao = DaoFactory.getChatRoomMessageDaoInstance();
		ChatRoomMessage message = new ChatRoomMessage(
				ChatRoomMessage.MessageType_Receive, "ÄãºÃÑ½");
		message.setNickName("wooooo");
		message.setTime(System.currentTimeMillis());
		message.setChatroomId(1);
		dao.insert(db, message);
	}

	public void testUpdate() {
		MyDataBaseHelper helper = MyDataBaseHelper
				.getDBHelperInstance(getContext());
		SQLiteDatabase db = helper.getWritableDatabase();
		ChatRoomMessageDao dao = DaoFactory.getChatRoomMessageDaoInstance();
		ChatRoomMessage message = new ChatRoomMessage(
				ChatRoomMessage.MessageType_Receive, "ÄãºÃÑ½");
		message.setNickName("wooooo");
		message.setTime(System.currentTimeMillis());
		message.setChatroomId(1);
		dao.insert(db, message);
		message.setId(2);
		message.setNickName("leeeee");
		dao.update(db, message);
	}

	public void testDelete() {
		ChatRoomMessage chatRoomMessage = new ChatRoomMessage(1, "asdf");
		chatRoomMessage.setId(1);
		ChatRoomMessageDao dao = DaoFactory.getChatRoomMessageDaoInstance();
		MyDataBaseHelper helper = MyDataBaseHelper
				.getDBHelperInstance(getContext());
		SQLiteDatabase db = helper.getWritableDatabase();
		dao.delete(db, chatRoomMessage);
	}

	public void testQuery() {
		final String sql = "select * from " + ConstUtil.CHATROOM_MESSAGE_TABLE
				+ " where _id = 2";
		MyDataBaseHelper helper = MyDataBaseHelper
				.getDBHelperInstance(getContext());
		SQLiteDatabase db = helper.getWritableDatabase();
		ChatRoomMessageDao dao = DaoFactory.getChatRoomMessageDaoInstance();
		Cursor cursor = dao.query(db, sql);
		if (cursor.moveToFirst()) {
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.move(i);
				cursor.getLong(0); // id
				cursor.getInt(1); // type
				cursor.getString(2); // content
				cursor.getLong(3); // time
				cursor.getLong(4); // chatroomId
				cursor.getString(5);// nickName
			}
		}
	}
}
