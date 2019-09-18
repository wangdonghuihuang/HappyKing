package com.king.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.king.common.redis.RedisSelect;
import com.king.util.RedisUtilsone;

@RestController
@RequestMapping(value="/selectone")
public class RedisSelectController {
	@Autowired
	private StringRedisTemplate redisTemplate;
	@Autowired
	private RedisUtilsone utilsOne;
@RequestMapping(value="/setZhi")
@RedisSelect(1)
public String getZhi() {
	//redisTemplate.opsForValue().set("hello", "测试");
	/**这里调用工具类操作，会出现问题，无法进入到类中，是因为RedisAspect切面类中，有判断open(applications配置中)，为false会不执行point.proceed，所以自然进不到工具类中。
	 * 将配置改为true，可以。*/
	utilsOne.set("hello", "测试");
	String hello=utilsOne.get("hello");
	return hello;
}
@RequestMapping(value="/getZhi")
@RedisSelect(1)
public String getNum() {
	String num=redisTemplate.opsForValue().get("hello");
	return num;
}
}
