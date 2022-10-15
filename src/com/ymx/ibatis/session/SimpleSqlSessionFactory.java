package com.ymx.ibatis.session;

import org.dom4j.DocumentException;

/**
 * @author 爱java的小于
 * @time 2022-7-23
 */
public interface SimpleSqlSessionFactory {
    /**
     * 创建一个SimpleSqlSession实例
     *
     * @return SimpleSqlSession
     * @throws DocumentException 解析xml过程可能会抛出此异常
     */
    public SimpleSqlSession openSession() throws DocumentException;

}
