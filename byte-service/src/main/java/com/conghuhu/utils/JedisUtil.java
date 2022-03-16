package com.conghuhu.utils;

import com.aliyun.tair.tairbloom.TairBloom;
import com.aliyun.tair.tairhash.TairHash;
import com.aliyun.tair.tairhash.params.ExhsetParams;
import com.aliyun.tair.tairroaring.TairRoaring;
import com.aliyun.tair.tairstring.TairString;
import com.aliyun.tair.tairstring.params.CasParams;
import com.aliyun.tair.tairts.TairTs;
import com.aliyun.tair.tairts.params.ExtsAttributesParams;
import com.aliyun.tair.tairts.results.ExtsDataPointResult;
import com.aliyun.tair.tairts.results.ExtsSkeyResult;
import com.conghuhu.config.JedisConfig;
import com.conghuhu.pubSub.MessageListener;
import com.conghuhu.pubSub.SubClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

/**
 * @author conghuhu
 * @create 2022-03-12 21:36
 */
@Slf4j
@Component
public class JedisUtil {

    public static final String CHANNEL = "product_message";

    private final JedisConfig jedisConfig;

    private final SubClient subClient;

    private static JedisPool jedisPool;

    public JedisUtil(JedisConfig jedisConfig, SubClient subClient) {
        this.jedisConfig = jedisConfig;
        this.subClient = subClient;
    }


    @PostConstruct
    public void init() {
        jedisPool = jedisConfig.redisPoolFactory();
        subClient.subscribeChannel(CHANNEL);
    }

    /**
     * 获取分布式锁
     *
     * @param lockKey
     * @param requestId
     * @param expireTime
     * @return
     */
    public static boolean tryGetDistributedLock(String lockKey, String requestId, long expireTime) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String result = jedis.set(lockKey, requestId, SetParams.setParams().nx().ex(expireTime));
            if ("OK".equals(result)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return false;
    }

