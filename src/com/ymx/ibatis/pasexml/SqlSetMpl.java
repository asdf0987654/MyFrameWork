package com.ymx.ibatis.pasexml;

import com.ymx.ibatis.exception.SqlMapperException;

import java.util.Map;

/**
 * @author 爱java的小于
 */
public class SqlSetMpl implements SqlSet {

    private Map<String, SqlStatement> sqlMap;

    @Override
    public SqlStatement getMapper(String sqlId) throws SqlMapperException {
        SqlStatement re =  this.sqlMap.get(sqlId);
        if(re == null)
            throw new SqlMapperException("不存在的ID:"+sqlId);

        return re;
    }

    public SqlSetMpl(Map<String, SqlStatement> map){
        this.sqlMap = map;
    }
}
