package com.sdll18.umedia.PackUtil;

import java.io.IOException;

import com.sdll18.umedia.Data.StreamGobbler;

public class PackApkUtil {

	public static final String ORIGINAL_PATH = "/UMediaData/oldApk";
	public static final String SAVE_PATH = "/UMediaData/";

	/**
	 * @param shFile
	 * @param outFile
	 * @param app_id
	 * @param appName
	 * @param hostName
	 * @param color
	 *            such as '#ff123456'
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void createApk(String shFile, String outFile, String app_id,
			String appName, String hostName, String color) throws IOException,
			InterruptedException {
		Process p = Runtime.getRuntime().exec(
				"/bin/sh " + shFile + " " + appName + " " + hostName + " "
						+ color + " " + outFile + " " + app_id);
		StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(),
				"ERROR");
		errorGobbler.start();
		StreamGobbler outGobbler = new StreamGobbler(p.getInputStream(),
				"STDOUT");
		outGobbler.start();
		p.waitFor();
	}

}
