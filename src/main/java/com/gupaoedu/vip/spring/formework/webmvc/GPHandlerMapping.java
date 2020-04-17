package com.gupaoedu.vip.spring.formework.webmvc;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @author 王延伟
 * @description TODO
 * @createTime 2020年4月8日$ 20点11分$
 */
public class GPHandlerMapping {

    private Object controller;

    private Method method;

    private Pattern pattern;

    public GPHandlerMapping(Pattern pattern, Object controller, Method method) {
        this.controller = controller;
        this.method = method;
        this.pattern = pattern;

    }


    public Object getController() {
        return controller;
    }


    public void setController(Object controller) {
        this.controller = controller;
    }


    public Method getMethod() {
        return method;
    }


    public void setMethod(Method method) {
        this.method = method;
    }


    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }


    public Pattern getPattern() {
        return pattern;
    }
}
