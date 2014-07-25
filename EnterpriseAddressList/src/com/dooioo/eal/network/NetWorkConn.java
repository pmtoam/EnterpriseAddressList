package com.dooioo.eal.network;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.dooioo.eal.dao.tools.EmployeeDBTool;
import com.dooioo.eal.entity.EmployeeGet;
import com.dooioo.eal.util.CommonUtil;
import com.dooioo.eal.util.Logger;
import com.google.gson.reflect.TypeToken;

public class NetWorkConn
{

	private final static String TAG = "NetWorkConn";
	private final static String DOWN_FILE_NAME = "NewEAL.apk";
	public final static String DOWN_FILE_NAME_DOOIOO_ALL = "dooiooAll.js";

	public static void downloadFile(final String downloadUrl,
			final Context context)
	{
		Logger.e(TAG, "--> downloadUrl = " + downloadUrl);

		CommonUtil.setDownloading(true, context);
		new Thread()
		{
			public void run()
			{
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(downloadUrl);
				HttpResponse response;
				try
				{
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;

					if (is != null)
					{
						File file = new File(
								Environment.getExternalStorageDirectory(),
								DOWN_FILE_NAME);
						fileOutputStream = new FileOutputStream(file);
						byte[] buf = new byte[1024];
						int ch = -1;
						int count = 0;
						while ((ch = is.read(buf)) != -1)
						{
							fileOutputStream.write(buf, 0, ch);
							count += ch;
							if (true)
								interrupt();
							Logger.e(TAG, "--> downloading count = " + count);
						}
					}
					fileOutputStream.flush();
					if (fileOutputStream != null)
						fileOutputStream.close();

					Logger.e(TAG, "--> download success.");
					CommonUtil.setDownloadSuccessTime(
							System.currentTimeMillis(), context);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				finally
				{
					CommonUtil.setDownloading(false, context);
				}
			}
		}.start();
	}

	public static void downloadFile(final String downloadUrl,
			final Context context, final String downloadFileName)
	{
		Logger.e(TAG, "--> downloadUrl = " + downloadUrl);

		new Thread()
		{
			public void run()
			{
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(downloadUrl);
				HttpResponse response;
				try
				{
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;

					if (is != null)
					{
						File file = new File(
								Environment.getExternalStorageDirectory(),
								downloadFileName);
						fileOutputStream = new FileOutputStream(file);
						byte[] buf = new byte[1024];
						int ch = -1;
						int count = 0;
						while ((ch = is.read(buf)) != -1)
						{
							fileOutputStream.write(buf, 0, ch);
							count += ch;
							if (true)
								interrupt();
							Logger.e(TAG, "--> downloading count = " + count);
						}
					}
					fileOutputStream.flush();
					if (fileOutputStream != null)
						fileOutputStream.close();

					Logger.e(TAG, "--> download success.");
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}.start();
	}

	private static final String getAllEmployeesUrl = "http://renzi.dooioo.com/oms/api/getAllEmployees";

	public static void downloadFile(final Context context)
	{
		DRequest<EmployeeGet> dRequest = new DRequest<EmployeeGet>()
		{

			@Override
			public void showResult(final EmployeeGet resp, Exception exception)
			{
				if (resp != null && resp.status.equals("ok"))
				{
					if (resp.emps != null && resp.emps.size() > 0)
					{
						CommonUtil.setDownloadSuccessTime(
								System.currentTimeMillis(), context);

						new Thread()
						{
							public void run()
							{
								long last = System.currentTimeMillis();
								for (int i = 0; i < resp.emps.size(); i++)
								{
									// Logger.e(TAG, "-->" + i +
									// resp.emps.get(i).userNameCn
									// + "  " + resp.emps.get(i).mobilePhone
									// + "  " + resp.emps.get(i).orgName
									// + "  " + resp.emps.get(i).userTitle);

									EmployeeDBTool employeeDBTool = new EmployeeDBTool(
											context);
									employeeDBTool
											.delete(resp.emps.get(i).mobilePhone);
									employeeDBTool.insert(resp.emps.get(i));
								}
								Log.e(TAG,
										"ºÄÊ± = "
												+ (System.currentTimeMillis() - last)
												/ 1000);
							};
						}.start();
					}
				}
			}
		};

		Type type = new TypeToken<EmployeeGet>()
		{
		}.getType();
		DAsyncTaskRequest<EmployeeGet> dAsyncTaskRequest = new DAsyncTaskRequest<EmployeeGet>(
				null, dRequest, type, false, null);
		dAsyncTaskRequest.execute(new DHttpRequest("POST", getAllEmployeesUrl,
				null));
	}

}
