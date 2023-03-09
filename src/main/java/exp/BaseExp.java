package exp;


import okhttp3.Response;
import utils.UserAgentUtil;
import utils.okhttplib.OkHttp;
import utils.okhttplib.ProxyConfig;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;


public abstract class BaseExp
{
    public String url = null;
    public int chunkCount = -1;
    public ProxyConfig proxyConfig = null;
    public HashMap<String, String> headers = new HashMap<String, String>();


    public String getStripUrl(String target){
        try {
            URL url = new URL(target);
            int port = url.getPort();
            return url.getProtocol() + "://" + url.getHost() + ":" + ((port == -1) ? "80" : String.valueOf(port));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public Response launch(String url, HashMap<String, String> headers) throws Exception {
        this.headers.put("User-Agent", UserAgentUtil.getRandomUserAgent());
        if (headers != null) {
            this.headers.putAll(headers);
        }
        if (this.proxyConfig != null) {
            return OkHttp.httpGet(url, this.headers, this.proxyConfig);
        }
        else {
            return OkHttp.httpGet(url, this.headers);
        }
    }

    public Response launch(String url, String body, String mime, HashMap<String, String> headers) throws Exception {
        this.headers.put("User-Agent", UserAgentUtil.getRandomUserAgent());
        if (headers != null) {
            this.headers.putAll(headers);
        }
        if (this.proxyConfig != null) {
            return OkHttp.httpPost(url, body, mime, this.headers, this.proxyConfig, this.chunkCount);
        }
        else {
            return OkHttp.httpPost(url, body, mime, this.headers, this.chunkCount);
        }
    }

    public Response launch(String url, byte[] body, String mime, HashMap<String, String> headers) throws Exception {
        this.headers.put("User-Agent", UserAgentUtil.getRandomUserAgent());
        if (headers != null) {
            this.headers.putAll(headers);
        }
        if (this.proxyConfig != null) {
            return OkHttp.httpPut(url, body, mime, this.headers, this.proxyConfig, this.chunkCount);
        }
        else {
            return OkHttp.httpPut(url, body, mime, this.headers, this.chunkCount);
        }
    }

    public Response launchPostByte(String url, byte[] body, String mime, HashMap<String, String> headers) throws Exception {
        this.headers.put("User-Agent", UserAgentUtil.getRandomUserAgent());
        if (headers != null) {
            this.headers.putAll(headers);
        }
        if (this.proxyConfig != null) {
            return OkHttp.httpPost(url, body, mime, this.headers, this.proxyConfig, this.chunkCount);
        } else {
            return OkHttp.httpPost(url, body, mime, this.headers, this.chunkCount);
        }
    }


}
