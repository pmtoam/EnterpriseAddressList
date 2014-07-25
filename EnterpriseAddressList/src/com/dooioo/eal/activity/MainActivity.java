package com.dooioo.eal.activity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Type;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.dooioo.eal.dao.DBHelper;
import com.dooioo.eal.dao.tools.EmployeeDBTool;
import com.dooioo.eal.entity.Employee;
import com.dooioo.eal.entity.EmployeeGet;
import com.dooioo.eal.network.DAsyncTaskRequest;
import com.dooioo.eal.network.DHttpConnUtil;
import com.dooioo.eal.network.DHttpRequest;
import com.dooioo.eal.network.DRequest;
import com.dooioo.eal.network.NetWorkConn;
import com.dooioo.eal.services.CoreService;
import com.dooioo.eal.util.CommonUtil;
import com.dooioo.eal.util.FileUtil;
import com.dooioo.eal.util.Logger;
import com.dooioo.enterprise.address.list.R;
import com.google.gson.reflect.TypeToken;

public class MainActivity extends Activity
{

	private final String TAG = "MainActivity";
	private Context context = this;
	private Activity activity = this;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// 获取token
//		getToken();
		
		//测试获取员工信息外网
//		testGetAllEmployeesRelease();
		
		// 测试组织架构数据解析
//		testDooiooAll();
		
		// 测试网络状态
//		testNetworkState();
		
		// 6.测试内网wifi状态下，组织架构json数据下载解析所用时间（数据大小2.42M）
//		testGetAllEmployees();
//		testQueryWhenGet();
		
		// 5.测试解析assets自带数据库
//		testCheckDB();

		// 4.测试获取根目录
		// /storage/emulated/0/Android/data/com.dooioo.enterprise.address.list/cache
//		Logger.e(TAG, "--> " + getExternalCacheDir().getAbsolutePath());
		// /storage/emulated/0
//		Logger.e(TAG, "--> " + Environment.getExternalStorageDirectory().getAbsolutePath());
		
		// 3.测试解压功能
//		testUnzip();
		
		// 2.启动Service
		startService(new Intent(context, CoreService.class));
		
