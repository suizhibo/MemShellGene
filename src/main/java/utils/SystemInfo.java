package utils;

import java.util.Locale;
import java.util.StringTokenizer;

public class SystemInfo {
    public static final boolean isWindows;

    public static final boolean isMacOS;

    public static final boolean isLinux;

    static {
        String osName = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
        isWindows = osName.startsWith("windows");
        isMacOS = osName.startsWith("mac");
        isLinux = osName.startsWith("linux");
    }

    public static final long osVersion = scanVersion(System.getProperty("os.version"));

    public static final boolean isWindows_10_orLater = (isWindows && osVersion >= toVersion(10, 0, 0, 0));

    public static final boolean isMacOS_10_11_ElCapitan_orLater = (isMacOS && osVersion >= toVersion(10, 11, 0, 0));

    public static final boolean isMacOS_10_14_Mojave_orLater = (isMacOS && osVersion >= toVersion(10, 14, 0, 0));

    public static final boolean isMacOS_10_15_Catalina_orLater = (isMacOS && osVersion >= toVersion(10, 15, 0, 0));

    public static final long javaVersion = scanVersion(System.getProperty("java.version"));

    public static final boolean isJava_9_orLater = (javaVersion >= toVersion(9, 0, 0, 0));

    public static final boolean isJava_11_orLater = (javaVersion >= toVersion(11, 0, 0, 0));

    public static final boolean isJava_15_orLater = (javaVersion >= toVersion(15, 0, 0, 0));

    public static final boolean isJetBrainsJVM = System.getProperty("java.vm.vendor", "Unknown")
            .toLowerCase(Locale.ENGLISH).contains("jetbrains");

    public static final boolean isJetBrainsJVM_11_orLater = (isJetBrainsJVM && isJava_11_orLater);

    public static final boolean isKDE = (isLinux && System.getenv("KDE_FULL_SESSION") != null);

    public static long scanVersion(String version) {
        int major = 1;
        int minor = 0;
        int micro = 0;
        int patch = 0;
        try {
            StringTokenizer st = new StringTokenizer(version, "._-+");
            major = Integer.parseInt(st.nextToken());
            minor = Integer.parseInt(st.nextToken());
            micro = Integer.parseInt(st.nextToken());
            patch = Integer.parseInt(st.nextToken());
        } catch (Exception exception) {}
        return toVersion(major, minor, micro, patch);
    }

    public static long toVersion(int major, int minor, int micro, int patch) {
        return (major << 48L) + (minor << 32L) + (micro << 16L) + patch;
    }
}
