package com.conghuhu.schedule;

import com.conghuhu.entity.WebSocketClient;
import com.conghuhu.service.WebSocketService;
import com.conghuhu.utils.JedisUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * @author conghuhu
 * @create 2022-03-14 18:11
 */
@Component
public class ScheduleTask {

    @Scheduled(cron = "10/1 1/10 * * * ? ")
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

}
