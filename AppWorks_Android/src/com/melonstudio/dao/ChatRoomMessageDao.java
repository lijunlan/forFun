package com.melonstudio.dao;

import java.util.ArrayList;
import java.util.List;

import com.melonstudio.model.ChatRoomMessage;
import com.melonstudio.util.ConstUtil;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 
 * @author Wo
 * 
 */
public class ChatRoomMessageDao {
	public void insert(SQLiteDatabase db, ChatRoomMessage chatRoomMessage) {
		String hql = "insert into " + ConstUtil.CHATROOM_MESSAGE_TABLE
				+ " values(null,?,?,?,?,?)";
		db.execSQL(
				hql,
				new String[] { String.valueOf(chatRoomMessage.getType()),
						chatRoomMessage.getContent(),
						String.valueOf(chatRoomMessage.getTime()),
						String.valueOf(chatRoomMessage.getChatroomId()),
						chatRoomMessage.getNickName() });
	}

	public void update(SQLiteDatabase db, ChatRoomMessage chatRoomMessage) {
		String hql = "update "
				+ ConstUtil.CHATROOM_MESSAGE_TABLE
				+ " set type=?,content=?,time=?,chatroomId=?,nickName=? where _id =?";
		db.execSQL(
				hql,
				new String[] { String.valueOf(chatRoomMessage.getType()),
						chatRoomMessage.getContent(),
						String.valueOf(chatRoomMessage.getTime()),
						String.valueOf(chatRoomMessage.getChatroomId()),
						chatRoomMessage.getNickName(),
						String.valueOf(chatRoomMessage.getId()) });
	}

	/**
	 * 根据传进来的对象，来删除
	 * 
	 * @param db
	 * @param chatRoomMessage
	 */
	public void delete(SQLiteDatabase db, ChatRoomMessage chatRoomMessage) {
		String hql = "delete from " + ConstUtil.CHATROOM_MESSAGE_TABLE
				+ " where _id = ?";
		db.execSQL(hql,
				new String[] { String.valueOf(chatRoomMessage.getId()) });
	}

	/**
	 * 根据传进来的sql语句进行删除，支持批量删除，但是未做sql正确性检查
	 * 
	 * @param db
	 * @param sql
	 */
	public void delete(SQLiteDatabase db, String sql) {
		db.execSQL(sql);
	}

	/**
	 * 查询符合sql的ChatRoomMessages对象
	 * 
	 * @param db
	 * @param sql
	 * @return cursor对象
	 */
	public Cursor query(SQLiteDatabase db, String sql) {
		return db.rawQuery(sql, null);
	}

	/**
	 * 查询所有符合条件的ChatRoomMessage对象
	 * 
	 * @param db
	 * @param sql
	 * @return 装有ChatRoomMessage对象的List集合
	 */
	public List<ChatRoomMessage> queryChatRoomMessages(SQLiteDatabase db,
			String sql) {
		List<ChatRoomMessage> list = new ArrayList<ChatRoomMessage>();
		Cursor cursor = query(db, sql);
		list.addAll(createChatRoomMessagesFromCursor(cursor));
		cursor.close();
		return list;
	}

	private List<ChatRoomMessage> createChatRoomMessagesFromCursor(Cursor cursor) {
		List<ChatRoomMessage> list = new ArrayList<ChatRoomMessage>();
		if (cursor.moveToFirst()) {
			for (int i = 0; i < cursor.getCount(); i++) {
				if (cursor.moveToPosition(i)) {
					ChatRoomMessage message = new ChatRoomMessage(
							cursor.getInt(1), cursor.getString(2));
					message.setId(cursor.getLong(0));
					message.setTime(cursor.getLong(3));
					message.setChatroomId(cursor.getLong(4));
					message.setNickName(cursor.getString(5));
					list.add(message);
				}
			}
		}
		cursor.close();
		return list;
	}
}
