package com.conghuhu.service.impl;

import com.conghuhu.entity.Card;
import com.conghuhu.mapper.CardMapper;
import com.conghuhu.params.CardDateParam;
import com.conghuhu.params.EditParam;
import com.conghuhu.result.JsonResult;
import com.conghuhu.result.ResultTool;
import com.conghuhu.service.CardService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author conghuhu
 * @since 2022-01-26
 */
@Service
public class CardServiceImpl extends ServiceImpl<CardMapper, Card> implements CardService {

    private final CardMapper cardMapper;

    public CardServiceImpl(CardMapper cardMapper) {
        this.cardMapper = cardMapper;
    }

    @Override
    public String addCard(Card card) {
        if (cardMapper.insert(card) > 0) {
            return "success";
        } else {
            return "fail";
        }
    }

    @Override
    public Card getByName(String cardname) {
        return cardMapper.getByCardName(cardname);
    }

    @Override
    public JsonResult editDescByCardId(EditParam editParam, Long cardId) {
        Card card = new Card();
        card.setCardId(cardId);
        card.setDescription(editParam.getEditContent());
        int res = cardMapper.updateById(card);
        if (res > 0) {
            return ResultTool.success();
        } else {
            return ResultTool.fail();
        }
    }

    @Override
    public JsonResult editCardNameByCardId(EditParam editParam, Long cardId) {
        Card card = new Card();
        card.setCardId(cardId);
        card.setCardname(editParam.getEditContent());
        int res = cardMapper.updateById(card);
        if (res > 0) {
            return ResultTool.success();
        } else {
            return ResultTool.fail();
        }
    }

    @Override
    public JsonResult setCardDeadline(CardDateParam cardDateParam, Long cardId) {
        Card card = new Card();
        card.setCardId(cardId);
        LocalDateTime beginTime = cardDateParam.getBeginTime();
        LocalDateTime deadline = cardDateParam.getDeadline();
        Long dueReminder = cardDateParam.getDueReminder();
        LocalDateTime now = LocalDateTime.now();
        if (deadline.isBefore(now)) {
            card.setExpired(true);
        }
        card.setBegintime(beginTime);
        card.setDeadline(deadline);

        int res = cardMapper.updateById(card);
        if (res > 0) {
            return ResultTool.success();
        } else {
            return ResultTool.fail();
        }
    }
}
