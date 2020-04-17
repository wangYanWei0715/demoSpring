package com.gupaoedu.vip.spring.demo.action;

import com.gupaoedu.vip.spring.demo.service.IModifyService;
import com.gupaoedu.vip.spring.demo.service.IQueryService;
import com.gupaoedu.vip.spring.formework.annotation.GPAutowired;
import com.gupaoedu.vip.spring.formework.annotation.GPController;
import com.gupaoedu.vip.spring.formework.annotation.GPRequestMapping;
import com.gupaoedu.vip.spring.formework.annotation.GPRequestParam;
import com.gupaoedu.vip.spring.formework.webmvc.GPModelAndView;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 王延伟
 * @description TODO
 * @createTime 2020年4月8日$ 23点17分$
 */
@GPController
@GPRequestMapping("/web")
public class MyAction {


    @GPAutowired
    private IQueryService queryService;

    @GPAutowired
    private IModifyService modifyService;

    @GPRequestMapping("/query")
    public GPModelAndView query(HttpServletRequest request , HttpServletResponse response,@GPRequestParam("name") String name){
        String result = queryService.query(name);
        return out(response,result);
    }


    private GPModelAndView out(HttpServletResponse response, String result) {
        try {
            response.getWriter().write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}
