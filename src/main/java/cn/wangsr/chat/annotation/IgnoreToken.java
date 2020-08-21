package cn.wangsr.chat.annotation;

import java.lang.annotation.*;

/**
 * @author wjl
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface IgnoreToken {
}
