package com.king.websocket;

import java.io.IOException;
import java.util.Iterator;

import org.apache.tomcat.util.bcel.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.king.common.Constant;
import com.king.netty.NettyCache;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;


/**消息发送类，可以再此类上做自己的业务扩展，比如，有商品表，与商品匹配发送信息等*/
@Component
public class WSSender {
private static Logger logger=LoggerFactory.getLogger(WSSender.class);
@SuppressWarnings("rawtypes")
public static void send(Object message,int pushType) throws IOException {
		send(message,pushType,(MsgFilter)null);
}
public static void send(Object message,int pushType,MsgFilter filter) {
	boolean flag;
	if(Constant.PUSH_TEST_ONE==pushType||Constant.PUSH_TEST_TWO==pushType) {
		flag=true;
	}else {
		flag=false;
	}
	Iterator<Channel> it=NettyCache.channels.get(pushType).iterator();
	while(it.hasNext()){

        Channel channel = it.next();

        JSONObject joMsg = new JSONObject();

        joMsg.put("pushType", NettyCache.channelMessage.get(channel.id()).getString("pushType"));
        JSONObject clientInfo = NettyCache.channelMessage.get(channel.id());
        if(filter !=null ){
            Object res =  filter.filter(message,clientInfo);
            if(res==null){
                continue;
            }else{
                message = res;
            }

        }
        joMsg.put("data", message);
        String result="测试推送成功";
        logger.debug("================" + result);
        TextWebSocketFrame sockTxt = new TextWebSocketFrame(result);
        channel.writeAndFlush(sockTxt);
    }
}
}
