package com.ymx.ibatis.plus.mapper.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
/**
 * @author 爱java的小于
 * @time 2022-8-27
 * @version 1.0.1
 *
 * 次注解标注bean类型对应的数据库的表
 */
public @interface TableName {
    String tableName();
}
