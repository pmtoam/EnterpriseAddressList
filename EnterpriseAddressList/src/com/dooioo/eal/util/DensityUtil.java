package com.dooioo.eal.util;


import android.content.Context;

/**
 * ������������λת��������
 * 
 * @author android_ls
 * 
 */
public class DensityUtil
{

	/**
	 * ����λΪdip��ֵת���ɵ�λΪpx��ֵ
	 * 
	 * @param context
	 *            Context
	 * @param dipValue
	 *            dipֵ
	 * @return pxֵ
	 */
	public static int dip2px(Context context, float dipValue)
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * ����λΪpx��ֵת���ɵ�λΪdip��ֵ
	 * 
	 * @param context
	 *            Context
	 * @param pxValue
	 *            ����ֵ
	 * @return dipֵ
	 */
	public static int px2dip(Context context, float pxValue)
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * ��pxֵת��Ϊspֵ����֤���ִ�С����
	 * 
	 * @param pxValue
	 * @param fontScale ��DisplayMetrics��������scaledDensity��
	 * @return
	 */
	public static int px2sp(Context context, float pxValue)
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * ��spֵת��Ϊpxֵ����֤���ִ�С����
	 * 
	 * @param spValue
	 * @param fontScale
	 *            ��DisplayMetrics��������scaledDensity��
	 * @return
	 */
	public static int sp2px(Context context, float spValue)
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (spValue * scale + 0.5f);
	}
}
