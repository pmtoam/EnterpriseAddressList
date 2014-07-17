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

		// ����Service
		startService(new Intent(context, CoreService.class));
		
		// ����������ݵ����ݿ�
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
