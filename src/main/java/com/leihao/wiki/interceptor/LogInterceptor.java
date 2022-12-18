package com.leihao.wiki.interceptor;

import org.omg.CORBA.PRIVATE_MEMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LogInterceptor implements HandlerInterceptor {
    private static final Logger LOG= LoggerFactory.getLogger(LogInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)throws Exception{
        LOG.info("--------------LogInterceptor 开始-------------");
        LOG.info("请求地址: {} {}",request.getRequestURL().toString(), request.getMethod());
        LOG.info("远程地址: {}",request.getRemoteAddr());

        long starTime = System.currentTimeMillis();
        request.setAttribute("requestStartTime",starTime);
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        Long startTime = (Long)request.getAttribute("requestStartTime");
        LOG.info("-----------------LogInterceptor 耗时：{} ms--------------",System.currentTimeMillis()-startTime);
    }
}
