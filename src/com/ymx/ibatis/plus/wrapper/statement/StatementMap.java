package com.ymx.ibatis.plus.wrapper.statement;

import java.util.Map;

/**
 * @author 爱java的小于
 * @time 2022-9-27
 *
 * 封装原始的sql语句和参数信息
 * 关键字的sql语句存储在Map中
 */
public class StatementMap {
    private Map<String,String> statementMap;
    private Object[] statementParam;

    public StatementMap(Map<String,String> statementMap , Object[] statementParam){
        this.statementMap = statementMap;
        this.statementParam = statementParam;
    }

    public StatementMap(){}

    public Map<String, String> getStatementMap() {
        return statementMap;
    }

    public void setStatementMap(Map<String, String> statementMap) {
        this.statementMap = statementMap;
    }

    public Object[] getStatementParam() {
        return statementParam;
    }

    public void setStatementParam(Object[] statementParam) {
        this.statementParam = statementParam;
    }
}
