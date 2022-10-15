package com.ymx.ibatis.plus.mapper;

import com.ymx.ibatis.exception.CommentException;
import com.ymx.ibatis.plus.mapper.util.AutoAddColum;
import com.ymx.ibatis.plus.mapper.util.ID;
import com.ymx.ibatis.plus.mapper.util.TableName;

import java.lang.reflect.Field;

/**
 * @author 爱java的小于
 * @time 2022-8-27
 * @version 1.0.1
 */
public class ParseBean {
    public static TableBean parseBean(Class module) throws CommentException, NoSuchMethodException {

        if(module == null) {
            throw new NullPointerException("必须为BaseMapper接口指定一个bean的class");
        }
        //解析表名注解
        TableName tNameAnnotation = (TableName) module.getAnnotation(TableName.class);
        if(tNameAnnotation == null){
            throw new CommentException("bean:"+module.getName()+" 没有注解TableName");
        }
        String tableName = tNameAnnotation.tableName();


        String autoColumName = null;    //解析自增属性注解
        String idColumName = null;      //解析标注id注解字段
        Field[] fields = module.getDeclaredFields();
        String[] columns = new String[fields.length];

        for(int i = 0 ; i < fields.length ; ++i){
            if(fields[i].getAnnotation(AutoAddColum.class) != null){
                autoColumName = fields[i].getName();
            }
            if(fields[i].getAnnotation(ID.class) != null){
                idColumName = fields[i].getName();
            }
            columns[i] = fields[i].getName();
        }
        //将信息添加至TableBean
        TableBean tableBean = new TableBean();
        tableBean.setTableName(tableName);
        tableBean.setColumName(columns);
        tableBean.setAutoAddColum(autoColumName);
        tableBean.setIdName(idColumName);
        tableBean.setModule(module);

        return tableBean;
    }
}
