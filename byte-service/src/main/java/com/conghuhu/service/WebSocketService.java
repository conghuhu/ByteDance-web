package com.conghuhu.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.conghuhu.dos.NotifyMessage;
import com.conghuhu.entity.WebSocketClient;
import com.conghuhu.pubSub.PubClient;
import com.conghuhu.utils.JedisUtil;
import com.conghuhu.utils.LocalIPUtil;
import com.conghuhu.utils.SpringBeanFactoryUtils;
import com.conghuhu.vo.WebsocketVo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


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

    public static Map<String, Map<String, WebSocketClient>> productChannels = new ConcurrentHashMap<>();

    public static final String ONLINE_CHANNELS = "onlineChannels";

    @Autowired
    private PubClient pubClient;

    @Autowired
    private LocalIPUtil localIPUtil;

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
    public void onOpen(@PathParam("productId") String productId, @PathParam("userId") String userId, Session session) {
        String sessionId = session.getId();
        log.info("现在来连接的客户id：" + sessionId + "项目Id：" + productId);
        String wsUserKey = productId + "-" + userId + "-" + sessionId;

        this.session = session;
        this.productId = productId;
        this.userId = userId;
        this.wsUserKey = wsUserKey;

        WebSocketClient client = new WebSocketClient();
        client.setSession(session);
        client.setUri(session.getRequestURI().toString());
        client.setIdentification(wsUserKey);

        Map<String, WebSocketClient> userWebsocketMap;
        if (!productChannels.containsKey(productId)) {
            JedisUtil.addItemToSet(ONLINE_CHANNELS, productId);
            // 频道数+1
            onlineProductCount.getAndIncrement();
            userWebsocketMap = new ConcurrentHashMap<>(50);
            userWebsocketMap.put(wsUserKey, client);
            productChannels.put(productId, userWebsocketMap);
        } else {
            userWebsocketMap = productChannels.get(productId);
            userWebsocketMap.put(wsUserKey, client);
        }

        log.info("----------------------------------------------------------------------------");

        try {
            while (true) {
                // 尝试获取锁
                if (JedisUtil.tryGetDistributedLock("userLoginChannel", wsUserKey, 6)) {
                    if (pubClient == null || localIPUtil == null) {
                        pubClient = SpringBeanFactoryUtils.getBean(PubClient.class);
                        localIPUtil = SpringBeanFactoryUtils.getBean(LocalIPUtil.class);
                    }

                    JedisUtil.setHash(productId, wsUserKey, localIPUtil.getUrl());

                    WebsocketVo websocketVo = new WebsocketVo();
                    websocketVo.setIdentification(wsUserKey);
                    websocketVo.setEvent("handShake");
                    log.info("用户连接:" + userId + "访问项目：" + productId + ",当前频道在线人数为:" + userWebsocketMap.size());
                    sendMessage(JSON.toJSONString(websocketVo));
                    break;
                }
            }
        } catch (IOException e) {
            log.error("用户:" + userId + ",网络异常!!!!!!");
        } finally {
            // 不管成功还是失败，要释放锁
            JedisUtil.releaseDistributedLock("userLoginChannel", wsUserKey);
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        log.info("----------------------------------------------------------------------------");
        if (JedisUtil.hasKey(productId)) {
            // 某一项目频道内，用户和对应连接的map
            Map<String, WebSocketClient> userWebsocketMap = productChannels.get(productId);
            if (JedisUtil.getHashValue(productId, wsUserKey) != null) {
                JedisUtil.deleteField(productId, wsUserKey);
                userWebsocketMap.remove(wsUserKey);
            }
            int size = JedisUtil.getAllFields(productId).size();
            if (size == 0) {
                JedisUtil.removeItemInSet(ONLINE_CHANNELS, productId);
                JedisUtil.deleteKey(productId);
            }
            log.info("productId - userId - sessionId: " + productId + "-" + wsUserKey + "用户退出" + "当前频道在线人数为:" + size);
        } else {
            log.info("productId - userId - sessionId: " + productId + "-" + wsUserKey + "用户退出出问题了");
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        //可以群发消息
        //消息保存到数据库、redis
        if (StringUtils.isNotBlank(message)) {
            WebsocketVo websocketVo = JSONObject.parseObject(message, WebsocketVo.class);
            if ("heart".equals(websocketVo.getEvent())) {
                String identification = websocketVo.getIdentification();
                String[] split = identification.split("-");
                String activeProductId = split[0];
                JedisUtil.setExpire(activeProductId, identification, 600);
            }
        }
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("productId - userId - sessionId 错误：" + wsUserKey + ",原因:" + error.getMessage());
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
            NotifyMessage notifyMessage = new NotifyMessage();
            notifyMessage.setProductId(productId);
            notifyMessage.setPublisher(localIPUtil.getUrl());
            notifyMessage.setMessage(message);
            pubClient.pub(JedisUtil.CHANNEL, JSON.toJSONString(notifyMessage));
            scanMap(productId, message);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 获取订阅消息后扫描本地map
     *
     * @param productId
     * @param message
     */
    public void getMessageAndConvert(String productId, WebsocketVo message) {
        try {
            scanMap(productId, message);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 扫描map中的productId
     *
     * @param productId
     * @param message
     */
    public void scanMap(String productId, WebsocketVo message) {
        if (!productChannels.containsKey(productId)) {
            return;
        }
        Map<String, WebSocketClient> userClientMap = productChannels.get(productId);
        Set<String> clientSet = JedisUtil.getAllFields(productId);
        clientSet.forEach(userKeyValid -> {
            if (userClientMap.containsKey(userKeyValid)) {
                WebSocketClient client = userClientMap.get(userKeyValid);
                message.setIdentification(client.getIdentification());
                client.getSession().getAsyncRemote().sendText(JSON.toJSONString(message));
            }
        });
    }

}
