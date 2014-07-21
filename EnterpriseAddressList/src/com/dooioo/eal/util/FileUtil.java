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
		final int BUFFER = 4096; // 这里缓冲区我们使用4KB，
		String strEntry; // 保存每个zip的条目名称

		try
		{
			BufferedOutputStream dest = null; // 缓冲输出流
			FileInputStream fis = new FileInputStream(zipFile);
			ZipInputStream zis = new ZipInputStream(
					new BufferedInputStream(fis));
			ZipEntry entry; // 每个zip条目的实例

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
	 * 获取根目录(应用cache 或者 SD卡) <br>
	 * <br>
	 * 优先获取SD卡根目录[/storage/sdcard0] <br>
	 * <br>
	 * 应用缓存目录[/data/data/应用包名/cache] <br>
	 * 
	 * @param context
	 *            上下文
	 * @return
	 */
	public static String getRootDir(Context context)
	{
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
		{
			// 优先获取SD卡根目录[/storage/sdcard0]
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		}
		else
		{
			// 应用缓存目录[/data/data/应用包名/cache]
			return context.getCacheDir().getAbsolutePath();
		}
	}

	private static SQLiteDatabase database;
	public static final String DATABASE_FILENAME = "dooiooAddressList.db3"; // 这个是DB文件名字
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
