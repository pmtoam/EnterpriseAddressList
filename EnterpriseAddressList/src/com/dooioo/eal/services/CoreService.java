package com.dooioo.eal.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;

import com.dooioo.eal.network.NetWorkConn;
import com.dooioo.eal.util.CommonUtil;
import com.dooioo.eal.util.Logger;
import com.dooioo.eal.util.NetWorkUtil;

public class CoreService extends Service
{

	private final String TAG = "CoreService";

	private Context context = this;
	private BroadcastReceiver mReceiver;
	private String screen_status = Intent.ACTION_SCREEN_ON;
	private Handler mHandler;
	private Runnable mRunnable;
	private final long delayMillis = 5000;
	private final long updateCycle = 1000 * 60;
	private final String downloadUrl = "http://app.dooioo.com/static/software/addressbook/AddressBook_v4_10.apk";

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		Logger.e(TAG, "--> onCreate()");

		mHandler = new Handler();

		mRunnable = new Runnable()
		{

			@Override
			public void run()
			{

				if (CommonUtil.isDownloading(context))
				{
					Logger.e(TAG, "--> 有下载中的线程任务。");
					return;
				}

				long currentTime = System.currentTimeMillis();
				if (currentTime - CommonUtil.getDownloadSuccessTime(context) < updateCycle)
				{
					Logger.e(TAG, "--> 还没到更新周期，updateCycle = " + updateCycle);
					return;
				}

				// 如果delayMillis之后screen还是off
				if (screen_status.equals(Intent.ACTION_SCREEN_OFF))
				{
					Logger.e(TAG,
							"--> delayMillis之后screen还是off, delayMillis = "
									+ delayMillis);

					if (NetWorkUtil.isConnected(context))
					{
						Logger.e(TAG, "--> 网络已连接。");
						if (NetWorkUtil.isConnectedWiFi(context))
						{
							Logger.e(TAG, "--> 已连接WiFi。");
							NetWorkConn.downloadFile(downloadUrl, context);
						}
						else
						{
							Logger.e(TAG, "--> 网络类型不是WiFi。");
						}
					}
					else
					{
						Logger.e(TAG, "--> 网络不可用。");
					}

				}
			}
		};

		mReceiver = new BroadcastReceiver()
		{

			@Override
			public void onReceive(final Context context, Intent intent)
			{
				if (Intent.ACTION_SCREEN_ON.equals(intent.getAction()))
				{
					Logger.e(TAG, "--> action_screen_on");
					screen_status = Intent.ACTION_SCREEN_ON;
					Logger.e(TAG, "--> mHandler.removeCallbacks()");
					mHandler.removeCallbacks(mRunnable);
				}
				else if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction()))
				{
					Logger.e(TAG, "--> action_screen_off");
					screen_status = Intent.ACTION_SCREEN_OFF;

					// if (!DeviceInfoUtil.isSpecial())
					// {
					// Logger.e(TAG, "--> 非定制机。");
					// return;
					// }

					Logger.e(TAG, "--> mHandler.postDelayed()");
					mHandler.postDelayed(mRunnable, delayMillis);
				}
				else if (Intent.ACTION_BATTERY_CHANGED.equals(intent
						.getAction()))
				{
					Logger.e(TAG, "--> action_battery_changed");
					startService(new Intent(context, CoreService.class));
				}

			}
		};

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
		intentFilter.addAction(Intent.ACTION_SCREEN_ON);
		intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(mReceiver, intentFilter);

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		Logger.e(TAG, "--> onStartCommand()");
		return START_STICKY;
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		Logger.e(TAG, "--> onDestroy()");

		try
		{
			unregisterReceiver(mReceiver);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		startService(new Intent(this, CoreService.class));
	}

}
