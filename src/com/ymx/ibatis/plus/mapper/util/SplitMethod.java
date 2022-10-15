package com.ymx.ibatis.plus.mapper.util;

public class SplitMethod {
    public synchronized static String splitSetMethod(String field){
        return "set"+field.substring(0, 1).toUpperCase() +
                field.substring(1);
    }
    public synchronized static String splitGetMethod(String field){
        return "get"+field.substring(0, 1).toUpperCase() +
                field.substring(1);
    }
    public synchronized static String setOrGetMethodToField(String methodName){
        return null;
    }
}
