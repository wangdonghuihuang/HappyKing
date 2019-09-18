package com.king.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.test.context.junit4.SpringRunner;

/**因为WebSocket是servlet容器所支持的，所以需要加载servlet容器：
webEnvironment参数为springboot指定ApplicationContext类型。
webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT表示内嵌的服务器将会在一个随机的端口启动。*/
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
public class springbootRedisTest {

}
