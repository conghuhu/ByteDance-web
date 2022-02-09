package com.conghuhu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.conghuhu.entity.Card;
import com.conghuhu.entity.CardTag;
import com.conghuhu.entity.Tag;
import com.conghuhu.mapper.CardMapper;
import com.conghuhu.mapper.CardTagMapper;
import com.conghuhu.mapper.TagMapper;
import com.conghuhu.params.TagParam;
import com.conghuhu.result.JsonResult;
import com.conghuhu.result.ResultCode;
import com.conghuhu.result.ResultTool;
import com.conghuhu.service.TagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.conghuhu.service.ThreadService;
import com.conghuhu.vo.TagVo;
import com.conghuhu.vo.WebsocketDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author conghuhu
 * @since 2022-01-27
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    private final ThreadService threadService;

    private final TagMapper tagMapper;

    private final CardTagMapper cardTagMapper;

    private final CardMapper cardMapper;

    public TagServiceImpl(TagMapper tagMapper, CardTagMapper cardTagMapper, CardMapper cardMapper, ThreadService threadService) {
        this.tagMapper = tagMapper;
        this.cardTagMapper = cardTagMapper;
        this.cardMapper = cardMapper;
        this.threadService = threadService;
    }

    @Override
    public JsonResult createTag(TagParam tagParam) {
        Tag tag = new Tag();
        String color = tagParam.getColor();
        String tagName = tagParam.getTagName();
        Long productId = tagParam.getProductId();
        tag.setColor(color);
        tag.setTagName(tagName);
        tag.setProductId(productId);
        Tag selectOne = tagMapper.selectOne(new LambdaQueryWrapper<Tag>()
                .eq(Tag::getColor, color)
                .eq(Tag::getTagName, tagName)
                .eq(Tag::getProductId, productId));
        if (selectOne != null) {
            return ResultTool.fail(ResultCode.TAG_CONSIST);
        }
        int res = tagMapper.insert(tag);
        if (res > 0) {
            return ResultTool.success(tag);
        } else {
            return ResultTool.fail();
        }
    }

    @Override
    public JsonResult updateTag(Tag tagParam) {
        Tag tag = new Tag();
        tag.setId(tagParam.getId());
        tag.setTagName(tagParam.getTagName());
        tag.setColor(tagParam.getColor());
        tag.setProductId(tagParam.getProductId());
        int res = tagMapper.updateById(tag);
        if (res > 0) {
            return ResultTool.success(tag);
        } else {
            return ResultTool.fail();
        }
    }

    @Override
    public JsonResult getTagsByProductId(Long productId) {
        LambdaUpdateWrapper<Tag> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Tag::getProductId, productId);
        List<Tag> tagList = tagMapper.selectList(updateWrapper);
        if (tagList != null) {
            return ResultTool.success(tagList);
        } else {
            return ResultTool.fail();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JsonResult removeTagById(Long id) {
        if (id == null) {
            return ResultTool.fail(ResultCode.PARAM_IS_BLANK);
        }
        cardTagMapper.delete(new LambdaQueryWrapper<CardTag>()
                .eq(CardTag::getTagId, id));

        int res = tagMapper.deleteById(id);
        if (res > 0) {
            return ResultTool.success();
        } else {
            // 手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultTool.fail();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JsonResult setTagByCardId(Long tagId, Long cardId) {
        Tag tag = tagMapper.selectById(tagId);
        Card selectCard = cardMapper.selectById(cardId);
        if (tag == null) {
            return ResultTool.fail(ResultCode.TAG_ID_NOT_CONSIST);
        } else if (selectCard == null) {
            return ResultTool.fail(ResultCode.CARD_ID_NOT_CONSIST);
        }
        CardTag cardTag = new CardTag();
        cardTag.setTagId(tagId);
        cardTag.setCardId(cardId);
        Integer cardTagCount = cardTagMapper.selectCount(new LambdaQueryWrapper<CardTag>()
                .eq(CardTag::getTagId, tagId)
                .eq(CardTag::getCardId, cardId));
        if (cardTagCount > 0) {
            return ResultTool.success();
        }
        int res = cardTagMapper.insert(cardTag);
        Card card = new Card();
        card.setCardId(cardId);
        card.setTag(true);
        int cardRes = cardMapper.updateById(card);
        if (res > 0 && cardRes > 0) {
            TagVo tagVo = new TagVo();
            tagVo.setTagId(tagId);
            tagVo.setId(cardId);
            tagVo.setName(tag.getTagName());
            tagVo.setBackground(tag.getColor());
            tagVo.setListAfterId(selectCard.getListId());
            threadService.notifyAllMemberByProductId(tag.getProductId(),
                    "updateModels", "Card",
                    new ArrayList<>(Arrays.asList("updates", "tags", "add"))
                    , tagVo);
            return ResultTool.success();
        } else {
            // 手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultTool.fail();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JsonResult removeTagByCardId(Long tagId, Long cardId) {
        Card selectCard = cardMapper.selectById(cardId);
        if (selectCard == null) {
            return ResultTool.fail(ResultCode.CARD_ID_NOT_CONSIST);
        }

        LambdaQueryWrapper<CardTag> queryWrapper = new LambdaQueryWrapper<CardTag>()
                .eq(CardTag::getTagId, tagId)
                .eq(CardTag::getCardId, cardId);
        Integer count = cardTagMapper.selectCount(queryWrapper);
        if (count <= 0) {
            return ResultTool.fail(ResultCode.PARAMS_ERROR);
        }
        int delete = cardTagMapper.delete(queryWrapper);
        if (delete > 0) {
            TagVo tagVo = new TagVo();
            tagVo.setTagId(tagId);
            tagVo.setId(cardId);
            tagVo.setListAfterId(selectCard.getListId());
            threadService.notifyAllMemberByProductId(selectCard.getProductId(),
                    "updateModels", "Card",
                    new ArrayList<>(Arrays.asList("updates", "tags", "remove"))
                    , tagVo);
            return ResultTool.success();
        } else {
            // 手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultTool.fail();
        }
    }
}
