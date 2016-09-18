package com.melonstudio.util;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.melonstudio.model.ChatRoomMessage;

/**
 * 
 * @author Wo
 * 
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtil {
	public static final int SECONDS_IN_DAY = 60 * 60 * 24;
	public static final long MILLIS_IN_DAY = 1000L * SECONDS_IN_DAY;

	private static SimpleDateFormat dayDateFormat;
	private static SimpleDateFormat generalDateFormat;
	private static SimpleDateFormat commentDateFormat;
	private static SimpleDateFormat simple_generalDateFormat;
	private static long lastMessageTime = 0;

	static {
		dayDateFormat = new SimpleDateFormat("HH:mm");
		generalDateFormat = new SimpleDateFormat("MM-dd HH:mm");
		commentDateFormat = new SimpleDateFormat("MMMMM dd,yyyy ", Locale.US);
		simple_generalDateFormat = new SimpleDateFormat("MM-dd");
	}

	public static boolean isSameDayOfMillis(final long ms1, final long ms2) {
		final long interval = ms1 - ms2;
		return interval < MILLIS_IN_DAY && interval > -1L * MILLIS_IN_DAY
				&& toDay(ms1) == toDay(ms2);
	}

	private static long toDay(long millis) {
		return (millis + TimeZone.getDefault().getOffset(millis))
				/ MILLIS_IN_DAY;
	}

	public static String simple_formateDateTime(long time) {

		Date date = null;
		date = new Date(time);

		Calendar current = Calendar.getInstance();

		Calendar today = Calendar.getInstance(); // ����

		today.set(Calendar.YEAR, current.get(Calendar.YEAR));
		today.set(Calendar.MONTH, current.get(Calendar.MONTH));
		today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
		// Calendar.HOUR����12Сʱ�Ƶ�Сʱ�� Calendar.HOUR_OF_DAY����24Сʱ�Ƶ�Сʱ��
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);

		Calendar yesterday = Calendar.getInstance(); // ����

		yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
		yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
		yesterday.set(Calendar.DAY_OF_MONTH,
				current.get(Calendar.DAY_OF_MONTH) - 1);
		yesterday.set(Calendar.HOUR_OF_DAY, 0);
		yesterday.set(Calendar.MINUTE, 0);
		yesterday.set(Calendar.SECOND, 0);

		current.setTime(date);

		if (current.after(today)) {
			return dayDateFormat.format(date);
		} else if (current.before(today) && current.after(yesterday)) {
			return "\u6628\u5929 " + dayDateFormat.format(date);
		} else {
			return simple_generalDateFormat.format(date);
		}

	}

	public static String formatDateTime(long time) {
		Date date = null;
		date = new Date(time);

		Calendar current = Calendar.getInstance();

		Calendar today = Calendar.getInstance(); // ����

		today.set(Calendar.YEAR, current.get(Calendar.YEAR));
		today.set(Calendar.MONTH, current.get(Calendar.MONTH));
		today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
		// Calendar.HOUR����12Сʱ�Ƶ�Сʱ�� Calendar.HOUR_OF_DAY����24Сʱ�Ƶ�Сʱ��
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);

		Calendar yesterday = Calendar.getInstance(); // ����

		yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
		yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
		yesterday.set(Calendar.DAY_OF_MONTH,
				current.get(Calendar.DAY_OF_MONTH) - 1);
		yesterday.set(Calendar.HOUR_OF_DAY, 0);
		yesterday.set(Calendar.MINUTE, 0);
		yesterday.set(Calendar.SECOND, 0);

		current.setTime(date);

		if (current.after(today)) {
			return dayDateFormat.format(date);
		} else if (current.before(today) && current.after(yesterday)) {
			return "\u6628\u5929 " + dayDateFormat.format(date);
		} else {
			return generalDateFormat.format(date);
		}
	}

	public static ChatRoomMessage checkTimeMessage() {
		long time = System.currentTimeMillis();
		ChatRoomMessage message = null;
		if (lastMessageTime == 0 || (time - lastMessageTime > 5 * 60 * 1000)) {
			message = new ChatRoomMessage(ChatRoomMessage.MessageType_Time,
					null);
			message.setTime(System.currentTimeMillis());
			lastMessageTime = time;
		}
		return message;
	}

	/**
	 * formate the date
	 * 
	 * @param time
	 *            the time you want to format
	 * @return the structured date string
	 */
	public static String generalFormat(long time) {
		Date date = new Date(time);
		String date_str = commentDateFormat.format(date);
		return date_str;
	}

	public static boolean isToday(long time) {
		Date date = null;
		date = new Date(time);

		Calendar current = Calendar.getInstance();

		Calendar today = Calendar.getInstance(); // ����

		today.set(Calendar.YEAR, current.get(Calendar.YEAR));
		today.set(Calendar.MONTH, current.get(Calendar.MONTH));
		today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
		// Calendar.HOUR����12Сʱ�Ƶ�Сʱ�� Calendar.HOUR_OF_DAY����24Сʱ�Ƶ�Сʱ��
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);

		Calendar yesterday = Calendar.getInstance(); // ����

		yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
		yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
		yesterday.set(Calendar.DAY_OF_MONTH,
				current.get(Calendar.DAY_OF_MONTH) - 1);
		yesterday.set(Calendar.HOUR_OF_DAY, 0);
		yesterday.set(Calendar.MINUTE, 0);
		yesterday.set(Calendar.SECOND, 0);

		current.setTime(date);

		if (current.after(today)) {
			return true;
		} else {
			return false;
		}

	}

	public static boolean isYesterday(long time) {
		Date date = null;
		date = new Date(time);

		Calendar current = Calendar.getInstance();

		Calendar today = Calendar.getInstance(); // ����

		today.set(Calendar.YEAR, current.get(Calendar.YEAR));
		today.set(Calendar.MONTH, current.get(Calendar.MONTH));
		today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
		// Calendar.HOUR����12Сʱ�Ƶ�Сʱ�� Calendar.HOUR_OF_DAY����24Сʱ�Ƶ�Сʱ��
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);

		Calendar yesterday = Calendar.getInstance(); // ����

		yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
		yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
		yesterday.set(Calendar.DAY_OF_MONTH,
				current.get(Calendar.DAY_OF_MONTH) - 1);
		yesterday.set(Calendar.HOUR_OF_DAY, 0);
		yesterday.set(Calendar.MINUTE, 0);
		yesterday.set(Calendar.SECOND, 0);

		current.setTime(date);
		if (current.before(today) && current.after(yesterday)) {
			return true;
		} else {
			return false;
		}
	}
}
