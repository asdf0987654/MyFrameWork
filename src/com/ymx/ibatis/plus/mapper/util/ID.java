package com.ymx.ibatis.plus.mapper.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
/**
 * @author 爱java的小于
 *  * @time 2022-8-27
 *  * @version 1.0.1
 *  该注解用来标注id字段
 *  * 如果字段名称为id则不需要标注
 */
public @interface ID {
}
