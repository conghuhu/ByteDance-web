package com.conghuhu.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author conghuhu
 * @create 2022-01-18 19:58
 */
@Slf4j
@Component
public class RedisUtil {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    /**
     * 根据key读取数据
     */
    public Object get(final String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 写入数据
     */
    public boolean set(final String key, Object value) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        try {
            redisTemplate.opsForValue().set(key, value);
            log.info("存入redis成功，key：{}，value：{}", key, value);
            return true;
        } catch (Exception e) {
            log.error("存入redis失败，key：{}，value：{}", key, value);
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 根据key更新数据
     */
    public boolean update(final String key, Object value) {
        boolean res = false;
        try {
            redisTemplate.opsForValue().getAndSet(key, value);
            log.info("更新redis成功，key：{}，value：{}", key, value);
            res = true;
        } catch (Exception e) {
            log.error("更新redis失败，key：{}，value：{}", key, value);
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 根据key删除数据
     */
    public boolean del(final String key) {
        boolean res = false;
        try {
            redisTemplate.delete(key);
            res = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 是否存在key
     */
    public boolean hasKey(final String key) {
        boolean res = false;
        try {
            res = redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 给指定的key设置存活时间
     * 默认为-1，表示永久不失效
     */
    public boolean setExpire(final String key, long seconds) {
        boolean res = false;
        try {
            if (0 < seconds) {
                res = redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 获取指定key的剩余存活时间
     * 默认为-1，表示永久不失效，-2表示该key不存在
     */
    public long getExpire(final String key) {
        long res = 0;
        try {
            res = redisTemplate.getExpire(key, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 移除指定key的有效时间
     * 当key的有效时间为-1即永久不失效和当key不存在时返回false，否则返回true
     */
    public boolean persist(final String key) {
        boolean res = false;
        try {
            res = redisTemplate.persist(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
