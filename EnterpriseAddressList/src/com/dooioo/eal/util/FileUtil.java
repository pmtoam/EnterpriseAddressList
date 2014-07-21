package com.dooioo.eal.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class FileUtil
{
	private final static String TAG = "FileUtil";

	/**
	 * 
	 * @param zipFile
	 *            path.getAbsolutePath() + File.separator + "TestUnzip.zip"
	 * @param targetDir
	 *            path.getAbsolutePath() + File.separator + "Dooioo2" +
	 *            File.separator
	 */
	public static void unZip(String zipFile, String targetDir)
	{
		final int BUFFER = 4096; // ���ﻺ��������ʹ��4KB��
		String strEntry; // ����ÿ��zip����Ŀ����

		try
		{
			BufferedOutputStream dest = null; // ���������
			FileInputStream fis = new FileInputStream(zipFile);
			ZipInputStream zis = new ZipInputStream(
					new BufferedInputStream(fis));
			ZipEntry entry; // ÿ��zip��Ŀ��ʵ��

			while ((entry = zis.getNextEntry()) != null)
			{

				try
				{
					Logger.e(TAG, "entry = " + entry);
					int count;
					byte data[] = new byte[BUFFER];
					strEntry = entry.getName();

					File entryFile = new File(targetDir + strEntry);
					File entryDir = new File(entryFile.getParent());
					if (!entryDir.exists())
					{
						entryDir.mkdirs();
					}

					FileOutputStream fos = new FileOutputStream(entryFile);
					dest = new BufferedOutputStream(fos, BUFFER);
					while ((count = zis.read(data, 0, BUFFER)) != -1)
					{
						dest.write(data, 0, count);
						Logger.e(TAG, "unziping... count = " + count);
					}
					dest.flush();
					dest.close();
					Logger.e(TAG, "unziping... dest.close();");
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
			zis.close();
			Logger.e(TAG, "unziping... zis.close();");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		Logger.e(TAG, "unziping... finish.");
	}

	/**
	 * ��ȡ��Ŀ¼(Ӧ��cache ���� SD��) <br>
	 * <br>
	 * ���Ȼ�ȡSD����Ŀ¼[/storage/sdcard0] <br>
	 * <br>
	 * Ӧ�û���Ŀ¼[/data/data/Ӧ�ð���/cache] <br>
	 * 
	 * @param context
	 *            ������
	 * @return
	 */
	public static String getRootDir(Context context)
	{
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
		{
			// ���Ȼ�ȡSD����Ŀ¼[/storage/sdcard0]
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		}
		else
		{
			// Ӧ�û���Ŀ¼[/data/data/Ӧ�ð���/cache]
			return context.getCacheDir().getAbsolutePath();
		}
	}

	private static SQLiteDatabase database;
	public static final String DATABASE_FILENAME = "dooiooAddressList.db3"; // �����DB�ļ�����
	public static final String CACHE_DIR_NAME = "dooioo3";
	public static String database_path;

	public static SQLiteDatabase openDatabase(Context context)
	{
		database_path = FileUtil.getRootDir(context);

		try
		{
			String databaseFilename = database_path + File.separator
					+ DATABASE_FILENAME;
			File dir = new File(database_path);
			if (!dir.exists())
			{
				dir.mkdir();
			}
			if (!(new File(databaseFilename)).exists())
			{

				AssetManager assetManager = context.getAssets();
				InputStream is = assetManager.open(DATABASE_FILENAME);
				FileOutputStream fos = new FileOutputStream(databaseFilename);
				byte[] buffer = new byte[8192];
				int count = 0;
				while ((count = is.read(buffer)) > 0)
				{
					fos.write(buffer, 0, count);
				}

				fos.close();
				is.close();
			}
			database = SQLiteDatabase.openOrCreateDatabase(databaseFilename,
					null);
			return database;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
