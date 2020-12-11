/**  
 * 项目名称  ：  bright
 * 文件名称  ：  Start.java
 * 日期时间  ：  2020年12月10日 - 下午3:16:04
 */
package com.bright.push.net;

/**
 * 启动命令。
 * 
 * @author 王晓峰
 * @since 1.0
 */
public class Start implements Command {

	private Controller controller;

	public Start(Controller controller) {
		this.controller = controller;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bright.emsunionpay.net.Command#execute()
	 */
	@Override
	public void execute() {
		controller.start();
	}

}
