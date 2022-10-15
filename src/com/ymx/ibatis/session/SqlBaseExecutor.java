package com.ymx.ibatis.session;

import com.ymx.ibatis.pasexml.SqlStatement;
import com.ymx.ibatis.plus.cache.BaseMapperCache;
import com.ymx.ibatis.structure.BeanStructure;
import com.ymx.ibatis.structure.Structure;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author 爱java的小于
 *
 * @time 2022-07-04
 *
 * @version 1.0.0
 */
public class SqlBaseExecutor implements Executor {
    /*数据库连接池*/
    private final DataSource dataSource;
    /*bean映射器*/
    private final Structure structure;
    /*缓存映射*/
    private final BaseMapperCache cache;
    /*方法执行器*/
    private final Invoke invoke;

    public SqlBaseExecutor(DataSource dataSource , Invoke invoke, BaseMapperCache cache){
        this.dataSource = dataSource;
        this.invoke = invoke;
        this.cache = cache;
        this.structure = new BeanStructure(this.invoke);
    }


    /**
     * 查找缓存
     * @param sqlId
     * @return
     */
    private Object findCache(String sqlId){
        return this.cache.get(sqlId);
    }


    /**
     * 将查询结果集放入缓存中
     * @param cacheId
     * @param value
     */
    private void addToCache(String cacheId,Object value){
        this.cache.put(cacheId,value);
    }


    /**
     * 清除缓存
     */
    private void clearCache(){
        this.cache.clear();
    }

    /**
     * 为每个查询缓存创建一个id号
     *
     * @param sql
     * @param type
     * @return
     * @throws SQLException sql语句中占位符和参数匹配个数不同会抛出此异常
     */
    private String createCacheKey(String sql, QueryType type, Object... args) throws SQLException {
        StringBuilder sn = new StringBuilder(sql);
        if(sn.indexOf("?") != -1 && args == null){
            throw new SQLException("sql:"+sql+" 中缺少占位符参数");
        }else{
            //检查args合法性
            args = this.initialiseIfEmpty(args);

            /*验证占位符与注入参数个数是否匹配*/
            int index = 0 , i = 0 , argLength = 0;
            argLength = args.length;
            while((index = sn.indexOf("?")) != -1){
                if(i == argLength){
                    throw new SQLException("sql:"+sql+" 中缺少占位符参数");
                }
                sn.replace(index,index+1,args[i++].toString());
            }
            /*参数个数多于占位符时抛出异常*/
            if(argLength > i){
                throw new SQLException("sql:"+sql+" 中注入参数的个数:"+argLength+" 大于占位符数量:"+i);
            }
            sn.append(type);
            String s = sn.toString().replaceAll(" ","").
                    replaceAll("\n","");

            return s;
        }
    }

    /**
     * 如果参数的值为null
     * 返回一个新的元素个数为0的新数组
     * @param args
     * @return
     */
    private Object[] initialiseIfEmpty(Object[]  args){
        return args == null ? new Object[0] : args;
    }



