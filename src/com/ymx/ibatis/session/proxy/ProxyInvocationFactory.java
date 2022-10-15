package com.ymx.ibatis.session.proxy;

import com.ymx.ibatis.exception.MapperException;
import com.ymx.ibatis.session.Executor;
import java.lang.reflect.Proxy;

/**
 * 动态代理工厂类
 */
public class ProxyInvocationFactory {
    public static <T> T build(Class<T> mapperClass, Executor executor){
        if(mapperClass == null){
            throw new MapperException("映射接口不能为为null");
        }
        return (T) Proxy.newProxyInstance(mapperClass.getClassLoader(),
                new Class[]{mapperClass},
                new ProxyInvocationHandler(executor,new MapperStatementHandler(mapperClass)));
    }
}
