package com.conghuhu.service.impl;

import com.conghuhu.entity.Card;
import com.conghuhu.mapper.CardMapper;
import com.conghuhu.service.CardService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author conghuhu
 * @since 2022-01-26
 */
@Service
public class CardServiceImpl extends ServiceImpl<CardMapper, Card> implements CardService {
    @Autowired
    private CardMapper cardMapper;

    @Override
    public String addCard(Card card) {
        if(cardMapper.insert(card)>0){
            return "success";
        }
        else {
            return "fail";
        }
    }

    @Override
    public Card getByName(String cardname) {
        return cardMapper.getByCardName(cardname);
    }
}
