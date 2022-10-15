package com.ymx.ibatis.exception;

/**
 * @author 爱java的小于
 * @time 2022-8-27
 * @version 1.0.1
 * 1.当缺少注解时将抛出该异常
 * 2.当注解中缺少内容时抛出该异常
 */
public class CommentException extends Exception{
    public CommentException(String msg){
        super(msg);
    }
}
