package com.conghuhu.cache;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.conghuhu.result.JsonResult;
import com.conghuhu.result.ResultTool;
import com.conghuhu.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;

/**
 * @author conghuhu
 * @create 2021-10-14 10:35
 */
@Aspect
@Component
@Slf4j
public class CacheAspect {

    private final RedisUtil redisUtil;

    public CacheAspect(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Pointcut("@annotation(com.conghuhu.cache.Cache)")
    public void pt() {
    }

    @Around("pt()")
    public Object around(ProceedingJoinPoint pjp) {
        try {
            Signature signature = pjp.getSignature();
            //类名
            String className = pjp.getTarget().getClass().getSimpleName();
            //调用的方法名
            String methodName = signature.getName();

            Class[] parameterTypes = new Class[pjp.getArgs().length];
            Object[] args = pjp.getArgs();
            //参数
            String params = "";
            for (int i = 0; i < args.length; i++) {
                if (args[i] != null) {
                    params += JSON.toJSONString(args[i]);
                    parameterTypes[i] = args[i].getClass();
                } else {
                    parameterTypes[i] = null;
                }
            }
            if (StringUtils.isNotEmpty(params)) {
                //加密 以防出现key过长以及字符转义获取不到的情况
                params = DigestUtils.md5Hex(params);
            }
            Method method = pjp.getSignature().getDeclaringType().getMethod(methodName, parameterTypes);
            //获取Cache注解
            Cache annotation = method.getAnnotation(Cache.class);
            //缓存过期时间
            long expire = annotation.expire();
            //缓存名称
            String name = annotation.name();
            //先从redis获取
            String redisKey = name + "::" + className + "::" + methodName + "::" + params;
            String redisValue = (String) redisUtil.get(redisKey);
            if (StringUtils.isNotEmpty(redisValue)) {
                log.info("走了缓存~~~,{},{}", className, methodName);
                return JSON.parseObject(redisValue, JsonResult.class);
            }

            Object proceed = pjp.proceed();
            redisUtil.set(redisKey, JSON.toJSONString(proceed));
            redisUtil.setExpire(redisKey, expire);
            log.info("存入缓存~~~ {},{}", className, methodName);
            return proceed;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return ResultTool.fail();
    }
}
