package com.dooioo.eal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.dooioo.eal.activity.MyApplication;
import com.dooioo.enterprise.address.list.R;

public class NullAdapter extends BaseAdapter
{

	private Context context;
	private String msg;
	private int height;

	public NullAdapter(Context context, String msg)
	{
		this.context = context;
		this.msg = msg;
	}

	public NullAdapter(Context context, String msg, int height)
	{
		this.context = context;
		this.msg = msg;
		this.height = height;
	}

	@Override
	public int getCount()
	{
		return 1;
	}

	@Override
	public Object getItem(int arg0)
	{
		return null;
	}

	@Override
	public long getItemId(int arg0)
	{
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		convertView = LayoutInflater.from(context).inflate(
				R.layout.null_adapter_list_item, null);
		RelativeLayout rl_null_prompt = (RelativeLayout) convertView
				.findViewById(R.id.rl_null_prompt);
		Button tv_null_prompt = (Button) convertView
				.findViewById(R.id.tv_null_prompt);
		tv_null_prompt.setText(msg);

		if (height != 0)
			rl_null_prompt.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, height));
		else if (MyApplication.getActualHeight() != 0)
			rl_null_prompt
					.setLayoutParams(new LayoutParams(
							LayoutParams.MATCH_PARENT, MyApplication
									.getActualHeight()));

		return convertView;
	}
}
