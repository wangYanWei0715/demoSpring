package com.gupaoedu.vip.spring.formework.context;

/**
 * @author 王延伟
 * @description Ioc容器类的顶层抽象类
 * @createTime 2020年4月7日$ 21点21分$
 */
public abstract class GPAbstractApplicationContext {

    //受保护，只提供给子类重写
    public void refresh() throws Exception{}
}
