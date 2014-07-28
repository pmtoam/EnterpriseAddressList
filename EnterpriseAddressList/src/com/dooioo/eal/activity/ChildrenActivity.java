package com.dooioo.eal.activity;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import com.dooioo.eal.adapter.DooiooAllAdapter;
import com.dooioo.eal.entity.DooiooAll;
import com.dooioo.enterprise.address.list.R;

public class ChildrenActivity extends Activity
{

	private final String TAG = "ChildrenActivity";
	private Context context = this;
	private Activity activity = this;

	private ListView lv_results;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_children);

		lv_results = (ListView) findViewById(R.id.lv_results);
		List<DooiooAll> dooiooAlls = (List<DooiooAll>) getIntent().getSerializableExtra("dooiooAlls");
		lv_results.setAdapter(new DooiooAllAdapter(context, dooiooAlls));

	}

}
