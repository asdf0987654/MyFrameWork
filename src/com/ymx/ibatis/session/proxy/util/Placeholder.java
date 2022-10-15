package com.ymx.ibatis.session.proxy.util;

import com.ymx.ibatis.exception.EntityAccessException;
import com.ymx.ibatis.plus.mapper.util.SplitMethod;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Placeholder {
    /**
     * 从sql字符串中根据左右边界符解析出关键字段放入列表中返回
     * 占位符位置替换为指定标识符
     * 列表的最后一个元素为替换后的sql字符串
     *
     * @param sql     sql字符串
     * @param pStart  占位符开始标识
     * @param pRight  占位符结束标识
     * @param replace 将指定占位符替换为自定标识符
     * @return List
     */
    public synchronized static List<String> parse(String sql, char pStart, char pRight, char replace) {
        List<String> paramName = new ArrayList<String>();
        StringBuilder paramBuilder = null;
        StringBuilder sqlBuilder = new StringBuilder();
        String sqlString = sql.toString();
        int sqlLength = sqlString.length();

        char c = ' ';
        //保存每个占位符的开始位置
        boolean savePStartIndex = false;
        for (int i = 0; i < sqlLength; i++) {
            c = sqlString.charAt(i);
            if (c == pStart) {
                sqlBuilder.append('?');
                savePStartIndex = true;
                paramBuilder = new StringBuilder();
                continue;
            } else if (c == pRight) {
                paramName.add(paramBuilder.toString());
                savePStartIndex = false;
                continue;
            }
            //savePStartIndex决定字符c放入的缓冲区
            if (savePStartIndex) {
                paramBuilder.append(c);
            } else {
                sqlBuilder.append(c);
            }
        }
        //将原生态sql放入列表最后
        paramName.add(sqlBuilder.toString());

        return paramName;
    }

    /**
     * 根据占位符的名称返回参数列表的值
     * 如果参数args的值为封装类
     * 使用反射机制读取实例中的值放入参数数组中并返回
     * 如果参数args为多个参数
     * 直接返回该args
     *
     * @param param 原生sql语句中的占位符关键字 param的大小可能为0
     * @param args  方法实例的参数
     * @return Object[]
     */
    public synchronized static Object[] getStatementParam(List<String> param, String srcName, Object[] args) throws SQLException {
        if (param.size() == 0) {
            return args;
        } else {
            /*
                判断方法参数列表的大小为1
             */
            if (args.length == 1) {
                /*如果是常数类型直接返回*/
                if (Constant.isConstant(args[0])) {
                    return args;
                    /*为封装类型时利用反射机制读取属性数据并返回*/
                } else {
                    Object bean = args[0];
                    Class beanClass = bean.getClass();
                    Object[] innerArgs = new Object[param.size()];

                    /*
                      遍历占位符参数名称
                      拼接get为方法并从实例中取出指定值
                     */
                    for (int i = 0; i < param.size(); ++i) {
                        //判断占位符参数长度等于零抛出异常
                        if (param.get(i).length() == 0) {
                            throw new SQLException("参数为实体类型时，sql中占位符不能为{},错误位置: "+srcName);
                        } else {
                            String getMethodName = SplitMethod.splitGetMethod(param.get(i));
                            try {
                                innerArgs[i] = beanClass.getDeclaredMethod(getMethodName).invoke(bean);
                            } catch (NoSuchMethodException e) {
                                throw new EntityAccessException("类:" + beanClass.getName() + "缺少方法:" + getMethodName);
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                                System.exit(1);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                                System.exit(1);
                            }
                        }

                    }
                    return innerArgs;
                }
                /*
                    方法的参数列表为多个参数时
                    默人该参数列表所有元素为常数类型
                    返回次参数列表
                 */
            } else {
                return args;
            }
        }
    }

}
