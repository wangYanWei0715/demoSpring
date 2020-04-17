package com.gupaoedu.vip.spring.formework.webmvc.servlet;

import com.gupaoedu.vip.spring.formework.annotation.GPController;
import com.gupaoedu.vip.spring.formework.annotation.GPRequestMapping;
import com.gupaoedu.vip.spring.formework.beans.GPApplicationContext;
import com.gupaoedu.vip.spring.formework.webmvc.GPHandlerAdapter;
import com.gupaoedu.vip.spring.formework.webmvc.GPHandlerMapping;
import com.gupaoedu.vip.spring.formework.webmvc.GPModelAndView;
import com.gupaoedu.vip.spring.formework.webmvc.GPView;
import com.gupaoedu.vip.spring.formework.webmvc.GPViewResolver;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 王延伟
 * @description TODO
 * @createTime 2020年4月7日$ 20点32分$
 */
@Slf4j
public class GPDispatcherServlet extends HttpServlet {

    private final String LOCATION = "contextConfigLocation";

    private List<GPHandlerMapping> handlerMappings = new ArrayList<GPHandlerMapping>();

    private Map<GPHandlerMapping, GPHandlerAdapter> handlerAdpters = new HashMap<GPHandlerMapping, GPHandlerAdapter>();



    private List<GPViewResolver> viewResolvers = new ArrayList<GPViewResolver>();

    private GPApplicationContext context;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            doDispatch(req, resp);
        } catch (Exception e) {
           // processDispatchResult(req,resp,new GPModelAndView("500"));

            resp.getWriter().write("<font size='25' color = 'blue'> 500 Exception </font> <br/> Deteils: <br/>" + Arrays
                .toString(e.getStackTrace()).replaceAll("\\[|\\]", "").replaceAll("\\s", "\r\n")
                + "<font color = 'green'> <i> Copyright@GupaoEDU</i> </font>");

            e.printStackTrace();
        }


    }


    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) {
        try {
            //找到handlerMapping
            GPHandlerMapping handler = getHandler(req);
            if (null == handler) {

                processDispatchResult(req, resp, new GPModelAndView("404"));
                return;
            }
            GPHandlerAdapter ha = getHandlerAdapter(handler);


            GPModelAndView mv = ha.handle(req, resp, handler);

            processDispatchResult(req, resp, mv);

        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }


    private GPHandlerAdapter getHandlerAdapter(GPHandlerMapping handler) {
        if (this.handlerAdpters.isEmpty()) {
            return null;
        }
        GPHandlerAdapter ha = this.handlerAdpters.get(handler);
        if (ha.supports(handler)) {
            return ha;
        }
        return null;

    }


    private void processDispatchResult(HttpServletRequest req, HttpServletResponse resp, GPModelAndView mv) {
        try {
            if (null == mv) {
                return;
            }

            if (this.viewResolvers.isEmpty()) {
                return;
            }


                for (GPViewResolver viewResolver : this.viewResolvers) {
                    GPView view = viewResolver.resolveViewName(mv.getViewName(), null);
                    if (null != view) {

                        view.render(mv.getModel(), req, resp);

                        return;
                    }


                }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private GPHandlerMapping getHandler(HttpServletRequest req) {

        if (this.handlerMappings.isEmpty()) {
            return null;
        }

        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = url.replaceAll(contextPath, "").replaceAll("/+", "/");

        for (GPHandlerMapping handler : this.handlerMappings) {
            Matcher matcher = handler.getPattern().matcher(url);
            if (!matcher.matches()) {
                continue;
            }
            return handler;
        }
        return null;
    }


    @Override
    public void init(ServletConfig config) throws ServletException {
        context = new GPApplicationContext(config.getInitParameter(LOCATION));
        initStrategies(context);
    }


    protected void initStrategies(GPApplicationContext context) {

        // ===========================这里就是传说中的九大组件=======================
     //   initMultipartResolver(context); //文件上传解析，如果请求类型是multpart,将通过MultipartResolver进行文件上传解析

        //本地化解析
      //  initLocaleResolver(context);

        //主题解析
       // initThemeResolver(context);

        //通过HandlerMapping将请求映射到处理器
        initHandlerMappings(context);

        //通过handlerAdapter进行多类型的参数动态匹配
        initHandlerAdapters(context);

        //initHandlerExceptionResolver(context);

      //  initRequestToViewNameTranslator(context);

        initViewResolvers(context);

       // initFlashMappinger(context); //Flash映射管理器

    }


    private void initFlashMappinger(GPApplicationContext context) {
    }


    private void initViewResolvers(GPApplicationContext context) {
        String templateRoot = context.getConfig().getProperty("templateRoot");


        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();

        File templateRootDir = new File(templateRootPath);
        for (File template : templateRootDir.listFiles()) {
            this.viewResolvers.add(new GPViewResolver(templateRoot));
        }


    }


    private void initRequestToViewNameTranslator(GPApplicationContext context) {
    }


    private void initHandlerExceptionResolver(GPApplicationContext context) {
    }


    private void initHandlerAdapters(GPApplicationContext context) {

        for (GPHandlerMapping handlerMapping : this.handlerMappings) {
            this.handlerAdpters.put(handlerMapping, new GPHandlerAdapter());
        }


    }


    private void initHandlerMappings(GPApplicationContext context) {
        if(this.context.getBeanDefinitionCount() == 0){ return;}
        String[] beanNames = context.getBeanDefinitionNames();

        for (String beanName : beanNames) {
            Object controller = context.getBean(beanName);
            Class<?> clazz = controller.getClass();

            if (!clazz.isAnnotationPresent(GPController.class)) {
                continue;
            }

            String baseUrl = "";

            if (clazz.isAnnotationPresent(GPRequestMapping.class)) {
                GPRequestMapping requestMapping = clazz.getAnnotation(GPRequestMapping.class);
                baseUrl = requestMapping.value();
            }

            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                Annotation[] declaredAnnotations =  method.getDeclaredAnnotations();
                for (Annotation declaredAnnotation : declaredAnnotations) {
                    System.out.println(declaredAnnotation);
                }
                if (!method.isAnnotationPresent(GPRequestMapping.class)) {
                    continue;
                }

                GPRequestMapping requestMapping = method.getAnnotation(GPRequestMapping.class);
                String regex = ("/" + baseUrl +"/"+ requestMapping.value().replaceAll("\\*", ".*")).replaceAll("/+", "/");
                Pattern pattern = Pattern.compile(regex);
                this.handlerMappings.add(new GPHandlerMapping(pattern, controller, method));
                log.info("Mapping:" + regex + ", " + method);
            }


        }


    }


    private void initThemeResolver(GPApplicationContext context) {
    }


    private void initLocaleResolver(GPApplicationContext context) {
    }


    private void initMultipartResolver(GPApplicationContext context) {
    }
}
