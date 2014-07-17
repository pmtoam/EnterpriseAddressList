package com.dooioo.eal.util;

public class DeviceInfoUtil
{

	private final String TAG = "DeviceinfoUtil";

	public static boolean isSpecial()
	{
		boolean rt = false;
		try
		{
			rt = android.os.Build.MODEL.equals("Coolpad 8720L") ? true : false;
			if (!rt)
			{
				rt = (android.os.Build.MODEL.equals("V370")) ? true : false;
			}
		}
		catch (Exception e)
		{
			rt = (android.os.Build.MODEL.equals("V370")) ? true : false;
		}
		return rt;
	}

}
