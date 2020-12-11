/**  
 * 项目名称  ：  bright
 * 文件名称  ：  State.java
 * 日期时间  ：  2020年12月10日 - 下午3:16:04
 */
package com.bright.push.net;

/**
 * 状态实体。
 * 
 * @author 王晓峰
 * @since 1.0
 */
public class State {

	private String value;

	public State(String value) {
		super();
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String start() {
		return "服务器：运行中...";

	}

	public String stop() {
		return "服务器：已停止...";

	}

	public String restart() {
		return "服务器：重启中...";

	}

}