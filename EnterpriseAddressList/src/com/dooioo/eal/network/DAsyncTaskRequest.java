package com.dooioo.eal.network;

import java.lang.reflect.Type;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.dooioo.eal.util.Logger;

/**
 * 
 * @author PMTOAM
 * 
 * @param <T>
 */
public class DAsyncTaskRequest<T> extends AsyncTask<DHttpRequest, Integer, T>
{

	private final static String TAG = "AsyncTaskRequest";
	private Activity activity;
	private DRequest<T> request;
	private Type type;
	private boolean isSecurityContext;
//	private ProgressDialog dialog;
	private String loadingMsg;
	private Exception exception;

	/**
	 * AsyncTask Request.
	 * 
	 * @param activity
	 * @param request
	 *            Request model instance.
	 * @param type
	 *            Return model type.
	 * @param isSecurityContext
	 *            Whether to support the security context.
	 */
	public DAsyncTaskRequest(Activity activity, DRequest<T> request, Type type,
			boolean isSecurityContext, String loadingMsg)
	{
		this.activity = activity;
		this.request = request;
		this.type = type;
		this.loadingMsg = loadingMsg;
		this.isSecurityContext = isSecurityContext;
	}

	@Override
	protected void onPreExecute()
	{
//		dialog = new ProgressDialog(activity);
//		if (loadingMsg != null && !loadingMsg.equals(""))
//			dialog.setMessage(loadingMsg);
//		else
//			dialog.setMessage("loading...");
//		dialog.setCanceledOnTouchOutside(false);
//		if (!activity.isFinishing())
//			dialog.show();
	}

	@Override
	protected T doInBackground(DHttpRequest... args)
	{
		T resp = null;
		try
		{
			if (isSecurityContext)
			{
				resp = DHttpConnUtil.requestSecurity(args[0].method,
						args[0].url, args[0].headersParams, args[0].params,
						type);
			}
			else
			{
				resp = DHttpConnUtil.request(args[0].method, args[0].url,
						DHttpConnUtil.encodeParam(args[0].params), type);
			}
			Logger.e(TAG, "doInBackground resp = " + resp);
		}
		catch (Exception e)
		{
			exception = e;
			e.printStackTrace();
			Logger.e(TAG, "e.toString() = " + e.toString());
			Logger.e(TAG, "e.getMessage() = " + e.getMessage());
			Logger.e(TAG,
					"e.getLocalizedMessage() = " + e.getLocalizedMessage());
		}
		return resp;
	}

	@Override
	protected void onPostExecute(T resp)
	{

//		if (dialog != null && dialog.isShowing())
//			dialog.dismiss();

		if (request != null)
			request.showResult(resp, exception);

	}

}
