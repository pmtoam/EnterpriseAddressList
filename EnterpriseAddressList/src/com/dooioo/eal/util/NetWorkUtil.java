package com.dooioo.eal.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class NetWorkUtil
{

	public static boolean isConnectedWiFi(Context context)
	{
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI)
		{
			return true;
		}
		return false;
	}

	// public static final int NETWORK_TYPE_UNKNOWN = 0;
	// public static final int NETWORK_TYPE_GPRS = 1;
	// public static final int NETWORK_TYPE_EDGE = 2; // 移动 2G
	// public static final int NETWORK_TYPE_UMTS = 3;
	// public static final int NETWORK_TYPE_CDMA = 4;
	// public static final int NETWORK_TYPE_EVDO_0 = 5;
	// public static final int NETWORK_TYPE_EVDO_A = 6; // 电信 3G
	// public static final int NETWORK_TYPE_1xRTT = 7;
	// public static final int NETWORK_TYPE_HSDPA = 8; // 移动 3G
	// public static final int NETWORK_TYPE_HSUPA = 9;
	// public static final int NETWORK_TYPE_HSPA = 10;
	// public static final int NETWORK_TYPE_IDEN = 11;
	// public static final int NETWORK_TYPE_EVDO_B = 12;
	// public static final int NETWORK_TYPE_LTE = 13; // 移动 4G
	// public static final int NETWORK_TYPE_EHRPD = 14;
	// public static final int NETWORK_TYPE_HSPAP = 15;
	/**
	 * activeNetInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_HSDPA // 移动
	 * 3G<br>
	 * activeNetInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_LTE // 移动 4G<br>
	 * <br>
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isAllowedNonWiFiNetwork(Context context)
	{
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null)
		{
			if (activeNetInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_HSDPA
					|| activeNetInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_LTE)
			{
				return true;
			}
		}
		return false;
	}

	public static boolean isConnected(Context context)
	{
		ConnectivityManager conManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
		if (networkInfo == null)
			return false;
		else
			return true;
	}

}
