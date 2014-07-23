package com.dooioo.eal.dao.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dooioo.eal.dao.DBHelper;
import com.dooioo.eal.entity.Employee;
import com.dooioo.eal.util.Logger;

public class EmployeeDBTool
{
	
	Context context;
	private final static String TAG = "EmployeeDBTool";
	
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
			Logger.e(TAG, "--> delete _id = " + _id);
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
		values.put("orgId", employee.orgId);
		values.put("userCode", employee.userCode);
		
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

	public static Employee queryEmployee(Context context, String incomingNumber)
	{
		Employee employee = null;
		DBHelper dbHelper = new DBHelper(context);
		SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
		Cursor c = sqLiteDatabase.query(DBHelper.TABLE_EMP, null, "mobilePhone = ?", new String[] { incomingNumber }, null, null, null);
		Logger.e(TAG, "--> c.getCount() = " + c.getCount());
		if (c != null)
		{
			while (c.moveToFirst())
			{
//				Logger.e(TAG, "--> " + c.getString(1));
//				Logger.e(TAG, "--> " + c.getString(2));
//				Logger.e(TAG, "--> " + c.getString(3));
//				Logger.e(TAG, "--> " + c.getString(4));
				employee = new Employee();
				employee.userNameCn = c.getString(1);
				employee.mobilePhone = c.getString(2);
				employee.orgName = c.getString(3);
				employee.userTitle = c.getString(4);
				break;
			}
		}
		c.close();
		sqLiteDatabase.close();
		return employee;
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
