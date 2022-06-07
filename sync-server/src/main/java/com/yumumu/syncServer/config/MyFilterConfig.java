package com.yumumu.syncServer.config;

import java.util.Collections;

import javax.annotation.Resource;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.yumumu.syncServer.filter.MyFilter;

/**
 * @author zhanghailin
 * @date 2022/6/7
 */
@Configuration
public class MyFilterConfig {

    @Resource
    private MyFilter myFilter;

    @Bean
    public FilterRegistrationBean<MyFilter> myFilterFilterRegistrationBean() {
        FilterRegistrationBean<MyFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(myFilter);
        registrationBean.setUrlPatterns(Collections.singleton("/*"));
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
