package com.king.common.redis;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RedisAspect {
private static Logger log=LoggerFactory.getLogger(RedisAspect.class);
@Value("${king.redis.open}")
private boolean open;
@Value("${spring.redis.database}")
private int defaultDatabase;
/**这里解释下point.proceed用法。
 * 环绕通知，简单说就是:环绕通知=前绕通知+目标方法执行+后置通知。
 * proceed方法就是用于启动目标方法执行的。
 * 如果在Around中不调用point.proceed(),那@Before注解的方法不会调用，不过after还是会调用*/
@Around("execution(* com.king.util.RedisUtilsone.*(..))")
public Object around(ProceedingJoinPoint point) throws Throwable {
	Object result=null;
	if(open) {
		try {
			//调用执行目标方法(result为目标方法执行结果)
			result=point.proceed();
		} catch (Exception e) {
			// TODO: handle exception
			log.error("redis error"+e);
		}
	}
	return result;
}
@Around("@annotation(com.king.common.redis.RedisSelect)")
@ConditionalOnBean(RedisSelectConfig.class)
public Object configRedis(ProceedingJoinPoint point) throws Throwable {
	int db=defaultDatabase;
	try {
		MethodSignature signature=(MethodSignature) point.getSignature();
		Method method=signature.getMethod();
		RedisSelect config=method.getAnnotation(RedisSelect.class);
		if(config!=null) {
			db=config.value();
		}
		RedisSelectSuport.select(db);
		return point.proceed();
	}finally {
		RedisSelectSuport.select(defaultDatabase);
		log.debug("redis reset {} to {}",db,defaultDatabase);
	}
}
}
