package com.dooioo.eal.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper
{
	public static final String DATABASE_NAME = "contact.db";
	public static final int DATABASE_VERSION = 1;
	public static final String TABLE_EMP = "tb_employee";

	public DBHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
//	values.put("userNameCn", employee.userNameCn);
//	values.put("mobilePhone", employee.mobilePhone);
//	values.put("orgName", employee.orgName);
//	values.put("userTitle", employee.userTitle);
	
	static String sql_employee = "";
	static
	{
		sql_employee = "CREATE TABLE " + TABLE_EMP + "("
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT," 
	            + "userNameCn TEXT,"
	            + "mobilePhone TEXT,"
	            + "orgName TEXT,"
				+ "userTitle TEXT"
				+ ")";
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(sql_employee);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
	}
	
}
