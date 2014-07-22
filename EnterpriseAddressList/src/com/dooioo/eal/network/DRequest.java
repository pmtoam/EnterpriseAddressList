package com.dooioo.eal.network;

/**
 * Network request callback interface.
 * 
 * @author PMTOAM
 * 
 * @param <T> generic types
 */
public interface DRequest<T>
{
	/**
	 * Used in the UI thread.
	 * 
	 * @param resp
	 * @param exception
	 */
	public void showResult(T resp, Exception exception);
}
