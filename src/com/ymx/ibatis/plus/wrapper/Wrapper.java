package com.ymx.ibatis.plus.wrapper;

import com.ymx.ibatis.plus.wrapper.split.SplitSqlString;
import com.ymx.ibatis.plus.mapper.util.SplitTypes;
import com.ymx.ibatis.plus.wrapper.statement.StatementMap;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 兴哥的小新
 * @time 2022-8-20
 *
 * Wrapper条件筛选器可以自定的范围
 *  select后面的字段
 *  where后面的条件
 *  group by分组
 *  order by排序
 *  having 条件筛选
 *  limit分页
 */
public abstract class Wrapper {
    private StringBuffer whereBuffer;
    private StringBuffer groupByBuffer;
    private StringBuffer orderByBuffer;
    private StringBuffer havingBuffer;
    private StringBuffer limitBuffer;
    private List statementParams;

    public Wrapper(){
        ini();
    }

    /**
     * 返回sql不同关键字的参数语句
     */
    public abstract StatementMap getStatementMap();

    private void ini(){
        this.whereBuffer = new StringBuffer();
        this.groupByBuffer = new StringBuffer();
        this.orderByBuffer = new StringBuffer();
        this.havingBuffer = new StringBuffer();
        this.limitBuffer = new StringBuffer();
        if(this.statementParams == null){
            this.statementParams = new ArrayList();
        }else{
            this.statementParams.clear();
        }
    }

    protected void resetSqlStringBuffer(){
        ini();
    }

    protected StringBuffer getWhereBuffer() {
        return whereBuffer;
    }

    protected StringBuffer getGroupByBuffer() {
        return groupByBuffer;
    }

    protected StringBuffer getOrderByBuffer() {
        return orderByBuffer;
    }

    protected StringBuffer getHavingBuffer() {
        return havingBuffer;
    }

    protected StringBuffer getLimitBuffer() {
        return limitBuffer;
    }

    protected List getStatementParams(){
        return statementParams;
    }


    public Wrapper parenLeft(){
        SplitSqlString.splitCode("(",this.whereBuffer);
        return this;
    }

    public Wrapper parenRight(){
        SplitSqlString.splitCode(")",this.whereBuffer);
        return this;
    }


    public Wrapper eq(String col,Object value){
        SplitSqlString.splitBinaryOperator("=",this.whereBuffer,col,value,statementParams);
        return this;
    }

    public Wrapper eqInSql(String col,String subSql){
        this.splitInSql(col,"=",subSql);
        return this;
    }

    public Wrapper ne(String col,Object value){
        SplitSqlString.splitBinaryOperator("!=",this.whereBuffer,col,value,statementParams);
        return this;
    }

    public Wrapper neInSql(String col,String subSql){
        this.splitInSql(col,"!=",subSql);
        return this;
    }

    private void splitInSql(String colum,String code,String subSql){
        SplitSqlString.splitSetInSql(colum,subSql,code,this.whereBuffer);
    }

    public Wrapper and(){
        SplitSqlString.splitCode("and",this.whereBuffer);
        return this;
    }

    public Wrapper or(){
        SplitSqlString.splitCode("or",this.whereBuffer);
        return this;
    }

    public Wrapper gt(String colum,Object value){
        SplitSqlString.splitBinaryOperator(">",this.whereBuffer,colum,value,statementParams);
        return this;
    }

    public Wrapper gtInSql(String colum,String subSql){
        this.splitInSql(colum,">",subSql);
        return this;
    }

    public Wrapper ge(String colum,Object value){
        SplitSqlString.splitBinaryOperator(">=",this.whereBuffer,colum,value,statementParams);
        return this;
    }

    public Wrapper geInSql(String colum,String subSql){
        this.splitInSql(colum,">=",subSql);
        return this;
    }

    public Wrapper lt(String colum,Object value){
        SplitSqlString.splitBinaryOperator("<",this.whereBuffer,colum,value,statementParams);
        return this;
    }

    public Wrapper ltInSql(String colum,String subSql){
        this.splitInSql(colum,"<",subSql);
        return this;
    }

    public Wrapper le(String colum,Object value){
        SplitSqlString.splitBinaryOperator("<=",this.whereBuffer,colum,value,statementParams);
        return this;
    }

    public Wrapper leInSql(String colum,String subSql){
        this.splitInSql(colum,"<=",subSql);
        return this;
    }

    public Wrapper between(String colum,Object one,Object two){
        SplitSqlString.special(SplitTypes.BETWEEN,this.whereBuffer,
                statementParams,colum,one,two);
        return this;
    }

    public Wrapper notIn(String colum,List values){
        SplitSqlString.splitSetIn(colum+" not in ",this.whereBuffer,
                statementParams,values.toArray());
        return this;
    }

    public Wrapper in(String colum,List values){
        SplitSqlString.splitSetIn(colum+" in ",this.whereBuffer,
                statementParams,values.toArray());
        return this;
    }

    public Wrapper like(String colum , String value){
        SplitSqlString.special(SplitTypes.LIKE,this.whereBuffer,statementParams,colum,value);
        return this;
    }

    public Wrapper leftLike(String colum , String value){
        SplitSqlString.special(SplitTypes.LIKELEFT,this.whereBuffer,statementParams,colum,value);
        return this;
    }

    public Wrapper rightLike(String colum , String value){
        SplitSqlString.special(SplitTypes.LIKERIGHT,this.whereBuffer,statementParams,colum,value);
        return this;
    }

    public Wrapper isNull(String colum){
        SplitSqlString.special(SplitTypes.ISNULL,this.whereBuffer,statementParams,colum);
        return this;
    }

    public Wrapper isNotNull(String colum){
        SplitSqlString.special(SplitTypes.ISNOTNULL,this.whereBuffer,statementParams,colum);
        return this;
    }

    public Wrapper inSql(String colum,String sql){
        SplitSqlString.splitSetInSql(colum,sql,"in",this.whereBuffer);
        return this;
    }

    public Wrapper notInSql(String colum,String sql){
        SplitSqlString.splitSetInSql(colum,sql,"not in",this.whereBuffer);
        return this;
    }

    public Wrapper groupBy(List<String> colum){
        SplitSqlString.special(SplitTypes.GROUPBY,this.groupByBuffer,statementParams,colum.toArray());
        return this;
    }

    public Wrapper orderBy(List colum){
        SplitSqlString.special(SplitTypes.ORDERBY,this.orderByBuffer,statementParams,colum.toArray());
        return this;
    }

    public Wrapper having(String havingSql){
        SplitSqlString.special(SplitTypes.HAVING,this.havingBuffer,statementParams,havingSql);
        return this;
    }

    public Wrapper limit(int one,int two){
        SplitSqlString.special(SplitTypes.LIMIT,this.limitBuffer,statementParams,one,two);
        return this;
    }


}

