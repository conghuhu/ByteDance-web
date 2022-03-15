package com.conghuhu.pubSub;

import com.conghuhu.utils.JedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author conghuhu
 * @create 2022-03-13 18:49
 */
@Slf4j
@Service
public class SubClient {

    private final MessageListener messageListener;

    public SubClient(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    @Async
    public void subscribeChannel(String channel) {
        log.info("成功订阅 {} 频道", channel);
        JedisUtil.subscribe(messageListener, channel);
    }

    public void unsubscribe(MessageListener listener, String channel) {
        listener.unsubscribe(channel);
    }
}
