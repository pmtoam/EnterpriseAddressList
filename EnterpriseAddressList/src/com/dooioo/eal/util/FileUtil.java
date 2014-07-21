package com.dooioo.eal.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
}
