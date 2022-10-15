package com.ymx.ibatis.session.proxy.anocation;

import com.ymx.ibatis.session.QueryType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Select {
    String value();
    QueryType queryType();
    String resultType() default "";
    String keyName() default "";
}
