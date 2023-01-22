package com.leihao.wiki.config;


import com.leihao.wiki.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

//    @Autowired
//    private LogInterceptor logInterceptor;
//
//    public void addInterceptors(InterceptorRegistry registry){
//        registry.addInterceptor(logInterceptor)
//                .addPathPatterns("/**");
//    }

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/user/login",
                        "/category/all",
                        "/ebook/list",
                        "/doc/all/**",
                        "/doc/find-content/**",
                        "/doc/vote/**",
                        "/ebook-snapshot/get-statistic"
                );
    }
}
