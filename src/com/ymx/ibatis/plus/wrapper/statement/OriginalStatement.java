package com.ymx.ibatis.plus.wrapper.statement;

/**
 * @author 爱java的小于
 * @time 2022-9-27
 *
 * 封装完整的sql语句信息
 * sql语句为拼接后的字符串对象
 */
public class OriginalStatement {
    private String sqlStatement;
    private Object[] param;

    public OriginalStatement(String sqlStatement,Object[] param){
        this.sqlStatement = sqlStatement;
        this.param = param;
    }

    public OriginalStatement(){}

    public void setParam(Object[] param) {
        this.param = param;
    }

    public void setSqlStatement(String sqlStatement) {
        this.sqlStatement = sqlStatement;
    }

    public Object[] getParam() {
        return param;
    }

    public String getSqlStatement() {
        return sqlStatement;
    }
}
