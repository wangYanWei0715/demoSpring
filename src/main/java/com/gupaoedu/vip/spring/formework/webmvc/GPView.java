package com.gupaoedu.vip.spring.formework.webmvc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 王延伟
 * @description TODO
 * @createTime 2020年4月8日$ 21点37分$
 */
public class GPView {

    public static  final  String DEFAULT_CONTENT_TYPE = "text/html;charset=utf-8";

    private File viewFile;


    public GPView(File templateFile) {
        this.viewFile = templateFile;
    }

    public String getContentType(){
        return DEFAULT_CONTENT_TYPE;
    }


    public void render(Map<String,?> model, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        StringBuffer sb = new StringBuffer();
        RandomAccessFile ra = new RandomAccessFile(this.viewFile, "r");
        try{
            String line = null;

            while (null != (line = ra.readLine())){
                line = new String(line.getBytes("ISO-8859-1"),"utf-8");
                Pattern pattern = Pattern.compile("￥\\{[^\\}]+\\}", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(line);

                while (matcher.find()){
                    String paramName = matcher.group();
                    paramName = paramName.replaceAll("￥\\{|\\}", "");
                    Object paramValue = model.get(paramName);
                    if (null == paramValue){ continue;}
                  line =   matcher.replaceFirst(makeStringForRegExp(paramValue.toString()));
                    matcher = pattern.matcher(line);
                }
                sb.append(line);
            }
        }finally {
            ra.close();
        }
        resp.setCharacterEncoding("utf-8");
        resp.getWriter().write(sb.toString());
    }


    private String makeStringForRegExp(String toString) {
        return toString.replace("\\","\\\\").replace("*","\\*")
            .replace("+","\\+").replace("|","\\|")
            .replace("{","\\{").replace("}","\\}")
            .replace("(","\\(").replace(")","\\)")
            .replace("^","\\^").replace("$","\\$")
            .replace("[","\\[").replace("]","\\]")
            .replace("?","\\?").replace(",","\\,")
            .replace(".","\\.").replace("&","\\&");



    }
}
