package utils.okhttplib;

import java.net.Authenticator;
import java.net.PasswordAuthentication;


public class ThreadLocalProxyAuthenticator extends Authenticator
{
	private ThreadLocal<PasswordAuthentication> credentials = null;
	private static class SingletonHolder
	{
		private static final ThreadLocalProxyAuthenticator instance = new ThreadLocalProxyAuthenticator();
	}
	
	public static final ThreadLocalProxyAuthenticator getInstance()
	{
		return SingletonHolder.instance;
	}
	
	public void setCredentials(PasswordAuthentication passAuth)
	{
		credentials.set(passAuth);
	}
	
	public static void clearCredentials()
	{
		ThreadLocalProxyAuthenticator authenticator = ThreadLocalProxyAuthenticator.getInstance();
		Authenticator.setDefault(authenticator);
		authenticator.credentials.set(null);
	}
	
	@Override
	public PasswordAuthentication getPasswordAuthentication()
	{
		return credentials.get();
	}
}
