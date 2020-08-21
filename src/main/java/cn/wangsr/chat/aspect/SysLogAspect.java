package cn.wangsr.chat.aspect;

import cn.wangsr.chat.util.HttpIpUtils;
import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;

/**
 *
 * @author wjl
 */

@Slf4j
@Aspect
@Component
public class SysLogAspect {



    @Pointcut("execution(* cn.wangsr.chat.controller..*.*(..))")
    public void log() {}


    @Before("log()")
    public void  doBefore(JoinPoint joinPoint) throws UnsupportedEncodingException {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String method = request.getMethod();
        StringBuffer paramsValue = new StringBuffer();
        Object paramsName=null;
        // get请求
        if (HttpMethod.GET.toString().equals(method)) {
            String queryString = request.getQueryString();
            if (!StringUtils.isEmpty(queryString)) {
                paramsName= JSON.parseObject(JSON.toJSONString(joinPoint.getSignature())).get("parameterNames");
                paramsValue.append( URLDecoder.decode(queryString,"UTF-8"));
            }
        } else {
            //其他请求
            Object[] paramsArray = joinPoint.getArgs();
            paramsName= JSON.parseObject(JSON.toJSONString(joinPoint.getSignature())).get("parameterNames");
            for (Object o :paramsArray){
                paramsValue.append(o+" ");
            }
        }
        String ip = HttpIpUtils.getClientIp(request);
        log.info("URLParamName  : " + paramsName);
        log.info("URLParamValue  : " + paramsValue);
        log.info("URL:  {}, HTTP_METHOD:  {}, IP:  {}, Method:  {} ",request.getRequestURL().toString(),request.getMethod(), ip,joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName());
    }


}
