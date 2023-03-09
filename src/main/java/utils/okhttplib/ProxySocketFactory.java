package utils.okhttplib;

import javax.net.SocketFactory;
import java.io.IOException;
import java.net.*;


public class ProxySocketFactory extends SocketFactory
{
	private ProxyConfig proxyConfig;
	
	public ProxySocketFactory(ProxyConfig proxyConfig)
	{
		this.proxyConfig = proxyConfig;
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
	public Socket createSocket(String host, int port) throws IOException, UnknownHostException
	{
		Socket socket = createSocket();
		try
		{
			socket.connect(new InetSocketAddress(host, port));
		}
		catch (IOException e)
		{
			socket.close();
			throw e;
		}
		return socket;
	}
	
	@Override
	public Socket createSocket(InetAddress address, int port) throws IOException
	{
		Socket socket = createSocket();
		try
		{
			socket.connect(new InetSocketAddress(address, port));
		}
		catch (IOException e)
		{
			socket.close();
			throw e;
		}
		return socket;
	}
	
	@Override
	public Socket createSocket(String host, int port, InetAddress clientAddress, int clientPort)
		throws IOException, UnknownHostException
	{
		Socket socket = createSocket();
		try
		{
			socket.bind(new InetSocketAddress(clientAddress, clientPort));
			socket.connect(new InetSocketAddress(host, port));
		}
		catch (IOException e)
		{
			socket.close();
			throw e;
		}
		return socket;
	}
	
	@Override
	public Socket createSocket(InetAddress address, int port, InetAddress clientAddress, int clientPort) throws IOException
	{
		Socket socket = createSocket();
		try
		{
			socket.bind(new InetSocketAddress(clientAddress, clientPort));
			socket.connect(new InetSocketAddress(address, port));
		}
		catch (IOException e)
		{
			socket.close();
			throw e;
		}
		return socket;
	}
	
	

}
