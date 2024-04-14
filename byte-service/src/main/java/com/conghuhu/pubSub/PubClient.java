package com.conghuhu.pubSub;

import com.conghuhu.utils.JedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author conghuhu
 * @create 2022-03-13 15:48
 */
@Slf4j
@Service
public class PubClient {

    @Async
    public void pub(String channel, String message) {
        log.info("  >>> 发布(PUBLISH) > Channel:" + channel + " > 发送出的Message:" + message);
        JedisUtil.pub(channel, message);
    }

    @Async
    public void close(String channel) {
        log.info("  >>> 发布(PUBLISH)结束 > Channel:" + channel + " > Message:quit");
        JedisUtil.closePublishChannel(channel);
    }

}
