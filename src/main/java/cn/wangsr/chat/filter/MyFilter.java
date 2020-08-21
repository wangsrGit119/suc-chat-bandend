package cn.wangsr.chat.filter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;


/**
 * @author: wjl
 */
@Component
@WebFilter(urlPatterns = { "/*" }, filterName = "MyFilter")
public class MyFilter implements Filter {

    private static Logger logger = LoggerFactory.getLogger(MyFilter.class);
    private final String REQUEST_ID = "request_id";
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("filter init ...");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;


        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, request.getHeader("Origin"));
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST, GET, OPTIONS, DELETE,PUT");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "3600");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,"Origin, X-Requested-With, Content-Type, Accept, Authorization");
        //请求唯一id
        String uid =  UUID.randomUUID().toString();
        MDC.put(REQUEST_ID,uid);
        request.setAttribute(REQUEST_ID,uid);
        filterChain.doFilter(request, response);
    }


    @Override
    public void destroy() {
        logger.info("filter destroy ...");
    }
}
