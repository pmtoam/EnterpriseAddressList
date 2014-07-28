package com.dooioo.eal.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class CommonUtil
{
	public static final String TAG = "CommonUtil";
	public static final String PREFS_CACHE_ONE = "prefs_cache_one";

	// ---------------------------------------------------------------------------
	public static void setDownloading(boolean isDownloading, Context context)
	{
		SharedPreferences sp = context.getSharedPreferences(PREFS_CACHE_ONE, 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean("isDownloading", isDownloading);
		editor.commit();
	}

	public static boolean isDownloading(Context context)
	{
		SharedPreferences sp = context.getSharedPreferences(PREFS_CACHE_ONE, 0);
		return sp.getBoolean("isDownloading", false);
	}

	// ---------------------------------------------------------------------------
	public static void setDownloadSuccessTime(long time, Context context)
	{
		SharedPreferences sp = context.getSharedPreferences(PREFS_CACHE_ONE, 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.putLong("downloadSuccessTime", time);
		editor.putLong("nonWiFiTime", time);
		editor.commit();
	}

	public static long getDownloadSuccessTime(Context context)
	{
		SharedPreferences sp = context.getSharedPreferences(PREFS_CACHE_ONE, 0);
		return sp.getLong("downloadSuccessTime", 0);
	}

	// ---------------------------------------------------------------------------
	public static void setWindowX(float x, Context context)
	{
		SharedPreferences sp = context.getSharedPreferences(PREFS_CACHE_ONE, 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.putFloat("x", x);
		editor.commit();
	}

	public static float getWindowX(Context context)
	{
		SharedPreferences sp = context.getSharedPreferences(PREFS_CACHE_ONE, 0);
		return sp.getFloat("x", 0);
	}

	// ---------------------------------------------------------------------------
	public static void setWindowY(float y, Context context)
	{
		SharedPreferences sp = context.getSharedPreferences(PREFS_CACHE_ONE, 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.putFloat("y", y);
		editor.commit();
	}

	public static float getWindowY(Context context)
	{
		SharedPreferences sp = context.getSharedPreferences(PREFS_CACHE_ONE, 0);
		return sp.getFloat("y", 260);
	}

	// ---------------------------------------------------------------------------
	public static void setNonWiFiTime(long time, Context context)
	{
		SharedPreferences sp = context.getSharedPreferences(PREFS_CACHE_ONE, 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.putLong("nonWiFiTime", time);
		editor.commit();
	}

	public static long getNonWiFiTime(Context context)
	{
		SharedPreferences sp = context.getSharedPreferences(PREFS_CACHE_ONE, 0);
		return sp.getLong("nonWiFiTime", 0);
	}

	public static boolean isMoreThan7Days(Context context)
	{
		Logger.e(TAG, "--> getNonWiFiTime(context) = "
				+ getNonWiFiTime(context));
		if (getNonWiFiTime(context) == 0)
		{
			setNonWiFiTime(System.currentTimeMillis(), context);
			return false;
		}

		// 7 * 24 * 60 * 60 * 1000
		if (System.currentTimeMillis() - getNonWiFiTime(context) > 7 * 24 * 60
				* 60 * 1000)
		{
			Logger.e(TAG, "--> ³¬¹ý7Ìì£¡£¡£¡£¡");
			setNonWiFiTime(System.currentTimeMillis(), context);
			return true;
		}

		return false;
	}

	// ---------------------------------------------------------------------------
	public static void setAccessToken(Context context, String value)
	{
		SharedPreferences sp = context.getSharedPreferences(
				CommonUtil.PREFS_CACHE_ONE, 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("token", value);
		editor.commit();
	}

	public static String getAccessToken(Context context)
	{
		SharedPreferences sp = context.getSharedPreferences(
				CommonUtil.PREFS_CACHE_ONE, 0);
		return sp.getString("token", "");
	}

	// ---------------------------------------------------------------------------
	public static void setDooiooAllResult(Context context, String value)
	{
		SharedPreferences sp = context.getSharedPreferences(
				CommonUtil.PREFS_CACHE_ONE, 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("dooiooAllResult", value);
		editor.commit();
	}

	public static String getDooiooAllResult(Context context)
	{
		SharedPreferences sp = context.getSharedPreferences(
				CommonUtil.PREFS_CACHE_ONE, 0);
		return sp.getString("dooiooAllResult", "");
	}

	// ---------------------------------------------------------------------------
	public static void setGetDooiooAllTime(long time, Context context)
	{
		SharedPreferences sp = context.getSharedPreferences(PREFS_CACHE_ONE, 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.putLong("getDooiooAllTime", time);
		editor.commit();
	}

	public static long getGetDooiooAllTime(Context context)
	{
		SharedPreferences sp = context.getSharedPreferences(PREFS_CACHE_ONE, 0);
		return sp.getLong("getDooiooAllTime", 0);
	}

	// ---------------------------------------------------------------------------
	/**
	 * 
	 * @param tag
	 *            Log tag
	 * @param e
	 *            Exception
	 * @return 1: org.apache.http.conn.ConnectTimeoutException<br>
	 *         2: org.apache.http.conn.HttpHostConnectException<br>
	 *         3: org.json.JSONException<br>
	 *         4: java.net.SocketTimeoutException<br>
	 *         5: java.net.ConnectException<br>
	 *         6: java.io.FileNotFoundException<br>
	 *         7: java.net.UnknownHostException<br>
	 *         8: java.net.SocketException<br>
	 * <br>
	 */
	public static int checkExceptionType(String tag, Exception e)
	{
		int result = -1;

		if (e instanceof org.apache.http.conn.ConnectTimeoutException)
		{
			Logger.e(TAG,
					"-->Exception Type : org.apache.http.conn.ConnectTimeoutException");
			result = 1;
		}
		else if (e instanceof org.apache.http.conn.HttpHostConnectException)
		{
			Logger.e(TAG,
					"-->Exception Type : org.apache.http.conn.HttpHostConnectException");
			result = 2;
		}
		else if (e instanceof org.json.JSONException)
		{
			Logger.e(TAG, "-->Exception Type : org.json.JSONException");
			result = 3;
		}
		else if (e instanceof java.net.SocketTimeoutException)
		{
			Logger.e(TAG, "-->Exception Type : java.net.SocketTimeoutException");
			result = 4;
		}
		else if (e instanceof java.net.ConnectException)
		{
			Logger.e(TAG, "-->Exception Type : java.net.ConnectException");
			result = 5;
		}
		else if (e instanceof java.io.FileNotFoundException)
		{
			Logger.e(TAG, "-->Exception Type : java.io.FileNotFoundException");
			result = 6;
		}
		else if (e instanceof java.net.UnknownHostException)
		{
			Logger.e(TAG, "-->Exception Type : java.net.UnknownHostException");
			result = 7;
		}
		else if (e instanceof java.net.SocketException)
		{
			Logger.e(TAG, "-->Exception Type : java.net.SocketException");
			result = 8;
		}
		return result;
	}

	public static void showToast(Context context, String text)
	{
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}
}
