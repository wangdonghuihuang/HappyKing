package com.king.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
/**获取bean，帮助类*/
@Component
public class ApplicationContextHelper implements ApplicationContextAware{
//spring容器上下文
	private static ApplicationContext APPCONTEXT;
	/**此方法可以把applicationContext对象inject到当前类中作为一个静态成员变量
	 * @param applicationContext
	 *  ApplicationContext 对象
	 *  @throws BeansException
	 * */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		APPCONTEXT=applicationContext;
	}
/**根据bean的名字，返回一个bean对象*/
	public static Object getBean(String beanName) {
		return APPCONTEXT.getBean(beanName);
	}
	/**根据一个class类，返回一个bean对象*/
	public static Object getBean(Class<?> classone) {
		return APPCONTEXT.getBean(classone);
	}
}
