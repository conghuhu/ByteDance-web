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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private final TagMapper tagMapper;

    private final CardTagMapper cardTagMapper;

    private final CardMapper cardMapper;

    public TagServiceImpl(TagMapper tagMapper, CardTagMapper cardTagMapper, CardMapper cardMapper) {
        this.tagMapper = tagMapper;
        this.cardTagMapper = cardTagMapper;
        this.cardMapper = cardMapper;
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
            return ResultTool.success();
        } else {
            return ResultTool.fail();
        }
    }

    @Override
    public JsonResult updateTag(TagParam tagParam) {
        Tag tag = new Tag();
        tag.setId(tagParam.getId());
        tag.setTagName(tagParam.getTagName());
        tag.setColor(tagParam.getColor());
        tag.setProductId(tagParam.getProductId());
        int res = tagMapper.updateById(tag);
        if (res > 0) {
            return ResultTool.success();
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

    @Override
    public JsonResult removeTagById(Long id) {
        int res = tagMapper.deleteById(id);
        if (res > 0) {
            return ResultTool.success();
        } else {
            return ResultTool.fail();
        }
    }

    @Override
    public JsonResult setTagByCardId(Long tagId, Long cardId) {
        Integer tagCount = tagMapper.selectCount(new LambdaQueryWrapper<Tag>().eq(Tag::getId, tagId));
        Integer cardCount = cardMapper.selectCount(new LambdaQueryWrapper<Card>().eq(Card::getCardId, cardId));
        if (tagCount <= 0) {
            return ResultTool.fail(ResultCode.TAG_ID_NOT_CONSIST);
        } else if (cardCount <= 0) {
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
        if (res > 0) {
            return ResultTool.success();
        } else {
            return ResultTool.fail();
        }
    }
}
