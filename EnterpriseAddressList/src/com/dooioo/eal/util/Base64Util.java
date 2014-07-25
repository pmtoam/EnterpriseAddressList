package com.dooioo.eal.util;

import android.util.Base64;

/**
 * @Title: Base64Util.java
 * @Package com.dooioo.common.string.util
 * @Description: TODO(BASE64加密和解�?
 * @author nuaaboy
 * @date 2013-6-17 上午10:11:27
 * 
 */
public class Base64Util {

	/**
	 * 加密
	 * 
	 * @param s
	 * @return
	 */
	public static String encode(String s) {
		s = StringsUtil.trim(s);

		if ("".equals(s))
			return s;
		try {
			byte[] encode = Base64.encode(s.getBytes("utf-8"), Base64.DEFAULT);
			String enc = new String(encode);

			return enc;
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}
	}

	/**
	 * 解密
	 * 
	 * @param s
	 * @return
	 */
	public static String decode(String s) {
		s = StringsUtil.trim(s);
		if ("".equals(s))
			return s;
		byte[] result = Base64.decode(s, Base64.DEFAULT);
		String res = new String(result);
		return res;
	}
}
