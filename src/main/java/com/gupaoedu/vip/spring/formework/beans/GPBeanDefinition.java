package com.gupaoedu.vip.spring.formework.beans;

import com.sun.org.apache.regexp.internal.RE;

/**
 * @author 王延伟
 * @description 用来保存bean的相关信息
 * @createTime 2020年4月7日$ 21点06分$
 */
public class GPBeanDefinition {

    //原生bean的全类名
    private String beanClassName;

    //标记是否延迟加载
    private boolean lazyInit = false;

    //保存beanName,在ioc容器中存储的key
    private String factoryBeanName;

    public String getBeanClassName(){
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName){
        this.beanClassName = beanClassName;
    }

    public boolean isLazyInit(){
        return lazyInit;
    }

    public void setLazyInit(boolean lazyInit){
        this.lazyInit = lazyInit;
    }


    public String getFactoryBeanName() {
        return factoryBeanName;
    }


    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }
}
