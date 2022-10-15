package com.ymx.ibatis.plus.mapper;

import com.ymx.ibatis.pasexml.SqlStatement;
import com.ymx.ibatis.plus.mapper.excute.Assistant;
import com.ymx.ibatis.plus.mapper.excute.AssistantMpl;
import com.ymx.ibatis.plus.wrapper.split.Delete;
import com.ymx.ibatis.plus.wrapper.split.Insert;
import com.ymx.ibatis.plus.wrapper.split.Select;
import com.ymx.ibatis.plus.wrapper.split.Update;
import com.ymx.ibatis.plus.wrapper.QueryWrapper;
import com.ymx.ibatis.plus.wrapper.UpdateWrapper;
import com.ymx.ibatis.plus.wrapper.Wrapper;
import com.ymx.ibatis.plus.wrapper.statement.OriginalStatement;
import com.ymx.ibatis.session.Executor;
import com.ymx.ibatis.session.QueryType;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author 爱java的小于
 * @time 2022-7-8
 * @version 1.0.1
 */
public class DefaultBaseMapper<T> implements BaseMapper<T>{
    private final Executor executor;
    private final TableBean bean;
    private final Assistant assistant;
    private final QueryWrapper queryWrapper = new QueryWrapper();
    private final UpdateWrapper updateWrapper = new UpdateWrapper();
    private boolean isPrintInformation;

    public DefaultBaseMapper(Executor executor,TableBean bean){
        this.executor = executor;
        this.bean = bean;
        this.assistant = new AssistantMpl(executor);
    }

    @Override
    public void printInformationToControl(boolean boo){
        this.isPrintInformation = boo;
    }


    /**
     * 封装sql信息
     * @param os
     * @param module
     * @param keyName
     * @return
     */
    private SqlStatement getSqlStatement(OriginalStatement os , Class module , String keyName){
        SqlStatement sqlStatement =  new SqlStatement(os.getSqlStatement(),
                module, keyName,null);
        sqlStatement.setParams(os.getParam());
        return sqlStatement;
    }


    @Override
    public T selectOneByMap(Map<String,Object> map) throws SQLException {
        List<T> ts = this.selectListByMap(map);

        return ts == null
                ? null
                : ts.get(0);
    }

    @Override
    public T selectOneById(Serializable id) throws SQLException {
        QueryWrapper wrapper = this.queryWrapper;
        wrapper.eq(this.bean.getIdName(),id);
        OriginalStatement os = Select.splitSelect(wrapper, this.bean.getTableName());
        List<T> reList= ((List<T>)this.assistant.query(
                this.getSqlStatement(os,this.bean.getModule(),null),
                QueryType.SELECT_ONE,isPrintInformation
        ));

        return reList == null
                ? null
                : reList.get(0);
    }

    @Override
    public T selectOne(Wrapper wrapper) throws SQLException {
        Wrapper wra = this.wrapperEqNull(wrapper, QueryType.SELECT_ONE);
        OriginalStatement os = Select.splitSelect(wra, this.bean.getTableName());
        List<T> resultList = (List<T>) this.assistant.
                query(this.getSqlStatement(os,this.bean.getModule(),null),
                        QueryType.SELECT_ONE,isPrintInformation);

        return resultList == null ? null : resultList.get(0);
    }

    @Override
    public List<T> selectListById(List<? extends Serializable> id) throws SQLException {
        QueryWrapper wrapper = this.queryWrapper;
        wrapper.in(this.bean.getIdName(),id);
        OriginalStatement os = Select.splitSelect(wrapper, this.bean.getTableName());

        return (List<T>)this.assistant.query(
                this.getSqlStatement(os,this.bean.getModule(),null),
                QueryType.SELECT_LIST,isPrintInformation
        );
    }

