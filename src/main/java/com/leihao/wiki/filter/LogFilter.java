//package com.leihao.wiki.filter;
//
//import com.sun.org.apache.regexp.internal.RE;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//
//@Component
//public class LogFilter implements Filter {
//    private static final Logger LOG= LoggerFactory.getLogger(LogFilter.class);
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//
//    }
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
//            throws IOException, ServletException {
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        LOG.info("--------------LogFilter 开始-------------");
//        LOG.info("请求地址: {} {}",request.getRequestURL().toString(), request.getMethod());
//        LOG.info("远程地址: {}",request.getRemoteAddr());
//
//        long start = System.currentTimeMillis();
//        filterChain.doFilter(servletRequest,servletResponse);
//        LOG.info("-----------------LogFilter结束 耗时：{} ms--------------",System.currentTimeMillis()-start);
//    }
//
//    @Override
//    public void destroy() {
//
//    }
//
//}
