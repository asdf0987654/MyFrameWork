package com.ymx.ibatis.session.proxy;

import com.ymx.ibatis.pasexml.SqlStatement;
import com.ymx.ibatis.exception.CommentException;
import com.ymx.ibatis.session.QueryType;
import com.ymx.ibatis.session.proxy.anocation.Delete;
import com.ymx.ibatis.session.proxy.anocation.Insert;
import com.ymx.ibatis.session.proxy.anocation.Select;
import com.ymx.ibatis.session.proxy.anocation.Update;
import com.ymx.ibatis.session.proxy.util.Placeholder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理注解中的语句及参数
 */
public class MapperStatementHandler implements StatementHandler {
    /*方法签名*/
    private Map<String,SqlStatement> statementMap;

    public MapperStatementHandler(Class mapperClass){
        try {
            initialize(mapperClass);
        } catch (CommentException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void initialize(Class mapperClass) throws CommentException {
        this.statementMap = new HashMap<>();
        //获取接口所有的方法将注解信息读取出来
        Method[] declaredMethods = mapperClass.getDeclaredMethods();
        for (Method m : declaredMethods){
            statementMap.put(this.parametersAsString(m),
                    parseToStatement(getMethodInfo(m)));
        }
    }

    /**
     * 读取接口方法中的注解信息
     *
     * @param m 接口方法
     * @return SqlStatement
     * @throws CommentException
     */
    private SqlStatement getMethodInfo(Method m) throws CommentException {
        Annotation annotation = this.getAnnotation(m);
        if(annotation instanceof Select){
            Select select = (Select) annotation;
            String sqlString = select.value();
            String resultType = select.resultType();
            String keyName = select.keyName();
            QueryType queryType = select.queryType();
            if((queryType != QueryType.SELECT_NUMBER) &&
                "".equals(resultType)){
                throw new CommentException("方法:"+m.getName()+" 中缺少返回值类型");
            }
            Class module = null;
            try {
                module = Class.forName(resultType);
            } catch (ClassNotFoundException e) {
                throw new CommentException("方法:"+m.getName()+
                        " 中resultType属性值:"+resultType+" 是一个不存在的类型");
            }
            SqlStatement statement = new SqlStatement(sqlString,module,keyName,null);
            statement.setQueryType(queryType);
            return statement;
        }else{
            String sqlString = null;
            if (annotation instanceof Update){
                sqlString = ((Update)annotation).value();
            }else if (annotation instanceof Insert){
                sqlString = ((Insert)annotation).value();
            } else if (annotation instanceof Delete){
                sqlString = ((Delete)annotation).value();
            }
            return new SqlStatement(sqlString,null,null,null);
        }
    }

    /**
     * 返回method对象上的注解实例
     *
     * @param method
     * @return
     */
    private Annotation getAnnotation(Method method) throws CommentException {
        Annotation annotation = null;
        if(method.getAnnotation(Select.class) != null){
            annotation = method.getAnnotation(Select.class);
        }else if(method.getAnnotation(Update.class) != null){
            annotation = method.getAnnotation(Update.class);
        }else if(method.getAnnotation(Delete.class) != null){
            annotation = method.getAnnotation(Delete.class);
        }else if(method.getAnnotation(Insert.class) != null){
            annotation = method.getAnnotation(Insert.class);
        }
        //如果指定方法不存在sql语句抛出异常
        if(annotation == null)
            throw new CommentException("为方法:"+method.getName()+"添加注解");

        return annotation;
    }


    /**
     * 将Statement中的sql提取出来
     * 解析其中的sql占位符
     *
     * @param statement statement
     * @return Statement
     */
    private SqlStatement parseToStatement(SqlStatement statement){
        /*将格式sql字符串分解为原始sql 在参数插入位置替换为 ? */
        List<String> parse = Placeholder.parse(statement.getSqlCode(), '{', '}', '?');
        /*替换后的sql字符串放置于List列表最后一个位置*/
        String sqlString = parse.remove(parse.size() - 1);
        statement.setSqlCode(sqlString);
        statement.setPlaceholderNameList(parse);

        return statement;
    }




    /**
     * 生成方法签名秘钥
     * 根据该秘钥区分指定方法的执行
     *
     * @param method 方法实例
     * @return 方法签名秘钥
     */
    private String parametersAsString(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        StringBuilder paramString = null;
        if (parameterTypes.length == 0)
            return method.getName();

        paramString = new StringBuilder(method.getName());
        paramString.append(parameterTypes[0].getSimpleName());
        for (int i = 1; i < parameterTypes.length; i++) {
            paramString.append(parameterTypes[i].getSimpleName());
        }
        return paramString.toString();
    }


    @Override
    public SqlStatement getStatement(Method method, Object[] args) throws ClassNotFoundException, SQLException {
        /*根据method方法签名秘钥获取SqlStatement*/
        SqlStatement statement = this.statementMap.get(this.parametersAsString(method));
        /*根据方法的参数列表，按sql占位符的顺序读取指定值，整合到obj数组中放入statement*/
        Object[] params = Placeholder.getStatementParam(statement.getPlaceholderNameList(),
                method.getName(),args);
        statement.setParams(params);
        return statement;
    }
}