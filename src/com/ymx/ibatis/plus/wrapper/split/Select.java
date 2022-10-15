package com.ymx.ibatis.plus.wrapper.split;

import com.ymx.ibatis.plus.wrapper.Wrapper;
import com.ymx.ibatis.plus.wrapper.statement.OriginalStatement;
import com.ymx.ibatis.plus.wrapper.statement.StatementMap;

import java.util.Map;

/**
 * @author 爱java的小于
 * @time 2022-8-27
 * @version 1.0.1
 */
public class Select {
    public synchronized static OriginalStatement splitSelect(Wrapper wrapper, String tableName){
        StringBuffer re = new StringBuffer();
        StatementMap statementMap = wrapper.getStatementMap();
        Map<String, String> sqlBuffer = statementMap.getStatementMap();

        if(!isNullString(sqlBuffer.get("select"))){
            re.append("select "+sqlBuffer.get("select"));
            re.append(" from "+tableName);
        }else{
            re.append("select *");
            re.append(" from "+tableName);
        }
        if(!isNullString(sqlBuffer.get("where"))){
            re.append(" where "+sqlBuffer.get("where"));
        }
        if(!isNullString(sqlBuffer.get("groupBy"))){
            re.append(" group by "+sqlBuffer.get("groupBy"));
        }
        if(!isNullString(sqlBuffer.get("orderBy"))){
            re.append(" order by "+sqlBuffer.get("orderBy"));
        }
        if(!isNullString(sqlBuffer.get("limit"))){
            re.append(" limit "+sqlBuffer.get("limit"));
        }
        if(!isNullString(sqlBuffer.get("having"))){
            re.append(" having "+sqlBuffer.get("having"));
        }

        return new OriginalStatement(re.toString(),
                statementMap.getStatementParam());
    }

    private static boolean isNullString(String str){
        return str == null || str.length() == 0;
    }
}
