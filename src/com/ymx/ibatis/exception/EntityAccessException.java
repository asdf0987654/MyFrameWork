package com.ymx.ibatis.exception;

/**
 * @author 爱java的小于
 * @time 2022-9-29
 * 访问此类的方法不存在时会抛出该异常
 */
public class EntityAccessException extends RuntimeException{
    public EntityAccessException(String entityName){
        super(entityName);
    }
}
