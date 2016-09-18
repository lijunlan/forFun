package com.melonstudio.dao;

import com.melonstudio.util.ConstUtil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * @author Wo
 * 
 */
public class MyDataBaseHelper extends SQLiteOpenHelper {

	public static MyDataBaseHelper myDataBaseHelper;

	final String CREATE_CHATROOM_TABLE = "create table "
			+ ConstUtil.CHATROOM_TALBE
			+ "(_id integer primary"
			+ " key autoincrement, name, date, nickname, unreadItems, messageNum, lastMessage)";
	final String CREATE_CHATROOMMESSAGE_TABLE = "create table "
			+ ConstUtil.CHATROOM_MESSAGE_TABLE
			+ "(_id integer primary key autoincrement, type, content, time, chatroomId, nickName)";

	private MyDataBaseHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_CHATROOM_TABLE);
		db.execSQL(CREATE_CHATROOMMESSAGE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	synchronized public static MyDataBaseHelper getDBHelperInstance(
			Context context) {
		if (myDataBaseHelper == null)
			myDataBaseHelper = new MyDataBaseHelper(context, ConstUtil.DB_NAME,
					null, 1);
		return myDataBaseHelper;
	}

}
