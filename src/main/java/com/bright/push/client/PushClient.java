/**  
 * 项目名称  ：  bright
 * 文件名称  ：  PushClient.java
 * 日期时间  ：  2020年12月10日 - 下午3:16:04
 */
package com.bright.push.client;

import javax.net.ssl.SSLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bright.push.util.Config;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

/**
 * 推送数据的客户端。
 * 
 * @author 王晓峰
 * @since 1.0
 */
public class PushClient implements Runnable {

	/** log:日志 */
	private static Logger log = LoggerFactory.getLogger(PushClient.class);
	public static final String HOST = Config.getPros().getProperty("host");
	public static final int PORT = Integer.parseInt(Config.getPros().getProperty("port"));

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			boolean ssl = Boolean.parseBoolean(Config.getPros().getProperty("ssl"));
			final SslContext sslCtx;
			if (ssl) {
				sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
			} else {
				sslCtx = null;
			}
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					if (sslCtx != null) {
						ch.pipeline().addLast(sslCtx.newHandler(ch.alloc(), HOST, PORT));
					}
					ch.pipeline().addLast(new ObjectEncoder(), new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
							new PushClientHandler());
				}
			});
			b.connect(HOST, PORT).sync().channel().closeFuture().sync();
		} catch (InterruptedException e) {
			log.error("PushClient 启动异常！", e);
		} catch (SSLException e) {
			log.error("PushClient 启动异常！SSL的相关问题。", e);
		} finally {
			group.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		new Thread(new PushClient()).start();
	}
}
