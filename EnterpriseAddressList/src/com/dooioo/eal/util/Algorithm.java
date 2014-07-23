package com.dooioo.eal.util;

import java.security.MessageDigest;
import java.util.Random;

import android.text.TextUtils;
import android.util.Log;

import com.dooioo.eal.activity.MyApplication;

/**
 * 加解密算�? *
 * 
 * @author yuli
 * 
 */
public class Algorithm
{

	private static final int RANDOM_SIZE = 1;
	private static int INDEX = 0;
	private static String[] numarr =
	{ "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
	private static String final_key = "";
	private final static int INITNUM = -1;
	private static String[] key1 =
	{ "f9f35621-5e3d-48b0-8e51-05d7df99df1a",
			"876a31bc-b7c9-4086-b22d-ede2c275d20b",
			"04091899-4160-4b79-91a6-929ded5808d8",
			"ad2e5216-4230-49f7-b69b-8682b9b9eaf7",
			"3fc9dbb3-a1d7-4932-98c4-e76ea17b498e",
			"43e4db7c-61d1-4acd-ad17-d9878e024b65",
			"18ec2628-4f50-48df-a93e-eca34c22fb47",
			"6c00c959-7436-40ad-a82c-5734f11034e2",
			"cde721ea-d475-49c7-862b-8944e6b30668",
			"dcc8d729-abd1-4b42-ba6a-01ca9bbae0cd",
			"9b9ba755-6d38-41ba-8007-eda8806f93d2",
			"bf9c85a9-9917-4f1e-b746-c796f802eb47",
			"466fc5a6-3301-4716-84bd-fc0d7ccb5a73",
			"e94d4748-6a65-4bb8-a91a-17400b17cfcf",
			"fe8a339c-7d35-4337-8813-e2685151065b",
			"0c399472-24ec-4743-8a6c-027200336b14",
			"69adbfc9-b79c-4efb-acac-fd861a2f0ad8",
			"247820c8-c41a-4362-b5dd-0e8e9574179d",
			"694a1b44-e593-444e-a3a1-417753dbad15",
			"e652d52b-f892-4e96-904e-d362c538c1b5",
			"fa3073b6-ae9e-43a9-b975-219bdbda3dd1",
			"ef0d26b1-70c5-4a2b-a9e3-5c27343a27f6",
			"a8befe3a-a509-4da3-a288-4a5531b6d024",
			"be3b3f03-666c-48c0-b016-b439107e1ced",
			"2c8e9a13-e720-447d-bb23-1c242142868a",
			"430deecf-6cec-4cd6-b9b4-b859219b07c7",
			"c1d12898-84bd-48ab-ae92-465edfe85149",
			"50f7dce9-a91b-4c47-b372-2479dabef1f4",
			"4e13277a-8c3b-4a7b-91d1-712be300b639",
			"4b2e7ff4-0050-4a0e-94ca-a9326a2e9fb6",
			"a580b410-ef91-47dd-9e18-1bb7f2ab9e54",
			"d17fbd37-a047-40f9-9d16-0868fa9ec57a",
			"04caad1e-3757-4efc-b88a-4dd93d2b69c0",
			"5854275f-d60b-4eea-b669-d4de6cbee9f4",
			"9dea8d14-c346-42b1-b7a2-ec8c7df85744",
			"d37f97a2-bdc8-41c8-837f-d38d24eb2af7",
			"dcb3aeeb-a24f-4fd6-aa3c-7b670da27324",
			"b7fd2bf4-7381-491b-afb5-008c9cb4edc8",
			"f8d15abc-4ecd-4b9a-9d18-b9ad48058c45",
			"0d0e4c8d-8198-4ca1-90b0-a21a66284ce9",
			"ee145ae0-5854-4123-ae61-c1d716808adf",
			"21314407-97e1-476a-a4b3-57154aafcea3",
			"713fe073-35e6-441c-bf11-de47267b8b16",
			"98debd9a-c064-4475-8dab-80e978832ed3",
			"5b630e6d-ef4f-40ed-b7f5-1f87d1bff28f",
			"ac72bc5a-5a41-4852-8e79-150e17478022",
			"affacade-3b87-4210-9dbd-30491b328f89",
			"885ef380-a95c-497a-8d07-e7aa8f5e15f7",
			"4ed1186f-f852-4253-b51c-4738d20a5dfa",
			"2a449760-98f7-44f2-b5ca-64c35c46348e",
			"7fe539ba-81b3-4625-a161-5fef7d4409a8",
			"335f304c-0073-4267-97dd-5af3e4868461",
			"1a3c9623-2db5-4cf3-ac0a-6819797954cc",
			"6de11423-26a1-46af-a0cc-0f21b08d9837",
			"d1204065-6249-4786-b428-94fa575bab6b",
			"5c6303ee-c112-4ec7-8415-36c1d3fb07f2",
			"19209161-66bc-4dfe-84dc-199d615ba661",
			"4a56b01e-db9b-4a71-a9c2-4e697b5a6d6e",
			"573572ff-bb34-4bf4-bcc1-46aa26f4cbec",
			"f7308cb1-28a6-4c21-8238-0013e9ae29d5",
			"c4a04045-2ab4-4367-a994-5b79339d27e4",
			"650a73c7-81f6-4250-92a2-c9ccbf1d0ef0",
			"828a4a06-3ffb-4d65-9ab2-320693e52609",
			"64c7f11e-2626-4358-91ed-c2049ca231b7",
			"e9b20047-24f7-449e-b358-420232b72171",
			"4117c527-bfd1-4306-862e-1ccc93fccfdb",
			"689841e5-bc48-48f6-ba42-f0df84aef5cf",
			"c7afc79b-4549-469b-aaf5-dd141e556c69",
			"ebccf984-03bd-4fb6-8d86-b817a359e670",
			"640ec997-2190-43f1-ace3-a6c18e7c9067",
			"e62618c8-8503-4b56-830f-2fd52eb53007",
			"74cab040-869e-45da-bff3-51ebc280d82a",
			"8f6c4fc4-0832-4340-bc83-2c4b691bd95f",
			"f4357bfa-1aed-4572-9069-c058db147f4b",
			"56825c8f-871a-4d3e-b64e-d31064866d26",
			"66c0434b-6360-4f49-9ba1-910172d92a6d",
			"e21f5d61-6d39-4d60-97ee-3653cc6975e9",
			"8492301a-fdfd-41f8-9bea-012be6588369",
			"64c2dd21-8ded-4bdc-ba6c-7a7f36030e8f",
			"1f0271c7-9bbd-457c-8536-0c5541712f6b",
			"80c32787-8b8c-4db8-9553-897e32b36ffc",
			"c2cbfaa2-12c1-47f1-9686-93f16ea5586c",
			"8b01a4b0-37ea-4352-a231-efe643d33e3e",
			"13fda412-e4e1-4702-ab10-af69a6171ad9",
			"632b857a-e3d5-4c39-a4b9-fa2b762999ae",
			"ec4d0d3a-9c32-42b7-96b5-a844042a722e",
			"f19bc26d-1baf-4121-b875-f221ee8ba600",
			"c3675ea9-b134-4c2e-908a-cc7aa78c2ddc",
			"155002f5-3da6-4e67-b55d-afd56dc0baba",
			"e3b41c72-b998-4c03-a947-187229cab00c",
			"1b4ac93b-1f28-4753-9227-da025092aeb0",
			"91377e58-8ece-4d18-bafb-02eeb6bf0dcc",
			"dc8a676a-0b40-4047-ac91-ad3e2066e997",
			"7baa98af-ae16-4be7-89c1-dc05ce538000",
			"6f5387f6-3a58-42bd-9075-5c5f3b8f1d58",
			"ec1551c8-958f-4255-9015-bd3886794611",
			"3f44cebf-b041-4302-84a1-2a0b2b2ba2cc",
			"d4003629-feb6-4790-bd53-a0c88a6c09c8",
			"035eaa0f-050e-4507-adfe-b53ee31f1e90" };

	private static void init(String Imei, String SimNum)
	{
		final_key = "";
		Random rnd = new Random();
		if (INDEX == 0)
		{
			INDEX = rnd.nextInt(RANDOM_SIZE);
		}

		String key = Imei + SimNum + key1[INDEX];
		String keycode = MD5(key);

		for (int j = 0; j < keycode.length(); j++)
		{
			Character code = keycode.charAt(j);
			int codenum = code.hashCode() - 48;
			final_key += numFormat(codenum);
		}

	}

	/**
	 * using default imei and imsi for encryption
	 * 
	 * @param tel
	 *            the phone number which should be encryption
	 * @return the encrypted phone number
	 */
	public static String encryption(String tel)
	{

		init(MyApplication.getImei(), MyApplication.getImsi());
		String encrynumber = "";
		try
		{
			for (int i = 0, j = 0; i < tel.length(); i++, j = j + 2)
			{
				Character code0 = final_key.charAt(j);
				Character code1 = final_key.charAt(j + 1);
				Character telcode = tel.charAt(i);

				int telindex = INITNUM;
				telindex = isNumber(telcode.toString(), numarr, telindex);
				if (telindex == INITNUM)
				{
					encrynumber += telcode.toString();
					continue;
				}

				int telcodenum = Integer.valueOf(code0.toString())
						+ Integer.valueOf(code1.toString()) + telindex;
				encrynumber += numFormat(telcodenum);
			}

			encrynumber += numFormat(INDEX);

			Random rnd = new Random();
			int randomvalue = rnd.nextInt(RANDOM_SIZE);
			encrynumber += numFormat(randomvalue);
			encrynumber += "0";
			encrynumber += "1";
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return encrynumber;

	}

	/**
	 * encrypt string using outer imei and imsi
	 * 
	 * @param tel
	 * @param imei
	 * @param imsi
	 * @return
	 */
	public static String encryption(String tel, String imei, String imsi)
	{
		if (TextUtils.isEmpty(imei))
		{
			throw new IllegalArgumentException("Args imei is empty or null!");
		}
		imsi = TextUtils.isEmpty(imsi) ? "" : imsi;

		init(imei, imsi);
		Log.e("yhb", "final_key3->" + final_key);
		String encrynumber = "";
		try
		{
			for (int i = 0, j = 0; i < tel.length(); i++, j = j + 2)
			{
				Character code0 = final_key.charAt(j);
				Character code1 = final_key.charAt(j + 1);
				Character telcode = tel.charAt(i);

				int telindex = INITNUM;
				telindex = isNumber(telcode.toString(), numarr, telindex);
				if (telindex == INITNUM)
				{
					encrynumber += telcode.toString();
					continue;
				}

				int telcodenum = Integer.valueOf(code0.toString())
						+ Integer.valueOf(code1.toString()) + telindex;
				encrynumber += numFormat(telcodenum);
			}

			encrynumber += numFormat(INDEX);

			Random rnd = new Random();
			int randomvalue = rnd.nextInt(RANDOM_SIZE);
			encrynumber += numFormat(randomvalue);
			encrynumber += "0";
			encrynumber += "1";
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return encrynumber;
	}

	/**
	 * using default imei and imsi for decryption.calculate the real string
	 * before it encrypted
	 * 
	 * @param number
	 * @return the real string
	 */
	public static String decryption(String number)
	{
		if (TextUtils.isEmpty(number))
		{
			throw new IllegalArgumentException("Args number is empty or null");
		}
		String Imsi = MyApplication.getImsi();
		String Imei = MyApplication.getImei();
		String realnumber = "";
		try
		{
			final_key = "";
			if (!number.contains(","))
			{
				realnumber = "";
				number = number.substring(0, number.length() - 1);
				int numbersize = Integer.valueOf(number.substring(
						number.length() - 1, number.length()));
				number = number.substring(0, number.length() - numbersize - 1);
				int index = Integer.valueOf(number.substring(
						number.length() - 4, number.length() - 2));

				String key = Imei + Imsi + key1[index];
				String keycode = MD5(key);

				for (int j = 0; j < keycode.length(); j++)
				{
					Character code = keycode.charAt(j);
					int codenum = code.hashCode() - 48;
					final_key += numFormat(codenum);
				}
				for (int i = 0, j = 0; i < number.length() - 4; j = j + 2)
				{

					Character num = number.charAt(i);

					int m = INITNUM;
					m = isNumber(num.toString(), numarr, m);
					if (m == INITNUM)
					{
						realnumber += num.toString();
						i++;
						continue;
					}
					else
					{
						Character code0 = final_key.charAt(j);
						Character code1 = final_key.charAt(j + 1);
						String telcode = number.substring(i, i + 2);
						int telcodenum = Integer.valueOf(telcode)
								- Integer.valueOf(code0.toString())
								- Integer.valueOf(code1.toString());
						realnumber += telcodenum;
						i = i + 2;
					}

				}
			}
			else
			{
				realnumber = number;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return realnumber;
	}

	/**
	 * calculate the real string before it encrypted
	 * 
	 * @param number
	 * @param imei
	 * @param imsi
	 * @return real string
	 */
	public static String decryption(String number, String imei, String imsi)
	{
		if (TextUtils.isEmpty(number))
		{
			throw new IllegalArgumentException("Args number is empty or null");
		}
		if (TextUtils.isEmpty(imei))
		{
			throw new IllegalArgumentException("Args imei is empty or null");
		}
		String Imsi = TextUtils.isEmpty(imsi) ? "" : imsi;
		String Imei = imei;
		String realnumber = "";
		try
		{
			final_key = "";
			if (!number.contains(","))
			{
				realnumber = "";
				number = number.substring(0, number.length() - 1);
				int numbersize = Integer.valueOf(number.substring(
						number.length() - 1, number.length()));
				number = number.substring(0, number.length() - numbersize - 1);
				int index = Integer.valueOf(number.substring(
						number.length() - 4, number.length() - 2));

				String key = Imei + Imsi + key1[index];
				String keycode = MD5(key);

				for (int j = 0; j < keycode.length(); j++)
				{
					Character code = keycode.charAt(j);
					int codenum = code.hashCode() - 48;
					final_key += numFormat(codenum);
				}
				for (int i = 0, j = 0; i < number.length() - 4; j = j + 2)
				{

					Character num = number.charAt(i);

					int m = INITNUM;
					m = isNumber(num.toString(), numarr, m);
					if (m == INITNUM)
					{
						realnumber += num.toString();
						i++;
						continue;
					}
					else
					{
						Character code0 = final_key.charAt(j);
						Character code1 = final_key.charAt(j + 1);
						String telcode = number.substring(i, i + 2);
						int telcodenum = Integer.valueOf(telcode)
								- Integer.valueOf(code0.toString())
								- Integer.valueOf(code1.toString());
						realnumber += telcodenum;
						i = i + 2;
					}

				}
			}
			else
			{
				realnumber = number;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return realnumber;
	}

	public static String cutString(String uinumber)
	{
		if (uinumber.contains("+86"))
		{
			uinumber = uinumber.substring(3, uinumber.length());
		}

		return uinumber;
	}

	public static String editString(String realnumber)
	{
		if (TextUtils.isEmpty(realnumber))
		{
			return "";
		}

		int length = realnumber.length();
		if (length > 7)
		{
			realnumber = realnumber.substring(0, (length - 7)) + "****"
					+ realnumber.substring((length - 3), length);
		}

		return realnumber;
	}

	public static int isNumber(String code, String[] arr, int m)
	{
		for (int i = 0; i < arr.length; i++)
		{
			if (arr[i].equals(code))
			{
				m = i;
				break;
			}
		}

		return m;
	}

	private static String numFormat(int num)
	{
		if (num < 10)
			return "0" + num;
		else
			return "" + num;
	}

	private static String MD5(String inStr)
	{
		MessageDigest md5 = null;
		try
		{
			md5 = MessageDigest.getInstance("MD5");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];

		byte[] md5Bytes = md5.digest(byteArray);

		StringBuffer hexValue = new StringBuffer();

		for (int i = 0; i < md5Bytes.length; i++)
		{
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}

		return hexValue.toString();
	}
}
