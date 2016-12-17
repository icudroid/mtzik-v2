package fr.k2i.adbeback.webapp.util;

/**
 * quirks to store in the thread the real ip of the user
 */
public class IPStorage {
    public static ThreadLocal<String> ip = new ThreadLocal<String>();

    public static String getIpAddress() {
        return ip.get();
    }


    public static void setLocation(String ipAddress) {
        ip.set(ipAddress);
    }

    public static void resetLocation() {
        ip.set(null);
    }
}
