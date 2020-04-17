package com.gupaoedu.vip.spring.demo.service.impl;

import com.gupaoedu.vip.spring.demo.service.IModifyService;

/**
 * @author 王延伟
 * @description TODO
 * @createTime 2020年4月8日$ $
 */
public class ModifyService implements IModifyService {

    @Override
    public String add(String name, String addr) {
        return "ModifyService add, name="+name+",addr="+addr;
    }


    @Override
    public String edit(Integer id, String name) {
        return "ModifyService edit, id="+id+",name="+name;
    }


    @Override
    public String remove(Integer id) {
        return "ModifyService remove , id="+id;
    }
}
