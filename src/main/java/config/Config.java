package config;

import java.util.*;

public class Config {

    public static final String TestInfo = "验证须知： \nHeader需要加入头部X-Requested-With: XmlHttpRequest。访问任意路径，若response返回的Success,说明内存马注入成功\n\n\n";
    public static final String GodzillaInfo = "上线哥斯拉须知：\n密码: pAS3\n秘钥: key\n请求Header中需要加入头部X-Requested-With: XMLHttpRequest\n\n\n";


    // 共9个
    public static List<String> AttackModule = Arrays.asList(
            "Shiro",
            "Fastjson",
            "JBoss",
            "Weblogic",
            "Confluence",
            "Ecology",
            "Seeyon",
            "SpringGateWay",
            "TongWeb"
    );

    public static List<String> CommonsGadget = Arrays.asList(
            "All",
            "CommonsBeanutils1",
            "CommonsBeanutils1_183",
            "CommonsBeanutilsAttrCompare",
            "CommonsBeanutilsAttrCompare_183",
            "CommonsBeanutilsObjectToStringComparator",
            "CommonsBeanutilsObjectToStringComparator_183",
            "CommonsBeanutilsPropertySource",
            "CommonsBeanutilsPropertySource_183",
            "CommonsBeanutilsString",
            "CommonsBeanutilsString_183",
            "CommonsBeanutilsString_192s",
            "CommonsCollections5",
            "CommonsCollections6",
            "CommonsCollectionsK1",
            "CommonsCollectionsK2",
            "Jdk7u21",
            "Spring1",
            "C3P0"
    );


    public static HashMap<String, List> AttackGadget = new HashMap<String, List>();
    static {
        AttackGadget.put("Shiro", CommonsGadget);
        AttackGadget.put("Fastjson", Arrays.asList("AutoType_Bypass"));
        AttackGadget.put("JBoss", Arrays.asList("CVE_2017_7504","CVE_2017_12149"));
        AttackGadget.put("Weblogic", Arrays.asList("CVE_2020_14883"));
        AttackGadget.put("Confluence", Arrays.asList("CVE_2021_26084"));
        AttackGadget.put("Ecology", Arrays.asList("BeanShell_RCE"));
        AttackGadget.put("Seeyon", Arrays.asList("Unauthorized_RCE"));
        AttackGadget.put("SpringGateWay", Arrays.asList("CVE_2021_22947"));
        AttackGadget.put("TongWeb", Arrays.asList("InjectMemShell"));
    }



    public static List<String> supportComponents = Arrays.asList(
            "Shiro_550",
            "Weblogic_CVE_2016_3510",
            "Weblogic_CVE_2020_14756",
            "Weblogic_CVE_2020_2883",
            "Weblogic_0day_1",
            "Weblogic_CVE_2020_14883",
            "FastJson_InjectMemShell",
            "TongWeb_InjectMemShell",
            "JBoss_CVE_2017_12149",
            "JBoss_CVE_2017_7504",
            "Confluence_CVE_2021_26084",
            "ECology_RCE",
            "Seeyon_TYR_2021_00028",
            "Spring_CVE_2022_22947"
    );

    public static HashMap<String, List> gadGetMap = new HashMap<String, List>();
    static {
        gadGetMap.put("Shiro_550", Arrays.asList("CommonsBeanutils1", "CommonsBeanutils2", "CommonsCollectionsK1"));
        gadGetMap.put("JBoss_CVE_2017_12149", Arrays.asList("CommonsCollections5", "CommonsBeanutils2", "CommonsCollectionsK1"));
        gadGetMap.put("JBoss_CVE_2017_7504", Arrays.asList("CommonsCollections5", "CommonsBeanutils1"));
        gadGetMap.put("Weblogic_CVE_2016_3510", Arrays.asList("CommonsCollectionsK1"));
        gadGetMap.put("Weblogic_CVE_2020_14756", Arrays.asList("CVE_2020_14756_2"));
        gadGetMap.put("Weblogic_0day_1", Arrays.asList("Weblogic_day_1_JDK7"));
        gadGetMap.put("Weblogic_CVE_2020_2883", Arrays.asList("CVE_2020_2883"));
        gadGetMap.put("Weblogic_CVE_2020_14883", Arrays.asList("CVE_2020_14883"));
        gadGetMap.put("FastJson_InjectMemShell", Arrays.asList("FastJson"));
        gadGetMap.put("TongWeb_InjectMemShell", Arrays.asList("CommonsBeanutils2"));
        gadGetMap.put("Confluence_CVE_2021_26084", Arrays.asList("CVE_2021_26084"));
        gadGetMap.put("ECology_RCE", Arrays.asList("ECology_RCE"));
        gadGetMap.put("Seeyon_TYR_2021_00028", Arrays.asList("TYR_2021_00028"));
        gadGetMap.put("Spring_CVE_2022_22947", Arrays.asList("CVE_2022_22947"));
    }

