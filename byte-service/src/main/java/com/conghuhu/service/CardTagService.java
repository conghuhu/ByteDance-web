package com.conghuhu.service;

import com.conghuhu.entity.CardTag;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author conghuhu
 * @since 2022-01-27
 */
public interface CardTagService extends IService<CardTag> {
    /**
     * 通过cardId删除card-tag关联
     *
     * @param cardId
     * @return
     */
    Boolean removeCardTagByCardId(Long cardId);
}
