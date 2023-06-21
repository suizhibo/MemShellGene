package utils.okhttplib;

import okhttp3.*;

import javax.net.ssl.*;
import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class OkHttp {
    public static final X509TrustManager NOP_TRUST_MANAGER = new X509TrustManager() {
        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) {
        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) {
        }

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[0];
        }
    };

    public static final HostnameVerifier NOP_HOSTNAME_VERIFIER = new HostnameVerifier() {
        @Override
        public boolean verify(String s, SSLSession sslSession) {
            return true;
        }
    };

    public static int DEFAULT_TIMEOUT = 10;
    public static SSLContext NOP_TLSV12_SSL_CONTEXT = null;

    static {
        try {
            NOP_TLSV12_SSL_CONTEXT = SSLContext.getInstance("TLSv1.2");
            NOP_TLSV12_SSL_CONTEXT.init(null, new TrustManager[]{(TrustManager) NOP_TRUST_MANAGER},
                    new java.security.SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    public static OkHttpClient buildOkHttpClient(ProxyConfig proxyConfig) {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .sslSocketFactory(new ProxySSLSocketFactory(proxyConfig, NOP_TLSV12_SSL_CONTEXT.getSocketFactory()), NOP_TRUST_MANAGER)
                .socketFactory(new ProxySocketFactory(proxyConfig))
                .hostnameVerifier(NOP_HOSTNAME_VERIFIER)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new OkHttpProxyInterceptor(proxyConfig))
                .followRedirects(false)
                .build();
        return httpClient;
    }

    public static Response httpGet(String url, HashMap<String, String> headers) throws Exception
    {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .followRedirects(false)
                .sslSocketFactory(NOP_TLSV12_SSL_CONTEXT.getSocketFactory(), NOP_TRUST_MANAGER)
                .hostnameVerifier(NOP_HOSTNAME_VERIFIER)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        Request.Builder requestBuilder = new Request.Builder();
        if (headers != null) {
            Iterator<Map.Entry<String, String>> iter = headers.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = iter.next();
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Request request = requestBuilder.url(url).build();
        Response response = httpClient.newCall(request).execute();
        return response;
    }

    public static Response httpGet(String url, HashMap<String, String> headers, ProxyConfig proxyConfig) throws Exception
    {
        OkHttpClient httpClient = null;
        Request request;
        if (proxyConfig != null) {
            if (proxyConfig.getProxyType().equalsIgnoreCase("http")) {
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                final String userName = proxyConfig.getUserName();
                final String passWord = proxyConfig.getPassWord();
                Authenticator proxyAuthenticator = new Authenticator() {
                    @Override
                    public Request authenticate(Route route, Response response) throws IOException {
                        String credential = Credentials.basic(userName, passWord);
                        return response.request().newBuilder()
                                .header("Proxy-Authorization", credential)
                                .build();
                    }
                };

                httpClient = builder
                        .proxy(proxyConfig.getProxy())
                        .proxyAuthenticator(proxyAuthenticator)
                        .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                        .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                        .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                        .sslSocketFactory(NOP_TLSV12_SSL_CONTEXT.getSocketFactory(), NOP_TRUST_MANAGER)
                        .hostnameVerifier(NOP_HOSTNAME_VERIFIER)
                        .followRedirects(false)
                        .build();
            }

            if (proxyConfig.getProxyType().startsWith("socks")) {
                httpClient = buildOkHttpClient(proxyConfig);
            }
        }
        Request.Builder requestBuilder = new Request.Builder();
        if (headers != null) {
            Iterator<Map.Entry<String, String>> iter = headers.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = iter.next();
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        request = requestBuilder.url(url).build();

        if (httpClient != null)
        {
            return httpClient.newCall(request).execute();
        }
        else
        {
            return null;
        }
    }

    public static Response httpPost(String url, String body, String mime, HashMap<String, String> headers, int chunkCount) throws Exception
    {
        MediaType mimeType = MediaType.parse(mime);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .followRedirects(false)
                .sslSocketFactory(NOP_TLSV12_SSL_CONTEXT.getSocketFactory(), NOP_TRUST_MANAGER)
                .hostnameVerifier(NOP_HOSTNAME_VERIFIER)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        RequestBody requestBody = TyrRequestBody.create(mimeType, body, chunkCount);
        Request.Builder requestBuilder = new Request.Builder();
        if (headers != null) {
            Iterator<Map.Entry<String, String>> iter = headers.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = iter.next();
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Request request = requestBuilder.url(url).post(requestBody).build();
        Response response = httpClient.newCall(request).execute();
        return response;
    }

    public static Response httpPost(String url, String body, String mime, HashMap<String, String> headers, ProxyConfig proxyConfig, int chunkCount) throws Exception
    {
        OkHttpClient httpClient = null;
        Request request;
        if (proxyConfig != null) {
            if (proxyConfig.getProxyType().equalsIgnoreCase("http")) {
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                final String userName = proxyConfig.getUserName();
                final String passWord = proxyConfig.getPassWord();
                Authenticator proxyAuthenticator = new Authenticator() {
                    @Override
                    public Request authenticate(Route route, Response response) {
                        String credential = Credentials.basic(userName, passWord);
                        return response.request().newBuilder()
                                .header("Proxy-Authorization", credential)
                                .build();
                    }
                };

                httpClient = builder
                        .proxy(proxyConfig.getProxy())
                        .proxyAuthenticator(proxyAuthenticator)
                        .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                        .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                        .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                        .sslSocketFactory(NOP_TLSV12_SSL_CONTEXT.getSocketFactory(), NOP_TRUST_MANAGER)
                        .hostnameVerifier(NOP_HOSTNAME_VERIFIER)
                        .followRedirects(false)
                        .build();
            }

            if (proxyConfig.getProxyType().startsWith("socks")) {
                httpClient = buildOkHttpClient(proxyConfig);
            }
        }
        MediaType mimeType = MediaType.parse(mime);
        RequestBody requestBody = TyrRequestBody.create(mimeType, body, chunkCount);
        Request.Builder requestBuilder = new Request.Builder();
        if (headers != null) {
            Iterator<Map.Entry<String, String>> iter = headers.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = iter.next();
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        request = requestBuilder.url(url).post(requestBody).build();
        Response response = httpClient.newCall(request).execute();
        return response;
    }

    public static Response httpPost(String url, File file, String mime, HashMap<String, String> headers, int chunkCount) throws Exception
    {
        MediaType mimeType = MediaType.parse(mime);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .followRedirects(false)
                .sslSocketFactory(NOP_TLSV12_SSL_CONTEXT.getSocketFactory(), NOP_TRUST_MANAGER)
                .hostnameVerifier(NOP_HOSTNAME_VERIFIER)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        RequestBody requestBody = TyrRequestBody.create(mimeType, file, chunkCount);
        RequestBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), requestBody)
                .build();
        Request.Builder requestBuilder = new Request.Builder();
        if (headers != null) {
            Iterator<Map.Entry<String, String>> iter = headers.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = iter.next();
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Request request = requestBuilder.url(url).post(multipartBody).build();
        Response response = httpClient.newCall(request).execute();
        return response;
    }

    public static Response httpPost(String url, File file, String mime, HashMap<String, String> headers, ProxyConfig proxyConfig, int chunkCount) throws Exception
    {
        OkHttpClient httpClient = null;
        Request request;
        if (proxyConfig != null) {
            if (proxyConfig.getProxyType().equalsIgnoreCase("http")) {
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                final String userName = proxyConfig.getUserName();
                final String passWord = proxyConfig.getPassWord();
                Authenticator proxyAuthenticator = new Authenticator() {
                    @Override
                    public Request authenticate(Route route, Response response) {
                        String credential = Credentials.basic(userName, passWord);
                        return response.request().newBuilder()
                                .header("Proxy-Authorization", credential)
                                .build();
                    }
                };

                httpClient = builder
                        .proxy(proxyConfig.getProxy())
                        .proxyAuthenticator(proxyAuthenticator)
                        .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                        .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                        .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                        .sslSocketFactory(NOP_TLSV12_SSL_CONTEXT.getSocketFactory(), NOP_TRUST_MANAGER)
                        .hostnameVerifier(NOP_HOSTNAME_VERIFIER)
                        .followRedirects(false)
                        .build();
            }

            if (proxyConfig.getProxyType().startsWith("socks")) {
                httpClient = buildOkHttpClient(proxyConfig);
            }
        }
        MediaType mimeType = MediaType.parse(mime);
        RequestBody requestBody = TyrRequestBody.create(mimeType, file, chunkCount);
        RequestBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), requestBody)
                .build();
        Request.Builder requestBuilder = new Request.Builder();
        if (headers != null) {
            Iterator<Map.Entry<String, String>> iter = headers.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = iter.next();
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        request = requestBuilder.url(url).post(multipartBody).build();
        Response response = httpClient.newCall(request).execute();
        return response;
    }

    public static Response httpPut(String url, byte[] body, String mime, HashMap<String, String> headers, int chunkCount) throws Exception
    {
        MediaType mimeType = MediaType.parse(mime);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .followRedirects(false)
                .sslSocketFactory(NOP_TLSV12_SSL_CONTEXT.getSocketFactory(), NOP_TRUST_MANAGER)
                .hostnameVerifier(NOP_HOSTNAME_VERIFIER)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        RequestBody requestBody = TyrRequestBody.create(mimeType, body, chunkCount);
        Request.Builder requestBuilder = new Request.Builder();
        if (headers != null) {
            Iterator<Map.Entry<String, String>> iter = headers.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = iter.next();
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Request request = requestBuilder.url(url).put(requestBody).build();
        Response response = httpClient.newCall(request).execute();
        return response;
    }

    public static Response httpPut(String url, byte[] body, String mime, HashMap<String, String> headers, ProxyConfig proxyConfig, int chunkCount) throws Exception
    {
        OkHttpClient httpClient = null;
        Request request;
        if (proxyConfig != null) {
            if (proxyConfig.getProxyType().equalsIgnoreCase("http")) {
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                final String userName = proxyConfig.getUserName();
                final String passWord = proxyConfig.getPassWord();
                Authenticator proxyAuthenticator = new Authenticator() {
                    @Override
                    public Request authenticate(Route route, Response response) {
                        String credential = Credentials.basic(userName, passWord);
                        return response.request().newBuilder()
                                .header("Proxy-Authorization", credential)
                                .build();
                    }
                };

                httpClient = builder
                        .proxy(proxyConfig.getProxy())
                        .proxyAuthenticator(proxyAuthenticator)
                        .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                        .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                        .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                        .sslSocketFactory(NOP_TLSV12_SSL_CONTEXT.getSocketFactory(), NOP_TRUST_MANAGER)
                        .hostnameVerifier(NOP_HOSTNAME_VERIFIER)
                        .followRedirects(false)
                        .build();
            }

            if (proxyConfig.getProxyType().startsWith("socks")) {
                httpClient = buildOkHttpClient(proxyConfig);
            }
        }
        MediaType mimeType = MediaType.parse(mime);
        RequestBody requestBody = TyrRequestBody.create(mimeType, body, chunkCount);
        Request.Builder requestBuilder = new Request.Builder();
        if (headers != null) {
            Iterator<Map.Entry<String, String>> iter = headers.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = iter.next();
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        request = requestBuilder.url(url).put(requestBody).build();
        Response response = httpClient.newCall(request).execute();
        return response;
    }

    public static Response httpPost(String url, byte[] body, String mime, HashMap<String, String> headers, int chunkCount) throws Exception
    {
        MediaType mimeType = MediaType.parse(mime);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .followRedirects(false)
                .sslSocketFactory(NOP_TLSV12_SSL_CONTEXT.getSocketFactory(), NOP_TRUST_MANAGER)
                .hostnameVerifier(NOP_HOSTNAME_VERIFIER)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        RequestBody requestBody = TyrRequestBody.create(mimeType, body, chunkCount);
        Request.Builder requestBuilder = new Request.Builder();
        if (headers != null) {
            Iterator<Map.Entry<String, String>> iter = headers.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = iter.next();
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Request request = requestBuilder.url(url).post(requestBody).build();
        Response response = httpClient.newCall(request).execute();
        return response;
    }

    public static Response httpPost(String url, byte[] body, String mime, HashMap<String, String> headers, ProxyConfig proxyConfig, int chunkCount) throws Exception
    {
        OkHttpClient httpClient = null;
        Request request;
        if (proxyConfig != null) {
            if (proxyConfig.getProxyType().equalsIgnoreCase("http")) {
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                final String userName = proxyConfig.getUserName();
                final String passWord = proxyConfig.getPassWord();
                Authenticator proxyAuthenticator = new Authenticator() {
                    @Override
                    public Request authenticate(Route route, Response response) {
                        String credential = Credentials.basic(userName, passWord);
                        return response.request().newBuilder()
                                .header("Proxy-Authorization", credential)
                                .build();
                    }
                };

                httpClient = builder
                        .proxy(proxyConfig.getProxy())
                        .proxyAuthenticator(proxyAuthenticator)
                        .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                        .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                        .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                        .sslSocketFactory(NOP_TLSV12_SSL_CONTEXT.getSocketFactory(), NOP_TRUST_MANAGER)
                        .hostnameVerifier(NOP_HOSTNAME_VERIFIER)
                        .followRedirects(false)
                        .build();
            }

            if (proxyConfig.getProxyType().startsWith("socks")) {
                httpClient = buildOkHttpClient(proxyConfig);
            }
        }
        MediaType mimeType = MediaType.parse(mime);
        RequestBody requestBody = TyrRequestBody.create(mimeType, body, chunkCount);
        Request.Builder requestBuilder = new Request.Builder();
        if (headers != null) {
            Iterator<Map.Entry<String, String>> iter = headers.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = iter.next();
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        request = requestBuilder.url(url).post(requestBody).build();
        Response response = httpClient.newCall(request).execute();
        return response;
    }


}
