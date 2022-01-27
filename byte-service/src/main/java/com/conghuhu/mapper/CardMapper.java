package com.conghuhu.mapper;

import com.conghuhu.entity.Card;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author conghuhu
 * @since 2022-01-26
 */
@Repository
@Mapper
public interface CardMapper extends BaseMapper<Card> {

    Card getByCardName(String cardname);

}
