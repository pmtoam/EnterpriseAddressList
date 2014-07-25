package com.dooioo.eal.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;


/** 
 * @Title: StringsUtil.java 
 * @Package com.dooioo.common.util 
 * @Description: TODO(字符串公共格式化�? 
 * @author nuaaboy 
 * @date 2013-6-8 下午2:40:51 
 * 
 */
public class StringsUtil {
	
	public static final CharSequence PACKAGE_NAME = "com.dooioo.addressbook.";
	
	private String TAG = getClass().getName().replace(StringsUtil.PACKAGE_NAME, "");
	
	/**
	 * 去掉首尾空字符串
	 * @param s
	 * @return
	 */
	public static String trim(String s){
		if(s == null)  return "";
		return s.trim();
	}
	
	/**
	 * 判断字符串是否为�?	 * @param s
	 * @return
	 */
	public static boolean isEmpty(String s) {
		if (s == null || s.length() <= 0) 
			return true;
		if (s.trim().equals("") || s.trim().equals("null"))
			return true;
		return false;
	}
	
	/**
	 * 全角字符 - 65248 = 半角字符 全角空格�?2288，半角空格为32
	 * @param input
	 * @return
	 */
	public static String ToDBC(String input) {  
        char[] c = input.toCharArray();  
        for (int i = 0; i < c.length; i++) {  
            if (c[i] == 12288) {  
                c[i] = (char) 32;  
                continue;  
            }  
            if (c[i] > 65280 && c[i] < 65375)  
                c[i] = (char) (c[i] - 65248);  
        }  
        return new String(c);  
    }
		
	/**
	 * 将从服务器上拿到的图片路径拼接成完整的url
	 * @param oldPath
	 * @param picSize
	 * @return
	 */
	public static String concactPicPath(String oldPath, String picSize) {
		String tail = oldPath.substring(oldPath.length() - 4, oldPath.length());
		//http://img1.dooioo.com/images/2013/1114/EBFD237CC16148AAB88491896BCE09F4.JPG_600x450.JPG
		String newPath = "http://img1.dooioo.com/images/" + oldPath + "_" + picSize + tail;
		return newPath;
	}
	
	/**
	 * 隐藏真实号码
	 * @param number
	 * @return
	 */
	public static String encodeUINumber(String number) {
		if (number.length() > 7) {
			String tail = number.substring(number.length() - 3, number.length());
			number = number.substring(0, number.length() - 7) + "****" + tail;
		}
		return number;
	}
	/**
	 * this method is design for delete +86 from phone number.(young)
	 * @param cnPhoneNumber the original phone number.<br/>such as +8618616363213
	 * @return deleted +86 symbol from original number.<br/>Example:18616363213
	 */
	public static String delPhoneNumberHeadCN(String cnPhoneNumber){
		if(cnPhoneNumber == null || "".equals(cnPhoneNumber)){
			return null;
		}
		String del;
		if(cnPhoneNumber.contains("+86")){
			del = cnPhoneNumber.substring(3,cnPhoneNumber.length());
		}
		else{
			del = cnPhoneNumber;
		}
		return del;
	}

	public static String trims(String src) {
		if(TextUtils.isEmpty(src)){
			return "";
		}
		String t = src.trim();
		if (t.contains(" ")) {
			Pattern p = Pattern.compile(" ");
			Matcher m = p.matcher(t);
			t = m.replaceAll("");
		}
		return t;
	}
	public static String append2ToExtPhoneNumber(String phoneNumber){
		if(TextUtils.isEmpty(phoneNumber)){
			throw new IllegalArgumentException("Args phoneNumber is empty or null!");
		}
		String rtn = phoneNumber;
		if(rtn.indexOf(",") != -1){
			int i = rtn.indexOf("#");
			if(i != -1) {
				rtn = rtn.substring(0,i);
			}
		}
		return rtn;
	}
}