		// 1.测试添加数据到数据库
//		testInsertData();

	}
	

	private void getToken()
	{
		MyApplication.getToken();
	}


	private void testDooiooAll()
	{
		String url = "http://dui.dooioo.com/public/json/dooioo/dooiooAll.js";
		NetWorkConn.downloadFile(url, context, NetWorkConn.DOWN_FILE_NAME_DOOIOO_ALL);
		File file = new File(Environment.getExternalStorageDirectory(), NetWorkConn.DOWN_FILE_NAME_DOOIOO_ALL);
		InputStream inputStream = null;
		try 
		{
			inputStream = new BufferedInputStream(new FileInputStream(file));
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		String result = null;
		try
		{
			result = DHttpConnUtil.readInputToString(inputStream);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		Logger.e(TAG, "--> result = " + result);
	}

	private void testNetworkState()
	{
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();	
		if (activeNetInfo == null)
		{
			Logger.e(TAG, "--> activeNetInfo == null");
			return;
		}
		
		Logger.e(TAG, "--> activeNetInfo.getType() = " + activeNetInfo.getType());
		Logger.e(TAG, "--> activeNetInfo.getSubtypeName() = " + activeNetInfo.getSubtypeName());
		Logger.e(TAG, "--> activeNetInfo.getExtraInfo() = " + activeNetInfo.getExtraInfo());
		Logger.e(TAG, "--> activeNetInfo.getReason() = " + activeNetInfo.getReason());
		Logger.e(TAG, "--> activeNetInfo.getSubtype() = " + activeNetInfo.getSubtype());
		Logger.e(TAG, "--> activeNetInfo.getTypeName() = " + activeNetInfo.getTypeName());
		Logger.e(TAG, "--> activeNetInfo.getDetailedState() = " + activeNetInfo.getDetailedState().toString());
	}

	private Handler handler = new Handler();
	private Runnable runnable = new Runnable()
	{
		
		@Override
		public void run()
		{
			Logger.e(TAG, "--> run()");
			DBHelper dbHelper = new DBHelper(context);
			SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
			Cursor c = sqLiteDatabase.query(DBHelper.TABLE_EMP, null, "mobilePhone = ?", new String[] { "18116136307" }, null, null, null);
			Logger.e(TAG, "--> c.getCount() = " + c.getCount());
			if (c != null)
			{
				while (c.moveToFirst())
				{
					Logger.e(TAG, "--> " + c.getString(1));
					Logger.e(TAG, "--> " + c.getString(2));
					Logger.e(TAG, "--> " + c.getString(3));
					Logger.e(TAG, "--> " + c.getString(4));
					Logger.e(TAG, "--> " + c.getString(5));
					Logger.e(TAG, "--> " + c.getString(6));
					break;
				}
			}
			c.close();
			sqLiteDatabase.close();
			handler.postDelayed(runnable, 5000);
		}
	};
	
	private void testQueryWhenGet()
	{
		handler.postDelayed(runnable, 5000);
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		handler.removeCallbacks(runnable);
	}

	private void testGetAllEmployeesRelease()
	{
		
		DRequest<EmployeeGet> dRequest = new DRequest<EmployeeGet>()
		{
			
			@Override
			public void showResult(EmployeeGet resp, Exception exception)
			{
				if (resp != null)
				{
					Logger.e(TAG, "---> resp != null");
				}
			}
		};
		
		String url = MyApplication.APP_SERVER_URL + "route/rest?method=renzi.employee.list";
		
		Type type = new TypeToken<EmployeeGet>(){}.getType();
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("empNo", "102804");
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("phoneNumber", number);
//		params.put("empNo", GlobalAPP.getEmployeeNum());
		
		Logger.e(TAG, "---> " + CommonUtil.getAccessToken(context));
		new DAsyncTaskRequest<EmployeeGet>(activity, dRequest, type, true, null).execute(new DHttpRequest("GET", url, MyApplication.tokenConfigMap, null));
	}
	
	private void testGetAllEmployees()
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
						new Thread()
						{
							public void run()
							{ 
								long last = System.currentTimeMillis();
								for (int i = 0; i < resp.emps.size(); i++)
								{
//									Logger.e(TAG, "-->" + i + resp.emps.get(i).userNameCn 
//											+ "  " + resp.emps.get(i).mobilePhone 
//											+ "  " + resp.emps.get(i).orgName 
//											+ "  " + resp.emps.get(i).userTitle);
									
									EmployeeDBTool employeeDBTool = new EmployeeDBTool(context);
									employeeDBTool.delete(resp.emps.get(i).mobilePhone);
									employeeDBTool.insert(resp.emps.get(i));
								}
								Log.e(TAG, "耗时 = " + (System.currentTimeMillis() - last) / 1000);
							};
						}.start();	
					}
				}
			}
		};
		
		String url = "http://renzi.dooioo.com/oms/api/getAllEmployees";
		Type type = new TypeToken<EmployeeGet>()
		{
		}.getType();
		DAsyncTaskRequest<EmployeeGet> dAsyncTaskRequest = new DAsyncTaskRequest<EmployeeGet>(activity, dRequest, type, false, null);
		dAsyncTaskRequest.execute(new DHttpRequest("POST", url, null));
	}

	private void testCheckDB()
	{
		new Thread()
		{
			public void run()
			{ 
				SQLiteDatabase sqLiteDatabase = FileUtil.openDatabase(context);
				// query(boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
				// Cursor c = db.query(false, "customer", new String[] { "*" }, "empNo=?", new String[] { String.valueOf(empNo) }, null, null, "id ASC", null);
				Cursor cursor = sqLiteDatabase.query("tb_employee", new String[] { "*" },null, null, null,null, null, null);
				if (cursor != null)
				{
					while (cursor.moveToNext())
					{
						Logger.e(TAG, "--> " + cursor.getString(1));
					}
					cursor.close();
				}
				sqLiteDatabase.close();
			};
		}.start();		
	}

	private void testUnzip()
	{
		new Thread()
		{
			public void run()
			{ 
				File path = Environment.getExternalStorageDirectory();
				String filePath = path.getAbsolutePath() + File.separator + "TestUnzip.zip";
				Logger.e(TAG, "--> " + filePath);
				FileUtil.unZip(path.getAbsolutePath() + File.separator + "TestUnzip.zip", path.getAbsolutePath() + File.separator + "Dooioo2" + File.separator);
			};
		}.start();
	}

	private void testInsertData()
	{
		new Thread()
		{
			public void run()
			{
				Employee employee = new Employee();
				// employee.empNum = "104409";
				// employee.empName = "王月星";
				// employee.sex = "男";
				// employee.department = "移动开发部";
				// employee.photo = "http://....";
				
//				employee.empNum = "93468";
//				employee.empName = "陈洪";
//				employee.sex = "男";
//				employee.department = "移动开发部";
//				employee.photo = "http://.....";
				
				long last = System.currentTimeMillis();
				EmployeeDBTool employeeDBTool = new EmployeeDBTool(context);
//				for (int i = 0; i < 10000; i++)
//				{
//					employeeDBTool.insert(employee);
//				}
				
				Log.e(TAG, "耗时 = " + (System.currentTimeMillis() - last) / 1000);
				// 10000 条插入，耗时 = 265 s 【acer】
				// 10000 条插入，耗时 = 189 s 【小米2s】
				// 10000 条插入，耗时 = 330 s 【三星 note2】
				// 10000 条插入，耗时 = 333 s 【三星 note2(恢复出厂设置后)】
				// 10000 条插入，耗时 = 483 s 【coolpad】
			};
		}.start();		
	}

}
