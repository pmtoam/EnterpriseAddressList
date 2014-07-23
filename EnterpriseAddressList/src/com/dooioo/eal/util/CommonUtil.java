package com.dooioo.eal.util;

import android.content.Context;
import android.content.SharedPreferences;

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
		Logger.e(TAG, "--> getNonWiFiTime(context) = " + getNonWiFiTime(context));
		if (getNonWiFiTime(context) == 0)
		{
			setNonWiFiTime(System.currentTimeMillis(), context);
			return false;
		}
		
		// 7 * 24 * 60 * 60 * 1000
		if (System.currentTimeMillis() - getNonWiFiTime(context) > 7 * 24 * 60 * 60 * 1000)
		{
			Logger.e(TAG, "--> ³¬¹ý7Ìì£¡£¡£¡£¡");
			setNonWiFiTime(System.currentTimeMillis(), context);
			return true;
		}
		
		return false;
	}
}
