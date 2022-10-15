package com.ymx.ibatis.plus.mapper.util;

/**
 * @author 爱java的小于
 * @time 2022-8-27
 * @version 1.0.1
 *
 * 用来指定sql拼接类型
 */
public enum SplitTypes {
    BETWEEN,
    NOTBETWEEN,
    LIKE,
    NOTLIKE,
    LIKELEFT,
    LIKERIGHT,
    ISNULL,
    ISNOTNULL,
    ORDERBY,
    GROUPBY,
    LIMIT,
    HAVING,
    WHERE,
    SELECT,
    INSERT,
    UPDATE,
    DELETE
}
