package com.ymx.ibatis.session;

import com.ymx.ibatis.pasexml.SqlStatement;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author 爱java的小于
 * @time 2022-7-26
 *
 * 此接口为sql语句的执行接口<br/>
 * Actuator接口面向的是终端人员，此接口面向的是jdbc,
 * 该接口供SimpleSqlSession接口底层调用
 */
public interface Executor {
    /**
     * 执行更新数据
     * @param lineSql
     * @return 影响大记录数
     * @throws SQLException
     */
    public int update(SqlStatement lineSql)throws SQLException;

    /**
     * 执行查询数据
     * @param lineSql
     * @return List集合
     * @throws SQLException
     */
    public <T> List<T> selectList(SqlStatement lineSql)throws SQLException;

    /**
     * 执行查询数据
     * @param linkSql
     * @return Map集合
     * @throws SQLException
     */
    public <K,V> Map<K,V> selectMap(SqlStatement linkSql)throws SQLException;

    /**
     * 执行查询数据
     * @param linkSql
     * @return Object
     * @throws SQLException
     */
    public Object queryOne(SqlStatement linkSql) throws SQLException;


    /**
     * 查询数据
     * 将记录集中的每条记录封装为Map
     * 将Map放入List集合中并返回
     * @param linkSql
     * @return
     * @throws SQLException
     */
    public List<Map<String,Object>> selectMapList(SqlStatement linkSql)throws SQLException;


    /**
     * 执行查询
     * 将记录集中的每行记录封装为Map
     * 将Map放入Map集合中并返回
     * @param linkSql
     * @param <T>
     * @return
     * @throws SQLException
     */
    public <T> Map<T,Map<String,Object>> selectMapInMap(SqlStatement linkSql)throws SQLException;

}

