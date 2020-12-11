/**  
 * 项目名称  ：  bright
 * 文件名称  ：  SupersonicServerHandler.java
 * 日期时间  ：  2020年12月10日 - 下午3:16:04
 */
package com.bright.push.server;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bright.push.cache.Cache;
import com.bright.push.cache.CacheManager;
import com.bright.push.model.Cube;
import com.bright.push.model.InMessage;
import com.bright.push.model.SessionClient;
import com.bright.push.util.EhcacheUtil;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * 推送数据的客户端处理程序。
 * 
 * @author 王晓峰
 * @since 1.0
 */
public class SupersonicServerHandler extends ChannelInboundHandlerAdapter {

	/** log:日志 */
	private Logger log = LoggerFactory.getLogger(SupersonicServerHandler.class);
	/** cache:缓存 */
	private EhcacheUtil cache = EhcacheUtil.getInstance();

	public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		log.debug("[推送服务] - " + ctx.channel().remoteAddress() + "在线");
	}

	@Override
	@SuppressWarnings("unchecked")
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		log.debug("[推送服务] - " + ctx.channel().remoteAddress() + "读取");
		try {
			if (msg instanceof Cube) {
				Cube<InMessage> cube = (Cube<InMessage>) msg;
				String uuid = cube.getId();
				CacheManager.getInstance();
				Cache c = CacheManager.getContent(uuid);
				if (c == null) {
					savePush(ctx, cube, uuid);
				} else {
					SessionClient session = (SessionClient) c.getValue();
					Channel channel = session.getChannel();
					if (channel.isActive()) {
						channel.writeAndFlush(cube);
						ctx.writeAndFlush(uuid + " 直接推送处理完成!");
						log.info("推送信息：" + cube.getId() + " - " + cube.getEntity().toString());
					} else {
						channel.disconnect();
						channel.close();
						CacheManager.invalidate(session.getUuid());
						log.info("移除断开客户端 " + uuid);
						savePush(ctx, cube, uuid);
					}
				}
			} else if (msg instanceof String) {
				String uuid = (String) msg;
				CacheManager.getInstance();
				Cache c = CacheManager.getContent(uuid);
				if (c == null) {
					saveSession(uuid, ctx);
					if (cache.get("push", uuid) == null) {
						cache.put("push", uuid, Collections.synchronizedList(new Vector<Cube<InMessage>>()));
					}
				} else {
					saveSession(uuid, ctx);
				}
				List<Cube<InMessage>> list = (List<Cube<InMessage>>) cache.get("push", uuid);
				if (list != null) {
					Iterator<Cube<InMessage>> iterators = list.iterator();
					while (iterators.hasNext()) {
						Cube<InMessage> cube = iterators.next();
						if (cube != null) {
							log.info("推送存储信息：" + cube.getId() + " - " + cube.getEntity().toString());
							ctx.writeAndFlush(cube.getEntity());
							iterators.remove();
						}
					}
					cache.put("push", uuid, list);
				}
			} else {
				log.error("收到未知数据：" + msg);
			}
			ctx.fireChannelRead(msg);
		} catch (Exception e) {
			log.error("[推送服务] - " + ctx.channel().remoteAddress() + "异常\n异常信息：" + e.getMessage(), e);
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
		log.debug("[推送服务] - " + ctx.channel().remoteAddress() + "完成");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		log.debug("[推送服务] - " + ctx.channel().remoteAddress() + "掉线");
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) {
		Channel incoming = ctx.channel();
		channels.add(incoming);
		log.debug("[客户端] - " + incoming.remoteAddress() + " 加入\n");
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) {
		Channel incoming = ctx.channel();
		channels.remove(incoming);
		log.debug("[客户端] - " + incoming.remoteAddress() + " 离开\n");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.close();
		log.error("[推送服务] - " + ctx.channel().remoteAddress() + "异常\n异常信息：" + cause.getMessage(), cause);
	}

	/**
	 * 保存Session
	 * 
	 * @param uuid 标识
	 * @param ctx  传递的信息
	 */
	private void saveSession(String uuid, ChannelHandlerContext ctx) {
		Channel channel = ctx.channel();
		SessionClient session = new SessionClient();
		session.setChannel(channel);
		session.setId(getID(channel.toString()));
		session.setUuid(uuid);
		CacheManager.getInstance();
		CacheManager.putContent(session.getUuid(), session, 1000 * 60 * 30);
	}

	/**
	 * 截取ID
	 * 
	 * @param str 包含ID字符串
	 * @return ID
	 */
	private String getID(String str) {
		String[] strs = str.split(",");
		String id = null;
		if (strs.length > 0) {
			String[] strs2 = strs[0].split("id: ");
			if (strs2.length > 0) {
				id = strs2[1];
			}
		}
		return id;

	}

	/**
	 * 保存推送信息。
	 * 
	 * @param ctx  通道处理器对象
	 * @param cube 数据包
	 * @param uuid 信息标识
	 */
	@SuppressWarnings("unchecked")
	private void savePush(ChannelHandlerContext ctx, Cube<InMessage> cube, String uuid) {
		List<Cube<InMessage>> list = (List<Cube<InMessage>>) cache.get("push", uuid);
		if (list != null) {
			list.add(cube);
			log.info(uuid + " 缓存信息：" + cube.getEntity().toString());
			log.info(uuid + " 缓存推送数量：" + list.size());
			cache.put("push", uuid, list);
			ctx.writeAndFlush(uuid + " 保存推送处理完成!");
		} else {
			log.info(uuid + " 用户没有登录过系统！");
			ctx.writeAndFlush(uuid + " 用户没有登录过系统无法推送处理!");
		}
		ctx.disconnect();
		ctx.close();
	}
}
