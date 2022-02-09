package com.conghuhu.entity;

import lombok.Data;

import javax.websocket.Session;

/**
 * @author conghuhu
 * @create 2022-02-06 9:52
 */
@Data
public class WebSocketClient {

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 连接的uri
     */
    private String uri;

    private String identification;
}