    /**
     * 返回Statement接口
     * 生成Statement过程中可能会出现线程安全问题
     * 此方法添加一个对象锁
     *
     * @param sql Sql字符串
     * @param args sql语句中的参数
     * @return PreparedStatement
     * @throws SQLException
     */
    private synchronized PreparedStatement getStatement(Connection connection,String sql,Object... args)throws SQLException{
            PreparedStatement statement = connection.prepareStatement(
                    sql,ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            args = this.initialiseIfEmpty(args);
            for (int i = 1; i <= args.length; i++) {
                statement.setObject(i,args[i-1]);
            }

            return statement;
    }


    /**
     * 更新数据表操作
     * 此函数由updateExecute() 或 delExecute() 两个函数共同调用
     *
     * @param sql sql字符串
     * @param args 参数列表
     * @return int 返回更新的记录数
     * @throws SQLException
     */
    private synchronized int updateExecute(String sql,Object... args)throws SQLException{
        Connection connection = dataSource.getConnection();
        PreparedStatement statement = this.getStatement(connection,sql,args);
        int executeUpdate = 0;
        //更新数据库中的数据时锁住该连接池,确保线程安全
        synchronized (dataSource){
            executeUpdate = statement.executeUpdate();
        }
        statement.close();
        connection.close();
        //数据更新成功清除缓存内容
        if(executeUpdate > 0 )
            this.clearCache();

        return executeUpdate;
    }


    @Override
    public int update(SqlStatement lineSql) throws SQLException {
        return lineSql == null
                ? 0
                : this.updateExecute(lineSql.getSqlCode(),lineSql.getParam());
    }


    @Override
    public List<Map<String, Object>> selectMapList(SqlStatement linkSql) throws SQLException {
        if(linkSql == null)
            return null;
        Object resul = this.query(linkSql, QueryType.SELECT_MAP_LIST);
        return resul == null
                ? null
                : (List<Map<String,Object>>) resul;
    }


    @Override
    public <T> Map<T, Map<String, Object>> selectMapInMap(SqlStatement linkSql) throws SQLException {
        if(linkSql == null)
            return null;
        Object resul = this.query(linkSql, QueryType.SELECT_MAP_MAP);
        return resul == null
                ? null
                : (Map<T,Map<String,Object>>) resul;
    }

    @Override
    public <K, V> Map<K, V> selectMap(SqlStatement linkSql) throws SQLException {
        if (linkSql == null)
            return null;
        Object resul = this.query(linkSql, QueryType.SELECT_MAP);
        return resul == null
                ? null
                : (Map<K,V>) resul;
    }

    /**
     * 查询记录并进行封装
     *
     * @param linkSql
     * @param resultType
     * @return Object
     * @throws SQLException
     */
    private synchronized Object query(SqlStatement linkSql , QueryType resultType)
            throws SQLException{
        /*首先从缓存中查找*/
        String cacheKey = this.createCacheKey(linkSql.getSqlCode(), resultType, linkSql.viewParam());
        Object resultCache = this.findCache(cacheKey);
        if(resultCache != null){
            return resultCache;
        }
        //向连接池获取连接访问数据库
        Connection connection = dataSource.getConnection();
        PreparedStatement statement = this.getStatement(connection,linkSql.getSqlCode(), linkSql.getParam());
        ResultSet resultSet = statement.executeQuery();
        Object re = doStructure(resultSet, linkSql.getKeyName(),
                linkSql.getModule(), resultType);
        resultSet.close();
        statement.close();
        connection.close();
        /*结果放入缓存中*/
        this.addToCache(cacheKey,re);

        return re;
    }

    /**
     * 将结果集封装到指定集合
     *
     * @param resultSet 原始记录集
     * @param keyName 当以map结果集返回时指定的key的字段
     * @param module bean的类型
     * @param resultType 封装的集合类型
     * @return Object
     * @throws SQLException
     */
    private Object doStructure(ResultSet resultSet, String keyName,
                               Class module, QueryType resultType)throws SQLException{
        //判断结果集为空直接退出
        if(resultSetIsNull(resultSet)) {
            return null;
        }
        Object result = null;//放置bean的列表
            switch (resultType){
                case SELECT_MAP:
                    result = this.structure.invokeToMap(keyName,module,resultSet);
                    break;
                case SELECT_MAP_LIST:
                    result = this.structure.invokeMapToList(module,resultSet);
                    break;
                case SELECT_MAP_MAP:
                    result = this.structure.invokeMap(keyName,module,resultSet);
                    break;
                case SELECT_LIST:
                    result = this.structure.invokeToList(module,resultSet);
                    break;
            }

        /*返回结果集实例的地址*/
        return result;
    }



    /**
     *判断结果集是否为空
     *
     * @param resultSet
     * @return 结果集为空为 true 不为空为false
     * @throws SQLException
     */
    private boolean resultSetIsNull(ResultSet resultSet) throws SQLException {
        boolean re = resultSet.next();
        resultSet.previous();

        return (!re);
    }


    @Override
    public <T> List<T> selectList(SqlStatement lineSql) throws SQLException {
        if(lineSql == null)
            return null;
        Object resul = this.query(lineSql, QueryType.SELECT_LIST);
        return  resul == null
                ? null
                : (List<T>) resul;
    }


    @Override
    public Object queryOne(SqlStatement linkSql) throws SQLException {
        return this.queryOne(linkSql.getSqlCode(),linkSql.getParam());
    }

    /**
     * 只获取查询的第一个结果
     * @param sql
     * @param args
     * @return String
     * @throws SQLException
     */
    private Object queryOne(String sql , Object... args)throws SQLException{
        Connection connection = dataSource.getConnection();
        PreparedStatement statement = this.getStatement(connection,sql,args);
        ResultSet resultSet = statement.executeQuery();
        Object value = null;
        if(resultSet.next()){
            value = resultSet.getObject(1);
        }
        resultSet.close();
        statement.close();
        connection.close();

        return value;
    }

}
