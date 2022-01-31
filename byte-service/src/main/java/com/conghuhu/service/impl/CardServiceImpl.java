package com.conghuhu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.conghuhu.entity.Card;
import com.conghuhu.entity.CardTag;
import com.conghuhu.entity.CardUser;
import com.conghuhu.entity.Tag;
import com.conghuhu.mapper.CardMapper;

import com.conghuhu.mapper.CardTagMapper;
import com.conghuhu.mapper.CardUserMapper;
import com.conghuhu.params.*;
import com.conghuhu.result.JsonResult;
import com.conghuhu.result.ResultCode;

import com.conghuhu.result.ResultTool;
import com.conghuhu.service.CardService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.conghuhu.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.LocalDateTime;

import java.util.List;


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

    private final CardTagMapper cardTagMapper;
    private final CardUserMapper cardUserMapper;
    private final CardMapper cardMapper;

    public CardServiceImpl(CardTagMapper cardTagMapper, CardMapper cardMapper, CardUserMapper cardUserMapper) {
        this.cardTagMapper = cardTagMapper;
        this.cardMapper = cardMapper;
        this.cardUserMapper = cardUserMapper;
    }

    @Override
    public Card getByName(String cardname) {
        return cardMapper.getByCardName(cardname);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JsonResult removeCardById(Long cardId) {
        int res = cardMapper.deleteById(cardId);
        cardTagMapper.delete(new LambdaQueryWrapper<CardTag>().eq(CardTag::getCardId, cardId));
        cardUserMapper.delete(new LambdaQueryWrapper<CardUser>().eq(CardUser::getCardId, cardId));
        if (res > 0) {
            return ResultTool.success();
        } else {
            // 手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultTool.fail();
        }
    }

    @Override
    public JsonResult createTag(CardParam cardParam) {
        Card card = new Card();

        BeanUtils.copyProperties(cardParam, card);

        String cardname = cardParam.getCardname();

        card.setTag(false);
        card.setExpired(false);
        card.setClosed(false);
        card.setExecutor(false);

        Card selectOne = cardMapper.selectOne(new LambdaQueryWrapper<Card>()
                .eq(Card::getCardname, cardname));

        if (selectOne != null) {
            return ResultTool.fail(ResultCode.CARD_CONSIST);
        }
        int res = cardMapper.insert(card);
        if (res > 0) {
            return ResultTool.success(card);
        } else {
            return ResultTool.fail();
        }
    }

    @Override
    public JsonResult updateTag(Card cardParam) {
        Card card = new Card();
        BeanUtils.copyProperties(cardParam, card);
        int res = cardMapper.updateById(card);
        if (res > 0) {
            return ResultTool.success();
        } else {
            return ResultTool.fail();
        }
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

    @Override
    public JsonResult moveCard(CardMoveParam cardMoveParam) {
        Long cardId = cardMoveParam.getCardId();
        Float pos = cardMoveParam.getPos();
        Long listId = cardMoveParam.getListId();
        Card card = new Card();
        card.setCardId(cardId);
        card.setPos(pos);
        if (listId != null) {
            // 跨列移动,为null就是同列移动
            card.setListId(listId);
        }
        int res = cardMapper.updateById(card);
        if (res > 0) {
            return ResultTool.success();
        } else {
            return ResultTool.fail();
        }
    }

    @Override
    public List<Tag> getTagsByCardId(Long cardId) {
        return cardMapper.getTagsByCardId(cardId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JsonResult setExecutor(ExecutorParam executorParam, Long cardId) {
        Long userId = executorParam.getUserId();
        CardUser cardUser = new CardUser();
        cardUser.setUserId(userId);
        cardUser.setCardId(cardId);
        Integer selectCount = cardUserMapper.selectCount(new LambdaQueryWrapper<CardUser>()
                .eq(CardUser::getCardId, cardId).eq(CardUser::getUserId, userId));
        if (selectCount > 0) {
            List<UserVo> executors = getExecutorsByCardId(cardId);
            return ResultTool.success(executors);
        }
        int res = cardUserMapper.insert(cardUser);
        Card card = new Card();
        card.setCardId(cardId);
        card.setExecutor(true);
        int updateCard = cardMapper.updateById(card);
        if (res > 0 && updateCard > 0) {
            List<UserVo> executors = getExecutorsByCardId(cardId);
            return ResultTool.success(executors);
        } else {
            // 手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultTool.fail();
        }
    }

    @Override
    public List<UserVo> getExecutorsByCardId(Long cardId) {
        List<UserVo> executors = cardMapper.getExecutorsByCardId(cardId);
        return executors;
    }
}
