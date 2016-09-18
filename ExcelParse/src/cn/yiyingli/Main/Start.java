package cn.yiyingli.Main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import cn.yiyingli.ExcelPrase.ExcelReader;

public class Start {

	public static void main(String[] args) throws SQLException, FileNotFoundException {
		List<String> phoneList = new ArrayList<String>();
		phoneList.add("86-15968179170");
		phoneList.add("86-13355928056");
		phoneList.add("86-13285522155");
		phoneList.add("86-18060735222");
		phoneList.add("86-18221023852");
		phoneList.add("86-18670084800");
		phoneList.add("1-9025806374");
		phoneList.add("1-6265548144");
		phoneList.add("86-18670762853");
		phoneList.add("86-17816862664");
		phoneList.add("86-18826076235");
		phoneList.add("86-13336023558");
		phoneList.add("86-13476831539");
		phoneList.add("86-15968850355");
		sendMultiMsg(phoneList, "请加微信好友：clove930423，进行咨询(马尔代夫义工项目)，添加时请 备注姓名+订单号后四位，谢谢.");
//		String message = "尊敬的用户您好，由于近期陈彦含导师有大量人预约，导致其微信无法添加更多好友，麻烦大家通过微信号：clove930423，查找并添加好友，同时请备注上：姓名+订单号末尾4位数。带给您的不便请谅解，谢谢您对一英里的支持。";
//		InputStream is = new FileInputStream("F:/陈彦含.xls");
//		ExcelReader excelReader = new ExcelReader();
//		List<List<String>> list = excelReader.getCellsByColNum(is, new int[] { 2 });
//		List<String> ps = new ArrayList<String>();
//		for (int i = 1; i <= list.size(); i++) {
//			if (i % 100 != 0) {
//				ps.add(list.get(i - 1).get(0));
//			} else {
//				sendMultiMsg(ps, message);
//				System.out.println(ps.size());
//				ps.clear();
//				ps.add(list.get(i - 1).get(0));
//			}
//		}
//		if (ps.size() > 0) {
//			sendMultiMsg(ps, message);
//		}
//		System.out.println(ps.size());
		// for (List<String> l : list) {
		// ps.add(l.get(0));
		// }
		// for (List<String> l : list) {
		//
		// // for (String p : l) {
		// // sendChinaMsg(p,
		// //
		// "2016将至，一英里谢谢你的陪伴！我们为你找到了更多摄影、求职、减肥达人，一站到底选手、麦肯锡大牛、时尚美妆博主，还贴心准备了[元旦取经红包]，用一顿饭的钱聊一场胜读十年书的天，登陆www.1yingli.cn，开开脑洞取取经！回N退订！");
		// // count++;
		// // }
		// }
		// sendChinaMsg("17764518709", message);
	}

	private static String sendMultiMsg(List<String> phoneList, String msg) {
		String message = "";
		String phones = "";
		for (String p : phoneList) {
			phones = phones + p.replaceAll("-", "") + ",";
		}
		phones = phones.substring(0, phones.length() - 1);
		System.out.println(phones);
		try {
			message = hexString(msg.getBytes("GBK"));

		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		HttpClient httpClient = HttpClients.createDefault();
		String url = "http://api2.santo.cc/submit?command=MULTI_MT_REQUEST&cpid=" + MESSAGE_NAME + "&cppwd="
				+ MESSAGE_PASSWD + "&da=" + phones + "&dc=15&sm=" + message;
		System.out.println(url);
		HttpGet get = new HttpGet(url);
		String result = null;
		try {
			HttpResponse response = httpClient.execute(get);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity(), "utf-8");
			}
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private static String sendChinaMsg(String phone, String msg) {
		String message = "";
		try {
			message = hexString(msg.getBytes("GBK"));

		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		HttpClient httpClient = HttpClients.createDefault();
		HttpGet get = new HttpGet("http://api2.santo.cc/submit?command=MT_REQUEST&cpid=" + MESSAGE_NAME + "&cppwd="
				+ MESSAGE_PASSWD + "&da=86" + phone + "&dc=15&sm=" + message);
		String result = null;
		try {
			HttpResponse response = httpClient.execute(get);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity(), "utf-8");
			}
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// private static final String MESSAGE_NAME = "yyl-ipxmt";
	// private static final String MESSAGE_NAME = "yylipxmt1";
	private static final String MESSAGE_NAME = "yylipxmt2";

	// private static final String MESSAGE_PASSWD = "IQ8R1Wpy";
	// private static final String MESSAGE_PASSWD = "dN0kLByh";
	private static final String MESSAGE_PASSWD = "LE0TMxXd";

	private static String hexString(byte[] b) {
		StringBuffer d = new StringBuffer(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			char hi = Character.forDigit((b[i] >> 4) & 0x0F, 16);
			char lo = Character.forDigit(b[i] & 0x0F, 16);
			d.append(Character.toUpperCase(hi));
			d.append(Character.toUpperCase(lo));
		}
		return d.toString().toLowerCase();
	}
}
