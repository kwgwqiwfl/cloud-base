package com.ring.welkin.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Base64.Decoder;

public class ImgUtils {

	/**
	 * 服务器图片转换base64 path：服务器图片路径 返回 base64编码（String 类型）
	 *
	 * @param path
	 * @return
	 */
	public static String imgToBase64(String path) {
		InputStream in = null;
		try {
			URL url = new URL(path);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			in = connection.getInputStream();

			return imgToBase64(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String imgToBase64(InputStream in) {
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int len = 0;
			while ((len = in.read(b)) != -1) {
				out.write(b, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.getStackTrace();
			}
		}
		return Base64.getEncoder()
					 .encodeToString(out.toByteArray());
	}

	public static String imgToBase64(ByteArrayOutputStream out) {
		return Base64.getEncoder()
					 .encodeToString(out.toByteArray());
	}

	/**
	 * base64 编码转换为图片， base：base64编码 字符串 path：转换完之后的图片存储地址
	 *
	 * @param base
	 * @param path
	 * @return
	 */
	public static boolean base64ToImg(String base, String path) {
		if (StringUtils.isBlank(base)) {
			return false;
		}
		Decoder decoder = Base64.getDecoder();
		OutputStream out = null;
		try {
			byte[] bytes = decoder.decode(base);
			for (int i = 0; i < bytes.length; ++i) {
				if (bytes[i] < 0) {
					bytes[i] += 256;
				}
			}
			out = new FileOutputStream(path);
			out.write(bytes);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.getStackTrace();
			}
		}
		return true;
	}

}
