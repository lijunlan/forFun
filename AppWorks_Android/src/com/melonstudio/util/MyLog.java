package com.melonstudio.util;

import android.util.Log;

public class MyLog {
	public static void log(String tag, String msg) {
		Log.println(Log.ASSERT, tag, msg);
	}
}