    public static HashMap<String, List> payloadMap = new HashMap<String, List>();
    static {
        payloadMap.put("Shiro_550", Arrays.asList("ShiroMemShell"));
        payloadMap.put("Confluence_CVE_2021_26084", Arrays.asList("TomcatFilterMemShell", "TomcatListenerMemShell"));
        payloadMap.put("JBoss_CVE_2017_12149", Arrays.asList("JBossFilterMemShell", "JBossListenerMemShell"));
        payloadMap.put("JBoss_CVE_2017_7504", Arrays.asList("JBossListenerMemShell", "JBossFilterMemShell", "CommandMemShell"));
        payloadMap.put("Weblogic_CVE_2020_14756", Arrays.asList("WeblogicListenerMemShell_CVE_2020_14756_2", "WeblogicFilterMemShell_CVE_2020_14756_2", "CommandMemShell"));
        payloadMap.put("Weblogic_CVE_2020_2883", Arrays.asList("WeblogicListenerMemshell", "WeblogicFilterMemshell"));
        payloadMap.put("Weblogic_CVE_2016_3510", Arrays.asList("WeblogicListenerMemshell", "WeblogicFilterMemshell"));
        payloadMap.put("Weblogic_0day_1", Arrays.asList("CommandMemShell", "WeblogicFilterMemshell", "WeblogicListenerMemshell"));
        payloadMap.put("Weblogic_CVE_2020_14883",Arrays.asList("WeblogicListenerMemshell", "WeblogicFilterMemshell"));
        payloadMap.put("FastJson_InjectMemShell", Arrays.asList("FastJsonFilterMemShell", "FastJsonListenerMemShell"));
        payloadMap.put("TongWeb_InjectMemShell", Arrays.asList("CommandMemShell", "TongWebListenerMemShell", "TongWebFilterMemShell"));
        payloadMap.put("ECology_RCE", Arrays.asList("TomcatFilterMemShell", "ResinListenerMemShell", "ResinFilterMemShell"));
        payloadMap.put("Seeyon_TYR_2021_00028", Arrays.asList("CommandMemShell", "TomcatListenerMemShell", "TomcatFilterMemShell"));
        payloadMap.put("Spring_CVE_2022_22947", Arrays.asList("SpringWebfluxHandlerMemShell"));
    }
    public static HashMap<String,  List> memShellNameMap = new HashMap<String, List>();
    static {
        memShellNameMap.put("ShiroMemShell", Arrays.asList("GodzillaTomcatFilter", "GodzillaTomcatListener"));
        memShellNameMap.put("WeblogicListenerMemshell", Arrays.asList("none"));
        memShellNameMap.put("JBossListenerMemShell", Arrays.asList("none"));
        memShellNameMap.put("JBossFilterMemShell", Arrays.asList("none"));
        memShellNameMap.put("WeblogicFilterMemshell", Arrays.asList("none"));
        memShellNameMap.put("CommandMemShell", Arrays.asList("none"));
        memShellNameMap.put("WeblogicListenerMemShell_CVE_2020_14756_2", Arrays.asList("none"));
        memShellNameMap.put("WeblogicFilterMemShell_CVE_2020_14756_2", Arrays.asList("none"));
        memShellNameMap.put("none", Arrays.asList("none"));
        memShellNameMap.put("WeblogicMemshell_CVE_2020_14883", Arrays.asList("none"));
        memShellNameMap.put("FastJsonListenerMemShell", Arrays.asList("none"));
        memShellNameMap.put("FastJsonFilterMemShell", Arrays.asList("none"));
        memShellNameMap.put("WeblogicFilterMemshell", Arrays.asList("none"));
        memShellNameMap.put("TongWebListenerMemShell", Arrays.asList("none"));
        memShellNameMap.put("TongWebFilterMemShell", Arrays.asList("none"));
        memShellNameMap.put("TomcatFilterMemShell", Arrays.asList("none"));
        memShellNameMap.put("TomcatListenerMemShell", Arrays.asList("none"));
        memShellNameMap.put("SpringWebfluxHandlerMemShell", Arrays.asList("none"));
        memShellNameMap.put("ResinListenerMemShell", Arrays.asList("none"));
        memShellNameMap.put("ResinFilterMemShell", Arrays.asList("none"));

    }



//    public static HashMap<String, List> MemHelp = new HashMap<String, List>();
//    static{
//        MemHelp.put("FastJson", Arrays.asList("注入须知：\n请求Header中需要包含Etags: xxx\n\n\n" + TestInfo + GodzillaInfo));
//        MemHelp.put("GlassFish", Arrays.asList("注入须知：\n无\n\n\n" + TestInfo + GodzillaInfo));
//        MemHelp.put("JBoss", Arrays.asList("注入须知：\n无\n\n\n" + TestInfo + GodzillaInfo));
//        MemHelp.put("Jetty", Arrays.asList("注入须知：\n请求Header中需要需要包含Etags: xxx\n\n\n" + TestInfo + GodzillaInfo));
//        MemHelp.put("Resin",  Arrays.asList("注入须知：\n无" + TestInfo + GodzillaInfo));
//        MemHelp.put("Tomcat", Arrays.asList("注入须知：\n请求Header中需要包含Etags: xxx\n\n\n" + TestInfo + GodzillaInfo));
//        MemHelp.put("TongWeb", Arrays.asList("注入须知：\n请求Header中需要包含Etags: xxx\n\n\n" + TestInfo + GodzillaInfo));
//        MemHelp.put("Weblogic", Arrays.asList("注入须知：\n无\n\n\n" + TestInfo + GodzillaInfo));
//        MemHelp.put("Spring", Arrays.asList("重要: SpringControlle 的内存马访问路径为 /sayNihao\n\n\n注入须知：\n无\n\n\n" + TestInfo + GodzillaInfo));
//        MemHelp.put("SpringWebflux", Arrays.asList("重要: SpringWebfluxhandler 的内存马访问路径为 /tyr\n\n\n注入须知：\n无\n\n\n"  + GodzillaInfo));
//    }




    public static List<String> get(HashMap map, String key){
        return (List<String>) map.get(key);
    }
    public static List<String> getShellName(HashMap map, String key){
        return (List<String>) map.get(key);
    }

    public static void set(HashMap map, String key, List<String> value){
        map.put(key, value);
    }

}
