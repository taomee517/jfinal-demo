package com.jfc.database;

/**
 * @author LuoTao
 * @email taomee517@qq.com
 * @date 2019/1/11
 * @time 11:26
 */

import java.sql.Connection;

import com.jfc.util.LogUtil;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.DbPro;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;

/**
 *@author houyu
 */
public class DB{
    public static final String URL = "jdbc:mysql://192.168.6.160:3306/db_carshare_auto?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true&nullNamePatternMatchesAll=true&serverTimezone=Asia/Shanghai";
    public static final String USER = "root";
    public static final String PWD = "fastgo123";
    private static org.slf4j.Logger logger = LogUtil.getLogger(DB.class);
    public final static String DBNAME = "carshare";
    ActiveRecordPlugin plugin = null;

    private static class SingletonHolder {
        public final static DB instance = new DB();
    }

    public DB() {
        try {
            String url = URL;
            String user = USER;
            String password = PWD;

            DruidPlugin pool = new DruidPlugin(url, user, password);
            //			pool.setDriverClass("org.postgresql.Driver");
            pool.setDriverClass("com.mysql.cj.jdbc.Driver");
            pool.setMaxActive(1000);
            pool.setInitialSize(10);
            pool.setMinIdle(1);
            pool.setMaxWait(60000);
//			pool.setTimeBetweenConnectErrorMillis(60000);//配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
            pool.setMinEvictableIdleTimeMillis(60000);//一个连接在池中最小生存的时间，单位是毫秒
            pool.setValidationQuery("SELECT 'x'");
            pool.setTestOnBorrow(false);
            pool.setTestOnReturn(false);
            pool.setTestWhileIdle(true);
            //SCache，并且指定每个连接上PSCache的大小
            pool.setMaxPoolPreparedStatementPerConnectionSize(20);
            pool.start();
            plugin = new ActiveRecordPlugin(DBNAME, pool);
            plugin.setDialect(new MysqlDialect());// 设置方言
            plugin.setContainerFactory(new CaseInsensitiveContainerFactory(true));//转化为小写. 因为postgresql 默认是转化为小写的
            plugin.setTransactionLevel(Connection.TRANSACTION_READ_COMMITTED);
            DS.registeClasses(plugin);//注册类
            /*****************************************************************************************************************************************************
             //不要直接启动 ,启动之后, 有的配置必须在启动之前设置
             *****************************************************************************************************************************************************/
//									plugin.start();
//									Db.use(DBNAME).find("select 1 ");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static ActiveRecordPlugin getArp() {
        return SingletonHolder.instance.plugin;
    }

    public static DbPro getDB() {
        return SingletonHolder.instance.getInstanceDB();
    }

    private DbPro getInstanceDB() {
        return Db.use(DBNAME);
    }
}

