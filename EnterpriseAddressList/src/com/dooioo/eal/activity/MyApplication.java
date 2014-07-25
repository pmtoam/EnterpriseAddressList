package com.dooioo.eal.activity;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.WindowManager;

import com.dooioo.eal.entity.TokenEntity;
import com.dooioo.eal.network.DAsyncTaskRequest;
import com.dooioo.eal.network.DHttpRequest;
import com.dooioo.eal.network.DRequest;
import com.dooioo.eal.util.Base64Util;
import com.dooioo.eal.util.CommonUtil;
import com.dooioo.eal.util.Logger;
import com.dooioo.eal.util.MD5Util;
import com.google.gson.reflect.TypeToken;

public class MyApplication extends Application
{

	public static final String TAG = "MyApplication";
	public static final String APP_SERVER_URL = "http://112.124.27.35:9090/";
	public static final String[] EMPLOYEE_NUMBER =
	{ "104409", "102804", "93468", "99289" };
	public static final String[] PWD =
	{ ".qwerty123", ".qwerty124", ".qwerty125" };
	public static Context context;
	private WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();

	public WindowManager.LayoutParams getMywmParams()
	{
		return wmParams;
	}

	private static String Imei, Imsi;

	public static Map<String, String> requestMap = new HashMap<String, String>();
	public static Map<String, String> tokenConfigMap = new HashMap<String, String>();

	@Override
	public void onCreate()
	{
		super.onCreate();

		TelephonyManager mTelephonyMgr = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		Imsi = mTelephonyMgr.getSubscriberId();
		Imsi = TextUtils.isEmpty(Imsi) ? "" : Imsi;
		Imei = mTelephonyMgr.getDeviceId();

		context = getApplicationContext();

		requestMap.put("client_id", "APP-Customer");
		requestMap.put("client_secret", "KiowPdfsal2380jfdsl");
		requestMap.put("appId", "APP-AddrBook");
		requestMap.put("token", CommonUtil.getAccessToken(context));

		tokenConfigMap.put("Authorization",
				"Bearer " + CommonUtil.getAccessToken(context));
		tokenConfigMap.put("Accept", "application/json; charset=utf-8");
	}

	public static String getImei()
	{
		return Imei;
	}

	public static String getImsi()
	{
		return Imsi;
	}

	private static int pwd_index = 0;
	private static int employee_number_index = 0;
	private static int get_token_times = 0;

	public static void getToken()
	{
		get_token_times++;
		if (get_token_times > 100)
		{
			get_token_times = 0;
			return;
		}

		DRequest<TokenEntity> dRequest = new DRequest<TokenEntity>()
		{

			@Override
			public void showResult(TokenEntity resp, Exception exception)
			{
				if (exception != null)
				{
					switch (CommonUtil.checkExceptionType(TAG, exception))
					{
					case 1:
					case 4:
						Logger.e(TAG, "---> connect_time_out_ex");
						break;
					case 2:
					case 5:
					case 7:
						Logger.e(TAG, "---> login_nonet");
						break;
					case 6:
						Logger.e(TAG, "---> invalid_host");
						break;
					case 3:// JSON error
						break;
					}
				}
				else
				{
					if (resp != null && resp.access_token != null
							&& resp.access_token.length() > 0)
					{
						Logger.e(TAG, "---> get_token_success.");
						CommonUtil.setAccessToken(context, resp.access_token);
					}
					else
					{
						Logger.e(TAG, "---> get_token_fail.");
						pwd_index++;
						if (pwd_index > PWD.length - 1)
						{
							pwd_index = 0;
							employee_number_index++;
							if (employee_number_index > EMPLOYEE_NUMBER.length - 1)
							{
								employee_number_index = 0;
							}
						}
						getToken();
					}
				}
			}
		};

		String token_url = APP_SERVER_URL + "/oauth/token";

		Map<String, String> map = requestMap;
		String clientId = map.get("client_id");
		String clientSecret = map.get("client_secret");
		Logger.e(TAG, "---> pwd_index = " + pwd_index);
		String pwdsec = MD5Util.getMD5ofStr(PWD[pwd_index]);
		String signStr = MD5Util
				.getMD5ofStr(EMPLOYEE_NUMBER[employee_number_index] + pwdsec
						+ clientId + clientSecret);
		String codeStr = Base64Util
				.encode(EMPLOYEE_NUMBER[employee_number_index] + ":" + pwdsec
						+ "aB1c");
		map.put("grant_type", "password");
		map.put("code", codeStr);
		map.put("sign", signStr);

		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("Authorization",
				"Basic " + Base64Util.encode(clientId + ":" + clientSecret));
		headerMap.put("Accept", "application/json; charset=utf-8");

		Type type = new TypeToken<TokenEntity>()
		{
		}.getType();
		new DAsyncTaskRequest<TokenEntity>(null, dRequest, type, true, null)
				.execute(new DHttpRequest("POST", token_url, headerMap, map));

	}

}
