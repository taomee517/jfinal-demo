/**
 * 
 */
package com.jfc;

import com.jfc.component.IComponentVersion;
import com.jfc.enums.ComponentEnum;
import com.jfc.util.LogUtil;
import com.jfc.util.StrUtil;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.predicate.Predicates;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.encoding.ContentEncodingRepository;
import io.undertow.server.handlers.encoding.EncodingHandler;
import io.undertow.server.handlers.encoding.GzipEncodingProvider;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.FilterInfo;
import org.slf4j.Logger;

import javax.servlet.DispatcherType;
import javax.servlet.ServletException;

/**
 * @author yu.hou
 *
 */
public class StartServer implements IComponentVersion {
	private static Logger logger = LogUtil.getLogger(StartServer.class);

	public static final String APPNAME = "/";

	public static void main(String[] args) throws ServletException {
		String port = "8301";
		if (args.length > 0) {
			port = args[0];
		}
		System.out.println("microService will start at port:" + port);
		//配置classLoader
		DeploymentInfo builder = Servlets.deployment().setClassLoader(StartServer.class.getClassLoader())
				//设置上下文路径
				.setContextPath(APPNAME)
				.setDeploymentName("microservice.war");
		//javeEE filter 配置
		FilterInfo filter = new FilterInfo("frame", com.jfinal.core.JFinalFilter.class);
		filter.setAsyncSupported(true);
		filter.addInitParam("configClass", "com.jfc.config.JConfig");
		builder.addFilter(filter);
		builder.addFilterUrlMapping("frame", "/*", DispatcherType.REQUEST);
		//配置默认访问路径
		builder.addWelcomePage("/index");
		//启动时初始化过滤器
		builder.setEagerFilterInit(true);
		//运行runtime时加载
		//builder.setResourceManager(new FileResourceManager(new File("./src/main/resources/"), 10 * 1024 * 1024l));
		//view文件存放根目录
		//builder.setResourceManager(new FileResourceManager(new File("C:/Users/FZK/Desktop/ms-price-1.0.0/webapps/"), 10 * 1024 * 1024l));
		//开发时加载
		builder.setResourceManager(new ClassPathResourceManager(StartServer.class.getClassLoader()));
		DeploymentManager manager = Servlets.defaultContainer().addDeployment(builder);
		manager.deploy();
		HttpHandler servletHandler = manager.start();
		PathHandler path = Handlers.path(Handlers.redirect(APPNAME)).addPrefixPath(APPNAME, servletHandler);
		//开启gzip压缩
		final EncodingHandler handler = new EncodingHandler(new ContentEncodingRepository().addEncodingHandler("gzip", new GzipEncodingProvider(), 50, Predicates.parse("max-content-size[5]")))
				.setNext(path);
//		api = new BlackTeaApi();
//		api.setPrivatekey(ApiConfig.RSA_PRIVATEKEY);
//		api.setProductKey(ApiConfig.productKey);
//		api.setUrl(ApiConfig.URL);
		//配置监听端口
		Undertow server = Undertow.builder().addHttpListener(Integer.parseInt(port), "0.0.0.0").setHandler(handler).build();
		server.start();
		//		StartUp instance = new StartUp();
		//		ProcessUtil.writePID(instance.getProgram(), instance.getVersion());
	}

	/**
	 * 当前发布的版本,
	 */
	@Override
	public String getVersion() {
		/**
		 * 主干版本号  架构变更、大批需求变更
		 */
		final String MainVersion = "1";
		/**
		 * 子版本号  批量功能增加
		 */
		final String SubVersion = "0";
		/**
		 * 修订版本号  功能修改
		 */
		final String ModifyVersion = "0";
		/**
		 *  测试版本号
		 *  
		 */
		final String TestVersion = "T00";
		return StrUtil.joinWith(".R", MainVersion, SubVersion, ModifyVersion, TestVersion);
	}

	/**
	 * 版本说明,每次都需要修改, 会当做svn的备注自动提交
	 */
	@Override
	public String getDesc() {
		return "修改了控制样式";
	}

	@Override
	public ComponentEnum getConfig() {
		return ComponentEnum.JFINAL_DEMO;
	}

}
