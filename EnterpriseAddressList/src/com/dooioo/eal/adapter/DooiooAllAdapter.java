package com.dooioo.eal.adapter;

import java.io.Serializable;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dooioo.eal.activity.ChildrenActivity;
import com.dooioo.eal.activity.ChildrenEmployeeActivity;
import com.dooioo.eal.entity.DooiooAll;
import com.dooioo.eal.util.Logger;
import com.dooioo.enterprise.address.list.R;

public class DooiooAllAdapter extends BaseAdapter
{

	private final String TAG = "DooiooAllAdapter";
	Context context;
	private List<DooiooAll> list;

	public DooiooAllAdapter(Context context, List<DooiooAll> list)
	{
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount()
	{
		return list.size();
	}

	@Override
	public DooiooAll getItem(int position)
	{
		return list.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;

		if (convertView == null)
		{
			convertView = LayoutInflater.from(context).inflate(
					R.layout.dooioo_all_item, null);
			holder = new ViewHolder();
			holder.tv = (TextView) convertView.findViewById(R.id.tv);
			holder.ll_item = (LinearLayout) convertView
					.findViewById(R.id.ll_item);

			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tv.setText(getItem(position).text);

		holder.ll_item
				.setOnClickListener(new android.view.View.OnClickListener()
				{

					@Override
					public void onClick(View v)
					{
						if (getItem(position).children != null
								&& getItem(position).children.size() > 0)
						{
							Intent intent = new Intent(context,
									ChildrenActivity.class);
							intent.putExtra("dooiooAlls",
									(Serializable) getItem(position).children);
							context.startActivity(intent);
						}
						else
						{
							Logger.e(TAG, "×ÓÏîÎª¿Õ");
							Intent intent = new Intent(context,
									ChildrenEmployeeActivity.class);
							intent.putExtra("dooiooAll",
									(Serializable) getItem(position));
							context.startActivity(intent);
						}
					}
				});

		return convertView;
	}

	class ViewHolder
	{
		TextView tv;
		LinearLayout ll_item;
	}

}
