package com.ymx.ibatis.session;

import com.ymx.ibatis.exception.SqlMapperException;
import com.ymx.ibatis.exception.CommentException;
import com.ymx.ibatis.plus.mapper.BaseMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author 爱java的小于
 * @time 2022-07-10
 * @version 1.0.0
 *
 * 打开一个session对话同mybatis相同
 */
public interface SimpleSqlSession {
    /**
     * 将一条sql语句执行插入操作
     * @param sqlId sql语句的id号
     * @param args 填充该sql语句中的占位符参数
     * @return 插入成功返回 true 否则返回 false
     * @throws SQLException 执行过程中出错抛出该异常
     * @throws SqlMapperException 指定sql id的sql语句找不到抛出该异常
     */
    public boolean insertExe(String sqlId,Object... args) throws SQLException, SqlMapperException;

    /**
     * 将一条sql语句执行删除操作
     * @param sqlId sql语句的id号
     * @param args 填充该sql语句中的占位符参数
     * @return 返回影响的行数
     * @throws SQLException 执行过程中出错抛出该异常
     * @throws SqlMapperException 指定sql id的sql语句找不到抛出该异常
     */
    public int deleteExe(String sqlId,Object... args) throws SQLException, SqlMapperException;

    /**
     * 将一条sql语句执行更新操作
     * @param sqlId sql语句的id号
     * @param args 填充该sql语句中的占位符参数
     * @return 返回影响的行数
     * @throws SQLException 执行过程中出错抛出该异常
     * @throws SqlMapperException 指定sql id的sql语句找不到抛出该异常
     */
    public int updateExe(String sqlId,Object... args) throws SQLException, SqlMapperException;

    /**
     * 将一条sql语句执行查询操作
     * 查询的结果以list集合的形式返回
     * @param sqlId sql语句的id号
     * @param args 填充该sql语句中的占位符参数
     * @return 当结果集不为空返回List<T> 否则返回null
     * @throws SQLException 执行过程中出错抛出该异常
     * @throws SqlMapperException 指定sql id的sql语句找不到抛出该异常
     */
    public <T> List<T> selectListExe(String sqlId , Object... args) throws SQLException, SqlMapperException;

    /**
     * 将一条sql语句执行查询操作
     * 此查询只返回一条结果 或 结果集中的首条记录
     * @param sqlId sql语句的id号
     * @param args 填充该sql语句中的占位符参数
     * @return 结果集不为空返回 T 否则返回null
     * @throws SQLException 执行过程中出错抛出该异常
     * @throws SqlMapperException 指定sql id的sql语句找不到抛出该异常
     */
    public <T> T selectOneExe(String sqlId , Object... args) throws SQLException, SqlMapperException;

    /**
     * 将一条sql语句执行查询操作<br/>
     * 以键值对的形式返回记录，该键值对的键为select标签中的keyName<br/>
     * 结果集中的每条记录对应一个bean放入value中
     * @param sqlId sql语句的id号
     * @param args 填充该sql语句中的占位符参数
     * @return 结果集不为空返回Map<K,V> 否则返回null
     * @throws SQLException 执行过程中出错抛出该异常
     * @throws SqlMapperException 指定sql id的sql语句找不到抛出该异常
     */
    public <K,V> Map<K,V> selectMapExe(String sqlId,Object... args) throws SQLException, SqlMapperException;

    /**
     * 将一条sql语句执行查询操作<br/>
     * 该查询结果的返回值为Map封装记录集
     * 结果集的每行都已键值对的方式返回
     * @param sqlId sql语句的id号
     * @param args 填充该sql语句中的占位符参数
     * @param <K>
     * @return 结果集不为空返回Map<K,Map<String,Object>>
     * @throws SqlMapperException
     * @throws SQLException
     */
    public <K> Map<K,Map<String,Object>> selectMapToMap(String sqlId,Object... args)throws SqlMapperException,SQLException;


    public List<Map<String,Object>> selectMapToList(String sqlId,Object... args) throws SqlMapperException, SQLException;

    /**
     * 将一条sql语句执行查询操作<br/>
     * 查询的结果为long类型的数值
     * 一般应用于聚合查询中
     * @param sqlId sql语句的id号
     * @param args 填充该sql语句中的占位符参数
     * @return 返回具体的结果值
     * @throws SQLException 执行过程中出错抛出该异常
     * @throws SqlMapperException 指定sql id的sql语句找不到抛出该异常
     */
    public long selectLongExe(String sqlId , Object... args) throws SQLException, SqlMapperException;

    /**
     * 将一条sql语句执行删除操作
     * 查询的结果为double类型的数据
     * 一般应用于聚合查询中
     * @param sqlId sql语句的id号
     * @param args 填充该sql语句中的占位符参数
     * @return 返回影响的行数
     * @throws SQLException 执行过程中出错抛出该异常
     * @throws SqlMapperException 指定sql id的sql语句找不到抛出该异常
     */
    public double selectDoubleExe(String sqlId , Object... args) throws SQLException, SqlMapperException;

    /**
     * 返回指定sql语句的执行器<br/>
     * @param sqlId sql语句的id号
     * @return 返回影响的行数
     * @throws SQLException 执行过程中出错抛出该异常
     * @throws SqlMapperException 指定sql id的sql语句找不到抛出该异常
     */
    public Actuator getActuator(String sqlId)throws SqlMapperException;



    /**
     * 返回BaseMapper接口
     * @param bean 需要传入一个bean的实例
     * @param <T>
     * @return BaseMapper的实例
     */
    public <T> BaseMapper<T> getBaseMapper(Class<T> bean) throws CommentException, NoSuchMethodException;

    /**
     * 返回指定接口的动态代理对象
     *
     * @param mapperClass interface.class
     * @param <T> T
     * @return T
     */
    public <T> T getMapper(Class<T> mapperClass);
 }
