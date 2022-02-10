package com.conghuhu.mapper;

import com.conghuhu.entity.Card;
import com.conghuhu.entity.CardUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author conghuhu
 * @since 2022-01-29
 */
@Repository
@Mapper
public interface CardUserMapper extends BaseMapper<CardUser> {

    /**
     * 根据项目id和userid删除cardUser
     * @param productId
     * @param userId
     * @return
     */
    Integer deleteCardUserByProductId(@Param("productId") Long productId,@Param("userId") Long userId);
}
