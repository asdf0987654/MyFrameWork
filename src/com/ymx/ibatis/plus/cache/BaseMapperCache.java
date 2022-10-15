package com.ymx.ibatis.plus.cache;

/**
 * @author 爱java的小于
 * @time 2022-8-27
 * @version 1.0.1
 *
 * 缓存
 */
public interface BaseMapperCache {
    public Object get(String key);

    public void put(String key,Object value);

    public void clear();
}
