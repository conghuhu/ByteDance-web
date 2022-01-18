package com.conghuhu.aop;

import java.lang.annotation.*;

/**
 * @author conghuhu
 * @create 2021-10-13 18:57
 */
// Type代表可以放在类上面，Method可以放在方法上
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {

    String module() default "";

    String operator() default "";
}
