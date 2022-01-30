package com.conghuhu.mapper;

import com.conghuhu.entity.Card;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.conghuhu.entity.Tag;
import com.conghuhu.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author conghuhu
 * @since 2022-01-26
 */
@Repository
@Mapper
public interface CardMapper extends BaseMapper<Card> {

    Card getByCardName(String cardname);

    /**
     * 通过cardId获取其所有的tag标签
     *
     * @param cardId
     * @return
     */
    List<Tag> getTagsByCardId(Long cardId);

    /**
     * 通过cardId获取其所有执行者
     *
     * @param cardId
     * @return
     */
    List<UserVo> getExecutorsByCardId(Long cardId);
}
