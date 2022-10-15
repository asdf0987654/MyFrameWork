package com.ymx.ibatis.exception;

/**
 * 动态代理指定接口找不到时抛出该异常
 */
public class MapperException extends RuntimeException{
    public MapperException(String msg){
        super(msg);
    }
}
