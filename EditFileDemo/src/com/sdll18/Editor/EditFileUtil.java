package com.sdll18.Editor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class EditFileUtil {

	public static void main(String[] arg) {
		File file = new File("/Users/SDLL18/Desktop/test.java");
		changeImport(file, "com.sdll18.test", "com.test.test.test");
	}

	public static void changeImportList(File directory) {
		if (directory.exists() && directory.isDirectory()) {
			File files[] = directory.listFiles();
			for (File f : files) {
				changeImportList(f);
			}
		}
	}

	public static void changeImport(File file, String oldPackage,
			String newPackage) {

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
		int start = sb.indexOf(oldPackage + ".R");
		int end = start + (oldPackage + ".R").length();
		if (start != -1) {
			sb.replace(start, end, newPackage + ".R");
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
