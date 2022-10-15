package com.ymx.ibatis.plus.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 爱java的小于
 * @time 2022-8-27
 * @version 1.0.1
 */
public class DefaultCache implements BaseMapperCache{
    private Map<Object,Object> cache;

    public DefaultCache(){
        this.cache = new HashMap<Object,Object>();
    }
    @Override
    public Object get(String key) {
        return cache.get(key);
    }

    @Override
    public void put(String key, Object value) {
        this.cache.put(key,value);
    }

    @Override
    public void clear() {
        this.cache.clear();
    }
}
