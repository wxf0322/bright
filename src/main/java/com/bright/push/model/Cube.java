/**  
 * 项目名称  ：  bright
 * 文件名称  ：  Cube.java
 * 日期时间  ：  2020年12月10日 - 下午3:16:04
 */
package com.bright.push.model;

import java.io.Serializable;

/**
 * 服务器接收的数据包。
 * 
 * @author 王晓峰
 * @since 1.0
 */
public class Cube<T> implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 6752915175008511418L;
	/** id:标识 */
	private String id;
	/** mode:传输模式 */
	private String mode;
	/** entity:实体 */
	private T entity;

	public Cube() {
	}

	public Cube(String id, String mode, T entity) {
		super();
		this.id = id;
		this.mode = mode;
		this.entity = entity;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}

	@Override
	public String toString() {
		return "Cube [id=" + id + ", mode=" + mode + ", entity=" + entity.toString() + "]";
	}

}
