package com.dooioo.eal.dao.tools;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.dooioo.eal.dao.DBHelper;
import com.dooioo.eal.entity.Employee;
import com.dooioo.eal.util.FileUtil;
import com.dooioo.eal.util.Logger;

public class EmployeeDBTool
{

	Context context;
	private final static String TAG = "EmployeeDBTool";

	public EmployeeDBTool(Context context)
	{
		super();
		this.context = context;
		net.sqlcipher.database.SQLiteDatabase.loadLibs(context);
	}

	public void delete(String mobilePhone)
	{
		DBHelper dbHelper = new DBHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase(DBHelper.SECRET_KEY);
		try
		{
			int _id = db.delete(DBHelper.TABLE_EMP, "mobilePhone = ?",
					new String[]
					{ mobilePhone });
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
		SQLiteDatabase db = dbHelper.getWritableDatabase(DBHelper.SECRET_KEY);

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

	public Employee queryEmployee(Context context, String incomingNumber,
			boolean isBring)
	{
		Employee employee = null;
		SQLiteDatabase sqLiteDatabase = null;

		if (isBring)
		{
			sqLiteDatabase = FileUtil.openDatabase(context);
		}
		else
		{
			DBHelper dbHelper = new DBHelper(context);
			sqLiteDatabase = dbHelper.getReadableDatabase(DBHelper.SECRET_KEY);
		}

		Cursor c = sqLiteDatabase.query(DBHelper.TABLE_EMP, null,
				"mobilePhone = ?", new String[]
				{ incomingNumber }, null, null, null);
		Logger.e(TAG, "--> c.getCount() = " + c.getCount());
		if (c != null)
		{
			while (c.moveToFirst())
			{
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

		if (c.getCount() == 0 && !isBring)
		{
			Logger.e(TAG, "c.getCount() == 0 && !isBring");
			return queryEmployee(context, incomingNumber, true);
		}
		return employee;
	}

	// public void insert(Employee employee)
	// {
	// ContentValues values = new ContentValues();
	// values.put("empNum", employee.empNum);
	// values.put("empName", employee.empName);
	// values.put("sex", employee.sex);
	// values.put("department", employee.department);
	// values.put("phoneNum", employee.phoneNum);
	// values.put("photo", employee.photo);
	//
	// DBHelper dbHelper = new DBHelper(context);
	// SQLiteDatabase db = dbHelper.getWritableDatabase();
	//
	// try
	// {
	// long _id = db.insert(DBHelper.TABLE_EMP, "id", values);
	// Logger.e(TAG, "--> insert _id = " + _id);
	// }
	// catch (Exception e)
	// {
	// e.printStackTrace();
	// }
	// finally
	// {
	// dbHelper.close();
	// }
	// }

}
