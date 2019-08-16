package com.king.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
/**通道打开，关闭，接收消息处理类，异常捕获*/
public class NettyServerHandler extends SimpleChannelInboundHandler<Object>{
	private static Logger log=LoggerFactory.getLogger(NettyServerHandler.class);
	//捕获异常
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
		}
	//打开通道
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
//连接默认添加
		NettyCache.defaultGroup.add(ctx.channel());
		log.info("客户端与服务端连接开启:"+ctx.channel().remoteAddress().toString());
	}
	//关闭通道
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//移除通道以及通道对应的消息
		if(NettyCache.defaultGroup.contains(ctx.channel())) {
			NettyCache.defaultGroup.remove(ctx.channel());
		}
		if(NettyCache.channelMessage.containsKey(ctx.channel().id())) {
			NettyCache.channels.get(NettyCache.channelMessage.get(ctx.channel().id()).getIntValue("pushType"))
            .remove(ctx.channel());
		}
		if(NettyCache.channelMessage.containsKey(ctx.channel().id())) {
			NettyCache.channelMessage.remove(ctx.channel().id());
			
			log.info("客户端与服务端连接关闭:"+ctx.channel().remoteAddress().toString());
		}
	}
	//通道读取数据完成
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
	//接收消息
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
		if(msg instanceof WebSocketFrame) {
			handlerWebocketFrame(ctx, (WebSocketFrame) msg);
		}
	}
	
	//返回应答消息
	private void handlerWebocketFrame(ChannelHandlerContext ctx,WebSocketFrame frame) {
		String requestMessage=((TextWebSocketFrame)frame).text();
		JSONObject jsonMessage=JSONObject.parseObject(requestMessage);
		log.info("接收到客户端发送的消息"+requestMessage);
		if(NettyCache.channelMessage.containsKey(ctx.channel().id())) {
			NettyCache.channels.get(NettyCache.channelMessage.get(ctx.channel().id()).getIntValue("pushType")).remove(ctx.channel());
		}
		NettyCache.channels.get(jsonMessage.getIntValue("pushType")).add(ctx.channel());
		NettyCache.channelMessage.put(ctx.channel().id(), jsonMessage);
		if(NettyCache.defaultGroup.contains(ctx.channel())) {
			NettyCache.defaultGroup.remove(ctx.channel());
		}
	}
}
