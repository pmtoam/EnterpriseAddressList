package com.dooioo.eal.activity;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import com.dooioo.eal.adapter.DooiooAllAdapter;
import com.dooioo.eal.adapter.DooiooChildrenEmployeeAdapter;
import com.dooioo.eal.dao.tools.EmployeeDBTool;
import com.dooioo.eal.entity.DooiooAll;
import com.dooioo.eal.entity.Employee;
import com.dooioo.eal.util.Logger;
import com.dooioo.enterprise.address.list.R;

public class ChildrenEmployeeActivity extends Activity
{

	private final String TAG = "ChildrenEmployeeActivity";
	private Context context = this;
	private Activity activity = this;

	private ListView lv_results;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_children);

		lv_results = (ListView) findViewById(R.id.lv_results);
		DooiooAll dooiooAll = (DooiooAll) getIntent().getSerializableExtra(
				"dooiooAll");

		// 'text': '浦城店',
		// 'id': '20179',
		// 'type': '门店',
		// 'status': '1',
		// 'orgClass': '前台',
		// 'children': [

		Logger.e(TAG, "*******************************");
		Logger.e(TAG, "text = " + dooiooAll.text);
		Logger.e(TAG, "id = " + dooiooAll.id);
		Logger.e(TAG, "type = " + dooiooAll.type);
		Logger.e(TAG, "status = " + dooiooAll.status);
		Logger.e(TAG, "orgClass = " + dooiooAll.orgClass);

		EmployeeDBTool employeeDBTool = new EmployeeDBTool(context);
		List<Employee> employees = employeeDBTool.queryEmployeeById(
				dooiooAll.id, false);

		// for (Employee employee : employees)
		// {
		// Logger.e(TAG, "" + employee.userNameCn);
		// }
		lv_results.setAdapter(new DooiooChildrenEmployeeAdapter(context,
				employees));

	}

}
