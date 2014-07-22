package com.dooioo.eal.activity;

import java.io.File;
import java.lang.reflect.Type;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.dooioo.eal.dao.DBHelper;
import com.dooioo.eal.dao.tools.EmployeeDBTool;
import com.dooioo.eal.entity.Employee;
import com.dooioo.eal.entity.EmployeeGet;
import com.dooioo.eal.network.DAsyncTaskRequest;
import com.dooioo.eal.network.DHttpRequest;
import com.dooioo.eal.network.DRequest;
import com.dooioo.eal.services.CoreService;
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

		// 6.��������wifi״̬�£���֯�ܹ�json�������ؽ�������ʱ�䣨���ݴ�С2.42M��
//		testGetAllEmployees();
//		testQueryWhenGet();
		
		// 5.���Խ���assets�Դ����ݿ�
//		testCheckDB();

		// 4.���Ի�ȡ��Ŀ¼
		// /storage/emulated/0/Android/data/com.dooioo.enterprise.address.list/cache
//		Logger.e(TAG, "--> " + getExternalCacheDir().getAbsolutePath());
		// /storage/emulated/0
//		Logger.e(TAG, "--> " + Environment.getExternalStorageDirectory().getAbsolutePath());
		
		// 3.���Խ�ѹ����
//		testUnzip();
		
		// 2.����Service
		startService(new Intent(context, CoreService.class));
		
		// 1.����������ݵ����ݿ�
//		testInsertData();

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
								Log.e(TAG, "��ʱ = " + (System.currentTimeMillis() - last) / 1000);
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
				// employee.empName = "������";
				// employee.sex = "��";
				// employee.department = "�ƶ�������";
				// employee.photo = "http://....";
				
//				employee.empNum = "93468";
//				employee.empName = "�º�";
//				employee.sex = "��";
//				employee.department = "�ƶ�������";
//				employee.photo = "http://.....";
				
				long last = System.currentTimeMillis();
				EmployeeDBTool employeeDBTool = new EmployeeDBTool(context);
//				for (int i = 0; i < 10000; i++)
//				{
//					employeeDBTool.insert(employee);
//				}
				
				Log.e(TAG, "��ʱ = " + (System.currentTimeMillis() - last) / 1000);
				// 10000 �����룬��ʱ = 265 s ��acer��
				// 10000 �����룬��ʱ = 189 s ��С��2s��
				// 10000 �����룬��ʱ = 330 s ������ note2��
				// 10000 �����룬��ʱ = 333 s ������ note2(�ָ��������ú�)��
				// 10000 �����룬��ʱ = 483 s ��coolpad��
			};
		}.start();		
	}

}
