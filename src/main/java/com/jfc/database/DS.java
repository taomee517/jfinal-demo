package com.jfc.database;

import com.jfc.dao.Org;
import com.jfc.dao.Role;
import com.jfc.enums.DefaultValue;
import com.jfc.util.LogUtil;
import com.jfc.util.SQLUtil;
import com.jfc.util.StrUtil;
import com.jfc.util.SystemUtil;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Config;
import org.slf4j.Logger;

import java.util.List;

/**
 * carshare data source
 * @author yu.hou
 */
public class DS {
	static final Logger log = LogUtil.getLogger(DS.class);
	private static final ThreadLocal<String> threadlocal_levelcode = new ThreadLocal<String>();
	private static final ThreadLocal<String> threadlocal_levelcode_suffix = new ThreadLocal<String>();
	public static final String TABLE_NAME_WRAPER = "@@";//约定的水平分表包裹器

	/**
	 * 注册映射
	 *
	 * @param arp
	 */
	public static void registeClasses(ActiveRecordPlugin arp) {
		arp.addMapping("sys_org", Org.ORGID, Org.class);
		arp.addMapping("sys_role", Role.ROLEID, Role.class);
	}

	/**
	 * 仅用于调试,请注意.
	 * 打印对应的sql语句,用于开发调试
	 * @param levelcodeOrHost  levelcode 或者 host.  e.g : 1/5/1/ ,  carshare-chengdu.mysirui.com
	 * @param sql
	 * @throws InterruptedException 
	 */
	public static void printSQL(String levelcodeOrHost, String sql) {
		try {
			if (SystemUtil.getLanIPCache().startsWith("192.168")) {
				levelcodeOrHost = levelcodeOrHost.toLowerCase();
				if (levelcodeOrHost.indexOf("mysirui.com") != -1) {
					setLevelcodeByHost(levelcodeOrHost);
				} else {
					setLevelcode(levelcodeOrHost);
				}
				String sqlStr = replaceTableName(DB.getDB().getConfig(), sql);
				System.out.println("----------------------------------------------------------------------------------------------------------------------------");
				Thread.sleep(200L);
				System.err.println(sqlStr);
				Thread.sleep(200L);
				System.out.println("----------------------------------------------------------------------------------------------------------------------------");
			} else {
				throw new Exception("不能在非测试环境使用打印SQL语句的方法");
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 使用@@包裹表名, 所有的表名都需要替换
	 * @param sql
	 * @return 替换之后的SQL
	 */
	public static StringBuilder replaceTableName(Config config, StringBuilder sql) throws Exception{
		return new StringBuilder(replaceTableName(config, sql.toString()));
	}

	/**
	 * 使用@@包裹表名, 所有的表名都需要替换
	 * @param sql
	 * @return 替换之后的SQL
	 */
	public static String replaceTableName(Config config, String sql) throws Exception {
		if (sql.indexOf(TABLE_NAME_WRAPER) != -1) {
			String levelcode = DS.getTableSuffix();
			if (StrUtil.isBlank(levelcode)) {
				//				log.info("没有指定分表的levelcode,sql:" + sql);
				//				throw new Exception("系统分表数据处理错误");
				levelcode = DefaultValue.DEFAULT_LEVEL_CODE_SUFFIX;
			}

			List<String> list = StrUtil.split(sql, TABLE_NAME_WRAPER);
			int size = list.size();
			StringBuilder newSQL = new StringBuilder();
			if (size > 2 && (size - 1) % 2 == 0) {//是配对的,总是偶数
				for (int i = 0; i < size; i++) {
					if (i % 2 == 0) {
						newSQL.append(list.get(i));
					} else {//需要替换
						newSQL.append(list.get(i) + levelcode);
					}
				}
				return newSQL.toString();
			} else {
				log.info("错误的分表包装器匹配," + TABLE_NAME_WRAPER + "个数:" + list.size() + " :" + sql);
				throw new Exception("系统分表数据处理错误");
			}
		} else {
			//			if (config.getName().equals(CSDB.DBNAME)) {//如果共享车数据库, 访问中必须带上@@
			//				log.info("数据操作异常,缺乏partitionKey:" + sql);
			//				throw new Exception("数据操作异常,缺乏partitionKey");
			//			} else {
			//				return sql;//不需要处理
			//			}
			return sql;
		}
	}

	public static void setLevelcodeByHost(String host) {
		try {
			setLevelcode(getLevelCodeByHostName(host));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setLevelcode(String levelcode) {
		if (StrUtil.isBlank(levelcode)) {
			log.info("不能设置一个空的levelcode");
		}
		String r = threadlocal_levelcode.get();
		//		log.info("setLevelcode里面的r：{}，传入的levelcode：{}", r, levelcode);
		if (null == r || !r.equals(levelcode)) {
			if (levelcode.startsWith("_")) {
				levelcode = levelcode.substring(1).replaceAll("_", "/") + "/";
			}
			threadlocal_levelcode.set(levelcode);
			threadlocal_levelcode_suffix.set(parseLevelcode(levelcode));
		}
	}

	public static String getLevelcode() {
		String r = threadlocal_levelcode.get();
		return r;
	}

	public static String getTableSuffix() {
		String r = threadlocal_levelcode_suffix.get();
		return r;
	}

	/**
	 * 防止内存溢出
	 */
	public static void cleanLevelCode() {
		threadlocal_levelcode.remove();
	}

	public static String getLevelCodeByHostName(String host) throws Exception{
		if (StrUtil.isBlank(host)) {
			throw new Exception("错误的域名");
		}
		host = host.trim().toLowerCase();
		String name = host;
		if (host.startsWith("http://")) {
			name = host.substring("http://".length());
		} else if (host.startsWith("https://")) {
			name = host.substring("https://".length());
		}
		String levelcode = "";
		//TODO 需要数据库中配置
//		String rpcLevelcode = SysLevelCodeUtil.getLevelCodeByDomain(name);
		String rpcLevelcode = null;
		if (null != rpcLevelcode) {
			levelcode = rpcLevelcode;
		}
		return levelcode;
	}

	public static String parseLevelcode(String levelcode) {
		if (levelcode.startsWith("_") || levelcode.startsWith("/")) {
			levelcode = levelcode.substring(1);
		}
		return "_" + StrUtil.removeLastWord(levelcode.replace("/", "_"), "_");//注意, 要使用 _ 开头
	}

	public static void startLocalUse() {
		/**
		 * 注册SQL动态管理插件, 注意 需要先注册,再启动. 不然启动无法成功.
		 */
		try {
			String basePath = System.getProperty("user.dir");
			SQLUtil.scanSQLFiles(DB.getArp(), basePath + "\\src\\main\\resources\\sql");
		} catch (Exception e) {
			System.err.println("!!!!!!!!!!!!!!!!!!!!! 加载SQL模板失败,错误:");
			e.printStackTrace();
		}
		DB.getArp().start();
	}
}
