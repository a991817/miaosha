package com.dgut.springboot.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


@Configuration
public class DruidConfig {
    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource druid(){
       return new DruidDataSource();
    }
//    配置druid监控
//    1、配置一个管理后台的servlet
    @Bean
    public ServletRegistrationBean StatViewServlet(){
        System.out.println("111111111111");
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
        Map<String,String> initParam = new HashMap<>();
        initParam.put("loginUsername","dadada");
        initParam.put("loginPassword","123456");
//        initParam.put("allow","*");
        servletRegistrationBean.setInitParameters(initParam);
        return servletRegistrationBean;
    }
//    2、配置一个监控的filter
    @Bean
    public FilterRegistrationBean WebStatFilter(){
        System.out.println("22222222222");
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        WebStatFilter webStatFilter = new WebStatFilter();
        filterRegistrationBean.setFilter(webStatFilter);
        filterRegistrationBean.addUrlPatterns("/*");
        Map<String,String> initParam = new HashMap<>();
        initParam.put("exclusions","*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*");
        filterRegistrationBean.setInitParameters(initParam);
        return filterRegistrationBean;

    }
}
