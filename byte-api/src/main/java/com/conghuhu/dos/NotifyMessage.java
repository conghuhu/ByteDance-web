package com.conghuhu.dos;

import com.conghuhu.vo.WebsocketVo;
import lombok.Data;

/**
 * @author conghuhu
 * @create 2022-03-14 13:45
 */
@Data
public class NotifyMessage {

    /**
     * 发布者地址
     */
    private String publisher;

    /**
     * 频道信息
     */
    private String productId;

    /**
     * 消息体
     */
    private WebsocketVo message;

}
