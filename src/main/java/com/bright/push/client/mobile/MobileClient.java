/**  
 * 项目名称  ：  bright
 * 文件名称  ：  MobileClient.java
 * 日期时间  ：  2020年12月10日 - 下午3:16:04
 */
package com.bright.push.client.mobile;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.SSLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bright.push.util.Config;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
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
import io.netty.handler.timeout.IdleStateHandler;

/**
 * 移动端的客户端。
 * 
 * @author 王晓峰
 * @since 1.0
 */
public class MobileClient implements Runnable {

	/** log:日志 */
	private static Logger log = LoggerFactory.getLogger(MobileClient.class);
	public static final String HOST = Config.getPros().getProperty("host");
	public static final int PORT = Integer.parseInt(Config.getPros().getProperty("port"));
	/** RECONNECT_DELAY:重新连接等待时间 */
	public static final int RECONNECT_DELAY = Integer.parseInt(System.getProperty("reconnectDelay", "20"));
	/** READ_TIMEOUT:读操作超时时间 */
	public static final int READ_TIMEOUT = Integer.parseInt(System.getProperty("readTimeout", "10"));
	/** WRITE_TIMEOUT:写操作超时时间 */
	public static final int WRITE_TIMEOUT = Integer.parseInt(System.getProperty("writeTimeout", "10"));
	/** handler:处理器 */
	private MobileClientHandler handler = new MobileClientHandler();

	/**
	 * 主方法启动客户端。
	 * 
	 * @param args 参数
	 * @throws Exception 异常
	 */
	public static void main(String[] args) throws Exception {
		ExecutorService service = Executors.newFixedThreadPool(1);
		service.execute(new MobileClient());
	}

	/**
	 * 配置引导程序。
	 * 
	 * @param b 引导程序
	 * @return 引导程序
	 * @throws SSLException
	 */
	public Bootstrap configureBootstrap(Bootstrap b) throws SSLException {
		return configureBootstrap(b, new NioEventLoopGroup());
	}

	/**
	 * 配置引导程序。
	 * 
	 * @param b 引导程序
	 * @param g 引导程序
	 * @return 引导程序
	 * @throws SSLException
	 */
	public Bootstrap configureBootstrap(Bootstrap b, EventLoopGroup g) throws SSLException {
		boolean ssl = Boolean.parseBoolean(Config.getPros().getProperty("ssl"));
		final SslContext sslCtx;
		if (ssl) {
			sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
		} else {
			sslCtx = null;
		}
		b.group(g).channel(NioSocketChannel.class).remoteAddress(HOST, PORT)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel ch) throws Exception {
						ChannelPipeline p = ch.pipeline();
						if (sslCtx != null) {
							p.addLast(sslCtx.newHandler(ch.alloc(), HOST, PORT));
						}
						p.addLast(new ObjectEncoder(), new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
								new IdleStateHandler(READ_TIMEOUT, WRITE_TIMEOUT, 0), handler);
					}
				});
		return b;
	}

	/**
	 * 连接到服务器。
	 * 
	 * @param b 引导程序
	 */
	public void connect(Bootstrap b) {
		b.connect().addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if (future.cause() != null) {
					handler.startTime = -1;
					log.info("Failed to connect: " + future.cause());
				}
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		Bootstrap b;
		try {
			b = configureBootstrap(new Bootstrap());
			ChannelFuture cf = b.connect();
			log.info("客户端启动：" + cf.channel().isOpen());
			// cf.channel().close();
		} catch (SSLException e) {
			log.error("MobileClient 启动异常！SSL的相关问题。", e);
		}
	}
}
