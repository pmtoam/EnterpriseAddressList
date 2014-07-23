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
import android.view.View;
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
	private final long updateCycle = 1000 * 60 * 60 * 12;// 1000 * 60 * 60 * 12
	private final String downloadUrl = "http://app.dooioo.com/static/software/addressbook/AddressBook_v4_10.apk";

	private LayoutInflater inflater;
	private WindowManager mWindowManager;
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

				// if (CommonUtil.isDownloading(context))
				// {
				// Logger.e(TAG, "--> �������е��߳�����");
				// return;
				// }

				if (System.currentTimeMillis()
						- CommonUtil.getDownloadSuccessTime(context) < updateCycle)
				{
					Logger.e(TAG, "--> ��û���������ڣ�updateCycle = " + updateCycle);
					return;
				}

				// ���delayMillis֮��screen����off
				if (screen_status.equals(Intent.ACTION_SCREEN_OFF))
				{
					Logger.e(TAG, "--> " + delayMillis + "����֮��screen��Ȼ��off.");

					if (NetWorkUtil.isConnected(context))
					{
						Logger.e(TAG, "--> ���������ӡ�");
						if (NetWorkUtil.isConnectedWiFi(context))
						{
							Logger.e(TAG, "--> ������WiFi��");
							// 1.�ļ���ʽ
							// NetWorkConn.downloadFile(downloadUrl, context);

							// 2.API��ʽ
							NetWorkConn.downloadFile(context);
						}
						else
						{
							Logger.e(TAG, "--> �������Ͳ���WiFi��");
						}
					}
					else
					{
						Logger.e(TAG, "--> ���粻���á�");
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
					// Logger.e(TAG, "--> �Ƕ��ƻ���");
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
				case TelephonyManager.CALL_STATE_IDLE: // ����״̬��û��ͨ��û������
					if (view != null)
						mWindowManager.removeView(view);
					if (myFloatView != null)
					{
						mWindowManager.removeView(myFloatView);
						myFloatView = null;
					}
					break;
				case TelephonyManager.CALL_STATE_RINGING: // ����״̬
					Log.e(TAG, "-->�����������" + incomingNumber);
					Log.e(TAG,
							"-->�����������" + Algorithm.decryption(incomingNumber));

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
				case TelephonyManager.CALL_STATE_OFFHOOK: // ͨ��״̬
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
		{
			tv_phone_num.setText(TextUtil.replaceFixedPos(employee.mobilePhone,
					5, 8, "****"));
		}
		else
		{
			tv_phone_num.setText(employee.mobilePhone);
		}
		TextView tv_org = (TextView) myFloatView.findViewById(R.id.tv_org);
		tv_org.setText(employee.orgName);
		TextView tv_title = (TextView) myFloatView.findViewById(R.id.tv_title);
		tv_title.setText(employee.userTitle);

		mWindowManager = (WindowManager) getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE);
		wmParams = ((MyApplication) getApplication()).getMywmParams();

		/**
		 * ���¶���WindowManager.LayoutParams��������� ������;�ɲο�SDK�ĵ�
		 */
		if (DeviceInfoUtil.isSpecial())
		{
			wmParams.type = LayoutParams.TYPE_PHONE
					| LayoutParams.TYPE_SYSTEM_OVERLAY; // ����window type
		}
		else
		{
			wmParams.type = LayoutParams.TYPE_PHONE; // ����window type
		}
		wmParams.format = PixelFormat.RGBA_8888; // ����ͼƬ��ʽ��Ч��Ϊ����͸��
		wmParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
				| LayoutParams.FLAG_NOT_FOCUSABLE;
		/*
		 * �����flags���Ե�Ч����ͬ���������� ���������ɴ������������κ��¼�,ͬʱ��Ӱ�������¼���Ӧ��
		 * wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL |
		 * LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCHABLE;
		 */
		wmParams.gravity = Gravity.LEFT | Gravity.TOP; // �����������������Ͻ�
		wmParams.x = (int) CommonUtil.getWindowX(context);
		wmParams.y = (int) CommonUtil.getWindowY(context);
		wmParams.width = LayoutParams.WRAP_CONTENT;
		wmParams.height = LayoutParams.WRAP_CONTENT;

		mWindowManager.addView(myFloatView, wmParams);
	}
}
