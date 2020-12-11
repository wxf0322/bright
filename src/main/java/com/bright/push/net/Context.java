/**  
 * 项目名称  ：  bright
 * 文件名称  ：  Context.java
 * 日期时间  ：  2020年12月10日 - 下午3:16:04
 */
package com.bright.push.net;

/**
 * 上下文。
 * 
 * @author 王晓峰
 * @since 1.0
 */
public class Context {

	private State state;

	public Context(State state) {
		this.state = state;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public String method() {
		String r = "";
		switch (state.getValue()) {
		case "start":
			r = state.start();
			break;
		case "stop":
			r = state.stop();
			break;
		case "restart":
			r = state.restart();
			break;
		default:
			break;
		}
		return r;
	}
}
