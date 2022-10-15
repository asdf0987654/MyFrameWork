package com.ymx.ibatis.config;

/**
 * @author 爱java的小于
 * @time 2022-7-27
 * @version 1.0.3
 */
public interface Configuration {
    /**
     * 返回不同数据库管理系统的的驱动
     * 必选
     * @return
     */
    public String getDriver();

    /**
     * 返回数据库的url连接地址
     * 必选
     * @return
     */
    public String getDataBaseUrl();

    /**
     * 返回数据库验证账户
     * 必选
     * @return
     */
    public String getUser();

    /**
     * 返回数据库验证密码
     * 必选
     * @return
     */
    public String getPassword();

    /**
     * 返回初始化数据库连接池中物理连接的数量
     * 可选
     * @return
     */
    public String getInitialSize();

    /**
     * 返回允许数据库连接池中物理连接的最大数量
     * 可选
     * @return
     */
    public String getMaxActive();

    /**
     * 返回允许连接空闲的最大时间
     * 可选
     *
     * @return
     */
    public String getTimeBetweenEvictionRunsMillis();
}
