package com.conghuhu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.conghuhu.entity.CardTag;
import com.conghuhu.mapper.CardTagMapper;
import com.conghuhu.service.CardTagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author conghuhu
 * @since 2022-01-27
 */
@Service
public class CardTagServiceImpl extends ServiceImpl<CardTagMapper, CardTag> implements CardTagService {

    private final CardTagMapper cardTagMapper;

    public CardTagServiceImpl(CardTagMapper cardTagMapper) {
        this.cardTagMapper = cardTagMapper;
    }

    @Override
    public Boolean removeCardTagByCardId(Long cardId) {
        int res = cardTagMapper.delete(new LambdaQueryWrapper<CardTag>().eq(CardTag::getCardId, cardId));
        return res > 0;
    }
}
