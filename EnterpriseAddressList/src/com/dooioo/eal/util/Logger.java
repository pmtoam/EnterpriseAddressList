package com.dooioo.eal.util;

import android.util.Log;

public class Logger
{
	/**
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void e(String tag, String msg)
	{
		Log.e(tag, "#####---> " + msg);
	}

}