    @Override
    public List<T> selectList(Wrapper wrapper) throws SQLException {
        Wrapper wra = this.wrapperEqNull(wrapper, QueryType.SELECT_LIST);
        OriginalStatement os = Select.splitSelect(wra,this.bean.getTableName());

        return (List<T>)this.assistant.query(
                this.getSqlStatement(os,this.bean.getModule(),null),
                QueryType.SELECT_LIST,isPrintInformation);
    }

    @Override
    public List<Map<String, Object>> selectMapList(Wrapper wrapper) throws SQLException {
        Wrapper wra = this.wrapperEqNull(wrapper, QueryType.SELECT_MAP_LIST);
        OriginalStatement os = Select.splitSelect(wra,this.bean.getTableName());

        return (List<Map<String,Object>>)this.assistant.query(
                this.getSqlStatement(os,this.bean.getModule(),null),
                QueryType.SELECT_MAP_LIST,isPrintInformation);
    }

    @Override
    public List<T> page(int index, int number) throws SQLException {
        QueryWrapper wrapper = this.queryWrapper;
        wrapper.limit(index,number);
        OriginalStatement os = Select.splitSelect(wrapper,this.bean.getTableName());

        return (List<T>)this.assistant.query(
                this.getSqlStatement(os,this.bean.getModule(),null),
                QueryType.SELECT_LIST,isPrintInformation);
    }

    @Override
    public long selectToLong(Wrapper wrapper) throws SQLException {
        return (Long)(
                this.selectNumber(wrapper, QueryType.SELECT_NUMBER)
        );
    }

    private Object selectNumber(Wrapper wrapper , QueryType qt) throws SQLException {
        OriginalStatement os = Select.splitSelect(wrapper,this.bean.getTableName());
        return this.assistant.query(
                this.getSqlStatement(os,null,null),
                qt,isPrintInformation);
    }

    @Override
    public double selectToDouble(Wrapper wrapper) throws SQLException {
        return (Double)(
                this.selectNumber(wrapper, QueryType.SELECT_NUMBER)
        );
    }

    @Override
    public boolean insert(T obj) throws SQLException {
        return this.insertEntity(obj);
    }

    private boolean insertEntity(Object... values)throws SQLException{
        OriginalStatement os = Insert.splitInsert(this.bean, values);
        return this.assistant.update(this.getSqlStatement(
                os,null,null
        ),isPrintInformation) > 0;
    }

    @Override
    public boolean insertBatch(List<T> objs) throws SQLException {
        return this.insertEntity(objs.toArray());
    }


    @Override
    public int deleteById(List<? extends Serializable> ids) throws SQLException {
        Wrapper wrapper = this.updateWrapper;
        wrapper.in(this.bean.getIdName(),ids);
        OriginalStatement os = Delete.splitDelete(this.bean.getTableName(), wrapper);

        return this.assistant.update(this.getSqlStatement(
                os,null,null
        ),isPrintInformation);
    }

    @Override
    public int delete(Wrapper wrapper) throws SQLException {
        OriginalStatement os = Delete.splitDelete(this.bean.getTableName(), wrapper);
        return this.assistant.update(this.
                getSqlStatement(os,null,null),
                isPrintInformation
        );
    }

    @Override
    public int update(Map<String,Object> keyColum, Wrapper wrapper) throws SQLException {
        OriginalStatement os = Update.splitUpdate(keyColum, this.bean.getTableName(),
                wrapper);
        return this.assistant.update(this.getSqlStatement(os,null,null),
                isPrintInformation);
    }

    @Override
    public int updateById(T entity)throws SQLException{
        return this.assistant.update(
                this.getSqlStatement(Update.splitUpdateById(entity,this.bean),null,null),
                this.isPrintInformation
        );
    }

    @Override
    public int updateByField(T entity, String field)throws SQLException{
        return this.assistant.update(
                this.getSqlStatement(Update.splitUpdate(entity,field,this.bean),null,null),
                this.isPrintInformation
        );
    }

