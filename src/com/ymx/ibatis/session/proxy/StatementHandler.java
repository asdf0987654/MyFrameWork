package com.ymx.ibatis.session.proxy;

import com.ymx.ibatis.pasexml.SqlStatement;

import java.lang.reflect.Method;
import java.sql.SQLException;

/**
 * 语句处理
 */
public interface StatementHandler {
    SqlStatement getStatement(Method method, Object[] args) throws ClassNotFoundException, SQLException;
}
