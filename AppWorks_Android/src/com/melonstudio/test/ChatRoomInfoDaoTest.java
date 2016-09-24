package com.melonstudio.test;

import com.melonstudio.dao.ChatRoomInfoDao;
import com.melonstudio.dao.MyDataBaseHelper;
import com.melonstudio.model.ChatRoomInfo;
import com.melonstudio.util.ConstUtil;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

/**
 * 
 * @author Wo
 * 
 */
public class ChatRoomInfoDaoTest extends AndroidTestCase {

	public void testInsert() {
		MyDataBaseHelper helper = MyDataBaseHelper
				.getDBHelperInstance(getContext());
		SQLiteDatabase db = helper.getWritableDatabase();
		ChatRoomInfo chatRoomInfo = new ChatRoomInfo();
		chatRoomInfo.setDate(System.currentTimeMillis());
		chatRoomInfo.setName("JUNIT");
		chatRoomInfo.setNickName("wohaosu");
		ChatRoomInfoDao dao = new ChatRoomInfoDao();
		long id = dao.insert(db, chatRoomInfo);
		System.out.println(id);
	}

	public void testUpdate() {
		MyDataBaseHelper helper = MyDataBaseHelper
				.getDBHelperInstance(getContext());
		SQLiteDatabase db = helper.getWritableDatabase();
		ChatRoomInfo chatRoomInfo = new ChatRoomInfo();
		ChatRoomInfoDao dao = new ChatRoomInfoDao();
		Cursor cursor = dao.query(db, "select * from "
				+ ConstUtil.CHATROOM_TALBE + " where _id = 1");
		if (cursor.moveToFirst()) {
			chatRoomInfo.setId(cursor.getInt(0));
			chatRoomInfo.setName(cursor.getString(1));
			chatRoomInfo.setNickName("wo");
			dao.update(db, chatRoomInfo);
		}
	}

	public void testDelete() {
		MyDataBaseHelper helper = MyDataBaseHelper
				.getDBHelperInstance(getContext());
		SQLiteDatabase db = helper.getWritableDatabase();
		ChatRoomInfoDao dao = new ChatRoomInfoDao();
		ChatRoomInfo chatRoomInfo = new ChatRoomInfo();
		chatRoomInfo.setId(2);
		dao.delete(db, chatRoomInfo);
	}

	public void testQuery() {
		MyDataBaseHelper helper = MyDataBaseHelper
				.getDBHelperInstance(getContext());
		SQLiteDatabase db = helper.getWritableDatabase();
		String sql = "select * from " + ConstUtil.CHATROOM_TALBE
				+ " where _id = 2";
		ChatRoomInfoDao dao = new ChatRoomInfoDao();
		Cursor cursor = dao.query(db, sql);
		System.out.println("≤È—ØΩ· ¯");
		if (cursor.moveToFirst()) {
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.move(i);
				System.out.println(cursor.getInt(0));
				System.out.println(cursor.getString(1));
			}
		}
	}

}
