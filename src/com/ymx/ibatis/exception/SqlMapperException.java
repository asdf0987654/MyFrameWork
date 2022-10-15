package com.ymx.ibatis.exception;

/**
 * 一下原因会抛出此异常
 * 1.SqlSet接口中查找不到指定Mapper
 * 2.用户提供的sql占位符索引有误
 * 3.动态代理机制发生错误
 */
public class SqlMapperException extends RuntimeException{
    public SqlMapperException(String exceptionMapper){
        super(exceptionMapper);
    }
}
