package com.king.netty;

import com.alibaba.fastjson.JSONObject;
import com.king.common.Constant;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**所有类型的通道保存类*/
@Component
public class NettyCache {

    public static Map<Integer,ChannelGroup> channels = new HashMap<Integer,ChannelGroup>();
    public static Map<ChannelId,JSONObject> channelMessage = new ConcurrentHashMap<ChannelId,JSONObject>();
    public static ChannelGroup defaultGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    @PostConstruct
    public void initAllChannel(){
        String[] pushTypes = getAllPushType();
        for (int i=0;i<pushTypes.length;i++){
            channels.put(Integer.valueOf(pushTypes[i]),new DefaultChannelGroup(GlobalEventExecutor.INSTANCE));
        }
    }

    /**
     * 获取所有推送的类型
     */
    private String[] getAllPushType(){
        Field[] fields = Constant.class.getDeclaredFields();
        String[] allType = null;
        List<String> pushType = new ArrayList<String>();
        for(int i = 0;i<fields.length;i++){
            String fieldName = fields[i].getName();
            if(fieldName.startsWith("PUSH_")){//
                try {
                    Object s = Constant.class.getField(fieldName).get(null);
                    pushType.add(s.toString());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        }
        allType = pushType.toArray(new String[pushType.size()]);
        return allType;
    }
}
