package exp;

import core.GenerateMemShell;
import javafx.scene.control.TextArea;
import utils.Util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class Run {
    public static void attack(TextArea textArea, String expName, HashMap<String, String> params){
        String packageName = expName.split("_")[0];
        String classPath = "exp." + packageName.toLowerCase() + "." + expName;
        try {
            Class clazz = Class.forName(classPath);
            Constructor con = clazz.getConstructor();
            Object aInstance = con.newInstance();
            Method startMethod = clazz.getDeclaredMethod("run", HashMap.class);
            startMethod.invoke(aInstance, params);
            String message = Util.messageQueue.poll();
            while (message !=null && !message.equalsIgnoreCase("")){
                textArea.appendText(message);
                message = Util.messageQueue.poll();
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (InvocationTargetException invocationTargetException) {
            invocationTargetException.printStackTrace();
        } catch (InstantiationException instantiationException) {
            instantiationException.printStackTrace();
        } catch (IllegalAccessException illegalAccessException) {
            illegalAccessException.printStackTrace();
        } catch (NoSuchMethodException noSuchMethodException) {
            noSuchMethodException.printStackTrace();
        }
    }

    public static String generateMemString(String memName, String encodeName, String version){
        return GenerateMemShell.generateMemShell(memName, encodeName, version);
    }
}
