package com.ymx.ibatis.plus.mapper;

import com.ymx.ibatis.plus.wrapper.Wrapper;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author 爱java的小于
 * @time 2022-8-27
 * @version 1.0.1
 *
 * @param <T>
 */
public interface BaseMapper<T> {
    /**
     * 打印运行参数信息到控制台
     *
     * @param boo true 打印   false 不打印
     */
    public void printInformationToControl(boolean boo);


    /**
     * 根据map条件查询一条记录
     *
     * @param map key为字段名   value为字段值
     * @return 返回一个bean对象
     */
    public T selectOneByMap(Map<String,Object> map) throws SQLException;

    /**
     * 根据id查询一条记录
     *
     * @param id
     * @return 返回一个bean对象
     */
    public T selectOneById(Serializable id) throws SQLException;

    /**
     * 根据条件筛选器查询一条记录
     * 如果条件为null则只查询首条记录
     *
     * @param wrapper
     * @return
     */
    public T selectOne(Wrapper wrapper) throws SQLException;

    /**
     * 根据id组为条件查询多条记录
     *
     * @param id 可以为数组 也可以为可变参数
     * @return 返回List框架
     */
    public List<T> selectListById(List<? extends Serializable> id) throws SQLException;

    /**
     * 根据map查询多条记录
     *
     * @param map key为字段名   value为字段值
     * @return 返回List框架
     */
    public List<T> selectListByMap(Map<String,Object> map) throws SQLException;

    /**
     * 根据条件筛选器查询多条记录
     * bean对象封装记录行
     * 将记录行bean放入List中
     *
     * @param wrapper 自定义筛选条件
     * @return List框架
     */
    public List<T> selectList(Wrapper wrapper) throws SQLException;

    /**
     *
     * @param wrapper
     * @return
     * @throws SQLException
     */
    public List<Map<String,Object>> selectMapList(Wrapper wrapper) throws SQLException;

    /**
     * 分页查询多条记录
     *
     * @param index 所在行的起始坐标
     * @param number 要查询的记录数
     * @return List框架
     */
    public List<T> page(int index,int number) throws SQLException;

    /**
     * 根据id组查询多条数据
     *
     * @param keyColum 指定为key的字段名
     * @param id 可以使数组 也可以是可变参数
     * @param <K> 键的类型
     * @return Map集合
     */
    public <K> Map<K,T> selectMapById(String keyColum,List<? extends Serializable> id) throws SQLException;

    /**
     * 根据map条件查询多条记录
     *
     * @param keyColum 指定为key的字段名
     * @param map key为字段名   value为字段值
     * @param <K> 键的类型
     * @return Map集合
     */
    public <K> Map<K,T> selectMapByMap(String keyColum,Map<String,Object> map) throws SQLException;

    /**
     * 根据条件筛选器查询多条记录
     * 每条记录封装为bean对象
     * 指定字段值为该bean的key
     *
     * @param keyColum 指定为key的字段名
     * @param wrapper 用户自定义筛选条件
     * @param <K> 键的类型
     * @return map框架
     */
    public <K> Map<K,T> selectMap(String keyColum,Wrapper wrapper) throws SQLException;


    /**
     * 根据条件筛选器查询多条记录
     * 使用Map集合保存记录行
     * 使用Map集合将Map记录行保存起来
     *
     * @param keyColum 指定字段值为key
     * @param wrapper 自定义条件筛选器
     * @param <K>
     * @return
     * @throws SQLException
     */
    public <K> Map<K,Map<String,Object>> selectMapToMap(String keyColum,Wrapper wrapper) throws SQLException;


    /**
     * 根据条件筛选器插叙一个整型数据
     *
     * @param wrapper 自定义筛选条件
     * @return 返回一个整型数据
     */
    public long selectToLong(Wrapper wrapper) throws SQLException;


    /**
     * 根据条件筛选器查询一个浮点型数据
     *
     * @param wrapper 自定义条件筛选
     * @return 返回一个浮点型数据
     */
    public double selectToDouble(Wrapper wrapper) throws SQLException;

    /**
     * 根据id组更新数据
     *
     * @param keyColum key=需要更新的字段 value=更新后的值
     * @param ids id组
     * @return
     */
    public int updateById(Map<String,Object> keyColum ,List<? extends Serializable> ids) throws SQLException;

    /**
     * 根据条件筛选器更新数据
     *
     * @param keyColum key=需要更新的字段 value=更新后的值
     * @param wrapper 自定义条件筛选
     * @return 返回更新后的行数
     */
    public int update(Map<String,Object> keyColum,Wrapper wrapper) throws SQLException;

    /**
     * 根据实体类中的某一字段的值更新记录
     *
     * @param entity 实体类
     * @param field 根据指定field的值进行更新
     * @return int
     * @throws SQLException
     */
    public int updateByField(T entity, String field)throws SQLException;

    /**
     * 根据实体对象进行更新记录
     * 实体对象中的id字段被解析为 where id = 值
     * 其他字段被当做值放入指定记录中
     *
     * @param entity 实体类
     * @return int
     * @throws SQLException
     */
    public int updateById(T entity)throws SQLException;


    /**
     * 插入一条记录
     *
     * @param obj 实体类对象
     * @return 插入结果 true为成功 false为失败
     */
    public boolean insert(T obj) throws SQLException;

    /**
     * 批量插入多条记录
     *
     * @param objs
     * @return
     * @throws SQLException
     */
    public boolean insertBatch(List<T> objs)throws SQLException;


    /**
     * 根据id组删除数据
     *
     * @param ids id组
     * @return 返回影响的行数
     */
    public int deleteById(List<? extends Serializable> ids) throws SQLException;

    /**
     * 根据条件筛选器删除数据
     *
     * @param wrapper 自定义条件筛选
     * @return 返回影响的行数
     */
    public int delete(Wrapper wrapper) throws SQLException;
}

