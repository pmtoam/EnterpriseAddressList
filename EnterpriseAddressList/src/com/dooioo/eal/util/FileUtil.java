package com.dooioo.eal.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

import com.dooioo.eal.dao.DBHelper;

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
		final int BUFFER = 4096;
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
	 * @return
	 */
	public static String getRootDir(Context context)
	{
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
		{
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		}
		else
		{
			return context.getCacheDir().getAbsolutePath();
		}
	}

	public static final String DATABASE_FILENAME = "contact.db";
	public static final String CACHE_DIR_NAME = "dooioo";
	public static String database_path;
	static String filePath = "data/data/com.dooioo.enterprise.address.list/"
			+ DATABASE_FILENAME;
	static String pathStr = "data/data/com.dooioo.enterprise.address.list";

	public static SQLiteDatabase openDatabase(Context context)
	{
		File jhPath = new File(filePath);
		if (jhPath.exists())
		{
			return SQLiteDatabase.openOrCreateDatabase(jhPath,
					DBHelper.SECRET_KEY, null);
		}
		else
		{
			File path = new File(pathStr);
			if (path.mkdir())
				Logger.e(TAG, "path.mkdir() == true");
			else
				Logger.e(TAG, "path.mkdir() == false");

			try
			{
				AssetManager am = context.getAssets();
				InputStream is = am.open(DATABASE_FILENAME);
				FileOutputStream fos = new FileOutputStream(jhPath);
				byte[] buffer = new byte[1024];
				int count = 0;
				while ((count = is.read(buffer)) > 0)
				{
					fos.write(buffer, 0, count);
				}
				fos.flush();
				fos.close();
				is.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return null;
			}
			return openDatabase(context);
		}
	}

	/**
	 * 复制单个文件
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf.txt
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf.txt
	 * @return boolean
	 */
	public static void copyFile(String oldPath, String newPath)
	{
		try
		{
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			File newfile = new File(newPath);
			if (!newfile.exists())
			{
				newfile.createNewFile();
			}
			if (oldfile.exists())
			{ // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ((byteread = inStream.read(buffer)) != -1)
				{
					bytesum += byteread; // 字节数 文件大小
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		}
		catch (Exception e)
		{
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();

		}

	}

	/**
	 * 复制整个文件夹内容
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf/ff
	 * @return boolean
	 */
	public void copyFolder(String oldPath, String newPath)
	{

		try
		{
			(new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++)
			{
				if (oldPath.endsWith(File.separator))
				{
					temp = new File(oldPath + file[i]);
				}
				else
				{
					temp = new File(oldPath + File.separator + file[i]);
				}

				if (temp.isFile())
				{
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath
							+ "/" + (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1)
					{
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}

				if (temp.isDirectory())
				{
					// 如果是子文件夹
					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}
}
