package com.king.common.redis;

import java.time.Duration;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
//此注解会自动获取application.properties中的redis配置值
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties(RedisProperties.class)
public class RedisTemplateConfig {
private RedisProperties redisProperties;
public RedisTemplateConfig(RedisProperties redisProperties) {
	this.redisProperties=redisProperties;
}
@Bean
@Primary
public JedisConnectionFactory jedisConnectionFactory() {
	RedisStandaloneConfiguration configuration=new RedisStandaloneConfiguration();
	configuration.setHostName(redisProperties.getHost());
	configuration.setPort(redisProperties.getPort());
	configuration.setPassword(RedisPassword.of(redisProperties.getPassword()));
	configuration.setDatabase(redisProperties.getDatabase());
	return new JedisConnectionFactory(configuration,getJedisClientConfiguration());
}
/**获取jedis客户端配置*/
private JedisClientConfiguration getJedisClientConfiguration() {
	JedisClientConfiguration.JedisClientConfigurationBuilder builder=JedisClientConfiguration.builder();
	//isssl，网络连接安全，一种安全协议
	if(redisProperties.isSsl()) {
		builder.useSsl();
	}
//Duration，执行时间，超时时间
	if(redisProperties.getTimeout()!=null) {
		Duration timeOut=redisProperties.getTimeout();
		builder.readTimeout(timeOut).connectTimeout(timeOut);
	}
	RedisProperties.Pool pool=redisProperties.getJedis().getPool();
	if(pool!=null) {
		builder.usePooling().poolConfig(jedisPoolConfig(pool));
	}
	return builder.build();
}
/**jedis连接池配置*/
private JedisPoolConfig jedisPoolConfig(RedisProperties.Pool redisProperties) {
	JedisPoolConfig config=new JedisPoolConfig();
	config.setMaxIdle(redisProperties.getMaxIdle());
	config.setMaxTotal(redisProperties.getMaxActive());
	config.setMinIdle(redisProperties.getMinIdle());
	if(redisProperties.getMaxWait()!=null) {
		config.setMaxWaitMillis(redisProperties.getMaxWait().toMillis());
	}
	return config;
}
/**redis的选择模板
 * 有很多人问为什么用注解primary,不多说，这里是说明作用:自动装配时，如果出现多个bean的候选者，被注解为primary的bean将作为首选者，优先选择，否则会抛出异常。*/
@Bean(name="redisTemplate")
@Primary
public RedisSelectConfig redisSelectConfig() {
	RedisSelectConfig tem=new RedisSelectConfig();
	tem.setConnectionFactory(jedisConnectionFactory());
	return tem;
}
}
