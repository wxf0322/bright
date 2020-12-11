/**  
 * 项目名称  ：  bright
 * 文件名称  ：  Cache.java
 * 日期时间  ：  2020年12月10日 - 下午3:16:04
 */
package com.bright.push.cache;

/**
 * 缓存。
 * 
 * @author 王晓峰
 * @since 1.0
 */
public class Cache {
	private String key;
	private Object value;
	private long timeOut;
	private boolean expired;

	public Cache() {
		super();
	}

	public Cache(String key, String value, long timeOut, boolean expired) {
		this.key = key;
		this.value = value;
		this.timeOut = timeOut;
		this.expired = expired;
	}

	public String getKey() {
		return key;
	}

	public long getTimeOut() {
		return timeOut;
	}

	public Object getValue() {
		return value;
	}

	public void setKey(String string) {
		key = string;
	}

	public void setTimeOut(long l) {
		timeOut = l;
	}

	public void setValue(Object object) {
		value = object;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean b) {
		expired = b;
	}
}
