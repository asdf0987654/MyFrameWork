package com.ymx.ibatis.structure;

import com.ymx.ibatis.exception.EntityAccessException;
import com.ymx.ibatis.plus.mapper.util.SplitMethod;
import com.ymx.ibatis.session.Invoke;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * @author 爱java的小于
 * 将结果集中的记录映射成java bean
 */
public class BeanStructure implements Structure{
    private final Invoke invoke;

    public BeanStructure(Invoke invoke){
        this.invoke = invoke;
    }

    @Override
    public <K,V> Map invokeToMap(String keyName,Class<?> bean,ResultSet resultSet)throws SQLException {
        FieldType[] fieldTypes = this.getFieldTypeInResultSet(resultSet);
        Map<FieldType,Method> methodMap = this.getMethodMap(fieldTypes,bean);
        if(methodMap.isEmpty()){
            return null;
        }else{
            Class v = this.findClassByName(fieldTypes,keyName);
            return this.invokeMap(resultSet,keyName,methodMap,v,bean);
        }
    }


    private <K,V> Map<K,V> invokeMap(ResultSet resultSet,String keyName , Map<FieldType,Method> methodMap,Class<K> kClass , Class<V> vClass) throws SQLException {
        Set<FieldType> keySet = methodMap.keySet();//存放字段信息
        Map<K,V> map = new HashMap<K,V>();
        Invoke invoke = this.invoke;

        Object tmpObj = null;
        Method tmpMethod = null;
        try {
            while (resultSet.next()) {//遍历记录集 转换每个字段的类型放入新创建的bean实例中
                tmpObj = vClass.newInstance();
                for (FieldType f : keySet) {
                    tmpMethod = methodMap.get(f);
                    invoke.invoke(resultSet, tmpMethod, f.getType(), f.getName(), tmpObj);//为实体类赋值
                }
                /*bean对象放入map*/
                map.put((K) resultSet.getObject(keyName), (V) tmpObj);
            }
        }catch (IllegalAccessException | InvocationTargetException e){
            throw new EntityAccessException(vClass.getName()+"方法访问失败");
        } catch (InstantiationException e) {
            throw new EntityAccessException(vClass.getName()+"创建实例失败");
        }

        return map;
    }

    @Override
    public <T> List<T> invokeToList(Class<?> bean , ResultSet resultSet) throws SQLException {
        FieldType[] fieldTypes = this.getFieldTypeInResultSet(resultSet);
        Map<FieldType,Method> methodMap = this.getMethodMap(fieldTypes,bean);
        if(methodMap.isEmpty()){
            return null;
        }

        return (List<T>) this.invokeList(methodMap,resultSet,bean);
    }


    /**
     * 将结果集中的数据映射成bean对象后加入list并返回
     *
     * @param methodMap
     * @param resultSet
     * @return
     * @throws Exception
     */
    private <T> List<T> invokeList(Map<FieldType, Method> methodMap, ResultSet resultSet, Class<T> module)throws SQLException{
        List<T> list = new ArrayList<T>();//存放bean对象的列表
        Set<FieldType> keySet = methodMap.keySet();//存放字段信息
        Invoke tmpInvoke = this.invoke;

        Object tmpObj = null;
        Method tmpMethod = null;
        try {
            while (resultSet.next()) {//遍历记录集 转换每个字段的类型放入新创建的bean对象中
                tmpObj = module.newInstance();
                for (FieldType f : keySet) {
                    tmpMethod = methodMap.get(f);
                    tmpInvoke.invoke(resultSet, tmpMethod, f.getType(), f.getName(), tmpObj);//为实体类赋值
                }
                /*bean对象放入list*/
                list.add((T) tmpObj);
            }
        }catch (InstantiationException | IllegalAccessException e) {
            throw new EntityAccessException(module.getName()+"创建实例失败");
        }catch (InvocationTargetException e){
            throw new EntityAccessException(module.getName()+"实例方法访问失败");
        }

        return list;
    }

    @Override
    public <T> Map<T,Map<String,Object>> invokeMap(String keyColumName , Class<?> bean , ResultSet resultSet)throws SQLException{
        FieldType[] fieldTypes = this.getFieldTypeInResultSet(resultSet);
        Class k = this.findClassByName(fieldTypes,keyColumName);

        return this.invokeMapInMap(keyColumName,k,fieldTypes,resultSet);
    }

