package core.config;

import java.util.Date;

import core.util.UtilFunctions;

public class AppConfig
{

	public static String getServerLogPath() {
		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("win")){
			String tomcatDir = System.getProperty("catalina.base");
			return tomcatDir+"\\logs\\localhost_access_log."+UtilFunctions.getStringFromDate(new Date(), "yyyy-MM-dd")+".txt";
		}
		else {
			return "/tomcat/logs/catalina.out"; 
		}
	}

	public static String getImagePath() {
		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("win")){
			return "C:\\eclipse\\workspace\\applied_project\\applied_project\\python-projects\\data\\";
		}
		else {
			return "/python/data/"; 
		}
	}
	
	public static String getServiceURL() {
		return "https://training.gfsolucoesti.com.br/service/";
	}

}
