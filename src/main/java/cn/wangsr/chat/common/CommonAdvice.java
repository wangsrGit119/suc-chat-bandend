package cn.wangsr.chat.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wjl
 */
@ControllerAdvice
@RestController
public class CommonAdvice {
    private static Logger logger = LoggerFactory.getLogger(CommonAdvice.class);

    /**
     * 原始异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ResponseData  handleExceptionForErrorOne(Exception e, HttpServletRequest request){
        ResponseData exceptionResult = ResponseData.builder()
                .requestId((String)request.getAttribute("request_id"))
                .code((HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();
        logger.info("Exception message  : {}",e.getMessage());
        logger.info("Exception from  : {}",e.getCause());
        logger.info("Exception print  : {}",e);
        return exceptionResult;
    }


    /**
     * 自定义全局异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(GlobalException.class)
    ResponseData  handleExceptionForErrorTwo(GlobalException e, HttpServletRequest request){
        ResponseData exceptionResult = ResponseData.builder()
                .requestId((String)request.getAttribute("request_id"))
                .code((e.getCode()))
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();
        logger.info("MyException message  : {}",e.getMessage());
        logger.info("MyException from  : {}",e.getCause());
        logger.info("MyException print  : {}",e);
        return exceptionResult;
    }



}
