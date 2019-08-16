package com.king.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/** 项目地址信息配置类 */
@Component
@ConfigurationProperties(prefix = "common")
@PropertySource("classpath:config/config.properties")
public class CommonConfig {
	// websocekt端口号
	private int webSocketPort;
	public int getWebSocketPort() {
		return webSocketPort;
	}

	public void setWebSocketPort(int webSocketPort) {
		this.webSocketPort = webSocketPort;
	}

}
