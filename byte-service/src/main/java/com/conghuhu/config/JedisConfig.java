package com.conghuhu.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author conghuhu
 * @create 2022-03-12 16:02
 */
@Slf4j
@Configuration
public class JedisConfig extends CachingConfigurerSupport {
    /**
     * 初始化连接超时时间
     */
    private static final int DEFAULT_CONNECTION_TIMEOUT = 5000;
    /**
     * 查询超时时间
     */
    private static final int DEFAULT_SO_TIMEOUT = 2000;

    private static final String HOST = "r-f8zbpxj2lyp6sennu4pd.redis.rds.aliyuncs.com";

    private static final int PORT = 6379;

    private static final String PASSWORD = "@cong0917";

    public JedisPool redisPoolFactory() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(16);
        jedisPoolConfig.setMaxIdle(16);
        jedisPoolConfig.setMinIdle(8);
        jedisPoolConfig.setMaxWaitMillis(10000);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestWhileIdle(true);
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(5000);

        JedisPool jedisPool = new JedisPool(jedisPoolConfig, HOST, PORT, DEFAULT_CONNECTION_TIMEOUT,
                DEFAULT_SO_TIMEOUT, PASSWORD, 0, null);
        log.info("JedisPool注入成功！！redis地址：" + HOST + ":" + PORT);
        return jedisPool;
    }
}
