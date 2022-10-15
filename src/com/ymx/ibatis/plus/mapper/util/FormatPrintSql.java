package com.ymx.ibatis.plus.mapper.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 爱java的小于
 *  * @time 2022-8-27
 *  * @version 1.0.1
 * 格式化打印sql语句
 */
public class FormatPrintSql {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日-HH点mm分ss秒");
    public synchronized static void printFormatQuery(String sql,long runTime){
        printSystemTime();
        printSqlString(sql);
        printRunTime(runTime);
        System.out.println();
    }
    public synchronized static void printFormatUpdate(String sql,long runTime,int rows){
        printSystemTime();
        printSqlString(sql);
        printRunTime(runTime);
        printAffectedRows(rows);
        System.out.println();
    }
    private static void printRunTime(long runTime){
        System.out.println("===>RunTime: "+runTime+"ms");
    }
    private static void printSqlString(String sql){
        System.out.println("===>sql : "+sql);
    }
    public static void printSystemTime(){
        Date now = new Date();
        System.out.println("===>time: "+sdf.format(now));
    }
    public static void printAffectedRows(int rows){
        System.out.println("===>affected rows: "+rows);
    }
}
