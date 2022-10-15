package com.ymx.ibatis.plus.wrapper.split;

import com.ymx.ibatis.plus.wrapper.Wrapper;
import com.ymx.ibatis.plus.wrapper.statement.OriginalStatement;
import com.ymx.ibatis.plus.wrapper.statement.StatementMap;

/**
 * @author 爱java的小于
 * @time 2022-8-27
 * @version 1.0.1
 */
public class Delete {
    public synchronized static OriginalStatement splitDelete(String tableName, Wrapper wrapper){
        StringBuffer buffer = new StringBuffer();
        StatementMap statementMap = wrapper.getStatementMap();
        buffer.append("delete from ").append(tableName).
                append(" where ").append(statementMap.
                        getStatementMap().get("where"));

        return new OriginalStatement(buffer.toString(),statementMap.getStatementParam());
    }

}
