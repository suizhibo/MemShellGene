package ui;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 配置管理类
 * 优化点：
 * 1. 使用不可变集合防止配置被修改
 * 2. 使用静态代码块初始化
 * 3. 添加配置验证方法
 * 4. 支持配置文件加载
 */
public class Config {

    // ============ 支持的模块 ============
    public static final List<String> SUPPORT_MODULES = Collections.unmodifiableList(Arrays.asList(
            "Shiro",
            "Weblogic",
            "Fastjson",
            "JBoss",
            "Confluence",
            "ECology",
            "Seeyon",
            "SpringGateWay",
            "TongWeb",
            "Landray"
    ));

    // ============ 服务器列表 ============
    public static final List<String> SERVER_NAME_LIST = Collections.unmodifiableList(Arrays.asList(
            "Tomcat",
            "TongWeb",
            "Weblogic",
            "Spring",
            "Netty",
            "JBoss",
            "Jetty",
            "Resin",
            "GlassFish",
            "WebSphere"
    ));

    // ============ 模块EXP映射 ============
    public static final Map<String, List<String>> MODULE_EXPS;
    static {
        Map<String, List<String>> map = new HashMap<>();
        map.put("Shiro", Collections.unmodifiableList(Arrays.asList("550")));
        map.put("Weblogic", Collections.unmodifiableList(Arrays.asList(
            "CVE_2020_14756", "CVE_2020_2883", "0Day_1", "CVE_2020_14883"
        )));
        map.put("Fastjson", Collections.unmodifiableList(Arrays.asList("AutoType_ByPass")));
        map.put("JBoss", Collections.unmodifiableList(Arrays.asList("CVE_2017_12149", "CVE_2017_7504")));
        map.put("Confluence", Collections.unmodifiableList(Arrays.asList("CVE_2022_26134", "CVE_2021_26084")));
        map.put("ECology", Collections.unmodifiableList(Arrays.asList("BeanShell_RCE")));
        map.put("Seeyon", Collections.unmodifiableList(Arrays.asList("Unauthorized_RCE")));
        map.put("SpringGateWay", Collections.unmodifiableList(Arrays.asList("CVE_2022_22947")));
        map.put("TongWeb", Collections.unmodifiableList(Arrays.asList("BeanShell_RCE")));
        map.put("Landray", Collections.unmodifiableList(Arrays.asList("BeanShell_RCE")));
        MODULE_EXPS = Collections.unmodifiableMap(map);
    }

    // ============ 模块-服务器映射 ============
    public static final Map<String, List<String>> MODULE_SERVERS;
    static {
        Map<String, List<String>> map = new HashMap<>();
        map.put("Shiro", SERVER_NAME_LIST);
        map.put("Weblogic", Collections.unmodifiableList(Arrays.asList("Weblogic")));
        map.put("Fastjson", Collections.unmodifiableList(Arrays.asList("Tomcat")));
        map.put("JBoss", Collections.unmodifiableList(Arrays.asList("JBoss")));
        map.put("Confluence", Collections.unmodifiableList(Arrays.asList("Tomcat")));
        map.put("ECology", Collections.unmodifiableList(Arrays.asList("Resin")));
        map.put("Seeyon", Collections.unmodifiableList(Arrays.asList("Tomcat")));
        map.put("SpringGateWay", Collections.unmodifiableList(Arrays.asList("Spring", "Netty")));
        map.put("TongWeb", Collections.unmodifiableList(Arrays.asList("TongWeb")));
        map.put("Landray", Collections.unmodifiableList(Arrays.asList("Tomcat")));
        MODULE_SERVERS = Collections.unmodifiableMap(map);
    }

