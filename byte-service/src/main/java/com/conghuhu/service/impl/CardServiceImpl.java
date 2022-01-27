package com.conghuhu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.conghuhu.entity.Card;
import com.conghuhu.entity.Tag;
import com.conghuhu.mapper.CardMapper;
import com.conghuhu.params.CardParam;
import com.conghuhu.result.JsonResult;
import com.conghuhu.result.ResultCode;
import com.conghuhu.result.ResultTool;
import com.conghuhu.service.CardService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

    @Override
    public JsonResult removeCardById(Long cardId) {
        int res = cardMapper.deleteById(cardId);
        if (res > 0) {
            return ResultTool.success();
        } else {
            return ResultTool.fail();
        }
    }

    @Override
    public JsonResult createTag(CardParam cardParam) {
        Card card = new Card();

        String cardname=cardParam.getCardname();

        card.setCardname(cardParam.getCardname());
        card.setDescription(cardParam.getDescription());
        card.setListId(cardParam.getListId());
        card.setProductId(cardParam.getProductId());
        card.setClosed(false);
        card.setPos(cardParam.getPos());
        card.setDeadline(LocalDateTime.now());
        card.setTag(cardParam.getTag());
        card.setExecutor(cardParam.getExecutor());
        card.setBegintime(LocalDateTime.now());
        card.setExpire(false);

        Card selectOne = cardMapper.selectOne(new LambdaQueryWrapper<Card>()
                .eq(Card::getCardname, cardname));

        if (selectOne != null) {
            return ResultTool.fail(ResultCode.CARD_CONSIST);
        }
        int res = cardMapper.insert(card);
        if (res > 0) {
            return ResultTool.success();
        } else {
            return ResultTool.fail();
        }
    }

    @Override
    public JsonResult updateTag(CardParam cardParam) {
        Card card = new Card();
        card.setCardId(cardParam.getCardId());
        card.setCardname(cardParam.getCardname());
        card.setDescription(cardParam.getDescription());
        card.setListId(cardParam.getListId());
        card.setProductId(cardParam.getProductId());
        card.setClosed(cardParam.getClosed());
        card.setPos(cardParam.getPos());
        card.setDeadline(LocalDateTime.now());
        card.setTag(cardParam.getTag());
        card.setExecutor(cardParam.getExecutor());
        card.setBegintime(LocalDateTime.now());
        card.setExpire(cardParam.getExpire());
        int res = cardMapper.updateById(card);
        if (res > 0) {
            return ResultTool.success();
        } else {
            return ResultTool.fail();
        }
    }

    @Override
    public JsonResult getCardsByListId(Long listId) {
        LambdaUpdateWrapper<Card> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Card::getListId, listId);
        List<Card> cardList = cardMapper.selectList(updateWrapper);
        if (cardList != null) {
            return ResultTool.success(cardList);
        } else {
            return ResultTool.fail();
        }
    }
}
