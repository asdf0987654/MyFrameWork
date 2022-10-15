package com.ymx.ibatis.pasexml;

import com.ymx.ibatis.exception.SqlMapperException;
import com.ymx.ibatis.session.QueryType;

import java.util.Arrays;
import java.util.List;

/**
 * @author 爱java的小于
 */
public class SqlStatement {
    private String sqlCode;
    private Object[] param;
    private String sqlId;
    private Class module;
    private String keyName;
    private QueryType queryType;
    private List<String> placeholderNameList;
    private int placeholderCount;

    public SqlStatement(String sqlCode , Class module , String keyName , String sqlId){
        this.sqlCode = sqlCode;
        this.module = module;
        this.sqlId = sqlId;
        this.keyName = keyName;
        this.placeholderCount = countCharOf(this.sqlCode,'?');
        this.param = new Object[this.placeholderCount];
    }

    public List<String> getPlaceholderNameList(){
        return this.placeholderNameList;
    }

    public void setPlaceholderNameList(List<String> placeholderNameList){
        this.placeholderNameList = placeholderNameList;
    }

    private int countCharOf(String str , char c){
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if(str.charAt(i) == c){
                ++count;
            }
        }

        return count;
    }

    public String getSqlCode() {
        return sqlCode;
    }

    public void setSqlCode(String sqlCode) {
        this.sqlCode = sqlCode;
    }

    public Object[] getParam() {
        Object[] re = this.param;
        this.param = this.param = new Object[this.placeholderCount];
        return re;
    }

    public Object[] viewParam(){
        if(this.param == null){
            return null;
        }else{
            return Arrays.copyOf(this.param, this.param.length);
        }
    }

    public void setParams(Object[] params) {
        this.param = params;
    }

    public String getSqlId() {
        return this.sqlId;
    }

    public String getKeyName() {
        return this.keyName;
    }

    public QueryType getQueryType(){return this.queryType;}

    public void setQueryType(QueryType queryType){this.queryType = queryType;}

    public void setKeyName(String key) {
        this.keyName = key;
    }

    public void setModule(Class module){
        this.module = module;
    }

    public Class getModule() {
        return this.module;
    }

    public void setObject(int index , Object param)throws SqlMapperException {
        if(index > this.param.length || index < 1){
            throw new SqlMapperException("不合法的占位符索引:"+index);
        }
        this.param[index-1] = param;
    }

}
