package com.leihao.wiki.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    private static final Logger LOG= LoggerFactory.getLogger(LoginInterceptor.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LOG.info("============登录拦截器开始=============");
        long startTime = System.currentTimeMillis();
        request.setAttribute("requestStartTime",startTime);
        //OPTIONS请求不做校验
        //前后端分离架构，前端会发出一个options请求先做预检，因此对他不做检验
        if(request.getMethod().toUpperCase().equals("OPTIONS")){
            return true;
        }
        String path=request.getRequestURL().toString();
        LOG.info("接口登录拦截：path:{}",path);

        //获取header里的token
        String token = request.getHeader("token");
        LOG.info("登录校验开始：token： {} ",token);
        if (token==null||token.isEmpty()){
            LOG.info("token为空，请求被拦截");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        Object o = redisTemplate.opsForValue().get(token);
        if(o==null){
            LOG.warn("token无效，请求被拦截");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }else{
            LOG.info("已登录，{}",o);
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        Long startTime = (Long)request.getAttribute("requestStartTime");
        LOG.info("==============LoginInterceptor结束耗时：{}ms===============",System.currentTimeMillis()-startTime);
    }


}
