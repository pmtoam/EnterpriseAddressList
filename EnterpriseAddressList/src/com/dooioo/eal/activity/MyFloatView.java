package com.dooioo.eal.activity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dooioo.eal.util.CommonUtil;
import com.dooioo.enterprise.address.list.R;

public class MyFloatView extends LinearLayout
{
	Context context;

	private float mTouchStartX;
	private float mTouchStartY;
	private float x;
	private float y;

	private WindowManager wm = (WindowManager) getContext()
			.getApplicationContext().getSystemService("window");

	private WindowManager.LayoutParams wmParams = ((MyApplication) getContext()
			.getApplicationContext()).getMywmParams();

	public MyFloatView(Context context)
	{
		super(context);
		this.context = context;
		LayoutInflater mInflater = LayoutInflater.from(context);
		View myView = mInflater.inflate(R.layout.activity_prompt, null);
		addView(myView);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		x = event.getRawX();
		y = event.getRawY() - 25;
		Log.i("currP", "currX" + x + "====currY" + y);
		switch (event.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			// 获取相对View的坐标，即以此View左上角为原点
			mTouchStartX = event.getX();
			mTouchStartY = event.getY();
			Log.i("startP", "startX" + mTouchStartX + "====startY"
					+ mTouchStartY);
			break;
		case MotionEvent.ACTION_MOVE:
			updateViewPosition();
			break;

		case MotionEvent.ACTION_UP:
			updateViewPosition();
			Toast.makeText(context, "位置已移动并保存", Toast.LENGTH_SHORT).show();
			mTouchStartX = mTouchStartY = 0;
			break;
		}
		return true;
	}

	private void updateViewPosition()
	{
		// 更新浮动窗口位置参数
		wmParams.x = (int) (x - mTouchStartX);
		wmParams.y = (int) (y - mTouchStartY);
		CommonUtil.setWindowX(wmParams.x, context);
		CommonUtil.setWindowY(wmParams.y, context);
		wm.updateViewLayout(this, wmParams);
	}

}
