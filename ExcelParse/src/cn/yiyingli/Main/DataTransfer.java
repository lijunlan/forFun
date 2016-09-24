package cn.yiyingli.Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import net.sf.json.JSONObject;

public class DataTransfer {

	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
			dest = dest.replaceAll("'", "\"");
			dest = dest.replaceAll("\b", "");
		}
		return dest;
	}

	private static String changeNull(String str) {
		String n = str.replaceAll("'null'", "null");
		n = n.replaceAll("'true'", "true");
		n = n.replaceAll("'false'", "false");
		return n;
	}

	private static String send(String url, String json) {
		System.out.println("send>>>" + json);
		HttpClient httpClient = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		post.addHeader(HTTP.CONTENT_TYPE, "text/json");
		StringEntity se = new StringEntity(json, "utf-8");
		se.setContentType("text/json");
		se.setContentEncoding(new BasicHeader(HTTP.CONTENT_ENCODING, "utf-8"));
		post.setEntity(se);
		String result = "";
		try {
			HttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity(), "utf-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("receive>>" + result);
		return result;
	}

	public static List<String> failList = new ArrayList<String>();

	public static void main(String[] args) {
		// for (int i = 1; i <= 354; i++) {
		// insertTeacher(i);
		// }
		// for (String s : failList) {
		// System.out.println("failed:" + s);
		// }
		// correctTeacherId();
		transferOrder();
		// refreshTeacherCount();
		// transferTeacher();
		// System.out.println(Calendar.getInstance().getTimeInMillis());
	}

	public static void refreshTeacherCount() {
		String urlNew = "jdbc:mysql://yiyingli.mysql.rds.aliyuncs.com:3306/yiyingli2_1?user=sdll18&password=ll1992917&useUnicode=true&characterEncoding=UTF8";
		Connection conn = null;
		String url = "jdbc:mysql://yiyingli.mysql.rds.aliyuncs.com:3306/yiyingli?user=sdll18&password=ll1992917&useUnicode=true&characterEncoding=UTF8";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url);
			Connection conn2 = DriverManager.getConnection(urlNew);
			Statement statement = conn2.createStatement();
			Statement stmt = conn.createStatement();
			String sql = "select * from teacher ;";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				long TEACHER_ID = rs.getLong("TEACHER_ID");
				long CHECKPASSAGENUMBER = rs.getLong("CHECKPASSAGENUMBER");
				long COMMENTNUMBER = rs.getLong("COMMENTNUMBER");
				long FINISHORDERNUMBER = rs.getLong("FINISHORDERNUMBER");
				long LIKENUMBER = rs.getLong("LIKENUMBER");
				long LOOKNUMBER = rs.getLong("LOOKNUMBER");
				long ORDERNUMBER = rs.getLong("ORDERNUMBER");
				long PASSAGENUMBER = rs.getLong("PASSAGENUMBER");
				long REFUSEPASSAGENUMBER = rs.getLong("REFUSEPASSAGENUMBER");
				String updateTeacherSql = "update teacher set teacher.CHECKPASSAGENUMBER=" + CHECKPASSAGENUMBER
						+ ",teacher.COMMENTNUMBER=" + COMMENTNUMBER + ",teacher.FINISHORDERNUMBER=" + FINISHORDERNUMBER
						+ ",teacher.LIKENUMBER=" + LIKENUMBER + ",teacher.LOOKNUMBER=" + LOOKNUMBER
						+ ",teacher.ORDERNUMBER=" + ORDERNUMBER + ",teacher.PASSAGENUMBER=" + PASSAGENUMBER
						+ ",teacher.REFUSEPASSAGENUMBER=" + REFUSEPASSAGENUMBER + " where teacher.TEACHER_ID="
						+ TEACHER_ID + ";";

				System.out.println(updateTeacherSql);
				int r = statement.executeUpdate(updateTeacherSql);
				// int r = statement.executeUpdate(updateSql);
				System.out.println(TEACHER_ID + ":" + r);
			}
			// conn2.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void transferTeacher() {
		String urlNew = "jdbc:mysql://yiyingli.mysql.rds.aliyuncs.com:3306/yiyingli2_1use?user=sdll18&password=ll1992917&useUnicode=true&characterEncoding=UTF8";
		Connection conn = null;
		Connection conn2 = null;
		String url = "jdbc:mysql://yiyingli.mysql.rds.aliyuncs.com:3306/yiyingli?user=sdll18&password=ll1992917&useUnicode=true&characterEncoding=UTF8";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url);
			conn2 = DriverManager.getConnection(urlNew);
			Statement statement = conn2.createStatement();
			Statement stmt = conn.createStatement();
			String sql = "select * from teacher;";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				long TEACHER_ID = rs.getLong("TEACHER_ID");
				String ADDRESS = rs.getString("ADDRESS");
				String ALIPAY = rs.getString("ALIPAY");
				String BGURL = rs.getString("BGURL");
				short CHECKDEGREESTATE = rs.getShort("CHECKDEGREESTATE");
				boolean CHECKEMAIL = rs.getBoolean("CHECKEMAIL");
				short CHECKIDCARDSTATE = rs.getShort("CHECKIDCARDSTATE");
				boolean CHECKPHONE = rs.getBoolean("CHECKPHONE");
				short CHECKWORKSTATE = rs.getShort("CHECKWORKSTATE");
				long COMMENTNUMBER = rs.getLong("COMMENTNUMBER");
				String CREATETIME = rs.getString("CREATETIME");
				String EMAIL = rs.getString("EMAIL");
				String FIRSTIDENTITY = rs.getString("FIRSTIDENTITY");
				String FORSEARCH = rs.getString("FORSEARCH");
				String ICONURL = rs.getString("ICONURL");
				String IMAGEURL = rs.getString("IMAGEURL");
				String INTRODUCE = rs.getString("INTRODUCE");
				INTRODUCE = replaceBlank(INTRODUCE);
				short LEVEL = rs.getShort("LEVEL");
				long LIKENUMBER = rs.getLong("LIKENUMBER");
				String NAME = rs.getString("NAME");
				String PHONE = rs.getString("PHONE");
				short SEX = rs.getShort("SEX");
				String TALKWAY = rs.getString("TALKWAY");
				long TIPMARK = rs.getLong("TIPMARK");
				long USER_ID = rs.getLong("USER_ID");
				boolean ONSERVICE = rs.getBoolean("ONSERVICE");
				long ORDERNUMBER = rs.getLong("ORDERNUMBER");
				String SIMPLEINFO = rs.getString("SIMPLEINFO");
				int SHOWWEIGHT1 = rs.getInt("SHOWWEIGHT1");
				int SHOWWEIGHT2 = rs.getInt("SHOWWEIGHT2");
				int SHOWWEIGHT4 = rs.getInt("SHOWWEIGHT4");
				int SHOWWEIGHT8 = rs.getInt("SHOWWEIGHT8");
				int SHOWWEIGHT16 = rs.getInt("SHOWWEIGHT16");
				long LOOKNUMBER = rs.getLong("LOOKNUMBER");
				String USERNAME = rs.getString("USERNAME");
				int HOMEWEIGHT = rs.getInt("HOMEWEIGHT");
				int SALEWEIGHT = rs.getInt("SALEWEIGHT");
				long PASSAGENUMBER = rs.getLong("PASSAGENUMBER");
				long REFUSEPASSAGENUMBER = rs.getLong("REFUSEPASSAGENUMBER");
				long CHECKPASSAGENUMBER = rs.getLong("CHECKPASSAGENUMBER");
				long FINISHORDERNUMBER = rs.getLong("FINISHORDERNUMBER");
				long MASKNUMBER = rs.getLong("MASKNUMBER");
				long MASKFINISHNUMBER = rs.getLong("MASKFINISHNUMBER");
				long MILE = rs.getLong("MILE");
				String PAYPAL = rs.getString("PAYPAL");
				long REWARDNUMBER = rs.getLong("REWARDNUMBER");

				Connection connection = DriverManager.getConnection(url);
				Statement statement2 = connection.createStatement();
				ResultSet rs2 = statement2
						.executeQuery("select TITLE,PRICETOTAL from tservice where TEACHER_ID=" + TEACHER_ID);
				String TOPIC = "";
				float PRICE = 0F;
				while (rs2.next()) {
					TOPIC = rs2.getString("TITLE");
					PRICE = rs2.getFloat("PRICETOTAL");
				}
				connection.close();

				String insertTeacherSql = "insert into teacher values('" + TEACHER_ID + "','" + ADDRESS + "','" + ALIPAY
						+ "','" + 0 + "','" + 0 + "','" + BGURL + "','" + CHECKDEGREESTATE + "','" + CHECKEMAIL + "','"
						+ CHECKIDCARDSTATE + "','" + CHECKPASSAGENUMBER + "','" + CHECKPHONE + "','" + CHECKWORKSTATE
						+ "','" + COMMENTNUMBER + "','" + CREATETIME + "','" + EMAIL + "','" + FINISHORDERNUMBER + "','"
						+ FIRSTIDENTITY + "','" + HOMEWEIGHT + "','" + ICONURL + "','" + INTRODUCE + "','" + LEVEL
						+ "','" + LIKENUMBER + "','" + LOOKNUMBER + "','" + MILE + "','" + NAME + "','" + ONSERVICE
						+ "','" + ORDERNUMBER + "','" + PASSAGENUMBER + "','" + PAYPAL + "','" + PHONE + "','" + 0
						+ "','" + PRICE + "','" + REFUSEPASSAGENUMBER + "','" + SALEWEIGHT + "','" + 0 + "','" + 0
						+ "','" + 0 + "','" + SEX + "','" + SHOWWEIGHT1 + "','" + SHOWWEIGHT16 + "','" + SHOWWEIGHT2
						+ "','" + SHOWWEIGHT4 + "','" + SHOWWEIGHT8 + "','" + SIMPLEINFO + "','" + TIPMARK + "','"
						+ TOPIC + "','" + USERNAME + "','" + USER_ID + "','" + REWARDNUMBER + "','" + 0 + "','" + 0
						+ "','" + MASKNUMBER + "','" + MASKFINISHNUMBER + "');";

				insertTeacherSql = changeNull(insertTeacherSql);
				// insertOrderSql = changeNull(insertOrderSql);
				System.out.println(insertTeacherSql);
				// System.out.println(insertOrderSql);
				try {
					int rol = statement.executeUpdate(insertTeacherSql);
					// int ro = statement.executeUpdate(insertOrderSql);
					// int r = statement.executeUpdate(updateSql);
					System.out.println(TEACHER_ID + ":" + TEACHER_ID + "\t" + rol);
				} catch (SQLException sqle) {
					sqle.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
				conn2.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void transferOrder() {
		String urlNew = "jdbc:mysql://yiyingli.mysql.rds.aliyuncs.com:3306/yiyingli2_1use?user=sdll18&password=ll1992917&useUnicode=true&characterEncoding=UTF8";
		Connection conn = null;
		String url = "jdbc:mysql://yiyingli.mysql.rds.aliyuncs.com:3306/yiyingli?user=sdll18&password=ll1992917&useUnicode=true&characterEncoding=UTF8";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url);
			Connection conn2 = DriverManager.getConnection(urlNew);
			Statement statement = conn2.createStatement();
			Statement stmt = conn.createStatement();
			String sql = "select * from orders;";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				long ORDER_ID = rs.getLong("ORDER_ID");
				String CREATETIME = rs.getString("CREATETIME");
				String ENDTIME = rs.getString("ENDTIME");
				float MONEY = rs.getFloat("MONEY");
				String OKTIME = rs.getString("OKTIME");
				String QUESTION = rs.getString("QUESTION");
				QUESTION = replaceBlank(QUESTION);
				String REFUSEREASON = rs.getString("REFUSEREASON");
				REFUSEREASON = replaceBlank(REFUSEREASON);
				String SELECTTIME = rs.getString("SELECTTIME");
				SELECTTIME = replaceBlank(SELECTTIME);
				String SERVICETITLE = rs.getString("SERVICETITLE");
				String STATE = rs.getString("STATE");
				float TIME = rs.getFloat("TIME");
				String USERINTRODUCE = rs.getString("USERINTRODUCE");
				USERINTRODUCE = replaceBlank(USERINTRODUCE);
				long USER_ID = rs.getLong("USER_ID");
				long TSERVICE_ID = rs.getLong("TSERVICE_ID");
				long TEACHER_ID = rs.getLong("TEACHER_ID");
				String ORDERNO = rs.getString("ORDERNO");
				String ALIPAYNO = rs.getString("ALIPAYNO");
				String UNIQUENO = rs.getString("UNIQUENO");
				String CUSTOMERNAME = rs.getString("CUSTOMERNAME");
				CUSTOMERNAME = replaceBlank(CUSTOMERNAME);
				String CUSTOMERCONTACT = rs.getString("CUSTOMERCONTACT");
				CUSTOMERCONTACT = replaceBlank(CUSTOMERCONTACT);
				String CUSTOMERPHONE = rs.getString("CUSTOMERPHONE");
				String CUSTOMEREMAIL = rs.getString("CUSTOMEREMAIL");
				CUSTOMEREMAIL = replaceBlank(CUSTOMEREMAIL);
				short SALARYSTATE = rs.getShort("SALARYSTATE");
				float ORIGINMONEY = rs.getFloat("ORIGINMONEY");
				long DISTRIBUTOR_ID = rs.getLong("DISTRIBUTOR_ID");
				String PAYTIME = rs.getString("PAYTIME");
				boolean ONSALE = rs.getBoolean("ONSALE");
				short PAYMETHOD = rs.getShort("PAYMETHOD");
				String PAYPALNO = rs.getString("PAYPALNO");
				String REMARK = rs.getString("REMARK");
				boolean RETURNVISIT = rs.getBoolean("RETURNVISIT");

				Connection connection = DriverManager.getConnection(url);
				Statement statement2 = connection.createStatement();
				ResultSet rs2 = statement2
						.executeQuery("select ICONURL,SIMPLEINFO from teacher where TEACHER_ID=" + TEACHER_ID);
				String ICONURL = "";
				String SIMPLEINFO = "";
				while (rs2.next()) {
					ICONURL = rs2.getString("ICONURL");
					SIMPLEINFO = rs2.getString("SIMPLEINFO");
				}
				connection.close();

				String insertOrderListSql = "insert into orderlist values('" + ORDER_ID + "','" + CREATETIME + "','"
						+ CUSTOMEREMAIL + "','" + CUSTOMERNAME + "','" + CUSTOMERPHONE + "','" + CUSTOMERCONTACT + "','"
						+ MONEY + "','" + ORDERNO + "','" + ORIGINMONEY + "','" + MONEY + "','" + PAYTIME + "','"
						+ STATE + "','" + TEACHER_ID + "','" + USER_ID + "');";
				String insertOrderSql = "insert into orders values('" + ORDER_ID + "','" + ALIPAYNO + "','" + 1 + "','"
						+ CREATETIME + "','" + CUSTOMERCONTACT + "','" + CUSTOMEREMAIL + "','" + CUSTOMERNAME + "','"
						+ CUSTOMERPHONE + "','" + ENDTIME + "','" + MONEY + "','" + TIME + "','" + OKTIME + "',"
						+ ONSALE + ",'" + ORDERNO + "','" + ORIGINMONEY + "','" + PAYMETHOD + "','" + PAYTIME + "','"
						+ PAYPALNO + "','" + "小时" + "','" + QUESTION + "','" + REFUSEREASON + "','" + REMARK + "',"
						+ RETURNVISIT + ",'" + SALARYSTATE + "','" + SELECTTIME + "'," + null + ",'" + SERVICETITLE
						+ "','" + STATE + "','" + UNIQUENO + "','" + USERINTRODUCE + "','" + USER_ID + "',"
						+ (DISTRIBUTOR_ID == 0 ? null : DISTRIBUTOR_ID) + ",'" + ORDER_ID + "','" + TEACHER_ID + "','"
						+ 1 + "','" + ICONURL + "','" + MONEY + "','" + ORIGINMONEY + "','" + SIMPLEINFO + "'," + null
						+ ");";
				insertOrderListSql = changeNull(insertOrderListSql);
				insertOrderSql = changeNull(insertOrderSql);
				System.out.println(insertOrderListSql);
				System.out.println(insertOrderSql);
				int rol = statement.executeUpdate(insertOrderListSql);
				int ro = statement.executeUpdate(insertOrderSql);
				// int r = statement.executeUpdate(updateSql);
				System.out.println(ORDERNO + ":" + rol + "\t" + ro);
			}
			// conn2.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private static void insertTeacher(long teacherId) {
		JSONObject toOld = new JSONObject();
		toOld.put("style", "manager");
		toOld.put("method", "getTeacherAllInfo");
		toOld.put("mid", "ac367fc7-80dd-4c26-9f69-b786d85dc92e");
		toOld.put("teacherId", teacherId + "");
		String result = send("http://service.1yingli.cn/yiyingliService/manage", toOld.toString());
		JSONObject jsonTeacher = JSONObject.fromObject(result);
		if (jsonTeacher.getString("state").equals("error")) {
			setIncrement(teacherId + 1);
			return;
		}
		jsonTeacher.put("topic", jsonTeacher.getString("serviceTitle"));
		jsonTeacher.put("checkStudy", jsonTeacher.getString("checkDegree"));
		String username = getUserName(teacherId);
		System.out.println(username);
		JSONObject toNew = new JSONObject();
		toNew.put("style", "manager");
		toNew.put("method", "createTeacher");
		toNew.put("mid", "ac367fc7-80dd-4c26-9f69-b786d85dc92e");
		toNew.put("teacher", jsonTeacher);
		toNew.put("username", username);
		String reString = send("http://test21.1yingli.cn/yiyingliService/manage", toNew.toString());
		JSONObject newResult = JSONObject.fromObject(reString);
		if (newResult.getString("state").equals("error")) {
			failList.add(teacherId + "");
		}
	}

	private static void setIncrement(long start) {
		String urlNew = "jdbc:mysql://yiyingli.mysql.rds.aliyuncs.com:3306/yiyingli2_1?user=sdll18&password=ll1992917&useUnicode=true&characterEncoding=UTF8";
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(urlNew);
			Statement stmt = conn.createStatement();
			String sql = "alter table teacher AUTO_INCREMENT=" + start + ";";
			int r = stmt.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private static void correctTeacherId() {
		String urlNew = "jdbc:mysql://yiyingli.mysql.rds.aliyuncs.com:3306/yiyingli2_1?user=sdll18&password=ll1992917&useUnicode=true&characterEncoding=UTF8";
		Connection conn = null;
		String url = "jdbc:mysql://yiyingli.mysql.rds.aliyuncs.com:3306/yiyingli?user=sdll18&password=ll1992917&useUnicode=true&characterEncoding=UTF8";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url);
			Connection conn2 = DriverManager.getConnection(urlNew);
			Statement statement = conn2.createStatement();
			Statement stmt = conn.createStatement();
			String sql = "select teacher.TEACHER_ID,teacher.USERNAME from teacher order by teacher.TEACHER_ID DESC;";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String TEACHER_ID = rs.getString("TEACHER_ID");
				String USERNAME = rs.getString("USERNAME");
				String updateSql = "update teacher set teacher.TEACHER_ID=" + TEACHER_ID + " where teacher.USERNAME='"
						+ USERNAME + "';";
				int r = statement.executeUpdate(updateSql);
				System.out.println(USERNAME + ":" + r);
			}
			conn2.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static String getUserName(long teacherId) {
		String sql;
		Connection conn = null;
		String url = "jdbc:mysql://yiyingli.mysql.rds.aliyuncs.com:3306/yiyingli?user=sdll18&password=ll1992917&useUnicode=true&characterEncoding=UTF8";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url);
			Statement stmt = conn.createStatement();
			sql = "select teacher.USERNAME from teacher where teacher.TEACHER_ID=" + teacherId + ";";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				return rs.getString("USERNAME");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	// public static void main(String[] args) {
	// String sql;
	// Connection conn = null;
	// String url =
	// "jdbc:mysql://yiyingli.mysql.rds.aliyuncs.com:3306/yiyingli?user=sdll18&password=ll1992917&useUnicode=true&characterEncoding=UTF8";
	// try {
	// Class.forName("com.mysql.jdbc.Driver");
	// conn = DriverManager.getConnection(url);
	// Statement stmt = conn.createStatement();
	// sql = "select * from teacher;";
	// ResultSet rs = stmt.executeQuery(sql);
	// while (rs.next()) {
	// String USER_ID = rs.getString("USER_ID");
	// String TEACHER_ID = rs.getString("TEACHER_ID");
	// String CREATETIME = rs.getString("CREATETIME");
	// String ADDRESS = rs.getString("ADDRESS");
	// String ALIPAY = rs.getString("ALIPAY");
	// String BGURL = rs.getString("BGURL");
	// String CHECKDEGREESTATE = rs.getString("CHECKDEGREESTATE");
	// String CHECKEMAIL = rs.getString("CHECKEMAIL");
	// String CHECKIDCARDSTATE = rs.getString("CHECKIDCARDSTATE");
	// String CHECKPHONE = rs.getString("CHECKPHONE");
	// String CHECKWORKSTATE = rs.getString("CHECKWORKSTATE");
	// String COMMENTNUMBER = rs.getString("COMMENTNUMBER");
	// String EMAIL = rs.getString("EMAIL");
	// String FIRSTIDENTITY = rs.getString("FIRSTIDENTITY");
	// //String address = rs.getString("FORSEARCH");
	// //String IMAGEURL = rs.getString("IMAGEURL");
	// String ICONURL = rs.getString("ICONURL");
	// String INTRODUCE = rs.getString("INTRODUCE");
	// String LEVEL = rs.getString("LEVEL");
	// String LIKENUMBER = rs.getString("LIKENUMBER");
	// String NAME = rs.getString("NAME");
	// String PHONE = rs.getString("PHONE");
	// String SEX = rs.getString("SEX");
	// String TALKWAY = rs.getString("TALKWAY");
	// String TIPMARK = rs.getString("TIPMARK");
	// String ONSERVICE = rs.getString("ONSERVICE");
	// String ORDERNUMBER = rs.getString("ORDERNUMBER");
	// String SIMPLEINFO = rs.getString("SIMPLEINFO");
	// String SHOWWEIGHT1 = rs.getString("SHOWWEIGHT1");
	// String SHOWWEIGHT2 = rs.getString("SHOWWEIGHT2");
	// String SHOWWEIGHT4 = rs.getString("SHOWWEIGHT4");
	// String SHOWWEIGHT8 = rs.getString("SHOWWEIGHT8");
	// String SHOWWEIGHT16 = rs.getString("SHOWWEIGHT16");
	// String LOOKNUMBER = rs.getString("LOOKNUMBER");
	// String USERNAME = rs.getString("USERNAME");
	// String HOMEWEIGHT = rs.getString("HOMEWEIGHT");
	// String SALEWEIGHT = rs.getString("SALEWEIGHT");
	// String PASSAGENUMBER = rs.getString("PASSAGENUMBER");
	// String REFUSEPASSAGENUMBER = rs.getString("REFUSEPASSAGENUMBER");
	// String CHECKPASSAGENUMBER = rs.getString("CHECKPASSAGENUMBER");
	// String FINISHORDERNUMBER = rs.getString("FINISHORDERNUMBER");
	// String MILE = rs.getString("MILE");
	// String PAYPAL = rs.getString("PAYPAL");
	// String insertSql = "insert into teacher() values();"
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// conn.close();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// }
	// }
}
