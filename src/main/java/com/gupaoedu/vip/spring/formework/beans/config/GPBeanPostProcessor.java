package com.gupaoedu.vip.spring.formework.beans.config;

/**
 * @author 王延伟
 * @description TODO
 * @createTime 2020年4月7日$ 22点47分$
 */
public class GPBeanPostProcessor {

    //在bean的初始化之前提供回调入口
    public Object postProcessBeforeInitialization(Object bean,String beanName) throws Exception{
        return bean;
    }


    //在bean的初始化之后提供回调入口
    public Object postProcessAfterInitialization(Object bean,String beanName) throws Exception{
        return bean;
    }

}
