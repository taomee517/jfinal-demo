package com.jfc.util;

/**
 * @author LuoTao
 * @email taomee517@qq.com
 * @date 2019/1/11
 * @time 10:59
 */

import java.net.Inet4Address;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class SystemUtil {
    private static org.slf4j.Logger logger = LogUtil.getLogger(SystemUtil.class);

    private static String lanIP = null;

    public static boolean isLocalIP(String ip) {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            for (String localIP : getAllLocalHostIP()) {
                if (ip.trim().equals(localIP.trim())) {
                    return true;
                }
            }
        } catch (UnknownHostException e) {
            return false;
        }
        return false;

    }

    private static String[] getAllLocalHostIP() {
        List<String> res = new ArrayList<String>();
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
                    continue;
                } else {
                    Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        ip = addresses.nextElement();
                        if (ip != null && ip instanceof Inet4Address && null != ip.getHostAddress()) {
                            res.add(ip.getHostAddress());
                        }
                    }
                }
            }
        } catch (SocketException e) {
            logger.error(e.getMessage(), e);
        }
        return (String[]) res.toArray(new String[0]);
    }

    /**
     * 缓存起来的方法
     * @return
     */
    public static String getLanIPCache() {
        if (null == lanIP) {
            lanIP = getLanIP();
        }
        return lanIP;
    }

    public static String getLanIP() {
        String[] all = getAllLocalHostIP();
        for (String ip : all) {
            if (ip.startsWith("10.") || ip.startsWith("192.168") || ip.startsWith("172")) {
                return ip;
            }
        }
        return all[0];
    }

    public static boolean isLanIP(String ip) {
        if (ip.startsWith("10.") || ip.startsWith("192.168") || ip.startsWith("172")) {
            return true;
        }
        return false;
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("window");
    }
}

