package com.xxl.job.admin.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.core.io.Resource;

import java.io.*;
//import java.net.URL;
import java.util.Properties;

/**
 * properties util
 * @author xuxueli 2015-8-28 10:35:53
 */
public class PropertiesUtil {
	private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
	private static final String file_name = "xxl-job-admin.properties";
	
	/**
	 * load properties
	 * @param propertyFileName
	 * @return
	 */
	public static Properties loadProperties(String propertyFileName) {
		Properties prop = new Properties();
		//InputStreamReader in = null;
		InputStream in = null;
		try {
			ClassLoader loder = Thread.currentThread().getContextClassLoader();
			//URL url = loder.getResource(propertyFileName);
			//in = new InputStreamReader(new FileInputStream(url.getPath()), "UTF-8");
			in = loder.getResourceAsStream(propertyFileName);
			prop.load(in);
			//支持把相关配置迁移到环境变量中
			//zhaolin 48182099@qq.com
//			String path = System.getenv("XXL_JOB_CONFIG");
//			if(path!=null){
//				while(path.endsWith(File.separator)){
//					path = path.substring(0,path.length()-File.separator.length());
//				}
//				Resource resource=new FileSystemResource(path+File.separator+propertyFileName);
//				in = resource.getInputStream();
//				prop.load(in);
//			}else{
//				logger.error("XXL_JOB_CONFIG NOT FOUND!");
//			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		return prop;
	}

	public static String getString(String key) {
		Properties prop = loadProperties(file_name);
		if (prop!=null) {
			return prop.getProperty(key);
		}
		return null;
	}
	
	public static void main(String[] args) {
		System.out.println(getString("xxl.job.login.username"));
	}

}
