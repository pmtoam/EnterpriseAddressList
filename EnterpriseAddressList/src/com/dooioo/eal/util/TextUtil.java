package com.dooioo.eal.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.telephony.SmsMessage;
import android.text.TextUtils;
/**
 * 
 * @author Young
 *
 */
public class TextUtil {
	private static String dooiooString = "ro.yulong.version.software";
	/**
	 * calculate the byte length of string(include chn,char etc.)
	 * @param str
	 * @return
	 */
	public static int lengthOfByte(String str) {
		int valueLength = 0;
		String chinese = "[\u0391-\uFFE5]";
		for (int i = 0; i < str.length(); i++) {
			String temp = str.substring(i, i + 1);
			if (temp.matches(chinese)) {
				valueLength += 2;
			} else {
				valueLength += 1;
			}
		}
		return valueLength;
	}
	/**
	 * merge long message.If the msgs is short,just return.
	 * Otherwise merge and return one string.<br/>
	 * <b>NOTE:</b><br/>If shouldCompare is true,phoneNumber <b>MUST NOT</b> be null or empty.
	 * 
	 * @param msgs ,the SmsMessage from intent.
	 * @param phoneNumber ,phone number of compare with.
	 * @param shouldCompare ,need compare flag.
	 * @return merged message
	 */
	public static String mergeLongMessage(SmsMessage[] msgs,
			String phoneNumber, boolean shouldCompare) {
		if (msgs == null) {
			return "";
		}
		if (shouldCompare && TextUtils.isEmpty(phoneNumber)) {
			throw new IllegalArgumentException(
					"Arg shouldCompare is true,but arg phoneNumber is empty!");
		}

		StringBuilder mssge = new StringBuilder();
		if (shouldCompare) {
			for (SmsMessage sm : msgs) {
				if (sm.getOriginatingAddress().equals(phoneNumber)) {
					mssge.append(sm.getDisplayMessageBody());
				}
			}
		} else {
			for (SmsMessage sm : msgs) {
				mssge.append(sm.getDisplayMessageBody());
			}
		}
		return mssge.toString();
	}
	/**
	 * check weather current device is a dooioo customized ROM or a normaL standard ROM
	 * @param context
	 * @return
	 */
	public static boolean isSpecial(Context context, String comp) {
		boolean rt = false;
		String resultString = "";
		//String cpb_version_string = SystemProperties.get("ro.yulong.version.software");
		ClassLoader cl = context.getClassLoader();
		try {
			 Class SystemProperties = cl.loadClass("android.os.SystemProperties");
			 Class[] parameters = new Class[1];
			 parameters[0] = String.class;
			 Method reflectionGet = SystemProperties.getMethod("get", parameters);
			 resultString = (String)reflectionGet.invoke(SystemProperties, dooiooString);
			 String[] local = resultString.split("-");
			 if(local.length != 2){
				 return false;
			 }
			 if(!local[1].equals("mpsg")){
				 return false;
			 }else{
				 return true;
			 }
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return rt;
		/*
		try{
			// TODO this method should overwrite when coolpad give us the interface
			rt = android.os.Build.MODEL.equals("Coolpad 8720L")?true:false;
			if(!rt){
				rt = (android.os.Build.MODEL.equals("V370")) ? true : false;
			}
		}
		catch(Exception e){
			rt = (android.os.Build.MODEL.equals("V370")) ? true : false;
		}
		return rt;
		*/
	}
	/**
	 * The method is just for compatible acer v370.
	 * @param context
	 * @return true if acer v370
	 */
	@Deprecated
	public static boolean isAcer(Context context){
		return (android.os.Build.MODEL.equals("V370")) ? true : false;
	}
	/**
	 * 
	 * @param source the source string which to cut
	 * @param start the start position of cut string
	 * @param end   the end position of cut string
	 * @param fill	the symbol of replace
	 * @return replaced string.Example:<br/>replaceFixedPos("18616363213",4,7,"****"),the result is :186****3213
	 */
	public static String replaceFixedPos(String source,int start,int end,String fill){
		String tSource = source;
		if(tSource == null || "".equals(tSource)){
			return null;
		}
		int len = tSource.length();
		if(start < 0){
			return null;
		}
		if(end > len){
			end = len;
		}
		if(start > len){
			start = len;
		}
		StringBuilder sb = new StringBuilder();
		String header = tSource.substring(0, start-1);
		sb.append(header).append(fill);
		String tail = tSource.substring(end, len);
		sb.append(tail);
		return sb.toString();
	}
	/**
	 * given a nice ext ui phone number 
	 * @param extPhoneNumber
	 * @return
	 */
	public static String replaceExtPhone2UiNumber(String extPhoneNumber){
		if(TextUtils.isEmpty(extPhoneNumber)){
			return "";
		}
		String []ps = extPhoneNumber.split(",");
		int tail = ps[1].length();
		if(tail == 3){
			return ps[0]+","+replaceFixedPos(ps[1], 2, 2, "*");
		}
		else{
			return ps[0]+","+replaceFixedPos(ps[1], 3, 6, "****");
		}
	}
	/**
	 * this method is design for delete +86 from phone number
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
	/**
	 * this method is designed for extracting fixed string from source
	 * @param regEx		the regular expression
	 * @param source	the source string which should be extract
	 * @return			the extract string
	 * Example:<br/>
	 * string:1234,4321 extractStrUseRegex("4321","1234,4321"),the result is 1234
	 */
	public static String extractStrUseRegex(String regEx, String source){
		if(TextUtils.isEmpty(regEx)){
			return "";
		}
		if(TextUtils.isEmpty(source)){
			return "";
		}
		Pattern p = Pattern.compile(regEx); 
		Matcher m = p.matcher(source);
		String t = m.replaceAll("");
		if(!TextUtils.isEmpty(t) && t.indexOf(";") > -1){
			t = extractStrUseRegex(";",t);
		}
		return t;
	}
	/**
	 * split format string to an other format
	 * @param source source format string
	 * @param divideComma source string divide comma
	 * @param wraperComma the new string which wrap with comma
	 * @return
	 */
	public static String extraStrWithComma(String source,String divideComma,String wraperComma){
		if(TextUtils.isEmpty(source)){
			return "";
		}
		String srStr[] = source.split(divideComma);
		StringBuilder sb = new StringBuilder();
		int len = srStr.length;
		for(int i = 0;i < len; i++){
			if(i != len -1){
				sb.append(wraperComma+srStr[i]+wraperComma+",");
			}
			else{
				sb.append(wraperComma+srStr[i]+wraperComma);
			}
		}
		return sb.toString();
	}
	/**
	 * get the uuid that is special
	 * @return
	 */
	public static String getUUID(){
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replaceAll("-", "");
	}
	/***
	 * delete space from one string
	 * @param src input string
	 * @return deleted string
	 * Example:"12&nbsp;&nbsp;&nbsp;34&nbsp;5&nbsp;6" convert to "123456"
	 */
	public static String trimAll(String src){
		if(TextUtils.isEmpty(src)){
			return "";
		}
		Pattern p = Pattern.compile(" ");
		Matcher m = p.matcher(src);
		String r = m.replaceAll("");
		return r;
	}
	/**
	 * add html sysmbol for digits which length is bigger than 5.
	 * @param src
	 * @param symbolStart
	 * @param symbolEnd
	 * @return
	 */
	public static String addHtmlSymbol(String src, String symbolStart, String symbolEnd) {
		Pattern p = Pattern.compile("\\d{5,}");
		Matcher m = p.matcher(src);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, symbolStart + m.group() + symbolEnd);
		}
		m.appendTail(sb);
		return sb.toString();
	}
}
