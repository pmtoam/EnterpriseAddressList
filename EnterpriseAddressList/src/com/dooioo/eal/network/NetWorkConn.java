package com.dooioo.eal.network;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.os.Environment;

import com.dooioo.eal.util.CommonUtil;
import com.dooioo.eal.util.Logger;
import com.dooioo.enterprise.address.list.R;


public class NetWorkConn
{

	private final static String TAG = "NetWorkConn";
	
	public static void downloadFile(final String downloadUrl, final Context context) 
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
						String downApkName = "New" + context.getString(R.string.app_name) + ".apk";
						File file = new File(Environment.getExternalStorageDirectory(), downApkName);
						Logger.e(TAG, "downApkName: " + downApkName);
						Logger.e(TAG, "path: " + Environment.getExternalStorageDirectory());
						
						fileOutputStream = new FileOutputStream(file);
						byte[] buf = new byte[1024];
						int ch = -1;
						int count = 0;
						while ((ch = is.read(buf)) != -1) 
						{
							fileOutputStream.write(buf, 0, ch);
							count += ch;
							if(true)
								interrupt();
							Logger.e(TAG, "--> downloading count = " + count);					
						}
					}
					fileOutputStream.flush();
					if (fileOutputStream != null) 
						fileOutputStream.close();
					
					Logger.e(TAG, "--> download success.");					
					CommonUtil.setDownloadSuccessTime(System.currentTimeMillis(), context);
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
	
}
