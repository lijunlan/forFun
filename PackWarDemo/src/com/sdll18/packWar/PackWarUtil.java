package com.sdll18.packWar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class PackWarUtil {

	public static void main(String[] args) {
		Map<String, String> sd = new HashMap<String, String>();
		sd.put("host", "http://www.sdll18.com");
		sd.put("hostName", "SDLL18");
		sd.put("cachePath", "/WebData/SDLL18/");
		sd.put("id", "1");
		Map<String, Map<String, String>> cd = new HashMap<String, Map<String, String>>();
		Map<String, String> tmp = new HashMap<String, String>();
		tmp.put("cancel_praise", "deletePraiseService");
		tmp.put("praise", "upPraiseService");
		cd.put("user", tmp);
		tmp = new HashMap<String, String>();
		tmp.put("get_comment_list", "downCommentService");
		cd.put("comment", tmp);
		File original = new File(
				"/Users/SDLL18/Desktop/packTest/AppFactoryService");
		File output = new File("/Users/SDLL18/Desktop/newPack/SDLL18");
		pack("SDLL18", original, output, sd, cd);
	}

	public static void command(File output, File original, String appName)
			throws IOException {
		// String[] commands = new String[] { "cd", original.getAbsolutePath(),
		// ";",
		// "jar", "-cvf", output.getAbsolutePath(), "./" };
		Runtime.getRuntime().exec(
				"/bin/sh /Users/SDLL18/Desktop/newPack/pack.sh");
		File file = new File("/Users/SDLL18/Desktop/newPack/sql.sh");
		replaceString(file, "NAME", appName);
		Runtime.getRuntime().exec(
				"/bin/sh /Users/SDLL18/Desktop/newPack/sql.sh");
	}

	public static void pack(String appName, File original, File output,
			Map<String, String> settingData,
			Map<String, Map<String, String>> configData) {
		if (output.getParentFile() != null && !output.getParentFile().exists()) {
			output.getParentFile().mkdirs();
		}
		try {
			FileUtil.copyDirectiory(original.getAbsolutePath(),
					output.getAbsolutePath());
			File config = new File(output.getAbsolutePath()
					+ "/WEB-INF/classes/config.xml");
			File hibernate = new File(output.getAbsolutePath()
					+ "/WEB-INF/classes/hibernate.cfg.xml");
			File js = new File(output.getAbsolutePath()
					+ "/appManager/functional_js/upPassage.js");
			File web = new File(output.getAbsolutePath() + "/WEB-INF/web.xml");
			WriteXmlUtil.writeXml(config, configData, settingData);
			replaceString(hibernate, "appfactory", appName);
			replaceString(hibernate, "update", "create");
			replaceAllString(js, "AppFactoryService", appName);
			replaceString(web, "AppFactoryService", appName);
			command(new File(output.getParentFile().getAbsolutePath() + "/"
					+ appName + ".war"), output, appName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void replaceAllString(File file, String oldString,
			String newString) {
		FileReader fr = null;
		BufferedReader br = null;
		StringBuffer sb = new StringBuffer();
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String temp;
			while ((temp = br.readLine()) != null) {
				sb.append(temp + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String ns = sb.toString().replaceAll(oldString, newString);
		FileWriter fw = null;
		PrintWriter pw = null;
		try {
			fw = new FileWriter(file);
			pw = new PrintWriter(fw);
			pw.write(ns);
			pw.flush();
			fw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pw != null)
					pw.close();
				if (fw != null)
					fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static void replaceString(File file, String oldString,
			String newString) {

		FileReader fr = null;
		BufferedReader br = null;
		StringBuffer sb = new StringBuffer();
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String temp;
			while ((temp = br.readLine()) != null) {
				sb.append(temp + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		int start = sb.indexOf(oldString);
		int end = start + (oldString).length();
		if (start != -1) {
			sb.replace(start, end, newString);
			FileWriter fw = null;
			PrintWriter pw = null;
			try {
				fw = new FileWriter(file);
				pw = new PrintWriter(fw);
				pw.write(sb.toString());
				pw.flush();
				fw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (pw != null)
						pw.close();
					if (fw != null)
						fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
