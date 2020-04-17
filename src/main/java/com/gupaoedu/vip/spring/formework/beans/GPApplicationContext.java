package com.gupaoedu.vip.spring.formework.beans;

import com.gupaoedu.vip.spring.formework.annotation.GPAutowired;
import com.gupaoedu.vip.spring.formework.annotation.GPController;
import com.gupaoedu.vip.spring.formework.annotation.GPService;
import com.gupaoedu.vip.spring.formework.beans.config.GPBeanPostProcessor;
import com.gupaoedu.vip.spring.formework.beans.support.GPBeanDefinitiionReader;
import com.gupaoedu.vip.spring.formework.beans.support.GPDefaultListableBeanFactory;
import com.gupaoedu.vip.spring.formework.core.GPBeanFactory;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 王延伟
 * @description  IOC DI MVC AOP
 * @createTime 2020年4月7日$ 21点28分$
 */
public class GPApplicationContext  extends GPDefaultListableBeanFactory  implements GPBeanFactory {

    private String [] configLoactions;
    private GPBeanDefinitiionReader reader;

    //单例的ioc容器缓存
    private Map<String,Object> factoryBeanObjectCache = new ConcurrentHashMap<String,Object>();

    //通用的ioc容器
    private Map<String,GPBeanWapper> factoryBeanInstanceCache = new ConcurrentHashMap<String, GPBeanWapper>();

    public GPApplicationContext(String... configLoactions){
        this.configLoactions = configLoactions;
        try {
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void refresh() throws Exception {
        //1.定位，定位配置文件 TODO
        reader = new GPBeanDefinitiionReader(this.configLoactions);

        //2.加载配置文件，扫描相关的类，把它们封装成BeanDefinition
        List<GPBeanDefinition> beanDeanfinitions  = reader.loadBeanDeanfinitions();

        //3.注册，把配置信息放到容器里面（伪ioc）
        doRegisterBeanDefinition(beanDeanfinitions);

        //4.把不是延迟加载的类提前初始化
        doAutoWrited();

    }


    private void doAutoWrited() {
        for (Map.Entry<String,GPBeanDefinition> beanDefinitionEntry : super.beanDefinitionMap.entrySet()){
            String beanName = beanDefinitionEntry.getKey();
            if (!beanDefinitionEntry.getValue().isLazyInit()){
                try {
                    getBean(beanName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }


    }


    private void doRegisterBeanDefinition(List<GPBeanDefinition> beanDeanfinitions) throws Exception {

        for ( GPBeanDefinition beanDefinition: beanDeanfinitions){
            if (super.beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())){
                throw new Exception("The “ "+beanDefinition.getBeanClassName()+" ”is  exists!");
            }
            beanDefinitionMap.put(beanDefinition.getFactoryBeanName(),beanDefinition);
            beanDefinitionMap.put(beanDefinition.getBeanClassName(),beanDefinition);
        }


    }


    @Override
    public Object getBean(String beanName)  {

        try {
            GPBeanDefinition beanDefinition = super.beanDefinitionMap.get(beanName);
            //生成通知事件

            GPBeanPostProcessor beanPostProcessor = new GPBeanPostProcessor();

            //获取每个bean 不管lazy不lazy
            Object instance = instantiateBean(beanDefinition);
            if (null == instance){
                return null;
            }

            beanPostProcessor.postProcessBeforeInitialization(instance, beanName);
            GPBeanWapper beanWapper = new GPBeanWapper(instance);

            this.factoryBeanInstanceCache.put(beanName,beanWapper);

            beanPostProcessor.postProcessAfterInitialization(instance,beanName);

            //DI
            populateBean(beanName,instance);

            return this.factoryBeanInstanceCache.get(beanName).getWrappedInstance();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    private void populateBean(String beanName, Object instance) {

        Class<?> clazz = instance.getClass();
        if (!(clazz.isAnnotationPresent(GPController.class) || clazz.isAnnotationPresent(GPService.class))){
            return;
        }
        //获取这类下面的所有字段
        Field[] fields = clazz.getDeclaredFields();

        for ( Field field : fields){
            if (!field.isAnnotationPresent(GPAutowired.class)){continue;}
            GPAutowired annotation = field.getAnnotation(GPAutowired.class);
            String autoWiredBeanName = annotation.value().trim();
            if ("".equals(autoWiredBeanName)){
                autoWiredBeanName = field.getType().getName();
            }
            field.setAccessible(true);
            try {
                if(this.factoryBeanInstanceCache.get(autoWiredBeanName) == null){
                    continue;
                }
                field.set(instance,this.factoryBeanInstanceCache.get(autoWiredBeanName).getWrappedInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    private Object instantiateBean(GPBeanDefinition beanDefinition) {
        Object instance = null;
        //获取全类名
        String className = beanDefinition.getBeanClassName();

        if (this.factoryBeanObjectCache.containsKey(className)){
            instance = this.factoryBeanObjectCache.get(className);
        }else {
            try {
                Class<?> clazz = Class.forName(className);
                instance = clazz.newInstance();

                this.factoryBeanObjectCache.put(beanDefinition.getFactoryBeanName(),instance);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return instance;
    }


    @Override
    public Object getBean(Class<?> beanClass) throws Exception {
        return getBean(beanClass.getName());
    }

    // 返回bean的数组
    public String[] getBeanDefinitionNames(){
        return this.beanDefinitionMap.keySet().toArray(new String[this.beanDefinitionMap.size()]);
    }

    //ioc容器的个数
    public int getBeanDefinitionCount(){
        return this.beanDefinitionMap.size();
    }

    public Properties getConfig(){
        return this.reader.getConfig();
    }



}
