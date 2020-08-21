package cn.wangsr.chat.filter;


import cn.wangsr.chat.annotation.IgnoreToken;
import cn.wangsr.chat.common.GlobalException;
import cn.wangsr.chat.util.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wjl
 */
public class JwtInterceptor extends HandlerInterceptorAdapter {
    private static Logger logger = LoggerFactory.getLogger(JwtInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (HttpMethod.OPTIONS.equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        if(handler.getClass().isAssignableFrom(HandlerMethod.class)){
            IgnoreToken ignoreToken = ((HandlerMethod) handler).getMethodAnnotation(IgnoreToken.class);
            logger.info("ignoreToken {}",ignoreToken);
            if(ignoreToken != null){
                return true;
            }
        }


        String token = request.getHeader(JwtUtils.AUTH_HEADER_KEY);
        logger.info("token {}",token);
        if(StringUtils.isEmpty(token)){
            throw new GlobalException(400,"用户未登录");
        }
        //解析token
        try {
            JwtUtils.parseJwt(token);
        }catch (ExpiredJwtException e) {
            logger.error("Token过期", e);
            throw new GlobalException(10010,"token过期");
        } catch (Exception e){
            logger.error("非法token", e);
            throw new GlobalException(10010,"token无效");
        }
        return super.preHandle(request, response, handler);
    }
}
