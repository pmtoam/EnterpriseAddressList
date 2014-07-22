package com.dooioo.eal.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.dooioo.eal.dao.DBHelper;

/**
 * 
 * @author PMTOAM
 *
 */
public class EmployeesProvider extends ContentProvider
{

	static final String PROVIDER_NAME = "com.dooioo.eal.provider.Employees";
	static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/employees");
	
	static final int EMPLOYEES = 1;
	static final int EMPLOYEE_EMP_NUM = 2;
	
	private static final UriMatcher uriMatcher;
	static
	{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, "employees", EMPLOYEES);
		uriMatcher.addURI(PROVIDER_NAME, "employees/#", EMPLOYEE_EMP_NUM);
	}
	
	SQLiteDatabase db;
	
	@Override
	public boolean onCreate()
	{
		Context context = getContext();
		DBHelper dbHelper = new DBHelper(context);
		db = dbHelper.getReadableDatabase();
		return (db == null) ? false : true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder)
	{
		SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
		sqlBuilder.setTables(DBHelper.TABLE_EMP);
		
		if (uriMatcher.match(uri) == EMPLOYEE_EMP_NUM)
		{
			sqlBuilder.appendWhere("mobilePhone = " + uri.getPathSegments().get(1));
		}
		
		Cursor c = sqlBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public String getType(Uri uri)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs)
	{
		// TODO Auto-generated method stub
		return 0;
	}

}
