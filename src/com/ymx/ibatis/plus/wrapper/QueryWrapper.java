package com.ymx.ibatis.plus.wrapper;

import com.ymx.ibatis.plus.wrapper.split.SplitSqlString;
import com.ymx.ibatis.plus.mapper.util.SplitTypes;
import com.ymx.ibatis.plus.wrapper.statement.StatementMap;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 爱java的小于
 * @time 2022-8-27
 * @version 1.0.1
 *
 */
public class QueryWrapper extends Wrapper{
    private StringBuffer columBuffer;

    public QueryWrapper() {
        columBuffer = new StringBuffer();
    }

    public Wrapper select(String... colum){
        SplitSqlString.special(SplitTypes.SELECT,this.columBuffer,getStatementParams(),colum);
        return this;
    }

    private void init(){
        this.resetSqlStringBuffer();
        this.columBuffer = new StringBuffer();
    }

    @Override
    public StatementMap getStatementMap() {
        Map<String,String> bufferMap = new HashMap<String,String>();
        bufferMap.put("select",this.columBuffer.toString());
        bufferMap.put("where",this.getWhereBuffer().toString());
        bufferMap.put("groupBy",this.getGroupByBuffer().toString());
        bufferMap.put("orderBy",this.getOrderByBuffer().toString());
        bufferMap.put("having",this.getHavingBuffer().toString());
        bufferMap.put("limit",this.getLimitBuffer().toString());
        StatementMap statement = new StatementMap(bufferMap,
                this.getStatementParams().toArray());
        this.init();

        return statement;
    }
}
