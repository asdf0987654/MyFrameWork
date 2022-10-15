package com.ymx.ibatis.plus.mapper;

import com.ymx.ibatis.plus.mapper.util.SplitMethod;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 爱java的小于
 * @time 2022-8-27
 * @version 1.0.1
 */
public class TableBean {
    private Class module;
    private String tableName;
    private String[] columName;
    private String autoAddColum;
    private Map<String,Method> getMethods;
    private String idName;

    public TableBean(){
    }

    public TableBean(String tName,String autoAddColum,String[] columName,String idName,Class bean) throws NoSuchMethodException {
        this.autoAddColum = autoAddColum;
        this.columName = columName;
        this.tableName = tName;
        this.module = bean;
        this.idName = idName;
        this.iniMethods(this.getModule());
    }

    private void iniMethods(Class module) throws NoSuchMethodException {
        int methodLength = this.autoAddColum == null ? this.columName.length : this.columName.length - 1;
        this.getMethods = new HashMap<String,Method>();
        int i=0;
        for(String field : this.columName){
            if(field.equals(this.autoAddColum)){
                continue;
            }else{
                String getMethodName = this.stitchingGetMethod(field);
                this.getMethods.put(field,module.getMethod(getMethodName));
            }
        }
    }
    /**
     * 拼接字段的get方法名
     * @param field 字段字符串
     * @return String 拼接后的方法名
     */
    private String stitchingGetMethod(String field){
        return SplitMethod.splitGetMethod(field);
    }

    public void setIdName(String idName) {
        this.idName = idName;
    }

    public String getIdName() {
        return idName == null ? "id" : idName;
    }

    public void setModule(Class module) throws NoSuchMethodException {
        this.module = module;
        this.iniMethods(this.getModule());
    }

    public void setAutoAddColum(String autoAddColum) {
        this.autoAddColum = autoAddColum;
    }

    public void setColumName(String[] columName) {
        this.columName = columName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getAutoAddColum() {
        return autoAddColum;
    }

    public String getTableName() {
        return tableName;
    }

    public String[] getColumName() {
        return columName;
    }

    public Class getModule() {
        return module;
    }

    public Map<String,Method> getGetMethodsMap(){
        return getMethods;
    }
}
