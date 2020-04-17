package com.gupaoedu.vip.spring.formework.beans;

/**
 * @author 王延伟
 * @description 主要用来封装创建后的对象实例，代理对象（proxy object）或者原生对象都由BeanWrapper来保存
 * @createTime 2020年4月7日$ 21点15分$
 */
public class GPBeanWapper {

    private Object wrappedInstance;

    private Class<?> wrappedClass;


    public GPBeanWapper(Object wrappedInstance) {
        this.wrappedInstance = wrappedInstance;
    }


    public Object getWrappedInstance() {
        return this.wrappedInstance;
    }


    public Class<?> getWrappedClass() {
        return this.wrappedInstance.getClass();
    }
}
