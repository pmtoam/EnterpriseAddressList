package com.dooioo.eal.util;

import android.content.Context;
import android.content.SharedPreferences;

public class CommonUtil
{
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
		return sp.getFloat("y", 0);
	}
}
