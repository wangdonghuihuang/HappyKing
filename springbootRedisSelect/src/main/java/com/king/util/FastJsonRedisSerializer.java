package com.king.util;

import java.nio.charset.Charset;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.cache.support.NullValue;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class FastJsonRedisSerializer<T> implements RedisSerializer<T>{
public static final Charset DEFAULT_CHARSET=Charset.forName("UTF-8");
public Class<T> clazz;
 public FastJsonRedisSerializer(Class<T> clazz) {
	// TODO Auto-generated constructor stub
	 super();
	 this.clazz=clazz;
}
 @Override
 public byte[] serialize(T t) throws SerializationException {
     if (t == null || t instanceof NullValue) {
         return new byte[0];
     }
     return JSONObject.toJSONString(t, SerializerFeature.WriteClassName).getBytes(DEFAULT_CHARSET);
 }

 @Override
 public T deserialize(byte[] bytes) throws SerializationException {
     if (bytes == null || bytes.length <= 0) {
         return null;
     }
     String str = new String(bytes, DEFAULT_CHARSET);
     T t ;
     try {
         t = (T) JSONObject.parseObject(str, clazz);
     }catch (Exception e){
         t = (T)new DeserializingConverter().convert(bytes);
         return t;
     }
     return t;
 }

}
