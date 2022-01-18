package com.conghuhu;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author conghuhu
 * @create 2021-09-30 20:23
 */
@SpringBootTest
class Test {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @org.junit.jupiter.api.Test
    void testRedis() {
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        System.out.println(passwordEncoder.encode("cong0917"));
        stringRedisTemplate.opsForValue().set("yourName","lisi");
        System.out.println(stringRedisTemplate.opsForValue().get("yourName"));
    }
}
