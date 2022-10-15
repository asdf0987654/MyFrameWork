package com.ymx.ibatis.plus.mapper.excute;

import com.ymx.ibatis.pasexml.SqlStatement;
import com.ymx.ibatis.session.QueryType;

import java.sql.SQLException;


public interface Assistant {
    Object query(SqlStatement statement, QueryType type, boolean isPrint) throws SQLException;

    int update(SqlStatement statement , boolean isPrint) throws SQLException;
}
