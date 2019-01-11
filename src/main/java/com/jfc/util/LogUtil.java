package com.jfc.util;

/**
 * @author LuoTao
 * @email taomee517@qq.com
 * @date 2019/1/11
 * @time 11:00
 */
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.util.StatusPrinter;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class LogUtil {
    static String config ="";
    public LogUtil() {
    }

    public static Logger getLogger(Class<?> logClass) {
        return LoggerFactory.getLogger(logClass);
    }

    public static Logger getAccessLog() {
        return LoggerFactory.getLogger("access");
    }

    public static Logger getAccessDBOutLog() {
       return LoggerFactory.getLogger("access_db");
    }

    static {
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            String path = "";
            if (null != cl && null != cl.getResource("")) {
                path = cl.getResource("").getPath();
            } else {
                path = System.getProperty("user.dir");
            }

            System.err.println("---------------------- log path :" + path);
            if (path.indexOf("/webapps/") != -1) {
                path = (String)StrUtil.split(path, "/webapps/").get(0);
            }

            System.err.println("log home:" + path);
            setLogHome(StrUtil.removeLastWord(path, "/") + "/logs");
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public static void setLogHome(String logPath) {
        System.getProperties().put("LOG_HOME", logPath);

        try {
            resetConfig();
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    private static void resetConfig() throws Exception {
        if (!StrUtil.isBlank(config)) {
            reConfig(IOUtils.toInputStream(config));
        } else {
            reConfig(LogUtil.class.getResourceAsStream("/logback.xml"));
        }

    }

    public static void reConfig(InputStream in) throws Exception {
        config = IOUtils.toString(in, "UTF-8");
        LoggerContext lc = (LoggerContext)LoggerFactory.getILoggerFactory();
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(lc);
        lc.reset();
        boolean isDevEnv = false;
        if (EnvUtil.isDevEvn()) {
            config = config.replace("<root level=\"INFO\">", "<root level=\"INFO\"><appender-ref ref=\"STDOUT\" />");
            isDevEnv = true;
        } else {
            config = config.replace("ref=\"STDOUT\"", "");
        }

        configurator.doConfigure(IOUtils.toInputStream(config, "UTF-8"));
        StatusPrinter.printInCaseOfErrorsOrWarnings(lc);
        Logger logger = LoggerFactory.getLogger(LogUtil.class);
        logger.info("初始化logback配置文件");
        if (isDevEnv) {
            logger.error("检测到开发环境运行, 自动开启控制台打印.");
        } else {
            logger.error("检测到正式环境运行, 自动关闭控制台打印");
        }

    }
}

