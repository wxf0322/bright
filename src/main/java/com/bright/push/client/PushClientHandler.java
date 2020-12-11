/**  
 * 项目名称  ：  bright
 * 文件名称  ：  PushClientHandler.java
 * 日期时间  ：  2020年12月10日 - 下午3:16:04
 */
package com.bright.push.client;

//import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bright.push.model.Cube;
import com.bright.push.model.InMessage;

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
public class PushClientHandler extends ChannelInboundHandlerAdapter {

	/** log:日志 */
	private Logger log = LoggerFactory.getLogger(PushClientHandler.class);
	public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	/**
	 * Creates a client-side handler.
	 */
	public PushClientHandler() {

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
			InMessage message = new InMessage();
			message.setContent("测试数据3");
			message.setCreateTime(System.currentTimeMillis());
			message.setMsgType("text");
			message.setFromUserName("123456");
			message.setTitle("回话");
//			log.info(UUID.randomUUID() + "");
			ctx.writeAndFlush(new Cube<InMessage>(/* UUID.randomUUID() + */"37fa5c42-22a8-4958-ac2c-bd5786740c57",
					"push", message));
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
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		try {
			if (msg != null) {
				log.info("服务器返回信息: " + msg);
			}
			// ctx.write(msg);
		} catch (Exception e) {
			log.error("[推送服务] - " + ctx.channel().remoteAddress() + "异常\n异常信息：" + e.getMessage(), e);
		} finally {
			ctx.disconnect();
			ctx.close();
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
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelInactive(io.netty.
	 * channel.ChannelHandlerContext)
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		log.debug("[1] - " + ctx.channel().remoteAddress() + "掉线");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.netty.channel.ChannelHandlerAdapter#handlerAdded(io.netty.channel.
	 * ChannelHandlerContext)
	 */
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) {
		Channel incoming = ctx.channel();
		channels.add(incoming);
		log.debug("[2] - " + incoming.remoteAddress() + " 加入\n");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.netty.channel.ChannelHandlerAdapter#handlerRemoved(io.netty.channel.
	 * ChannelHandlerContext)
	 */
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) {
		Channel incoming = ctx.channel();
		channels.remove(incoming);
		log.debug("[3] - " + incoming.remoteAddress() + " 离开\n");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#exceptionCaught(io.netty.
	 * channel.ChannelHandlerContext, java.lang.Throwable)
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		log.error(cause.getMessage(), cause);
		ctx.close();
	}
}
