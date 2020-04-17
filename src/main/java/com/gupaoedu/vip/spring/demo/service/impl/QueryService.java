package com.gupaoedu.vip.spring.demo.service.impl;

import com.gupaoedu.vip.spring.demo.service.IQueryService;
import com.gupaoedu.vip.spring.formework.annotation.GPService;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 王延伟
 * @description TODO
 * @createTime 2020年4月8日$ 23点06分$
 */
@Slf4j
@GPService
public class QueryService implements IQueryService {
    @Override
    public String query(String name) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
        String time = sdf.format(new Date());
        String json = "{name:\""+name+"\",time:\""+time+"\"}";
        log.info("这是在业务方法中打印的："+json);
        return json;
    }
}
