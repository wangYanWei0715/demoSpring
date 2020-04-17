package com.gupaoedu.vip.spring.formework.core;

/**
 * @description ioc的顶层接口， 接口中定义了bean的属性
 *
 * @author 王延伟
 * @createTime 2020/4/7 20:58
 * @method
 * @param
 * @return
 * @throws TODO
 */
public interface GPBeanFactory {

    Object getBean(String beanName) throws Exception;

    Object getBean(Class<?> beanClass) throws Exception;
}