    @Override
    public int updateById(Map<String,Object> keyColum, List<? extends Serializable> ids) throws SQLException {
        Wrapper wrapper = this.updateWrapper;
        wrapper.in(this.bean.getIdName(),ids);
        OriginalStatement os = Update.splitUpdate(keyColum,
                this.bean.getTableName(),
                wrapper);
        return this.assistant.update(
                this.getSqlStatement(os,null,null), isPrintInformation);
    }

    /**
     * 用户不设置查询条件默认查询所有
     *
     * @param wrapper
     * @return 返回自动生成的sql语句wrapper
     */
    private Wrapper wrapperEqNull(Wrapper wrapper, QueryType type){
        QueryWrapper wra = null;
        if(wrapper == null){
            wra = new QueryWrapper();
            if(type == QueryType.SELECT_ONE){
                wra.select("*").limit(0,1);
            }else{
                wra.select("*");
            }
        }else{
            return wrapper;
        }
        return wra;
    }

    @Override
    public <K> Map<K,T> selectMap(String keyColum, Wrapper wrapper) throws SQLException {
        Wrapper wra = this.wrapperEqNull(wrapper, QueryType.SELECT_MAP);
        OriginalStatement os = Select.splitSelect(wra, this.bean.getTableName());

        return (Map<K,T>)this.assistant.query(
                this.getSqlStatement(os,this.bean.getModule(),keyColum),
                QueryType.SELECT_MAP,isPrintInformation);
    }

    @Override
    public <K> Map<K, Map<String, Object>> selectMapToMap(String keyColum, Wrapper wrapper) throws SQLException {
        Wrapper wra = this.wrapperEqNull(wrapper, QueryType.SELECT_MAP_MAP);
        OriginalStatement os = Select.splitSelect(wra, this.bean.getTableName());

        return (Map<K, Map<String, Object>>)
                this.assistant.query(
                        this.getSqlStatement(os,null,keyColum),
                        QueryType.SELECT_MAP_MAP,isPrintInformation);
    }

    @Override
    public <K> Map<K,T> selectMapByMap(String keyColum, Map<String, Object> map) throws SQLException {
        Wrapper wrapper = this.mapWrapperSplit(map);
        OriginalStatement os = Select.splitSelect(
                wrapper, this.bean.getTableName());

        return (Map<K,T>)this.assistant.query(
                this.getSqlStatement(os,this.bean.getModule(),keyColum),
                QueryType.SELECT_MAP,isPrintInformation);
    }

    @Override
    public <K> Map<K,T> selectMapById(String keyColum, List<? extends Serializable> id) throws SQLException {
        Wrapper wrapper = this.queryWrapper;
        wrapper.in(this.bean.getIdName(),id);
        OriginalStatement os = Select.splitSelect(wrapper, this.bean.getTableName());

        return (Map<K, T>) this.assistant.query(
                this.getSqlStatement(os,this.bean.getModule(),keyColum),
                QueryType.SELECT_MAP,isPrintInformation);
    }

    @Override
    public List<T> selectListByMap(Map<String,Object> map) throws SQLException {
        Wrapper stringMap = this.mapWrapperSplit(map);
        OriginalStatement os = Select.splitSelect
                (stringMap, this.bean.getTableName());

        return (List<T>) this.assistant.query(
                this.getSqlStatement(os,this.bean.getModule(),null),
                QueryType.SELECT_LIST,isPrintInformation);
    }

    /**
     * 分解当用户以map的形式设置的筛选条件
     * map中的键值对统一使用eq拼接
     * @param map
     * @return Wrapper 拼接后的条件筛选器
     */
    private Wrapper mapWrapperSplit(Map<String,Object> map){
        Wrapper wrapper = this.queryWrapper;
        Object[] keys = map.keySet().toArray();

        for (int i = 0; i < keys.length; ++i) {
            if(i == keys.length-1){
                wrapper.eq((String)keys[i],map.get(keys[i]));
            }else{
                wrapper.eq((String)keys[i],map.get(keys[i])).and();
            }
        }

        return wrapper;
    }
}
