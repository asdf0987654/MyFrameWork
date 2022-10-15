package com.ymx.ibatis.session;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 爱java的小于
 * @time 2022-7-23
 * @version 1.0.3
 *
 * 将ResultSet中的结果赋值给bean实例中的属性时使用该接口来完成
 */
public interface Invoke {
    /**
     * 将字段值赋值给bean实例
     *
     * @param type 将字段值赋值给bean实例时会根据该常数指明强制转换的类型
     * @param method 调用method实例的方法完成赋值
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public void invoke(ResultSet resultSet , Method method , int type ,String fName, Object o) throws InvocationTargetException, IllegalAccessException, SQLException;

}
