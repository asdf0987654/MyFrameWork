package com.ymx.ibatis.session;

import com.ymx.ibatis.exception.SqlMapperException;
import com.ymx.ibatis.pasexml.SqlSet;
import com.ymx.ibatis.pasexml.SqlStatement;
import com.ymx.ibatis.exception.CommentException;
import com.ymx.ibatis.plus.mapper.BaseMapper;
import com.ymx.ibatis.plus.mapper.DefaultBaseMapper;
import com.ymx.ibatis.plus.mapper.ParseBean;
import com.ymx.ibatis.session.proxy.ProxyInvocationFactory;
import com.ymx.ibatis.session.proxy.util.Placeholder;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author 爱java的小于
 * @time 2022-7-30
 *
 */
public class SimpleSqlSessionMpl implements SimpleSqlSession {
    /*sql语句集合*/
    private final SqlSet sqlSet;
    /*执行器*/
    private final Executor executor;

    private BaseMapper baseMapper;

    public <T> SimpleSqlSessionMpl(Executor executor , SqlSet sqlSet){
        this.executor = executor;
        this.sqlSet = sqlSet;
    }

    public boolean insertExe(String sqlId,Object... args)
            throws SQLException, SqlMapperException {
        SqlStatement mapper = this.insertParam(sqlId,args);
        return this.executeUpdate(mapper) > 0;
    }

    public int deleteExe(String sqlId,Object... args)
            throws SQLException, SqlMapperException {
        SqlStatement mapper = this.insertParam(sqlId,args);
        return this.executeUpdate(mapper);
    }

    public int updateExe(String sqlId,Object... args)
            throws SQLException, SqlMapperException {
        SqlStatement mapper = this.insertParam(sqlId,args);
        return this.executeUpdate(mapper);
    }

    public <T> List<T> selectListExe(String sqlId, Object... args)
            throws SQLException, SqlMapperException {
        SqlStatement mapper = this.insertParam(sqlId,args);
        this.moduleIsEmpty(mapper,sqlId);
        Object re = this.executeQuery(mapper, QueryType.SELECT_LIST);
        return re == null ? null : (List<T>)re;
    }

    /**
     * 返回sql id号对应的Statement实例
     *
     * @param sqlId sqlid
     * @param args 如果存在占位符并将值加入该sql语句
     * @return SqlStatement
     * @throws SqlMapperException
     */
    private SqlStatement insertParam(String sqlId  , Object... args)
            throws SqlMapperException, SQLException {
        SqlStatement statement = this.sqlSet.getMapper(sqlId);
//        for (int i = 0; i < args.length; ++i) {
//            mapper.setObject(i+1,args[i]);
//        }
        List<String> params = Placeholder.parse(statement.getSqlCode(), '{', '}', '?');
        String sqlString = params.remove(params.size()-1);
        Object[] statementParam = Placeholder.getStatementParam(params, statement.getSqlId(), args);
        //设置sql字符串和参数列表
        statement.setSqlCode(sqlString);
        statement.setParams(statementParam);

        return statement;
    }


    private Object executeQuery(SqlStatement mapper , QueryType queryType) throws SQLException {
            Object re = null;
            switch (queryType){
                case SELECT_NUMBER:
                    re = this.executor.queryOne(mapper);
                    break;
                case SELECT_ONE:
                case SELECT_LIST:
                    re = this.executor.selectList(mapper);
                    break;
                case SELECT_MAP:
                    re = this.executor.selectMap(mapper);
                    break;
                case SELECT_MAP_MAP:
                    re = this.executor.selectMapInMap(mapper);
                    break;
                case SELECT_MAP_LIST:
                    re = this.executor.selectMapList(mapper);
                    break;
            }
            /*返回结果集*/
            return re;
    }

    /**
     * 执行更新数据操作
     * @param mapper
     * @return
     */
    private int executeUpdate(SqlStatement mapper) throws SQLException {
        return this.executor.update(mapper);
    }


    public <T> T selectOneExe(String sqlId , Object... args)
            throws SQLException, SqlMapperException {
        SqlStatement mapper = this.insertParam(sqlId,args);
        this.moduleIsEmpty(mapper,sqlId);
        Object re = executeQuery(mapper, QueryType.SELECT_ONE);

        return re == null ? null : ((List<T>)re).get(0);
    }

    @Override
    public <K, V> Map<K, V> selectMapExe(String sqlId, Object... args)
            throws SQLException, SqlMapperException {
        SqlStatement mapper = this.insertParam(sqlId,args);
        this.moduleIsEmpty(mapper,sqlId);
        Object re = executeQuery(mapper, QueryType.SELECT_MAP);

        return re == null ? null : (Map<K, V>) re;
    }


