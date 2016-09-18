package com.sdll18.notificationserver.network.data;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class return the tools automatically
 * 
 * @author SDLL18
 * 
 */
public class ExUtil {

	public static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yy/MM/dd HH:mm");

	/**
	 * convert time which is Long type into time which is String type
	 * 
	 * @param str
	 * @param isFormulateTime
	 *            decide whether the output should be a formulated time or just
	 *            milliseconds
	 * @return
	 */
	public static String time2String(String str, boolean isFormulateTime) {
		if (isFormulateTime) {
			long temp = Long.parseLong(str);
			Date d = new Date(temp);
			return dateFormat.format(d);
		} else {
			return str;
		}
	}

}