    /**
     * 释放CodeGlance分布式锁
     *
     * @param lockKey
     * @param requestId
     * @return
     */
    public static boolean releaseDistributedLock(String lockKey, String requestId) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            TairString tairString = new TairString(jedis);
            Long ret = tairString.cad(lockKey, requestId);
            if (1 == ret) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return false;
    }

    /**
     * 锁续命
     *
     * @param resourceKey
     * @param randomValue
     * @param expireTime
     * @return
     */
    public static boolean renewDistributedLock(String resourceKey, String randomValue, int expireTime) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            TairString tairString = new TairString(jedis);
            CasParams casParams = new CasParams();
            casParams.ex(expireTime);
            Long ret = tairString.cas(resourceKey, randomValue, randomValue, casParams);
            return 1 == ret;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return false;
    }

    /**
     * 设置哈希表某个字段的值
     * 新建field并成功为它设置值：1。
     * field已经存在，成功覆盖旧值：0。
     * 指定了XX且field不存在：-1。
     * 指定了NX且field已经存在：-1。
     * 指定了VER且版本和当前版本不匹配："ERR update version is stale"。
     * 其它情况返回相应的异常信息。
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    public static long setHash(String key, String field, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            TairHash tairHash = new TairHash(jedis);
            ExhsetParams exhsetParams = new ExhsetParams();
            exhsetParams.nx().ex(600);
            return tairHash.exhset(key, field, value, exhsetParams);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return -1;
    }

    /**
     * 获取hash中某field对应的value值
     * field存在且操作成功：field对应的值。
     * key不存在或者field不存在：nil。
     * 其它情况返回相应的异常信息。
     *
     * @param key
     * @param field
     * @return
     */
    public static String getHashValue(String key, String field) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            TairHash tairHash = new TairHash(jedis);
            String res = tairHash.exhget(key, field);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return null;
    }

    /**
     * 为某个字段设置相对有效期
     * field存在且设置成功：1。
     * field不存在：0。
     * 其它情况返回相应的异常信息。
     *
     * @param key
     * @param field
     * @param expireTime
     * @return
     */
    public static boolean setExpire(String key, String field, int expireTime) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            TairHash tairHash = new TairHash(jedis);
            Boolean res = tairHash.exhexpire(key, field, expireTime);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return false;
    }

    /**
     * 获取所有未过期的字段
     *
     * @param key
     * @return
     */
    public static Set<String> getAllFields(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            TairHash tairHash = new TairHash(jedis);
            Set<String> res = tairHash.exhkeys(key);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return null;
    }

    /**
     * 删除hash中的field
     * key不存在或者field不存在：0。
     * 删除成功：1。
     * 其它情况返回相应的异常信息。
     *
     * @param key
     * @param field
     * @return
     */
    public static long deleteField(String key, String field) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            TairHash tairHash = new TairHash(jedis);
            Long res = tairHash.exhdel(key, field);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return 0;
    }

    /**
     * 设置bit
     *
     * @param key
     * @param offset
     * @param value
     * @return
     */
    public static long setBit(String key, long offset, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            TairRoaring tairRoaring = new TairRoaring(jedis);
            return tairRoaring.trsetbit(key, offset, value);
        } catch (Exception e) {
            e.printStackTrace(); // handle exception
        } finally {
            jedis.close();
        }
        return -1;
    }

    /**
     * 获取bit
     *
     * @param key
     * @param offset
     * @return
     */
    public static long getBit(String key, long offset) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            TairRoaring tairRoaring = new TairRoaring(jedis);
            return tairRoaring.trgetbit(key, offset);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return -1;
    }

    /**
     * 创建一个布隆过滤器
     *
     * @param key
     * @param capacity
     * @param errorRate
     * @return
     */
    public static boolean createBloom(String key, long capacity, double errorRate) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            TairBloom tairBloom = new TairBloom(jedis);
            String result = tairBloom.bfreserve(key, capacity, errorRate);
            if ("OK".equals(result)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return false;
    }

    /**
     * 添加元素至布隆过滤器中
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean addItemToBloom(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            TairBloom tairBloom = new TairBloom(jedis);
            Boolean res = tairBloom.bfadd(key, value);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return false;
    }

    /**
     * 判断元素是否可能存在布隆过滤器中
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean existItemInBloom(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            TairBloom tairBloom = new TairBloom(jedis);
            Boolean res = tairBloom.bfexists(key, value);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return false;
    }

    /**
     * 向Ts中添加序列
     *
     * @param pKey
     * @param sKey
     * @param ts
     * @param value
     * @return
     */
    public static boolean addItemToTs(String pKey, String sKey, String ts, double value, long expireTime, String label) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            TairTs tairTs = new TairTs(jedis);
            ExtsAttributesParams extsAttributesParams = new ExtsAttributesParams();
            extsAttributesParams.dataEt(expireTime);
            extsAttributesParams.labels(new ArrayList<>(Arrays.asList(label)));
            String res = tairTs.extsadd(pKey, sKey, ts, value, extsAttributesParams);
            if ("OK".equals(res)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return false;
    }

    /**
     * 获取最近的一组Datapoint数据
     *
     * @param pKey
     * @param sKey
     * @param fromTs
     * @param toTs
     * @return
     */
    public static ArrayList<ExtsDataPointResult> getLastTsValue(String pKey, String sKey, String fromTs, String toTs) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            TairTs tairTs = new TairTs(jedis);
            ExtsSkeyResult result = tairTs.extsrange(pKey, sKey, fromTs, toTs);
            ArrayList<ExtsDataPointResult> dataPoints = result.getDataPoints();
            return dataPoints;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return null;
    }

    /**
     * 添加item至set中
     *
     * @param key
     * @param value
     * @return
     */
    public static long addItemToSet(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Long res = jedis.sadd(key, value);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return 0;
    }

    /**
     * 移除set中item元素
     *
     * @param key
     * @param value
     * @return
     */
    public static long removeItemInSet(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Long res = jedis.srem(key, value);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return 0;
    }

    /**
     * 获取set中的元素
     *
     * @param key
     * @return
     */
    public static Set<String> getSetItem(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.smembers(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return null;
    }


    /**
     * 检验是否存在key
     *
     * @param key
     * @return
     */
    public static boolean hasKey(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.exists(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return false;
    }

    /**
     * 删除tair中的key
     *
     * @param key
     * @return
     */
    public static long deleteKey(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Long res = jedis.del(key);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return 0;
    }

    /**
     * 发布消息
     *
     * @param channel
     * @param message
     */
    public static long pub(String channel, String message) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Long publishCount = jedis.publish(channel, message);
            return publishCount;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return 0;
    }


    /**
     * 关闭消息通道
     *
     * @param channel
     */
    public static void closePublishChannel(String channel) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.publish(channel, "quit");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
    }

    public static void subscribe(MessageListener listener, String channel) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.subscribe(listener, channel);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
    }

}
