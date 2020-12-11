/**  
 * 项目名称  ：  bright
 * 文件名称  ：  Controller.java
 * 日期时间  ：  2020年12月10日 - 下午3:16:04
 */
package com.bright.push.net;

/**
 * 控制器。
 * 
 * @author 王晓峰
 * @since 1.0
 */
public interface Controller {

	void start();
	
	void stop();

	void restart();

	String status();
	
}
