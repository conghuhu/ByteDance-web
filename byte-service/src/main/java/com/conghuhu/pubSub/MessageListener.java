package com.conghuhu.pubSub;

import com.alibaba.fastjson.JSONObject;
import com.conghuhu.dos.NotifyMessage;
import com.conghuhu.service.WebSocketService;
import com.conghuhu.utils.LocalIPUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPubSub;

/**
 * @author conghuhu
 * @create 2022-03-13 18:55
 */
@Slf4j
@Service
public class MessageListener extends JedisPubSub {

    private final LocalIPUtil localIPUtil;

    private final WebSocketService webSocketService;

    public MessageListener(LocalIPUtil localIPUtil, WebSocketService webSocketService) {
        this.localIPUtil = localIPUtil;
        this.webSocketService = webSocketService;
    }

    @Override
    public void onMessage(String channel, String message) {
        log.info("接收到的消息为:频道-{},消息-{}", channel, message);
        // 当接收到的message为quit时，取消订阅（被动方式）
        if ("quit".equalsIgnoreCase(message)) {
            this.unsubscribe(channel);
        }
        NotifyMessage notifyMessage = JSONObject.parseObject(message, NotifyMessage.class);

        if (!localIPUtil.getUrl().equals(notifyMessage.getPublisher())) {
            webSocketService.getMessageAndConvert(notifyMessage.getProductId(), notifyMessage.getMessage());
        }
    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {
        super.onPMessage(pattern, channel, message);
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        super.onSubscribe(channel, subscribedChannels);
    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        super.onUnsubscribe(channel, subscribedChannels);
    }

    @Override
    public void onPUnsubscribe(String pattern, int subscribedChannels) {
        super.onPUnsubscribe(pattern, subscribedChannels);
    }

    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {
        super.onPSubscribe(pattern, subscribedChannels);
    }

    @Override
    public void onPong(String pattern) {
        super.onPong(pattern);
    }


    @Override
    public void ping() {
        super.ping();
    }

    @Override
    public void ping(String argument) {
        super.ping(argument);
    }

    @Override
    public int getSubscribedChannels() {
        return super.getSubscribedChannels();
    }
}
