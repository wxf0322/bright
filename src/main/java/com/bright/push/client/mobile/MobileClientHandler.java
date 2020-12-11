/**  
 * 项目名称  ：  bright
 * 文件名称  ：  MobileClientHandler.java
 * 日期时间  ：  2020年12月10日 - 下午3:16:04
 */
package com.bright.push.client.mobile;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bright.push.model.Cube;
import com.bright.push.model.InMessage;
import com.bright.push.util.Config;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 移动端的客户端处理程序。
 * 
 * @author 王晓峰
 * @since 1.0
 */
@Sharable
public class MobileClientHandler extends ChannelInboundHandlerAdapter {

	/** log:日志 */
	private Logger log = LoggerFactory.getLogger(MobileClientHandler.class);
	private String uuid = "37fa5c42-22a8-4958-ac2c-bd5786740c57";
	long startTime = -1;

	/**
	 * Creates a client-side handler.
	 */
	public MobileClientHandler() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelActive(io.netty.
	 * channel.ChannelHandlerContext)
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		try {
			ctx.writeAndFlush(uuid);
		} catch (Exception e) {
			log.error("[推送服务] - " + ctx.channel().remoteAddress() + "异常\n异常信息：" + e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelRead(io.netty.
	 * channel.ChannelHandlerContext, java.lang.Object)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		if (msg instanceof Cube) {
			Cube<InMessage> cube = (Cube<InMessage>) msg;
			log.info(cube.toString());
		} else if (msg instanceof String) {
			log.info(msg.toString());
		} else {
			log.error(msg.toString());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.netty.channel.ChannelInboundHandlerAdapter#userEventTriggered(io.netty
	 * .channel.ChannelHandlerContext, java.lang.Object)
	 */
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
		try {
			if (!(evt instanceof IdleStateEvent)) {
				return;
			}

			IdleStateEvent e = (IdleStateEvent) evt;
			if (e.state() == IdleState.READER_IDLE) {
				ctx.writeAndFlush(uuid);
			}
		} catch (Exception e) {
			log.error("[推送服务] - " + ctx.channel().remoteAddress() + "异常\n异常信息：" + e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelUnregistered(io.
	 * netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void channelUnregistered(final ChannelHandlerContext ctx) throws Exception {
		log.info("待连接时间: " + MobileClient.RECONNECT_DELAY + 's');
		try {
			final EventLoop loop = ctx.channel().eventLoop();
			loop.schedule(new Runnable() {
				@Override
				public void run() {
					MobileClient mc = new MobileClient();
					log.info("重新连接: " + Config.getPros().getProperty("host") + ':'
							+ Integer.parseInt(Config.getPros().getProperty("port")));
					try {
						mc.connect(mc.configureBootstrap(new Bootstrap(), loop));
					} catch (SSLException e) {
						log.error("MobileClient 重新连接异常！", e);
					}
				}
			}, MobileClient.RECONNECT_DELAY, TimeUnit.SECONDS);
		} catch (Exception e) {
			log.error("[推送服务] - " + ctx.channel().remoteAddress() + "异常\n异常信息：" + e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelReadComplete(io.
	 * netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		try {
			ctx.flush();
		} catch (Exception e) {
			log.error("[推送服务] - " + ctx.channel().remoteAddress() + "异常\n异常信息：" + e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#exceptionCaught(io.netty.
	 * channel.ChannelHandlerContext, java.lang.Throwable)
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.close();
		log.error("[推送服务] - " + ctx.channel().remoteAddress() + "异常\n异常信息：" + cause.getMessage(), cause);
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}