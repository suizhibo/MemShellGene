package utils;

import com.sun.org.apache.bcel.internal.Repository;
import com.sun.org.apache.bcel.internal.classfile.JavaClass;
import com.sun.org.apache.bcel.internal.classfile.Utility;
import javassist.*;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.zip.GZIPOutputStream;

public class Util {

    public static BlockingQueue<String> messageQueue = new ArrayBlockingQueue<String>(1024);

    public static CtClass addSuperClass(Class clazz, String superClassName){
        String clazzName = clazz.getName();
        ClassPool cp = ClassPool.getDefault();
        CtClass ctClass = null;
        try {
            ClassClassPath classPath = new ClassClassPath(Util.class);
            cp.insertClassPath(classPath);
            CtClass superClass = cp.get(superClassName);
            ctClass = cp.getAndRename(clazzName, clazzName + System.nanoTime());
            ctClass.setSuperclass(superClass);
        } catch (RuntimeException e) {

        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
        return ctClass;
    }

    public static CtClass addInterface(Class clazz, String interfaceName){
        String clazzName = clazz.getName();
        ClassPool cp = ClassPool.getDefault();
        CtClass ctClass = null;
        try {
            ClassClassPath classPath = new ClassClassPath(Util.class);
            cp.insertClassPath(classPath);
            CtClass interFaceClass = cp.get(interfaceName);
            ctClass = cp.getAndRename(clazzName, clazzName + System.nanoTime());
            ctClass.setInterfaces(new CtClass[]{interFaceClass});
        } catch (RuntimeException e) {

        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return ctClass;
    }

    public static String getClassCode(Class clazz) throws Exception {
        byte[] bytes = getClassBytes(clazz);
        String result = Util.base64Encode(bytes);

        return result;
    }

    public static byte[] getClassBytes(Class clazz) throws Exception {
        String className = clazz.getName();
        String resoucePath = className.replaceAll("\\.", "/") + ".class";
        InputStream in = Util.class.getProtectionDomain().getClassLoader().getResourceAsStream(resoucePath);
        byte[] bytes = new byte[1024];
        ByteArrayOutputStream baous = new ByteArrayOutputStream();
        int len = 0;
        while((len = in.read(bytes)) != -1){
            baous.write(bytes, 0 , len);
        }

        in.close();
        baous.close();

        return baous.toByteArray();
    }

    public static String getRandomString(final int length) {
        String str = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char ch = str.charAt(new Random().nextInt(str.length()));
            sb.append(ch);
        }
        return sb.toString();
    }

    public static String base64Encode(byte[] bytes) throws Exception {
        String result;

        try {
            Class clazz = Class.forName("java.util.Base64");
            Method method = clazz.getDeclaredMethod("getEncoder");
            Object obj = method.invoke(null);
            method = obj.getClass().getDeclaredMethod("encodeToString", byte[].class);
            obj = method.invoke(obj, bytes);
            result = (String) obj;
        } catch (ClassNotFoundException e) {

            Class clazz = Class.forName("sun.misc.BASE64Encoder");
            Method method = clazz.getMethod("encodeBuffer", byte[].class);
            Object obj = method.invoke(clazz.newInstance(), bytes);
            result = (String) obj;
            result = result.replaceAll("\r|\n|\r\n", "");
        }

        return result;
    }

    public static byte[] base64Decode(String str) throws Exception {
        byte[] bytes;

        try {
            Class clazz = Class.forName("java.util.Base64");
            Method method = clazz.getDeclaredMethod("getDecoder");
            Object obj = method.invoke(null);
            method = obj.getClass().getDeclaredMethod("decode", String.class);
            obj = method.invoke(obj, str);
            bytes = (byte[]) obj;
        } catch (ClassNotFoundException e) {
            Class clazz = Class.forName("sun.misc.BASE64Decoder");
            Method method = clazz.getMethod("decodeBuffer", String.class);
            Object obj = method.invoke(clazz.newInstance(), str);
            bytes = (byte[]) obj;
        }

        return bytes;
    }

    public static byte[] serialize(Object ref) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(out);
        objOut.writeObject(ref);
        return out.toByteArray();
    }

    public static void serialize(Object ref, String path) throws IOException {
        FileOutputStream out = new FileOutputStream(path);
        ObjectOutputStream objOut = new ObjectOutputStream(out);
        objOut.writeObject(ref);
        out.flush();
        out.close();
    }

    public static byte[] AESEncrypt(byte[] plainText, byte[] key) throws Exception {
        int ivSize = 16;
        byte[] iv = new byte[ivSize];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(1, secretKeySpec, ivParameterSpec);
        byte[] encrypted = cipher.doFinal(plainText);
        byte[] encryptedIvandtext = new byte[ivSize + encrypted.length];
        System.arraycopy(iv, 0, encryptedIvandtext, 0, ivSize);
        System.arraycopy(encrypted, 0, encryptedIvandtext, ivSize, encrypted.length);
        return encryptedIvandtext;
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString != null && !hexString.equals("")) {
            hexString = hexString.toUpperCase();
            int length = hexString.length() / 2;
            char[] hexChars = hexString.toCharArray();
            byte[] d = new byte[length];

            for (int i = 0; i < length; ++i) {
                int pos = i * 2;
                d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
            }

            return d;
        } else {
            return null;
        }
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static byte[] GetByteByFile(String FilePath) throws Exception {
        FileInputStream fi = new FileInputStream(FilePath);
        byte[] temp = new byte[50000000];
        int length = fi.read(temp);
        byte[] file = new byte[length];

        for (int i = 0; i < length; ++i) {
            file[i] = temp[i];
        }

        fi.close();
        return file;
    }

    public static String[] getIPAndPortFromBase(String base) throws NumberFormatException {
        int firstIndex = base.lastIndexOf(":");
        String port = base.substring(firstIndex + 1);

        int secondIndex = base.lastIndexOf("/", firstIndex - 1);
        if (secondIndex < 0) {
            secondIndex = 0;
        }

        String ip = base.substring(secondIndex + 1, firstIndex);
        return new String[]{ip, Integer.parseInt(port) + ""};
    }

    public static String generateBcelCode1(Class clazz) throws Exception {
        JavaClass evilJavaClazz = Repository.lookupClass(clazz);
        String code = Utility.encode(evilJavaClazz.getBytes(), true);
        String bcelCode = "$$BCEL$$" + code;
        return bcelCode;
    }

    public static String generateBcelCode2(byte[] codes) throws Exception {
        String code = Utility.encode(codes, true);
        String bcelCode = "$$BCEL$$" + code;
        return bcelCode;
    }

    public static String GZIPCompress(String str)
            throws IOException
    {
        if ((str == null) || (str.length() == 0)) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(str.getBytes("UTF-8"));
        gzip.close();
        return out.toString("ISO-8859-1");
    }
    public static String stringToUnicode(String str) {
        char[] utfBytes = str.toCharArray();
        StringBuilder unicodeBytes = new StringBuilder();
        for (char utfByte : utfBytes) {
            String hexB = Integer.toHexString(utfByte);
            if (hexB.length() <= 2) {
                hexB = "00" + hexB;
            }
            unicodeBytes.append("\\u").append(hexB);
        }
        return unicodeBytes.toString();
    }

}
