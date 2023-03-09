package utils.okhttplib;

import okhttp3.Interceptor;
import okhttp3.Response;

import java.io.IOException;


public class OkHttpProxyInterceptor implements Interceptor
{
	private ProxyConfig proxyConfig;
	private ThreadLocalProxyAuthenticator authenticator = ThreadLocalProxyAuthenticator.getInstance();
	
	public OkHttpProxyInterceptor(ProxyConfig proxyConfig)
	{
		this.proxyConfig = proxyConfig;
	}
	
	@Override
	public Response intercept(Chain chain) throws IOException
	{
		boolean clearCredentials = false;
		if (proxyConfig != null)
		{
			if (proxyConfig.getAuthentication() != null)
			{
				authenticator.setCredentials(proxyConfig.getAuthentication());
				clearCredentials = true;
			}
		}
		
		try
		{
			return chain.proceed(chain.request());
		}
		finally
		{
			if (clearCredentials)
			{
				ThreadLocalProxyAuthenticator.clearCredentials();
			}
		}
	}
}
