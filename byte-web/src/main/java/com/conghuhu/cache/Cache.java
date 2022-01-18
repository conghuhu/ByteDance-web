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

    long expire() default 1 * 60 * 1000;

    String name() default "";

}
