/**  
 * 项目名称  ：  bright
 * 文件名称  ：  SessionClient.java
 * 日期时间  ：  2020年12月10日 - 下午3:16:04
 */
package com.bright.push.model;

import io.netty.channel.Channel;

/**
 * 客户端存根。
 * 
 * @author 王晓峰
 * @since 1.0
 */
public class SessionClient {

	private String id;

	private String uuid;

	private Channel channel;

	/**
	 * <默认构造函数>
	 */
	public SessionClient() {
	}

	public SessionClient(String id, String uuid, Channel channel) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.channel = channel;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	@Override
	public String toString() {
		return "SessionClient [id=" + id + ", uuid=" + uuid + ", channel=" + channel + "]";
	}

}
