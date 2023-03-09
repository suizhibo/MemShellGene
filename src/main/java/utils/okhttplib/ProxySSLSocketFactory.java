package utils.okhttplib;

import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;


public class ProxySSLSocketFactory extends SSLSocketFactory
{
	private ProxyConfig proxyConfig;
	private SSLSocketFactory socketFactory;
	
	public ProxySSLSocketFactory(ProxyConfig proxyConfig, SSLSocketFactory socketFactory)
	{
		this.proxyConfig = proxyConfig;
		this.socketFactory = socketFactory;
	}
	
	@Override
	public String[] getDefaultCipherSuites()
	{
		return socketFactory.getDefaultCipherSuites();
	}
	
	@Override
	public String[] getSupportedCipherSuites()
	{
		return socketFactory.getSupportedCipherSuites();
	}
	
	@Override
	public Socket createSocket() throws IOException
	{
		Proxy proxy = proxyConfig.getProxy();
		if (proxy != null)
		{
			return new Socket(proxy);
		}
		else
		{
			return new Socket();
		}
	}
	
	@Override
	public Socket createSocket(String host, int port) throws IOException
	{
		Socket socket = createSocket();
		try
		{
			return socketFactory.createSocket(socket, host, port, true);
		}
		catch (IOException e)
		{
			socket.close();
			throw e;
		}
	}
	
	@Override
	public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException
	{
		// ��Ϊ�������һ���Ѿ����õ�socket�������޷�����
		return socketFactory.createSocket(s, host, port, autoClose);
	}
	
	@Override
	public Socket createSocket(InetAddress address, int port) throws IOException
	{
		Socket socket = createSocket();
		try
		{
			return socketFactory.createSocket(socket, address.getHostAddress(), port, true);
		}
		catch (IOException e)
		{
			socket.close();
			throw e;
		}
	}
	
	@Override
	public Socket createSocket(String host, int port, InetAddress clientAddress, int clientPort) throws IOException
	{
		Socket socket = createSocket();
		try
		{
			socket.bind(new InetSocketAddress(clientAddress, clientPort));
			return socketFactory.createSocket(socket, host, port, true);
		}
		catch (IOException e)
		{
			socket.close();
			throw e;
		}
	}
	
	@Override
	public Socket createSocket(InetAddress address, int port, InetAddress clientAddress, int clientPort) throws IOException
	{
		Socket socket = createSocket();
		try
		{
			socket.bind(new InetSocketAddress(clientAddress, clientPort));
			return socketFactory.createSocket(socket, address.getHostAddress(), port, true);
		}
		catch (IOException e)
		{
			socket.close();
			throw e;
		}
	}
}