package com.ymx.ibatis.Default;

import com.ymx.ibatis.session.Invoke;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class DefaultInvoke implements Invoke {
    @Override
    public void invoke(ResultSet resultSet,Method method,int type ,String fName ,Object obj)
            throws InvocationTargetException, IllegalAccessException, SQLException {
        switch (type){
            case Types.INTEGER:
                method.invoke(obj,resultSet.getInt(fName));
                break;
            case Types.BIGINT:
                method.invoke(obj,resultSet.getLong(fName));
                break;
            case Types.DOUBLE:
                method.invoke(obj,resultSet.getDouble(fName));
                break;
            case Types.FLOAT:
                method.invoke(obj,resultSet.getFloat(fName));
                break;
            case Types.VARCHAR:
                method.invoke(obj,resultSet.getString(fName));
                break;
            case Types.TIMESTAMP:
                method.invoke(obj,resultSet.getTimestamp(fName));
                break;
            case Types.DATE:
                method.invoke(obj,resultSet.getDate(fName));
                break;
            case Types.BOOLEAN:
                method.invoke(obj,resultSet.getBoolean(fName));
                break;
            case Types.SMALLINT:
                method.invoke(obj,resultSet.getShort(fName));
                break;
        }
    }
}
