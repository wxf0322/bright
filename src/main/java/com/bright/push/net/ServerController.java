/**  
 * 项目名称  ：  bright
 * 文件名称  ：  Controller.java
 * 日期时间  ：  2020年12月10日 - 下午3:16:04
 */
package com.bright.push.net;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bright.push.server.SupersonicServer;

/**
 * 服务器控制器。
 * 
 * @author 王晓峰
 * @since 1.0
 */
public class ServerController implements Controller {

	/** 日志对象 */
	private Log log = LogFactory.getLog(ServerController.class);
	private Thread thread;
	private State state;
	private SupersonicServer server;

	public void start() {
		state = new State("start");
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				server = new SupersonicServer();
				server.start();
			}
		});
		thread.start();
	}

	public void stop() {
		state = new State("stop");
		server.stop();
	}

	public void restart() {
		state = new State("restart");
		this.stop();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			log.error("重新启动服务器异常！", e);
		}
		this.start();

	}

	public String status() {
		return new Context(state).method();
	}

}
