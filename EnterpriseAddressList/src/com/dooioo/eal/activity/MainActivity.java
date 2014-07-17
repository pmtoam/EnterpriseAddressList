package com.dooioo.eal.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.dooioo.eal.dao.tools.EmployeeDBTool;
import com.dooioo.eal.entity.Employee;
import com.dooioo.eal.services.CoreService;
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

		// 启动Service
		startService(new Intent(context, CoreService.class));
		
		// 测试添加数据到数据库
//		testInsertData();

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
				employee.empNum = "93468";
				employee.empName = "陈洪";
				employee.sex = "男";
				employee.department = "移动开发部";
				employee.photo = "http://.....";
				
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
