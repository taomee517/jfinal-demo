package com.jfc.util;

/**
 * @author LuoTao
 * @email taomee517@qq.com
 * @date 2019/1/11
 * @time 11:25
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

/**
 * @author yu.hou
 *
 */
public class SQLUtil {

    private static org.slf4j.Logger logger = LogUtil.getLogger(SQLUtil.class);
    /**
     * sql中的分割符, 用于区分from部分
     */
    public static final String FROMCHAR = "--from";
    public static final String FROMCHAR_MYSQL = "-- from";
    public static final String ORDERBYCHAR = "-- orderby";

    /**
     * 获取from 部分, 用于导出excel和分页的设置.
     * @param sql
     * @return
     */
    public static String getFromPart(String sql) {
        int offset = sql.toLowerCase().indexOf(SQLUtil.FROMCHAR);
        if(offset == -1) {
            offset = sql.toLowerCase().indexOf(SQLUtil.FROMCHAR_MYSQL);
            return sql.substring(offset + SQLUtil.FROMCHAR_MYSQL.length());
        }
        return sql.substring(offset + SQLUtil.FROMCHAR.length());
    }

    public static void scanSQLFiles(ActiveRecordPlugin arp, String rootPath) throws IOException {
        arp.setBaseSqlTemplatePath(rootPath);
        List<File> _fileList = new ArrayList<>();
        logger.info("------------- SQL模版 根目录:" + rootPath);
        List<File> list = FileUtil.getFilesByPostfix(new File(rootPath), ".sql", _fileList);
        for (File f : list) {
            String path = f.getCanonicalPath();
            //需要适配操作系统
            path = FileUtil.getFilePathIgnoreOpeatingSystem(path);
            rootPath = FileUtil.getFilePathIgnoreOpeatingSystem(rootPath);
            arp.addSqlTemplate(path.substring(path.indexOf(rootPath) + rootPath.length() + 1));
        }
    }
}

