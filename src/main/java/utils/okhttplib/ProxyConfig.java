package utils.okhttplib;

import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;


public class ProxyConfig 
{
	private String proxyType = null;
	private String proxyHost = null;
	private int proxyPort = 0;
	private String userName = null;
	private String passWord = null;

	public ProxyConfig(String proxyType, String proxyHost, int proxyPort)
	{
		this.proxyType = proxyType;
		this.proxyHost = proxyHost;
		this.proxyPort = proxyPort;		
	}
	
	public ProxyConfig(String proxyType, String proxyHost, int proxyPort, String userName, String passWord)
	{
		this.proxyType = proxyType;
		this.proxyHost = proxyHost;
		this.proxyPort = proxyPort;
		this.userName = userName;
		this.passWord = passWord;
	}
	
	public Proxy getProxy()
	{
		Proxy proxy = null;
		if (proxyType.equalsIgnoreCase("http"))
		{
			proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
		}
		if (proxyType.equalsIgnoreCase("socks5") || proxyType.equalsIgnoreCase("socks4"))
		{
			proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(proxyHost, proxyPort));
		}
		
		return proxy;
	}
	
	public String getProxyType()
	{
		return proxyType;
	}
	
	public String getUserName()
	{
		return userName;
	}
	
	public String getPassWord()
	{
		return passWord;
	}
	
	public PasswordAuthentication getAuthentication()
	{
		if (userName != null && passWord != null)
		{
			return new PasswordAuthentication(userName, passWord.toCharArray());
		}
		else
		{
			return null;
		}
	}
}
