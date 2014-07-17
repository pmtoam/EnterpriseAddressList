package com.dooioo.eal.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dooioo.eal.util.Logger;

public class CoreReceiver extends BroadcastReceiver
{

	private final String TAG = "CoreReceiver";

	@Override
	public void onReceive(Context context, Intent intent)
	{
		Logger.e(TAG, "--> intent.getAction() = " + intent.getAction());
		context.startService(new Intent(context, CoreService.class));
	}

}
