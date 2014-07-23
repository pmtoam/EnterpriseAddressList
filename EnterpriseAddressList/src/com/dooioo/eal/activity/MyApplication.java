package com.dooioo.eal.activity;

import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.WindowManager;

public class MyApplication extends Application
{

	private WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();

	public WindowManager.LayoutParams getMywmParams()
	{
		return wmParams;
	}

	private static String Imei, Imsi;

	@Override
	public void onCreate()
	{
		super.onCreate();

		TelephonyManager mTelephonyMgr = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		Imsi = mTelephonyMgr.getSubscriberId();
		Imsi = TextUtils.isEmpty(Imsi) ? "" : Imsi;
		Imei = mTelephonyMgr.getDeviceId();
	}

	public static String getImei()
	{
		return Imei;
	}

	public static String getImsi()
	{
		return Imsi;
	}
}
