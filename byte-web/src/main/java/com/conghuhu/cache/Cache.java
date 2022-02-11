package com.conghuhu.cache;

import java.lang.annotation.*;

/**
 * @author conghuhu
 * @create 2021-10-14 10:34
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cache {

    /**
     * 有效期，秒，默认值是4小时
     * @return
     */
    long expire() default 60 * 60 * 4;

    String name() default "";

}
