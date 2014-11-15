package com.tb.cigarette.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Utility {
	public static Utility utility = null;

	public static Utility getInstance() {
		if (utility == null) {
			utility = new Utility();
		}
		return utility;
	}

	/**
	 * 根据流转换字符串
	 * 
	 * @param is
	 *            输入流
	 * @return 输入流转换完毕的字符串
	 */
	public String readStream(InputStream is) {
		try {
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			int i = is.read();
			while (i != -1) {
				bo.write(i);
				i = is.read();
			}
			return bo.toString();
		} catch (IOException e) {
			return "";
		}
	}
}
