package com.ymx.ibatis.session.proxy.util;

import java.util.Date;

public class Constant {
    public static synchronized boolean isConstant(Object entity){
        return (entity instanceof String || entity instanceof Number
                || entity instanceof Date || entity instanceof Character
                || entity instanceof Boolean);
    }
}
