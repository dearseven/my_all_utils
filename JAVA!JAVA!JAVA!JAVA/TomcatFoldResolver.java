package com.qqjf.moudle.recommend.utils;

import java.io.File;

import javax.servlet.ServletContextEvent;

public class TomcatFoldResolver {
	public void getParentOfWebdeploymentFolder() {
		String path=System.getProperty("catalina.home");
		System.out.println("============TomcatFoldResolver==============");
		System.out.println(path);
	}
	/**
	 * 在catalina.home目录下创建文件目录，并返回地址
	 * @param path \\webapps\\a
	 */
	public static String mkDirBaseCatalinaHome(String folderPath){
		String path=System.getProperty("catalina.home");
		path=path+folderPath;
		File folder=new File(path);
		if(!folder.exists()) {
			folder.mkdirs();
		}
		return folder.getAbsolutePath();
	}
}
