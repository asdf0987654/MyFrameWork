package com.ymx.ibatis.session.proxy;

import com.ymx.ibatis.pasexml.SqlStatement;
import com.ymx.ibatis.session.Executor;
import com.ymx.ibatis.session.QueryType;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.SQLException;

/**
 * 动态代理控制器
 */
public class ProxyInvocationHandler implements InvocationHandler {
    private final Executor executor;
    private final StatementHandler statementHandler;
    public ProxyInvocationHandler(Executor executor, StatementHandler handler){
        this.executor = executor;
        this.statementHandler = handler;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        /*调用statementHandler接口处理注解中的sql及方法参数 返回一个SqlStatement实例*/
        SqlStatement statement = this.statementHandler.getStatement(method,args);

        return this.invokeSqlStatement(statement);
    }


    /**
     * 执行sql语句
     *
     * @param statement 封装sql语句的接口类型
     * @return Object
     */
    private Object invokeSqlStatement(SqlStatement statement) throws SQLException {
        Object result = null;
        /*
            queryType不为null时根据枚举的值执行相应查询
         */
        QueryType queryType = statement.getQueryType();
        if(queryType != null){
            switch (queryType){
                case SELECT_LIST:
                    result = executor.selectList(statement);
                    break;
                case SELECT_ONE:
                    result = executor.selectList(statement).get(0);
                    break;
                case SELECT_MAP:
                    result = executor.selectMap(statement);
                    break;
                case SELECT_MAP_LIST:
                    result = executor.selectMapList(statement);
                    break;
                case SELECT_MAP_MAP:
                    result = executor.selectMapInMap(statement);
                    break;
                case SELECT_NUMBER:
                    result = executor.queryOne(statement);
                    break;
            }
            /*
                queryType属性为null表示数据库更新操作
             */
        }else {
            result = executor.update(statement);
        }

        return result;
    }

}
