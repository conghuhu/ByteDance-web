package com.conghuhu.service;

import com.conghuhu.entity.Card;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author conghuhu
 * @since 2022-01-26
 */
public interface CardService extends IService<Card> {

    String addCard(Card card);

    Card getByName(String cardname);

}
