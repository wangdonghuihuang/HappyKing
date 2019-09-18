package com.king.common.redis;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.StringRedisTemplate;

public class RedisSelectConfig extends StringRedisTemplate{
	 @Override
	    protected RedisConnection createRedisConnectionProxy(RedisConnection pm) {
	        return super.createRedisConnectionProxy(pm);
	    }

	    @Override
	    protected RedisConnection preProcessConnection(RedisConnection connection, boolean existingConnection) {
	        Integer db;
	        if((db = RedisSelectSuport.getSelect()) != null){
	            connection.select(db);
	        }
	        return super.preProcessConnection(connection, existingConnection);
	    }
}
