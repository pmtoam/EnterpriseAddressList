package com.dooioo.eal.network;

import java.util.Map;

public class DHttpRequest
{
	// Request method (POST|GET).
	public String method;
	// Target urlAddress.
	public String url;
	public Map<String, String> params;
	public Map<String, String> headersParams;

	/**
	 * 
	 * @param method
	 * @param url
	 * @param params
	 */
	public DHttpRequest(String method, String url, Map<String, String> params)
	{
		this.method = method;
		this.url = url;
		this.params = params;
	}

	/**
	 * 
	 * @param method
	 * @param url
	 * @param headersMap
	 * @param params
	 */
	public DHttpRequest(String method, String url,
			Map<String, String> headersMap, Map<String, String> params)
	{
		this.method = method;
		this.url = url;
		this.headersParams = headersMap;
		this.params = params;
	}
}
