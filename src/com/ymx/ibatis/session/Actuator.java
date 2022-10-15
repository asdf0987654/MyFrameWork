package com.ymx.ibatis.session;

import com.ymx.ibatis.exception.SqlMapperException;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author 爱java的小于
 * @time 2022-7-30
 *
 * sql语句的执行器接口 此接口的实例由SimpleSqlSession返回
 * 该接口实例创建成功后只对应一个sql语句
 * 所有的操作都是基于一个sql语句实现<br/>
 * 如果该接口对应的是插入操作的sql语句块.则只能进行插入操作 否则 会抛出异常
 */
public interface Actuator {
    /**
     * 执行插入功能
     * @return 插入成功返回True 否则 返回 false
     * @throws SQLException
     */
    public boolean insertExe() throws SQLException;


    /**
     * 执行更新功能
     * @return 返回影响的行数
     * @throws SQLException
     */
    public int updateExe() throws SQLException;

    /**
     * 执行查询功能
     * @param <T>
     * @return 结果集为空返回null 否则返回List<T>
     * @throws SQLException
     */
    public <T> List<T> selectListExe() throws SQLException;

    /**
     * 执行查询功能
     * @param <T>
     * @return 结果集为空返回null 否则返回一个bean实例
     * @throws SQLException
     */
    public <T> T selectOneExe() throws SQLException;

    /**
     * 执行查询功能
     * @param <K>
     * @param <V>
     * @return 结果集为空返回null 否则返回一个Map<K,V>
     * @throws SQLException
     */
    public <K, V> Map<K, V> selectMapExe() throws SQLException;


    /**
     * 执行查询
     * 将结果集中的每条记录封装为map
     * 将map放入map集合并返回
     * @param <K> 指定map的键
     * @return
     * @throws SQLException
     */
    public <K> Map<K,Map<String,Object>> selectMapToMap() throws SQLException;


    /**
     * 执行查询
     * 将结果集中的每条记录封装为Map
     * 将Map放入List集合中返回
     * @return
     * @throws SQLException
     */
    public List<Map<String,Object>> selectMapList() throws SQLException;

    /**
     * 执行查询功能
     * 一般用于聚合查询
     * @return 返回结果为一个long类型的数值型数据
     * @throws SQLException
     */
    public long selectLongExe() throws SQLException;

    /**
     * 执行查询功能
     * 一般用于聚合查询
     * @return 返回结果为一个double类型的数值型数据
     * @throws SQLException
     */
    public double selectDoubleExe() throws SQLException;

    /**
     * 为对应sql语句添加占位符参数<br/>
     * 当sql语句出现占位符通过此方法进行占位符的填充
     * @param index 指定占位符的索引
     * @param param 填入的值
     * @throws SqlMapperException
     */
    public void setObject(int index, Object param) throws SqlMapperException;

}
