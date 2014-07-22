package com.dooioo.eal.test;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

public class EmployeeProviderTest extends AndroidTestCase
{
	public final String TAG = "EmployeeProviderTest";

	static final String PROVIDER_NAME = "com.dooioo.eal.provider.Employees";
	static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME
			+ "/employees");

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public void retrieveData()
	{
		// Uri uri = CONTENT_URI;
		Uri uri = Uri.parse("content://" + PROVIDER_NAME + "/employees"
				+ "/18116136307");
		Cursor c = null;
		if (android.os.Build.VERSION.SDK_INT < 11)
		{
			c = ((Activity)getContext()).managedQuery(uri, null, null, null, null);
		}
		else
		{
			CursorLoader cursorLoader = new CursorLoader(getContext(), uri, null,
					null, null, null);
			c = cursorLoader.loadInBackground();
		}

		if (c == null)
		{
			Log.e(TAG, "--> c == null");
			return;
		}

		Log.e(TAG, "--> c.getCount() = " + c.getCount());

		if (c.moveToFirst())
		{
			do
			{
				Log.e(TAG, "--> " + c.getString(c.getColumnIndex("userNameCn")));
				Log.e(TAG, "--> " + c.getString(c.getColumnIndex("mobilePhone")));
				Log.e(TAG, "--> " + c.getString(c.getColumnIndex("orgName")));
				Log.e(TAG, "--> " + c.getString(c.getColumnIndex("userTitle")));
			}
			while (c.moveToNext());
		}
	}
}
