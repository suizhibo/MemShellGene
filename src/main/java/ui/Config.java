package ui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Config {

    public static List<String> supportModules = Arrays.asList(
            "Shiro",
            "Weblogic",
            "Fastjson",
            "JBoss",
            "Confluence",
            "ECology",
            "Seeyon",
            "SpringGateWay"
    );

    public static List<String> serverNameList = Arrays.asList(
            "Tomcat",
            "Weblogic",
            "Spring",
            "Netty",
            "JBoss",
            "Jetty",
            "Resin",
            "GlassFish",
            "WebSphere");

    public static HashMap<String, List<String>> moduleExps = new HashMap<String, List<String>>();

    static{
        moduleExps.put("Shiro", Arrays.asList("550"));
        moduleExps.put("Weblogic", Arrays.asList("CVE_2020_14756", "CVE_2020_2883", "0Day_1", "CVE_2020_14883"));
        moduleExps.put("Fastjson", Arrays.asList("AutoType_ByPass"));
        moduleExps.put("JBoss", Arrays.asList("CVE_2017_12149", "CVE_2017_7504"));
        moduleExps.put("Confluence", Arrays.asList("CVE_2022_26134", "CVE_2021_26084"));
        moduleExps.put("ECology", Arrays.asList("BeanShell_RCE"));
        moduleExps.put("Seeyon", Arrays.asList("Unauthorized_RCE"));
        moduleExps.put("SpringGateWay", Arrays.asList("CVE_2022_22947"));

    }

    public static HashMap<String, List<String>> moduleServers = new HashMap<String, List<String>>();

    static{
        moduleServers.put("Shiro", serverNameList);
        moduleServers.put("Weblogic", Arrays.asList("Weblogic"));
//        moduleServers.put("Fastjson", serverNameList);
        moduleServers.put("Fastjson", Arrays.asList("Tomcat"));
        moduleServers.put("JBoss", Arrays.asList("JBoss"));
        moduleServers.put("Confluence", Arrays.asList("Tomcat"));
        moduleServers.put("ECology", Arrays.asList("Resin"));
        moduleServers.put("Seeyon", Arrays.asList("Tomcat"));
        moduleServers.put("SpringGateWay", Arrays.asList("Spring", "Netty"));

    }





    public static HashMap<String, List<String>> serverComponent = new HashMap<String, List<String>>();
    static {
        serverComponent.put("Spring", Arrays.asList("Boot", "Controller", "Interceptor", "WebfluxHandler"));
        serverComponent.put("Tomcat", Arrays.asList("Filter", "Listener"));
        serverComponent.put("WebSphere", Arrays.asList("Filter"));
        serverComponent.put("Netty", Arrays.asList("Handler"));

    }

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

    public static HashMap<String, List<String>> gadGetMap = new HashMap<String, List<String>>();
    static {
        gadGetMap.put("Shiro_550", CommonsGadget);
        gadGetMap.put("JBoss_CVE_2017_12149", CommonsGadget);
        gadGetMap.put("JBoss_CVE_2017_7504", CommonsGadget);
//        gadGetMap.put("Weblogic_CVE_2016_3510", CommonsGadget);
        gadGetMap.put("Weblogic_CVE_2020_14756", Arrays.asList("Weblogic_CVE_2020_14756"));
        gadGetMap.put("Weblogic_0Day_1", Arrays.asList("Weblogic_0Day_JDK7"));
        gadGetMap.put("Weblogic_CVE_2020_2883", Arrays.asList("Weblogic_CVE_2020_2883"));
        gadGetMap.put("Weblogic_CVE_2020_14883", Arrays.asList("Weblogic_CVE_2020_14883"));
        gadGetMap.put("Fastjson_AutoType_ByPass", Arrays.asList("Fastjson_AutoType_ByPass"));
        gadGetMap.put("Confluence_CVE_2021_26084", Arrays.asList("Confluence_CVE_2021_26084"));
        gadGetMap.put("Confluence_CVE_2022_26134", Arrays.asList("Confluence_CVE_2022_26134"));
        gadGetMap.put("ECology_BeanShell_RCE", Arrays.asList("ECology_BeanShell_RCE"));
        gadGetMap.put("Seeyon_Unauthorized_RCE", Arrays.asList("Seeyon_Unauthorized_RCE"));
        gadGetMap.put("SpringGateWay_CVE_2022_22947", Arrays.asList("SpringGateWay_CVE_2022_22947"));
    }

    public static HashMap<String, List<String>> protocolMap = new HashMap<String, List<String>>();
    static {
        protocolMap.put("Shiro_550", Arrays.asList("http"));
        protocolMap.put("JBoss_CVE_2017_12149", Arrays.asList("http"));
        protocolMap.put("JBoss_CVE_2017_7504", Arrays.asList("http"));
        protocolMap.put("Weblogic_CVE_2020_14756",Arrays.asList("t3", "iiop"));
//        protocolMap.put("Weblogic_CVE_2020_14756",Arrays.asList("t3", "iiop", "t3s"));
        protocolMap.put("Weblogic_0Day_1", Arrays.asList("t3", "iiop"));
//        protocolMap.put("Weblogic_0Day_1", Arrays.asList("t3", "iiop", "t3s"));
        protocolMap.put("Weblogic_CVE_2020_2883", Arrays.asList("t3", "iiop"));
//        protocolMap.put("Weblogic_CVE_2020_2883", Arrays.asList("t3", "iiop", "t3s"));
        protocolMap.put("Weblogic_CVE_2020_14883", Arrays.asList("http"));
        protocolMap.put("Fastjson_AutoType_ByPass", Arrays.asList("http"));
        protocolMap.put("Confluence_CVE_2021_26084", Arrays.asList("http"));
        protocolMap.put("Confluence_CVE_2022_26134", Arrays.asList("http"));
        protocolMap.put("ECology_BeanShell_RCE", Arrays.asList("http"));
        protocolMap.put("Seeyon_Unauthorized_RCE", Arrays.asList("http"));
        protocolMap.put("SpringGateWay_CVE_2022_22947", Arrays.asList("http"));
    }

}
