package com.gupaoedu.vip.spring.formework.webmvc;

import com.gupaoedu.vip.spring.formework.annotation.GPRequestParam;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 王延伟
 * @description TODO
 * @createTime 2020年4月8日$ 20点19分$
 */
public class GPHandlerAdapter {

    public GPModelAndView handle(HttpServletRequest req, HttpServletResponse resp, GPHandlerMapping handler)
        throws InvocationTargetException, IllegalAccessException {

        GPHandlerMapping handlerMapping = (GPHandlerMapping) handler;

        HashMap<String, Integer> paramMapping = new HashMap<>();

        Annotation[][] pa = handlerMapping.getMethod().getParameterAnnotations();

        for (int i = 0; i <pa.length ; i++) {
            for ( Annotation a : pa[i]){
                if (a instanceof GPRequestParam){
                    String paramName = ((GPRequestParam) a).value();
                    if (! "".equals(paramName.trim())){
                        paramMapping.put(paramName,i);
                    }
                }
            }
        }
        //拿到方法上面的形参列表 放到map里面
        Class<?>[] paramTypes = handlerMapping.getMethod().getParameterTypes();
        for (int i = 0; i <paramTypes.length; i++) {
            Class<?> type = paramTypes[i];
            if (type == HttpServletResponse.class || type == HttpServletRequest.class){
                paramMapping.put(type.getName(),i);
            }
        }

        //拿到所有的实参
        Map<String,String[]> reqParameterMap =req.getParameterMap();

        Object[] paramValues = new Object[paramTypes.length];

        for (Map.Entry<String,String[]> param : reqParameterMap.entrySet()){
            String value = Arrays.toString(param.getValue()).replaceAll("\\[|\\]", "").replaceAll("\\s", "");
            if (!paramMapping.containsKey(param.getKey())){continue;}

            Integer index = paramMapping.get(param.getKey());
            paramValues[index] = caseStringValue(value,paramTypes[index]);

        }

        if (paramMapping.containsKey(HttpServletRequest.class.getName())){
            Integer respIndex = paramMapping.get(HttpServletRequest.class.getName());
            paramValues[respIndex] = req;
        }

        if (paramMapping.containsKey(HttpServletResponse.class.getName())){
            Integer respIndex = paramMapping.get(HttpServletResponse.class.getName());
            paramValues[respIndex] = resp;
        }

        Object result = handlerMapping.getMethod().invoke(handlerMapping.getController(), paramValues);

        if (result == null){return  null;}

        boolean b = handlerMapping.getMethod().getReturnType() == GPModelAndView.class;

        if (b){
            return (GPModelAndView) result;
        }else {
            return null;
        }

    }


    private Object caseStringValue(String value, Class<?> clazz) {
        if (clazz == String.class){
            return value;
        }else if (clazz == Integer.class){
            return Integer.valueOf(value);
        }else if (clazz == int.class){
            return Integer.valueOf(value).intValue();
        }else {
            return null;
        }

    }


    public boolean supports(GPHandlerMapping handler) {
        return (handler instanceof GPHandlerMapping);

    }
}
