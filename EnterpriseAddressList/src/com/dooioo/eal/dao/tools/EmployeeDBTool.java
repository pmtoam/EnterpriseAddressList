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

	public void delete(String mobilePhone)
	{
		DBHelper dbHelper = new DBHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();		
		try
		{
			int _id = db.delete(DBHelper.TABLE_EMP, "mobilePhone = ?", new String[] { mobilePhone });
			Logger.i(TAG, "--> delete _id = " + _id);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			db.close();
		}
	}

	public void insert(Employee employee)
	{
		ContentValues values = new ContentValues();
		values.put("userNameCn", employee.userNameCn);
		values.put("mobilePhone", employee.mobilePhone);
		values.put("orgName", employee.orgName);
		values.put("userTitle", employee.userTitle);
		
		DBHelper dbHelper = new DBHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		try
		{
			long _id = db.insert(DBHelper.TABLE_EMP, "id", values);
			Logger.i(TAG, "--> insert _id = " + _id);
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

//	public void insert(Employee employee)
//	{
//		ContentValues values = new ContentValues();
//		values.put("empNum", employee.empNum);
//		values.put("empName", employee.empName);
//		values.put("sex", employee.sex);
//		values.put("department", employee.department);
//		values.put("phoneNum", employee.phoneNum);
//		values.put("photo", employee.photo);
//		
//		DBHelper dbHelper = new DBHelper(context);
//		SQLiteDatabase db = dbHelper.getWritableDatabase();
//		
//		try
//		{
//			long _id = db.insert(DBHelper.TABLE_EMP, "id", values);
//			Logger.e(TAG, "--> insert _id = " + _id);
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//		finally
//		{
//			dbHelper.close();
//		}
//	}

}
