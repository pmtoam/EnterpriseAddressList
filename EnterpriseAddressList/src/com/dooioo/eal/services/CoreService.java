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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.dooioo.eal.activity.MyApplication;
import com.dooioo.eal.activity.MyFloatView;
import com.dooioo.eal.dao.tools.EmployeeDBTool;
import com.dooioo.eal.entity.Employee;
import com.dooioo.eal.network.NetWorkConn;
import com.dooioo.eal.util.Algorithm;
import com.dooioo.eal.util.CommonUtil;
import com.dooioo.eal.util.DeviceInfoUtil;
import com.dooioo.eal.util.Logger;
import com.dooioo.eal.util.NetWorkUtil;
import com.dooioo.eal.util.TextUtil;
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
	private final long updateCycle = 1000 * 60 * 60 * 24;// 1000 * 60 * 60 * 12
	private final String downloadUrl = "http://app.dooioo.com/static/software/addressbook/AddressBook_v4_10.apk";
	private static final String action_new_out_call = "com.dooioo.phone.intent.NEW_OUTGOING_CALL";

	private LayoutInflater inflater;
	private WindowManager mWindowManager;

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

				// if (CommonUtil.isDownloading(context))
				// {
				// Logger.e(TAG, "--> Download the threads in the task.");
				// return;
				// }

				if (System.currentTimeMillis()
						- CommonUtil.getDownloadSuccessTime(context) < updateCycle)
				{
					Logger.e(TAG, "--> Not to update cycle£¬updateCycle = "
							+ updateCycle);
					return;
				}

				if (screen_status.equals(Intent.ACTION_SCREEN_OFF))
				{
					Logger.e(TAG, "--> After " + delayMillis
							+ "MS, screen is still off.");

					if (NetWorkUtil.isConnected(context))
					{
						Logger.e(TAG, "--> The network is connected.");

						if (NetWorkUtil.isConnectedWiFi(context))
						{
							Logger.e(TAG, "--> Successfully connected to WiFi.");
							// 1. download file way
							// NetWorkConn.downloadFile(downloadUrl, context);

							// 2.API way
							NetWorkConn.downloadFile(context);
						}
						else
						{
							Logger.e(TAG, "--> Network type is not WiFi.");

							if (NetWorkUtil.isAllowedNonWiFiNetwork(context))
							{
								Logger.e(TAG,
										"--> Allowed types of non WiFi network.");

								if (CommonUtil.isMoreThan7Days(context))
								{
									Logger.e(TAG,
											"--> Not connected to the WiFi to update the data for more than 7 days.");
									NetWorkConn.downloadFile(context);
								}
								else
								{
									Logger.e(TAG,
											"--> Do not use WiFi to update the data of not more than 7 days.");
								}
							}
							else
							{
								Logger.e(TAG,
										"--> Not allowed types of non WiFi network.");
							}
						}
					}
					else
					{
						Logger.e(TAG, "--> The network is not available.");
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
					// Logger.e(TAG, "--> Non custom machine.");
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
				else if (Intent.ACTION_NEW_OUTGOING_CALL.equals(intent
						.getAction()))
				{
					Logger.e(TAG, "--> normal mobile phone outgoing call");

				}
				else if (action_new_out_call.equals(intent.getAction()))
				{
					// customization mobile phone
					Logger.e(TAG,
							"--> customization mobile phone outgoing call");

				}

			}
		};

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
		intentFilter.addAction(Intent.ACTION_SCREEN_ON);
		intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
		intentFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
		intentFilter.addAction(action_new_out_call);
		registerReceiver(mReceiver, intentFilter);

		inflater = LayoutInflater.from(getApplicationContext());
		mWindowManager = (WindowManager) getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE);

		TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		tm.listen(new MyPhoneStatusListener(),
				PhoneStateListener.LISTEN_CALL_STATE);
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
		public void onCallStateChanged(int state, final String incomingNumber)
		{
			super.onCallStateChanged(state, incomingNumber);
			try
			{
				switch (state)
				{
				case TelephonyManager.CALL_STATE_IDLE:
					if (myFloatView != null)
					{
						mWindowManager.removeView(myFloatView);
						myFloatView = null;
					}
					break;
				case TelephonyManager.CALL_STATE_RINGING:
					Log.e(TAG, "--> incomingNumber = " + incomingNumber);
					Log.e(TAG,
							"--> decryption incomingNumber = "
									+ Algorithm.decryption(incomingNumber));

					// Intent intent = new Intent();
					// intent.setAction("android.intent.action.DOOIOO_CALL_STATE_RINGING");
					// sendBroadcast(intent);
					Employee employee = null;
					if (DeviceInfoUtil.isSpecial())
					{
						employee = EmployeeDBTool.queryEmployee(context,
								Algorithm.decryption(incomingNumber));
					}
					else
					{
						employee = EmployeeDBTool.queryEmployee(context,
								incomingNumber);
					}

					if (null != employee)
					{
						addView(employee);
					}
					else
					{
						if (DeviceInfoUtil.isSpecial())
						{
							addView(new Employee("", "",
									Algorithm.decryption(incomingNumber), ""));
						}
						else
						{
							addView(new Employee("", "", incomingNumber, ""));
						}
					}

					break;
				case TelephonyManager.CALL_STATE_OFFHOOK:
					if (myFloatView != null)
					{
						mWindowManager.removeView(myFloatView);
						myFloatView = null;
					}
					break;
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	private WindowManager.LayoutParams wmParams;
	private MyFloatView myFloatView;

	public void addView(Employee employee)
	{
		myFloatView = new MyFloatView(getApplicationContext());

		TextView tv_name = (TextView) myFloatView.findViewById(R.id.tv_name);
		tv_name.setText(employee.userNameCn);
		TextView tv_phone_num = (TextView) myFloatView
				.findViewById(R.id.tv_phone_num);

		if (DeviceInfoUtil.isSpecial())
			tv_phone_num.setText(TextUtil.replaceFixedPos(employee.mobilePhone,
					5, 8, "****"));
		else
			tv_phone_num.setText(employee.mobilePhone);

		TextView tv_org = (TextView) myFloatView.findViewById(R.id.tv_org);
		tv_org.setText(employee.orgName);
		TextView tv_title = (TextView) myFloatView.findViewById(R.id.tv_title);
		tv_title.setText(employee.userTitle);

		mWindowManager = (WindowManager) getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE);
		wmParams = ((MyApplication) getApplication()).getMywmParams();

		if (DeviceInfoUtil.isSpecial())
		{
			wmParams.type = LayoutParams.TYPE_PHONE
					| LayoutParams.TYPE_SYSTEM_OVERLAY;
		}
		else
		{
			wmParams.type = LayoutParams.TYPE_PHONE;
		}
		wmParams.format = PixelFormat.RGBA_8888;
		wmParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
				| LayoutParams.FLAG_NOT_FOCUSABLE;
		wmParams.gravity = Gravity.LEFT | Gravity.TOP;
		wmParams.x = (int) CommonUtil.getWindowX(context);
		wmParams.y = (int) CommonUtil.getWindowY(context);
		wmParams.width = LayoutParams.WRAP_CONTENT;
		wmParams.height = LayoutParams.WRAP_CONTENT;

		mWindowManager.addView(myFloatView, wmParams);
	}
}
