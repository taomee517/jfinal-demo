package com.jfc.util;

/**
 * @author LuoTao
 * @email taomee517@qq.com
 * @date 2019/1/11
 * @time 11:10
 */
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;

public class AddressUtil {
    static Logger logger = LogUtil.getLogger(AddressUtil.class);
    private static String lanIP = null;
    private static String macAdress = null;

    public AddressUtil() {
    }

    private static byte[] getMACAddressBytes() throws SocketException, UnknownHostException {
        InetAddress address = InetAddress.getLocalHost();
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(address);
        return networkInterface.getHardwareAddress();
    }

    public static void main(String[] args) {
        String ip = "101.37.80.130";
        System.out.println(IPCheck(ip));
    }

    public static String getMacAddress() {
        if (StrUtil.isBlank(macAdress)) {
            try {
                macAdress = BytesUtil.bytesToHexShortString(getMACAddressBytes());
            } catch (Exception var1) {
                logger.error(var1.getMessage(), var1);
                macAdress = ThreadLocalRandom.current().nextLong(1000000000L, 9000000000L) + "";
            }
        }

        return macAdress;
    }

    public static boolean isLocalIP(String ip) {
        String[] var1 = getAllLocalHostIP();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            String localIP = var1[var3];
            if (ip.trim().equals(localIP.trim())) {
                return true;
            }
        }

        return false;
    }

    private static String[] getAllLocalHostIP() {
        ArrayList res = new ArrayList();

        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;

            while(true) {
                NetworkInterface netInterface;
                do {
                    do {
                        do {
                            if (!allNetInterfaces.hasMoreElements()) {
                                return (String[])((String[])res.toArray(new String[0]));
                            }

                            netInterface = (NetworkInterface)allNetInterfaces.nextElement();
                        } while(netInterface.isLoopback());
                    } while(netInterface.isVirtual());
                } while(!netInterface.isUp());

                Enumeration addresses = netInterface.getInetAddresses();

                while(addresses.hasMoreElements()) {
                    ip = (InetAddress)addresses.nextElement();
                    if (ip != null && ip instanceof Inet4Address && null != ip.getHostAddress()) {
                        res.add(ip.getHostAddress());
                    }
                }
            }
        } catch (SocketException var5) {
            logger.error(var5.getMessage(), var5);
            return (String[])((String[])res.toArray(new String[0]));
        }
    }

    public static boolean IPCheck(String str) {
        if (str != null && !str.isEmpty()) {
            String regex = "^(((\\d{1,2})|(1\\d{2})|(2[2-4]\\d)|(25[0-5]))\\.){3}((\\d{1,2})|(1\\d{2})|(2[1-4]\\d)|(25[0-5]))$";
            return str.matches(regex);
        } else {
            return false;
        }
    }

    public static boolean isDomain(String domain) {
        if (StrUtil.isNotBlank(domain)) {
            String regex = "^.*?\\.(com|cn|net|org|biz|info|cc|tv)$";
            return domain.matches(regex);
        } else {
            return false;
        }
    }

    public static String getLanIPCache() {
        if (null == lanIP) {
            lanIP = getLanIP();
        }

        return lanIP;
    }

    public static String getLanIP() {
        String[] all = getAllLocalHostIP();
        String[] var1 = all;
        int var2 = all.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            String ip = var1[var3];
            if (ip.startsWith("10.") || ip.startsWith("192.168.0") || ip.startsWith("172")) {
                return ip;
            }
        }

        return all[0];
    }

    public static List<String> getLanIPS() {
        List<String> r = new ArrayList();
        String[] all = getAllLocalHostIP();
        String[] var2 = all;
        int var3 = all.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String ip = var2[var4];
            if (ip.startsWith("10.") || ip.startsWith("192.168.0") || ip.startsWith("172")) {
                r.add(ip);
            }
        }

        return r;
    }

    public static List<String> getIPS() {
        List<String> r = new ArrayList();
        String[] all = getAllLocalHostIP();
        String[] var2 = all;
        int var3 = all.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String ip = var2[var4];
            r.add(ip);
        }

        return r;
    }

    public static boolean isLanIP(String ip) {
        return ip.startsWith("10.") || ip.startsWith("192.168.0") || ip.startsWith("172");
    }

    public static boolean isTestIP() {
        String ip = getLanIPCache();
        logger.info("获取到的IP为{}", ip);
        return ip.startsWith("192.168.6");
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }
}

