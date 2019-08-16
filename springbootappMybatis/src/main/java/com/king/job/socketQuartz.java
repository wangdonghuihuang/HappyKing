package com.king.job;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.king.websocket.WSSender;

@Component
public class socketQuartz {
private Logger log=LoggerFactory.getLogger(socketQuartz.class);
//固定延迟5秒调用一次，在上一次结束之后，5秒在执行
    @Scheduled(fixedDelay = 5000)
    public void send(){
    	try {
			WSSender.send("这是成功了",1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}