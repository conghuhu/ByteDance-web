package com.conghuhu.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.conghuhu.entity.WebSocketClient;
import com.conghuhu.vo.WebsocketVo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author conghuhu
 * @create 2022-02-06 9:56
 */
@Data
@Slf4j
@ServerEndpoint(value = "/websocket/{productId}/{userId}")
@Service
public class WebSocketService {

    /**
     * 当前在线的product频道数
     */
    private static AtomicInteger onlineProductCount = new AtomicInteger(0);

    private static Map<String, ConcurrentHashMap<String, WebSocketClient>> productChannels = new ConcurrentHashMap<>();


    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    /**
     * 接收productId
     */
    private String productId = "";

    private String userId = "";

    private String wsUserKey = "";

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("productId") String productId, @PathParam("userId") String userId) {
        String sessionId = session.getId();
        log.info("现在来连接的客户id：" + sessionId + "项目Id：" + productId);
        String wsUserKey = userId + "-" + sessionId;

        this.session = session;
        this.productId = productId;
        this.userId = userId;
        this.wsUserKey = wsUserKey;

        WebSocketClient client = new WebSocketClient();
        client.setSession(session);
        client.setUri(session.getRequestURI().toString());
        client.setIdentification(productId + "-" + wsUserKey);

        if (!productChannels.containsKey(productId)) {
            // 频道数+1
            onlineProductCount.getAndIncrement();

            ConcurrentHashMap<String, WebSocketClient> userWebsocketMap = new ConcurrentHashMap<>(50);
            userWebsocketMap.put(wsUserKey, client);
            productChannels.put(productId, userWebsocketMap);
        }

        ConcurrentHashMap<String, WebSocketClient> userWebsocketMap = productChannels.get(productId);
        userWebsocketMap.put(wsUserKey, client);

        log.info("----------------------------------------------------------------------------");
        log.info("用户连接:" + userId + "访问项目：" + productId + ",当前在线人数为:" + userWebsocketMap.size());
        log.info("连接标识为：" + productId + "-" + wsUserKey);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("result", true);
            jsonObject.put("identification", productId + "-" + wsUserKey);
            sendMessage(jsonObject.toJSONString());
        } catch (IOException e) {
            log.error("用户:" + userId + ",网络异常!!!!!!");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        log.info("----------------------------------------------------------------------------");
        if (productChannels.containsKey(productId)) {
            // 某一项目频道内，用户和对应连接的map
            ConcurrentHashMap<String, WebSocketClient> userWebsocketMap = productChannels.get(productId);
            if (userWebsocketMap.containsKey(wsUserKey)) {
                userWebsocketMap.remove(wsUserKey);
            }
            if (userWebsocketMap.size() == 0) {
                productChannels.remove(productId);
            }
            log.info("productId - userId - sessionId" + productId + "-" + wsUserKey + "用户退出" + "当前频道在线人数为:" + userWebsocketMap.size());
        } else {
            log.info("productId - userId - sessionId" + productId + "-" + wsUserKey + "用户退出出问题了");
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("来自客户端消息：" + message + "客户端的标识是：" + productId + "-" + wsUserKey);
        //可以群发消息
        //消息保存到数据库、redis
        if (StringUtils.isNotBlank(message)) {

        }
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("productId - userId - sessionId 错误：" + productId + "-" + wsUserKey + ",原因:" + error.getMessage());
        error.printStackTrace();
    }

    /**
     * 连接服务器成功后主动推送
     */
    public void sendMessage(String message) throws IOException {
        synchronized (session) {
            this.session.getBasicRemote().sendText(message);
        }
    }

    /**
     * 向某个product频道发送消息
     *
     * @param productId
     * @param message
     */
    public void sendMessageToAll(String productId, WebsocketVo message) {
        try {
            for (Map.Entry<String, WebSocketClient> webSocketClientEntry : productChannels.get(productId).entrySet()) {
                WebSocketClient client = webSocketClientEntry.getValue();
                message.setIdentification(client.getIdentification());
                client.getSession().getAsyncRemote().sendText(JSON.toJSONString(message));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
