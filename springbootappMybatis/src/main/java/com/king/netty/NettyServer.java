package com.king.netty;

import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.king.common.CommonConfig;
import com.king.util.ApplicationContextHelper;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
@Service
public class NettyServer {
	private static Logger log=LoggerFactory.getLogger(NettyServer.class);
@PostConstruct
	public void initServer() {
	new Thread() {
		public void run() {
			new NettyServer().run();
		}
	}.start();
}
public void run() {
	EventLoopGroup bossGroup=new NioEventLoopGroup();
	EventLoopGroup workGroup=new NioEventLoopGroup();
	try {
		//监听socket服务
		ServerBootstrap socket=new ServerBootstrap();
		socket.group(bossGroup,workGroup);
		//设置非阻塞
		socket.channel(NioServerSocketChannel.class);
		socket.childHandler(new ChildChannelHandler());
		CommonConfig config=(CommonConfig) ApplicationContextHelper.getBean(CommonConfig.class);
		//绑定端口，开始接收进来的数据
		Channel channel=socket.bind(config.getWebSocketPort()).sync().channel();
		//服务端管道关闭的监听器并同步阻塞，知道channel关闭，线程才会往下执行，结束进程。主线程执行到这里就wait子线程结束，子线程才是真正监听和接收请求的。子线程就是netty启动的监听端口的线程。
		//即closeFuture()是 开启了一个channel的监听器，负责监听channel是否关闭的状态，如果未来监听到channel关闭了，子线程才会释放，syncUninterruptibly()让主线程同步等待子线程结果。
		//补充:channel.close()才是主动关闭通道的方法。
		channel.closeFuture().sync();
		log.info("服务端启动成功");
	} catch (Exception e) {
		log.error(e.getMessage(),e);
	}finally {
		bossGroup.shutdownGracefully();
		workGroup.shutdownGracefully();
	}
}
}
