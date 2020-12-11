/**  
 * 项目名称  ：  bright
 * 文件名称  ：  Config.java
 * 日期时间  ：  2020年12月10日 - 下午3:16:04
 */
package com.bright.push.util;

import java.io.IOException;
import java.util.Properties;

/**
 * 配置文件读取。
 * 
 * @author 王晓峰
 * @since 1.0
 */
public class Config  {

	/**
	 * 读取config.properties文件
	 * 
	 * @author wangxiaofeng
	 */
	private static Properties pros = new Properties();
	static {
		try {
			// 加载config.properties
			pros.load(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取config.properties文件对象
	 * @return
	 */
	public static Properties getPros() {
		return pros;
	}

}
