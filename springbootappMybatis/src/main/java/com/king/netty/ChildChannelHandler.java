package com.king.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class ChildChannelHandler extends ChannelInitializer<SocketChannel>{

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast("http-codec",new HttpServerCodec());
		ch.pipeline().addLast("aggregator",new HttpObjectAggregator(65536));
		ch.pipeline().addLast(new WebSocketServerProtocolHandler("/websocket"));
		ch.pipeline().addLast("http-chunked",new ChunkedWriteHandler());
		ch.pipeline().addLast("handler",new NettyServerHandler());
	}

}