    // ============ 服务器组件映射 ============
    public static final Map<String, List<String>> SERVER_COMPONENTS;
    static {
        Map<String, List<String>> map = new HashMap<>();
        map.put("Spring", Collections.unmodifiableList(Arrays.asList(
            "Boot", "Controller", "Interceptor", "WebfluxHandler"
        )));
        map.put("Tomcat", Collections.unmodifiableList(Arrays.asList("Filter", "Listener")));
        map.put("TongWeb", Collections.unmodifiableList(Arrays.asList("Filter", "Listener")));
        map.put("WebSphere", Collections.unmodifiableList(Arrays.asList("Filter")));
        map.put("Netty", Collections.unmodifiableList(Arrays.asList("Handler")));
        map.put("JBoss", Collections.unmodifiableList(Arrays.asList("Filter", "Listener")));
        map.put("Jetty", Collections.unmodifiableList(Arrays.asList("Filter", "Listener")));
        map.put("Resin", Collections.unmodifiableList(Arrays.asList("Filter", "Listener")));
        map.put("GlassFish", Collections.unmodifiableList(Arrays.asList("Filter", "Listener")));
        map.put("Weblogic", Collections.unmodifiableList(Arrays.asList("Filter", "Listener")));
        SERVER_COMPONENTS = Collections.unmodifiableMap(map);
    }

    // ============ Gadget链 ============
    public static final List<String> COMMONS_GADGETS = Collections.unmodifiableList(Arrays.asList(
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
    ));

    // ============ EXP-Gadget映射 ============
    public static final Map<String, List<String>> GADGET_MAP;
    static {
        Map<String, List<String>> map = new HashMap<>();
        // Shiro和JBoss支持所有Gadget
        map.put("Shiro_550", COMMONS_GADGETS);
        map.put("JBoss_CVE_2017_12149", COMMONS_GADGETS);
        map.put("JBoss_CVE_2017_7504", COMMONS_GADGETS);
        
        // Weblogic使用特定Gadget
        List<String> weblogicGadgets = Collections.unmodifiableList(Arrays.asList(
            "CommonsCollections5", "CommonsCollections6", "Jdk7u21"
        ));
        map.put("Weblogic_CVE_2020_14756", weblogicGadgets);
        map.put("Weblogic_CVE_2020_2883", weblogicGadgets);
        map.put("Weblogic_0Day_1", weblogicGadgets);
        map.put("Weblogic_CVE_2020_14883", Collections.emptyList());
        
        // Fastjson不使用Gadget
        map.put("Fastjson_AutoType_ByPass", Collections.emptyList());
        
        // Confluence
        map.put("Confluence_CVE_2022_26134", Collections.unmodifiableList(Arrays.asList(
            "CommonsCollections5", "CommonsCollections6"
        )));
        map.put("Confluence_CVE_2021_26084", Collections.unmodifiableList(Arrays.asList(
            "CommonsCollections5", "CommonsCollections6"
        )));
        
        // 其他使用CommonsCollections
        List<String> ccGadgets = Collections.unmodifiableList(Arrays.asList(
            "CommonsCollections5", "CommonsCollections6"
        ));
        map.put("ECology_BeanShell_RCE", ccGadgets);
        map.put("Seeyon_Unauthorized_RCE", ccGadgets);
        map.put("SpringGateWay_CVE_2022_22947", ccGadgets);
        map.put("TongWeb_BeanShell_RCE", ccGadgets);
        map.put("Landray_BeanShell_RCE", ccGadgets);
        
        GADGET_MAP = Collections.unmodifiableMap(map);
    }

    // ============ 工具方法 ============
    
    /**
     * 获取模块支持的EXP列表
     */
    public static List<String> getExpsByModule(String module) {
        return MODULE_EXPS.getOrDefault(module, Collections.emptyList());
    }

    /**
     * 获取模块支持的服务器列表
     */
    public static List<String> getServersByModule(String module) {
        return MODULE_SERVERS.getOrDefault(module, SERVER_NAME_LIST);
    }

    /**
     * 获取服务器支持的组件
     */
    public static List<String> getComponentsByServer(String server) {
        return SERVER_COMPONENTS.getOrDefault(server, Collections.emptyList());
    }

    /**
     * 获取EXP支持的Gadget列表
     */
    public static List<String> getGadgetsByExp(String exp) {
        return GADGET_MAP.getOrDefault(exp, COMMONS_GADGETS);
    }

    /**
     * 检查模块是否有效
     */
    public static boolean isValidModule(String module) {
        return SUPPORT_MODULES.contains(module);
    }

    /**
     * 检查服务器是否有效
     */
    public static boolean isValidServer(String server) {
        return SERVER_NAME_LIST.contains(server);
    }

    /**
     * 私有构造函数，防止实例化
     */
    private Config() {
        throw new AssertionError("Config类不应被实例化");
    }
}
