package com.ymx.ibatis.structure;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * 当以Map形式返回数据时
 * 允许作为key的字段类型
 */
public class KeyTypeUtil {
    private static final Map<Integer,Class> SIMPLE_TYPES;
    static {
        SIMPLE_TYPES = new HashMap<Integer,Class>();
        SIMPLE_TYPES.put(Types.INTEGER,Integer.class);
        SIMPLE_TYPES.put(Types.BIGINT,Long.class);
        SIMPLE_TYPES.put(Types.DOUBLE,Double.class);
        SIMPLE_TYPES.put(Types.FLOAT,Float.class);
        SIMPLE_TYPES.put(Types.VARCHAR, String.class);
        SIMPLE_TYPES.put(Types.CHAR,Character.class);
        SIMPLE_TYPES.put(Types.TINYINT,Short.class);
        SIMPLE_TYPES.put(Types.SMALLINT,Short.class);
        SIMPLE_TYPES.put(Types.REAL,Float.class);
        SIMPLE_TYPES.put(Types.TIMESTAMP, Timestamp.class);
        SIMPLE_TYPES.put(Types.BOOLEAN, Boolean.class);
    }
    public static Class getType(int type){
        return SIMPLE_TYPES.get(type);
    }
}
