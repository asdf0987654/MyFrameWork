package com.ymx.ibatis.plus.mapper.excute;

import com.ymx.ibatis.pasexml.SqlStatement;
import com.ymx.ibatis.plus.mapper.util.FormatPrintSql;
import com.ymx.ibatis.session.Executor;
import com.ymx.ibatis.session.QueryType;

import java.sql.Date;
import java.sql.SQLException;

/**
 * @author 爱java的小于
 * @date 2022-10-02
 */
public class AssistantMpl implements Assistant{
    private final int QUERY = 1;
    private final int UPDATE = 2;
    private final Executor executor;
    private Object[] param;

    public AssistantMpl(Executor executor){
        this.executor = executor;
    }

    @Override
    public Object query(SqlStatement statement, QueryType type, boolean isPrint) throws SQLException {
        return this.execute(statement,type,QUERY,isPrint);
    }

    private String injectionParam(String sql , Object[] param){
        StringBuilder builder = new StringBuilder();
        int paramIndex = 0;
        for (int i = 0; i < sql.length(); i++) {
            if(sql.charAt(i) == '?'){
                builder.append(
                        (param[paramIndex] instanceof String || param[paramIndex] instanceof Date)
                            ? "'"+param[paramIndex]+"'"
                            : param[paramIndex]
                );
                ++paramIndex;
            }else{
                builder.append(sql.charAt(i));
            }
        }

        return builder.toString();
    }


    /**
     * 执行代理方法，用于更新 或 查询的时间
     * @param statement sql语句接口
     * @param queryType 查询的类型
     * @param exeType 执行类型 “查询” 或 “更新”
     * @param isPrint 是否打印
     * @return Object
     * @throws SQLException 可能会抛出语句异常
     */
    private Object execute(SqlStatement statement , QueryType queryType , int exeType, boolean isPrint) throws SQLException {
        String sql = statement.getSqlCode();
        Object[] param = statement.viewParam();
        long start = System.currentTimeMillis();
        Object re = exeType == QUERY
                ? this.queryExe(statement,queryType)
                : this.updateExe(statement);
        long end = System.currentTimeMillis();
        if(isPrint){
            if (exeType == QUERY) {
                FormatPrintSql.printFormatQuery
                        (this.injectionParam(sql, param), end - start);
            } else {
                FormatPrintSql.printFormatUpdate
                        (this.injectionParam(sql, param), end - start, Integer.parseInt(re.toString()));
            }
        }

        return re;
    }


    private Object queryExe(SqlStatement statement, QueryType types) throws SQLException {
        Object re = null;
        switch (types){
            case SELECT_LIST:
            case SELECT_ONE:
                re = this.executor.selectList(statement);
                break;
            case SELECT_NUMBER:
                re = this.executor.queryOne(statement);
                break;
            case SELECT_MAP:
                re = this.executor.selectMap(statement);
                break;
            case SELECT_MAP_LIST:
                re = this.executor.selectMapList(statement);
                break;
            case SELECT_MAP_MAP:
                re = this.executor.selectMapInMap(statement);
                break;
        }

        return re;
    }

    @Override
    public int update(SqlStatement statement, boolean isPrint) throws SQLException {
        return (Integer) this.execute(statement,null,UPDATE,isPrint);
    }

    private Object updateExe(SqlStatement statement) throws SQLException {
        return this.executor.update(statement);
    }
}
