package com.conghuhu.service;

import com.conghuhu.entity.Card;
import com.baomidou.mybatisplus.extension.service.IService;
import com.conghuhu.entity.Tag;
import com.conghuhu.params.*;
import com.conghuhu.result.JsonResult;
import com.conghuhu.vo.UserVo;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author conghuhu
 * @since 2022-01-26
 */
public interface CardService extends IService<Card> {


    Card getByName(String cardname);


    JsonResult removeCardById(Long cardId);

    JsonResult createTag(CardParam cardParam);

    JsonResult updateTag(Card cardParam);

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

    /**
     * 移动卡片
     *
     * @param cardMoveParam
     * @return
     */
    JsonResult moveCard(CardMoveParam cardMoveParam);

    /**
     * 通过cardId获取其所有标签
     *
     * @param cardId
     * @return
     */
    List<Tag> getTagsByCardId(Long cardId);

    /**
     * 设置卡片的执行者
     *
     * @param executorParam
     * @param cardId
     * @return
     */
    JsonResult setExecutor(ExecutorParam executorParam, Long cardId);

    /**
     * 通过cardId获取其所有执行者
     *
     * @param cardId
     * @return
     */
    List<UserVo> getExecutorsByCardId(Long cardId);
}
