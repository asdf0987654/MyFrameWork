package com.ymx.ibatis.plus.wrapper.split;

import com.ymx.ibatis.plus.mapper.util.SplitTypes;

import java.util.List;

/**
 * @author 爱java的小于
 * @time 2022-8-27
 * @version 1.0.1
 */
public class SplitSqlString {
    /**
     * 拼接单个操作符
     * @param code
     * @param sqlBuffer
     */
    public synchronized static void splitCode(String code,StringBuffer sqlBuffer){
        sqlBuffer.append(" ").append(code).append(" ");
    }

    /**
     * 拼接二元操作
     * @param code
     * @param sqlBuffer
     */
    public synchronized static void splitBinaryOperator(String code, StringBuffer sqlBuffer, Object one, Object two, List param){
        param.add(two);
        sqlBuffer.append(one).append(" ").append(code).append(" ").append('?');
    }

    /**
     * 拼接集合范围性操作
     * @param code
     * @param args
     */
    public synchronized static void splitSetIn(String code,StringBuffer sqlBuffer,List param,Object... args){
        sqlBuffer.append(code);
        sqlBuffer.append('(');
        for(int i = 0 ; i < args.length ; ++i){
            param.add(args[i]);
            sqlBuffer.append('?');
            sqlBuffer.append(',');
        }
        //最后一个逗号去掉
        sqlBuffer.deleteCharAt(sqlBuffer.length()-1);
        sqlBuffer.append(')');
    }

    /**
     * 拼接 in sql语句 或 not in sql语句
     * @param colum
     * @param sql
     * @param code
     * @param buffer
     */
    public synchronized static void splitSetInSql(String colum,String sql,String code,StringBuffer buffer){
        buffer.append(colum).append(" ").append(code).append(" (").append(sql).append(")");
    }

    /**
     * 关键字的拼接
     * @param types
     * @param args
     */
    public synchronized static void special(SplitTypes types , StringBuffer sqlBuffer, List param , Object... args){
        if(types == SplitTypes.BETWEEN){
            sqlBuffer.append(args[0]).append(" between ? and ?");
            param.add(args[1]) ; param.add(args[2]);
        }else if(types == SplitTypes.NOTBETWEEN){
            sqlBuffer.append(args[0]).append("not between ? and ?");
            param.add(args[1]) ; param.add(args[2]);
        }else if(types == SplitTypes.ISNULL){
            sqlBuffer.append(args[0]).append(" is null");
        }else if(types == SplitTypes.ISNOTNULL){
            sqlBuffer.append(args[0]).append(" is not null");
        }else if(types == SplitTypes.LIKE){
            sqlBuffer.append(args[0]).append(" like %?%");
            param.add(args[1]);
        }else if(types == SplitTypes.LIKELEFT){
            sqlBuffer.append(args[0]).append(" like %?");
            param.add(args[1]);
        }else if(types == SplitTypes.LIKERIGHT){
            sqlBuffer.append(args[0]).append(" like ?%");
            param.add(args[1]);
        }else if(types == SplitTypes.SELECT){
            select(sqlBuffer,args);
        }else if(types == SplitTypes.LIMIT){
            sqlBuffer.append("? , ?");
            param.add(args[0]);
            param.add(args[1]);
        }else if(types == SplitTypes.HAVING){
            sqlBuffer.append(args[0]);
        }else if(types == SplitTypes.GROUPBY){
            splitGroupBy(sqlBuffer,args);
        }else if(types == SplitTypes.ORDERBY){
            splitOrderBy(sqlBuffer,args);
        }
    }

    private static void splitOrderBy(StringBuffer sb,Object... args){
        //order by name asc,score asc,sex asc
        for(Object o : args){
            if("false".equals(o.toString()) || "true".equals(o.toString())){
                sb.append(o.toString().equals("true") ? "asc," : "desc,");
            }else{
                sb.append(o).append(" ");
            }
        }
        sb.deleteCharAt(sb.length()-1);
    }

    private static void splitGroupBy(StringBuffer sqlBuffer,Object...  args){
        joinColum(sqlBuffer ,args);
    }

    private static void joinColum(StringBuffer sqlBuffer,Object... args){
        for(Object i : args) {
            sqlBuffer.append(i).append(",");
        }
        sqlBuffer.deleteCharAt(sqlBuffer.length()-1);
    }

    private static void select(StringBuffer sqlBuffer,Object...  args){
        joinColum(sqlBuffer,args);
    }

}
