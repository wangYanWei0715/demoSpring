package com.gupaoedu.vip.spring.formework.webmvc;

import java.util.Map;

/**
 * @author 王延伟
 * @description TODO
 * @createTime 2020年4月8日$ 21点29分$
 */
public class GPModelAndView {

    private String viewName;

    private Map<String,?> model;


    public GPModelAndView(String viewName, Map<String,?> model) {
        this.viewName = viewName;
        this.model = model;
    }


    public void setViewName(String viewName) {
        this.viewName = viewName;
    }


    public void setModel(Map<String, ?> model) {
        this.model = model;
    }


    public GPModelAndView(String viewName) {
        this(viewName,null)  ;

    }


    public String getViewName() {
        return viewName;
    }


    public Map<String, ?> getModel() {
        return model;
    }
}
