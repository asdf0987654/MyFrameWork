package com.ymx.ibatis.structure;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author 爱java的小于
 * @time 2022-7-30
 * 将记录映射成bean对象
 */
public interface Structure {
    /**
     * 以map对象返回该记录集
     * @param keyName 指明结果集中为bean实例的键的列
     * @param <K>   键的类型
     * @param <V>   bean的类型
     * @return  Map
     * @throws Exception
     */
    public <K,V> Map<K,V> invokeToMap(String keyName, Class<?> bean , ResultSet resultSet) throws SQLException;

    /**
     * 以列表的形式返回该记录集
     * @param <T>   bean的类型
     * @return List
     * @throws Exception
     */
    public <T> List<T> invokeToList(Class<?> bean , ResultSet resultSet) throws SQLException;

    /**
     * 以键值对的方式返回记录行
     * 并且所有的记录行以键值对的方式返回
     * @param keyColumName
     * @param bean
     * @param resultSet
     * @param <T>
     * @return
     * @throws SQLException
     */
    public <T> Map<T,Map<String,Object>> invokeMap(String keyColumName , Class<?> bean , ResultSet resultSet)throws SQLException;


    /**
     * 记录行以键值对的方式返回
     * 键值对以List的方式返回
     * @return
     * @throws SQLException
     */
    public List<Map<String,Object>> invokeMapToList(Class<?> bean , ResultSet resultSet)throws SQLException;
}
