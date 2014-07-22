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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dooioo.eal.activity.MyApplication;
import com.dooioo.eal.activity.MyFloatView;
import com.dooioo.eal.network.NetWorkConn;
import com.dooioo.eal.util.CommonUtil;
import com.dooioo.eal.util.DensityUtil;
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
		windowManager = (WindowManager) getApplicationContext()
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
		public void onCallStateChanged(int state, String incomingNumber)
		{
			super.onCallStateChanged(state, incomingNumber);
			try
			{
				switch (state)
				{
				case TelephonyManager.CALL_STATE_IDLE: // ����״̬��û��ͨ��û������
					 if (view != null)
					 windowManager.removeView(view);
					if (myFV != null)
						windowManager.removeView(myFV);
					break;
				case TelephonyManager.CALL_STATE_RINGING: // ����״̬
					Log.e(TAG, "-->�����������" + incomingNumber);

					// Intent intent = new Intent();
					// intent.setAction("android.intent.action.DOOIOO_CALL_STATE_RINGING");
					// sendBroadcast(intent);

					addView();
					break;
				case TelephonyManager.CALL_STATE_OFFHOOK: // ͨ��״̬
					break;
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	private WindowManager.LayoutParams wmParams = null;
	/* ����View�Ĵ��룬������һ��ImageView��Ϊ��ʾ */
	private MyFloatView myFV = null;

	public void addView()
	{
////		view = inflater.inflate(R.layout.activity_prompt, null);
//		 // TextView tv_address = (TextView) view
//		 // .findViewById(R.id.tv_show_address);
//		 // TextView tv_number = (TextView) view
//		 // .findViewById(R.id.tv_show_number);
//		 // tv_address.setText(address);
//		 //��ѯ�ú����Ӧ������
//		 // String numbername = queryNumberName(incomingNumber);
//		 // tv_number.setText(numbername);
//		 final WindowManager.LayoutParams params = new
//		 WindowManager.LayoutParams();
//		 params.height = WindowManager.LayoutParams.MATCH_PARENT;
//		 params.width = WindowManager.LayoutParams.MATCH_PARENT;
//		 params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//		 | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
//		 | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
//		 params.format = PixelFormat.TRANSLUCENT;
//		 //��ȡƫ����
//		 // int dx= sp.getInt("dx", 0);
//		 // int dy = sp.getInt("dy", 0);
//		 // System.out.println("dx =" + dx);
//		 // System.out.println("dy =" + dx);
////		 params.x= 500 +params.x ;
////		 params.y = 500 +params.y;
//		 params.x = (int) CommonUtil.getWindowX(context);
//		 params.y = (int) CommonUtil.getWindowY(context);
//		
//		 params.type = WindowManager.LayoutParams.TYPE_TOAST;
//		 // add window bad token
//		 windowManager.addView(view, params);

		myFV = new MyFloatView(getApplicationContext());
		TextView tv_name = (TextView) myFV.findViewById(R.id.tv_name);
		tv_name.setText("ϰ��ƽ");
		// ��ȡWindowManager
		windowManager = (WindowManager) getApplicationContext()
				.getSystemService("window");
		// ����LayoutParams(ȫ�ֱ�������ز���
		wmParams = ((MyApplication) getApplication()).getMywmParams();

		/**
		 * ���¶���WindowManager.LayoutParams��������� ������;�ɲο�SDK�ĵ�
		 */
		wmParams.type = LayoutParams.TYPE_PHONE; // ����window type
		wmParams.format = PixelFormat.RGBA_8888; // ����ͼƬ��ʽ��Ч��Ϊ����͸��

		// ����Window flag
		/*
		 * FLAG_NOT_TOUCH_MODAL = 0x00000020;(ʮ������)-->32(ʮ����)-->100000(������)
		 * FLAG_NOT_FOCUSABLE = 0x00000008;(ʮ������)--> 8(ʮ����)-->001000(������)
		 * LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE =
		 * 101000(������)-->40(ʮ����)-->0x0028(ʮ������)
		 */
		wmParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
				| LayoutParams.FLAG_NOT_FOCUSABLE;
		/*
		 * �����flags���Ե�Ч����ͬ���������� ���������ɴ������������κ��¼�,ͬʱ��Ӱ�������¼���Ӧ��
		 * wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL |
		 * LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCHABLE;
		 */

		wmParams.gravity = Gravity.LEFT | Gravity.TOP; // �����������������Ͻ�
		// ����Ļ���Ͻ�Ϊԭ�㣬����x��y��ʼֵ
		wmParams.x = (int) CommonUtil.getWindowX(context);
		wmParams.y = (int) CommonUtil.getWindowY(context);

		// �����������ڳ�������
		wmParams.width = LayoutParams.WRAP_CONTENT;
		wmParams.height = LayoutParams.WRAP_CONTENT;

		// ��ʾmyFloatViewͼ��
		windowManager.addView(myFV, wmParams);
	}
}
