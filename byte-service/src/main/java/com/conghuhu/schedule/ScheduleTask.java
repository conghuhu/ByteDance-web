package com.conghuhu.schedule;

import com.conghuhu.entity.WebSocketClient;
import com.conghuhu.service.WebSocketService;
import com.conghuhu.utils.JedisUtil;
import com.google.common.util.concurrent.AtomicDouble;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Map;
import java.util.Set;

/**
 * @author conghuhu
 * @create 2022-03-14 18:11
 */
@Slf4j
@Component
public class ScheduleTask {

    public static final String MONITOR = "monitor";

    public static final String ONLINE_USER_COUNT = "onlineUserCount";

    public static final String ONLINE_PRODUCT_COUNT = "onlineProductCount";

    @Scheduled(cron = "0 1/5 * * * ? ")
    public void clearInvalidTair() {
        Set<String> setItem = JedisUtil.getSetItem(WebSocketService.ONLINE_CHANNELS);
        if (setItem == null || setItem.size() == 0) {
            return;
        }
        setItem.forEach(productId -> {
            Set<String> allUserKeys = JedisUtil.getAllFields(productId);
            Map<String, WebSocketClient> userClientMap = WebSocketService.productChannels.get(productId);
            if (userClientMap != null) {
                for (Map.Entry<String, WebSocketClient> client : userClientMap.entrySet()) {
                    if (!allUserKeys.contains(client.getKey())) {
                        userClientMap.remove(client.getKey());
                    }
                }
            }
        });
    }

    @Scheduled(cron = "15/2 * * * * ? ")
    public void uploadAllActiveUser() {
        Set<String> setItem = JedisUtil.getSetItem(WebSocketService.ONLINE_CHANNELS);
        AtomicDouble currentOnlineCount = new AtomicDouble();
        setItem.forEach(productId -> {
            Set<String> allProductChannels = JedisUtil.getAllFields(productId);
            currentOnlineCount.getAndAdd(allProductChannels.size());
        });
        long curTimeStamp = getCurTimeStamp();
        JedisUtil.addItemToTs(MONITOR, ONLINE_USER_COUNT, String.valueOf(curTimeStamp),
                currentOnlineCount.doubleValue(), 1000 * 60 * 5, ONLINE_USER_COUNT);
    }

    @Scheduled(cron = "15/1 * * * * ? ")
    public void uploadAllActiveProductCount() {
        Set<String> setItem = JedisUtil.getSetItem(WebSocketService.ONLINE_CHANNELS);
        long curTimeStamp = getCurTimeStamp();
        JedisUtil.addItemToTs(MONITOR, ONLINE_PRODUCT_COUNT, String.valueOf(curTimeStamp),
                Double.valueOf(setItem.size()), 1000 * 60 * 5, ONLINE_PRODUCT_COUNT);
    }


    public static long getCurTimeStamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp.toInstant().toEpochMilli();
    }

}
