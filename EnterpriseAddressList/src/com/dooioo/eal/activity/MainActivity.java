package com.dooioo.eal.activity;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.dooioo.eal.dao.tools.EmployeeDBTool;
import com.dooioo.eal.entity.Employee;
import com.dooioo.eal.util.FileUtil;
import com.dooioo.eal.util.Logger;
import com.dooioo.enterprise.address.list.R;

public class MainActivity extends Activity
{

	private final String TAG = "MainActivity";
	private Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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
//		startService(new Intent(context, CoreService.class));
		
		// 1.����������ݵ����ݿ�
//		testInsertData();

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
				employee.empNum = "93468";
				employee.empName = "�º�";
				employee.sex = "��";
				employee.department = "�ƶ�������";
				employee.photo = "http://.....";
				
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