    private <T> Map<T,Map<String,Object>> invokeMapInMap(String keyName,Class<T> t,FieldType[] fieldTypes,ResultSet resultSet)throws SQLException{
        //创建Map实例
        Map<T,Map<String,Object>> map = new HashMap<T,Map<String,Object>>();

        //获取map实例的put方法的对象
            Map<String,Object> value = null;
            while(resultSet.next()){
                value = new HashMap<String,Object>();
                for(FieldType f : fieldTypes){
                    value.put(f.getName(), resultSet.getObject(f.getName()));
                }
                map.put((T)resultSet.getObject(keyName), value);
            }

        return map;
    }


    @Override
    public List<Map<String,Object>> invokeMapToList(Class<?> bean , ResultSet resultSet)throws SQLException{
        FieldType[] fieldTypes = this.getFieldTypeInResultSet(resultSet);
        return this.mapToList(resultSet,fieldTypes);
    }

    private List<Map<String,Object>> mapToList(ResultSet resultSet,FieldType[] fieldTypes)throws SQLException{
        List<Map<String,Object>> re = new ArrayList<Map<String,Object>>();
        Map<String,Object> map = null;
        while(resultSet.next()){
            map = new HashMap<String,Object>();
            for(FieldType fieldType : fieldTypes){
                map.put(fieldType.getName(), resultSet.getObject(fieldType.getName()));
            }
            //add to list
            re.add(map);
        }

        return re;
    }

    /**
     * 返回指定字段类型的class实例
     *
     * @param fieldTypes
     * @param columName
     * @return Class实例
     */
    private Class findClassByName(FieldType[] fieldTypes,String columName)throws SQLException{
        Class v = null;
        for(FieldType f : fieldTypes){
            if(f.getName().equals(columName)){
                v = getKeyType(f.getType());
                break;
            }
        }
        if(v == null){
            throw new SQLException("错误的字段: '"+columName+"' 原因可能是1:指定的字段在结果集中不存在  2:指定字段的类型不能作为键返回");
        }
        return v;
    }

    /**
     * 根据字段类型常量返回Class实例
     * @param columType
     * @return Class实例
     */
    private Class getKeyType(int columType){
        return KeyTypeUtil.getType(columType);
    }

    /**
     * 拼接字段的set方法名
     * @param field 字段字符串
     * @return String 拼接后的方法名
     */
    private String stitchingSetMethod(String field){
        return SplitMethod.splitSetMethod(field);
    }


    /**
     * 将结果集中字段名和字段类型保存到数组中
     * 拼接并保存bean对象中的set方法
     *
     * @param resultSet
     * @return fieldTypes
     * @throws SQLException
     */
    private FieldType[] getFieldTypeInResultSet(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        FieldType[] fieldTypes = new FieldType[columnCount];

        for (int i = 1; i <= columnCount; i++) {
            fieldTypes[i-1] = new FieldType(
                    metaData.getColumnLabel(i),
                    this.stitchingSetMethod(metaData.getColumnLabel(i)),
                    metaData.getColumnType(i)
            );
        }

        return fieldTypes;
    }


    /**
     * 根据结果集中的字段创建set方法对象，
     * 将set方法对象加入map中
     *
     * @param fieldTypes FieldType数组
     * @return map
     */
    private Map<FieldType,Method> getMethodMap(FieldType[] fieldTypes , Class module){
        Map<FieldType,Method> methodMap = new HashMap<FieldType,Method>();
        Method[] methods = module.getMethods();
        for(FieldType fieldType : fieldTypes){
            for(Method method : methods){
                if(method.getName().equals(fieldType.getSetMethod())){
                    methodMap.put(fieldType, method);
                    break;
                }
            }
        }

        return methodMap;
    }

    /**
     * 封装结果集字段信息
     */
    private class FieldType<T>{
        /*字段名称*/
        private final String name;
        /*字段对应bean对象中的set方法*/
        private final String setMethod;
        /*字段类型*/
        private final int type;

        public String getName() {
            return name;
        }

        public int getType() {
            return type;
        }

        public String getSetMethod() {
            return setMethod;
        }

        public FieldType(String fieldName , String setMethod , int fieldType){
            this.name = fieldName;
            this.setMethod = setMethod;
            this.type = fieldType;
        }
    }


}
