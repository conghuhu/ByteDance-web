package com.conghuhu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.conghuhu.entity.Card;
import com.conghuhu.entity.CardTag;
import com.conghuhu.mapper.CardMapper;
import com.conghuhu.mapper.CardTagMapper;
import com.conghuhu.mapper.CardUserMapper;
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

    private final CardMapper cardMapper;
    private final CardUserMapper cardUserMapper;
    private final WebSocketService webSocketService;

    public ThreadService(WebSocketService webSocketService, CardUserMapper cardUserMapper, CardMapper cardMapper) {
        this.webSocketService = webSocketService;
        this.cardUserMapper = cardUserMapper;
        this.cardMapper = cardMapper;
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

    @Async
    public void deleteCardUserByProductId(Long productId, Long userId) {
        cardUserMapper.deleteCardUserByProductId(productId, userId);
    }

    @Async
    public void deleteCardByListId(Long listId) {
        cardMapper.delete(new LambdaQueryWrapper<Card>()
                .eq(Card::getListId, listId));
    }
}
