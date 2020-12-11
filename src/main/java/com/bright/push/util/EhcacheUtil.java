/**  
 * 项目名称  ：  bright
 * 文件名称  ：  EhcacheUtil.java
 * 日期时间  ：  config.properties文件
 */
package com.bright.push.util;

import java.net.URL;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * 缓存工具类。
 * 
 * @author 王晓峰
 * @since 1.0
 */
public class EhcacheUtil {

	private static final String path = "/ehcache.xml";
	private URL url;
	private CacheManager manager;
	private static EhcacheUtil ehCache;
	private ReadWriteLock rwl = new ReentrantReadWriteLock();

	private EhcacheUtil(String path) {
		url = getClass().getResource(path);
		manager = CacheManager.create(url);
	}

	public static EhcacheUtil getInstance() {
		if (ehCache == null) {
			ehCache = new EhcacheUtil(path);
		}
		return ehCache;
	}

	public void put(String cacheName, String key, Object value) {
		rwl.writeLock().lock();
		try {
			Cache cache = manager.getCache(cacheName);
			Element element = new Element(key, value);
			cache.put(element);
		} finally {
			rwl.writeLock().unlock();
		}
	}

	public Object get(String cacheName, String key) {
		rwl.readLock().lock();
		Element element = null;
		try {
			Cache cache = manager.getCache(cacheName);
			element = cache.get(key);
		} finally {
			rwl.readLock().unlock();
		}
		return element == null ? null : element.getObjectValue();
	}

	public Cache get(String cacheName) {
		return manager.getCache(cacheName);
	}

	public void remove(String cacheName, String key) {
		Cache cache = manager.getCache(cacheName);
		cache.remove(key);
	}
}
