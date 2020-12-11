/**  
 * 项目名称  ：  bright
 * 文件名称  ：  CacheManager.java
 * 日期时间  ：  2020年12月10日 - 下午3:16:04
 */
package com.bright.push.cache;

import java.util.Date;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 缓存管理器。
 * 
 * @author 王晓峰
 * @since 1.0
 */
public class CacheManager {
	private static ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<String, Cache>();

	private CacheManager() {
	}

	private static class CacheManagerFactory {
		private static CacheManager instance = new CacheManager();
	}

	public static CacheManager getInstance() {
		return CacheManagerFactory.instance;
	}

	/**
	 * returns cache item from hashmap
	 * 
	 * @param key
	 * @return Cache
	 */
	private synchronized static Cache getCache(String key) {
		return (Cache) cacheMap.get(key);
	}

	/**
	 * Looks at the hashmap if a cache item exists or not
	 * 
	 * @param key
	 * @return Cache
	 */
	private synchronized static boolean hasCache(String key) {
		return cacheMap.containsKey(key);
	}

	/**
	 * Invalidates all cache
	 */
	public synchronized static void invalidateAll() {
		cacheMap.clear();
	}

	/**
	 * Invalidates a single cache item
	 * 
	 * @param key
	 */
	public synchronized static void invalidate(String key) {
		cacheMap.remove(key);
	}

	/**
	 * Adds new item to cache hashmap
	 * 
	 * @param key
	 * @return Cache
	 */
	private synchronized static void putCache(String key, Cache object) {
		cacheMap.put(key, object);
	}

	/**
	 * Reads a cache item's content
	 * 
	 * @param key
	 * @return
	 */
	public static Cache getContent(String key) {
		if (hasCache(key)) {
			Cache cache = getCache(key);
			if (cacheExpired(cache)) {
				cache.setExpired(true);
			}
			return cache;
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param key
	 * @param content
	 * @param ttl
	 */
	public static void putContent(String key, Object content, long ttl) {
		Cache cache = new Cache();
		cache.setKey(key);
		cache.setValue(content);
		cache.setTimeOut(ttl + new Date().getTime());
		cache.setExpired(false);
		putCache(key, cache);
	}

	/**
	 * Cache never expires
	 * 
	 * @param cache
	 * @return
	 */
	private static boolean cacheExpired(Cache cache) {
		if (cache == null) {
			return false;
		}
		long milisNow = new Date().getTime();
		long milisExpire = cache.getTimeOut();
		if (milisExpire < 0) { // Cache never expires
			return false;
		} else if (milisNow >= milisExpire) {
			return true;
		} else {
			return false;
		}
	}

}
