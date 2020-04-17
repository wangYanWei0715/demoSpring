package com.gupaoedu.vip.spring.formework.context;

import com.gupaoedu.vip.spring.formework.beans.GPApplicationContext;

/**
 * @description
 *
 * @author 王延伟
 * @createTime 2020/4/7 22:35
 * @method
 * @param
 * @return
 * @throws TODO
 */
public interface GPApplicationContextAware {

    /**
     * @description 通过解耦的方式获得ioc容器的顶层设计
     * 后面将通过一个监听器去扫面所有的类，只要实现了此接口
     * 将自动调用setApplicationContext（）方法，从而将ioc容器注入目标类中
     *
     * @author 王延伟
     * @createTime 2020/4/7 22:36
     * @method setApplicationContext
     * @param [applicationContext]
     * @return void
     * @throws TODO
     */
    void setApplicationContext(GPApplicationContext applicationContext);

}
