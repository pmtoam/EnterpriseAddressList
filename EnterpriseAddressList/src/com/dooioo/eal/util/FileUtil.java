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
	 * ���Ƶ����ļ�
	 * 
	 * @param oldPath
	 *            String ԭ�ļ�·�� �磺c:/fqf.txt
	 * @param newPath
	 *            String ���ƺ�·�� �磺f:/fqf.txt
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
			{ // �ļ�����ʱ
				InputStream inStream = new FileInputStream(oldPath); // ����ԭ�ļ�
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ((byteread = inStream.read(buffer)) != -1)
				{
					bytesum += byteread; // �ֽ��� �ļ���С
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		}
		catch (Exception e)
		{
			System.out.println("���Ƶ����ļ���������");
			e.printStackTrace();

		}

	}

	/**
	 * ���������ļ�������
	 * 
	 * @param oldPath
	 *            String ԭ�ļ�·�� �磺c:/fqf
	 * @param newPath
	 *            String ���ƺ�·�� �磺f:/fqf/ff
	 * @return boolean
	 */
	public void copyFolder(String oldPath, String newPath)
	{

		try
		{
			(new File(newPath)).mkdirs(); // ����ļ��в����� �������ļ���
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
					// ��������ļ���
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
