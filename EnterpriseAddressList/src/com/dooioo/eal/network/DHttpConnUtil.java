package com.dooioo.eal.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.dooioo.eal.util.Logger;
import com.google.gson.Gson;

public class DHttpConnUtil
{

	private static final String TAG = "HttpConnUtil";

	// public static int TIME_OUT = 20000;

	// private static void setTimeout(HttpURLConnection conn)
	// {
	// int s = 20000;
	// conn.setConnectTimeout(s);
	// conn.setReadTimeout(s);
	// }

	public static String encodeParam(Map<String, String> params)
	{
		if (params == null)
			return "";

		StringBuilder sb = new StringBuilder();

		for (String key : params.keySet())
		{
			Object val = params.get(key);
			if (sb.length() != 0)
				sb.append("&");

			if (val instanceof Long[])
			{
				Long[] longval = (Long[]) val;

				try
				{
					for (int i = 0; i < longval.length; i++)
					{
						String valStr = (val == null) ? "" : URLEncoder.encode(
								longval[i].toString(), "utf-8");

						if (i != longval.length - 1)
							sb.append(key + "=" + valStr + "&");
						else
							sb.append(key + "=" + valStr);
					}

				}
				catch (UnsupportedEncodingException e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				try
				{
					String valStr = (val == null) ? "" : URLEncoder.encode(
							val.toString(), "utf-8");
					sb.append(key + "=" + valStr);
				}
				catch (UnsupportedEncodingException e)
				{
					e.printStackTrace();
				}
			}

		}
		return sb.toString();
	}

	public static <T> T request(String method, String url, String encodeParam,
			Type type) throws Exception
	{
		if (method.equals("POST"))
			return excutePost(url, encodeParam, type);
		else if (method.equals("GET"))
		{
			throw new RuntimeException("method.equals(\"GET\")");
		}
		else
			throw new RuntimeException("Only support post and get.");
	}

	private static <T> T excutePost(String targetURL, String encodeParam,
			Type type) throws Exception
	{
		Logger.e(TAG, "(0)--START----------------------------");
		Logger.e(TAG, "--> targetURL = " + targetURL);
		Logger.e(TAG, "--> encodeParam = " + encodeParam);

		HttpURLConnection conn = null;
		InputStream is = null;
		DataOutputStream wr = null;

		URL url = new URL(targetURL);
		conn = (HttpURLConnection) url.openConnection();
		// setTimeout(conn);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Connection", "Keep-Alive");
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded;charset=UTF-8");
		// conn.setRequestProperty("Content-Type",
		// ("application/xml; charset=utf-8").replaceAll("\\s", ""));
		// conn.setRequestProperty("Content-Type", "application/octet-stream;");
		conn.setRequestProperty("Content-Length",
				"" + Integer.toString(encodeParam.getBytes().length));
		conn.setUseCaches(false);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		// conn.connect();
		wr = new DataOutputStream(conn.getOutputStream());
		wr.writeBytes(encodeParam);
		wr.flush();
		wr.close();

		int code = conn.getResponseCode();
		Logger.e(TAG, "(1)--code = " + code);

		is = conn.getInputStream();
		String txt = readInputToString(is);
		is.close();
		conn.disconnect();

		Logger.e(TAG, "(2)--text = " + txt);
		Logger.e(TAG, "(3)--END------------------------------");

		Gson gson = new Gson();
		T result = gson.fromJson(txt, type);
		return result;
	}

	private static String readInputToString(InputStream is) throws Exception
	{
		BufferedReader bd = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		char[] cbuf = new char[1024];
		int ch = -1;
		int _count = 0;
		while ((ch = bd.read(cbuf)) >= 0)
		{
			sb.append(cbuf, 0, ch);
			_count += ch;
			Logger.e(TAG, "--> downloading _count = " + _count);
		}
		bd.close();
		return sb.toString();
	}

	public static <T> T requestSecurity(String method, String url,
			Map<String, String> headersMap, Map<String, String> params,
			Type type) throws Exception
	{
		if (method.equals("POST"))
		{
			return postSecurity(url, headersMap, params, type);
		}
		else if (method.equals("GET"))
		{
			return getSecurity(url, headersMap, params, type);
		}
		else
		{
			throw new RuntimeException("Only support post and get.");
		}
	}

	private static <T> T postSecurity(String url,
			Map<String, String> headersMap, Map<String, String> params,
			Type type) throws Exception
	{
		HttpPost httpPost = new HttpPost(url);
		Logger.e(TAG, "(post_S0)--url = " + url);
		if (headersMap != null && headersMap.size() > 0)
		{
			for (Entry<String, String> entry : headersMap.entrySet())
			{
				String key = entry.getKey().trim();
				String v = entry.getValue().trim();
				if (key.equals("") || v.equals(""))
					continue;
				httpPost.addHeader(key, v);
			}
		}

		List<NameValuePair> _params = new ArrayList<NameValuePair>();
		for (Entry<String, String> entry : params.entrySet())
		{
			BasicNameValuePair valuesPair = new BasicNameValuePair(
					entry.getKey(), entry.getValue());
			_params.add(valuesPair);
		}

		httpPost.setEntity(new UrlEncodedFormEntity(_params, "UTF-8"));

		HttpParams httpParams = new BasicHttpParams();
		// ConnManagerParams.setTimeout(httpParams, TIME_OUT);
		// HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
		// HttpConnectionParams.setSoTimeout(httpParams, TIME_OUT);
		SchemeRegistry schreg = new SchemeRegistry();
		schreg.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		schreg.register(new Scheme("https",
				SSLSocketFactory.getSocketFactory(), 443));
		ClientConnectionManager conman = new ThreadSafeClientConnManager(
				httpParams, schreg);
		HttpClient httpClient = new DefaultHttpClient(conman, httpParams);
		HttpResponse response = httpClient.execute(httpPost);

		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
		{
			String txt = EntityUtils.toString(response.getEntity()).trim();
			Logger.e(TAG, "(post_S2)--text = " + txt);
			Logger.e(TAG, "(post_S3)--END------------------------------");
			Gson gson = new Gson();
			T result = null;

			try
			{
				result = gson.fromJson(txt, type);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				try
				{
					result = gson.fromJson(txt.replace("\"\"", "[]"), type);
				}
				catch (Exception e1)
				{
					e1.printStackTrace();
					result = gson.fromJson(
							txt.replace(",\"propertyDescVoteList\":[]", ""),
							type);
				}
			}

			return result;
		}
		httpPost.abort();

		return null;
	}

	private static <T> T getSecurity(String url,
			Map<String, String> headerParams, Map<String, String> params,
			Type type) throws Exception
	{
		if (params != null)
		{
			StringBuffer buffer = new StringBuffer();
			int i = 0;
			for (Entry<String, String> entry : params.entrySet())
			{
				buffer.append(entry.getKey());
				buffer.append("=");
				buffer.append(URLEncoder.encode(entry.getValue(), "utf-8"));
				if (i != params.size() - 1)
				{
					buffer.append("&");
				}
			}

			if (url.indexOf("?") >= 0)
				url = url + "&" + buffer.toString();
			else
				url = url + "?" + buffer.toString();
		}

		HttpGet httpGet = new HttpGet(url);
		if (headerParams != null && headerParams.size() > 0)
		{
			for (Entry<String, String> entry : headerParams.entrySet())
			{
				String key = entry.getKey().trim();
				String v = String.valueOf(entry.getValue()).trim();
				if (key.equals("") || v.equals(""))
					continue;
				httpGet.addHeader(key, v);
			}
		}

		HttpParams httpParams = new BasicHttpParams();
		HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);
		HttpProtocolParams.setUseExpectContinue(httpParams, true);
		HttpProtocolParams
				.setUserAgent(
						httpParams,
						"Mozilla/5.0(Linux;U;Android;en-us;Nexus One Build.FRG83) "
								+ "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");
		// ConnManagerParams.setTimeout(httpParams, TIME_OUT);
		// HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
		// HttpConnectionParams.setSoTimeout(httpParams, TIME_OUT);
		SchemeRegistry schreg = new SchemeRegistry();
		schreg.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		schreg.register(new Scheme("https",
				SSLSocketFactory.getSocketFactory(), 443));
		ClientConnectionManager conman = new ThreadSafeClientConnManager(
				httpParams, schreg);
		HttpClient httpClient = new DefaultHttpClient(conman, httpParams);
		HttpResponse response = httpClient.execute(httpGet);

		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
		{
			String txt = EntityUtils.toString(response.getEntity()).trim();
			Logger.e(TAG, "(getS2)--text = " + txt);
			Logger.e(TAG, "(getS3)--END------------------------------");
			Gson gson = new Gson();
			T result = null;
			try
			{
				result = gson.fromJson(txt, type);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				txt = txt.replace("\"\"", "[]");
				result = gson.fromJson(txt, type);
			}
			return result;
		}
		httpGet.abort();

		return null;
	}

}
