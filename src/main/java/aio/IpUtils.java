package aio;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public final class IpUtils {

    private static final List<InetAddress> IPV4_ADDRESS = loadIpv4Address();

    private static final String LOCAL_ADDRESS = "127.0.0.1";

    private static List<InetAddress> loadIpv4Address() {
        List<InetAddress> result = new ArrayList<>(4);
        Enumeration<NetworkInterface> netInterfaces;
        try {
            netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> allIps = ni.getInetAddresses();
                while (allIps.hasMoreElements()) {
                    InetAddress address = allIps.nextElement();
                    if (address instanceof Inet4Address) {
                        result.add(address);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<InetAddress> getIpv4Address() {
        return IPV4_ADDRESS;
    }

}
