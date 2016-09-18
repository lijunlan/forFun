package com.melonstudio.dao;

import com.melonstudio.model.ChatRoomInfo;
import com.melonstudio.util.ConstUtil;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 
 * @author Wo
 * 
 */
public class ChatRoomInfoDao {
	public final static String searchAllSql = "Select * from "
			+ ConstUtil.CHATROOM_TALBE;
	private final String queryByName = "select * from "
			+ ConstUtil.CHATROOM_TALBE + " where name = ? ";

	public long insert(SQLiteDatabase db, ChatRoomInfo chatRoomInfo) {
		db.execSQL(
				"insert into " + ConstUtil.CHATROOM_TALBE
						+ " values(null,?,?,?,?,?,?)",
				new String[] { chatRoomInfo.getName(),
						String.valueOf(chatRoomInfo.getDate()),
						chatRoomInfo.getNickName(),
						String.valueOf(chatRoomInfo.getUnreadItems()),
						String.valueOf(chatRoomInfo.getMessageNum()),
						chatRoomInfo.getLastMessage() });
		String sql = "select last_insert_rowid() from "
				+ ConstUtil.CHATROOM_TALBE;
		Cursor cursor = db.rawQuery(sql, null);
		long id = -1;
		if (cursor.moveToFirst()) {
			id = cursor.getLong(0);
		}
		return id;
	}

	/*
	 * public long insertWithId(SQLiteDatabase db, ChatRoomInfo chatRoomInfo) {
	 * ContentValues contentValues = new ContentValues();
	 * contentValues.put("name", chatRoomInfo.getName());
	 * contentValues.put("date", chatRoomInfo.getDate());
	 * contentValues.put("nickname", chatRoomInfo.getNickName());
	 * contentValues.put("haspassword", chatRoomInfo.getHasPassword());
	 * contentValues.put("password", chatRoomInfo.getPassword());
	 * contentValues.put("unreadItems", chatRoomInfo.getUnreadItems()); return
	 * db.insert(ConstUtil.CHATROOM_TALBE, null, contentValues); }
	 */

	public void update(SQLiteDatabase db, ChatRoomInfo chatRoomInfo) {
		db.execSQL(
				"update "
						+ ConstUtil.CHATROOM_TALBE
						+ " set name = ?, date=?, nickname=?, unreadItems=?, messageNum=?, lastMessage=? where _id = ?",
				new String[] { chatRoomInfo.getName(),
						String.valueOf(chatRoomInfo.getDate()),
						chatRoomInfo.getNickName(),
						String.valueOf(chatRoomInfo.getUnreadItems()),
						String.valueOf(chatRoomInfo.getMessageNum()),
						chatRoomInfo.getLastMessage(),
						String.valueOf(chatRoomInfo.getId()) });
	}

	public void delete(SQLiteDatabase db, ChatRoomInfo chatRoomInfo) {
		db.execSQL(
				"delete from " + ConstUtil.CHATROOM_TALBE + " where _id = ?",
				new String[] { String.valueOf(chatRoomInfo.getId()) });

	}

	public Cursor query(SQLiteDatabase db, String sql) {
		Cursor cursor = db.rawQuery(sql, null);
		return cursor;
	}

	public ChatRoomInfo queryChatRoomInfo(SQLiteDatabase db, String sql) {
		Cursor cursor = query(db, sql);
		ChatRoomInfo chatRoomInfo = null;
		chatRoomInfo = createChatRoomFromCursor(cursor, 0);
		cursor.close();
		return chatRoomInfo;
	}

	/**
	 * 根据ChatRoomInfo的名字来查询
	 * 
	 * @param db
	 *            数据库的实例
	 * @param name
	 *            聊天室的名字
	 * @return ChatRoomInfo对象
	 */
	public ChatRoomInfo queryChatRoomInfoByName(SQLiteDatabase db, String name) {
		Cursor cursor = db.rawQuery(queryByName, new String[] { name });
		ChatRoomInfo chatRoomInfo = createChatRoomFromCursor(cursor, 0);
		return chatRoomInfo;
	}

	/**
	 * 调用完该方法后，cursor会被关闭
	 * 
	 * @param cursor
	 * @param index
	 * @return
	 */
	public ChatRoomInfo createChatRoomFromCursor(Cursor cursor, int index) {
		ChatRoomInfo chatRoomInfo = null;
		if (cursor.moveToPosition(index)) {
			chatRoomInfo = new ChatRoomInfo();
			chatRoomInfo.setId(cursor.getLong(0));
			chatRoomInfo.setName(cursor.getString(1));
			chatRoomInfo.setDate(cursor.getLong(2));
			chatRoomInfo.setNickName(cursor.getString(3));
			chatRoomInfo.setUnreadItems(cursor.getInt(4));
			chatRoomInfo.setMessageNum(cursor.getLong(5));
			chatRoomInfo.setLastMessage(cursor.getString(6));
		}
		cursor.close();
		return chatRoomInfo;
	}

	/**
	 * 调用完该方法以后，不会关闭cursor
	 * 
	 * @param cursor
	 * @param index
	 * @return
	 */
	public ChatRoomInfo createChatRoomFromCursorWithNoCloseCursor(
			Cursor cursor, int index) {
		ChatRoomInfo chatRoomInfo = null;
		if (cursor.moveToPosition(index)) {
			chatRoomInfo = new ChatRoomInfo();
			chatRoomInfo.setId(cursor.getLong(0));
			chatRoomInfo.setName(cursor.getString(1));
			chatRoomInfo.setDate(cursor.getLong(2));
			chatRoomInfo.setNickName(cursor.getString(3));
			chatRoomInfo.setUnreadItems(cursor.getInt(4));
			chatRoomInfo.setMessageNum(cursor.getLong(5));
			chatRoomInfo.setLastMessage(cursor.getString(6));
		}
		return chatRoomInfo;
	}

}
