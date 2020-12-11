/**  
 * 项目名称  ：  bright
 * 文件名称  ：  RefreshFilter.java
 * 日期时间  ：  2020年12月10日 - 下午3:16:04
 */
package com.bright.push.action;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 防刷新过滤器。
 * 
 * @author 王晓峰
 * @since 1.0
 */
public class RefreshFilter implements Filter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 * javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		filter((HttpServletRequest) request, (HttpServletResponse) response, chain);
	}

	private void filter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String requestURI = request.getRequestURI();
		Cache cache = Cache.getInstance();
		cache.increment(requestURI);
		if (cache.isUpCount(requestURI)) {
			response.getWriter().println("到达次数，不允许请求，请稍候再试");
			return;
		}
		chain.doFilter(request, response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig config) throws ServletException {

	}

	/**
	 * 缓存
	 * 
	 * @author 王晓峰
	 * @since 1.0
	 */
	private static class Cache {

		private static final ConcurrentHashMap<String, CopyOnWriteArrayList<Long>> map = new ConcurrentHashMap<String, CopyOnWriteArrayList<Long>>();
		private static final long EXPIRE_TIME = 1000 * 20L;
		private static final long CLEAR_TIME = 1000 * 2L;
		private static final int MAX_REFRESH_COUNT = 20;

		private static final Cache cache = new Cache();

		private Cache() {
			new Thread(new ClearCacheRunnable()).start();
		}

		public static Cache getInstance() {
			return cache;
		}

		/**
		 * 增长指定URL的点击次数
		 * @param key
		 */
		public void increment(String key) {
			CopyOnWriteArrayList<Long> list = map.get(key);
			if (list == null) {
				map.put(key, new CopyOnWriteArrayList<Long>());
			}
			map.get(key).add(new Long(System.currentTimeMillis()));
		}

		/**
		 * 是否到达指定数量
		 * @param key
		 * @return
		 */
		public boolean isUpCount(String key) {
			CopyOnWriteArrayList<Long> list = map.get(key);
			if (list == null) {
				return false;
			}
			return list.size() > MAX_REFRESH_COUNT;
		}

		/**
		 * 清理过期缓存
		 * 
		 * @author 王晓峰
		 * @since 1.0
		 */
		private static class ClearCacheRunnable implements Runnable {

			/** 日志对象 */
			private Log log = LogFactory.getLog(ClearCacheRunnable.class);
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(Cache.CLEAR_TIME);
						clear();
					} catch (InterruptedException e) {
						log.error("线程异常！", e);
					}
				}
			}

			private void clear() {
				for (String key : map.keySet()) {
					CopyOnWriteArrayList<Long> list = map.get(key);
					for (Long date : list) {
						if (System.currentTimeMillis() - date > Cache.EXPIRE_TIME) {
							log.debug("移除缓存：" + date + ":" + list.remove(date));
						}
					}
				}
			}
		}
	}

}
