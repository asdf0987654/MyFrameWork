package com.ymx.ibatis.session;

import com.ymx.ibatis.Default.DefaultInvoke;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.ymx.ibatis.config.Configuration;
import com.ymx.ibatis.pasexml.ParseXml;
import com.ymx.ibatis.pasexml.SqlSet;
import com.ymx.ibatis.plus.cache.DefaultCache;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * @author 爱java的小于
 * @time 2022-7-27
 * @version 1.0.3
 *
 */
public class SimpleSqlSessionFactoryBuilder implements SimpleSqlSessionFactory {
    /*数据库连接池*/
    private static DataSource dataSource;
    /*sql语句的集合*/
    private SqlSet sqlSet;

    /**
     * 构造方法私有化
     *
     * @param config 主配置信息接口
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private SimpleSqlSessionFactoryBuilder(Configuration config,SqlSet sqlSet) throws
            Exception {
        this.sqlSet = sqlSet;
        this.iniDataSource(config);
    }


    /**
     * 初始化数据库连接池接口
     *
     * @param configuration
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private void iniDataSource(Configuration configuration) throws Exception {
        if (dataSource == null){
            HashMap<Object, Object> map = new HashMap<Object,Object>();
            map.put("driverClassName",configuration.getDriver());
            map.put("url",configuration.getDataBaseUrl());
            map.put("username",configuration.getUser());
            map.put("password",configuration.getPassword());
            if(configuration.getInitialSize() != null){
                map.put("initialSize",configuration.getInitialSize());
            }
            if(configuration.getMaxActive() != null){
                map.put("maxActive",configuration.getMaxActive());
            }
            if(configuration.getTimeBetweenEvictionRunsMillis() != null){
                map.put("timeBetweenEvictionRunsMillis",configuration.getTimeBetweenEvictionRunsMillis());
            }
            //configuration info add map to createDataSource
            dataSource = DruidDataSourceFactory.createDataSource(map);
        }
    }

    /**
     * 构建基于实现jdbc规范的数据库操作对象
     *
     * @return SimpleSqlSessionFactory
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static SimpleSqlSessionFactory build(InputStream stream)throws Exception{
        //读取信息流
        ParseXml parseXml = new ParseXml();
        parseXml.read(stream);
        return new SimpleSqlSessionFactoryBuilder(parseXml.getConfiguration(),
                    parseXml.getMapper());
    }


    @Override
    public  SimpleSqlSession openSession(){
        Executor executor = new SqlBaseExecutor(dataSource,new DefaultInvoke(),new DefaultCache());
        return new SimpleSqlSessionMpl(executor,sqlSet);
    }

}
