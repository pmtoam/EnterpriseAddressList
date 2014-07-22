package com.dooioo.eal.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.dooioo.eal.network.NetWorkConn;
import com.dooioo.eal.util.CommonUtil;
import com.dooioo.eal.util.Logger;
import com.dooioo.eal.util.NetWorkUtil;
import com.dooioo.enterprise.address.list.R;

public class CoreService extends Service
{

	private final String TAG = "CoreService";

	private Context context = this;
	private BroadcastReceiver mReceiver;
	private String screen_status = Intent.ACTION_SCREEN_ON;
	private Handler mHandler;
	private Runnable mRunnable;
	private final long delayMillis = 5000;
	private final long updateCycle = 1000 * 60 * 60;
	private final String downloadUrl = "http://app.dooioo.com/static/software/addressbook/AddressBook_v4_10.apk";
	
	private LayoutInflater inflater;
	private WindowManager windowManager;
	private View view;

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

//				if (CommonUtil.isDownloading(context))
//				{
//					Logger.e(TAG, "--> 有下载中的线程任务。");
//					return;
//				}

				if (System.currentTimeMillis() - CommonUtil.getDownloadSuccessTime(context) < updateCycle)
				{
					Logger.e(TAG, "--> 还没到更新周期，updateCycle = " + updateCycle);
					return;
				}

				// 如果delayMillis之后screen还是off
				if (screen_status.equals(Intent.ACTION_SCREEN_OFF))
				{
					Logger.e(TAG, "--> " + delayMillis + "毫秒之后screen依然是off.");

					if (NetWorkUtil.isConnected(context))
					{
						Logger.e(TAG, "--> 网络已连接。");
						if (NetWorkUtil.isConnectedWiFi(context))
						{
							Logger.e(TAG, "--> 已连接WiFi。");
							// 1.文件方式
							// NetWorkConn.downloadFile(downloadUrl, context);
							
							// 2.API方式
							NetWorkConn.downloadFile(context);
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

		inflater = LayoutInflater.from(getApplicationContext());
		windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		
		TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		tm.listen(new MyPhoneStatusListener(), PhoneStateListener.LISTEN_CALL_STATE);
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

	class MyPhoneStatusListener extends PhoneStateListener
	{

		@Override
		public void onCallStateChanged(int state, String incomingNumber)
		{
			super.onCallStateChanged(state, incomingNumber);
			try
			{
				switch (state)
				{
				case TelephonyManager.CALL_STATE_IDLE: // 空闲状态，没有通话没有响铃
					if (view != null) 
	                    windowManager.removeView(view);
					break;
				case TelephonyManager.CALL_STATE_RINGING: // 响铃状态
					Log.e(TAG, "-->发现来电号码" + incomingNumber);
					
//					Intent intent = new Intent();
//				    intent.setAction("android.intent.action.DOOIOO_CALL_STATE_RINGING");
//				    sendBroadcast(intent);
					
					addView();
					break;
				case TelephonyManager.CALL_STATE_OFFHOOK: // 通话状态
					break;
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void addView()
	{
        view = inflater.inflate(R.layout.activity_prompt, null);
//        TextView tv_address = (TextView) view
//                .findViewById(R.id.tv_show_address);
//        TextView tv_number = (TextView) view
//                .findViewById(R.id.tv_show_number);
//        tv_address.setText(address);
        //查询该号码对应的名字 
//        String numbername = queryNumberName(incomingNumber);
//        tv_number.setText(numbername);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
         //获取偏移量 
//        int dx= sp.getInt("dx", 0);
//        int dy = sp.getInt("dy", 0);
//        System.out.println("dx =" + dx);
//        System.out.println("dy =" + dx);
        params.x= 500 +params.x ;
        params.y = 500 +params.y;
         
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        // add window bad token
        windowManager.addView(view, params);		
	}
}
