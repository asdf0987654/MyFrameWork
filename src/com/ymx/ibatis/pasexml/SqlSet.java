package com.ymx.ibatis.pasexml;

import com.ymx.ibatis.exception.SqlMapperException;

/**
 * @author 爱Java的小于
 *
 * 存放所有sql配置文件中的sql信息
 */
public interface SqlSet {
    /**
     * 根据sql的ID号返回Mapper接口
     * @param sqlID
     * @return SqlStatement
     * @throws SqlMapperException 当找不到Mapper时会抛出此异常
     */
    public abstract SqlStatement getMapper(String sqlID)
            throws SqlMapperException;
}
