package com.ssic.zk.base_util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesUtil {

	public static final Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
	
	private String zkUrl = "127.0.0.1";
	
	private int zkTimeOut = 3000;
	
	private String root= "/";
	
	public boolean setProperties(String key, String value) {
		ZkUtil zkUtil = new ZkUtil(zkUrl, zkTimeOut);
		try {	
			key = root+key;
			if(!zkUtil.isExists(key)) {
				zkUtil.createPath(key, value);
				return true;
			}
		}catch(Exception e) {
			
		}finally {
			zkUtil.releaseConnection();
		}
		return false;
	}
	
	public String getProperties(String key) {
		ZkUtil zkUtil = new ZkUtil(zkUrl, zkTimeOut);
		try {
			key = root+key;
			return zkUtil.getData(key);
		}catch(Exception e) {
			
		}finally {
			zkUtil.releaseConnection();
		}
		return null;
	}
	
	public static void main(String[] args) {
		PropertiesUtil propertiesUtil = new PropertiesUtil();
		propertiesUtil.setProperties("dev/spring.datasource.password", "123456");
		System.out.println(propertiesUtil.getProperties("dev/spring.datasource.type"));
	}
}
