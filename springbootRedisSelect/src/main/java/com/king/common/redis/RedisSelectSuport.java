package com.king.common.redis;
/**redis切换库支持类*/

public class RedisSelectSuport {
private static final ThreadLocal<Integer> SELECT_CONTEXT=new ThreadLocal<>();
public static void select(int db) {
	SELECT_CONTEXT.set(db);
}
public static Integer getSelect() {
	return SELECT_CONTEXT.get();
}
}
