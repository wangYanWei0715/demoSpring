package com.gupaoedu.vip.spring.formework.beans.support;

import com.gupaoedu.vip.spring.formework.beans.GPBeanDefinition;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author 王延伟
 * @description //对配置文件进行查找 读取 和解析
 * @createTime 2020年4月7日$ 21点36分$
 */
public class GPBeanDefinitiionReader {

    //把所有配置文件下以.class结尾的权限定类名放入registyBeanClasses
    private  List<String> registyBeanClasses = new ArrayList<String>();

    private Properties config = new Properties();

    private final String SCAN_PACKAGE = "scanpackage";

    public GPBeanDefinitiionReader(String... configLoactions) {

        InputStream is = this.getClass().getClassLoader()
            .getResourceAsStream(configLoactions[0].replace("classpath:", ""));
        try {
            config.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (null != is){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        doScanner(config.getProperty(SCAN_PACKAGE));
    }


    private void doScanner(String property) {
        URL url = this.getClass().getClassLoader().getResource("/" + property.replaceAll("\\.", "/"));
        File classPath = new File(url.getFile());
       for (File file : classPath.listFiles()){
           if (file.isDirectory()){
               doScanner(property + "."+file.getName());
           }else {
               if (!file.getName().endsWith(".class")){continue;}
               String className = property + "." + file.getName().replace(".class", "");
               registyBeanClasses.add(className);
           }


       }
    }


    //把配置文件中扫描的所有配置信息换为GPBeanfintion对象，以便于之后的ioc操作
    //将所有配置文件下面以.calss结尾的所有类都以beanDefinition包装，并放到list里面去
    //
    public List<GPBeanDefinition> loadBeanDeanfinitions() {

        List<GPBeanDefinition> result = new ArrayList<GPBeanDefinition>();

        try {
            for ( String className : registyBeanClasses){
                Class<?> beanClass = Class.forName(className);
                if (beanClass.isInterface()){continue;}
                result.add(doCreateBeanDefinition(toLowerFistCase(beanClass.getSimpleName()),beanClass.getName()));
                for (Class<?> i : beanClass.getInterfaces()){
                    result.add(doCreateBeanDefinition(i.getName(),beanClass.getName()));
                }
            }
        }catch ( Exception e){
            e.printStackTrace();
        }

        return result;

    }


    private GPBeanDefinition doCreateBeanDefinition(String toLowerFistCase, String name) {
        GPBeanDefinition beanDefinition = new GPBeanDefinition();
        beanDefinition.setBeanClassName(name);
        beanDefinition.setFactoryBeanName(toLowerFistCase);
        return beanDefinition;
    }


    private String toLowerFistCase(String simpleName) {
        char[] chars = simpleName.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }


    public Properties getConfig() {
        return this.config;

    }
}
