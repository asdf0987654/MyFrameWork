package com.ymx.ibatis.plus.wrapper.split;

import com.ymx.ibatis.exception.EntityAccessException;
import com.ymx.ibatis.plus.mapper.TableBean;
import com.ymx.ibatis.plus.mapper.util.SplitMethod;
import com.ymx.ibatis.plus.wrapper.Wrapper;
import com.ymx.ibatis.plus.wrapper.statement.OriginalStatement;
import com.ymx.ibatis.plus.wrapper.statement.StatementMap;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author 爱java的小于
 * @time 2022-8-27
 * @version 1.0.1
 */
public class Update {

    public synchronized static OriginalStatement splitUpdateById(Object entity, TableBean tableBean){
        String idName = tableBean.getIdName() == null ? "id" : tableBean.getIdName();
        return splitUpdate(entity,idName,tableBean);
    }

    /**
     * 以实体类的形式拼接更新sql的语句
     *
     * @param entity
     * @param tableBean
     * @return
     */
    public synchronized static OriginalStatement splitUpdate(Object entity, String field, TableBean tableBean){
        List params = new ArrayList();
        String saveErrorMethodName = null;
        StringBuilder sqlBuilder = new StringBuilder();
        Map<String,Method> getMethods = tableBean.getGetMethodsMap();
        sqlBuilder.append("update ").append(tableBean.getTableName()).append(" set");

        try {
            for (String filedName : getMethods.keySet()) {
                saveErrorMethodName = filedName;
                Object tmpValue = getMethods.get(filedName).invoke(entity);
                if (!(filedName.equals(field))) {
                    params.add(tmpValue);
                    sqlBuilder.append(" ").append(filedName).append("=").append("?,");
                }
            }
            //去掉最后一个逗号
            sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
            //添加条件
            params.add(getMethods.get(field).invoke(entity));
            sqlBuilder.append(" ").append("where ").
                    append(field).append("=?");

        }catch (IllegalAccessException  | InvocationTargetException e){
            throw new EntityAccessException("实体类: "+entity+" 没有方法:"+saveErrorMethodName);
        }

        return new OriginalStatement(sqlBuilder.toString(),
                params.toArray());
    }

    /**
     * 拼接以wrapper的形式更新数据的SQL语句
     *
     * @param updateValue
     * @param tableName
     * @param wrapper
     * @return
     */
    public synchronized static OriginalStatement splitUpdate(Map<String,Object> updateValue,
                                                String tableName , Wrapper wrapper){
        List param = new ArrayList();
        StringBuffer buffer = new StringBuffer();
        StatementMap statementMap = wrapper.getStatementMap();
        String where = statementMap.getStatementMap().get("where");

        buffer.append("update "+tableName+" set");
        Object v = null;
        for(String col : updateValue.keySet()){
            param.add(updateValue.get(col));
            buffer.append(" ").append(col).append("=?,");
        }
        //去掉最后一个逗号
        buffer.deleteCharAt(buffer.length() - 1);
        //如果有条件语句 追加到sql字符串中
        if(where != null){
            buffer.append(" where "+where);
        }
        //将条件参数追加到参数列表中
        List asList = Arrays.asList(statementMap.getStatementParam());
        param.addAll(asList);

        return new OriginalStatement(buffer.toString(), param.toArray());
    }

}
