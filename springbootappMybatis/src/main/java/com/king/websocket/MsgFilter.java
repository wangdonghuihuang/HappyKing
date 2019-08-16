package com.king.websocket;
/**推送消息时用来过滤消息*/
public interface MsgFilter<T> {
T filter(T obj,Object clientInfo);
}
