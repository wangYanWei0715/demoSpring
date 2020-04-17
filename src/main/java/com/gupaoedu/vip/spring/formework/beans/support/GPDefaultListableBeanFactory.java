package com.gupaoedu.vip.spring.formework.beans.support;

import com.gupaoedu.vip.spring.formework.beans.GPBeanDefinition;
import com.gupaoedu.vip.spring.formework.context.GPAbstractApplicationContext;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 王延伟
 * @description TODO
 * @createTime 2020年4月7日$ 21点24分$
 */
public class GPDefaultListableBeanFactory extends GPAbstractApplicationContext {

    //存储注册信息的BeanDefinition
    protected  final Map<String,GPBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, GPBeanDefinition>();


}
