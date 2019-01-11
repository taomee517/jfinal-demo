package com.jfc.config;

import com.jfc.controller.IndexCtrl;
import com.jfc.controller.OrgCtrl;
import com.jfc.database.DB;
import com.jfc.util.FileUtil;
import com.jfc.util.LogUtil;
import com.jfc.util.SQLUtil;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.cron4j.Cron4jPlugin;
import com.jfinal.template.Engine;
import org.slf4j.Logger;

public class JConfig extends JFinalConfig{
	Logger logger = LogUtil.getLogger(JConfig.class);

	/**
	 * 配置常量
	 */
	@Override
	public void configConstant(Constants me) {
		me.setDevMode(true);
		
	}

	@Override
	public void configEngine(Engine me) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void configHandler(Handlers me) {
		// TODO Auto-generated method stub
		me.add(new ContextPathHandler("contextPathName"));
		
	}

	@Override
	public void configInterceptor(Interceptors me) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 配置插件，数据源
	 */
	@Override
	public void configPlugin(Plugins me) {
		// DataSourceSetting.setMaxActive(20);//设置最大连接数
		/**
		 * 注册SQL动态管理插件, 注意 需要先注册,再启动. 不然启动无法成功.
		 */
		try {
			SQLUtil.scanSQLFiles(DB.getArp(), PathKit.getRootClassPath() + FileUtil.FILE_SEPARATOR + "sql");
		} catch (Exception e) {
			System.err.println("!!!!!!!!!!!!!!!!!!!!! 加载SQL模板失败,错误:");
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		DB.getArp().start();
		DB.getDB();

		me.add(DB.getArp());
//		// 定时任务 待处理 2018.1.11
//		me.add(new Cron4jPlugin(PropKit.use("task.properties")));
	}

	/**配置路由
	 *
	 * @param me
	 */
	@Override
	public void configRoute(Routes me) {
		me.add("/index", IndexCtrl.class);
		me.add("/org", OrgCtrl.class);
		
	}

}
