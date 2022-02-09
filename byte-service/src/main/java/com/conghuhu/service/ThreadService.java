package com.conghuhu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.conghuhu.entity.CardTag;
import com.conghuhu.mapper.CardTagMapper;
import com.conghuhu.vo.WebsocketDetail;
import com.conghuhu.vo.WebsocketVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author conghuhu
 * @create 2022-01-30 9:26
 */
@Service
public class ThreadService {

    private final WebSocketService webSocketService;

    public ThreadService(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @Async
    public void notifyAllMemberByProductId(Long productId, String event, String typeName,
                                           List<String> tags, WebsocketDetail websocketDetail) {
        WebsocketVo websocketVo = new WebsocketVo();
        websocketVo.setEvent(event);
        websocketVo.setTypeName(typeName);
        websocketVo.setTags(tags);
        websocketVo.setDetail(websocketDetail);
        webSocketService.sendMessageToAll(String.valueOf(productId), websocketVo);
    }

}
