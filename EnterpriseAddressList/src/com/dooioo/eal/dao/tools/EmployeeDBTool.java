package com.dooioo.eal.dao.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.dooioo.eal.dao.DBHelper;
import com.dooioo.eal.entity.Employee;
import com.dooioo.eal.util.Logger;

public class EmployeeDBTool
{
	
	Context context;
	private final String TAG = "EmployeeDBTool";
	
	public EmployeeDBTool(Context context)
	{
		super();
		this.context = context;
	}

	public void insert(Employee employee)
	{
		ContentValues values = new ContentValues();
		values.put("empNum", employee.empNum);
		values.put("empName", employee.empName);
		values.put("sex", employee.sex);
		values.put("department", employee.department);
		values.put("phoneNum", employee.phoneNum);
		values.put("photo", employee.photo);
		
		DBHelper dbHelper = new DBHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		try
		{
			long _id = db.insert(DBHelper.TABLE_EMP, "id", values);
			Logger.e(TAG, "--> insert _id = " + _id);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			dbHelper.close();
		}
	}

}