    @Override
    public <K> Map<K, Map<String, Object>> selectMapToMap(String sqlId,Object... args) throws SqlMapperException, SQLException {
        SqlStatement mapper = this.insertParam(sqlId,args);
        this.moduleIsEmpty(mapper,sqlId);
        Object re = executeQuery(mapper, QueryType.SELECT_MAP_MAP);

        return re == null ? null : (Map<K, Map<String,Object>>) re;
    }


    @Override
    public List<Map<String,Object>> selectMapToList(String sqlId,Object...  args) throws SqlMapperException, SQLException {
        SqlStatement mapper = this.insertParam(sqlId,args);
        this.moduleIsEmpty(mapper,sqlId);
        Object re = executeQuery(mapper, QueryType.SELECT_MAP_LIST);

        return re == null ? null : (List<Map<String, Object>>)re;
    }

    public long selectLongExe(String sqlId , Object... args)
            throws SQLException, SqlMapperException {
        SqlStatement mapper = this.insertParam(sqlId,args);
        return (Long)(this.executeQuery(mapper, QueryType.SELECT_NUMBER));
    }

    public double selectDoubleExe(String sqlId , Object... args)
            throws SQLException, SqlMapperException {
        SqlStatement mapper = this.insertParam(sqlId,args);
        return (Double)(this.executeQuery(mapper, QueryType.SELECT_NUMBER));
    }


    private void moduleIsEmpty(SqlStatement mapper , String sqlId){
        if(mapper.getModule() == null){
            throw new NullPointerException("sql id 为:\""+sqlId+"\"的sql语句的resultType的值为null");
        }
    }

    @Override
    public <T> BaseMapper getBaseMapper(Class<T> bean) throws CommentException, NoSuchMethodException {
        if(this.baseMapper == null)
            this.baseMapper = new DefaultBaseMapper<T>(this.executor, ParseBean.parseBean(bean));
        return this.baseMapper;
    }

    @Override
    public Actuator getActuator(String sqlId) throws SqlMapperException {
        return new ActuatorMpl(this.sqlSet.getMapper(sqlId),this);
    }


    public <T> T getMapper(Class<T> mapperClass){
        return ProxyInvocationFactory.build(mapperClass,executor);
    }


    /**
     * 实现Actuator接口的内部类
     */
    private class ActuatorMpl implements Actuator{
        private final SqlStatement mapper;
        private final SimpleSqlSession session;
        @Override
        public boolean insertExe() throws SQLException {
            try {
                return this.session.insertExe(mapper.getSqlId(),mapper.getParam());
            } catch (SqlMapperException e) {
                return false;
            }
        }

        @Override
        public int updateExe() throws SQLException {
            try {
                return this.session.updateExe(mapper.getSqlId(),mapper.getParam());
            } catch (SqlMapperException e) {
                return 0;
            }
        }

        @Override
        public <T> List<T> selectListExe() throws SQLException {
            try {
                return this.session.selectListExe(this.mapper.getSqlId(),
                        this.mapper.getParam());
            } catch (SqlMapperException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public <T> T selectOneExe() throws SQLException {
            try {
                return this.session.selectOneExe(this.mapper.getSqlId(),
                        this.mapper.getParam());
            } catch (SqlMapperException e) {
                return null;
            }
        }

        @Override
        public <K, V> Map<K, V> selectMapExe() throws SQLException {
            try {
                return this.session.selectMapExe(this.mapper.getSqlId(),
                        this.mapper.getParam());
            } catch (SqlMapperException e) {
                return null;
            }
        }

        @Override
        public <K> Map<K, Map<String, Object>> selectMapToMap() throws SQLException {
            try {
                return this.session.selectMapToMap(this.mapper.getSqlId(),
                        this.mapper.getParam());
            } catch (SqlMapperException e) {
                return null;
            }
        }

        @Override
        public List<Map<String, Object>> selectMapList() throws SQLException {
            try {
                return this.session.selectMapToList(this.mapper.getSqlId(),
                        this.mapper.getParam());
            } catch (SqlMapperException e) {
                return null;
            }
        }

        @Override
        public long selectLongExe() throws SQLException {
            try {
                return this.session.selectLongExe(this.mapper.getSqlId(),
                        this.mapper.getParam());
            } catch (SqlMapperException e) {
                return 0;
            }
        }

        @Override
        public double selectDoubleExe() throws SQLException {
            try {
                return this.session.selectDoubleExe(this.mapper.getSqlId(),
                        this.mapper.getParam());
            } catch (SqlMapperException e) {
                return 0.0;
            }
        }

        @Override
        public void setObject(int index, Object param) throws SqlMapperException {
            mapper.setObject(index,param);
        }

        public ActuatorMpl(SqlStatement sqlMapper, SimpleSqlSession session){
            this.mapper = sqlMapper;
            this.session = session;
        }
    }

}
