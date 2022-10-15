package com.ymx.ibatis.plus.wrapper;

import com.ymx.ibatis.plus.wrapper.statement.StatementMap;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 爱java的小于
 * @time 2022-8-27
 * @version 1.0.1
 */
public class UpdateWrapper extends Wrapper{
    public UpdateWrapper() {
    }

    @Override
    public StatementMap getStatementMap() {
        Map<String,String> re = new HashMap<String,String>();
        re.put("where",this.getWhereBuffer().toString());
        StatementMap statementMap = new
                StatementMap(re,this.getStatementParams().toArray());
        this.resetSqlStringBuffer();

        return statementMap;
    }
}
