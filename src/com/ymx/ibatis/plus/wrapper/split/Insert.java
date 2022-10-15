package com.ymx.ibatis.plus.wrapper.split;

import com.ymx.ibatis.exception.EntityAccessException;
import com.ymx.ibatis.plus.mapper.TableBean;
import com.ymx.ibatis.plus.wrapper.statement.OriginalStatement;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 爱java的小于
 * @time 2022-8-27
 * @version 1.0.1
 */
public class Insert {
    public synchronized static OriginalStatement splitInsert(TableBean table, Object... value){
        StringBuffer re = new StringBuffer();
        //split colum String
        String colum = splitColumString(table.getColumName(),table.getAutoAddColum());
        //split value String
        OriginalStatement statement = splitValue(table.getColumName(),table.getAutoAddColum(),
                table.getGetMethodsMap(), value);
        //split the colum and value to sql String
        re.append("insert into ").append(table.getTableName()).append(colum)
                .append(statement.getSqlStatement());
        statement.setSqlStatement(re.toString());

        return statement;
    }

    private  static String splitColumString(String[] colum,String autoColum){
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append('(');
        for(String col : colum){
            if(!col.equals(autoColum)){
                stringBuffer.append(col).append(",");
            }
        }
        //去最后一个逗号,并添加一个括号
        stringBuffer.deleteCharAt(stringBuffer.length()-1).append(")");

        return stringBuffer.toString();
    }

    private static OriginalStatement splitValue(String[] colum, String autoColum, Map<String,Method> methodMap, Object... beans){
        int methodsLength = autoColum == null ? colum.length : colum.length - 1;
        Method[] methods = new Method[methodsLength];
        List params = new ArrayList();

        //循环取出方法实例放入数组
        for(int i = 0, j = 0 ; i < colum.length ; ++i,++j){
            if(colum[i].equals(autoColum)){
                --j;
                continue;
            }
            methods[j] = methodMap.get(colum[i]);
        }

        //异常发生时记录发生异常的实体
        Object bean = null;
        StringBuilder re = new StringBuilder("value ");
        try {
            for (int i = 0 ; i < beans.length ; ++i) {
                re.append('(');
                bean = beans[i];
                for (Method method : methods) {
                    params.add(method.invoke(bean));
                    re.append("?,");
                }
                //去最后一个逗号,并添加一个括号和逗号
                re.deleteCharAt(re.length() - 1).append(')').append(',');
            }
        }catch (IllegalAccessException | InvocationTargetException e) {
            throw new EntityAccessException("实体类"+bean.toString()+"方法访问错误");
        }
        //去逗号
        re.deleteCharAt(re.length()-1);

        return new OriginalStatement(re.toString(), params.toArray());
    }
}
