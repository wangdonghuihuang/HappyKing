package com.king.websocket;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
/**websocket连接配置，信息收发处理类*/

import com.alibaba.fastjson.JSONObject;
@ServerEndpoint(value="/websocket")
@Component
public class DataPushSocket {
private static Logger logger=LoggerFactory.getLogger(DataPushSocket.class);
//静态变量，记录当前在线连接数。最好设计成线程安全的
private static int onlineCount=0;
//concurrent包的线程安全set,用来存放每个客户端对应的MyWebSocket对象
public static CopyOnWriteArraySet<DataPushSocket> wsClientMap=new CopyOnWriteArraySet<>();
//与某个客户端的连接会话，需要通过此来给客户端发送数据
private Session session;
//前端请求参数
private JSONObject jsonObject;
//获取DataPushSocket对象
public DataPushSocket getDataPushSocket() {
	return this;
}
public DataPushSocket() {
	
}
/**
 * 连接建立成功调用的方法
 * @param session 当前会话session
 */
@OnOpen
public void onOpen (Session session){
    this.session = session;
    wsClientMap.add(this);
    addOnlineCount();
    logger.info(session.getId()+"有新链接加入，当前链接数为：" + wsClientMap.size());
}

/**
 * 连接关闭
 */
@OnClose
public void onClose (){
    wsClientMap.remove(this);
    subOnlineCount();
    logger.info("有一链接关闭，当前链接数为：" + wsClientMap.size());
}

/**
 * 收到客户端消息
 * @param message 客户端发送过来的消息
 * @param session 当前会话session
 * @throws IOException
 */
@OnMessage
public void onMessage (String message, Session session) throws IOException {
    logger.info("接收到客户端发送的消息:" + message);
    jsonObject= JSONObject.parseObject(message);
    try{
        Iterator<DataPushSocket> it = wsClientMap.iterator();
        while (it.hasNext()){
            DataPushSocket sock = it.next();
            if(sock.session.getId().equals(this.session.getId())){
                sock.jsonObject=jsonObject;
            }
        }

    }catch(Exception e){
    	logger.error(e.getMessage());
    }
}

/**
 * 发生错误
 */
@OnError
public void onError(Session session, Throwable error) {
    logger.error("wsClientMap发生错误!",error);
}

/**
 * 给所有客户端群发消息
 * @param message 消息内容
 * @throws IOException
 */
public void sendMsgToAll(String message) throws IOException {
    for ( DataPushSocket item : wsClientMap ){
       	 if(item.session.isOpen()) {
       		item.session.getBasicRemote().sendText(message);
         } 
    }
    logger.debug("成功群送一条消息:" + wsClientMap.size());
}


public void sendMessage (String message) throws IOException {
	 if(session.isOpen()) {
		 this.session.getBasicRemote().sendText(message);
		 logger.debug("成功发送一条消息:" + message);
     } 
}
public static synchronized  int getOnlineCount (){
    return DataPushSocket.onlineCount;
}

public static synchronized void addOnlineCount (){
    DataPushSocket.onlineCount++;
}

public static synchronized void subOnlineCount (){
    DataPushSocket.onlineCount--;
}


public Session getSession() {
    return session;
}

public void setSession(Session session) {
    this.session = session;
}

public static Logger getLogger() {
	return logger;
}

public static void setLogger(Logger logger) {
	DataPushSocket.logger = logger;
}

public static CopyOnWriteArraySet<DataPushSocket> getWsClientMap() {
	return wsClientMap;
}

public static void setWsClientMap(CopyOnWriteArraySet<DataPushSocket> wsClientMap) {
	DataPushSocket.wsClientMap = wsClientMap;
}

public JSONObject getJsonMessage() {
	return jsonObject;
}

public void setJsonMessage(JSONObject jsonMessage) {
	this.jsonObject = jsonMessage;
}

public static void setOnlineCount(int onlineCount) {
	DataPushSocket.onlineCount = onlineCount;
}
}
