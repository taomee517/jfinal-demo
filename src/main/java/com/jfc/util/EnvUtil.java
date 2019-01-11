package com.jfc.util;

/**
 * @author LuoTao
 * @email taomee517@qq.com
 * @date 2019/1/11
 * @time 11:07
 */

import java.io.File;
import org.slf4j.Logger;

public class EnvUtil {
    static Logger logger = LogUtil.getLogger(EnvUtil.class);
    static String os = System.getProperty("os.name");

    public EnvUtil() {
    }

    public static boolean isWindows() {
        logger.info("EnvUtil 查到的os.name为 {}", os);
        if (StrUtil.isBlank(os)) {
            return false;
        } else {
            return os.toLowerCase().startsWith("win");
        }
    }

    public static boolean isDevEvn() {
        String serverIp = AddressUtil.getLanIPCache();
        return serverIp.startsWith("192.168.6")||serverIp.startsWith("192.168.1");
    }

    public static String getRootPath() {
        return System.getProperty("user.dir");
    }

    public static String getCurrentVersion() {
        File file = new File(getRootPath() + "/pid.txt");
        if (file.exists()) {
            try {
                String text = FileUtil.readText(file.getCanonicalPath());
                String part1 = (String)StrUtil.split(text, "version=").get(1);
                return (String)StrUtil.split(part1, FileUtil.LINESEPERATOR).get(0);
            } catch (Exception var3) {
                logger.error(var3.getMessage(), var3);
            }
        }

        return "";
    }

    public static String getCurrentAppName() {
        File file = new File(getRootPath() + "/pid.txt");
        if (file.exists()) {
            try {
                String text = FileUtil.readText(file.getCanonicalPath());
                String part1 = (String)StrUtil.split(text, "appname=").get(1);
                return (String)StrUtil.split(part1, FileUtil.LINESEPERATOR).get(0);
            } catch (Exception var3) {
                logger.error(var3.getMessage(), var3);
            }
        }

        return "";
    }
}
