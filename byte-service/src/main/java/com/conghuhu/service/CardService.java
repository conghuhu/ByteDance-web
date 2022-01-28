package com.conghuhu.service;

import com.conghuhu.entity.Card;
import com.baomidou.mybatisplus.extension.service.IService;
import com.conghuhu.params.CardParam;
import com.conghuhu.params.CardDateParam;
import com.conghuhu.params.EditParam;
import com.conghuhu.result.JsonResult;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author conghuhu
 * @since 2022-01-26
 */
public interface CardService extends IService<Card> {

    String addCard(Card card);

    Card getByName(String cardname);


    JsonResult removeCardById(Long cardId);

    JsonResult createTag(CardParam cardParam);

    JsonResult updateTag(CardParam cardParam);

    JsonResult getCardsByListId(Long listId);

    /**
     * 根据cardId编辑内容
     *
     * @param editParam
     * @param cardId
     * @return
     */
    JsonResult editDescByCardId(EditParam editParam, Long cardId);

    /**
     * 根据cardId编辑名称
     *
     * @param editParam
     * @param cardId
     * @return
     */
    JsonResult editCardNameByCardId(EditParam editParam, Long cardId);

    /**
     * 设置卡片的时间
     *
     * @param cardDateParam
     * @param cardId
     * @return
     */
    JsonResult setCardDeadline(CardDateParam cardDateParam, Long cardId);

}
