/**  
 * 项目名称  ：  bright
 * 文件名称  ：  SupersonicServer.java
 * 日期时间  ：  2020年12月10日 - 下午3:16:04
 */
package com.bright.push.server;

import java.security.cert.CertificateException;

import javax.net.ssl.SSLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bright.push.util.Config;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * 推送服务器。
 * 
 * @author 王晓峰
 * @since 1.0
 */
public final class SupersonicServer {

	/** log:日志 */
	private Logger log = LoggerFactory.getLogger(SupersonicServer.class);
	private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
	private EventLoopGroup workerGroup = new NioEventLoopGroup();

	/**
	 * 启动服务器。
	 */
	public void start() {
		try {
			ServerBootstrap b = new ServerBootstrap();
			boolean ssl = Boolean.parseBoolean(Config.getPros().getProperty("ssl"));
			final SslContext sslCtx;
			if (ssl) {
				SelfSignedCertificate ssc = new SelfSignedCertificate();
				sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
			} else {
				sslCtx = null;
			}
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							if (sslCtx != null) {
								ch.pipeline().addLast(sslCtx.newHandler(ch.alloc()));
							}
							ch.pipeline().addLast(new ObjectEncoder(),
									new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
									new SupersonicServerHandler());
							ch.pipeline().addLast("idleStateHandler", new IdleStateHandler(300, 300, 180));
							ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
								@Override
								public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
									if (evt instanceof IdleStateEvent) {
										IdleStateEvent event = (IdleStateEvent) evt;
										if (event.state() == IdleState.ALL_IDLE) {
											log.warn(ctx.channel() + " ALL_IDLE 超时");
											ctx.disconnect();
											ctx.close();
										} else if (event.state() == IdleState.READER_IDLE) {
											log.warn(ctx.channel() + " READER_IDLE 超时");
											ctx.disconnect();
											ctx.close();
										}
									}
								}
							});
						}
					}).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true)
					.childOption(ChannelOption.SO_REUSEADDR, true);
			b.bind(Integer.parseInt(Config.getPros().getProperty("port"))).sync().channel().closeFuture().sync();
		} catch (InterruptedException e) {
			log.error("SupersonicServer 启动异常！", e);
		} catch (SSLException | CertificateException e) {
			log.error("SupersonicServer 启动异常！SSL的相关问题。", e);
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	public void stop() {
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
	}

	public static void main(String[] args) {
		new SupersonicServer().start();
	}
}
