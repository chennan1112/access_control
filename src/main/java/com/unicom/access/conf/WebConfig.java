package com.unicom.access.conf;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author mrChen
 * @date 2021/2/1 21:32
 */
//@Component
public class WebConfig extends WebMvcConfigurerAdapter {
    @Bean
    public FilterRegistrationBean httpServletRequestReplacedRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new HttpServletRequestReplacedFilter());
        registration.addUrlPatterns("/*");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("httpServletRequestReplacedFilter");
        registration.setOrder(1);
        return registration;
    }

    /**
     * 配置拦截器
     * @author yuqingquan
     * @param registry
     */
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration addInterceptor=registry.addInterceptor(new TokenInterceptor());
        // 排除拦截地址配置
        addInterceptor.excludePathPatterns("/error", "/login/**");
        // 拦截配置
        addInterceptor.addPathPatterns("/**");
    }
}
